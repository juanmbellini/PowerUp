require 'json'
require_relative '../query'

q = Query.new("#{Query::API_URL}/keywords/?limit=50&scroll=0", 'user-key' => Query::KEY, 'Accept' => 'application/json')

big_result = []
q.get_all do |page|
  parsed_page = JSON.parse(page)
  puts parsed_page.to_json
  big_result += parsed_page
end

puts "Done, writing result to file"
File.open('keyword_ids.json', 'w') do |f|
  f.write(big_result.to_json)
end
