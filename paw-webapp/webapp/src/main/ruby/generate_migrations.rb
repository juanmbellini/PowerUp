require 'json'
require 'date'

# Escape quotes Postgres-style (ie. ' => '') if str is not nil. Otherwise return 'NULL'.
def postgres_string(str)
  str ? "'#{str.gsub("'", "''")}'" : 'NULL'
end

# Convert epoch timestamps (in milliseconds) to ISO-8601 date (ie. YYYY-MM-DD). If nil, return 'NULL'
def postgres_date(timestamp)
  timestamp ? "'#{DateTime.strptime("#{timestamp/1000}", '%s').to_date.iso8601}'" : 'NULL'
end

data_file = File.open('data.sql', 'w')

#
# 1) Insert data
#

# 1.1) Insert keywords
puts "Processing keywords"
keywords = JSON.parse(File.open('keywords/keywords.json', 'r:UTF-8', &:read)) # Ensure file is read as UTF-8: https://stackoverflow.com/a/21329178/2333689
keywords.each do |entry|
  id, name = entry['id'], postgres_string(entry['name'])
  data_file.puts "INSERT INTO keywords (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

data_file.puts '-------------------------------------------------------------'

# 1.2) Insert companies
puts "Processing companies"
companies = JSON.parse(File.open('companies/companies.json', 'r:UTF-8', &:read))
companies.each do |entry|
  id, name = entry['id'], postgres_string(entry['name'])
  data_file.puts "INSERT INTO companies (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

data_file.puts '-------------------------------------------------------------'

# 1.2) Insert platforms
puts "Processing platforms"
platforms = JSON.parse(File.open('platforms/platforms.json', 'r:UTF-8', &:read))
platforms.each do |entry|
  id, name = entry['id'], postgres_string(entry['name'])
  data_file.puts "INSERT INTO platforms (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

data_file.puts '-------------------------------------------------------------'

# 1.4) Insert games
puts "Processing games"
games = JSON.parse(File.open('games/games.json', 'r:UTF-8', &:read))
games.each do |entry|
  id, name, summary = entry['id'], postgres_string(entry['name']), postgres_string(entry['summary'])
  rating = (entry['rating'] || 0) / 10
  release = postgres_date(entry['first_release_date'])
  cover = entry['cover'] ? postgres_string(entry['cover']['cloudinary_id']) : 'NULL'

  data_file.puts "INSERT INTO games (id, name, summary, avg_score, release, cover_picture_cloudinary_id) " \
            "VALUES (#{id}, #{name}, #{summary}, #{rating}, #{release}, #{cover}) " \
            "ON CONFLICT (id) DO UPDATE SET name = #{name}, summary = #{summary}, avg_score = #{rating}, " \
            "release = #{release}, cover_picture_cloudinary_id = #{cover};"
end

data_file.close
relations_file = File.open('relations.sql', 'w')

#
# 2) Insert relations
#

# 2.0) Keep track of existing games. Some relations point to nonexistent games. We will skip those.
game_ids = Hash.new(false)
games.each { |game| game_ids[game['id']] = true }
# Idem platforms.
platform_ids = Hash.new(false)
platforms.each { |platform| platform_ids[platform['id']] = true }

# 2.1) Game developers
# NOTE: Developers and publishers could be handled in the same loop, but I prefer doing 1 table at a time. Same for other relations.
puts "Processing game developers"
companies.each do |company|
  (company['developed'] || []).
    # Select only those for games that exist
    select { |game_id| game_ids[game_id] }.
    each do |game_id|
      relations_file.puts "INSERT INTO game_developers (game_id, developer_id) VALUES (#{game_id}, #{company['id']}) " \
                          "ON CONFLICT (game_id, developer_id) DO NOTHING;"
  end
end

relations_file.puts '-------------------------------------------------------------'

# 2.2) Game genres
puts "Processing game genres"
games.each do |game|
  (game['genres'] || []).each do |genre|
    relations_file.puts "INSERT INTO game_genres (game_id, genre_id) " \
                        "VALUES (#{game['id']}, #{genre}) " \
                        "ON CONFLICT (game_id, genre_id) DO NOTHING;"
  end
end

relations_file.puts '-------------------------------------------------------------'

# 2.3) Game keywords
puts "Processing game keywords"
keywords.each do |keyword|
  (keyword['games'] || []).
    select { |game_id| game_ids[game_id] }.
    each do |game_id|
      relations_file.puts "INSERT INTO game_keywords (game_id, keyword_id) VALUES (#{game_id}, #{keyword['id']}) " \
                          "ON CONFLICT (game_id, keyword_id) DO NOTHING;"
    end
end

relations_file.puts '-------------------------------------------------------------'

# 2.4) Game pictures
puts "Processing game pictures"
games.each do |game|
    (game['screenshots'] || []).each do |screenshot|
      relations_file.puts "INSERT INTO game_pictures (game_id, cloudinary_id, width, height) " \
                          "VALUES (#{game['id']}, #{postgres_string(screenshot['cloudinary_id'])}, #{screenshot['width']}, #{screenshot['height']}) " \
                          "ON CONFLICT (cloudinary_id) DO NOTHING;"
    end
end

relations_file.puts '-------------------------------------------------------------'

# 2.5) Game platforms
puts "Processing game platforms"
games.each do |game|
    (game['release_dates'] || []).
      select do |release|
        release['date'] && release['platform'] && platform_ids[release['platform']]
      end.
      each do |release|
      relations_file.puts "INSERT INTO game_platforms (game_id, platform_id, release_date) " \
                          "VALUES (#{game['id']}, #{release['platform']}, #{postgres_date(release['date'])}) " \
                          "ON CONFLICT (game_id, platform_id, release_date) DO NOTHING;"
    end
end

relations_file.puts '-------------------------------------------------------------'

# 2.6) Game publishers
puts "Processing game publishers"
companies.each do |company|
  (company['published'] || []).
    select { |game_id| game_ids[game_id] }.
    each do |game_id|
      relations_file.puts "INSERT INTO game_publishers (game_id, publisher_id) VALUES (#{game_id}, #{company['id']}) " \
                          "ON CONFLICT (game_id, publisher_id) DO NOTHING;"
  end
end

relations_file.puts '-------------------------------------------------------------'

# 2.7) Game videos
puts "Processing game videos"
games.
  each do |game|
    (game['videos'] || []).
      reject { |video| !video['name']}. # There's a single video with no name, and we have a NOT NULL constraint for that
      each do |video|
        relations_file.puts "INSERT INTO game_videos (game_id, video_id, name) " \
                              "VALUES (#{game['id']}, #{postgres_string(video['video_id'])}, #{postgres_string(video['name'])}) " \
                              "ON CONFLICT (game_id, video_id) DO NOTHING;"
    end
end

relations_file.close
puts 'Done'
puts 'Generated "data.sql" and "relations.sql"; rename them, join them or move them as appropriate.'
