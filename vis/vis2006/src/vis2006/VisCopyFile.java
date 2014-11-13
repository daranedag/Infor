/*
 * CopyFile.java
 *
 * Created on 1. März 2006, 12:48
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package vis2006;

/**
 *
 * @author nagel
 */
import java.io.*;

 public class VisCopyFile{
  
     public boolean copy( String src, String dest ) {
         boolean erfolg = false;
         try{
             copy( new FileInputStream( src ), new FileOutputStream( dest ) );
             erfolg = true;
         }
         catch( IOException e ) {
             e.printStackTrace();
         }
         return erfolg;
     }
     
     
  static void copy( InputStream fis, OutputStream fos )
  {
    try
    {
      byte  buffer[] = new byte[0xffff];
      int   nbytes;
      
      while ( (nbytes = fis.read(buffer)) != -1 )
        fos.write( buffer, 0, nbytes );
    }
    catch( IOException e ) {
      System.err.println( e );
    }
    finally {
      if ( fis != null )
        try {
          fis.close();
        } catch ( IOException e ) {}
      
      try {
        if ( fos != null )
          fos.close();
      } catch ( IOException e ) {}
    }

}
 }
