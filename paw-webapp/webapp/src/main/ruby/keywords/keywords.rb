require 'json'
require_relative '../query'

game_ids = JSON.parse(File.read('keyword_ids.json')).map { |entry| entry['id'] }

big_result = []
i = 0
game_ids.each_slice(500) do |slice|
  q = Query.new("#{Query::API_URL}/keywords/#{slice.join(',')}", 'user-key' => Query::KEY, 'Accept' => 'application/json')
  puts "Parsing keywords ##{i}-#{i += 500}"
  big_result += JSON.parse(q.fire.read)
end

puts "Done, writing result to file"
File.open('keywords.json', 'w') do |f|
  f.write(big_result.to_json)
end
