package org.morningcoffee.powerteam.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

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
			
			dbl.getLogs();
		} else {
			baseRequest.setHandled(true);

			//System.out.println(data);
			
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