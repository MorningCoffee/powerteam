package morningcoffee.powerteam_server;

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
        
    	/*String testDate = request.getParameter("Date");
    	String testName = request.getParameter("Name");
    	String testCount = request.getParameter("Count");
    	String testFailed = request.getParameter("Failures");
    	String testErrors = request.getParameter("Errors");
    	String testSkipped = request.getParameter("Skipped");
    	
        System.out.println("Name: " + testName + "; Date: " + testDate + "; Count: " + testCount + 
        		"; Failed: " + testFailed+ "; Errors: " + testErrors + "; Skipped: " + testSkipped);*/
        
        String data = request.getParameter("data");
        System.out.println(data);
        
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new Main());
 
        server.start();
        server.join();
    }
}