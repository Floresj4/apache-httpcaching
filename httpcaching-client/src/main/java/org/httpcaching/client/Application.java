package org.httpcaching.client;

import java.io.IOException;

import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.impl.client.cache.ehcache.EhcacheHttpCacheStorage;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An application designed to exercise the implementation
 * of the apache httpclient-cache.  The httpcaching-service/SimpleController
 * has a single endpoint that returns a jpeg image.  This application
 * requests the SimpleController/get/some/resource twice to demonstrate
 * the initial cache missed followed by a cache hit.
 * @author Jason
 */
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	/**
	 * @return a cacheconfig with a set number of entries allowed
	 * and the maximum size for a single object
	 */
	static CacheConfig getCacheConfig() {
		return CacheConfig.custom()
		        .setMaxCacheEntries(1000)
		        .setMaxObjectSize(10 * 1000000)
		        .build();
	}

	/**
	 * @return a request configuration with a set connection
	 * socket timeout
	 */
	static RequestConfig getRequestConfig() {
		return RequestConfig.custom()
		        .setConnectTimeout(30000)
		        .setSocketTimeout(30000)
		        .build();
	}

	static EhcacheHttpCacheStorage getEhcacheHttpCacheStorage() {
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		//add log4j with apache logging enabled to see what's going on
		PropertyConfigurator.configure("log4j.properties");
		
		//create the caching client
		CloseableHttpClient cachingClient = CachingHttpClients.custom()
		        .setCacheConfig(getCacheConfig())
		        .setDefaultRequestConfig(getRequestConfig())
		        .build();

		for(int i = 0; i < 2; i++) {
			HttpCacheContext context = HttpCacheContext.create();
			HttpGet httpget = new HttpGet("http://localhost:8080/get/some/resource");	
			CloseableHttpResponse response = cachingClient.execute(httpget, context);

			try {
			    CacheResponseStatus responseStatus = context.getCacheResponseStatus();
			    switch (responseStatus) {
			        case CACHE_HIT:
			            logger.debug("A response was generated from the cache with no requests sent upstream");
			            break;
			            
			        case CACHE_MODULE_RESPONSE:
			        	logger.debug("The response was generated directly by the caching module");
			            break;
			            
			        case CACHE_MISS:
			        	logger.debug("The response came from an upstream server");
			            break;
			            
			        case VALIDATED:
			        	logger.debug("The response was generated from the cache after validating the entry with the origin server");
			            break;
			    }
			}
			finally {
			    response.close();
			}
		}
	}
}
