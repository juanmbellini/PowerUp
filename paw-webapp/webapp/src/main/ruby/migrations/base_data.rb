require_relative '../migrator'

MIGRATION_NAME = 'more_base_data'.freeze
migrator = Migrator.new(MIGRATION_NAME, 5e6)

# 1.1) Insert keywords
puts "Processing keywords"
keywords = JSON.parse(File.open('../keywords/keywords.json', 'r:UTF-8', &:read)) # Ensure file is read as UTF-8: https://stackoverflow.com/a/21329178/2333689
keywords.each do |entry|
  id, name = entry['id'], Migrator.postgres_string(entry['name'])
  migrator.append "INSERT INTO keywords (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

migrator.append '-------------------------------------------------------------'

# 1.2) Insert companies
puts "Processing companies"
companies = JSON.parse(File.open('../companies/companies.json', 'r:UTF-8', &:read))
companies.each do |entry|
  id, name = entry['id'], Migrator.postgres_string(entry['name'])
  migrator.append "INSERT INTO companies (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

migrator.append '-------------------------------------------------------------'

# 1.3) Insert platforms
puts "Processing platforms"
platforms = JSON.parse(File.open('../platforms/platforms.json', 'r:UTF-8', &:read))
platforms.each do |entry|
  id, name = entry['id'], Migrator.postgres_string(entry['name'])
  migrator.append "INSERT INTO platforms (id, name) VALUES (#{id}, #{name}) " \
                  "ON CONFLICT (id) DO UPDATE SET name = #{name};"
end

migrator.append '-------------------------------------------------------------'


# 1.4) Insert games
puts "Processing games"
games = JSON.parse(File.open('../games/games.json', 'r:UTF-8', &:read))
games.each do |entry|
  id, name, summary = entry['id'], Migrator.postgres_string(entry['name']), Migrator.postgres_string(entry['summary'])
  rating = (entry['rating'] || 0) / 10
  release = Migrator.postgres_date(entry['first_release_date'])
  cover = entry['cover'] ? Migrator.postgres_string(entry['cover']['cloudinary_id']) : 'NULL'

  migrator.append "INSERT INTO games (id, name, summary, avg_score, release, cover_picture_cloudinary_id) " \
            "VALUES (#{id}, #{name}, #{summary}, #{rating}, #{release}, #{cover}) " \
            "ON CONFLICT (id) DO UPDATE SET name = #{name}, summary = #{summary}, avg_score = #{rating}, " \
            "release = #{release}, cover_picture_cloudinary_id = #{cover};"
end

num_files = migrator.finish
puts 'Done'
puts "Split '#{MIGRATION_NAME}' migration into #{num_files} file(s)"
