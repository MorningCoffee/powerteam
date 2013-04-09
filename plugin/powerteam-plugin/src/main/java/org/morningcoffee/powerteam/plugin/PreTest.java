package org.morningcoffee.powerteam.plugin;

import java.util.Date;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;


@Mojo(name = "time")
public class PreTest extends AbstractMojo
{
	public static long time;
	
    public void execute() throws MojoExecutionException
    {
    	time = new Date().getTime();
    }
}