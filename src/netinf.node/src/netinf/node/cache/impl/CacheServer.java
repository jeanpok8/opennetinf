package netinf.node.cache.impl;

/**
 * Interface for an cache adapter that can be used by a NetInfCache
 * 
 * @author PG NetInf 3, University of Paderborn
 */
public interface CacheServer {

   /**
    * Caches a given BO in the Server
    * 
    * @param bo
    *           the bitlevel-object
    * @param hashOfBO
    *           the hash-value of the given BO
    * @return true if the operation was successfully executed, otherwise false
    */
   boolean cacheBO(byte[] bo, String hashOfBO);

   /**
    * Checks if the cache contains a specific BO
    * 
    * @param hashOfBO
    *           the hash-value of the bitlevel-object
    * @return true if BO exists, otherwise false
    */
   boolean containsBO(String hashOfBO);

   /**
    * checks if the adapter is successfully connected to the cache server
    * 
    * @return true if cache is connected, otherwise false
    */
   boolean isConnected();
}