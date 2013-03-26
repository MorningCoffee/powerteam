require 'net/http'
require 'json'

class Client
	def print_log
		print ENV['LOG'] 
	end

	def post
		uri = URI('http://httpbin.org/post')
		res = Net::HTTP.post_form(uri, 'q' => 'ruby', 'max' => '50')
		#puts res.body
		
		case res
		when Net::HTTPSuccess, Net::HTTPRedirection
  			puts "NO ERRORS!"
			data='111'
			puts data
		else
  			puts "ERROR!"
		end
	end


	def json_string
		puts "\n\nJSON\n\n"
		log_r=ENV['LOG']
		user=ENV['USER']
		type='client' 	
		
		hash=log_r[0..7]
		date=log_r[36..59]

		puts type
		puts user
		puts hash		
		puts date

		puts "\n"
		

		#log_r.scan("\z")
		
		json_string = {
			      	"type"=>type, 
			      	"user"=>user,
				"hash"=>hash,
				"date"=>date,
			      }.to_json
		puts json_string						
		
	end
end

client = Client.new
client.print_log
#client.post
client.json_string
