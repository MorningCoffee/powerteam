require 'net/http'
require 'uri'
require 'json'

ERROR_UNKNOWN_HOST = 1
ERROR_POST_REJECTED = 2
ERROR_NO_LOCAL_REPO = 3

class Client	

	def post_json_string		
		
		type='client' 
		user=ENV['USER']
		log_r=ENV['LOG']
		#log_user=ENV['LOG_USER']
		log_host=ENV['LOG_HOST']
		log_repo=ENV['LOG_GIT_REPO']

#http://httpbin.org/get
#/home/ababichok/Documents/powerteam/
		
		# Check HOST
		begin
		uri = URI(log_host)
		res = Net::HTTP.get_response(uri)
		if res.code == '200'
			puts "FIND HOST. SUCCESS!!!"
		end
		rescue
			puts ERROR_UNKNOWN_HOST
			exit 1
		end

		#check REPO
		find = false
		Dir.foreach(log_repo) do |fname|
			if fname == ".git"
				puts "FIND REPO. SUCCESS!!!"
				find = true
				break
			end
		end
		if find == false
			puts ERROR_NO_LOCAL_REPO
			exit 3
		end
		
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
				#logrrrr_host='http://localhost:8080/'
				begin
					uri = URI(log_host)
					res = Net::HTTP.post_form(uri, 'data' => json_string)
					
					res = Net::HTTP.get_response(uri)

					if res.code == '200'
						puts "FIND HOST FOR POST. SUCCESS!!!"
					end

					#puts "\n"				
					
					Net::HTTPSuccess
					puts "Completed successfully"
					exit 0
				rescue
					puts ERROR_POST_REJECTED
					exit 2
				end

				puts "\n"
			end
		end						
	end

end

client = Client.new
client.post_json_string