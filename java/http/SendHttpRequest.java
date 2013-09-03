package com.auto.util.http;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SendHttpRequest {
	private final static Logger LOGGER  = Logger.getLogger(SendHttpRequest.class); 

	Client client = new Client();
	public String getByUrl(String url, Map<String,String> paramStrs) throws Exception {
		PostParameter[] params = new PostParameter[paramStrs.entrySet().size()];
		int offset=0;
		try {
			for(Entry<String, String> entry: paramStrs.entrySet()){
				params[offset++] = new PostParameter(entry.getKey(),entry.getValue());
			}
			return client.get(url, params);
		} catch (Exception e) {
			LOGGER.error("httpget_error", e);
			throw e;
		}
	}
	
	public JSONObject post(String url, PostParameter[] params) throws Exception {
		try {
			String result = client.post(url, params);
			if(StringUtils.isNotEmpty(result)){
				return JSONObject.fromObject(result);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("httppost_error", e);
			throw e;
		}
	}

	public JSONObject get(String url, PostParameter[] params) throws Exception {
		try {
			String paramString = client.get(url, params);
			System.out.println(paramString);
			if(StringUtils.isNotEmpty(paramString) && paramString.indexOf("{") != -1){
				return JSONObject.fromObject(paramString);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("httpget_error", e);
			throw e;
		}
	}


	class Client {
		private static final String GBK_CHARSET = "gbk";
		private MultiThreadedHttpConnectionManager connectionManager;
		private int maxSize;
		HttpClient client = new HttpClient();

		public Client() {
			// change timeout to 2s avoid block thread-pool (Tim)
			this(150, 2000, 2000, 1024 * 1024);
		}

		public Client(int maxConPerHost, int conTimeOutMs, int soTimeOutMs,int maxSize) {
			connectionManager = new MultiThreadedHttpConnectionManager();
			HttpConnectionManagerParams params = connectionManager.getParams();
			params.setDefaultMaxConnectionsPerHost(maxConPerHost);
			params.setConnectionTimeout(conTimeOutMs);
			params.setSoTimeout(soTimeOutMs);

			HttpClientParams clientParams = new HttpClientParams();
			// 忽略cookie 避免 Cookie rejected 警告
			clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			client = new HttpClient(clientParams, connectionManager);
			this.maxSize = maxSize;
		}

		public String get(String url, PostParameter[] params)
				throws Exception {
			if (null != params && params.length > 0) {
				String encodedParams = encodeParameters(params);
				if (-1 == url.indexOf("?")) {
					url += "?" + encodedParams;
				} else {
					url += "&" + encodedParams;
				}
			}
			GetMethod getmethod = new GetMethod(url);
			getmethod.getParams().setContentCharset(GBK_CHARSET);
			return httpRequest(getmethod);

		}

		/*
		 * 对parameters进行encode处理
		 */
		public String encodeParameters(PostParameter[] postParams) {
			StringBuffer buf = new StringBuffer();
			for (int j = 0; j < postParams.length; j++) {
				if (j != 0) {
					buf.append("&");
				}
				try {
					if (null == postParams[j]) {
						continue;
					}
					buf.append(postParams[j].getName()).append("=")
							.append(postParams[j].getValue());
					// buf.append(
					// URLEncoder.encode(postParams[j].getName(),
					// GBK_CHARSET))
					// .append("=")
					// .append(URLEncoder.encode(postParams[j].getValue(),
					// GBK_CHARSET));
				} catch (Exception neverHappen) {
				}
			}
			return buf.toString();
		}

		public String post(String url, PostParameter[] params)throws Exception {
			PostMethod postMethod = new PostMethod(url);
			for (PostParameter param : params) {
				if (null == param) {
					continue;
				}
				postMethod.addParameter(param.getName(), param.getValue());
			}
			HttpMethodParams param = postMethod.getParams();
			param.setContentCharset(GBK_CHARSET);
//			postMethod.addRequestHeader("User-Agent"," Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; CIBA; .NET CLR 2.0.50727; MAXTHON 2.0)");
//			String accept = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*";
//			postMethod.setRequestHeader("Accept", accept);
			return httpRequest(postMethod);
		}

		public String httpRequest(HttpMethod method) throws Exception {
			int responseCode = -1;
			try {
//				List<Header> headers = new ArrayList<Header>();
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));
				client.executeMethod(method);
				// Header[] resHeader = method.getResponseHeaders();
				responseCode = method.getStatusCode();
				LOGGER.info(String.valueOf(responseCode));
				if (responseCode != 200) {
				} else {
//					byte[] responseBody = method.getResponseBody();
					LOGGER.info(method.getResponseBodyAsString());
					return method.getResponseBodyAsString();
				}
				LOGGER.info(method.getResponseBodyAsString());
				return null;
			} catch (IOException ioe) {
				throw ioe;
			} finally {
				method.releaseConnection();
			}
		}

		public int getMaxSize() {
			return maxSize;
		}
		
		
	}

	
}
