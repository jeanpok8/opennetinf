/**
 * 
 */
package netinf.node.transferDeluxe;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author indy
 *
 */
public interface StreamProvider {

   InputStream getStream(String url) throws IOException;
   
}