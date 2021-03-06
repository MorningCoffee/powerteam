require 'net/http'
require 'uri'
require 'json'

ERROR_UNKNOWN_HOST = 1
ERROR_POST_REJECTED = 2
ERROR_NO_LOCAL_REPO = 3

#test data
#http://httpbin.org/get
#/home/ababichok/Documents/powerteam/

class Client	

	def post_json_string		
		
		type='client' 
		user=ENV['USER']
		log_r=ENV['LOG']
		log_host=ENV['LOG_HOST']
		log_repo=ENV['LOG_GIT_REPO']
	
		#Show user

		puts "USER:" + user 

		# Check HOST
		#begin
		#uri = URI(log_host)
		#res = Net::HTTP.get_response(uri)
		#if res.code == '200'
		#	puts "FIND HOST. SUCCESS!!!"
		#end
		#rescue
		#	puts ERROR_UNKNOWN_HOST
		#	exit 1
		#end

		#check REPO
		#find = false
		#Dir.foreach(log_repo) do |fname|
		#	if fname == ".git"
		#		puts "FIND REPO. SUCCESS!!!"
		#		find = true
		#		break
		#	end
		#end
		#if find == false
		#	puts ERROR_NO_LOCAL_REPO
		#	exit 3
		#end
		
		log_r.each_line do |line|			
			if line.include? "update by push"
				#hash=line[0..6]
				#date=line[36..59]
				hash, date = line.match(/^(.*) refs.*@{(.*)}.*$/i).captures			
				json_string = {
					"type"=>type, 
					"user_name"=>user,
					"hash"=>hash,
					"date"=>date
				}.to_json

				begin
					uri = URI(log_host)
					res = Net::HTTP.post_form(uri, 'data' => json_string)
					
					res = Net::HTTP.get_response(uri)

					if res.code == '200'
						puts "FIND HOST FOR POST. SUCCESS!!!"
					end	
					
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