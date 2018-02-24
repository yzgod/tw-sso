package login;


import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

/**
 * @author		yangz
 * @date		2017年9月13日 上午8:03:19
 * @description	
 */
public class HttpUtil extends AbstractHttpUtil {
	
	protected static final Header ajaxHeader 		= new BasicHeader("X-Requested-With", "XMLHttpRequest");
	protected static final Header jsonContentHeader = new BasicHeader("Content-Type","application/json;charset=utf-8");
	protected static final Header formContentHeader = new BasicHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
	protected static final Header htmlContentHeader = new BasicHeader("Content-Type","text/html; charset=utf-8");
	protected static final Header userAgentHeader 	= new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");

	public HttpUtil() {
	}
	
	public HttpUtil(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	
	public HttpUtil(BasicClientCookie... addCookies) {
		super(addCookies);
	}

	public HttpUtil(BasicClientCookie[] addCookies, CookieStore cookieStore, HttpHost proxy) {
		super(addCookies, cookieStore, proxy);
	}

	public HttpUtil(CookieStore cookieStore, HttpHost proxy) {
		super(cookieStore, proxy);
	}

	public HttpUtil(HttpHost proxy) {
		super(proxy);
	}

	public String getAjaxHeader(String url) {
		return super.get(url, userAgentHeader,ajaxHeader);
	}
	
	public String getJsonHeader(String url) {
		return super.get(url, userAgentHeader,jsonContentHeader);
	}
	
	public String getHtmlHeader(String url) {
		return super.get(url, userAgentHeader,htmlContentHeader);
	}
	
	public String postAjaxHeader(String url,String params) {
		return super.post(url,params,userAgentHeader,ajaxHeader);
	}
	
	public String postFormHeader(String url,String params) {
		return super.post(url,params,userAgentHeader,formContentHeader);
	}
	
	public String postJsonHeader(String url,String params) {
		return super.post(url,params,userAgentHeader,jsonContentHeader);
	}
	
}
