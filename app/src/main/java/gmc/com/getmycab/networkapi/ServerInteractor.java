package gmc.com.getmycab.networkapi;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
public class ServerInteractor {
	public static final int TIMEOUTCONNECTION = 30000;
	private static final String TAG = ServerInteractor.class.getSimpleName();







	public static Object httpServerPost(String jsonString, String URL) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				TIMEOUTCONNECTION);
		HttpResponse httpresponse;
		String response = "";
		try {
			HttpPost post = new HttpPost(URL);
			Log.e(TAG, "Request: "+jsonString);
			Log.e(TAG,"URL: "+ URL);
			StringEntity se = new StringEntity(jsonString);
			post.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");

			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=utf-8"));
			post.setEntity(se);

			httpresponse = client.execute(post);
			response = EntityUtils.toString(httpresponse.getEntity()).trim();

			Log.e(TAG,"Resoponse: "+ response);

		} catch (Exception e) {
			e.printStackTrace();
			return e;

		}
		return response;
	}


	public static Object httpServerPostNew(String jsonString, String _url) {

		String response = "";


		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(50000 /* milliseconds */);
			conn.setConnectTimeout(50000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setRequestProperty("content-type", "application/json");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
			Log.e(TAG, "Request: " + jsonString);
			Log.e(TAG, "URL: " + url);

			conn.connect();
			InputStream is = conn.getInputStream();
			response = readIt(is/*, 1024*1024*1024*/);
			Log.e(TAG,"Resoponse: "+ response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return response;

		}
	}

	public static String  readIt(InputStream stream/*, int len*/) throws IOException, UnsupportedEncodingException {
//		Reader reader = null;
//		reader = new InputStreamReader(stream);
//		char[] buffer = new char[len];
//		reader.read(buffer);
//		return new String(buffer);

		java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
