package com.xiaoai.wakeup.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.xiaoai.wakeup.net.ServiceParameters.KokozuFile;
import com.xiaoai.wakeup.net.asynctask.AsyncServiceTask;
import com.xiaoai.wakeup.net.asynctask.AsyncServiceTask.IAsyncUpdateListener;
import com.xiaoai.wakeup.net.exception.NetworkConnectionException;
import com.xiaoai.wakeup.net.jsoncache.JsonCache;
import com.xiaoai.wakeup.util.log.Log;

public class Request implements IServiceBase {

	private static final long EXPIRE_TIME_INIT = -2L;

	private static final String DEFAULT_ENCODING = "UTF-8";

	private String mUrl;

	private String mMethod;

	private String mEncoding;

	private Context mContext;

	private ServiceParameters mParams;

	private boolean isQueryCache;

	private long mJsonExpireTime = EXPIRE_TIME_INIT;

	public Request(Context context, ServiceParameters params) {
		this(context, HttpUtil.DEFAULT_SERVICE, params);
	}

	public Request(Context context, String url, ServiceParameters params) {
		this(context, url, params, METHOD_GET);
	}

	public Request(Context context, String url, ServiceParameters params,
			String method) {
		this(context, url, params, METHOD_GET, DEFAULT_ENCODING);
	}

	public Request(Context context, String url, ServiceParameters params,
			String method, String encodeing) {
		this.mContext = context;
		this.mUrl = url;
		this.mParams = params;
		this.mMethod = method;
		this.mEncoding = encodeing;
		this.isQueryCache = true;
	}

	public AsyncServiceTask makeAsyncTask(int token,
			IAsyncUpdateListener listener) {
		AsyncServiceTask task = new AsyncServiceTask(token, this, listener);
		task.execute();
		return task;
	}

	public AsyncServiceTask makeAsyncTask(IAsyncUpdateListener listener) {
		AsyncServiceTask task = new AsyncServiceTask(this, listener);
		task.execute();
		return task;
	}

	@Override
	public ServiceResult makeRequest() {
		ServiceResult result = null;

		String urlKey = mUrl + encodeParameters(mParams, true) + "?method="
				+ mMethod;

		// request data from JSON cache
		result = fetchFromJsonCache(urlKey);
		if (result != null) {
			Log.i("request from json cache, url_key: " + urlKey);
			return result;
		}

		try {
			checkNetworkAvailable();
		} catch (NetworkConnectionException e) {
			return ServiceResult.makeNetworkUnavailableResult();
		}

		try {
			if (METHOD_GET.equalsIgnoreCase(mMethod)) {
				result = doGet();
			} else if (METHOD_POST.equalsIgnoreCase(mMethod)) {
				result = doPost();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceResult.makeExceptionResult(e);
		}

		// save response json cache
		if (result.getStatus() == 0) {
			JsonCache.saveJson(mContext, urlKey, result.getData(),
					mJsonExpireTime);
		}

		return result;
	}

	/**
	 * Fetch data from JSON cache.
	 * 
	 * @param urlKey
	 *            the key of JSON cache data in database
	 * @return
	 */
	private ServiceResult fetchFromJsonCache(String urlKey) {
		if (mJsonExpireTime == EXPIRE_TIME_INIT) {
			mJsonExpireTime = JsonCache.getExpireTime(urlKey);
		}

		ServiceResult result = null;

		if (isQueryCache) {
			String data = JsonCache.getJson(mContext, urlKey);
			if (!JsonCache.NO_RECORD.equals(data)) {
				try {
					JSONObject object = new JSONObject(data);
					result = ServiceResult.makeResult(object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * Check if the network is available, if not, show Toast.
	 * 
	 * @throws NetworkConnectionException
	 */
	private void checkNetworkAvailable() throws NetworkConnectionException {
		if (!NetworkManager.isNetworkAvailable(mContext)) {
			throw new NetworkConnectionException();
		}
	}

	/**
	 * Request service by GET method.
	 * 
	 * @return
	 * @throws Exception
	 */
	private ServiceResult doGet() throws Exception {
		String url = mUrl + encodeParameters(mParams);
		Log.i("service GET url: " + url);

		HttpGet request = new HttpGet(url);
		HttpClient httpClient = getHttpClient(url);
		HttpResponse response = httpClient.execute(request);

		return processResponse(response);
	}

	/**
	 * Request service by POST method.
	 * 
	 * @return
	 * @throws Exception
	 */
	private ServiceResult doPost() throws Exception {
		Log.i("service POST url: " + mUrl);

		HttpPost request = new HttpPost(mUrl);
		setPostRequestEntity(request);

		HttpClient httpClient = getHttpClient(mUrl);
		HttpResponse response = httpClient.execute(request);

		return processResponse(response);
	}

	/**
	 * Join URL by the supplied ServiceParameters.
	 * 
	 * @param params
	 * @return
	 */
	private static String encodeParameters(ServiceParameters params) {
		return encodeParameters(params, false);
	}

	/**
	 * Join URL by the supplied ServiceParameters.
	 * 
	 * @param params
	 * @param ignoreTimestamp
	 *            if ignore the key "time_stamp" in ServiceParameters
	 * @return
	 */
	private static String encodeParameters(ServiceParameters params,
			boolean ignoreTimestamp) {
		if (params == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("?");
		for (int i = 0; i < params.size(); i++) {
			if (i != 0) {
				sb.append("&");
			}

			String key = params.getKey(i);
			if (key == null) {
				key = "";
			}
			if (ignoreTimestamp && "time_stamp".equals(key)) {
				continue;
			}

			String value = params.getValue(i);
			if (value == null) {
				value = "";
			}
			sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(value));
		}
		return sb.toString();
	}

	private HttpClient getHttpClient(String strUrl)
			throws MalformedURLException {
		HttpClient httpClient;
		URL url = new URL(strUrl);
		if ("http".equalsIgnoreCase(url.getProtocol())) {
			httpClient = new DefaultHttpClient();
		}
		httpClient = getHttpsClient(mContext);
		return httpClient;
	}

	private void setPostRequestEntity(HttpPost request)
			throws UnsupportedEncodingException {
		if (mParams.fileSize() == 0) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			for (int i = 0; i < mParams.size(); i++) {
				params.add(new BasicNameValuePair(mParams.getKey(i), mParams
						.getValue(i)));
			}

			HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			request.setEntity(httpEntity);
		} else {
			MultipartEntity multiEntity = new MultipartEntity();
			for (int i = 0; i < mParams.size(); i++) {
				ContentBody body = new StringBody(URLEncoder.encode(mParams
						.getValue(i)));
				multiEntity.addPart(URLEncoder.encode(mParams.getKey(i)), body);
			}

			for (int i = 0; i < mParams.fileSize(); i++) {
				KokozuFile kFile = mParams.getFileValue(i);
				ContentBody body = new FileBody(kFile.file, kFile.mineType);
				multiEntity.addPart(URLEncoder.encode(URLEncoder.encode(mParams
						.getFileKey(i))), body);
			}

			request.setEntity(multiEntity);
		}
	}

	private ServiceResult processResponse(HttpResponse response)
			throws Exception {
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		byte[] bytes = readStream(is);
		String data = new String(bytes, mEncoding);

		int resCode = response.getStatusLine().getStatusCode();
		if (resCode / 100 == 2) {
			return processResponseSuccess(data);
		}
		return processResponseNotSuccess(data);
	}

	private ServiceResult processResponseSuccess(String data)
			throws JSONException {
		JSONObject object = new JSONObject(data);
		Log.i("json: " + object.toString());

		ServiceResult result = ServiceResult.makeResult(data);
		result.setJsonData(object);
		return result;
	}

	private ServiceResult processResponseNotSuccess(String data) {
		Log.i("service: " + mUrl + ", msg: " + data);
		return ServiceResult.makeResultCodeNot200Result();
	}

	public static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bout.write(buffer, 0, len);
		}
		bout.close();
		inputStream.close();
		return bout.toByteArray();
	}

	public static HttpClient getHttpsClient(Context context) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryOperator(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public void setEncoding(String encoding) {
		this.mEncoding = encoding;
	}

	public static String makeUrl(String host, ServiceParameters params) {
		return host + encodeParameters(params);
	}

}
