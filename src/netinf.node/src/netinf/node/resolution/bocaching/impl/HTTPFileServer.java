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
package netinf.node.resolution.bocaching.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import netinf.common.exceptions.NetInfCheckedException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Serves files from the specified directory, used for caching of Binary Objects
 * 
 * @author PG NetInf 3, University of Paderborn
 */
public class HTTPFileServer implements HttpHandler {

   private static final Logger LOG = Logger.getLogger(HTTPFileServer.class);

   private static final String REQUEST_PATH_PATTERN = "/[0-9a-zA-Z_]+";
   private static final int MAX_CONNECTIONS = 10;

   private int port;
   private String directory;
   private HttpServer server;
   private String ip;

   private boolean clearCacheOnStartup = true;

   @Inject
   public HTTPFileServer(@Named("resolution.cache.httpPort") int port, @Named("resolution.cache.httpIP") String ip,
         @Named("resolution.cache.directory") String directory) {
      this.port = port;
      this.directory = directory;
      this.ip = ip;

      // create folder if not existing
      File cacheFolder = new File(directory);
      cacheFolder.mkdir();
      if (clearCacheOnStartup) {
         File[] files = cacheFolder.listFiles();
         for (File f : files) {
            if (f.isFile()) {
               f.delete();
            }
         }
      }

   }

   /**
    * Starts the Server
    * 
    * @throws NetInfCheckedException
    */
   public void start() throws NetInfCheckedException {
      LOG.trace(null);

      try {
         server = HttpServer.create(new InetSocketAddress(port), MAX_CONNECTIONS);
         server.createContext("/", this);
      } catch (IOException e) {
         LOG.error("Error encountered while initializing the HTTPFileServer on port " + port, e);
         throw new NetInfCheckedException(e);
      }
      server.start();
   }

   @Override
   public void handle(HttpExchange httpExchange) throws IOException {
      String requestPath = httpExchange.getRequestURI().getPath();

      if (!requestPath.matches(REQUEST_PATH_PATTERN)) {
         LOG.debug("(HTTPFilesServer ) 403 Error");
         httpExchange.sendResponseHeaders(403, 0);
      } else {
         File file = new File(directory + requestPath);
         if (!file.exists()) {
            LOG.debug("(HTTPFilesServer ) 404 Error");
            httpExchange.sendResponseHeaders(404, 0);
         } else if (!file.canRead()) {
            LOG.debug("(HTTPFilesServer ) 403 Error");
            httpExchange.sendResponseHeaders(403, 0);
         } else {
            Headers h = httpExchange.getResponseHeaders();
            DataInputStream stream = new DataInputStream(new FileInputStream(file));

            // read content type and send
            if (!requestPath.contains("chunk")) {
               LOG.debug("(HTTPFilesServer ) Reading Content Type...");
               int contentTypeSize = stream.readInt();
               byte[] stringBuffer = new byte[contentTypeSize];
               stream.read(stringBuffer);
               h.set("Content-Type", new String(stringBuffer));
            }

            httpExchange.sendResponseHeaders(200, file.length());
            IOUtils.copy(stream, httpExchange.getResponseBody());

            // close streams
            IOUtils.closeQuietly(stream);
         }
      }
      httpExchange.close();
   }

   /**
    * Returns the port of the server.
    * 
    * @return The port.
    */
   public int getPort() {
      return port;
   }

   /**
    * Sets the port of the server.
    * 
    * @param port
    *           The specified port.
    */
   public void setPort(int port) {
      this.port = port;
   }

   /**
    * Returns the related directory of the server where the files are stored.
    * 
    * @return The directory name.
    */
   public String getDirectory() {
      return directory;
   }

   /**
    * Sets the related directory of the server where the files are stored.
    * 
    * @param directory
    *           The name or path of the directory.
    */
   public void setDirectory(String directory) {
      this.directory = directory;
   }

   public String getUrlForHash(String hash) {
      InetAddress thisIp = null;
      try {
         if (ip == null || ip.trim().isEmpty()) {
            thisIp = InetAddress.getLocalHost();
            return "http://" + thisIp.getHostAddress() + ":" + getPort() + '/' + hash;
         } else {
            return "http://" + ip + ":" + getPort() + '/' + hash;
         }
      } catch (UnknownHostException e) {
         return null;
      }
   }

}
