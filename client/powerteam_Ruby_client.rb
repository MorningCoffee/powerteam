1.times { print "hello\n" }

line_num=0
text=File.open('log.txt').read
text.gsub!(/\r\n?/, "\n")
text.each_line do |line|
  print "#{line_num += 1} #{line}"
end
