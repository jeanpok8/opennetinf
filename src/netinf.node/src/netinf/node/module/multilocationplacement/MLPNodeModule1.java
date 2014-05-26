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
package netinf.node.module.multilocationplacement;

import org.apache.commons.lang.ArrayUtils;

import netinf.access.HTTPServer;
import netinf.access.NetInfServer;
import netinf.access.TCPServer;
import netinf.common.communication.MessageEncoder;
import netinf.common.communication.MessageEncoderXML;
import netinf.common.datamodel.rdf.module.DatamodelRdfModule;
import netinf.common.datamodel.translation.module.DatamodelTranslationModule;
import netinf.common.utils.Utils;
import netinf.node.access.AccessServer;
import netinf.node.module.AbstractNodeModule;
import netinf.node.resolution.ResolutionInterceptor;
import netinf.node.resolution.ResolutionService;
import netinf.node.resolution.mdht.MDHTResolutionService;
import netinf.node.resolution.mdht.module.MDHTResolutionModule;
import netinf.node.search.SearchService;

import com.google.inject.Provides;
import com.google.inject.Singleton;

import netinf.node.search.rdf.SearchServiceRDF;
import netinf.node.access.rest.module.RESTModule;
import netinf.node.access.rest.RESTAccessServer;
import netinf.node.cache.CachingInterceptor;
import netinf.node.cache.network.NetworkCacheModule;
import netinf.node.cache.peerside.PeersideCacheModule;
import netinf.node.search.rdf.module.SearchServiceRDFModule;

import netinf.node.resolution.rdf.RDFResolutionService;
import netinf.node.resolution.rdf.module.RDFResolutionServiceModule;
import netinf.node.resolution.remote.RemoteResolutionFactory;

public class MLPNodeModule1 extends AbstractNodeModule {

   public static final String NODE_PROPERTIES = "../configs/MLP/Node1.properties";

   public MLPNodeModule1() {
      super(Utils.loadProperties(NODE_PROPERTIES));
   }

   @Override
   protected void configure() {
      super.configure();
      bind(MessageEncoder.class).to(MessageEncoderXML.class).in(Singleton.class);
      // The Datamodel
      install(new DatamodelRdfModule());
      install(new DatamodelTranslationModule());

      // ResolutionServices
      //install(new RDFResolutionServiceModule());
      install(new MDHTResolutionModule(NODE_PROPERTIES));

      // RESTful API
      install(new RESTModule());

      // SearchServices
      install(new SearchServiceRDFModule());
      
      // Caches
      //install(new PeersideCacheModule(NODE_PROPERTIES));
      
      // Caches
      install(new NetworkCacheModule(NODE_PROPERTIES));


   }

   @Singleton
   @Provides
   ResolutionService[] provideResolutionServices(RemoteResolutionFactory remoteResolutionFactory,
		   	MDHTResolutionService mdhtResolutionService){//, 
//		   	RDFResolutionService rdfResolutionService) {
//	   return new ResolutionService[] { mdhtResolutionService, rdfResolutionService };
     ResolutionService[] otherRS = { mdhtResolutionService};//, rdfResolutionService };
     ResolutionService[] remoteRS = {}; //remoteResolutionFactory.getRemoteResolutionServices().toArray(new ResolutionService[] {});
     return (ResolutionService[]) ArrayUtils.addAll(remoteRS, otherRS);
   }

   /**
    * This method provides all the {@link ResolutionInterceptor}s which are automatically inserted into the node. In order to get
    * an instance of the according {@link ResolutionInterceptor}, add an additional parameter to this method, since this puts
    * GUICE in charge of creating the correct instance of the according service. The {@link ResolutionInterceptor}s will be called
    * in the given order.
    * 
    * @param localResolutionService
    * @param rdfResolutionService
    * @return
    */
   @Singleton
   @Provides
   ResolutionInterceptor[] provideResolutionInterceptors(CachingInterceptor caching) {
      return new ResolutionInterceptor[] { caching };
   }

   @Singleton
   @Provides
   SearchService[] provideSearchServices(SearchServiceRDF searchServiceRdf) {
      return new SearchService[] { searchServiceRdf };
   }


   @Singleton
   @Provides
   AccessServer[] provideAccessServers(RESTAccessServer rest) {
      return new AccessServer[] { rest };
   }


   @Singleton
   @Provides
   NetInfServer[] providesAccess(TCPServer tcpServer, HTTPServer httpServer) {
      return new NetInfServer[] { tcpServer, httpServer };
   }
}
