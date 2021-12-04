package APITestTool.APITestingTool;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FetchCompanyApiKey 
{	
	public static String serverURL = "https://qatest.fareye.co";
	public static String loginEndPoint = "/app/authentication";
	public static String username = "007_admin";
	public static String password = "admin";
	public static String apiKeyEndPoint = "/v1/app/rest/user/api_key";
	public static String logoutEndpoint = "/v1/app/logout";
	
	static private Cookies cookies;
	
	public static String getEncryptedPassword() throws NoSuchAlgorithmException
	{
		return EncryptPassword.toHexString(EncryptPassword.getSHA(password));
	}
	
	public static void companyLogin() throws NoSuchAlgorithmException
	{	
		cookies = given()
		   .param("j_username", username)
		   .param("j_password",getEncryptedPassword())
		
		.when()
	      .post(serverURL.concat(loginEndPoint))
	      .then()
          .statusCode(200)
          .extract()
          .response()
          .getDetailedCookies();
	}
	
	public static String getCompanyApiKey() throws NoSuchAlgorithmException
	{
		String apiKey = given()
		.cookies(cookies)
    	.when()
    	.get(serverURL.concat(apiKeyEndPoint))
    	.then()
        .statusCode(200)
        .extract()
        .path("token");
		
		return apiKey;
	}
	
	public static void companyLogout()
	{
		given()
		.cookies(cookies)
    	.when()
    	.get(serverURL.concat(logoutEndpoint))
    	.then()
    	.statusCode(200);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		companyLogin();
		System.out.println(getCompanyApiKey());
		companyLogout();
	}
}
