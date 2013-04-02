package org.morningcoffee.powerteam.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

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
			page.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"4\">");
			page.println("<tr><td><b>USER</b></td><td><b>COMMIT</b></td><td><b>PUSH TIME</b></td>" +
					"<td><b>TEST TIME</b></td><td><b>TEST RESULT</b></td></tr>");
			
			List<HashMap<String, String>> tableData = dbl.getLogs();
			for(int i = 0; i < tableData.size(); i++) {
				HashMap<String, String> map = tableData.get(i);
				
				page.println("<tr>");
				
				page.println("<td>");
				page.print(map.get("user_name"));
				page.println("</td>");
				page.println("<td>");
				page.print(map.get("push_hash"));
				page.println("</td>");
				page.println("<td>");
				page.print(map.get("push_time"));
				page.println("</td>");
				page.println("<td>");
				page.print(map.get("test_time"));
				page.println("</td>");
				page.println("<td>");
				page.print(map.get("test_result"));
				page.println("</td>");
				
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
		server.setHandler(new Main());

		server.start();
		server.join();
	}
}