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
				
				String apiKey = "AIzaSyBUdKmBr6mH6Nniv2Ng692ZbW1dh0AxlKk";
				
				//開始建立傳送資料及傳送給GCM Server
				//建立Post連線設置
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
				
				JSONObject message = new JSONObject();  //要傳送的JSON物件
				message.put("data", data);
				message.put("to", RegID);
					
				//建立StringEntity後使用Http Post送出JSON資料
				httpPost.setEntity(new StringEntity(message.toString()));
				HttpResponse response = closeableHttpClient.execute(httpPost); 
				System.out.println(response.toString());
				
				//取得回傳的JSON格式資料
				String ss = EntityUtils.toString(response.getEntity());
				JSONObject jj = new JSONObject(ss);
				System.out.println(jj);

				//JSONArray aUnregID = jj.getJSONArray("results");
				//int unregcnt = aUnregID.length();
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




/*
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;


import org.json.JSONArray;
import org.json.JSONObject;
*/

/*
 			//開始建立傳送資料及傳送給GCM Server
			//建立Post連線設置
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
			
			HttpPost httpPost = new HttpPost("https://android.googleapis.com/gcm/send");
			
			//GCM服務的API Key                    
			httpPost.addHeader("Authorization", "key=AIzaSyBUdKmBr6mH6Nniv2Ng692ZbW1dh0AxlKk");
			//選擇使用JSON型式傳送;，在Header中加上Content-Type為application/json的參數
			httpPost.addHeader("Content-Type", "application/json");
			//建立JSON內容
			JSONObject jsonObject = new JSONObject();   //要傳送的JSON物件
			// Registration 要放在JSON Array中，再把JSON Array塞進JSON物件中
			JSONArray registrationID = new JSONArray();                   
			registrationID.put(test_token);      //將Registration ID放入jsonArray中

			//填好JSONObject的各項值
			jsonObject.put("registration_ids", registrationID);
			jsonObject.put("data.message1", "message1");
			jsonObject.put("data.message2", "message2");
			jsonObject.put("data.message3", "message3");
			//建立StringEntity後使用Http Post送出JSON資料
			httpPost.setEntity(new StringEntity(jsonObject.toString()));
			HttpResponse response = closeableHttpClient.execute(httpPost); 
			System.out.println(response.toString());
			//取得回傳的JSON格式資料
			String ss = EntityUtils.toString(response.getEntity());
			JSONObject jj = new JSONObject(ss);
			System.out.println(jj);

			//JSONArray aUnregID = jj.getJSONArray("results");
			//int unregcnt = aUnregID.length();
 */


/*
String apiKey = "AIzaSyBUdKmBr6mH6Nniv2Ng692ZbW1dh0AxlKk";

// 1. URL
URL url = new URL("https://android.googleapis.com/gcm/send");

// 2. Open connection
HttpURLConnection conn_gcm = (HttpURLConnection) url.openConnection();

// 3. Specify POST method
conn_gcm.setRequestMethod("POST");

// 4. Set the headers
conn_gcm.setRequestProperty("Content-Type", "application/json");
conn_gcm.setRequestProperty("Authorization", "key="+apiKey);

conn_gcm.setDoOutput(true);

// 5. Add JSON data into POST request body
// 5.1 Use Jackson(??) object mapper to convert Content object into JSON
ObjectMapper mapper = new ObjectMapper();

// 5.2 Get connection output stream
DataOutputStream wr = new DataOutputStream(conn_gcm.getOutputStream());

// 5.3 Copy Content "json" into
mapper.writeValue(wr, app);

// 5.4 Send the request
wr.flush();

// 5.5 close
wr.close();

// 6. Get the response
int responseCode = conn_gcm.getResponseCode();
System.out.println("\nSending 'POST' request to URL : " + url);
System.out.println("Response Code : " + responseCode);

BufferedReader in = new BufferedReader(new InputStreamReader(conn_gcm.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();

while ((inputLine = in.readLine()) != null) {
	response.append(inputLine);
}
in.close();

// 7. Print result
System.out.println(response.toString());
*/