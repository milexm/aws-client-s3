package com.acloudysky.s3;

import java.io.IOException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/***
 * Performs S3 object operations. 
 * Each method calls the related AWS s3 API. 
 * <p>For more information, see 
 * <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingObjects.html" target="_blank">Working with Amazon S3 Objects</a>.
 * </p>
 * @author Michael Miele
 *
 */
public class ObjectOperations {
	
	// Authorized client
	private static AmazonS3 s3Client;
	
	
	/***
	 * Initializes global status variables.
	 * @param authorizedClient Client authorized to access the S3 service.
	 */
	public static void initObjectOperations(AmazonS3 authorizedClient) {
		s3Client = authorizedClient;
	}
	
	    
    /***
     * Uploads an object to a bucket. 
     * <b>Notes</b> 
     * <ul>
     *   <li>You can easily upload a file to S3, or upload directly an 
     * 		InputStream if you know the length of the data in the stream </li>
     *   <li>You can also specify your own metadata when uploading to S3, which allows you 
     * 		to set a variety of options like content-type and content-encoding, plus additional 
     * 		metadata specific to your applications</li>
     * </ul>
     * @param bucketName The name of the bucket to hold the object
     * @param keyName The name (key name) of the object to upload
     * @param fileName The name of the object to upload. In the example this is a text file 
     * that must already exist in the resources folder. 
     * @throws IOException Error encountered while uploading the object
     */
	public static void uploadObject(String bucketName, String keyName, String fileName) throws IOException {			
		
		try {
	            // Upload an object to the selected bucket. 
				System.out.println(String.format("\nUpload %s to S3", keyName));
	            s3Client.putObject(new PutObjectRequest(bucketName, keyName, Utility.getResourceFile(fileName)));
          
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
			catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered "
	                    + "a serious internal problem while trying to communicate with S3, "
	                    + "such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
        }
    }
	
    
	/***
	 * Downloads an object. 
	 * <b>Notes</b>
	 * <ul>
	 * 	<li>When you download an object, you get all of the object's metadata and a stream 
	 * 	from which to read the contents</li>
	 * 	<li>It's important to read the contents of the stream as quickly as possible 
	 * 	since the data is streamed directly from Amazon S3 and your network connection 
	 * 	remains open until you read all the data or close the input stream</li>
	 * 	<li>GetObjectRequest also supports several other options, including conditional 
	 * 	downloading of objects based on modification times, ETags, and selectively 
	 * 	downloading a range of an object</li>
	 * </ul>
	 * @param bucketName The name of the bucket that contains the object
	 * @param keyName The name of the object to download
	 * @throws IOException Error encountered while downloading the object
	 */
	public static void downloadObject(String bucketName, String keyName) throws IOException {
	
		try {
		
				System.out.println(String.format("\nDownload %s", keyName));
				S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
				System.out.println(String.format("Content-Type: %s", object.getObjectMetadata().getContentType()));
				Utility.displayTextInputStream(object.getObjectContent());
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
		catch (AmazonClientException ace) {
     		System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
     		System.out.println("Error Message: " + ace.getMessage());
     	}
    }
	
	/***
	 * Lists objects contained in the specified object.
	 * @param bucketName The name of the bucket that contains the objects
	 * @throws IOException Error encountered while lisitng the objects
	 */
	public static void listObject(String bucketName) throws IOException {			
		
		try {
				System.out.println(String.format("\nList objects"));
			   
	            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            	.withBucketName(bucketName);
	            ObjectListing objectListing;            
	            do {
	                objectListing = s3Client.listObjects(listObjectsRequest);
	                for (S3ObjectSummary objectSummary : 
	                	objectListing.getObjectSummaries()) {
	                    System.out.println(" - " + objectSummary.getKey() + "  " +
	                            "(size = " + objectSummary.getSize() + 
	                            ")");
	                }
	                listObjectsRequest.setMarker(objectListing.getNextMarker());
	            } while (objectListing.isTruncated());
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
		catch (AmazonClientException ace) {
     		System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
     		System.out.println("Error Message: " + ace.getMessage());
     	}	
 	}
	
	/***
	 * Deletes object in a non-versioned bucket.
	 * @param bucketName he name of the bucket that contains the object
	 * @param keyName The name of the object to delete
	 * @throws IOException Error encountered while deleting the object
	 */
	public static void deleteObject(String bucketName, String keyName) throws IOException {
		
		try {
				System.out.println(String.format("\nDelete object %s", keyName));
				s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
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
		catch (AmazonClientException ace) {
     		System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
     		System.out.println("Error Message: " + ace.getMessage());
     	}
    }
	
}
