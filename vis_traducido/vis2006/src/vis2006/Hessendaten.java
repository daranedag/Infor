/*
 * Hessendaten.java
 *
 * Created on 30. M�rz 2006, 11:10
 */

package vis2006;
import java.io.*;
import java.util.*;
import DatabaseManagement.*;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author  nagel
 */
public class Hessendaten extends javax.swing.JDialog {
    String id="";
    String lokaleDB = "";
    HessenFileInfo hfi[] = new HessenFileInfo[400];
    int nhfi=0;
    javax.swing.table.DefaultTableModel data= new javax.swing.table.DefaultTableModel(
            new Object [][] {  },
            new String [] {
               "Datos", "Parcela", "A�o", "Temporada", "Edad",  "Tipo", "cm", "Alturas", "ExtraH","ZB", "lectura"
            }
        );
    Object[] rowData={" "," "," "," "," "," "," "," "," "," "," "};
// Protokoll
    
    /** Creates new form Hessendaten */
    public Hessendaten(java.awt.Frame parent, boolean modal,String idSelected, String lokaleDB) {
        super(parent, modal);
        initComponents();
        this.id= idSelected;
        this.lokaleDB = lokaleDB;
        jLabel1.setText("ID Seleccionada : "+id);
        setVisible(true);
        nhfi=0;
        jButton2.setEnabled(false);

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lectura de datos Hessen");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("jLabel1");
        jPanel3.add(jLabel1);

        jLabel2.setText("   hess. Intento n�mero: ");
        jPanel3.add(jLabel2);

        jTextField1.setText("2122");
        jTextField1.setMinimumSize(new java.awt.Dimension(31, 19));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField1);

        getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setAutoscrolls(true);

        jTable1.setModel(data);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Datos pre-comprobados");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setText("en conformidad con la Tabla");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-683)/2, (screenSize.height-613)/2, 683, 613);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
 // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
       dbconn.openDBConnection(DBConnection.ACCESS, 1, lokaleDB, "", "", false, true);
       StringTokenizer stx;
       String delim=";";
       HessenArten hessenArten = new HessenArten();
       int maxid = 0;
       String s = "";
       try {
          Statement stmt = dbconn.Connections[1].createStatement(); 
          String myparz="-999";
          String myparzvor ="";
          String mymonat="3";
          int myjahralt=-99;
          String myZF="";
          String mytyp="H";
          String mymonatalt="X";
          int myauf=0;
          int md =0;
//          for (int i=0;i<nhfi; i++)
//          stmt.execute("ALTER TABLE Baum id PRIMARY KEY ");

//          stmt.execute("ALTER TABLE Baum DROP column id ");
//
//          stmt.execute("ALTER TABLE Baum add column id INT PRIMARY KEY  ");
          try {
             stmt.execute("DROP INDEX edvid on Baum");
          }
          catch (Exception e){  System.out.println("Ning�n �ndice de base de datos :"+e); }
          stmt.execute("CREATE INDEX edvid ON Baum (edvid, auf) ");
          for (int i=0;i<nhfi; i++)
              
             if (hfi[i].einlesen.indexOf("ja")>-1){
                System.out.println(i+". File");
// Parz Daten einf�gen           
                String mybemerk="";   // "Hessendaten"
                if (myparz.compareTo(hfi[i].parzelle)!=0) {
                    myparz=hfi[i].parzelle.trim();
                    mytyp="H";
                    if (myparz.length() < 2) 
                        myparzvor="0";
                    if (myparz.length() >= 2) {
                        myparz=myparz.substring(0,2);
                        myparzvor="";
                        
                    } 
                    myauf=0;
                    stmt.execute("INSERT INTO Parz "+
                               "(id, edvid , parzelle, bhdfile, hohfile, insfile, behandl, flache," +
                               " bemerk, stand, bearbeiter, stvp, nkro, PZ) "+
                               "VALUES  ('"+id+myparzvor+myparz+"', '"+id+"', '"+myparzvor+myparz+"', '', 0, ''," +
                               " '', '', '"+mybemerk+"', '',  '', 0, 0, 0)");                  
                    
                }
                if (hfi[i].zeit.indexOf("F")>-1) mymonat="3"; else mymonat="10";
                Double fl = hfi[i].flha;
                //if (hfi[i].typ.indexOf("U")>-1) mybemerk="Umfangmessband, Hessendaten";
                //              else  mybemerk="Kluppung, Hessendaten";
                mybemerk = "Daten Hessen";
                if (hfi[i].extraH.indexOf("ja")<0 && hfi[i].nHoehen<=0) mybemerk="keine H�hen,"+mybemerk;
                if (myjahralt != hfi[i].jahr || mymonatalt.compareTo(mymonat)!=0){
                    myauf=myauf+1;
                    stmt.execute("INSERT INTO Auf  "+
                               "( id, edvid , auf, monat, jahr, flha, qualitat, typ, messh, repfac, bemerk, PZ) "+
                               "VALUES  ('"+id+myparzvor+myparz+myauf+"', '"+id+myparzvor+myparz+"', "+myauf+", "+mymonat+", "+hfi[i].jahr+", "+fl.toString()+
                               ", -1, '"+mytyp+"', '', '', '"+mybemerk+"', 0 )");
                    mymonatalt=mymonat;
                    myjahralt=hfi[i].jahr;
                }
 //               stmt.close();
// Einzelbaumdaten
                BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(hfi[i].pfadDatei)));
// 29 Zeilen �berlesen
//                for( int j=0;j<30;j++) s=in.readLine();
            do {
                s=in.readLine();
                int m = s.indexOf("ungen");
                int mm=m;
            } while (s.indexOf("UNGEN")<0  && s.indexOf("ungen")<0);

// Baumwerte einlesen
                int nn=0;
                int cmanzahl=0;
                int cmartcode=-99;
                int cmd=-9;
                int cmmh=-9;
                int cmauf=0;
                String cmausge="-77";
                while (true){
                   s=in.readLine();
                   if (s == null || s.length() < 10) break;
                   s=ascii2(s);
                   stx = new StringTokenizer(s,delim,false);
                   String nr = stx.nextToken();
                   String art = stx.nextToken();
                   String kraft = stx.nextToken();
                   String d1s = stx.nextToken();
                   String d2s = stx.nextToken();
                   String mhs = stx.nextToken();
                   String aus = stx.nextToken();
                   String hs = stx.nextToken();
                   String kas = stx.nextToken();
                   String zf = stx.nextToken();
                   String bemerk = stx.nextToken();
                   int artcode=hessenArten.getCode(art); 
                   double d1=0;double d2=0;double ka=0;
                   double h =0; int mh = 13;
                   if(d1s.compareTo(" ")!=0) d1 = Double.parseDouble(d1s)*10;
                   if(d2s.compareTo(" ")!=0) d2 = Double.parseDouble(d2s)*10;
                   if(hs.compareTo(" ")!=0) h = Double.parseDouble(hs)*10;
                   if(kas.compareTo(" ")!=0) ka = Double.parseDouble(kas)*10;
                   if(kas.compareTo(" ")!=0) mh = Integer.parseInt(mhs)*10;
                   if (mh==10) mh=13;
                   String ausge = " ";
                   if (aus.indexOf("0")<0) ausge="1";
                   bemerk=zf+bemerk;
                   myZF="";
                   if (hfi[i].nZBaum>0) myZF="F";
                   if (zf.indexOf("Z")>=0) myZF="Z";
                   nn=nn+1;
                   int myanzahl=1;
                   if (hfi[i].cm.indexOf("ja")>= 0) nr="cm";
                   if (bemerk.indexOf("H�he") >= 0 ){
                       nr="nurH";
                       myanzahl=0;}
                   
                   if (nr.indexOf("cm")>-1 || cmanzahl>0){
                       if (cmanzahl > 0 && (cmd!=(int)d1 || cmausge.compareTo(ausge)!=0 || cmartcode!=artcode)) {
                          Statement stmt2 = dbconn.Connections[1].createStatement(); 
                          md=md+1;
                          maxid = maxid + 1;
                          stmt2.execute("INSERT INTO Baum  "+
                               "(id, edvid , auf, nr, nralt, art, anzahl, repfl, alt, r, zf, ou, schicht, sonder, kraft, dkrz1, dkrz2, dmess, mh, "+
                               "d, a, e, h, k, hbr, g0, g5, g10, g15, g20, g25, g30, g35, hmaxkb, hastfrei, sfg, sfe, bemerk ) "+
                               "VALUES  ( "+maxid+", '"+id+myparzvor+myparz+"', "+myauf+", 'cm', '', "+cmartcode+", "+cmanzahl+", 1.0 , "+hfi[i].alter+
                               ", ' ', '', '' , -1, "+
                               " ' ', 0 , 0, 0, "+cmd+", "+cmmh+", 0, '"+cmausge+"', ' ', 0, 0 "+
                               ", '0' , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ' ' )");
                               stmt2.close();
                               cmanzahl=0;
                        }
                       cmartcode=artcode;
                       if(nr.indexOf("cm")>-1) cmanzahl=cmanzahl+1;
                       cmd=(int) d1;
                       cmmh=mh;
                       cmausge=ausge;
                       cmauf =myauf;
                       
                   }
//                   System.out.println("In die Baum"+nn);
                   int dk1 = 0;
                   int dk2 = 0;
                   if (d2 > 0) {
                       dk1 = (int)Math.round(d1);
                       dk2 = (int)Math.round(d2);
                       d1 = Math.round((d1+d2)/2.0);
                   }
                   if (nr.indexOf("cm")<0){
                      Statement stmt2 = dbconn.Connections[1].createStatement(); 
                      md=md+1;
                      maxid = maxid + 1;
                      stmt2.execute("INSERT INTO Baum  "+
                               "(  id, edvid , auf, nr, nralt, art, anzahl, repfl, alt, r, zf, ou, schicht, sonder, kraft, dkrz1, dkrz2, dmess, mh, "+
                               " d, a, e, h, k, hbr, g0, g5, g10, g15, g20, g25, g30, g35, hmaxkb, hastfrei, sfg, sfe, bemerk ) "+
                               "VALUES  ( "+maxid+", '"+id+myparzvor+myparz+"', "+myauf+", '"+nr+"', '', "+artcode+", "+myanzahl+"  , 1.0 , "+hfi[i].alter+
                               ", ' ', '"+myZF+"', '' , -1, "+
                               " '"+kraft+"', 0 , "+dk1+", "+dk2+", "+((int)d1)+", "+mh+", 0, '"+ausge+"', ' ', "+((int)h)+", "+((int)ka)+
                               ", '0' , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '"+bemerk+"' )");
                    stmt2.close();
                    }
                   
                   
            }
// noch vorhandene cm auschreiben
            if (cmanzahl>0){
              Statement stmt2 = dbconn.Connections[1].createStatement();
              md=md+1;
                          System.out.println("end");
              maxid = maxid + 1;
                          
              stmt2.execute("INSERT INTO Baum  "+
                   "( id, edvid , auf, nr, nralt, art, anzahl, repfl, alt, r, zf, ou, schicht, sonder, kraft, dkrz1, dkrz2, dmess, mh, "+
                   "d, a, e, h, k, hbr, g0, g5, g10, g15, g20, g25, g30, g35, hmaxkb, hastfrei, sfg, sfe, bemerk ) "+
                   "VALUES  (  "+maxid+", '"+id+myparzvor+myparz+"', "+myauf+", 'cm', '', "+cmartcode+", "+cmanzahl+", 1.0 , "+hfi[i].alter+
                   ", ' ', '"+myZF+"', '' , -1, "+
                   " ' ', 0 , 0, 0, "+cmd+", "+cmmh+", 0, '"+cmausge+"', ' ', 0, 0 "+
                   ", '0' , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ' ' )");
                stmt2.close();
                cmanzahl=0;
            }

            in.close();
// Ende                
             }
       }
       catch (Exception e){  System.out.println("Error de base de datos :"+s+e); }
       dbconn.closeAll();
       this.dispose();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// Verzeichnis w�hlen
        jButton2.setEnabled(true);

        java.io.File f = new java.io.File("");
        String localPath="";
        try{
           localPath=  f.getCanonicalPath();
        }
        catch (Exception e){};
        
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setCurrentDirectory(new File(localPath+"//Hessendaten"));
        fc.setDialogTitle(" Seleccione un directorio con datos Hessen de un laboratorio");
        fc.setApproveButtonText("Tomar el control");
      
        fc.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        int k=fc.showOpenDialog(this);
        java.io.File verzeichnis = fc.getSelectedFile();
        String inOrdner = verzeichnis.getAbsolutePath();        
// Liste mit Dateien erstellen 
        String entries[]=verzeichnis.list();
        for ( int i = 0; i < entries.length; i++ ) {
             if (entries[i].indexOf("."+jTextField1.getText())>0) readHessenFileInfo(inOrdner,entries[i]);
        }
// Sortieren  nach Jahr  
        HessenFileInfo hfiTemp =new HessenFileInfo();
        //for ( int i = 0; i < nhfi-1; i++ ) System.out.println(":"+hfi[i].parzelle+" "+hfi[i].file);
        for ( int i = 0; i < nhfi-1; i++ ) {
            for ( int j = i; j < nhfi; j++ ) {
                if (hfi[i].parzelle.compareTo(hfi[j].parzelle) > 0){
                    hfiTemp=hfi[i];
                    hfi[i]=hfi[j];
                    hfi[j]=hfiTemp;
                }
            }
        }
// Sortieren  nach Jahr  
        for ( int i = 0; i < nhfi-1; i++ ) {
            for ( int j = i; j < nhfi; j++ ) {
                if (hfi[i].parzelle.compareTo(hfi[j].parzelle)==0 && hfi[i].jahr > hfi[j].jahr){
                    hfiTemp=hfi[i];
                    hfi[i]=hfi[j];
                    hfi[j]=hfiTemp;
                }
            }
        }
        
// Liste schreiben
       try { 
       OutputStream os=new FileOutputStream("ProtokollHessenFile.txt"); 
       PrintWriter out= new PrintWriter(
	new OutputStreamWriter(os)); 
        for ( int i = 0; i < nhfi; i++ ) {
            data.addRow(rowData);
            out.println(hfi[i].file+" "+hfi[i].parzelle+" "+hfi[i].aufNr+" "+hfi[i].jahr+" "+hfi[i].zeit+" "+hfi[i].typ);
            jTable1.setValueAt(hfi[i].file, i,0);
            jTable1.setValueAt(hfi[i].parzelle, i,1);
            jTable1.setValueAt(hfi[i].jahr, i,2);
            jTable1.setValueAt(hfi[i].zeit, i,3);
            jTable1.setValueAt(hfi[i].alter, i,4);
            jTable1.setValueAt(hfi[i].typ, i,5);
            jTable1.setValueAt(hfi[i].cm, i,6);
            jTable1.setValueAt(hfi[i].nHoehen, i,7);
            jTable1.setValueAt(hfi[i].extraH, i,8);
            jTable1.setValueAt(hfi[i].nZBaum, i,9);
            jTable1.setValueAt(hfi[i].einlesen, i,10);
        }
       out.close();
       }
       catch (Exception e) {System.out.println(e); }       

    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    private void readHessenFileInfo(String path, String filename){
        System.out.println("Hessenfile"+filename);
        hfi[nhfi] = new HessenFileInfo();
        hfi[nhfi].file=filename;
        hfi[nhfi].pfadDatei=path+"//"+filename;
        readHessenFile(path, filename);
        nhfi=nhfi+1;
        
    }
    
    private void readHessenFile(String path, String fn){
     	char c = 34;
        String ss = String.valueOf(c);
        HessenArten hessenArten = new HessenArten();
        String s = null;
        try{
            BufferedReader in=
            new BufferedReader(
            new InputStreamReader(
            new FileInputStream(path+"//"+fn)));
            try{
               s=in.readLine();
               s=in.readLine();s=in.readLine();
               s=ascii(in.readLine());
               if (s.length()==0) s="0";
               hfi[nhfi].alter=Integer.parseInt(s);
               s=ascii(in.readLine());
               if (s.length()==0) s="0";
               hfi[nhfi].aufNr=Integer.parseInt(s);
               s=in.readLine();
               s=in.readLine();
               s=in.readLine();
               s=in.readLine();
               s=in.readLine();
               hfi[nhfi].parzelle=ascii(in.readLine());
               hfi[nhfi].typ=ascii(in.readLine());
               s=in.readLine();
               hfi[nhfi].flha=Double.parseDouble(ascii(in.readLine()));
               hfi[nhfi].jahr=Integer.parseInt(ascii(in.readLine()));
               hfi[nhfi].zeit=ascii(in.readLine());
               hfi[nhfi].mh=Integer.parseInt(ascii(in.readLine()));
               hfi[nhfi].extraH="nein";
               hfi[nhfi].nHoehen=0;
               hfi[nhfi].einlesen="ja";
               hfi[nhfi].cm="ja";
               hfi[nhfi].nZBaum=0;
               s=in.readLine();
// 2 Zeilen �berlesen
//            for( int i=0;i<12;i++)
//               s=in.readLine();
               do {
                s=in.readLine();
                int m = s.indexOf("ungen");
               } while (s.indexOf("UNGEN")<0  && s.indexOf("ungen")<0);
            }
            catch (Exception e) {System.out.println(s + e); }   

// Baumwerte einlesen
            StringTokenizer stx;
            String delim=";";
            while (true){
                s=in.readLine();
                if (s == null || s.length() < 10) break;
                s=ascii2(s);
                stx = new StringTokenizer(s,delim);
                String nr = stx.nextToken();
                String art = stx.nextToken();
                String kraft = stx.nextToken();
                String d1s = stx.nextToken();
                String d2s = stx.nextToken();
                String mhs = stx.nextToken();
                String aus = stx.nextToken();
                String hs = stx.nextToken();
                String ka = stx.nextToken();
                String zf = stx.nextToken();
                String bemerk = stx.nextToken();
                if (hessenArten.getCode(art)==0) { System.out.println("Falta tipo de �rbol: "+art);
                    JTextArea about = new JTextArea("Tipo de arbol :"+art+" en Archivo HessenArten.txt no est� definido");
                    JOptionPane.showMessageDialog(this, about, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
                    jButton2.setEnabled(false);

                    break;
                }
                double d1=0;
                double h =0;
                if(d1s.compareTo(" ")!=0) d1 = Double.parseDouble(d1s);
                if(hs.compareTo(" ")!=0) h = Double.parseDouble(hs);
                if( h>0 && d1 > 0 ) hfi[nhfi].nHoehen=hfi[nhfi].nHoehen+1;
                if(d1 >0 && d1s.indexOf(".")>-1  && zf.indexOf("H�he")<0) 
                          hfi[nhfi].cm="nein";
                if( zf.indexOf("H�he")>=0) hfi[nhfi].extraH="ja";
                if( zf.indexOf("Z")>=0) hfi[nhfi].nZBaum=hfi[nhfi].nZBaum+1;
//                System.out.println(nr);
            }
             
            
            
            
        }
        catch (Exception e) {System.out.println(s + e); }   
//        String test="test";

    }
    
    private String ascii( String s){
        char c[] = s.toCharArray();
        String sn="";
        for (int i=0;i<s.length();i++)
            if (c[i] != 34) sn=sn+c[i];
        sn=sn.replaceAll(",", ".");
        return sn;
    }

    private String ascii2( String s){
        char c[] = s.toCharArray();
        String sn="";
        for (int i=0;i<s.length();i++)
            if (c[i] != 34) sn=sn+c[i]; else sn=sn+"&";
        sn=sn.replaceAll("&&", "& &");
        sn=sn.replaceAll("&,", ";");
        sn=sn.replaceAll("&", "");
        sn=sn.replaceAll(",", ".");
        return sn;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
}