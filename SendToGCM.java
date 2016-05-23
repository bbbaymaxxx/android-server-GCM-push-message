package edu.ntust.jersey.module;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.*;
import org.json.JSONObject;

import edu.ntust.dao.MySQL;

@Path("gcm")
public class SendToGCM {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendtogcm (
			@FormParam("eviFile") String eviFile,
			@FormParam("requestFile") String requestFile,
			@FormParam("requestFileName") String requestFileName,
			@FormParam("eviFileDetail") String eviFileDetail,
			@FormParam("mac_address") String mac_address) throws SQLException{
		Connection conn = null;
		Statement query = null;
		JSONObject list = new JSONObject();
		ResultSet rs = null;

		try {
			conn = MySQL.getInstance().getConnection();
			query = conn.createStatement();
			rs = query.executeQuery("SELECT * FROM client_data WHERE mac_address ='" + mac_address + "'");
			while (rs.next()){
				String RegID = rs.getString(1);
				list.put("RegID", RegID);
				
				String apiKey = "AIzaSyBUdKmBr6mH6Nniv2Ng6...";
				
				HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
				CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
				
				HttpPost httpPost = new HttpPost("https://android.googleapis.com/gcm/send");
				                    
				httpPost.addHeader("Authorization", "key=" + apiKey);
				httpPost.addHeader("Content-Type", "application/json");
				
				// data 存成 JSON Object
				JSONObject data = new JSONObject();
				data.put("eviFile", eviFile);
				data.put("requestFile", requestFile);
				data.put("requestFileName", requestFileName);
				data.put("eviFileDetail", eviFileDetail);
				System.out.println(data);
				
				JSONObject message = new JSONObject(); 
				message.put("data", data);
				message.put("to", RegID);
					
				httpPost.setEntity(new StringEntity(message.toString()));
				HttpResponse response = closeableHttpClient.execute(httpPost); 
				System.out.println(response.toString());
				
				String ss = EntityUtils.toString(response.getEntity());
				JSONObject jj = new JSONObject(ss);
				System.out.println(jj);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
}