require_relative '../migrator'

MIGRATION_NAME = 'more_game_data'.freeze
migrator = Migrator.new(MIGRATION_NAME, 5e6)

# 2.0.1) Read data
puts 'Reading data...'
keywords = JSON.parse(File.open('../keywords/keywords.json', 'r:UTF-8', &:read)) # Ensure file is read as UTF-8: https://stackoverflow.com/a/21329178/2333689
companies = JSON.parse(File.open('../companies/companies.json', 'r:UTF-8', &:read))
platforms = JSON.parse(File.open('../platforms/platforms.json', 'r:UTF-8', &:read))
games = JSON.parse(File.open('../games/games.json', 'r:UTF-8', &:read))


# 2.0.2) Keep track of existing games. Some relations point to nonexistent games. We will skip those.
game_ids = Hash.new(false)
games.each { |game| game_ids[game['id']] = true }
# 2.0.3) Idem platforms.
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
        migrator.append "INSERT INTO game_developers (game_id, developer_id) VALUES (#{game_id}, #{company['id']}) " \
                              "ON CONFLICT (game_id, developer_id) DO NOTHING;"
  end
end

migrator.append '-------------------------------------------------------------'

# 2.2) Game genres
puts "Processing game genres"
games.each do |game|
  (game['genres'] || []).each do |genre|
    migrator.append "INSERT INTO game_genres (game_id, genre_id) VALUES (#{game['id']}, #{genre}) " \
                         "ON CONFLICT (game_id, genre_id) DO NOTHING;"
  end
end

migrator.append '-------------------------------------------------------------'

# 2.3) Game keywords
puts "Processing game keywords"
keywords.each do |keyword|
  (keyword['games'] || []).
      select { |game_id| game_ids[game_id] }.
      each do |game_id|
    migrator.append "INSERT INTO game_keywords (game_id, keyword_id) VALUES (#{game_id}, #{keyword['id']}) " \
                         "ON CONFLICT (game_id, keyword_id) DO NOTHING;"
  end
end

migrator.append '-------------------------------------------------------------'

# 2.4) Game pictures
puts "Processing game pictures"
games.each do |game|
  (game['screenshots'] || []).each do |screenshot|
    migrator.append "INSERT INTO game_pictures (game_id, cloudinary_id, width, height) " \
                          "VALUES (#{game['id']}, #{Migrator.postgres_string(screenshot['cloudinary_id'])}, #{screenshot['width']}, #{screenshot['height']}) " \
                          "ON CONFLICT (cloudinary_id) DO NOTHING;"
  end
end

migrator.append '-------------------------------------------------------------'

# 2.5) Game platforms
puts "Processing game platforms"
games.each do |game|
  (game['release_dates'] || []).
  select do |release|
    release['date'] && release['platform'] && platform_ids[release['platform']]
  end.
  each do |release|
    migrator.append "INSERT INTO game_platforms (game_id, platform_id, release_date) " \
                          "VALUES (#{game['id']}, #{release['platform']}, #{Migrator.postgres_date(release['date'])}) " \
                          "ON CONFLICT (game_id, platform_id, release_date) DO NOTHING;"
  end
end

migrator.append '-------------------------------------------------------------'

# 2.6) Game publishers
puts "Processing game publishers"
companies.each do |company|
  (company['published'] || []).
    select { |game_id| game_ids[game_id] }.
    each do |game_id|
      migrator.append "INSERT INTO game_publishers (game_id, publisher_id) VALUES (#{game_id}, #{company['id']}) " \
                          "ON CONFLICT (game_id, publisher_id) DO NOTHING;"
  end
end

migrator.append '-------------------------------------------------------------'

# 2.7) Game videos
puts "Processing game videos"
games.
  each do |game|
  (game['videos'] || []).
  reject { |video| !video['name']}. # There's a single video with no name, and we have a NOT NULL constraint for that
  each do |video|
    migrator.append "INSERT INTO game_videos (game_id, video_id, name) " \
                              "VALUES (#{game['id']}, #{Migrator.postgres_string(video['video_id'])}, #{Migrator.postgres_string(video['name'])}) " \
                              "ON CONFLICT (game_id, video_id) DO NOTHING;"
  end
end

num_files = migrator.finish
puts 'Done'
puts "Split '#{MIGRATION_NAME}' migration into #{num_files} file(s)"
