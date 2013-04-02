require 'net/http'
require 'uri'
require 'json'

class Client

def post_json_string	
type='client'
user=ENV['USER']
log_r=ENV['LOG']

puts log_r	

log_r.each_line do |line|	
if line.include? "update by push"
puts line
hash=line[0..6]
date=line[36..59]	
json_string = {
"type"=>type,
"user_name"=>user,
"hash"=>hash,
"date"=>date
}.to_json
puts json_string

uri = URI('http://localhost:8080/')
res = Net::HTTP.post_form(uri, 'data' => json_string)

case res
when Net::HTTPSuccess, Net::HTTPRedirection
   puts "NO ERRORS!"
else
   puts "ERROR!"
end
puts "\n"
end
end	
end

end

client = Client.new
client.post_json_string

at_exit do
  if $!.nil? || $!.is_a?(SystemExit) && $!.success?
    puts 'Finish ruby: 0'
  else
    code = $!.is_a?(SystemExit) ? $!.status : 1
    puts "failure with code #{code}"
  end
end