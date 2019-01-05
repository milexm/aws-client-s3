package com.acloudysky.s3;


import java.io.BufferedReader;
import java.io.IOException;

import com.acloudysky.s3.Utility;


/*** 
 * Displays a selection menu for the user. Processes the  user's input and calls the proper 
 * method based on the user's selection. 
 * Each method calls the related S3 REST API.
 * <p>
 * For more information, see:
 * <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/Welcome.html" target="_blank">AWS S3 API</a>.
 * </p>
 * @author Michael Miele.
 *
 */
public class SimpleUI extends UserInterface {
	

	private String bucketName, keyName;
	
	/**
	 * Instantiates SimpleUI class along with its superclass.
	 */
	SimpleUI() {
		super();
		// Display menu.
		Utility.displayMenu(Utility.getMenuEntries());
		
	}
	
	/*
	 * Reads user input.
	 */
	private static String readUserInput(String msg) {
		
		// Open standard input.
		BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));

		String selection = null;
		
		//  Read the selection from the command-line; need to use try/catch with the
		//  readLine() method
		try {
			if (msg == null)
				System.out.print("\n>>> ");
			else
				System.out.print("\n" + msg);
			selection = br.readLine();
		} catch (IOException e) {
			System.out.println("IO error trying to read your input!");
			System.out.println(String.format("%s", e.getMessage()));
			System.exit(1);
		}
		
		return selection;

	}
	
	/*
	 * Executes the selected operation.
	 */
	private void performOperation(String operation) {
	
		// Select operation to perform.
		switch(operation) {
		
			case "cb": {
			
				try{
					// Create a bucket.
					do {
						System.out.println(String.format("%s", "The bucket name must follow the format: chowx-i-92aea747.d.eanitea.com"));
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
					} while(bucketName.isEmpty());
					BucketOperations.createBucket(bucketName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "lb": {
				try{
					// List the buckets contained in the account.
					BucketOperations.listBuckets();
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "db": {
				try{
					// Delete a bucket.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
					}while(bucketName.isEmpty());	
					BucketOperations.deleteBucket(bucketName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "uo": {
				try{
					// Upload an object in the specified bucket.
					// Note that the object to upload ia a text file that must already
					// exist in the resources folder. 
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Object (key) name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					ObjectOperations.uploadObject(bucketName, keyName, "sanmartino.txt");
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
				
			case "do": {
				try{
					// Download an object.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Object (key) name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					ObjectOperations.downloadObject(bucketName, keyName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "lo": {
				try{
					// List objects contained in the specified bucket.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
					}while(bucketName.isEmpty());
					ObjectOperations.listObject(bucketName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "cf": {
				try{
					// Get CloudFront signed URL.
					do {
						// bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Object (key) name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					BucketOperations.getPresignedUrls(keyName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
			
			case "xo": {
				try{
					// Delete an object.
					do {
						bucketName = readUserInput("Bucket name: ").toLowerCase();	
						keyName = readUserInput("Object (key) name: ").toLowerCase();	
					}while(bucketName.isEmpty() || keyName.isEmpty());
					ObjectOperations.deleteObject(bucketName, keyName);
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}
				
			
			default: {
				System.out.println(String.format("%s is not allowed", operation));
				break;
			}
		}
				

	}
	
	
	/***
	 * Gets user selection and calls the related method.
	 * Loops indefinitely until the user exits the application.
	 */
	public void processUserInput() {
		
		while (true) {
			
			// Get user input.
			String userSelection = readUserInput(null).toLowerCase();	
			// Normalize user's input.
			String normalizedUserSelection = userSelection.trim().toLowerCase();
			

			try{
				// Exit the application.
				if ("x".equals(normalizedUserSelection)){
					break;
				}
				else
					if ("m".equals(normalizedUserSelection)) {
						// Display menu
						Utility.displayMenu(Utility.getMenuEntries());
						continue;
					}
				
			}
			catch (Exception e){
				// System.out.println(e.toString());
				System.out.println(String.format("Input %s is not allowed%n", userSelection));
				continue;
			}
			
			performOperation(normalizedUserSelection);
		}
		
	}
	
}
