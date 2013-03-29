package org.morningcoffee.powerteam.plugin;

import java.io.*;
import java.util.Properties;

public class GitInfo {
	
	private static String exec(String path, String command) {
		Runtime run = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = run.exec(new String[]{"/bin/bash", "-c", "cd " + path + "/..; " + command});
			pr.waitFor();
		} catch (Exception e) { 
			System.out.println("\nCannot execute bash command\n");
    		System.exit(-3);
		}
		
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));

		String commandOutput = "";
		try {
			String tempLine;
			while ((tempLine = buf.readLine()) != null) 
				commandOutput += tempLine;
		} catch (IOException e) { 
			System.out.println("\nCannot read bash output\n");
    		System.exit(-4);
		}
		
		return commandOutput;
	}
	
	public static String getName() {

    	Properties prop = new Properties();
    	String gitPath = "";
    	try {
    		prop.load(PostTest.class.getResourceAsStream("/config.properties"));
        	gitPath = prop.getProperty("gitpath");
 
    	} catch (IOException ex) {
    		System.out.println("\nCannot get repository path\n");
    		System.exit(-5);
        }
    	
    	return GitInfo.exec(gitPath, "git config user.name");
	}
}
