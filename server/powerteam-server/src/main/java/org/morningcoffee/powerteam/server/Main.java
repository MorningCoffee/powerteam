package org.morningcoffee.powerteam.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
 
public class Main extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        baseRequest.setHandled(true);
        
    	String data = request.getParameter("data");
        System.out.println(data);
        
        DBLogger rl = new DBLogger();
        rl.createConnection();
        rl.addLog(data);
        //rl.closeConnection();
        
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new Main());
 
        server.start();
        server.join();
    }
}