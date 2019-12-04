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

public class Application {
	
	static CacheConfig getCacheConfig() {
		return CacheConfig.custom()
		        .setMaxCacheEntries(1000)
		        .setMaxObjectSize(8192)
		        .build();
	}

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

		CloseableHttpClient cachingClient = CachingHttpClients.custom()
		        .setCacheConfig(getCacheConfig())
		        .setDefaultRequestConfig(getRequestConfig())
		        .build();

		HttpCacheContext context = HttpCacheContext.create();

		for(int i = 0; i < 4; i++) {
			HttpGet httpget = new HttpGet("http://localhost:8080/get/some/resource");	
			CloseableHttpResponse response = cachingClient.execute(httpget, context);

			try {
			    CacheResponseStatus responseStatus = context.getCacheResponseStatus();
			    switch (responseStatus) {
			        case CACHE_HIT:
			            System.out.println("A response was generated from the cache with " +
			                    "no requests sent upstream");
			            break;
			        case CACHE_MODULE_RESPONSE:
			            System.out.println("The response was generated directly by the " +
			                    "caching module");
			            break;
			        case CACHE_MISS:
			            System.out.println("The response came from an upstream server");
			            break;
			        case VALIDATED:
			            System.out.println("The response was generated from the cache " +
			                    "after validating the entry with the origin server");
			            break;
			    }
			} finally {
			    response.close();
			}
		}
	}
}
