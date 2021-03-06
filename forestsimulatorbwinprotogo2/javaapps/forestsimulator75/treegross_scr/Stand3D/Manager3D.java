/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Gr�tzelstr.2, 37079 G�ttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package treegross.Stand3D;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import treegross.base.Stand;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Canvas3D;
import com.sun.j3d.utils.image.TextureLoader;
import java.net.*;
//import WaldPlaner.WPClipboardSupport.*;

/**
 *
 * @author jhansen
 */
public class Manager3D implements ActionListener {
     private Stand3DScene scene=null;
     private Texture2D[] textures = new Texture2D[7];
     private JPanel home=null;    
     private Stand st=null;
     private boolean speccol=false;
     private boolean treestatus=true;
     private boolean showharvested=false;
     private boolean available3d=false;
     private boolean textured=false;
     private boolean showinfo=true;
     private boolean showfog=true;
     private boolean showmesh=true;
     private int[] speciestoshow=null;
     private String texpath=null;
     //new swing components:
     private JPanel jPanel2;    
     private ToolBar3D toolbar3d;
     private String tz=System.getProperty("file.separator");

     public boolean isStandLoaded=false;
     
    /** Creates a new instance of Manager3D 
     *  homepanel (JPanel) the place where teh 3dcanvas should be drawn
     *  texturepath (String) the path to the textures for crowns etc
     *  if null the option texture will not be available
     */
    public Manager3D(JPanel homepanel, String texturepath, boolean showtoolbar){
        home=homepanel;
        texpath=texturepath+tz+"3dtextures";
        initSwing(texturepath+tz+"icons");
        setToolbarVisible(showtoolbar);
        PackageInfo info3d=new PackageInfo();
        if(info3d.isJ3DInstalled()){
            new Query3DProperties();
            available3d=true;
        } 
        if (showtoolbar == false) {
            showinfo=false;
            showfog = false;
            showmesh = false;
        }
        if(!available3d) JOptionPane.showMessageDialog(null,"Es ist keine Java3D-API installiert.","Java3D",JOptionPane.ERROR_MESSAGE);
        if (showtoolbar) loadTextures();        
    }
    
    private void initSwing(String iconpath){
        home.setLayout(new BorderLayout());
        home.setOpaque(false);
        jPanel2= new JPanel();
        jPanel2.setLayout(new BorderLayout());        
        home.add("Center", jPanel2);
        // ToolBar:
        if(iconpath!=null) toolbar3d= new ToolBar3D(this,true, iconpath);
        else toolbar3d= new ToolBar3D(this,false, iconpath);
        home.add("North",toolbar3d);
    }
    
    public boolean get3DAvailable(){
        return available3d;
    }
    
    private void loadTextures(){
        if(available3d && texpath!=null){
            
            URL url0 =null;
            URL url1 =null;
            URL url2 =null;
            URL url3 =null;
            URL url4 =null;
            URL url5 =null;
            URL url6 =null;
            int m = texpath.toUpperCase().indexOf("FILE");
            if ( m < 0 || m > 4) texpath="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                    +System.getProperty("file.separator")+texpath;
//            System.out.println("Manager3D: URL: "+texpath);
            String fname0=texpath+System.getProperty("file.separator")+"blcrown.jpg";
            String fname1=texpath+System.getProperty("file.separator")+"blcrownblend.jpg";
            String fname2=texpath+System.getProperty("file.separator")+"conicrown.jpg";
            String fname3=texpath+System.getProperty("file.separator")+"conicrownbottom.jpg";
            String fname4=texpath+System.getProperty("file.separator")+"standbase.jpg";
            String fname5=texpath+System.getProperty("file.separator")+"bltrunk.jpg";
            String fname6=texpath+System.getProperty("file.separator")+"crownshad1.JPG";
            try {
                 url0 = new URL(fname0);
                 url1 = new URL(fname1);
                 url2 = new URL(fname2);
                 url3 = new URL(fname3);
                 url4 = new URL(fname4);
                 url5 = new URL(fname5);
                 url6 = new URL(fname6);}
            catch (Exception e){ }
            try {

//           System.out.println("Manager3D: URL: Ohne");

            Canvas3D canvas = new Canvas3D(com.sun.j3d.utils.universe.SimpleUniverse.getPreferredConfiguration()); 
            textures[0]=(Texture2D)(new TextureLoader(url0,canvas)).getTexture();           
            textures[1]=(Texture2D)(new TextureLoader(url1,canvas)).getTexture();
            textures[2]=(Texture2D)(new TextureLoader(url2,canvas)).getTexture();
            textures[3]=(Texture2D)(new TextureLoader(url3,canvas)).getTexture(); 
            textures[4]=(Texture2D)(new TextureLoader(url4,canvas)).getTexture();
            textures[5]=(Texture2D)(new TextureLoader(url5,canvas)).getTexture();
            textures[6]=(Texture2D)(new TextureLoader(url6,canvas)).getTexture();
//            System.out.println("Manager3D: textures build."+texpath+tz);
//            System.out.println("Manager3D: textures build.");
            }
            catch (Exception e){ }

        }
    }
    
    public void setStand(Stand stand, int[] species){
        if(available3d){
            speciestoshow=species;
            clean3D();
            st=stand;
            if(stand !=null){
                scene= new Stand3DScene(st, speccol, showharvested, textured, treestatus, showinfo, showfog, showmesh, speciestoshow, textures);
                jPanel2.add(scene);
                isStandLoaded=true;
                scene.setPickFocus();
            }
        }
    } 
    
    public void setStand(Stand stand){
        if(available3d){
            speciestoshow=new int[stand.nspecies];
            for(int i=0; i<stand.nspecies; i++){
                speciestoshow[i]=stand.sp[i].code;
            }
            clean3D();
            st=stand;
            if(stand !=null){
                scene= new Stand3DScene(st, speccol, showharvested, textured, treestatus, showinfo, showfog, showmesh, speciestoshow, textures);
                jPanel2.add(scene);
                isStandLoaded=true;
                scene.setPickFocus();
            }
        }
    }    
    
    public void refreshStand(){
        if(scene!=null){
            scene.setShowingSpecies(null);
            scene.setNewTreeDataAndRefresh(null);
        }
    }
    
     public void refreshStand(int[] species){
        if(scene!=null){
            scene.setShowingSpecies(species);
            scene.setNewTreeDataAndRefresh(null);
        }
    }    
   
    public void showAllSpecies(){
        if(scene!=null){
            scene.setShowingSpecies(null);
        } 
    }
    
    public void setSpecies(int[] species){
        if(scene!=null){
            scene.setShowingSpecies(species);
        }
    } 
    
    public void grabFocus(){
        if(scene!=null){
            scene.setPickFocus();
        }    
    }
    
    public String getSelectedTree(){
        String result="";
        if(scene!=null){
            
        }
        return result;        
    }
    
    public void letTreefall(String treeno){
        if(scene!=null){
            scene.harvestTreeInStand(treeno);
        }      
    }

    
    public void clean3D(){
      double freemb=Runtime.getRuntime().freeMemory()/1024/1024;
      double totalmb=Runtime.getRuntime().totalMemory()/1024/1024;
      double maxmb=Runtime.getRuntime().maxMemory()/1024/1024;
      System.out.println("MB free before clean3D: "+freemb+" total MB: "+ totalmb+" max MB: "+maxmb);
      if(scene!=null){
          scene.cleanScene(); 
          jPanel2.remove(scene);          
      }
      jPanel2.removeAll();
      scene=null;
      st=null;
      isStandLoaded=false;
      //System.runFinalization();
      System.gc();
      freemb=Runtime.getRuntime().freeMemory()/1024/1024;
      System.out.println("MB free after clean3D: "+freemb);
    }
    
    public void setToolbarVisible(boolean visible){
        toolbar3d.setVisible(visible);
//        toolbar3d.jToggleButton6.setActionCommand("showfog");
//        toolbar3d.jToggleButton6.setActionCommand("showfog");
    }
    
    public boolean isToolbarVisible(){
        return toolbar3d.isVisible();
    }
    
     public void actionPerformed(ActionEvent e) {
         if(scene!=null){                
                // toolbar3d
                 if(e.getActionCommand().equals("setstatus")){
                    treestatus=!treestatus;
                    scene.setStatus(treestatus);
                    scene.setPickFocus();
                }   
             
                if(e.getActionCommand().equals("settexture")){
                    textured=!textured;
                    scene.setTextured(textured);
                    scene.setPickFocus();
                }             
                
                if(e.getActionCommand().equals("setdead")){
                    showharvested=!showharvested;
                    scene.setShowDeadTrees(showharvested);
                    scene.setPickFocus();                                      
                }
                
                if(e.getActionCommand().equals("setspecies")){
                    speccol=!speccol;
                    if(textured){
                        textured=false;
                        toolbar3d.jToggleButton3.setSelected(textured);
                    }
                    scene.setSpeciesColor(speccol);
                    scene.setPickFocus();
                }
                
                if(e.getActionCommand().equals("harvest")){
                    scene.harvestAllMarkedTrees();
                    scene.setPickFocus();
                }
                
                if(e.getActionCommand().equals("showinfo")){
                    showinfo=!showinfo;
                    scene.showtreeinfo=showinfo;
                    scene.setPickFocus();
                }
                
                 if(e.getActionCommand().equals("goback")){
                    scene.setViewAndPositionStart();
                    scene.setPickFocus();
                } 
                if(e.getActionCommand().equals("screenshot")){
                    saveScreenShot();
                    scene.setPickFocus();
                }  
                if(e.getActionCommand().equals("showfog")){
                    showfog=toolbar3d.jToggleButton6.isSelected();
                    scene.setFogEnable(toolbar3d.jToggleButton6.isSelected());
                }
                if(e.getActionCommand().equals("showmesh")){
                    showmesh=toolbar3d.jToggleButton7.isSelected();
                    scene.setMeshVisible(toolbar3d.jToggleButton7.isSelected());
                }
                if(e.getActionCommand().equals("toolpos")){
                    if(!toolbar3d.isLeft){
                        home.remove(toolbar3d);
                        toolbar3d.setOrientation(ToolBar3D.VERTICAL);                        
                        toolbar3d.isLeft=true;                        
                        home.add("West",toolbar3d);
                        home.revalidate();
                    }
                    else{
                        home.remove(toolbar3d);
                        toolbar3d.setOrientation(ToolBar3D.HORIZONTAL);
                        home.add("North",toolbar3d);
                        home.revalidate();
                        toolbar3d.isLeft=false;
                    }
                }                               
         }
    }
     
    //     
     private void saveImageAsJPEG(BufferedImage img, String filename) {           
        File file = new File(filename);
        int ok=JOptionPane.YES_OPTION;
        if  (file.exists()){
            System.out.println("file alrerady exists");
            ok = JOptionPane.showConfirmDialog(home,"Die Datei existiert bereits! �berschreiben?","Speichern",JOptionPane.YES_NO_OPTION);                            
        }    
        if (ok == JOptionPane.YES_OPTION){
            try{
                OutputStream out = new FileOutputStream(filename);
                com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(out);
                encoder.encode(img);
                out.close();
            }   catch (Exception e) {
                System.out.println(e); 
            }
        }
     }
   
   public void copyToClipboard(){
       if (scene!=null){  
            //ClipboardUtils.setContents(scene.getScreenShot(), null);
       }
   }
   
   public void harvestTrees(){
       scene.harvestAllMarkedTrees();
       scene.setPickFocus();
   }
   
   public void setInitialView(){
       scene.setViewAndPositionStart();
       scene.setPickFocus();
   }

  public void setFogDisabled(){
       scene.setFogEnable(false);
   }
   
   public void saveScreenShot(){
        if (scene!=null){       
            BufferedImage img = scene.getScreenShot();           
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter( new FileFilter() {
            public boolean accept( File f ) {
                return f.isDirectory() ||
                   f.getName().toLowerCase().endsWith(".jpg");
            }
             public String getDescription() {
                return "JPG";
            }
            } );    
            int returnVal = fc.showSaveDialog( home );
    
            if ( returnVal == JFileChooser.APPROVE_OPTION ){
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();
                if (path.toLowerCase().endsWith(".jpg")==false) path=path+".jpg";
                saveImageAsJPEG(img, path ); 
            }
            else System.out.println( "Speichern abgebrochen" );
       }
       else{JOptionPane.showMessageDialog(home,"Sie haben kein File ge�ffnet.","Speichern",JOptionPane.INFORMATION_MESSAGE);}
   }
        
    
}
