require 'sinatra'
require 'mysql'
require 'json'


get '/' do
	db = DBLogger.new

	page = "<h1 align='center'>Powerteam Server</h1>" +
			"<table border='1' cellspacing='0' cellpadding='4' align='center'>" +
			"<tr><td> </td><td><b>USER</b></td><td><b>COMMIT</b></td><td><b>PUSH TIME</b></td>" +
			"<td><b>TEST TIME</b></td><td><b>TEST RESULT</b></td></tr>"

	db.createConnection()
	logs = db.getLogs()
	db.closeConnection()

	prevDate = 9223372036854775807

	logs.each do |log|
		tempDate = log["push_time"].to_i

		if prevDate - tempDate >= 24 * 60 * 60 * 1000
			date = DateTime.strptime((log["push_time"].to_i / 1000.0).to_s, '%s')
			page += "<tr><td colspan='6'><b> #{date.strftime("%d.%m.%Y")} </b></td></tr>"
					
			prevDate = log["push_time"].to_i
		end

		page += "<tr>"

		if log["test_result"] == nil || log["test_result"] == "failed"
			page += "<td> <img src='/thumbsdown.jpg'/> </td>"
		else 
			page += "<td> <img src='/thumbsup.jpg'/> </td>"
		end

		page += "<td>#{log['user_name']}</td>"
		page += "<td>#{log['push_hash']}</td>"
		date = DateTime.strptime((log["push_time"].to_i / 1000.0).to_s, '%s')
		page += "<td>" + date.strftime("%H:%M:%S")  + "</td>"
		
		if(log["test_time"] != nil)
			date = DateTime.strptime((log["test_time"].to_i / 1000.0).to_s, '%s')
			page += "<td>" + date.strftime("%H:%M:%S") + "</td>"
		else 
			page += "<td> - </td>"
		end

		if log["test_result"] == nil
			page += "<td> - </td>"
		else 
			if log["test_result"] == "failed"
				page += "<td> <span style='color: #CD2626'>" + log["test_result"] + "</span> </td>"
			else 
				page += "<td> <span style='color: green'>" + log["test_result"] + "</span> </td>"
			end
		end

		page += "</tr>"
	end 

	page
end

post '/new' do
	db = DBLogger.new

	db.createConnection()
	db.addLog(params[:data])
	db.closeConnection()
end



DBLogger = Class.new do
	
	@con = nil

	def createConnection()
		@con = Mysql.new('localhost', 'root', 'root', 'powerteam')
	end

	def closeConnection()
		@con.close
	end

	def addLog(data)
		puts data
		parsed = JSON.parse(data)

		sqlReq = ""
		if parsed["type"] == "plugin" 
			sqlReq = "INSERT INTO powerteam.pluginlogs (start_time, end_time, test_result, user_id) values " +
					"(#{parsed['start_time']}, #{parsed['end_time']}, '#{parsed['test_result']}', " +
					"(SELECT user_id FROM powerteam.users WHERE user_name = '#{parsed['user_name']}'))"
		else
			date = DateTime.strptime(parsed["date"], "%a %b %d %H:%M:%S %Y").to_time.getgm.to_i * 1000
			sqlReq = "INSERT INTO powerteam.clientlogs (hash, push_time, user_id) values ('#{parsed['hash']}', " +
					"#{date}, (SELECT user_id FROM powerteam.users WHERE user_name = '#{parsed['user_name']}'))"
		end

		begin
			@con.query(sqlReq)
		rescue Mysql::Error => e
			puts e.errno
			puts e.error
		end
	end

	def getLogs()
		@result = Array.new

		request = "SELECT u.user_name, c.hash, c.push_time, p.end_time, p.test_result " +
				"FROM powerteam.clientlogs c " +
				"LEFT OUTER JOIN powerteam.pluginlogs p ON p.end_time = " +
				"(SELECT p2.end_time " +
				"FROM powerteam.pluginlogs p2 " +
				"WHERE p2.end_time < c.push_time " +
				"AND c.push_time - p2.end_time <= 5 * 60 * 1000 " +
				"ORDER BY p2.end_time " +
				"DESC LIMIT 1) " + 
				"JOIN powerteam.users u ON c.user_id = u.user_id " +
				"ORDER BY c.push_time DESC"
		puts request
		begin
			rs = @con.query(request)

			rs.each_hash do |row|
				map = {	"user_name" => row['user_name'],
						"push_hash" => row['hash'],
						"push_time" => row['push_time'],
						"test_time" => row['end_time'],
						"test_result" => row['test_result']
					}
				@result.push(map)
			end
		rescue Mysql::Error => e
			puts e.errno
			puts e.error
		end

		return @result
	end
end