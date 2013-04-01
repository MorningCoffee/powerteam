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
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String data = request.getParameter("data");

		if (data == null) {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			PrintWriter page = response.getWriter();
			page.println("<h1>Powerteam Server</h1>");
		} else {
			baseRequest.setHandled(true);

			System.out.println(data);
			DBLogger rl = new DBLogger();
			rl.createConnection();
			rl.addLog(data);
			// rl.closeConnection();
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		server.setHandler(new Main());

		server.start();
		server.join();
	}
}