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
package netinf.eventservice.siena.module;

import java.util.Properties;

import netinf.eventservice.framework.EventServiceNetInf;
import netinf.eventservice.framework.module.EventServiceFrameworkTestModule;
import netinf.eventservice.framework.subscription.SubscriberDatabaseController;
import netinf.eventservice.siena.EventServiceSiena;
import netinf.eventservice.siena.dummy.MockSubscriberDatabaseController;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * 
 * This is a special module, used for testing the translation from {@link ESFEventMessage} to {@link Notification} (used within
 * Siena). It has to be a separate module, since we do create new {@link SubscriberNetInf}, which results in the access and
 * modification of the database. Accordingly, we have to provide an additional mockup database controller (
 * {@link MockSubscriberDatabaseController}.
 * 
 * @author PG Augnet 2, University of Paderborn
 * 
 */
public class EventServiceSienaTranslationTestModule extends AbstractModule {
   private final Properties properties;

   public EventServiceSienaTranslationTestModule(Properties properties) {
      this.properties = properties;
   }

   @Override
   protected void configure() {
      install(new EventServiceFrameworkTestModule(properties));

      bind(EventServiceNetInf.class).to(EventServiceSiena.class);
      bind(EventServiceSiena.class).in(Singleton.class);
      bind(SubscriberDatabaseController.class).to(MockSubscriberDatabaseController.class);
   }
}
