package netinf.node.cache;

import java.util.Properties;

import netinf.common.datamodel.impl.module.DatamodelImplModule;
import netinf.common.security.impl.module.SecurityModule;
import netinf.node.cache.impl.CacheServer;
import netinf.node.cache.impl.EhCacheServerImpl;
import netinf.node.cache.impl.NetInfCacheImpl;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * The module for the in-network-caching component
 * 
 * @author PG NetInf 3, University of Paderborn
 */
public class CacheModuleTest extends PrivateModule {

   private final Properties properties;

   public CacheModuleTest(final Properties properties) {
      this.properties = properties;
   }

   @Override
   public void configure() {
      Names.bindProperties(binder(), properties);
      bind(NetInfCache.class).to(NetInfCacheImpl.class).in(Singleton.class);
      bind(CacheServer.class).to(EhCacheServerImpl.class);
      expose(NetInfCache.class);

      install(new DatamodelImplModule());
      install(new SecurityModule());
   }

}
