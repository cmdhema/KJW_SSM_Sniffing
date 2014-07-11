import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.*;

import javax.imageio.ImageIO;

public class NetworkConn {

	public static final String serverURL = "http://ec2-54-238-239-102.ap-northeast-1.compute.amazonaws.com:8000/";
	public static String getSMSInfo(String flag) {
		
		MasterProgramMain info = new MasterProgramMain();
		
		String parsingUserInfo = null;
		
		StringBuilder queryStringParams = new StringBuilder();
		queryStringParams.append(serverURL);
		if(flag.equals("SMSSend")) {
			queryStringParams.append("info/smssend?");
		} else if (flag.equals("SMSRecv")) {
			queryStringParams.append("info/smsrecv?");
		} else if (flag.equals("Photo")) {
			queryStringParams.append("photo?");
		} else if (flag.equals("Calling")){
			queryStringParams.append("info/calling?");
		} else if (flag.equals("Info")) {
			queryStringParams.append("info?");
		}
		
		queryStringParams.append("num=");
		queryStringParams.append(info.getPhoneNumber());
		
		try {
			parsingUserInfo = getHttpURLConnection(queryStringParams.toString());
		} catch (Exception e) {
			
		}
		
		return parsingUserInfo;
	}
	
	public static String getHttpURLConnection(String targetURL) {

		HttpURLConnection httpConnection = null;
		InputStream fromServer = null;
		StringBuilder jsonBuf = new StringBuilder();

		try {

			URL url = new URL(new String(targetURL.getBytes("UTF-8")));
			
			httpConnection = (HttpURLConnection) url.openConnection();
			
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(10000);
			
			System.out.println("cc");
			
			httpConnection.connect();
			
			System.out.println("AA");
			int responseCode = httpConnection.getResponseCode();
			System.out.println(responseCode);
			System.out.println("BB");
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

			String line = "";
			while ((line = br.readLine()) != null) {
				jsonBuf.append(line);
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("Connect Success");
				System.out.println(jsonBuf.toString());
			} else {
				System.out.println("Connect Fail : " + responseCode );
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fromServer != null) {
				try {
					fromServer.close();
				} catch (IOException ioe) {
				}
			}
			if (httpConnection != null)
				httpConnection.disconnect();
		}

		return jsonBuf.toString();
	}

	public static String getHttp_savePhoto() {

		MasterProgramMain infoImage = new MasterProgramMain();
		
		HttpURLConnection httpConnection = null;		
		InputStream fromServer = null;		
		StringBuilder jsonBuf = new StringBuilder();		

		String targetURL = null;
		
		String parsingUserInfo = null;
		
		StringBuilder queryStringParams = new StringBuilder();
		queryStringParams.append(serverURL);
		queryStringParams.append("user/photo?");
		queryStringParams.append("name=");
		//1387251367135
		queryStringParams.append(infoImage.getPhotoName());
		
		
		targetURL = queryStringParams.toString();
		
		try {

			URL url = new URL(new String(targetURL.getBytes("UTF-8")));
			
			httpConnection = (HttpURLConnection) url.openConnection();
			
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(10000);
			
			httpConnection.connect();
			
			int responseCode = httpConnection.getResponseCode();
			System.out.println("responseCode : " + responseCode);			
	
			BufferedImage bi = ImageIO.read(url.openStream());
			
			File file = new File(infoImage.getPhotoName());


			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("Connect Success");
				try{
					ImageIO.write(bi, "png", file);
					System.out.println("Sava Image~" + infoImage.getPhotoName());
				}catch(IllegalArgumentException e){
					System.out.println("Fail~");
				}		
			
			} else {
				System.out.println("Connect Fail : " + responseCode );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fromServer != null) {
				try {
					fromServer.close();
				} catch (IOException ioe) {
				}
			}
			if (httpConnection != null)
				httpConnection.disconnect();
		}

		return jsonBuf.toString();
	}
	
	
	
	
	
}
