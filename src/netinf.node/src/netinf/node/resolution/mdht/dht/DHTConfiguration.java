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
package netinf.node.resolution.mdht.dht;

/***
 * Class for storing configuration for individual DHT rings. Used as a convenience class
 * 
 * @author PG NetInf3
 */
public class DHTConfiguration {

   private String bootHost;
   private int bootPort;
   private int listenPort;
   private int level;

   /**
    * Parameterized constructor for the configuration.
    * @param bootHost This is the hostname of the bootstrap node in the MDHT. If this 
    * 				  is the FIRST node in the DHT, just use "localhost".
    * @param bootPort The port to connect to on the bootstrap node. This differs according to level.
    * @param listenPort The port to listen on for DHT services on the local node.
    * @param level	This parameter describes which level is being configured on the current node.
    */
   public DHTConfiguration(String bootHost, int bootPort, int listenPort, int level) {
      this.bootHost = bootHost;
      this.bootPort = bootPort;
      this.listenPort = listenPort;
      this.level = level;
   }

   public String getBootHost() {
      return bootHost;
   }

   public int getBootPort() {
      return bootPort;
   }

   public int getListenPort() {
      return listenPort;
   }

   public int getLevel() {
      return level;
   }

}
