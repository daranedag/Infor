/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StammvErstellen.java
 *
 * Created on 18.12.2009, 16:19:47
 */

package vis2006;

import Hilfsklassen.HeuteString;
import DatabaseManagement.DBConnection;
import java.awt.Color;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author sprauer
 */
public class StammvErstellen extends javax.swing.JDialog {
    DBConnection con = null;
    String pfad = null;
    String auf = null;
    String edvid = null;
    ArrayList<StammvBaum> baeume = new ArrayList();
    HashMap<String,StammvStandpunkt> standpunkte = new HashMap<String, StammvStandpunkt>();


    /** Creates new form StammvErstellen */
    public StammvErstellen(java.awt.Frame parent, boolean modal, DBConnection Con, 
            String Edvid, String Pfad, boolean anzeigen) {
        super(parent, modal);
        initComponents();
        con = Con;
        edvid = Edvid;
        pfad = Pfad;
        jTextField1.setText(pfad);

        if(anzeigen){
            Statement stmt = null;
            ResultSet rs = null;
            int nauf = 0;
            try{
                stmt = con.Connections[0].createStatement();
                rs = stmt.executeQuery("SELECT auf, jahr FROM Auf ORDER BY auf");
                while(rs.next()){
                    int aufx = rs.getInt("auf");
                    int jahrx = rs.getInt("jahr");
                    jComboBox1.addItem(aufx+" ("+jahrx+")");
                    nauf ++;
                }
            } catch (Exception e) {e.printStackTrace();
            } finally{
                try{
                    if(stmt!=null) rs.close();
                    if(stmt!=null) stmt.close();
                }catch(Exception e) {e.printStackTrace();}
            }
            setVisible(true);
        }
    }

    public String getPfad(){
        return pfad;
    }

    public boolean topconLesen(File datei, boolean txtSchreiben){
        boolean ok = false;
        String line = null;
        int nElement = 0;
        boolean stpunkt = false;
        BufferedReader buff =null;

        NumberFormat df=NumberFormat.getInstance();
        df=NumberFormat.getInstance(new Locale("en","US"));
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);

        String fehlertext="Unbekannter Fehler!";
        try{
            buff = new BufferedReader(new FileReader(datei));
            StammvStandpunkt stp = new StammvStandpunkt("",0.0,0.0,0.0,0.0);
            //  Zeilen einlesen
            int zaehler = 0;
            while((line = buff.readLine()) != null){
                zaehler ++;
                // Sicherstellen, dass zuerst ein Gerätestandpunkt definiert wird
                // sonst haben die ersten Bäume keinen Bezug
                if(zaehler==1 && !line.startsWith("Topcon Messwertdatei")){
                    JOptionPane.showMessageDialog (this, "Fehler im Dateiformat? \n"
                            + "Zu Beginn muss ein Gerätestandpunkt definiert sein: \n"
                            + "Topcon Messwertdatei \n"
                            + "STP...",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                nElement=0;
                StringTokenizer tok = new StringTokenizer(line,",");
                StammvBaum baumx = new StammvBaum();
                fehlertext = "Fehler beim Lesen folgender Zeile: \n"+line;
                while (tok.hasMoreTokens()){
                    String element = tok.nextToken().trim();
                    if(element.contains("`")){
                        element = element.substring(element.indexOf("`")+1);
                    }
                    if(element.equalsIgnoreCase("Topcon Messwertdatei")){
                        System.out.println("Neuer Standpunkt");
                        stpunkt = true;
                    } else if(stpunkt == true){
                        while(tok.hasMoreTokens()) element = element +" "+ tok.nextToken().trim();
                        String name = element.substring(0, element.indexOf(" "));
                        double geraeteH = Double.valueOf(element.substring(element.lastIndexOf(" "), (element.length()-1)));
                        stp = standpunkte.get(name);
                        if(stp!=null){
                            standpunkte.remove(name);
                            stp.hLinse = geraeteH;
                            standpunkte.put(name, stp);
                        }
                        else {
                            stp = new StammvStandpunkt(name, 0.0, 0.0, 0.0, geraeteH);
                            standpunkte.put(name, stp);
                        }
                        stpunkt=false;
                    }
                    else{
                        switch (nElement){
                            case 0 :
                                baumx.name = element;
                                break;
                            case 1 :
                                baumx.art = 0;
                                try{  // es können nur Zahlen übernommen werden -> Int-Feld in DB
                                    baumx.art = Integer.parseInt(element);
                                }catch (Exception ex){}
                                
                                break;
                            case 2 :
                                baumx.spiegelHoehe = Double.parseDouble(element.replace("m", ""));
                                break;
                            case 3 :
                                baumx.winkel = Double.parseDouble(element.replace("g", ""));
                                break;
                            case 4 :
                                baumx.entfernung = Double.parseDouble(element.replace("m", ""));
                                break;
                            case 5 :
                                baumx.zDifferenz = Double.parseDouble(element.replace("m", ""));
                                break;
                        }
                        nElement++;
                    }
                }
                if(!baumx.name.equals("")){
                    baumx.standpunkt = stp.name;
                    if(baumx.name.startsWith("E")) baumx.typ="Ecke";            // Ecke
                    if(baumx.name.substring(0, 1).equalsIgnoreCase("S")){       // Standpunkt 
                        StammvStandpunkt stpx = standpunkte.get(baumx.name);
                        if(stpx != null) standpunkte.remove(baumx.name);
                        // wenn Standpunkt noch nicht vorhanden
                        else stpx = new StammvStandpunkt(baumx.name, 0.0, 0.0, 0.0, 0.0);
                        baumx.calculateXY(stp.x, stp.y);
                        baumx.calculateZ(stp.z, stp.hLinse);
                        stpx.koordinatenSetzen(baumx.xwert, baumx.ywert, baumx.zwert);
                        standpunkte.put(baumx.name, stpx);
                   } else baeume.add(baumx);                                    // Regulärer Baum
                        
                }
            }
            fehlertext = "Fehler bei der Koordinatenberechnung.";
            // Koordinaten für alle nicht-S-Bäume berechnen
            // sind Standpunkte vorhanden?
            if (standpunkte.size() < 1) {
                JOptionPane.showMessageDialog (this, "Es wurde kein Gerätestandpunkt gefunden!",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            }else{
                StammvStandpunkt bezug = null;
                for (int i = 0; i < baeume.size(); i++) {
                    StammvBaum baumi = baeume.get(i);
                    if (!baumi.name.substring(0, 1).equalsIgnoreCase("S")) {
                        bezug = standpunkte.get(baumi.standpunkt);
                        baumi.calculateXY(bezug.x, bezug.y);

                        //System.out.println("12345678;" + baumi.name + "; 0;" + baumi.xwert + "; " + baumi.ywert);
                        baumi.calculateZ(bezug.z, bezug.hLinse);
                    }
                }

                Collection stps = standpunkte.keySet();
                Iterator it = stps.iterator();
                String text = "";
                while (it.hasNext()) {
                    int n = 0;
                    StammvStandpunkt stp2 = standpunkte.get(it.next().toString());
                    for (int i = 0; i < baeume.size(); i++) {
                        if (baeume.get(i).standpunkt.equals(stp2.name)) {
                            n++;
                        }
                    }
                    text = text + "Standpunkt " + stp2.name + " (Kordinaten: " + df.format(stp2.x) + ","
                            + df.format(stp2.y) + ") " + n + " Bäume \n";
                }
                JOptionPane.showMessageDialog(this, text, "Hinweis", JOptionPane.INFORMATION_MESSAGE);

                // xy-Koordinaten als txt ablegen
                if (txtSchreiben) {
                    koordsTxtSchreiben();
                }

                ok = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog (this, fehlertext,
                    "Fehler", JOptionPane.ERROR_MESSAGE);
        }finally{
            try{
                if(buff!=null) buff.close();
            } catch (Exception e) {e.printStackTrace();}
        }
        return ok;
    }

    public void anzeigen(){
        XYSeriesCollection dataset = new XYSeriesCollection();
        ArrayList<String> baumnrs = new ArrayList<String>();
        // Bäume, Ecken und andere Punkte
        XYSeries series = new XYSeries("Ecken");
        XYSeries series2 = new XYSeries("Bäume", false); // 15.6.2011 "false" unterbindet automatische Sortieren -> Problem mit Nummern
        for(int i = 0; i < baeume.size(); i++){
            StammvBaum baumx = baeume.get(i);
            if(baumx.typ.equals("Ecke")) series.add(baeume.get(i).ywert, baeume.get(i).xwert);
            else{
                //System.out.println(baumx.name + " Koordinaten: " + baumx.xwert + "," + baumx.ywert);
                series2.add(baumx.ywert, baumx.xwert);
                baumnrs.add(baumx.name);
            }
        }
        System.out.println("Anzahl Nummern: " + baumnrs.size());
        //System.out.println(" Serienlänge: " + series2.getItemCount());
        dataset.addSeries(series);
        dataset.addSeries(series2);


        // Gerätestandpunkte
        Collection stps = standpunkte.keySet();
        Iterator it = stps.iterator();
        XYSeries series3 = new XYSeries("Standpunkte");
        while(it.hasNext()){
            StammvStandpunkt stp = standpunkte.get(it.next().toString());
            series3.add(stp.y, stp.x);
        }
        dataset.addSeries(series3);

        JFreeChart chart = ChartFactory.createScatterPlot("",  // Title
                      "",           // X-Axis label
                      "",           // Y-Axis label
                      dataset,          // Dataset
                      org.jfree.chart.plot.PlotOrientation.HORIZONTAL,
                      true,                // Show legend
                      true,               // tooltip
                      false                // urls
                     );

        XYPlot plot = chart.getXYPlot();
        ValueAxis xax = plot.getDomainAxis();
        xax.setUpperMargin(0.1);
        xax.setLowerMargin(0.1);
        ValueAxis yax = plot.getRangeAxis();
        yax.setUpperMargin(0.1);
        yax.setLowerMargin(0.1);
        plot.setDomainAxis(xax);
        plot.setRangeAxis(yax);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false); // Ecken
        renderer.setSeriesShapesFilled(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesPaint(0, Color.GREEN);

        renderer.setSeriesLinesVisible(1, false); // Bäume u.a.
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesFilled(0, true);
        renderer.setSeriesPaint(1, Color.BLUE);

        renderer.setSeriesLinesVisible(2, false); // Gerätestandpunkte
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesPaint(2, Color.RED);
        plot.setRenderer(renderer);

        // Baumkoordinaten beschriften       
        XYItemRenderer rend = plot.getRenderer();
        rend.setSeriesItemLabelGenerator(1,new LabelGenerator(baumnrs));
        rend.setSeriesItemLabelsVisible(1, true);        
        plot.setRenderer(1,rend);


        ChartFrame frame = new ChartFrame("Stammverteilungsplan", chart);
        frame.setVisible(true);
        frame.setSize(500,500);

    }


     public void koordsTxtSchreiben(){
        PrintWriter ausgabe;

        NumberFormat df=NumberFormat.getInstance();
        df=NumberFormat.getInstance(new Locale("en","US"));
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);

        try {
            ausgabe = new java.io.PrintWriter(new FileWriter("stammv.txt"));
            ausgabe.println("Nr;Art;x;y;z");
            for(int i = 0; i < baeume.size(); i++){
                StammvBaum baumx = baeume.get(i);
                ausgabe.println(baumx.name+";"+baumx.art+";"
                        +df.format(baumx.xwert)+";"+df.format(baumx.ywert)+";"+df.format(baumx.zwert));
            }
            ausgabe.close();

        } catch (IOException ex) {
            Logger.getLogger(StammvErstellen.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Koordinaten als txt abgelegt");
    }


/**
     * A custom label generator.
    */ 
    static class LabelGenerator implements XYItemLabelGenerator {
         ArrayList<String> labels;
        /**
         * Creates a new generator that displays tree numbers as labels
        */ 
        public LabelGenerator(ArrayList<String> labels ) {
            this.labels = labels;
        }
        /**
         * Generates a label for the specified item. The label is typically a
         * formatted version of the data value, but any text can be used.
         *
         * @param dataset the dataset (<code>null</code> not permitted).
         * @param series the series index (zero-based).
         * @param category the category index (zero-based).
         *
         * @return the label (possibly <code>null</code>).
         */
        public String generateLabel(XYDataset dataset,
                int series,
                int category) {
            String result;
            result = labels.get(category);
            return result;
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Stammverteilungsplan erstellen");
        setLocationByPlatform(true);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("ok");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");

        jLabel1.setText("Topcon-Datei wählen");

        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setLabelFor(jComboBox1);
        jLabel2.setText("Nummer der Aufnahme");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(264, 264, 264)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    // Datei wählen
    JFileChooser fc = new JFileChooser(pfad);
    fc.setDialogTitle(" Topcon-Datei auswählen ");
    fc.setApproveButtonText("übernehmen");
    fc.showOpenDialog(this);
    File inputFile = fc.getSelectedFile();
    jTextField1.setText(inputFile.getAbsolutePath());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    // Stammverteilungsplan erstellen
    NumberFormat df=NumberFormat.getInstance();
    df=NumberFormat.getInstance(new Locale("en","US"));
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);
    df.setGroupingUsed(false);
    
    pfad = jTextField1.getText();
    String tempauf = jComboBox1.getSelectedItem().toString();
    auf = tempauf.substring(0,tempauf.indexOf(" ("));
    
    // Lesen
    File datei = new File(pfad);
    boolean ok;
    ok = topconLesen(datei, false);  // lesen, aber keine txt mit Koordinaten ablegen

    // Ergebnisse schreiben
    System.out.println("Ergebnisse schreiben");
    if(ok){
        HeuteString datum = new HeuteString();
        String datumStr = datum.get();
        Statement stmt = null;
        try{        
            stmt = con.Connections[0].createStatement();
            // Bäume einfügen
            for(int i = 0; i < baeume.size(); i++){
                StammvBaum baumi = baeume.get(i);
                stmt.execute("INSERT INTO Stammv (edvid, nr, art, auf, x, y, z, datum, Stempel ) " +
                        "VALUES('"+edvid+"','"+baumi.name+"',"+baumi.art+
                        ","+auf+","+df.format(baumi.xwert)+","+df.format(baumi.ywert)+
                        ","+df.format(baumi.zwert)+",'"+datumStr+"','"+datumStr+"');");
      
            }
            //Standpunkte einfügen
            Collection stps = standpunkte.keySet();
            Iterator it = stps.iterator();
            while(it.hasNext()){
                StammvStandpunkt stp2 = standpunkte.get(it.next().toString());               
                stmt.execute("INSERT INTO Stammv (edvid, nr, art, auf, x, y, z, datum, Stempel ) " +
                        "VALUES('"+edvid+"','"+stp2.name+"',"+0+
                        ","+auf+","+df.format(stp2.x)+","+df.format(stp2.y)+","+df.format(stp2.z)+",'"+datumStr+"','"+datumStr+"');");
            }
            System.out.println("fertig");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog (this, "Fehler beim Schreiben: "+e,
                    "Fehler", JOptionPane.ERROR_MESSAGE);
        }finally{
            try{
                if(stmt!=null) stmt.close();
            } catch (Exception e) {e.printStackTrace();}
        }
    }
    

    this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
