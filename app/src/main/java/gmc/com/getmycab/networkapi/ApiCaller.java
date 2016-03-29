package gmc.com.getmycab.networkapi;

import org.json.JSONObject;

/**
 *
 */
public class ApiCaller  {
	private static ApiCaller apiCaller;

	public enum RequestType {
		HTTP_POST, BOOK, /*HTTP_REQUEST_IN_CHAT, HTTP_REQUEST_WITH_FILE,*/ HTTP_POST_REQUEST_WITH_IMAGE
	}

	private ApiCaller() {
	}

	public static ApiCaller getCaller() {
		if (apiCaller == null)
			apiCaller = new ApiCaller();
		return apiCaller;
	}


	public Object getServiceResponce(RequestType requestType, String url,
			JSONObject jsonParameter) {
		Object obj = null;

		switch (requestType) {
		case HTTP_POST:
			obj = ServerInteractor
					.httpServerPostNew(jsonParameter.toString(), url);
			break;


		default:
			break;
		}

		return obj;

	}



}
