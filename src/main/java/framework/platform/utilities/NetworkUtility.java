package framework.platform.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.Assert.fail;

/**
 * Contains utility methods for work with network etc.
 */
public class NetworkUtility {

	/** Gets HTTP response code from given URL and return true if it's OK. */
	public static boolean testResponseCode(String url) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con;
			con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestMethod("HEAD");
	        return (con.getResponseCode()==HttpURLConnection.HTTP_OK);
		} 
		
		catch (MalformedURLException e) {
			e.printStackTrace();
			fail("The test method testFrontEnd failed -- Malformed URL " + e.getMessage());
		} 
		
		catch (IOException e) {
			e.printStackTrace();
			fail("The test method testFrontEnd failed " + e.getMessage());
		}		
			
		return false;

		}

}
