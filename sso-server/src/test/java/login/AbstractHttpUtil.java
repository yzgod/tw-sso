package login;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.tongwei.sso.util.DateUtils;


/**
 * @author		yangz
 * @date		2017年9月13日 上午8:03:19
 * @description	
 */
public abstract class AbstractHttpUtil {
	
	protected BasicClientCookie[] addCookies;
	protected CookieStore cookieStore = new BasicCookieStore();
	protected String domain;		//cookie的domain
	protected int timeout = 5000;	//默认5s超时
	protected HttpHost proxy;		//代理
	protected Header[] headers;
	
	public AbstractHttpUtil() {
	}
	
	public AbstractHttpUtil(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public AbstractHttpUtil(HttpHost proxy) {
		this.proxy = proxy;
	}

	public AbstractHttpUtil(CookieStore cookieStore, HttpHost proxy) {
		this(cookieStore);
		this.proxy = proxy;
	}

	public AbstractHttpUtil(BasicClientCookie[] addCookies, CookieStore cookieStore, HttpHost proxy) {
		this(addCookies);
		this.cookieStore = cookieStore;
		this.proxy = proxy;
	}

	public AbstractHttpUtil(BasicClientCookie... addCookies) {
		this.addCookies = addCookies;
		for (BasicClientCookie cookie : addCookies) {
			cookieStore.addCookie(cookie);
		}
	}
	
	public BasicClientCookie[] getAddCookies() {
		return addCookies;
	}

	public void setAddCookies(BasicClientCookie[] addCookies) {
		this.addCookies = addCookies;
		for (BasicClientCookie cookie : addCookies) {
			cookieStore.addCookie(cookie);
		}
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	
	public HttpHost getProxy() {
		return proxy;
	}

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	@SuppressWarnings("deprecation")
	public HttpClientConnectionManager httpsInit() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
                // 默认信任所有证书  
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {  
                    return true;  
                }  
            }).build();  
            // AllowAllHostnameVerifier: 这种方式不对主机名进行验证，验证功能被关闭，是个空操作(域名验证)  
            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext,  
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
		    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
		            .register("http", PlainConnectionSocketFactory.getSocketFactory())  
		            .register("https", sslcsf)  
		            .build();  
		    return new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public String post(String url, String params,Header... addHeaders) {
		return doPost(url, params, addHeaders);
	}
	
	public String get(String url,Header... addHeaders) {
		return doGet(url, addHeaders);
	}
	
	/**
	 * post请求
	 */
	private String doPost(String url, String params, Header... addHeaders) {
		HttpClientConnectionManager manager = httpsInit();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(manager).build();
		CloseableHttpResponse response = null;
		HttpPost httpPost =null;
		try {
			httpPost = httpPost(url, params, addHeaders);
			response = httpClient.execute(httpPost);
			setCookieStore(response);//设置cookie
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, manager, response, httpPost);
		}
		return null;
	}


	/**
	 * get请求
	 */
	private String doGet(String url,Header... addHeaders) {
		HttpClientConnectionManager manager = httpsInit();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(manager).build();
		CloseableHttpResponse response = null;
		HttpGet httpGet = null;
		try {
			httpGet = httpGet(url, addHeaders);
			response = httpClient.execute(httpGet);
			setCookieStore(response);//设置cookie
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(httpClient, manager, response, httpGet);
		}
		return null;
	}


	/**
	 * @param url
	 * @param httpClient
	 * @param addHeaders
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private HttpGet httpGet(String url, Header... addHeaders)
			throws IOException, ClientProtocolException {
		HttpGet httpGet = new HttpGet(url);
		setDomain(url);
		Builder builder = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout);//设置请求和传输超时时间
		if(proxy!=null){
			builder.setProxy(proxy);
		}
		httpGet.setConfig(builder.build());
		if(headers!=null){
			httpGet.setHeaders(headers);
		}
		for (Header header : addHeaders) {
			if (header!=null) {
				httpGet.addHeader(header);
			}
		}
		return httpGet;
	}
	
	/**
	 * @param url
	 * @param params
	 * @param httpClient
	 * @param addHeaders
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private HttpPost httpPost(String url, String params,Header... addHeaders) throws UnsupportedEncodingException, IOException, ClientProtocolException {
		HttpPost httpPost = new HttpPost(url);
		Builder builder = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout);//设置请求和传输超时时间
		if(proxy!=null){
			builder.setProxy(proxy);
		}
		httpPost.setConfig(builder.build());
		if(headers!=null){
			httpPost.setHeaders(headers);
		}
		for (Header header : addHeaders) {
			if (header!=null) {
				httpPost.addHeader(header);
			}
		}
		StringEntity entity = new StringEntity(params);
		httpPost.setEntity(entity);
		return httpPost;
	}
	
	/**
	 * domain设置
	 */
	private void setDomain(String url){
		if(domain!=null){
			return;
		}
		if(StringUtils.startsWithIgnoreCase(url, "http://")){
			url = url.substring(7);
		}else if(StringUtils.startsWithIgnoreCase(url, "https://")){
			url = url.substring(8);
		}else{
			throw new IllegalArgumentException("url地址格式不正确!");
		}
		int i = url.indexOf("/");
		if(i > 0){
			domain = url.substring(0,i);
		}
	}
	
	/**
	 * 设置cookieStore
	 */
	private void setCookieStore(CloseableHttpResponse response) {
		Header[] cookies = response.getHeaders("set-cookie");
		for (Header cookie : cookies) {
			String value = cookie.getValue();
			String[] split = value.split("; ");
			BasicClientCookie clientCookie = null;
			for (int i = 0; i < split.length; i++) {
				String s = split[i];
				int index = s.indexOf("=");
				if(index != -1){
					String name = s.substring(0,index);
					String val = s.substring(index+1);
					if(i==0){
						clientCookie = new BasicClientCookie(name,val);
					}else{
						if(StringUtils.containsIgnoreCase(s, "path")){
							clientCookie.setPath(val);
						}else if(StringUtils.containsIgnoreCase(s, "expires")){
							clientCookie.setExpiryDate(DateUtils.addDays(new Date(), 10));
						}else if(StringUtils.containsIgnoreCase(s, "domain")){
							clientCookie.setDomain(val);
						}
					}
				}else{
				}
			}
			if(clientCookie.getDomain() == null){
				if(domain==null){
					continue;
				}
				clientCookie.setDomain(domain);
			}
			cookieStore.addCookie(clientCookie);
		}
	}
	
	/**
	 * 关闭
	 */
	private void close(CloseableHttpClient httpClient, HttpClientConnectionManager manager,
			CloseableHttpResponse response, HttpRequestBase request) {
		try {
			if (request!=null) {
				request.releaseConnection();
			}
			if (response != null) {
				response.close();
			}
			if (manager != null) {
				manager.shutdown();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
