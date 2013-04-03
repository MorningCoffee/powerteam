require 'net/http'
require 'uri'
require 'json'

#ERROR_UNKNOWN_HOST = 1

class Client	

	def post_json_string		
		
		type='client' 
		user=ENV['USER']
		log_r=ENV['LOG']
		#log_user=ENV['LOG_USER']
		#log_host=ENV['LOG_HOST']
		#log_repo_path=ENV['LOG_GIT_REPO']

		#puts "USER: " + log_user 
		#puts "HOST: " + log_host
		#puts "LOCAL REPO: " + log_repo_path			
		
		log_r.each_line do |line|			
			if line.include? "update by push"
				#puts line
				hash=line[0..6]
				date=line[36..59]			
				json_string = {
			      		"type"=>type, 
			      		"user_name"=>user,
					"hash"=>hash,
					"date"=>date
			     	}.to_json
				#puts json_string

				#http://localhost:8080/
				log_host='http://localhost:8080/'

				uri = URI(log_host)
				res = Net::HTTP.post_form(uri, 'data' => json_string)
				
				puts "\n"

				begin				
					Net::HTTPSuccess
					#puts "Completed successfully"
					exit 0
				rescue
					puts "ERROR!!! Finish ruby: 1"
					exit 1
				end

				puts "\n"
			end
		end						
	end

end

client = Client.new
client.post_json_string


