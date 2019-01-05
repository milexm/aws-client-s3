package com.acloudysky.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.amazonaws.regions.Regions;


/***
* Defines utility methods and variables to support the application operations
* such as menu creation, regions list initialization and so on.
* @author Michael Miele
*
*/
public class Utility implements IUtility {
	
	/***
	 * Gets the application menu.
	 * @return List containing the menu.
	 */
	public static ArrayList<String> getMenuEntries() {
			return menuEntries;
	}
		
	
	/***
	 * Displays the available regions. 
	 */
	public static void displayRegions() {
		Set<?> set = s3Regions.entrySet();
	    Iterator<?> iterator = set.iterator();
		while(iterator.hasNext()) {
	         Map.Entry<?, ?> mentry = (Map.Entry<?,?>)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
		} 
	}
	
	/***
	 * Gets the region related to the specified key.
	 * @param key The key representing the region to look for. For example, us-west-2.
	 * @return The enumerated value representing the desired region. 
	 */
	public static Regions getRegion(String key) {
		Regions currentRegion = null;
		Set<?> set = s3Regions.entrySet();
	    Iterator<?> iterator = set.iterator();
		while(iterator.hasNext()) {
	       Map.Entry<?, ?> mentry = (Map.Entry<?,?>)iterator.next();
	       
	       // Test
	       // System.out.println(String.format("Key: %s Value: %s", mentry.getKey(), mentry.getValue()));
	       
	       // Check if the key exists in the list.
	       int keyExist = key.trim().toLowerCase().compareTo(mentry.getKey().toString());
	       
	       if (keyExist == 0) {
	    	   // Assign the selected region. 
	    	   currentRegion = (Regions) mentry.getValue();
	    	   break;
	       }	 
		} 
		
		if (currentRegion == null)
			System.out.println(String.format("Selected region %s not allowed!", key.trim().toLowerCase()));
		return currentRegion;
	}
	
	 /***
	  * Displays the menu.
	  * @param entry The array containing the menu entries. 
	  */
	 public static void displayMenu(ArrayList<String> entry) {
		
		// Display menu header.
		System.out.println(dividerLine("*", DIVIDER_LENGTH));
		
		// Display menu entries.
	 	Iterator<String> i = entry.iterator();
	 	while (i.hasNext()) {
	 		System.out.println(i.next());
	 	}	
	 	
	 	// Display menu footer.
	 	System.out.println(dividerLine("*", DIVIDER_LENGTH));
	 }
	 
	 /***
	  * Displays welcome message.
	  * @param message The message to display.
	  */
	 public static void displayWelcomeMessage(String message)
	 {
	     System.out.println(dividerLine("*", DIVIDER_LENGTH));
	     String welcome = "Welcome to " + message; 
	     System.out.println(headerLine(welcome, DIVIDER_LENGTH));
	     System.out.println(dividerLine("*", DIVIDER_LENGTH));
	 }
	
	 /***
	  * Displays good bye message.
	  * @param message The message to display.
	  */
	 public static void displayGoodbyeMessage(String message)
	 {
		 headerLine(message, DIVIDER_LENGTH);
	     System.out.println(dividerLine("*", DIVIDER_LENGTH));
	     String bye = "Thank you for using " + message; 
	     System.out.println(headerLine(bye, DIVIDER_LENGTH));
	     System.out.println(dividerLine("*", DIVIDER_LENGTH));
	 }
	 
	 /***
	  * Gets the name of the OS and the user home directory.
	  * @return The array containing the OS name and the user home directory.
	  */
	 public static ArrayList<String> getEnvironment() {
		 return environment;
	 }
	 
	 
	 /***
	  * Gets the specified resource file
	  * @param fileName The name of the resource file.
	  * @return The resource file.
	  * <b>Note</b>. The file should exist in the project resources folder. 
	  */
	 public static File getResourceFile(String fileName){
		 
		 File resourceFile = null;
		 
		 URL resource = Utility.class.getResource("/" + fileName);
		 
		 // Test
		 // System.out.println(String.format("File name is: %s", fileName));
		 // System.out.println(String.format("URL is: %s", resource.toString()));
		 
		 try {
			resourceFile = Paths.get(resource.toURI()).toFile();
		 } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }	
		 
		 return resourceFile;
		 
	 }
	 
	 /***
      * Displays the contents of the specified input stream as text.
      * @param input The input stream to display as text.
      * @throws IOException Error encountered while handling the stream.
      */
    public static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }
	 
	 /*************************
	  ** Internal utilities. **
	  *************************/
	 
	 /***
	  * Creates the header to display.
	  * @param headerText The text to display in the header.
	  * @param length The length of the header.  
	  * @return Formatted header line.
	  */
	 private static String headerLine(String headerText, int length)
	 {
	     String header = "";
	     header = header.concat("***");
	     int blankSpaces = (length - (header.length() + headerText.length()))/2;
	     
	     for(int i = 2; i < blankSpaces; i++)
	     	header = header.concat(" ");
	     header = header.concat(headerText);
	     for(int i = header.length(); i < length - 3; i++)
	     	header = header.concat(" ");
	     header = header.concat("***");
	     return header;
	 }
	 
	 /***
	  * Creates the divider line.
	  * @param c The character to use to create the divider line.
	  * @param length The length of the divider line.
	  * @return Formatted divider line.
	  */
	 private static String dividerLine(String c, int length)
	 {
	     String divider = "";
	     for(int i = 0; i < length; i++)
	         divider = divider.concat(c);
	
	     return divider;
	 }
}

