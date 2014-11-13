/** TreeGrOSS : class VisGrafik
   Version 05-May-2002 */
/*   (c) 2002 Juergen Nagel, Forest Research Station of  Lower Saxony, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@NW-FVA.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details
 */
package vis2006;
import treegross.base.*;
import org.jfree.chart.*;
import org.jfree.data.*;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ChartUtilities;
import java.io.*;


import java.awt.*;
import javax.swing.*;
import java.util.*;

/** draws a stand map and allows thinning by mouse click */
class VisGrafik extends JPanel 
{
    Stand st =new Stand();
    int graphType=1;
    int speciesCode=211;
/** width and height of the drawing area */
    public int w,h; 
//    public dvert dver= new dvert();
//    public heightdistrib hdist = new heightdistrib(); 
     ChartPanel chartPanel;
     JFreeChart jfreeChart = null;
     String preferredLanguage="en";
   

    public VisGrafik(Stand stl)
    {  
        st = stl;
        Dimension scr= Toolkit.getDefaultToolkit().getScreenSize(); 
        setPreferredSize(new Dimension(600,400));      
    }
	
    public void starten()
    {
        Dimension d=getSize();
	if (st.ntrees >0) 
	{
            GraphicDiameterDistribution chart = new GraphicDiameterDistribution( preferredLanguage);
            chartPanel = new ChartPanel(chart.createChart(st,speciesCode) );
            chartPanel.repaint();
            this.removeAll();
            this.add(chartPanel);
            this.repaint();
        }
 

    }

    public void drawGraph()
    {
        if (st.ntrees >0){
            if (graphType==0){
                GraphicSpeciesByCrownSurfaceArea chart = new GraphicSpeciesByCrownSurfaceArea(preferredLanguage);
                jfreeChart=chart.createChart(st);
                chartPanel = new ChartPanel(jfreeChart);
            }
            if (graphType==1){
                GraphicDiameterDistribution chart = new GraphicDiameterDistribution(preferredLanguage);
                jfreeChart=chart.createChart(st, speciesCode);
                chartPanel = new ChartPanel(jfreeChart);
                
            }
            if (graphType==2){
                GraphicDiameterDistributionCT chart = new GraphicDiameterDistributionCT(preferredLanguage);
                jfreeChart=chart.createChart(st);
                chartPanel = new ChartPanel(jfreeChart);
            } 
            if (graphType==3){
                VisGraphicHeightDiameterPlot chart = new VisGraphicHeightDiameterPlot();
                jfreeChart=chart.createChart(st, speciesCode);
                chartPanel = new ChartPanel(jfreeChart);
            } 
            chartPanel.setPreferredSize(new Dimension(600, 400));
            this.removeAll();
            this.add(chartPanel);
           
        }

    }
    
    public void  neuzeichnen(){
        drawGraph();
    }
    
    public void setGraphType(int i, int art){
        graphType=i;
        speciesCode=art;
    }
    
    public void saveToJPEG(String dir){
//        File fn= new File(dir, "grafik.jpg");
            
        File fn= new File(dir, "grafik"+graphType+".png");
        ChartUtilities ut = null;
        try{
//            ut.saveChartAsJPEG(fn ,jfreeChart,  800, 800);
            ut.saveChartAsPNG(fn ,jfreeChart,  400, 400);
        }
      	catch (Exception e){	System.out.println(e); } 


    }
}
//------------------------------------------------------ 
 class GraphicSpeciesByCrownSurfaceArea {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicSpeciesByCrownSurfaceArea(String preferredLanguage){
      Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("treegross.standsimulation.TgGrafik",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
   
  public JFreeChart createChart(Stand st) {
// create the dataset...
     DefaultPieDataset dataset = new DefaultPieDataset();
     for (int j=0;j<st.nspecies;j++){
           dataset.setValue(st.sp[j].spDef.shortName,  st.sp[j].percCSA);
     }
//     
     JFreeChart chart = ChartFactory.createPieChart(
        messages.getString("speciespercentage"), // chart title
        dataset, // data
        true, // include legend
        true, // tooltips?
        false // URLs?
       );
       PiePlot piePlot = (PiePlot) chart.getPlot();
       for (int i=0;i<st.nspecies;i++){
          piePlot.setSectionPaint(i, new Color(st.sp[i].spDef.colorRed,st.sp[i].spDef.colorGreen,st.sp[i].spDef.colorBlue));
     }
     return chart;
     
	} 
  
  public void redraw(){
 //     repaint();
  } 
  

   }
	
	
//------------------------------------------------------ 
 class GraphicDiameterDistribution {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicDiameterDistribution(String preferredLanguage)
  {   Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("treegross.standsimulation.TgGrafik",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
  
   
  public JFreeChart createChart(Stand st, int speciesCode) {
// create the dataset...
     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
     
// Durchmesserhäufigkeiten in 5-cm-Klassen bestimmen (Klassenmitten: 7.5 bis max. 152.5)
     for (int i=0;i< 30;i++){
         for (int j=0;j<3;j++){
             double anz=0;
             double gesamtZahl=0;
             for (int k=0;k<st.ntrees;k++){
                 if (speciesCode==st.tr[k].code && st.tr[k].fac > 0.0){
                 if ((st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5))
                        gesamtZahl= gesamtZahl+st.tr[k].fac;
                 if ((j==2)&&(st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0) 
                     && (st.tr[k].crop==true))
                        anz= anz+st.tr[k].fac;
                 if ((j==0)&&(st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0)
                     && (st.tr[k].crop==false)) 
                        anz= anz+st.tr[k].fac;
                 if ((j==1)&&(st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out > 0)) 
                        anz= anz+st.tr[k].fac;
                }
             }
             if (gesamtZahl > 0 ){
                     Integer m = (5*i)+2;
                     String textcode="";
                     if( j==0) textcode="verbleibend";
                     if( j==1) textcode="ausscheidend";
                     if( j==2) textcode="Z-Bäume";
                     dataset.addValue(anz/st.size,  // Anzahl pro ha
                             textcode,              // Gruppe
                             m.toString()+".5");    // Durchmesserklassenmitte
   
             }
         }
     }

//     
     JFreeChart chart = ChartFactory.createStackedBarChart(
        messages.getString("diameterDistribution"), // chart title
        messages.getString("dbhClass"), // domain axis label
        messages.getString("numberOfStems"), // range axis label
        dataset, // data
        PlotOrientation.VERTICAL, // orientation
        true, // include legend
        true, // tooltips?
        false // URLs?
       );
     CategoryPlot plot = chart.getCategoryPlot();
//     plot.setBackgroundPaint(Color.lightGray);
//     plot.setDomainGridlinePaint(Color.white);
//     plot.setDomainGridlinesVisible(true);
//     plot.setRangeGridlinePaint(Color.white);
// reenderer
     StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
     renderer.setDrawBarOutline(false);
// set up gradient paints for series...
     renderer.setSeriesPaint(0,Color.GREEN);
     renderer.setSeriesPaint(1,Color.RED);
     renderer.setSeriesPaint(2,Color.BLUE);
     //renderer.setMaxBarWidth(0.15);
     renderer.setMaximumBarWidth(0.15);
     
     if(dataset.getColumnCount() >10){
         CategoryAxis xAxis = (CategoryAxis)plot.getDomainAxis();
         if (dataset.getColumnCount() < 21) xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);     
         else xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
     }

        
     return chart;
	} 
  
  public void redraw(){
 //     repaint();
  } 
   }
 
// -------------------------------------------------------
  class GraphicDiameterDistributionCT {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicDiameterDistributionCT(String preferredLanguage)
  {   Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("treegross.standsimulation.TgGrafik",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
   
  public JFreeChart createChart(Stand st) {
// create the dataset...
     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
     for (int i=0;i< 25;i++){
         for (int j=0;j<st.nspecies;j++){
             double anz=0;
             for (int k=0;k<st.ntrees;k++){
                 if ((st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0) 
                     && st.tr[k].code==st.sp[j].code && st.tr[k].crop==true) anz= anz+st.tr[k].fac;
                 
             }
             if (anz > 0 ){
                  Integer m = (5*i)+2;
                  dataset.addValue(anz*st.size, st.sp[j].spDef.shortName, m.toString());
                 
             }
         }
     }
//     
     JFreeChart chart = ChartFactory.createStackedBarChart(
        messages.getString("diameterDistributionCT"), // chart title
        messages.getString("dbhClass"), // domain axis label
        messages.getString("numberOfStems"), // range axis label
        dataset, // data
        PlotOrientation.VERTICAL, // orientation
        true, // include legend
        true, // tooltips?
        false // URLs?
       );
     CategoryPlot plot = chart.getCategoryPlot();
     plot.setBackgroundPaint(Color.lightGray);
     plot.setDomainGridlinePaint(Color.white);
     plot.setDomainGridlinesVisible(true);
     plot.setRangeGridlinePaint(Color.white);
// reenderer
     StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
     renderer.setDrawBarOutline(false);
// set up gradient paints for series...
     for (int i=0;i<st.nspecies;i++){
          renderer.setSeriesPaint(i, new Color(st.sp[i].spDef.colorRed,st.sp[i].spDef.colorGreen,st.sp[i].spDef.colorBlue));
     }

      
     return chart;
	} 
  
  public void redraw(){
 //     repaint();
  } 
   }
// -------------------------------------------------------
  class VisGraphicHeightDiameterPlot {
   JFreeChart chart;

   public VisGraphicHeightDiameterPlot()
  {   
  }  
  public JFreeChart getChart(){
      return chart;
  }
   
  public JFreeChart createChart(Stand st, int speciesCode) {
// create the dataset...
     int code=speciesCode; 
     int merk=0; 
     for (int i=0; i< st.nspecies;i++)
         if (st.sp[i].code==code ) merk=i;
     XYSeriesCollection dataset = new XYSeriesCollection();

// Werte der Höhenkurve
     XYSeries series0 = new XYSeries("Höhenkurve: "+st.sp[merk].heightcurveUsed);
     for (int k=0;k<st.ntrees;k++){
            if ((st.tr[k].d > 0) && (st.tr[k].h > 0) && st.tr[k].code==code && st.tr[k].fac > 0.0) 
                    series0.add(st.tr[k].d,st.tr[k].h);             
             }
         dataset.addSeries(series0);
         
// Messwerte aus der Versuchsfläche
     XYSeries series1 = new XYSeries("Messwerte");
     for (int k=0;k<st.ntrees;k++){
            if ((st.tr[k].d > 0) && (st.tr[k].hMeasuredValue > 0) && st.tr[k].code==code && st.tr[k].fac > 0.0) 
                    series1.add(st.tr[k].d,st.tr[k].hMeasuredValue);
             }
     dataset.addSeries(series1);

// Andere Messwerte (HG, nurH) 
     XYSeries series2 = new XYSeries("Andere Höhen");
     for (int k=0;k<st.ntrees;k++){
            if ((st.tr[k].d > 0) && (st.tr[k].hMeasuredValue > 0) && st.tr[k].code==code 
                    && (st.tr[k].no.contains("nurH") || st.tr[k].no.contains("HG") )) 
                series2.add(st.tr[k].d,st.tr[k].hMeasuredValue);
             }
     dataset.addSeries(series2);

/*   Wozu ist die gut?
     double dmin=9000;
     double dmax=-9000;
     for (int k=0;k<st.ntrees;k++){
         if (st.tr[k].code==st.sp[merk].code && st.tr[k].fac > 0.0){
            if (dmax <st.tr[k].d ) dmax=st.tr[k].d;
            if (dmin >st.tr[k].d ) dmin=st.tr[k].d;
             }
     }

     XYSeries series3 = new XYSeries("");
     int idmin = (int)(Math.round(dmin));
     int idmax = (int)(Math.round(dmax))+1;
     for (int k=idmin;k<=idmax;k++){
         double dwert=k*1.0;
         if (st.sp[merk].heightcurveUsed.indexOf("Einheits")>-1){
            UniformHeight uh = new UniformHeight();
            series3.add(dwert,uh.height(st.sp[merk],dwert,
                         st.sp[merk].dg,st.sp[merk].hg,st));        
         }
         else {
           HeightCurve hc =new HeightCurve(); 
           series3.add(dwert,hc.getHeight(st.sp[merk].spDef.heightCurve,dwert,st.sp[merk].heightcurveUsedP0,
                         st.sp[merk].heightcurveUsedP1,st.sp[merk].heightcurveUsedP2));
         }
       }
         dataset.addSeries(series3);
 */
//     
      JFreeChart chart = ChartFactory.createScatterPlot(
	     "Höhenkurve  "+ st.sp[merk].spDef.longName,
	     "BHD [cm]",
	     "Höhe [m]",
	     dataset,
             org.jfree.chart.plot.PlotOrientation.VERTICAL,
             true,   //legend
	     false,  // tooltips
             false); // urls
     XYPlot plot = chart.getXYPlot();
     plot.setDomainCrosshairVisible(true);
     plot.setRangeCrosshairVisible(true);
     plot.getDomainAxis().setAutoRangeMinimumSize(1.0);
     plot.getRangeAxis().setAutoRangeMinimumSize(5.0);
    
  
//
     XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
     
     renderer.setSeriesLinesVisible(0, true); // Höhenkurve - rote Linie
     renderer.setSeriesStroke(0, new BasicStroke(2));
     renderer.setSeriesShapesVisible(0, false);
     renderer.setSeriesPaint(0, Color.RED);
    
     renderer.setSeriesLinesVisible(1, false); // Parzellenmesswerte - blaue Symbole
     renderer.setSeriesShapesVisible(1, true);
     renderer.setSeriesPaint(1, Color.BLUE);
     
     renderer.setSeriesLinesVisible(2, false);  // Andere Messwerte - blaue leere Symbole
     renderer.setSeriesShapesVisible(2, true);
     renderer.setSeriesShapesFilled(2, false);
     renderer.setSeriesPaint(2, Color.BLUE);
     
//     renderer.setSeriesLinesVisible(3, true);  
//     renderer.setSeriesShapesVisible(3,false);
//     renderer.setSeriesPaint(3, Color.BLUE);    
//   renderer.setSeriesPaint(1, new Color(st.sp[merk].spDef.colorRed,st.sp[merk].spDef.colorGreen,st.sp[merk].spDef.colorBlue));

     plot.setRenderer(renderer);
     System.out.println("Plot done");

      
     return chart;
	} 
  
  public void redraw(){
 //     repaint();
  } 
   }





