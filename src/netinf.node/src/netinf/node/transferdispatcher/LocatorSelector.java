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
package netinf.node.transferdispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import netinf.common.datamodel.DefinedAttributePurpose;
import netinf.common.datamodel.InformationObject;
import netinf.common.datamodel.attribute.Attribute;
import netinf.common.datamodel.attribute.DefinedAttributeIdentification;

/**
 * Helper for iterating over a locator-list.
 * 
 * @author PG NetInf 3, University of Paderborn.
 */
public class LocatorSelector implements Iterator<String> {

   private InformationObject io;
   private List<Attribute> locatorList;
   private Iterator<Attribute> locatorIterator;

   /**
    * Constructor.
    * 
    * @param io
    *           The IO.
    */
   public LocatorSelector(InformationObject io) {
      this.io = io;
      locatorList = getLocatorList();
      locatorIterator = locatorList.iterator();
   }

   /**
    * Provides the list of locators of the underlying IO.
    * 
    * @return List of locators.
    */
   private List<Attribute> getLocatorList() {
      List<Attribute> result = new ArrayList<Attribute>();
      List<Attribute> locators = io.getAttributesForPurpose(DefinedAttributePurpose.LOCATOR_ATTRIBUTE.toString());
      for (Attribute locator : locators) {
         if (locator.getIdentification() != DefinedAttributeIdentification.CHUNK.getURI()) {
            result.add(locator);
         }
      }
      return result;
   }

   @Override
   public boolean hasNext() {
      return locatorIterator.hasNext();
   }

   @Override
   public String next() {
      Attribute loc = locatorIterator.next();
      return loc.getValue(String.class);
   }

   @Override
   public void remove() {
      locatorIterator.remove();
   }

}
