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
package netinf.common.datamodel.rdf.identity;

import netinf.common.datamodel.attribute.Attribute;
import netinf.common.datamodel.attribute.DefinedAttributeIdentification;
import netinf.common.datamodel.identity.GroupIdentityObject;
import netinf.common.datamodel.rdf.DatamodelFactoryRdf;
import netinf.common.exceptions.NetInfCheckedException;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * This is a {@link GroupIdentityObjectRdf}, it might only be used within the rdf-implementation of the datamodel
 * 
 * @author PG Augnet 2, University of Paderborn
 */
public class GroupIdentityObjectRdf extends IdentityObjectRdf implements GroupIdentityObject {

   public GroupIdentityObjectRdf(Model model, DatamodelFactoryRdf datamodelFactoryRdf) throws NetInfCheckedException {
      super(model, datamodelFactoryRdf);
   }

   @Override
   public Attribute getEncryptedGroupKeys() {
      return getSingleAttribute(DefinedAttributeIdentification.ENCRYPTED_GROUP_KEYS.getURI());
   }

   @Override
   public void setEncryptedGroupKeys(Attribute attribute) {
      resetAttribute(DefinedAttributeIdentification.ENCRYPTED_GROUP_KEYS.getURI(), attribute);
   }

   @Override
   public Attribute getMembers() {
      return getSingleAttribute(DefinedAttributeIdentification.MEMBERS_OF_GROUP.getURI());
   }

   @Override
   public void setMembers(Attribute attribute) {
      resetAttribute(DefinedAttributeIdentification.MEMBERS_OF_GROUP.getURI(), attribute);
   }

   @Override
   public String describe() {
      StringBuffer buf = new StringBuffer("a Group Identity Object that ");
      buf.append(getIdentifier().describe());
      return buf.toString();
   }

}
