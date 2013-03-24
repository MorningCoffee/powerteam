line_num=0
text=File.open('log.txt').read
text.gsub!(/\r\n?/, "\n") #review> this replacement won't work - see documentation for gsub! method
text.each_line do |line|
  print "#{line_num += 1} #{line}"
end

#require 'net/http'

#Parse full page

#url = URI.parse('http://www.computerhope.com/starthtm.htm')
#req = Net::HTTP::Get.new(url.path)
#res = Net::HTTP.start(url.host, url.port) {|http|
  #http.request(req)
#}
#puts res.body

 require 'net/http'
 require 'uri'
    Net::HTTP.get_print URI.parse('http://www.computerhope.com/starthtm.htm')


#Parse only headers

#uri = URI('http://www.computerhope.com/starthtm.htm')
#res = Net::HTTP.get_response(uri)

#res['Set-Cookie']            
#res.get_fields('set-cookie') 
#res.to_hash['set-cookie']    
#puts "Headers: #{res.to_hash.inspect}"



#Send with closing

#s = TCPsocket.new('127.0.0.1',2000)
#s.send('Hell!!!',0)
#s.close



#Send without closing

#socket = TCPsocket.new('127.0.0.1',2000)
#socket.send('some message\000', 0)



