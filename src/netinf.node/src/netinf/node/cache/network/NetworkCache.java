/*
 * Copyright (C) 2009-2011 University of Paderborn, Computer Networks Group
 * (Full list of owners see http://www.netinf.org/about-2/license)
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Paderborn nor the names of its contributors may be used to endorse
 *       or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDERS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package netinf.node.cache.network;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import netinf.node.cache.BOCacheServer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 * Ehcache-Server adapter for NetInfCache.
 * 
 * @author PG NetInf 3, University of Paderborn.
 */
public class NetworkCache implements BOCacheServer {

   private static final Logger LOG = Logger.getLogger(NetworkCache.class);
   private String cacheAddress = "";
   private boolean isConnected;
   private int mdhtScope;

   /**
    * Constructor
    * 
    * @param host
    *           address where the server is hosted
    */
   public NetworkCache(String host, String port, String tablename, int scope) {

      mdhtScope = scope;

      // create address of cache
      cacheAddress = buildCacheAddress(host, port, tablename);

      // create/check Ehcache tables
      if (!cacheExists(cacheAddress)) {
         boolean success = createCache(cacheAddress);
         if (success) {
            isConnected = true;
         } else {
            isConnected = false;
         }
      } else {
         isConnected = true;
      }
   }

   @Override
   public boolean cacheBO(byte[] bo, String hashOfBO) {
      if (isConnected()) {
         HttpClient client = new DefaultHttpClient();
         HttpPut httpPut = new HttpPut(cacheAddress + "/" + hashOfBO);
         ByteArrayEntity entity = new ByteArrayEntity(bo);
         httpPut.setEntity(entity);
         try {
            // execute request
            HttpResponse response = client.execute(httpPut);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_CREATED || status == HttpStatus.SC_OK) {
               return true;
            }
            return false;
         } catch (ClientProtocolException e) {
            LOG.error("ProtocolException in EhCache");
            return false;
         } catch (IOException e) {
            LOG.error("IOException in EhCache");
            return false;
         }
      }

      return false; // not connected
   }

   @Override
   public boolean containsBO(String hashOfBO) {
      if (isConnected()) {
         HttpClient client = new DefaultHttpClient();
         HttpHead httpHead = new HttpHead(cacheAddress + "/" + hashOfBO);
         try {
            HttpResponse response = client.execute(httpHead);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_NOT_FOUND) {
               return false;
            }
            LOG.info("Cache contains BO with hash: " + hashOfBO);
            return true;
         } catch (ClientProtocolException e) {
            LOG.error("ProtocolException in EhCache");
            return false;
         } catch (IOException e) {
            LOG.error("IOException in EhCache");
            return false;
         }
      }
      return false; // not connected
   }

   @Override
   public boolean isConnected() {
      if (isConnected) {
         return true;
      }
      return false;
   }

   /**
    * checks if the cache exists
    * 
    * @param pathToCache
    *           URL to the desired cache
    * @return true if the cache exists, otherwise false
    */
   private boolean cacheExists(String pathToCache) {
      HttpClient client = new DefaultHttpClient();
      HttpHead httpHead = new HttpHead(pathToCache);
      try {
         HttpResponse response = client.execute(httpHead);
         int statusCode = response.getStatusLine().getStatusCode();
         LOG.debug("(EhCache ) Status of cache: " + statusCode);
         if (statusCode == HttpStatus.SC_NOT_FOUND) {
            LOG.info("Cache server running - tables do not exist");
            return false;
         }
         LOG.info("Cache server running - tables exist");
         return true;
      } catch (ClientProtocolException e) {
         LOG.error("ProtocolException in EhCache");
         return false;
      } catch (IOException e) {
         LOG.error("IOException - Cache server not running...");
         return false;
      }
   }

   /**
    * Creates the necessary Ehcache tables
    * 
    * @param pathOfCache
    *           URL to the cache
    */
   private boolean createCache(String pathOfCache) {
      HttpClient client = new DefaultHttpClient();
      HttpPut httpPut = new HttpPut(pathOfCache);
      try {
         HttpResponse response = client.execute(httpPut);
         int statusCode = response.getStatusLine().getStatusCode();
         if (statusCode == HttpStatus.SC_CREATED) {
            LOG.info("Cache server running - created cache tables");
            return true;
         }
         return false;
      } catch (ClientProtocolException e) {
         LOG.error("ProtocolException in EhCache");
         return false;
      } catch (IOException e) {
         LOG.error("IOException - Cache server not running...");
         return false;
      }
   }

   /**
    * Build the whole address to the cache
    * 
    * @param host
    *           the address of the host that runs the cache server
    * @return the URL of the cache
    */
   private String buildCacheAddress(String host, String port, String tablename) {
      String cacheUrl = "http://" + host + ":" + port;
      try {
         URL url = new URL(cacheUrl);
         LOG.info("Using cache server on IP: " + url.getHost());
      } catch (MalformedURLException e) {
         LOG.warn("Wrong cache address - trying with localhost...");
         cacheUrl = "http://127.0.0.1:8080";
      }

      return cacheUrl + tablename;
   }

   @Override
   public String getURL(String hashOfBO) {
      if (isConnected()) {
         return cacheAddress + "/" + hashOfBO;
      }
      return null;
   }

   @Override
   public String getAddress() {
      return cacheAddress;
   }

   @Override
   public int getScope() {
      return mdhtScope;
   }

}
