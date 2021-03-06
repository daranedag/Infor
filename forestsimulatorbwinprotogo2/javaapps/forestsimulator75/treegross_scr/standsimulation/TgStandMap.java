/** TreeGrOSS : class tgstandmap
   Version 05-May-2002 */
/* http://www.nw-fva.de
   Version 17-11-2008

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
package treegross.standsimulation;
import treegross.base.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;

/** draws a stand map and allows thinning by mouse click */
public class TgStandMap extends JPanel implements  MouseListener
{
    	private int x=-100,y=-100; 
	Stand st =new Stand();
        
	/** variables for scaling the graph */
	double sk,xmax,ymax,xmin,ymin; 
	
        /** width and height of the drawing area */
	public int w,h;
        int zoomStatus=0; //0=normal, 1=select lower corner, 2= select upper corner
        double xlzoom=0; double ylzoom=0; double xuzoom=0; double yuzoom=0;
        boolean doJPEG=false;
        
        StringBuffer s;
        TgJFrame frame;
        boolean screenToJpeg=false;
        boolean standMapInfo=true;
        boolean plotCrownWidth=false;
        boolean plotTreeNumber=false;
        boolean mouseThinning = true;
        boolean mouseCropTree = false;
        Dimension screensize;
        int dbhFactor=5;
        int lettersize = 11;
        String jpgFilename="standmap.jpg";
        //
        public TgStandMap(Stand stl, TgJFrame parent)
        {
            addMouseListener(this); 
            setBackground(Color.white);
	    st=stl; 
            frame = parent;

            Dimension scr= Toolkit.getDefaultToolkit().getScreenSize();             
            setPreferredSize(new Dimension((((scr.width-140)/2)-(scr.width/50)), (scr.height/2)-(scr.height/50)));               
	    neuzeichnen();
        }
//
//        
	/** draws graph */
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
                st.standinfo();
                
// next few lines are to print the jpg file always in the same size and then reset the size for the screen
               if (screenToJpeg==true) {
                    setSize(screensize);
                    lettersize=11;
                    screenToJpeg=false;
                }
               if (doJPEG==true) {
                    screensize= getSize();
                    setSize(new Dimension(3090,3000));
                    lettersize=52;
                    screenToJpeg=true;
                }
//                
	        Dimension d=getSize(); 
		w=d.width; h=d.height;
//
                BufferedImage img = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
                if (doJPEG==true){
                   g = img.getGraphics();
                }
//
		g.setColor(Color.white); 
		g.fillRect(0,0,w,h); 
		g.setColor(Color.black); 
		
                if (st.year>0 ) //only if a stand is loaded
		{	// draw stand name and size 
                       g.setFont(new java.awt.Font("Tahoma", 0, lettersize));
                     
			if (standMapInfo==true) g.drawString(st.standname+"  "+st.size+" ha",0,10); 
			// write stand values
			NumberFormat number=NumberFormat.getInstance(); 
			number=NumberFormat.getInstance(new Locale("en","US")); 
			number.setMaximumFractionDigits(1); 
			number.setMinimumFractionDigits(1); 
                        if (standMapInfo==true && lettersize < 20){ 
 			g.drawString("Jahr: "+st.year,w-100,25); 
                        g.drawString("N/ha    : "+number.format(st.nha),w-100,50); 
			g.drawString("G/ha    : "+number.format(st.bha),w-100,75); 
			g.drawString("N/ha th.: "+number.format(st.nhaout),w-100,100); 
                        g.drawString("G/ha th.: "+number.format(st.bhaout),w-100,125);
                        g.drawString("N/ha to.: "+number.format(st.nhatotal),w-100,150); 
                        g.drawString("G/ha to.: "+number.format(st.bhatotal),w-100,175); 
                        }

		}
	
         // determine scale factor for graph
		int i; 
		xmax=-999.9; ymax=-999.9; ymin=99999.9; xmin=99999.9; 
		for (i=0; i<st.ntrees; i++)
		{	if (st.tr[i].x>xmax) xmax=st.tr[i].x; 
			if (st.tr[i].x<xmin) xmin=st.tr[i].x; 
			if (st.tr[i].y>ymax) ymax=st.tr[i].y; 
			if (st.tr[i].y<ymin) ymin=st.tr[i].y; 
		} //end of for loop
		for (i=0; i<st.ncpnt; i++)
		{	if (st.cpnt[i].x>xmax) xmax=st.cpnt[i].x; 
			if (st.cpnt[i].x<xmin) xmin=st.cpnt[i].x; 
			if (st.cpnt[i].y>ymax) ymax=st.cpnt[i].y; 
			if (st.cpnt[i].y<ymin) ymin=st.cpnt[i].y; 
		} //end of for loop
                
	// add 1m for a boundary
		xmin=xmin-1.0; xmax=xmax+1.0; ymin=ymin-1.0; ymax=ymax+1.0; 
        // Zoom
                if (zoomStatus==0 && xlzoom!=0.0 && ylzoom != 0.0)
                {
                    //choose the smaller x and y value as xmin/ymin, the other one as xmax/ymax  
                    if(xlzoom<xuzoom)
                    {
                        xmin=xlzoom;
                        xmax=xuzoom;
                    }
                    else
                    {
                        xmin=xuzoom;
                        xmax=xlzoom;
                    }
                    
                    if(ylzoom<yuzoom)
                    {
                        ymin=ylzoom;
                        ymax=yuzoom;
                    }
                    else
                    {
                        ymin=yuzoom;
                        ymax=ylzoom;
                    }
                    
                }
		// scale
		sk=(w-120)/(xmax-xmin); 
		double sky=(h-60)/(ymax-ymin); 
		//	choose the smaller of the two scaling values
		if (sky<sk) sk=sky; 
		// draw stand area as line by line
		int j,xp,yp,xp2,yp2,ra;
		for (i=0;i<st.ncpnt;i++)
		 { 
                   if (i==st.ncpnt-1) j=0; 
                   else j=i+1;
		   xp=10+(int)((st.cpnt[i].x-xmin)*sk); 
		   yp=h-40-(int)((st.cpnt[i].y-ymin)*sk); 
		   xp2=10+(int)((st.cpnt[j].x-xmin)*sk); 
		   yp2=h-40-(int)((st.cpnt[j].y-ymin)*sk); 
		   g.setColor(Color.lightGray); 
		   g.drawLine(xp,yp,xp2,yp2); 
		} 
// draw north direction                
		   xp2=10+(int)((xmax)*sk); 
		   yp2=h-40-(int)((ymin)*sk);
                   g.drawLine(xp2+20,yp2-20,xp2+20,yp2-60); 
		   g.drawLine(xp2+25,yp2-50,xp2+20,yp2-60); 
     	           g.drawLine(xp2+15,yp2-50,xp2+20,yp2-60); 
                   g.drawString("N",xp2+10,yp2-30); 
                
// Plot Crown Width or radius only of the living trees
                if (plotCrownWidth==true) for(i=0; i<st.ntrees; i++){
                    if (st.tr[i].out < 0 ){
		          xp=10+(int)((st.tr[i].x-xmin)*sk); 
			  yp=h-40-(int)((st.tr[i].y-ymin)*sk); 
			  ra=(int)(sk*st.tr[i].cw/2.0); 
			  g.setColor(new java.awt.Color(st.tr[i].sp.spDef.colorRed, 
                                    st.tr[i].sp.spDef.colorGreen,st.tr[i].sp.spDef.colorBlue));
                          g.drawOval(xp-ra,yp-ra,2*ra,2*ra);                                                
                    }
                }


                
        // 2. values of rectangle is the width and height of it
	// draw tree stems as circles slightly enlarged
		for (i=0; i<st.ntrees; i++)
		{	if (st.tr[i].out < 0 || st.tr[i].out==st.year)
		    { 
                          xp=10+(int)((st.tr[i].x-xmin)*sk); 
			  yp=h-40-(int)((st.tr[i].y-ymin)*sk); 
			  ra=(int)(dbhFactor*2*sk*st.tr[i].d/200); 
			  if (ra==0) ra=1; 
			//oval second values is the width and height
			  g.setColor(new java.awt.Color(st.tr[i].sp.spDef.colorRed, 
                                    st.tr[i].sp.spDef.colorGreen,st.tr[i].sp.spDef.colorBlue));
			  if (st.tr[i].out==st.year) {
                               g.drawRect(xp-ra,yp-ra,2*ra,2*ra);
                               
                          }
			  if (st.tr[i].out<=0) 
			     { g.fillOval(xp-ra,yp-ra,2*ra,2*ra);			       
			     }
			  

			  if (st.tr[i].crop==true)
			     {g.setColor(Color.red);
			      g.drawOval(xp-ra,yp-ra,2*ra,2*ra);
			      g.drawOval(xp-ra+1,yp-ra-1,2*ra-1,2*ra+1);
			      g.drawOval(xp-ra+2,yp-ra-2,2*ra-2,2*ra+2);                             
                              ra=(int)(dbhFactor*sk*st.tr[i].d/200); 
                              g.fillOval(xp-ra,yp-ra,2*ra,2*ra);
                              
			     }
                          
			
                          if (st.tr[i].tempcrop==true)
                             {g.setColor(Color.green); 
                              g.drawOval(xp-ra,yp-ra,2*ra,2*ra);
                              g.drawOval(xp-ra+1,yp-ra-1,2*ra-1,2*ra+1);
                              g.drawOval(xp-ra+2,yp-ra-2,2*ra-2,2*ra+2);
                              ra=(int)(dbhFactor*sk*st.tr[i].d/200); 
                              g.fillOval(xp-ra,yp-ra,2*ra,2*ra);                              
                             }                               
                          if (st.tr[i].habitat==true)
                             {g.setColor(Color.yellow); 
                              g.drawOval(xp-ra,yp-ra,2*ra,2*ra);
                              g.drawOval(xp-ra+1,yp-ra-1,2*ra-1,2*ra+1);
                              g.drawOval(xp-ra+2,yp-ra-2,2*ra-2,2*ra+2);
                              ra=(int)(dbhFactor*sk*st.tr[i].d/200); 
                              g.fillOval(xp-ra,yp-ra,2*ra,2*ra);
                              
                             } 
			}  
        
		} // end of for loop for drawing trees
// Plot Tree Numbers  
                if (plotTreeNumber==true) for(i=0; i<st.ntrees; i++){
                    if (st.tr[i].out < 0 || st.tr[i].out==st.year ){
		          xp=10+(int)((st.tr[i].x-xmin)*sk); 
			  yp=h-40-(int)((st.tr[i].y-ymin)*sk); 
			  ra=(int)(dbhFactor*2*sk*st.tr[i].d/200); 
			  g.setColor(Color.black);
                          g.drawString(st.tr[i].no,xp+ra,yp-ra);
                    }
                }

//------------------------------------------------------------------------
// Copy to JPEG
                
                if (doJPEG==true)
                {
                    try
                    {
                      int size = 0;
                      float quality = 1f;
                      ByteArrayOutputStream out = new ByteArrayOutputStream( 0xfff );
                      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
                      JPEGEncodeParam param;
                      param = encoder.getDefaultJPEGEncodeParam( img );
                      param.setQuality( quality, true );
                      encoder.encode( img, param );
//                      File file= new File(frame.workingDir, jpgFilename);
                      File file= new File(jpgFilename);
                      FileOutputStream fos = new FileOutputStream(file.getCanonicalPath());
                      fos.write( out.toByteArray() );
                      fos.close();
                      out.close();
                      doJPEG=false;
                      neuzeichnen();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e); 
                    }		 
                }
                
//        
// Zoom controll
                g.setColor(Color.red); 
                if (zoomStatus==1) g.drawString("select lower corner",50,h-5); 
                if (zoomStatus==2) g.drawString("select upper corner",50,h-5); 


	}
	/** method to thin by mouse */
	public void mousePressed (MouseEvent e)
	{	int i; 
		x=e.getX(); y=e.getY(); 
                if (zoomStatus>0){
                    if (zoomStatus==1) {
                        xlzoom=((x-10)/sk)+xmin; 
		        ylzoom=(-(y-h+40)/sk)+ymin; 
                    }
                    if (zoomStatus==2) {
                        xuzoom=((x-10)/sk)+xmin; 
		        yuzoom=(-(y-h+40)/sk)+ymin; 
                    }
                    zoomStatus=zoomStatus+1;
                    if (zoomStatus==3)zoomStatus=0;
                                         
                }
                else
                {    
		double xx,yy,emin,ent; 
		int merk=0; 
		xx=((x-10)/sk)+xmin; 
		yy=(-(y-h+40)/sk)+ymin; 
		emin=9999.9; 
		for (i=0; i<st.ntrees; i++)
		{	if (st.tr[i].out < 0 || st.tr[i].out==st.year) //only living trees can be thinned
		    {
		       ent=Math.pow((xx-st.tr[i].x),2.0)+Math.pow((yy-st.tr[i].y),2.0); 
			   if (ent>0 ) ent=Math.sqrt(ent); 
			   if (ent<=0) ent=0; 
			   if (emin>ent) {	emin=ent; merk=i; }
			}   
		}
                if (emin<1.0 && mouseThinning==true) { 
                    if (st.tr[merk].out < 0) {st.tr[merk].out=st.year; st.tr[merk].outtype=2;}
                    else {st.tr[merk].out=-1; st.tr[merk].outtype=0;}
//                    frame.updatetp(false);
                    st.notifyStandChanged("tree thinned",this);
                }
                if (emin<1.0 && mouseCropTree==true) { 
                    if (st.tr[merk].crop == true) st.tr[merk].crop=false; 
                    else st.tr[merk].crop=true;
//                    frame.updatetp(false);
                    st.notifyStandChanged("croptree selected",this);
                }
		repaint(); 
	}}
	/** empty methods */
	public void mouseReleased (MouseEvent e) {	}
	public void mouseEntered (MouseEvent e) {	}
	public void mouseExited (MouseEvent e) {	}
	public void mouseClicked (MouseEvent e) {	}
	/** method to renew the graph */
	public void neuzeichnen()	{	repaint();st.sortbyd(); }
        public void setStandMapInfo(boolean status) {standMapInfo=status; }
        public void setPlotCrownWidth(boolean status){plotCrownWidth=status; }
        public void setPlotTreeNumber(boolean status){plotTreeNumber=status;} 
        public void setDbhFactor(int f){dbhFactor=f;} 
        public void setMouseThinning(boolean status){mouseThinning=status;} 
        public void setMouseCropTree(boolean status){mouseCropTree=status;} 
       
        
        
	
	
        public void setJPGFilename(String fn){jpgFilename=fn;}
 
        
        void getJPEG(){
            doJPEG=true;
            neuzeichnen();
       }    
    
// end of class	
}
