/*
 * Copyright (C) 2009,2010 University of Paderborn, Computer Networks Group
 * Copyright (C) 2009,2010 Christian Dannewitz <christian.dannewitz@upb.de>
 * Copyright (C) 2009,2010 Thorsten Biermann <thorsten.biermann@upb.de>
 * 
 * Copyright (C) 2009,2010 Eduard Bauer <edebauer@mail.upb.de>
 * Copyright (C) 2009,2010 Matthias Becker <matzeb@mail.upb.de>
 * Copyright (C) 2009,2010 Frederic Beister <azamir@zitmail.uni-paderborn.de>
 * Copyright (C) 2009,2010 Nadine Dertmann <ndertmann@gmx.de>
 * Copyright (C) 2009,2010 Michael Kionka <mkionka@mail.upb.de>
 * Copyright (C) 2009,2010 Mario Mohr <mmohr@mail.upb.de>
 * Copyright (C) 2009,2010 Felix Steffen <felix.steffen@gmx.de>
 * Copyright (C) 2009,2010 Sebastian Stey <sebstey@mail.upb.de>
 * Copyright (C) 2009,2010 Steffen Weber <stwe@mail.upb.de>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Paderborn nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package netinf.node.module.scenario;

import netinf.access.HTTPServer;
import netinf.access.NetInfServer;
import netinf.access.TCPServer;
import netinf.common.datamodel.rdf.module.DatamodelRdfModule;
import netinf.common.datamodel.translation.module.DatamodelTranslationModule;
import netinf.common.utils.Utils;
import netinf.node.gp.module.GPConnectionModule;
import netinf.node.gp.module.GPNodeCapabilityModule;
import netinf.node.gp.module.GPResolveModule;
import netinf.node.module.AbstractNodeModule;
import netinf.node.resolution.ResolutionInterceptor;
import netinf.node.resolution.ResolutionService;
import netinf.node.resolution.bocaching.impl.BOCachingInterceptor;
import netinf.node.resolution.bocaching.module.BOCachingModule;
import netinf.node.resolution.iocaching.impl.IOCacheImpl;
import netinf.node.resolution.iocaching.module.LocalIOCachingModule;
import netinf.node.resolution.locator.impl.LocatorSelectorImpl;
import netinf.node.resolution.remote.gp.GPResolutionService;
import netinf.node.search.SearchService;

import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * The Class ANodeModule.
 * 
 * @author PG Augnet 2, University of Paderborn
 */
public class ANodeModule extends AbstractNodeModule {
   public static final String NODE_PROPERTIES = "../configs/scenario1/netinfnode_a.properties";

   public ANodeModule() {
      super(Utils.loadProperties(NODE_PROPERTIES));
   }

   @Override
   protected void configure() {
      super.configure();

      // The datamodel
      install(new DatamodelRdfModule());
      install(new DatamodelTranslationModule());

      // Caching Storage
      install(new LocalIOCachingModule());
      install(new BOCachingModule());

      install(new GPConnectionModule());
      install(new GPResolveModule());

      install(new GPNodeCapabilityModule(getProperties()));
   }

   @Singleton
   @Provides
   ResolutionService[] provideResolutionServices(IOCacheImpl ioCache, GPResolutionService gpResolutionService) {
      return new ResolutionService[] { ioCache.getRS(), gpResolutionService };
   }

   @Singleton
   @Provides
   ResolutionInterceptor[] provideResolutionInterceptors(BOCachingInterceptor boCaching, IOCacheImpl ioCache,
         LocatorSelectorImpl locatorSelector) {
      return new ResolutionInterceptor[] { ioCache, boCaching, locatorSelector };
   }

   @Singleton
   @Provides
   SearchService[] provideSearchServices() {
      return new SearchService[] {};
   }

   @Singleton
   @Provides
   NetInfServer[] providesAccess(TCPServer tcpServer, HTTPServer httpServer) {
      return new NetInfServer[] { tcpServer, httpServer };
   }
}
