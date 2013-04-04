package org.morningcoffee.powerteam.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class Main extends AbstractHandler {
	
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
			
			page.println("<h1>Powerteam Server</h1>");
			page.println("<table border='1' cellspacing='0' cellpadding='4'>");
			page.println("<tr><td> </td><td><b>USER</b></td><td><b>COMMIT</b></td><td><b>PUSH TIME</b></td>" +
					"<td><b>TEST TIME</b></td><td><b>TEST RESULT</b></td></tr>");
			
			List<HashMap<String, String>> tableData = dbl.getLogs();
			DateFormat fullDate = new SimpleDateFormat("dd.MM.yyyy");
			DateFormat shortDate = new SimpleDateFormat("HH:mm:ss");
			
			long tempDate = Long.MAX_VALUE;
			for(int i = 0; i < tableData.size(); i++) {
				HashMap<String, String> map = tableData.get(i);
				
				if(tempDate - Long.parseLong(map.get("push_time"), 10) >= 24 * 60 * 60 * 1000) {
					page.println("<tr>");
					page.print("<td colspan='6'>");
					page.print("<b>" + fullDate.format(new Date(Long.parseLong(map.get("push_time"), 10))) + "</b>");
					page.print("</td>");
					page.println("</tr>");
					
					tempDate = Long.parseLong(map.get("push_time"), 10);
				}
				
				page.println("<tr>");
				
				if(map.get("test_result") == null || map.get("test_result").equals("failed"))
					page.print("<td><img src='/images/thumbsdown.jpg'/></td>");
				else page.print("<td><img src='/images/thumbsup.jpg'/></td>");
				
				page.print("<td>");
				page.print(map.get("user_name"));
				page.print("</td>");
				page.print("<td>");
				page.print(map.get("push_hash"));
				page.print("</td>");
				page.print("<td>");
				page.print(shortDate.format(new Date(Long.parseLong(map.get("push_time"), 10))));
				page.print("</td>");
				page.print("<td>");
				if(map.get("test_time") != null)
					page.print(shortDate.format(new Date(Long.parseLong(map.get("test_time"), 10))));
				else page.print(" - ");
				page.print("</td>");
				page.print("<td>");
				if(map.get("test_result") == null)
					page.print(" - ");
				else if(map.get("test_result").equals("failed"))
					page.print("<span style='color: #CD2626'>" + map.get("test_result") + "</span>");
				else page.print("<span style='color: green'>" + map.get("test_result") + "</span>");
				page.print("</td>");
				
				page.println("</tr>");
			}
			page.println("</table>");

		} else {
			baseRequest.setHandled(true);

			System.out.println(data);
			dbl.addLog(data);
			
			// rl.closeConnection();
		}
	}

	public static void main(String[] args) throws Exception {
		dbl = new DBLogger();
		
		Server server = new Server(8080);
		
        ResourceHandler resHandler = new ResourceHandler();
        resHandler.setResourceBase(System.getProperty("user.dir") + "/res");
        ContextHandler ctx = new ContextHandler("/images");
        ctx.setHandler(resHandler);
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { ctx, new Main() });
        
        server.setHandler(handlers);
        
		server.start();
		server.join();
	}
}