require 'net/http'

class Client
	def print_log
		line_num=0
		text=File.open('log.txt').read
		text.each_line do |line|
	  	print "#{line_num += 1} #{line}"
		end
	end

	def post
		uri = URI('http://httpbin.org/post')
		res = Net::HTTP.post_form(uri, 'q' => 'ruby', 'max' => '50')
		puts res.body
		case res
		when Net::HTTPSuccess, Net::HTTPRedirection
  			puts "NO ERRORS!"	
		else
  			puts "ERROR!"
		end
	end
end

client = Client.new
client.print_log
client.post
