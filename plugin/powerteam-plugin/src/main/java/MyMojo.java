import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;


@Mojo( name = "report" )
public class MyMojo extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
    	File testResultDir = new File(System.getProperty("user.dir") + "/target/surefire-reports/");
    	File[] listOfFiles = testResultDir.listFiles();
    	
    	for (int i = 0; i < listOfFiles.length; i++) 
    	{
    		File testResult = listOfFiles[i];
    		
    		if (testResult.isFile() && testResult.getName().endsWith(".xml")) 
    		{
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        Document doc = null;
		        
				try {
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					doc = dBuilder.parse(testResult);
				} 
				catch (Exception e) { e.printStackTrace(); }
		        
				doc.getDocumentElement().normalize();
				NodeList nodes = doc.getElementsByTagName("testsuite");
		        
				for (int j = 0; j < nodes.getLength(); j++)
				{
					NamedNodeMap testAttributes = nodes.item(j).getAttributes();
					
					String testName = testAttributes.getNamedItem("name").getNodeValue();
					String testsCount = testAttributes.getNamedItem("tests").getNodeValue();
					String testsFailures = testAttributes.getNamedItem("failures").getNodeValue();
					String testsErrors = testAttributes.getNamedItem("errors").getNodeValue();
					String testsSkipped = testAttributes.getNamedItem("skipped").getNodeValue();
					String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
				
					try {
						String data = "Date=" + timeStamp + "&Name=" + testName + "&Count=" + testsCount + 
								"&Failures=" + testsFailures + "&Errors=" + testsErrors + "&Skipped=" + testsSkipped;
				 
				        URL url = new URL("http://httpbin.org/post");
				        URLConnection conn = url.openConnection();
				        conn.setDoOutput(true);
				        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				        wr.write(data);
				        wr.flush();
				        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				        
				        String line;
				        while ((line = rd.readLine()) != null)
				            System.out.println(line);

				        wr.close();
				        rd.close();
					} catch (Exception e) { e.printStackTrace(); }
				}
				
				testResult.delete();
    		}
    	}

    }
}