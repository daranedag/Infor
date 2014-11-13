/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.base;

import java.awt.Color;

/**
*
* @author jhansen
*/
public class CrownProjection {
      private double factor=1;
   private Stand st=null;
   private int imgsize=500;
   private double maxx, minx, maxy, miny;
      /** Creates a new instance of CrownProjection */
   public CrownProjection() {
   }
      private double findMinX(){
       double result=0;
       if(st.ntrees > 0){
           result = st.tr[0].x;
           for(int i=1; i< st.ntrees; i++){
               if(st.tr[i].x < result) result=st.tr[i].x;
           }                   }
       if(st.ncpnt >2){
           for(int k=0; k < st.ncpnt; k++){
               if(st.cpnt[k].x < result) result=st.cpnt[k].x;
           }
       }
       return result;
   }
      private double findMinY(){
       double result=0;
       if(st.ntrees > 0){
           result = st.tr[0].y;
           for(int i=1; i< st.ntrees; i++){
               if(st.tr[i].y < result) result=st.tr[i].y;
           }
       }
       if(st.ncpnt >2){
           for(int k=0; k < st.ncpnt; k++){
               if(st.cpnt[k].y < result) result=st.cpnt[k].y;
           }
       }
       return result;
   }
      private double findMaxX(){
       double result=0;         if(st.ntrees > 0){
           result = st.tr[0].x;
           for(int i=1; i< st.ntrees; i++){
               if(st.tr[i].x > result) result=st.tr[i].x;
           }
       }
       if(st.ncpnt >2){
           for(int k=0; k < st.ncpnt; k++){
               if(st.cpnt[k].x > result) result=st.cpnt[k].x;
           }
       }
       return result;
   }
      private double findMaxY(){
       double result=0;
        if(st.ntrees > 0){
           result = st.tr[0].y;
           for(int i=1; i< st.ntrees; i++){
               if(st.tr[i].y > result) result=st.tr[i].y;
           }
       }
       if(st.ncpnt >2){
           for(int k=0; k < st.ncpnt; k++){
               if(st.cpnt[k].y > result) result=st.cpnt[k].y;
           }
       }
       return result;
   }
       private double findMaxCrown(){
       double result=0;
        if(st.ntrees > 0){
           result = st.tr[0].cw;
           for(int i=1; i< st.ntrees; i++){
               if(st.tr[i].cw > result) result=st.tr[i].cw;
           }
       }
       return result;
   }
       private void paintCrowns(java.awt.Graphics2D g){
       g.setColor(Color.white);
       g.fillRect(0,0, imgsize, imgsize);
       g.setColor(Color.black);
       double pkdurchmesser=0;
       double diffx=maxx-minx;
       double diffy=maxy-miny;
       if(diffx>=diffy) pkdurchmesser=diffx;
       else pkdurchmesser= diffy;
       factor=imgsize/pkdurchmesser;
         for  (int i=0; i < st.ntrees; i++) {
           if(st.tr[i].out==-1){
               int x= (int)((st.tr[i].x)*factor);
               int y =(int)(imgsize-((st.tr[i].y)*factor));
               int d=(int)(st.tr[i].cw*factor);
               g.fillOval(x-d/2,y-d/2, d,d);
           }
         }
       }
      private double getPercentageCovered(){
      double result=0.0;
      int counter=0;
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(imgsize,
                           imgsize, java.awt.image.BufferedImage.TYPE_INT_RGB);
       java.awt.Graphics2D g = image.createGraphics();
       paintCrowns(g);
       g.dispose();
       double factor_m2=factor*factor;
       for(int x=0; x<image.getWidth(); x++){
           for(int y=0; y<image.getHeight(); y++){
               int c = image.getRGB(x,y);
               int  red = (c & 0x00ff0000) >> 16;
               int  green = (c & 0x0000ff00) >> 8;
               int  blue = c & 0x000000ff;
               if(red==0 && green==0 && blue==0) counter++;
           }
       }
       result=counter/factor_m2;
       //System.out.println("covered area [m²] in "+st.year+": "+result);
       result=result/(st.size*10000);
       if(result>1) result=1.0;
       image.flush();          //System.out.println("stand "+st.standname.trim()+" covered area[%] in "+st.year+": "+result);
       return result;
  }
      public double getCrownProjectionPercentage(Stand st){
       double result=0;
       this.st=st;
       maxx=findMaxX();
       maxy=findMaxY();
       minx=findMinX();
       miny=findMinY();
       result=getPercentageCovered();
       return result;
   }
}