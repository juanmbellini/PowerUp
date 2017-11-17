require 'json'
require 'date'

# Escape quotes Postgres-style (ie. ' => '') if str is not nil. Otherwise return nil.
def postgres_quote_escape(str)
  str ? "'#{str.gsub("'", "''")}'" : 'NULL'
end

data_file = File.open('data.sql', 'w')

#
# 1) Insert data
#

# 1.1) Insert keywords
puts "Processing keywords"
keywords = JSON.parse(File.open('keywords/keywords.json', 'r:UTF-8', &:read)) # Ensure file is read in UTF-8: https://stackoverflow.com/a/21329178/2333689
keywords.each do |entry|
  id, name = entry['id'], postgres_quote_escape(entry['name'])
  data_file.puts "INSERT INTO keywords (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

data_file.puts '-------------------------------------------------------------'

# 1.2) Insert companies
puts "Processing companies"
companies = JSON.parse(File.open('companies/companies.json', 'r:UTF-8', &:read))
companies.each do |entry|
  id, name = entry['id'], postgres_quote_escape(entry['name'])
  data_file.puts "INSERT INTO companies (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

data_file.puts '-------------------------------------------------------------'

# 1.3) Insert games
puts "Processing games"
games = JSON.parse(File.open('games/games.json', 'r:UTF-8', &:read))
games.each do |entry|
  id, name, summary = entry['id'], postgres_quote_escape(entry['name']), postgres_quote_escape(entry['summary'])
  rating = (entry['rating'] || 100) / 10
  # Epoch milliseconds => DateTime => Date => ISO8601 ("YYYY-MM-DD")
  release = entry['first_release_date'] ? "'#{DateTime.strptime("#{entry['first_release_date']/1000}", '%s').to_date.iso8601}'" : 'NULL'
  cover = entry['cover'] ? "'#{entry['cover']['cloudinary_id']}'" : 'NULL'
  data_file.puts "INSERT INTO games (id, name, summary, avg_score, release, cover_picture_cloudinary_id) " \
            "VALUES (#{id}, #{name}, #{summary}, #{rating}, #{release}, #{cover}) ON CONFLICT (id) DO UPDATE " \
            "SET name = #{name}, summary = #{summary}, avg_score = #{rating}, release = #{release}, cover_picture_cloudinary_id = #{cover};"
end

data_file.close
relations_file = File.open('relations.sql', 'w')

#
# 2) Insert relations
#

# 2.1) Game keywords
puts "Processing game keywords"
keywords.each do |entry|
  (entry['games'] || []).each do |game_id|
    relations_file.puts "INSERT INTO game_keywords (game_id, keyword_id) VALUES (#{game_id}, #{entry['id']}) ;--ON CONFLICT DO NOTHING;"
  end
end


relations_file.close
puts 'Done'
