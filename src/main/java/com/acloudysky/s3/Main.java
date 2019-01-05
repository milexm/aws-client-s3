package com.acloudysky.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.acloudysky.auth.AuthenticateAwsServiceClient;
import com.acloudysky.s3.Utility;


/***
 * Instantiates the S3 authenticated service client, initializes the operations and the UI classes.  
 * Before running the code, you need to set up your AWS security credentials. You can do this by creating a 
 * file named "credentials" at <i>~/.aws/</i> (<i>C:\Users\USER_NAME.aws\</i> for Windows users) and saving the following lines in 
 * the file:
 * <pre>
 *   [default]
 *   aws_access_key_id = your access key id
 *   aws_secret_access_key = your secret key
 * </pre>
 * For more information, see <a href="http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html" target="_blank">Providing AWS Credentials in the AWS SDK for Java</a> 
 * and <a href="https://console.aws.amazon.com/iam/home?#security_credential" target="_blank">Welcome to Identity and Access Management</a>.
 * <b>WARNING</b>: To avoid accidental leakage of your credentials, DO NOT keep the credentials file in your source directory.
 * @author Michael Miele
 *
 */
public class Main {

	private static AmazonS3  s3Client = null;
	
	// Selected S3 region. Enumerated value.
	private static Regions currentRegion = null;
	
	// Selected region. String value such as "us-west-2".
	private static String region = null;
	
	/***
	 * Instantiates the S3 client, initializes the operation classes. 
	 * Instantiates the SimpleUI class to display the selection menu and process the user's input. 
	 * @see SimpleUI#SimpleUI()
	 * @see BucketOperations#initBucketOperations(AmazonS3)
	 * @see ObjectOperations#initObjectOperations(AmazonS3)
	 * @param args; 
	 *  args[0] = region (key) for example, us-west-2
	 *  <p>Note. To generate the couple aws_access_key_id and the aws_secret_access_key to allow programmatic access 
	 *  to the S3 service, perfronm these steps:
	 *  <ol>
	 *  	<li>In your browser navigate to: http://aws.expedia.biz and login into AWS</li>
	 *  	<li>In the AWS dash-board, select IAM. </li>
	 *  	<li>In the left pane select Users.</li>
	 *  	<li>In the right pane, select the pdbauto user.</li>
	 *  	<li>Click on the Security credentials tab.</li>
	 *  	<li>Click the Create access key button.</li>
	 *  	<li>Download the .csv file containing the couple aws_access_key_id and the aws_secret_access_key.</li>
	 *  	<li>Copy the key in the .aws/Credentials file. </li>
	 *  </ol>
	 */
	public static void main(String[] args) {
		
		
		// Display greeting message.
		Utility.displayWelcomeMessage("AWS S3");
				
		// Read input parameters.
		try {
				region = args[0];
				// System.out.println(region);
		}
		catch (Exception e) {
			System.out.println("IO error trying to read application input! Assigning default values.");
			// Assign default values if none are passed.
			if (args.length==0) {
				region = "us-west-2";
			}
			else {
				System.out.println("IO error trying to read application input!");
				System.exit(1); 
			}
		}
		
		
		try {
			
				// Instantiate AwsClientAuthencation class.
//				AwsClientAuthencation clientAuth = new AwsClientAuthencation();
//			
//				// Obtain authenticated S3 client.
//				s3Client = (AmazonS3) clientAuth.getAuthenticatedClient("s3");
//				
//				// Set region.
//				currentRegion = Utility.getRegion(region);
//				
//				if (currentRegion != null)
//					clientAuth.setClientRegion("s3", currentRegion);
				
				// Instantiate the AuthenticateAwsServiceClient class. 
				AuthenticateAwsServiceClient authClient = new AuthenticateAwsServiceClient();
				
				// Get the region enum value. 
				currentRegion = Utility.getRegion(region);
				
				// Get the authenticated client. 
				if (currentRegion != null)
					s3Client = authClient.getAuthenticatedS3Client();
		} 
		
		catch (AmazonServiceException ase) {
        	StringBuffer err = new StringBuffer();
        	err.append(("Caught an AmazonServiceException, which means your request made it "
                      + "to Amazon S3, but was rejected with an error response for some reason."));
       	   	err.append(String.format("%n Error Message:  %s %n", ase.getMessage()));
       	   	err.append(String.format(" HTTP Status Code: %s %n", ase.getStatusCode()));
       	   	err.append(String.format(" AWS Error Code: %s %n", ase.getErrorCode()));
       	   	err.append(String.format(" Error Type: %s %n", ase.getErrorType()));
       	   	err.append(String.format(" Request ID: %s %n", ase.getRequestId()));
        	
    	} 
		
		if (s3Client != null) {
			
			// Initialize the BucketOperations class to handle related REST API calls.
			BucketOperations.initBucketOperations(s3Client); 
			
			// Initialize the ObjectOperations class to handle related REST API calls.
			ObjectOperations.initObjectOperations(s3Client);
			
			// Instantiate the SimpleUI class and display menu.
			SimpleUI sui = new SimpleUI();
	
			// Start processing user's input.
			sui.processUserInput();
		}
		else 
			String.format("Error %s", "Main: authorized S3 client object is null.");
		

		// Display goodbye menu.
		Utility.displayGoodbyeMessage("AWS S3");	
	}
	
}
