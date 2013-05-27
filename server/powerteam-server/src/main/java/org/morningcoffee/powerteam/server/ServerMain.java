package org.morningcoffee.powerteam.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.mortbay.jetty.*;
import org.mortbay.jetty.handler.*;

public class ServerMain extends AbstractHandler {
	
	private static DBLogger dbl;
	
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		dbl.createConnection();
		String data = request.getParameter("data");

		if (data == null) {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			PrintWriter page = response.getWriter();
			
			page.println("<h1 align='center'>Powerteam Server</h1>");
			page.println("<table border='1' cellspacing='0' cellpadding='4' align='center'>");
			page.println("<tr><td> </td><td><b>USER</b></td><td><b>COMMIT</b></td><td><b>PUSH TIME</b></td>" +
					"<td><b>TEST TIME</b></td><td><b>TEST RESULT</b></td></tr>");
			
			List<HashMap<String, String>> tableData = dbl.getLogs();
			DateFormat fullDate = new SimpleDateFormat("dd.MM.yyyy");
			DateFormat shortDate = new SimpleDateFormat("HH:mm:ss");
			
			long prevDate = Long.MAX_VALUE;
			
			for(int i = 0; i < tableData.size(); i++) {
				HashMap<String, String> map = tableData.get(i);
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(map.get("push_time"), 10));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				long tempDate = cal.getTimeInMillis();
				
				if(prevDate - tempDate >= 24 * 60 * 60 * 1000) {
					page.println("<tr>");
					page.print("<td colspan='6'><b>");
					page.print(fullDate.format(new Date(Long.parseLong(map.get("push_time"), 10))));
					page.print("</b></td>");
					page.println("</tr>");
					
					prevDate = Long.parseLong(map.get("push_time"), 10);
				}
				
				page.println("<tr>");
				
				if(map.get("test_result") == null || map.get("test_result").equals("failed"))
					page.print("<td> <img src='/images/thumbsdown.jpg'/> </td>");
				else page.print("<td> <img src='/images/thumbsup.jpg'/> </td>");
				
				page.print("<td>" + map.get("user_name") + "</td>");
				page.print("<td>" + map.get("push_hash") + "</td>");
				page.print("<td>" + shortDate.format(new Date(Long.parseLong(map.get("push_time"), 10))) + "</td>");
				if(map.get("test_time") != null)
					page.print("<td>" + shortDate.format(new Date(Long.parseLong(map.get("test_time"), 10))) + "</td>");
				else page.print("<td> - </td>");
				if(map.get("test_result") == null)
					page.print("<td> - </td>");
				else if(map.get("test_result").equals("failed"))
					page.print("<td> <span style='color: #CD2626'>" + map.get("test_result") + "</span> </td>");
				else page.print("<td> <span style='color: green'>" + map.get("test_result") + "</span> </td>");
				
				page.println("</tr>");
			}
			page.println("</table>");

		} else {
			baseRequest.setHandled(true);

			System.out.println(data);
			dbl.addLog(data);
		}
		
		dbl.closeConnection();
	}

	public static void main(String[] args) throws Exception {
		dbl = new DBLogger();
		
		Server server = new Server(8080);
		
        ResourceHandler resHandler = new ResourceHandler();
        File jarPath = new File(DBLogger.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        resHandler.setResourceBase(jarPath.getParent() + "/../webres");
        ContextHandler conth = new ContextHandler("/images");
        conth.setHandler(resHandler);
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { conth, new ServerMain() });
        
        server.setHandler(handlers);
        
		server.start();
		server.join();
	}

	@Override
	public void handle(String arg0, HttpServletRequest arg1,
			HttpServletResponse arg2, int arg3) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		
	}
}