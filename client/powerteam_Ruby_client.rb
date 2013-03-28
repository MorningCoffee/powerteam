require 'net/http'
require 'json'

class Client

	def post_json_string		
		type='client' 
		user=ENV['USER']
		log_r=ENV['LOG']
		hash=log_r[0..6]
		date=log_r[36..59]
	
		log_r.each_line do |line|			
			if line.include? "update by push"
				puts line
				json_string = {
			      		"type"=>type, 
			      		"user"=>user,
					"hash"=>hash,
					"date"=>date
			     	}.to_json
				puts "\n"
				puts json_string
				puts "\n"

				uri = URI('http://httpbin.org/post')
				res = Net::HTTP.post_form(uri, json_string)
		
				case res
				when Net::HTTPSuccess, Net::HTTPRedirection
  					puts "NO ERRORS!"
				else
  					puts "ERROR!"
				end
			end
		end						
	end

end

client = Client.new
client.post_json_string
