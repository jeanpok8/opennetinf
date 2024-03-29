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
package netinf.node.rdf;

import java.util.Properties;

import netinf.common.utils.Utils;

import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.JDBC;
import com.hp.hpl.jena.sdb.sql.SDBConnection;

/**
 * This class can be used to create the tables and indices of the SDB database specified in testing.properties. As a prerequisite
 * the specified user and database (name) must already exist in the DB system.
 * <p>
 * One can also use the SQL script in /configs/netinf_node_data.sql to create everything needed for the SDB database from scratch
 * 
 * @author PG Augnet 2, University of Paderborn
 */
public class CreateSDBDatabaseTableStructure {

   private static final String CONFIGS_TESTING_PROPERTIES = "../configs/testing.properties";

   public static void main(final String[] args) {

      final Properties properties = Utils.loadProperties(CONFIGS_TESTING_PROPERTIES);

      final StoreDesc storeDesc = new StoreDesc(properties.getProperty("resolution_rdf_db_layout"),
            properties.getProperty("resolution_rdf_db_type"));
      JDBC.loadDriver(properties.getProperty("resolution_rdf_db_driver"));
      final String jdbcURL = "jdbc:" + properties.getProperty("resolution_rdf_db_type").toLowerCase() + "://"
            + properties.getProperty("resolution_rdf_db_host") + ":" + properties.getProperty("resolution_rdf_db_port") + "/"
            + properties.getProperty("resolution_rdf_db_dbname");
      final SDBConnection conn = new SDBConnection(jdbcURL, properties.getProperty("resolution_rdf_db_user"),
            properties.getProperty("resolution_rdf_db_pw"));
      final Store store = SDBFactory.connectStore(conn, storeDesc);

      store.getTableFormatter().create();
      System.out.println("Table structures successfully created");

      store.close();
      conn.close();
   }

}
