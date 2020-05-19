package stat.ga;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;

import org.apache.log4j.Logger;

public class GoogleAnalyticsTracker {

	private static Logger logger = Logger
			.getLogger(GoogleAnalyticsTracker.class);
	static Proxy proxy = Proxy.NO_PROXY;

	final AnalyticsConfigData configData;
	final GoogleAnalyticsV4_7_2 builder;

	private static ExecutorService EXECUTOR = null;

	private ExecutorService getExecutor() {
		if (EXECUTOR == null) {
			synchronized (GoogleAnalyticsTracker.class) {
				if (EXECUTOR == null) {
					EXECUTOR = Executors.newFixedThreadPool(2);
					Runtime.getRuntime().addShutdownHook(new Thread() {

						@Override
						public void run() {
							try {
								EXECUTOR.shutdown();
								EXECUTOR.awaitTermination(1, TimeUnit.SECONDS);
							} catch (InterruptedException ignored) {
								EXECUTOR.shutdownNow();
							}
						}
					});
				}
			}
		}
		return EXECUTOR;
	}

	public GoogleAnalyticsTracker(AnalyticsConfigData argConfigData) {
		configData = argConfigData;
		builder = new GoogleAnalyticsV4_7_2(configData);
	}

	/**
	 * Define the proxy to use for all GA tracking requests.
	 * <p>
	 * Call this static method early (before creating any tracking requests).
	 * 
	 * @param argProxy
	 *            The proxy to use
	 */
	public static void setProxy(Proxy argProxy) {
		proxy = (argProxy != null) ? argProxy : Proxy.NO_PROXY;
	}

	/**
	 * Define the proxy to use for all GA tracking requests.
	 * <p>
	 * Call this static method early (before creating any tracking requests).
	 * 
	 * @param proxyAddr
	 *            "addr:port" of the proxy to use; may also be given as URL
	 *            ("http://addr:port/").
	 */
	public static void setProxy(String proxyAddr) {
		if (proxyAddr != null) {
			Scanner s = new Scanner(proxyAddr);

			// Split into "proxyAddr:proxyPort".
			proxyAddr = null;
			int proxyPort = 8080;
			try {
				s.findInLine("(http://|)([^:/]+)(:|)([0-9]*)(/|)");
				MatchResult m = s.match();

				if (m.groupCount() >= 2) {
					proxyAddr = m.group(2);
				}

				if ((m.groupCount() >= 4) && (!m.group(4).isEmpty())) {
					proxyPort = Integer.parseInt(m.group(4));
				}
			} finally {
				s.close();
			}

			if (proxyAddr != null) {
				SocketAddress sa = new InetSocketAddress(proxyAddr, proxyPort);
				setProxy(new Proxy(Type.HTTP, sa));
			}
		}
	}

	/**
	 * Tracks a page view.
	 * 
	 * @param argPageURL
	 *            required, Google won't track without it. Ex:
	 *            <code>"org/me/javaclass.java"</code>, or anything you want as
	 *            the page url.
	 * @param argPageTitle
	 *            content title
	 * @param argHostName
	 *            the host name for the url
	 * @param argReferrerSite
	 *            site of the referrer. ex, www.dmurph.com
	 * @param argReferrerPage
	 *            page of the referrer. ex, /mypage.php
	 */
	public void trackPageViewFromReferrer(String argPageURL,
			String argPageTitle, String argHostName, String argReferrerSite,
			String argReferrerPage) {
		if (argPageURL == null) {
			throw new IllegalArgumentException(
					"Page URL cannot be null, Google will not track the data.");
		}
		AnalyticsRequestData data = new AnalyticsRequestData();
		data.setHostName(argHostName);
		data.setPageTitle(argPageTitle);
		data.setPageURL(argPageURL);
		data.setReferrer(argReferrerSite, argReferrerPage);
		makeCustomRequest(data);
	}

	/**
	 * Tracks a page view.
	 * 
	 * @param argPageURL
	 *            required, Google won't track without it. Ex:
	 *            <code>"org/me/javaclass.java"</code>, or anything you want as
	 *            the page url.
	 * @param argPageTitle
	 *            content title
	 * @param argHostName
	 *            the host name for the url
	 * @param argReferrerSite
	 *            site of the referrer. ex, www.dmurph.com
	 * @param argReferrerPage
	 *            page of the referrer. ex, /mypage.php
	 */
	public void trackPageViewWithLocalhostAsReferrer(String argPageURL,
			String argPageTitle, String argHostName, String argReferrerPage) {
		if (argPageURL == null) {
			throw new IllegalArgumentException(
					"Page URL cannot be null, Google will not track the data.");
		}
		AnalyticsRequestData data = new AnalyticsRequestData();
		data.setHostName(argHostName);
		data.setPageTitle(argPageTitle);
		data.setPageURL(argPageURL);
		String localAddr = "127.0.0.1";
		try {
			InetAddress localAddrIP = InetAddress.getLocalHost();
			localAddr = localAddrIP.getCanonicalHostName();
		} catch (UnknownHostException ignored) {
		}
		data.setReferrer(localAddr, argReferrerPage);
		makeCustomRequest(data);
	}

	/**
	 * Tracks a page view.
	 * 
	 * @param argPageURL
	 *            required, Google won't track without it. Ex:
	 *            <code>"org/me/javaclass.java"</code>, or anything you want as
	 *            the page url.
	 * @param argPageTitle
	 *            content title
	 * @param argHostName
	 *            the host name for the url
	 * @param argSearchSource
	 *            source of the search engine. ex: google
	 * @param argSearchKeywords
	 *            the keywords of the search. ex: java google analytics tracking
	 *            utility
	 */
	public void trackPageViewFromSearch(String argPageURL, String argPageTitle,
			String argHostName, String argSearchSource, String argSearchKeywords) {
		if (argPageURL == null) {
			throw new IllegalArgumentException(
					"Page URL cannot be null, Google will not track the data.");
		}
		AnalyticsRequestData data = new AnalyticsRequestData();
		data.setHostName(argHostName);
		data.setPageTitle(argPageTitle);
		data.setPageURL(argPageURL);
		data.setSearchReferrer(argSearchSource, argSearchKeywords);
		makeCustomRequest(data);
	}

	/**
	 * 
	 * 
	 * @param data
	 */
	private void makeCustomRequest(AnalyticsRequestData data) {
		final String url = builder.buildURL(data);
		getExecutor().execute(new Runnable() {

			@Override
			public void run() {
				dispatchRequest(url);
			}
		});
	}

	private final static void dispatchRequest(String argURL) {
		try {
			URL url = new URL(argURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection(proxy);
			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				logger.error("JGoogleAnalyticsTracker: Error requesting url '{}', received response code {} "
						+ argURL + responseCode);
			} else {
				logger.debug("JGoogleAnalyticsTracker: Tracking success for url '{} '"
						+ argURL);
			}
		} catch (Exception e) {
			logger.error("Error making tracking request", e);
		}
	}

	/**
	 * Tracks an event. To provide more info about the page, use
	 * {@link #makeCustomRequest(AnalyticsRequestData)}.
	 * 
	 * @param argCategory
	 * @param argAction
	 */
	public void trackEvent(String argCategory, String argAction) {
		trackEvent(argCategory, argAction, null, null);
	}

	/**
	 * Tracks an event. To provide more info about the page, use
	 * {@link #makeCustomRequest(AnalyticsRequestData)}.
	 * 
	 * @param argCategory
	 * @param argAction
	 * @param argLabel
	 */
	public void trackEvent(String argCategory, String argAction, String argLabel) {
		trackEvent(argCategory, argAction, argLabel, null);
	}

	/**
	 * Tracks an event. To provide more info about the page, use
	 * {@link #makeCustomRequest(AnalyticsRequestData)}.
	 * 
	 * @param argCategory
	 *            required
	 * @param argAction
	 *            required
	 * @param argLabel
	 *            optional
	 * @param argValue
	 *            optional
	 */
	public void trackEvent(String argCategory, String argAction,
			String argLabel, Integer argValue) {
		AnalyticsRequestData data = new AnalyticsRequestData();
		data.setEventCategory(argCategory);
		data.setEventAction(argAction);
		data.setEventLabel(argLabel);
		data.setEventValue(argValue);

		makeCustomRequest(data);
	}

	/**
	 * Resets the session cookie.
	 */
	public void resetSession() {
		builder.resetSession();
	}

}
