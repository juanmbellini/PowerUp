require 'json'
require_relative '../query'

game_ids = JSON.parse(File.read('game_ids.json')).map { |entry| entry['id'] }

big_result = []
i = 0
game_ids.each_slice(500) do |slice|
  q = Query.new("#{Query::API_URL}/games/#{slice.join(',')}", 'user-key' => Query::KEY, 'Accept' => 'application/json')
  puts "Parsing games ##{i}-#{i += 500}"
  big_result += JSON.parse(q.fire.read)
end

puts "Done, writing result to file"
File.open('games.json', 'w') do |f|
  f.write(big_result.to_json)
end
