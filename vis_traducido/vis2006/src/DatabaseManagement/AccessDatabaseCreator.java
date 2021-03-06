/*
 * AccessDatabaseCreator.java
 *
 * Created on 10. Januar 2006, 11:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package DatabaseManagement;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.*;


/**
 *
 * @author jhansen
 * this class extends the basis class DatabaseCreator
 * and overwrites its methods.
 * this technique makes it possible to implementate
 * interfaces for other databases easily.
 */
public class AccessDatabaseCreator extends DatabaseCreator {
    
    private String mdpath="";  // a destination where an empty access database exists 
 // private String newdbpath=""; // the path and name of the new database;  inherited from DatabaseCreator
    
    
    /** Creates a new instance of AccessDatabaseCreator */
    public AccessDatabaseCreator(String metadatapath) {
        mdpath=metadatapath; 
        type=1; //-> 1=ACCESS must be the same as in DBConn inherited static field from basis class DatabaseCreator
    }
    
    public AccessDatabaseCreator(String metadatapath, String newdatabasepath, boolean create ) {
        type=1; // inherited static field from basis class DatabaseCreator
        mdpath=metadatapath;
        newdbpath=newdatabasepath;
        // direct creation of the mdb within the construcor:
        if (create){
            createNewDB(newdbpath);
        }
    }
    
    public void setMetadatapath(String path){
        mdpath=path;
    }
    
    public void setNewpath(String path){
        newdbpath=path;
    }
    
    public String getMetadatapath(){
        return mdpath;
    }
    
    public String getNewpath(){
        return newdbpath;
    }
    
    public boolean showFileSaveDialog(){
        boolean created=false;
        // 1. eine leere Datenbank im gew�nschten Verzeichnis erzeugen:
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter( new FileFilter() {
          public boolean accept( File f ) {
            return f.isDirectory() ||
                   f.getName().toLowerCase().endsWith(".mdb");
          }
          public String getDescription() {
            return "Access Datenbank";
          }
        } );    
        int returnVal = fc.showSaveDialog( null );    
        if ( returnVal == JFileChooser.APPROVE_OPTION ){
            File file = fc.getSelectedFile();
            if  (file.exists()){
                System.out.println("file already exists");
                newdbpath=file.getAbsolutePath();
                created=true;
            }
            else{
                created=createNewDB(file.getAbsolutePath());                
            }
        }
        else{
            System.out.println( "Selecci�n abortada" );
            created=false;
        }
        return created;
    }
    
    public boolean createNewDB(String newpath){
        if(mdpath.compareTo("")!=0 && newpath.compareTo("")!=0){
            String newdb=newpath;
            if(newdb.endsWith(".mdb")==false){
                newdb=newpath+".mdb";
            }
            copyDB(newdb);
            newdbpath=newdb;
            File file = new File(newdb);
            return file.exists(); 
        }
        else{
            return false;
        }
    }
    
    private void copyFile(String source, String destination) throws Exception {
       FileInputStream fis;
       BufferedInputStream bis;
       FileOutputStream fos;
       BufferedOutputStream bos;
       byte[] b;
       fis = new FileInputStream(source);
       fos = new FileOutputStream(destination);
       bis = new BufferedInputStream(fis);
       bos = new BufferedOutputStream(fos);
       try{
           b = new byte[bis.available()];
           bis.read(b);
           bos.write(b);
           bis.close();
           bos.close();
       }
       catch (IOException eio){System.out.println(eio);}
    }
    
    private void copyDB(String newdb){
      String emptydb=mdpath+"empty.mdb";
      try{
          copyFile(emptydb,newdb);
      }
      catch (Exception e){JOptionPane.showMessageDialog(null,"Error al crear la nueva base de datos","Crear proyecto",JOptionPane.ERROR_MESSAGE);} 
    }
    
}
