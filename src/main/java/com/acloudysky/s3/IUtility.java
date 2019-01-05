package com.acloudysky.s3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.amazonaws.regions.Regions;

/**
 * Defines fields and methods to implement the Utility class.
 * @see Utility
 * @author Michael Miele
 *
 */
public interface IUtility {
	
	// The divider length used in displaying menu and other.
	int DIVIDER_LENGTH = 66;
		
	// OS specific new line.
	String newline = System.getProperty("line.separator");
	
	
	/**
	 * Environment parameters such as OS name, user's home directory. 
	 */
	ArrayList<String> environment = new  ArrayList<String>(
			Arrays.asList(
					System.getProperty("os.name"), 		// OS name.
					System.getProperty("user.home")		// User home directory.
			)
	);
	
	
	/**
	 * Application menu entries.
	 */
	ArrayList<String> menuEntries = new  ArrayList<String>(
			Arrays.asList(
							"cb - Create bucket", 
							"lb - List buckets",
							"db - Delete bucket",
							"uo - Upload object",
							"do - Download object",
							"lo - List objects",
							"cf - Get CF Url",
							"xo - Delete object",
							"m  - Display menu",
							"x  - Quit the application"
						)
	);
	
	/**
	 * AWS S3 US regions  
	 * <b>Note</b>: Only US regions are used, for simplicity.
	 */
	HashMap<String, Enum<Regions>> s3Regions = new HashMap<String, Enum<Regions>>()
	{ 
		// Avoid compiler warning. 
		private static final long serialVersionUID = 1L;
		// Initialize the ec2Regions. 
		{
			put("us-east-1", Regions.US_EAST_1); // US East (N. Virginia) 
			put("us-west-1", Regions.US_WEST_1); // US West (N. California)
			put("us-west-2", Regions.US_WEST_2); // US West (Oregon)	
		}
	};
	
}


