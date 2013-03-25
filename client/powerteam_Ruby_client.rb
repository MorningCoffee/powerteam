<<<<<<< HEAD
require 'net/http'

class Client
	def print_log
		line_num=0
		text=File.open('log.txt').read
		text.gsub!(/\r\n?/, "\n")
		text.each_line do |line|
	  	print "#{line_num += 1} #{line}"
		end
	end

	def post
		uri = URI('http://httpbin.org/post')
		res = Net::HTTP.post_form(uri, 'q' => 'ruby', 'max' => '50')
		puts res.body
	end
=======
line_num=0
text=File.open('log.txt').read
text.gsub!(/\r\n?/, "\n") #review> this replacement won't work - see documentation for gsub! method
text.each_line do |line|
  print "#{line_num += 1} #{line}"
>>>>>>> 2e8073542809759c9e7ef7d0568ccbbb4cb39602
end

client = Client.new
client.print_log
client.post




