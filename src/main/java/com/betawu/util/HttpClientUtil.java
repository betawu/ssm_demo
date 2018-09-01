package com.betawu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
  
/*  
 * 利用HttpClient进行post请求的工具类  
 */  
public class HttpClientUtil{ 
    
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    
    /**
     * https请求
     * @param url
     * @param map 请求参数
     * @param charset 字符集
     * @param reqHeader 请求头信息
     * @return
     */
    public static String httpsPost(String url,String s, String charset,Map<String, String> reqHeader) {  
        HttpClient httpClient = null;  
        String result = null;  
        try {  
            httpClient = init();  
            result=request(httpClient, url, s, charset, reqHeader);
        } catch (Exception ex) {  
            logger.error("HttpsClientUtil-->doPost发送https请求出错",ex);  
        }  
        return result;  
    }  
    public static String httpsPost(String url,String name,File file, Object o, String charset) {  
    	HttpClient httpClient = null;  
    	String result = null;  
    	try {  
    		httpClient = init();  
    		result=request(httpClient,url,name,file,o,charset);
    	} catch (Exception ex) {  
    		logger.error("HttpsClientUtil-->doPost发送https请求出错",ex);  
    	}  
    	return result;  
    }  
    
    /**
     * http请求
     * @param url
     * @param map 请求参数
     * @param charset 字符集
     * @param reqHeader 请求头信息
     * @return
     */
    public static String httpPost(String url, Map<String, String> map, String charset,Map<String, String> reqHeader){
        HttpClient httpClient = null;  
        String result = null;  
        try {  
            httpClient = new DefaultHttpClient(); 
            result=request(httpClient, url, map, charset, reqHeader);
        } catch (Exception ex) {  
            logger.error("HttpsClientUtil-->httpPost发送http请求出错",ex);
        }  
        return result;  
    }
    
    public static String httpGet(String url, Map<String,String> header) {
        
        try {
        	CloseableHttpClient httpClient = HttpClients.createDefault();
        	
        	HttpGet httpGet = new HttpGet(url);
        	for (String name : header.keySet()) {
        		httpGet.addHeader(name, header.get(name));
        	}
			CloseableHttpResponse response = httpClient.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity,"utf-8");
		} catch (Exception e) {
			 logger.error("HttpClientUtil-->doGet发送http请求出错",e);
		}
        return null;
	}
    
    /**
     * 组装请求
     * @param url
     * @param map 请求参数
     * @param charset 字符集
     * @param reqHeader 请求头信息
     * @return
     */
    private static String request(HttpClient httpClient,String url, 
            Map<String, String> map, String charset,
            Map<String, String> reqHeader){
        HttpPost httpPost = null;  
        String result = null;  
        try {  
            httpPost = new HttpPost(url);  
            // 设置参数requestBody  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            for(Map.Entry<String,String > elem:map.entrySet()){  
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));  
            }  
            if (list.size() > 0) {  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,  
                        charset);  
                httpPost.setEntity(entity);  
            }
            //设置请求头
            for(Map.Entry<String,String > entry:reqHeader.entrySet()){  
                httpPost.addHeader(entry.getKey(),entry.getValue());
            }  
            //执行http请求
            HttpResponse response = httpClient.execute(httpPost);  
            if (response != null) {  
                HttpEntity resEntity = response.getEntity();  
                if (resEntity != null) {  
                    result = EntityUtils.toString(resEntity, charset);  
                }  
            }  
        } catch (Exception ex) {  
            logger.error("HttpsClientUtil-->request发送请求出错",ex);
        }  
        return result;  
    }
    /**
     * 组装请求
     * @param url
     * @param map 请求参数
     * @param charset 字符集
     * @param reqHeader 请求头信息
     * @return
     */
    private static String request(HttpClient httpClient,String url,String name, 
    		File file,Object o, String charset
    		){
    	HttpPost httpPost = null;  
    	String result = null;  
    	try {  
    		httpPost = new HttpPost(url);  
    		// 设置参数requestBody  
    		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    		multipartEntityBuilder.addBinaryBody(name,file);
    		Field[] fields = o.getClass().getDeclaredFields();
    		for (Field f : fields) {
    			f.setAccessible(true);
    			multipartEntityBuilder.addTextBody(f.getName(),(String) f.get(o));
    			logger.info(f.getName()+"="+f.get(o));
    		}
    		HttpEntity httpEntity = multipartEntityBuilder.build();
    		httpPost.setEntity(httpEntity);
    		//执行http请求
    		
    		HttpResponse response = httpClient.execute(httpPost);  
    		if (response != null) {  
    			HttpEntity resEntity = response.getEntity();  
    			if (resEntity != null) {  
    				result = EntityUtils.toString(resEntity, charset);  
    			}  
    		}  
    	} catch (Exception ex) {  
    		logger.error("HttpsClientUtil-->request发送请求出错",ex);
    	}  
    	return result;  
    }
    /**
     * 组装请求
     * @param url
     * @param map 请求参数
     * @param charset 字符集
     * @param reqHeader 请求头信息
     * @return
     */
    private static String request(HttpClient httpClient,String url, 
    		String s, String charset,
    		Map<String, String> reqHeader){
    	HttpPost httpPost = null;  
    	String result = null;  
    	try {  
    		httpPost = new HttpPost(url);  
    		// 设置参数requestBody  
    		httpPost.setEntity(new StringEntity(s,charset));
    		//设置请求头
    		for(Map.Entry<String,String > entry:reqHeader.entrySet()){  
    			httpPost.addHeader(entry.getKey(),entry.getValue());
    		}  
    		//执行http请求
    		HttpResponse response = httpClient.execute(httpPost);  
    		if (response != null) {  
    			HttpEntity resEntity = response.getEntity();  
    			if (resEntity != null) {  
    				result = EntityUtils.toString(resEntity, charset);  
    			}  
    		}  
    	} catch (Exception ex) {  
    		logger.error("HttpsClientUtil-->request发送请求出错",ex);
    	}  
    	return result;  
    }
    
    private static HttpClient init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("证书路径"));//加载本地的证书进行https加密传输
        try {
            keyStore.load(instream, "证书密码".toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "证书密码".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        return httpClient;
    }
	public static String httpsGet(String url,Map<String,String> header) {
		 HttpClient httpClient = null;  
	        String result = null;  
	        try {  
	            httpClient = init();
	            HttpGet httpGet = new HttpGet(url);
	            
	            Set<String> keySet = header.keySet();
	            for (String s : keySet) {
	            	httpGet.setHeader(s,header.get(s));
				}
	            
	            HttpResponse response = httpClient.execute(httpGet);
	            if (response != null) {  
	    			HttpEntity resEntity = response.getEntity();  
	    			if (resEntity != null) {  
	    				result = EntityUtils.toString(resEntity, "UTF-8");  
	    			}  
	    		}  
	        } catch (Exception ex) {  
	            logger.error("HttpsClientUtil-->doPost发送https请求出错",ex);  
	        }  
	        return result;  
	}  
}
