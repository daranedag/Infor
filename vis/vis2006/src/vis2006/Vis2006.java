/*
 * VIS2006.java
 *
 * Created on 14. Januar 2005, 15:40
 */

package vis2006;
import Hilfsklassen.LastModificationTime;
import Hilfsklassen.HeuteString;
import treegross.base.*;
//import treegross.standsimulation.*;
import java.io.*; 
import java.text.*;
import javax.swing.*;

import DatabaseManagement.*;
import Hilfsklassen.ExcelDateiWählen;
import java.sql.*;
import java.util.*;
import nwfva.stammverteilung.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.HtmlWriter;
//import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Cursor;



/**
 *
 * @author  nagel
 */
public class Vis2006 extends javax.swing.JFrame {
    
    static boolean full = false; // Programmumfang: true -> inkl. aller Menueinträge (Sonderfunktionen und Altes)
    static boolean plus = false; // Programmumfang: true -> inkl. Sonderfunktionen
    String edvid6="";
    String parzelleSelected="";
    String landSelected="auswählen";
    String foaSelected="";
    String abtSelected="";
    String parzSelected="";
    String idSelected="";
    String aufSelected="";
    int landNoSelected=0;
    DataExchange ex =new DataExchange();
    boolean sucheNachId = false;
    boolean ausgabePDF = true; 
    DBConnection dbconn = null;
    Boolean dbconnOpen = false;
    Stand st =new Stand();
    private ProgressMonitor pm = null;
    private Bestandeswerte bestandeswerte;
    String datenPfad = "";                      // Pfad: aktuell bearbeiteter Versuch (<edvid>.mdb)
    String localPath="";
    String excelPfad = "";
    String excelPfad2 = "";
    String stammvPfad = "";
    String sadPfad = "";
    SpeciesDefMap SDM = new SpeciesDefMap();
    int rundung = 0;
    boolean stammvNralt = false;
    boolean uAufsAnzeigen = false;
    boolean nachNrSortieren = false;
    String visdatum = "";

    
    /** Creates new form VIS2005 */
    public Vis2006() {
        initComponents();
// Setting from ini File        
        String s ="";
        java.io.File f = new java.io.File("vis2006.ini");
        if (f.exists()==false) {
           try {
             java.io.PrintWriter ausgabe = new java.io.PrintWriter(new java.io.FileWriter("vis2006.ini"));
             ausgabe.println(1);
             ausgabe.println("");
             ausgabe.println("");
             ausgabe.println("");
             ausgabe.close();
            }
           catch(java.io.IOException e){System.out.println("Error! writing File vis2006.ini");}}
         System.out.println("Settings laden "); 
           try {
	    BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream("vis2006.ini")));
            s =in.readLine();
            int m = Integer.parseInt(s);
            
            s = in.readLine();
            jTextField1.setText(s);
            jTextField1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jButton1.doClick();
                    
                }
            });
            jComboBox7.setSelectedIndex(m);
            
            String pfad = in.readLine();
            if(pfad!=null && !pfad.equals("null")) datenPfad = pfad;
            excelPfad = in.readLine();
            excelPfad2 = in.readLine();
            stammvPfad = in.readLine();
            in.close();
            }
           catch(java.io.IOException e){System.out.println("Error! reading File vis2006.ini");}

        // Verzeichnis definieren
        try{ localPath= new File(".").getCanonicalPath();
        } catch (Exception e){e.printStackTrace();};

        if(excelPfad == null) excelPfad = localPath;
        if(excelPfad2 == null) excelPfad2 = localPath;
        if(stammvPfad == null || stammvPfad.length()<1) stammvPfad = localPath;
        if(excelPfad.length()<1) excelPfad=localPath;
        if(excelPfad2.length()<1) excelPfad2=localPath;


        // Baumarteneinstellungen laden
        try {
            SDM.readFromPath(localPath+System.getProperty("file.separator")+st.FileXMLSettings);
            } catch (Exception e) {
                e.printStackTrace();
            }
        st.setSDM(SDM);

        if(!full){ // alte Funktionalitäten für Standardbenutzer ausblenden zum Einschalten Programmaufruf mit Zusatz "full"
            System.out.println("Ausgeblendet");
            //jMenu2.remove(jMenuItem3);   // Husky-Daten erzeugen
            //jMenu7.remove(jMenuItem23); // Husky-Daten (csv) einlesen, jetzt werden Excel-Dateien direkt eingelesen
            if(!plus){
               jMenuBar1.remove(jMenu11);  //Sonderfunktionen
            }
            validate();
        }
        visdatum = "keine Angabe";
        try{
            visdatum = LastModificationTime.getModificationDate(localPath+System.getProperty("file.separator")+
                        "dist"+System.getProperty("file.separator")+"Vis2006.jar");
        } catch(IOException exc) {System.out.println(exc); exc.printStackTrace();}
        System.out.println("Version vom "+visdatum);
        dbAuswahl();

        if(s!=null){
            String erg = loadID(s);
            jLabel9.setText(erg);
        } 
        ToolTipManager.sharedInstance().setDismissDelay(18000);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox8 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VIS - Versuchsfl\u00e4cheninformationen und Standardauswertung NW-FVA  Version 0.9");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(java.awt.Color.lightGray);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(2, 0));

        jPanel2.setMinimumSize(new java.awt.Dimension(870, 80));
        jPanel2.setPreferredSize(new java.awt.Dimension(880, 80));
        jPanel13.setMinimumSize(new java.awt.Dimension(480, 33));
        jPanel13.setPreferredSize(new java.awt.Dimension(490, 33));
        jLabel8.setText("Datenbank");
        jPanel13.add(jLabel8);

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "keine", "Waldwachstum", "Lokal", "Waldwachstum (BZE)" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jPanel13.add(jComboBox7);

        jButton2.setText("sichern");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel13.add(jButton2);

        jButton3.setText("laden");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel13.add(jButton3);

        jPanel2.add(jPanel13);

        jPanel12.setMinimumSize(new java.awt.Dimension(280, 33));
        jPanel12.setOpaque(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(280, 33));
        jPanel12.setRequestFocusEnabled(false);
        jLabel1.setText("nach EDV-ID");
        jPanel12.add(jLabel1);

        jTextField1.setColumns(8);
        jPanel12.add(jTextField1);

        jButton1.setText("suchen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel12.add(jButton1);

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "alle" }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jPanel12.add(jComboBox8);

        jPanel2.add(jPanel12);

        jPanel1.add(jPanel2);

        jPanel3.setMinimumSize(new java.awt.Dimension(213, 40));
        jPanel3.setPreferredSize(new java.awt.Dimension(233, 40));
        jPanel6.setLayout(new java.awt.GridLayout(2, 0));

        jLabel2.setText("Land");
        jPanel6.add(jLabel2);

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel6.add(jComboBox1);

        jPanel3.add(jPanel6);

        jPanel7.setLayout(new java.awt.GridLayout(2, 0));

        jLabel3.setText("FoA");
        jPanel7.add(jLabel3);

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jPanel7.add(jComboBox2);

        jPanel3.add(jPanel7);

        jPanel8.setLayout(new java.awt.GridLayout(2, 0));

        jLabel4.setText("Abt");
        jPanel8.add(jLabel4);

        jComboBox3.setMaximumSize(new java.awt.Dimension(100, 32767));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jPanel8.add(jComboBox3);

        jPanel3.add(jPanel8);

        jPanel9.setLayout(new java.awt.GridLayout(2, 0));

        jLabel7.setText("ID");
        jPanel9.add(jLabel7);

        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jPanel9.add(jComboBox6);

        jPanel3.add(jPanel9);

        jPanel10.setLayout(new java.awt.GridLayout(2, 0));

        jLabel5.setText("Parzelle");
        jPanel10.add(jLabel5);

        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jPanel10.add(jComboBox4);

        jPanel3.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridLayout(2, 0));

        jLabel6.setText("Aufnahme");
        jPanel11.add(jLabel6);

        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jPanel11.add(jComboBox5);

        jPanel3.add(jPanel11);

        jPanel1.add(jPanel3);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel4.setAlignmentX(1.0F);
        jPanel4.setPreferredSize(new java.awt.Dimension(75, 600));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 290));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 330));
        jTable1.setMinimumSize(new java.awt.Dimension(30, 60));
        jTable1.setOpaque(false);
        jTable1.setPreferredSize(new java.awt.Dimension(250, 260));
        jTable1.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jPanel4.add(jScrollPane1);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 25));
        jPanel5.add(jLabel9);

        getContentPane().add(jPanel5, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("bearbeiten");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("lokale Daten \u00f6ffnen");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem1);

        jMenu7.setText("Eingabetabelle");
        jMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu7ActionPerformed(evt);
            }
        });

        jMenuItem12.setText("Eingabetabelle erstellen");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem12);

        jMenuItem13.setText("Eingabetabelle nach Baum \u00fcbernehmen");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem13);

        jMenuItem25.setText("Eingabetabelle l\u00f6schen");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem25);

        jMenuItem41.setText("H\u00f6heneingabetabelle erstellen");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem41);

        jMenuItem42.setText("H\u00f6heneingabetabelle: Werte nach Baum \u00fcbernehmen");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem42);

        jMenuItem43.setText("H\u00f6heneingabetabelle l\u00f6schen");
        jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem43ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem43);

        jMenuItem34.setText("Excel-Daten in Eingabetabelle \u00fcbernehmen");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });

        jMenu7.add(jMenuItem34);

        jMenu1.add(jMenu7);

        jMenu8.setText("Baumtabelle");
        jMenuItem14.setText("Durchmesser berechnen");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });

        jMenu8.add(jMenuItem14);

        jMenuItem15.setText("Baumnummer positionieren");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });

        jMenu8.add(jMenuItem15);

        jMenuItem22.setText("Fehlstammberechnung");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });

        jMenu8.add(jMenuItem22);

        jMenuItem29.setText("Umnummerieren");
        jMenuItem29.setToolTipText("<html><strong>Neue Baumnummern vergeben</strong><br>  Diese Funktion erm\u00f6glicht es neue Baumnummern f\u00fcr eine ganze Parzelle<br> mit allen Aufnahmen zu vergeben.<br><br> <strong>Vorbereitung</strong><br> 1. Letzte Aufnahme: alle Nummern nach nralt \u00fcbertragen (mit SQL)<br> 2. Neue Nummern in die Spalte nr eintragen<br> <br> In Vis die gew\u00fcnschte Parzelle und die letzte Aufnahme ausw\u00e4hlen <br> und \"umnummerieren\" klicken.<br> <br> <strong>Achtung!</strong><br> Dabei wird die Nummerierung aller vorhergehenden Aufnahmen angepasst. <br> Sollten Eintr\u00e4ge unter nralt bereits von einer fr\u00fcheren Umnummerierung <br> vorhanden sein, gehen diese verloren. ");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });

        jMenu8.add(jMenuItem29);

        jMenu1.add(jMenu8);

        jMenu9.setText("Lokale Datenbank");
        jMenu9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu9ActionPerformed(evt);
            }
        });

        jMenuItem17.setText("HessenDaten als Neuanlage einlesen");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });

        jMenu9.add(jMenuItem17);

        jMenuItem39.setText("Sachsen-Anhalt-Daten einlesen");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });

        jMenu9.add(jMenuItem39);

        jMenuItem20.setText("Doppelte Eintr\u00e4ge aus Parzelle entfernen");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });

        jMenu9.add(jMenuItem20);

        jMenuItem37.setText("Stammverteilungsplan erstellen");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });

        jMenu9.add(jMenuItem37);

        jMenuItem38.setText("Stammverteilungsplan: Nullpunkt neu w\u00e4hlen");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });

        jMenu9.add(jMenuItem38);

        jMenu1.add(jMenu9);

        jMenu10.setText("Daten in SQL-Datenbank");
        jMenu10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu10ActionPerformed(evt);
            }
        });

        jMenuItem18.setText("locale Parzelle in SQL-Datenbank kopieren");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });

        jMenu10.add(jMenuItem18);

        jMenuItem19.setText("Versuchsanlage in SQL Datenk kopieren");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });

        jMenu10.add(jMenuItem19);

        jMenu1.add(jMenu10);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Pr\u00fcfen");
        jMenu3.setToolTipText("<html>Folgende <strong>Spaltennamen</strong> werden erkannt:<br> EDVID &#32;&#32; Nr &#32;&#32; Art &#32;&#32; ZF &#32;&#32; OU &#32;&#32; MH &#32;&#32; D1 &#32;&#32; A1 &#32;&#32; H1 &#32;&#32; Alt2 &#32;&#32; D2 &#32;&#32; D2_1 &#32;&#32; D2_2 &#32;&#32; A2 &#32;&#32; H2 &#32;&#32; K2<br>   MHN &#32;&#32; DN &#32;&#32; DNK &#32;&#32; AN &#32;&#32; EN &#32;&#32; Alt_EN &#32;&#32; HN &#32;&#32; KN &#32;&#32; R &#32;&#32; Bemerk &#32;&#32; ZFN &#32;&#32; OUN &#32;&#32; Qua<br><br>  Reihenfolge der Spalten sowie Gro\u00df- und Kleinschreibung sind dabei unerheblich.");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem21.setText("Vis-Daten");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });

        jMenu3.add(jMenuItem21);

        jMenuItem35.setText("Excel Felddaten");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });

        jMenu3.add(jMenuItem35);

        jMenuItem40.setText("Stammverteilungsplan");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });

        jMenu3.add(jMenuItem40);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("kopieren");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed1(evt);
            }
        });

        jMenuItem28.setText("Excel Felddatendatei");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem28);

        jMenuItem31.setText("Parzelle in tempor\u00e4re Datei kopieren");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem31);

        jMenuItem9.setText("Versuchsfl\u00e4che in tempor\u00e4re Datei kopieren");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem9);

        jMenuItem16.setText("Neuanlage");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem16);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Einstellungen");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem6.setText("PDF statt HTML");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });

        jMenu4.add(jMenuItem6);

        jCheckBoxMenuItem1.setText("Stammverzeichnis mit alten Nummern");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });

        jMenu4.add(jCheckBoxMenuItem1);

        jCheckBoxMenuItem3.setText("Stammverzeichnis nach Baumnummer sortieren");
        jCheckBoxMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem3ActionPerformed(evt);
            }
        });

        jMenu4.add(jCheckBoxMenuItem3);

        jCheckBoxMenuItem2.setText("Ergebnisbogen mit Dauerumfangmessungen");
        jCheckBoxMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem2ActionPerformed(evt);
            }
        });

        jMenu4.add(jCheckBoxMenuItem2);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Ausgabe");
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });

        jMenuItem7.setText("H\u00f6henverzeichnis");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem7);

        jMenuItem8.setText("Stammverzeichnis");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem8);

        jMenuItem4.setText("Einzelberechnung (Ergebnisbogen Aufnahme)");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem4);

        jMenuItem5.setText("Ergebnisbogen Parzelle");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem5);

        jMenuItem10.setText("Ergebnisbogen Versuch");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem10);

        jMenuItem27.setText("XML-Rohdaten f\u00fcr TreeGrOSS exportieren");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem27);

        jMenuItem36.setText("BZE Datenauswertung");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem36);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Dateneditor");
        jMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu6ActionPerformed(evt);
            }
        });

        jMenuItem26.setText("Stammverteilungsplan");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });

        jMenu6.add(jMenuItem26);

        jMenuBar1.add(jMenu6);

        jMenu11.setText("Sonderfunktionen");
        jMenuItem32.setText("Bestandeswerte (aktueller Versuch)");
        jMenuItem32.setToolTipText("<html><strong>Berechnen von Kennzahlen</strong><br><br> Es werden bestandesweise und baumartenweise grupperierte Kennzahlen <br> f\u00fcr den ausgew\u00e4hlten Versuch (alle Parzellen und Aufnahmen) <br> in die vorbereiteten Tabellen \"Bestandeswerte\" und \"Baumartenwerte\" <br> der Datenbank geschrieben. <br>F\u00fcr Berechnungen in der lokalen Datenbank m\u00fcssen diese Tabellen <br> sowie die Tabelle tempAuf zuvor angelegt werden.");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });

        jMenu11.add(jMenuItem32);

        jMenuItem30.setText("Bestandeswerte (alle)");
        jMenuItem30.setToolTipText("<html><strong>Berechnen von Kennzahlen</strong><br>\n<br>\nEs werden bestandesweise und baumartenweise grupperierte Kennzahlen <br>\nf\u00fcr alle(<strong>!</strong>) Versuche (alle Parzellen und Aufnahmen) <br>\nin die vorbereiteten Tabellen \"Bestandeswerte\" und \"Baumartenwerte\" <br>\nder Datenbank geschrieben.<br>\n<br>F\u00fcr Berechnungen in der lokalen Datenbank m\u00fcssen diese Tabellen <br> sowie die Tabelle tempAuf zuvor angelegt werden.<br>\n<br>\n<strong>Achtung!</strong><br>\nDie Berechnung in der Waldwachstums-Datenbank kann einige Zeit dauern.\n");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });

        jMenu11.add(jMenuItem30);

        jMenuBar1.add(jMenu11);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-793)/2, (screenSize.height-564)/2, 793, 564);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
//  Umnummerieren es werden alle Baumnummern der vorgehenden Aufnahmen mit dem neuen Wert ersetzt
     this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
     int neuAuf=Integer.parseInt((String) jComboBox5.getSelectedItem());
     String strNr[] = new String[4000]; 
     String strAlt[] = new String[4000];
     int anz=0;
     try{
        Statement stmt = dbconn.Connections[0].createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT * FROM Baum WHERE edvid = \'"+idSelected+"\'  And auf = "+neuAuf+"  ");
        while (rs.next()){
                 String nrx = rs.getObject("nr").toString();
                 String nrxalt = rs.getObject("nralt").toString().trim();
                 if (nrx.compareTo("cm")!=0 && nrx.compareTo("onum")!=0 && nrx.compareTo("nurH")!=0 && nrxalt.trim().length() > 0){
                     strNr[anz] = new String();
                     strNr[anz] = nrx;
                     strAlt[anz] = nrxalt;
                     anz=anz+1;
                 } 
             } // Ende While
 //          Nummern ändern
        for (int i=0;i< anz;i++){
            stmt.executeUpdate("UPDATE Baum SET nralt = '"+strAlt[i]+"' , nr = '"+strNr[i]+"' where edvid = \'"+idSelected+"\'  And auf < "+neuAuf+
                    " AND trim(nr) = '"+strAlt[i]+"' ");
            System.out.println(strNr[i]+"  "+strAlt[i]);
        }
 //          stmt.executeUpdate("UPDATE Baum SET nr = trim(nr)  " );
 //           
          }
          catch (Exception e){  System.out.println("Unnummerierung :"+e); }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
     
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
// Loesche die Eingabetabelle
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Eingabetabelle egt = new Eingabetabelle(this, datenPfad);
        jLabel9.setText(egt.deleteTable());
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// lokale Datenbank laden
        jLabel9.setText("");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    
        // Bisherige Daten sichern
        boolean sichernErfolgreich = true;
        if(datenPfad!=null && datenPfad.length()> 0 && edvid6.length()>0){
            sichernErfolgreich =  tempDatenSichern(datenPfad);
        }
        
        if (sichernErfolgreich) {
            // Pfad für zu ladende Daten herausfinden
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            fc.setCurrentDirectory(new File(localPath + "//tempdaten"));
            fc.setDialogTitle("Versuchsanlage auswählen");
            fc.setApproveButtonText("übernehmen");
            // FileFilter
            javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".mdb") || f.isDirectory();
                }
                @Override
                public String getDescription() {
                    return ("Access-Datenbanken (.mdb)");
                }
            };            
            fc.setFileFilter(ff);
            if(datenPfad.length()>0) fc.setSelectedFile(new File(datenPfad));
            int auswahl = fc.showOpenDialog(this); 
            if(auswahl == JFileChooser.APPROVE_OPTION){
                String zielpfad = fc.getSelectedFile().getPath();
                // Verhindern, dass die Sicherung geladen wird
                if(!zielpfad.contains("tempdaten\\sicher")){
                    datenPfad = zielpfad;
                    String edvid = fc.getSelectedFile().getName();
                    // Verbindung herstellen und laden
                    //dbconn.closeDBConnection(0);
                    dbAuswahl();
                    openDataBank();
                    if(dbconnOpen){
                        jTextField1.setText(edvid.substring(0,6));
                        loadID(edvid.substring(0,6));
                        jLabel9.setText("geladen");
                    }
                } else jLabel9.setText("Nicht aus dem Sicherungsverzeichnis laden, das ist gefährlich!");
            }
            else jLabel9.setText("Datenbank konnte nicht geöffnet werden.");    
        }
        else jLabel9.setText("Fehler beim Sichern der bisher verwendeten Daten.");

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jLabel9.setText(""); 
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String text = "Fehler beim Sichern!";
        boolean ok = false;
        if(datenPfad.length()> 0 ){
            ok = tempDatenSichern(datenPfad);
            if(ok) text = "Sicherung von " + edvid6 + " erfolgreich.";
        } else text = "Keine Datei zum Sichern!";
        jLabel9.setText(text);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
// Excel Felddaten-Datei(en) erzeugen
        jLabel9.setText("");
        int auf1 = 0; 
        int auf2 = 0;
        boolean parzOnly = true;
        boolean uAuf = false;
        boolean ok = false;
        Connection con = dbconn.Connections[0];
        
//  Verzeichnis wählen
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser(); //javax.swing.JF
        fc.setCurrentDirectory(new File(excelPfad2));
        fc.setDialogTitle(" Verzeichnis für Excel Daten auswählen");
        fc.setApproveButtonText("übernehmen");
        fc.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);   
        javax.swing.JCheckBox parzOnlyCheck = new javax.swing.JCheckBox("alle Parzellen");
        //fc.add(parzOnlyCheck, java.awt.BorderLayout.LINE_END);
        javax.swing.JCheckBox uAufCheck = new javax.swing.JCheckBox("Ablesung Dauerumfangmessung");
        JPanel checkPanel = new JPanel();    // Panel rechts des Hauptfensters
        checkPanel.setLayout(new BorderLayout());
        JPanel checkPanel2 = new JPanel();   // Panel innerhalb des ersten (unten)
        checkPanel2.setLayout(new BorderLayout());
        //checkPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
        checkPanel2.add(parzOnlyCheck, BorderLayout.CENTER);
        checkPanel2.add(uAufCheck, BorderLayout.SOUTH);
        checkPanel.add(checkPanel2, BorderLayout.SOUTH);
        fc.add(checkPanel, BorderLayout.LINE_END);       
        int k=fc.showOpenDialog(this);        
        parzOnly = parzOnlyCheck.isSelected();  // Dateien für alle Parzellen einer edvid?
        uAuf = uAufCheck.isSelected();          // Ablesung von Dauerumfangmessbändern?
        java.io.File verzeichnis = fc.getSelectedFile();
        String inOrdner = verzeichnis.getAbsolutePath();
        excelPfad2 = inOrdner;
// Dateien erzeugen        
        int nparz = jComboBox4.getItemCount();

        if(parzOnly){
            int ndateien = 0;
            for(int i=0; i < nparz; i++){
                idSelected = idSelected.substring(0,6)+jComboBox4.getItemAt(i);
                setAufNeu();
                // Letzte und vorletzte Aufnahme raussuchen
                int anzahlAufn = jComboBox5.getItemCount();
                auf2 = Integer.parseInt((String)(jComboBox5.getItemAt(anzahlAufn - 1)));
                if (auf2 > 1) auf1 = Integer.parseInt((String)(jComboBox5.getItemAt(anzahlAufn - 2)));
                
                ExcelFelddaten excelFelddaten = new ExcelFelddaten(this, idSelected);
                ok = excelFelddaten.erzeugen(con, inOrdner, auf2, auf1, uAuf);
                if (ok) ndateien = ndateien+1;
            }
            jLabel9.setText(ndateien + " von "+ nparz + " Dateien erzeugt");       
        }
        else{ 
            // Letzte und vorletzte Aufnahme raussuchen
            int anzahlAufn = jComboBox5.getItemCount();
            auf2 = Integer.parseInt((String)(jComboBox5.getItemAt(anzahlAufn - 1)));
            if (auf2 > 1 && anzahlAufn > 1) auf1 = Integer.parseInt((String)(jComboBox5.getItemAt(anzahlAufn - 2)));
            
            ExcelFelddaten excelFelddaten = new ExcelFelddaten(this, idSelected);
            ok = excelFelddaten.erzeugen(con, inOrdner, auf2, auf1, uAuf);
            if (ok) jLabel9.setText("Eine Datei erzeugt");
            else jLabel9.setText("Fehler! keine Datei erstellt");
        }
        System.out.println("beendet");
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
// xml-File mit Rohdaten schreiben
        jLabel9.setText("");
         LoadStand lts = new  LoadStand(); 
         Stand st = new Stand();
         st.setSDM(SDM);
         boolean abbrechen = false;
         if(jComboBox7.getSelectedIndex()!=3){ //keine BZE-Daten
             int aufNr= Integer.parseInt((String)jComboBox5.getSelectedItem());
             // Bestand laden ohne Randbäume
             st=lts.loadFromDB(this, dbconn, st, idSelected,aufNr, false, false, false);
         } else{
             String text = "Bitte einzelne Parzelle auswählen.";
             if(jComboBox8.getSelectedIndex()==0){
                javax.swing.JOptionPane.showMessageDialog (this, text, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                abbrechen = true;
             }
                 
             else st = lts.loadBZE(this, dbconn, st, idSelected, false, false);
         }
         
         // Fehlende Koordinaten (ohne cm-Bäume) -> Hinweis
         if(! abbrechen){
            int fehlkoord = 0;
            for(int i=0; i<st.ntrees; i++){
                if((st.tr[i].x ==-9.0 || st.tr[i].y==-9.0)&& !st.tr[i].no.trim().equals("cm")) fehlkoord = fehlkoord + 1;
            }
            if(fehlkoord > 0){
              String text = fehlkoord + " fehlende Koordinate(n)! \n       (ohne cm-Bäume)";
              javax.swing.JOptionPane.showMessageDialog (this, text, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            }
         
            System.out.println("LTS down");
            TreegrossXML2 treegrossXML2 = new TreegrossXML2();
            System.out.println("LTS down");
            JFileChooser fc = new JFileChooser();
            String localPath="";
            fc.setCurrentDirectory(new File(localPath));
            int auswahl = fc.showOpenDialog(this);
            String pa= fc.getSelectedFile().getPath();
            if(!pa.endsWith(".xml")) pa = pa+".xml";
            st.setProgramDir(localPath);
            treegrossXML2.saveAsXML(st,pa);
            jLabel9.setText("XML gespeichert");
         }
        
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
// Stammverteilungsplan bearbeiten
        boolean bzedaten = false;
        boolean stammvOeffnen = true;
        int aufNr = 0;
        if(jComboBox7.getSelectedIndex()==3) bzedaten=true;
         LoadStand lts = new  LoadStand();
         Stand stx = new Stand();
         stx.setSDM(SDM);
         if(!bzedaten){    // Normalfall
             String aufNrStr= (String) (jComboBox5.getSelectedItem());
             System.out.println("Stammverteilung "+aufNrStr); 
             aufNr = Integer.parseInt(aufNrStr);
             stx=lts.loadFromDB(this, dbconn, stx, idSelected,aufNr, true, false, true); //Bestand mit Randbäumen laden 
             // Sonderpunkte hinzufügen
             Statement stmt = null;
             ResultSet rs = null;
             try{
                 stmt = dbconn.Connections[0].createStatement();
                 rs = stmt.executeQuery("SELECT nr, x, y, z FROM Stammv WHERE edvid = '"+idSelected+"' " +
                         "AND LEFT(trim(nr),1) NOT BETWEEN '0' AND '9' AND LEFT(trim(nr),3)<>'ECK';");
                 while(rs.next()){
                     String namex = "";
                     Object namexo = rs.getObject("nr");
                     if(namexo != null) namex = namexo.toString().trim();
                     double xx = rs.getDouble("x");
                     double yy = rs.getDouble("y");
                     double zz = rs.getDouble("z");
                     stx.addcornerpoint(namex, xx, yy, zz);
                 }

             }catch(Exception e){e.printStackTrace();
             } finally{
                 try{
                     if(rs != null) rs.close();
                     if(stmt != null) stmt.close();
                 } catch (Exception e){e.printStackTrace();}
             }
         }
         else {   // für BZE-Daten
             if(idSelected.length()< 1){
                 String text = "Konkreten BZE-Plot auswählen";
                 javax.swing.JOptionPane.showMessageDialog (this, text, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                 stammvOeffnen = false;
             }
             else {
                 stx = lts.loadBZE(this, dbconn, stx, idSelected, true, false);
                 double radius = Math.sqrt(10000.0*stx.size/Math.PI);
                 stx.addcornerpoint("Mitte", radius, radius, 0);
             }

         }

         if(stammvOeffnen){
              boolean viewOnly = true;
              if (jComboBox7.getSelectedIndex() == 2) viewOnly=false;
              StammVerteilungsPlan stv = new StammVerteilungsPlan(this,true,stx,viewOnly);
              stv.setVisible(true);

// Koordinaten abspeichern
              if(!bzedaten) lts.saveXYToDB(dbconn, stx, idSelected, aufNr);
         }
        

    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenu9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu9ActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jMenu9ActionPerformed

    private void jMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu7ActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jMenu7ActionPerformed

    private void jMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu6ActionPerformed
        jLabel9.setText("");
    }//GEN-LAST:event_jMenu6ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed

  /* Alte Version mit Fehlstammberechnung für U-Aufnahmen (wieder aktivieren falls nötig)
        jLabel9.setText("");
        boolean fehlAufTypU = false;
        boolean beenden = false;
        int fehlAuf=Integer.parseInt((String) jComboBox5.getSelectedItem());
        int vorauf = 0;   // Integer.parseInt((String) jComboBox5.getItemAt(jComboBox5.getSelectedIndex()-1));
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = dbconn.Connections[0].createStatement();
            rs = stmt.executeQuery("SELECT auf, typ FROM Auf WHERE edvid = '" + idSelected + "' AND auf <= " + fehlAuf + " "
                    + "ORDER BY auf DESC;");
            while (rs.next() && !beenden){
                int aufx = rs.getInt("auf");
                String typx = "";
                Object typobj = rs.getObject("typ");
                if(typobj != null) typx = typobj.toString().trim();
                if(aufx == fehlAuf && typx.equals("U")){
                    fehlAufTypU = true;
                }
                if(aufx < fehlAuf && !fehlAufTypU && !typx.equals("U")){
                    vorauf = aufx;
                    beenden = true;
                }else if(aufx < fehlAuf && fehlAufTypU){
                    vorauf = aufx;
                    beenden = true;
                }
            }
            
        }catch(Exception e){ e.printStackTrace();}
        finally {
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            }catch (Exception e) {}
        }
        if(vorauf == 0) {
            String text = "Für die erste Aufnahme ist keine Fehlstammberechnung möglich.";
            javax.swing.JOptionPane.showMessageDialog (this, text, "Fehler", JOptionPane.ERROR_MESSAGE);
        }else{
            // Durchführung der Fehlstammberechnung
            Fehlstamm fehlstamm = new Fehlstamm(this,true,idSelected,dbconn, fehlAuf, vorauf, fehlAufTypU);
        }
        jLabel9.setText("fertig");
    */
        jLabel9.setText("");
        boolean beenden = false;
        int fehlAuf=Integer.parseInt((String) jComboBox5.getSelectedItem());
        int vorauf = 0;   // Integer.parseInt((String) jComboBox5.getItemAt(jComboBox5.getSelectedIndex()-1));
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = dbconn.Connections[0].createStatement();
            rs = stmt.executeQuery("SELECT auf, typ FROM Auf WHERE edvid = '" + idSelected + "' AND auf <= " + fehlAuf + " "
                    + "ORDER BY auf DESC;");
            while (rs.next() && !beenden){
                int aufx = rs.getInt("auf");
                String typx = "";
                Object typobj = rs.getObject("typ");
                if(typobj != null) typx = typobj.toString().trim();
                if(aufx == fehlAuf && typx.equals("U")){
                    beenden = true;
                    String text= "Keine Fehlstammberechnung für U-Aufnahmen.";
                    JOptionPane.showMessageDialog (this, text, "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                else if(aufx < fehlAuf){
                    vorauf = aufx;
                    beenden = true;
                }
            }

        }catch(Exception e){ e.printStackTrace();}
        finally {
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            }catch (Exception e) {}
        }
        if(vorauf == 0 && beenden == false) {
            String text = "Für die erste Aufnahme ist keine Fehlstammberechnung möglich.";
            JOptionPane.showMessageDialog (this, text, "Fehler", JOptionPane.ERROR_MESSAGE);
        }else if (vorauf > 0){
            // Durchführung der Fehlstammberechnung
            Fehlstamm fehlstamm = new Fehlstamm(this,true,idSelected,dbconn, fehlAuf, vorauf);
        }
        jLabel9.setText("fertig");
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
    // Vis-Daten prüfen
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CheckVisData cvd = new CheckVisData();
        openDataBank();
        String filename="checkReport.htm";
        cvd.createCheckReport(filename, idSelected);
        int ndoppel=cvd.doppelteDatensaetze(idSelected, dbconn);
        int fehler = cvd.plausiCheck(idSelected, dbconn); 
        cvd.closeCheckReport();
        if (ndoppel < 0  || fehler < 0) jLabel9.setText("Fehler");
        else {
            if (ndoppel > 0 || fehler > 0) {
                try {
                    Runtime.getRuntime().exec(" rundll32 url.dll,FileProtocolHandler " + filename);
                } catch (Exception e2) {System.out.println(e2);}
                jLabel9.setText(ndoppel + " doppelte Datensätze, " + fehler + " Hinweise");
            } else jLabel9.setText("Keine Hinweise, kein Datensatz doppelt");   
        }        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
// Doppelte Einträge ein Versuchsparzelle entfernen
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CopyExperimentData ced = new CopyExperimentData();
        jLabel9.setText(ced.cleanDoppelte(idSelected, datenPfad, true));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
// Versuchsfläche mit allen Parzellen in SQL- Datenbank kopieren
       Passwortabfrage pwd = new Passwortabfrage(Vis2006.this, true);
        if (pwd.istPasswortKorrekt()) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String idParzelle = idSelected.substring(0, 6);
            String parzellen[] = new String[300];
            int nparzellen = 0;
            try {
                Statement stmt = dbconn.Connections[0].createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Parz WHERE edvid = \'" + idParzelle + "\' ORDER BY  parzelle");
                while (rs.next()) {
                    String par = rs.getObject("parzelle").toString();
                    par = par.trim();
                    if (par == null || par.length() < 1) {
                        break;
                    }
                    parzellen[nparzellen] = idParzelle + par;
                    nparzellen = nparzellen + 1;
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                System.out.println("Parzellen: " + e);
            }
            for (int i = 0; i < nparzellen; i++) {
//           System.out.println("Parzelle: "+parzellen[i]);
                CopyExperimentData ced = new CopyExperimentData();
                String txt = ced.addTempDaten(parzellen[i], datenPfad, false);
                if (txt.indexOf("FEHLER") == -1) {
                    jLabel9.setText("erfolgreich eingefügt");
                } else {
                    jLabel9.setText("FEHLER beim einfügen");
                }
            }
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//        CopyExperimentData ced = new CopyExperimentData();
//        jLabel9.setText(ced.addTempDaten(idSelected, true));

    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
// Versuchsparzelle in SQL- Datenbank kopieren
        Passwortabfrage pwd = new Passwortabfrage(Vis2006.this, true);
        if(pwd.istPasswortKorrekt()){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            CopyExperimentData ced = new CopyExperimentData();
            jLabel9.setText(ced.addTempDaten(idSelected, datenPfad, true));
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        String idHessen = jTextField1.getText();
        if (idHessen.length()>6) idHessen=idHessen.substring(0,6);
        Hessendaten hessendaten = new Hessendaten(this,true,idHessen, datenPfad);

    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // Neuanlage
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String text = "";
        
       // Bisherige Daten sichern
        boolean sichernErfolgreich = true;
        if(datenPfad.length()> 0) sichernErfolgreich =  tempDatenSichern(datenPfad);
        
        if(sichernErfolgreich){
            CopyExperimentData cde = new CopyExperimentData();
            text=cde.checkOutExperimentData(idSelected);
            datenPfad = cde.getZielDatei();
            if(datenPfad != null){
                Neuanlage neuanlage = new Neuanlage(this);
                String parzname = neuanlage.getParzName();
                
                HeuteString hs = new HeuteString();
                String stand = hs.getDatumStrichGetrennt();
                String datum = hs.get();
                
                // Auf Lokale DB umstellen und Daten laden
                jComboBox7.setSelectedIndex(2);
                dbAuswahl();
                
                // Verbindung herstellen und laden
                dbconn.closeDBConnection(0);
                openDataBank();
                loadID(idSelected.substring(0,6));
                            
                Statement stmt = null;
                try{
                    stmt = dbconn. Connections[0].createStatement();
                    stmt.executeUpdate("INSERT INTO Parz (id, edvid, parzelle, bhdfile, hohfile, insfile,"
                            + "behandl, flache, bemerk, stand, bearbeiter, stvp, nkro, PZ, Datum) VALUES "
                            + "('" + edvid6.concat(parzname) + "', '" + edvid6 + "', '" + parzname + "', "
                            + "'',0,'','',0,'','" + stand + "','',0,0,0, '" + datum + "' );");
                    stmt.executeUpdate("UPDATE Parz SET Stempel = now();");
                    text = text + " Parzelle angelegt."; 
                } catch(Exception e){System.out.println("Baumnummer setzen "+e);
                } finally{
                    try{
                        if(stmt != null) stmt.close();
                    }catch(Exception e){e.printStackTrace();}
                }
            } else text = text + " Parzelle konnte nicht angelegt werden.";     
        }
        else text = "Fehler beim Sichern der bisherigen Daten!";
        jLabel9.setText(text);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // Baumnummern positionieren
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // Baumtabelle
        Statement stmt = null;
        try{
            stmt = dbconn.Connections[0].createStatement();
            stmt.executeUpdate("UPDATE Baum SET nr = trim(nr);" );
            System.out.println(" 1. Statement fertig");
            //stmt.executeUpdate("UPDATE Baum SET nr = rtrim(nr)  " );
            //System.out.println(" 2. Statement fertig");
            stmt.executeUpdate("UPDATE Baum SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 2. Statement fertig");
            stmt.executeUpdate("UPDATE Baum SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 3. Statement fertig");
            stmt.executeUpdate("UPDATE Baum SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 4. Statement fertig");
            stmt.executeUpdate("UPDATE Baum SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 5. Statement fertig");
            stmt.executeUpdate("UPDATE Baum SET nr = nr+' '  WHERE (asc(mid(nr,5,1)) < 64 AND len(nr) = 5)" );
            System.out.println(" 6. Statement fertig");
            stmt.executeUpdate("UPDATE Baum SET nr = ' '+nr  WHERE (asc(mid(nr,5,1)) > 64 AND len(nr) = 5)" );
            System.out.println(" Baum fertig!");
            //
            try {
                stmt.execute("DROP INDEX edvid on Baum");  
            } catch (Exception e){ }
            try{
                stmt.execute("CREATE INDEX edvid ON Baum (edvid, auf) ");
            }catch(Exception e){}
        } catch(Exception e){
            System.out.println("Baumnummer setzen ");
            e.printStackTrace();
        } finally{
            try{
                if(stmt != null) stmt.close();
            }catch(Exception e){e.printStackTrace();}
        }
        
        // Stammv (im Gegensatz zu Baum.nr (varchar) hat Stammv.nr die feste Länge von 6 Zeichen)
        Statement stmt2 = null;
        try{
            stmt2 = dbconn.Connections[0].createStatement();
            stmt2.executeUpdate("UPDATE Stammv SET nr = trim(nr);" );
            System.out.println(" 1. Statement fertig");
            //stmt.executeUpdate("UPDATE Baum SET nr = rtrim(nr)  " );
            //System.out.println(" 2. Statement fertig");
            stmt2.executeUpdate("UPDATE Stammv SET nr = '    '+ trim(nr) WHERE len(trim(nr)) = 1;" );
            System.out.println(" 2. Statement fertig");
            stmt2.executeUpdate("UPDATE Stammv SET nr = '   '+ trim(nr) WHERE len(trim(nr)) = 2;" );
            System.out.println(" 3. Statement fertig");
            stmt2.executeUpdate("UPDATE Stammv SET nr = '  '+ trim(nr) WHERE len(trim(nr)) = 3;" );

            stmt2.executeUpdate("UPDATE Stammv SET nr = ' '+ trim(nr) WHERE len(trim(nr)) = 4;" );
            System.out.println(" 4. Statement fertig");
            stmt2.executeUpdate("UPDATE Stammv SET nr = ' '+ rtrim(nr) WHERE RIGHT(trim(nr),1) NOT BETWEEN '0' AND '9' " +
                    "AND LEFT(trim(nr),1) BETWEEN '0' AND '9'" );
            System.out.println(" Stammv fertig!");
            //           
            try {
                stmt2.execute("DROP INDEX edvid on Stammv");
            }
            catch (Exception e){ }
            stmt2.execute("CREATE INDEX edvid ON Stammv (edvid, auf) ");
        } catch(Exception e){System.out.println("Baumnummer setzen "+e);
        } finally{
            try{
                if(stmt2 != null) stmt2.close();
            }catch(Exception e){e.printStackTrace();}
        }
        
         // Qualittabelle
        Statement stmt3 = null;
        try{
            stmt3 = dbconn.Connections[0].createStatement();
            stmt3.executeUpdate("UPDATE Qualit SET nr = trim(nr);" );
            System.out.println(" 1. Statement fertig");
            //stmt3.executeUpdate("UPDATE Qualit SET nr = rtrim(nr)  " );
            //System.out.println(" 2. Statement fertig");
            stmt3.executeUpdate("UPDATE Qualit SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 2. Statement fertig");
            stmt3.executeUpdate("UPDATE Qualit SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 3. Statement fertig");
            stmt3.executeUpdate("UPDATE Qualit SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 4. Statement fertig");
            stmt3.executeUpdate("UPDATE Qualit SET nr = ' '+nr  WHERE len(nr) < 5 " );
            System.out.println(" 5. Statement fertig");
            stmt3.executeUpdate("UPDATE Qualit SET nr = nr+' '  WHERE (asc(mid(nr,5,1)) < 64 AND len(nr) = 5)" );
            System.out.println(" 6. Statement fertig");
            stmt3.executeUpdate("UPDATE Qualit SET nr = ' '+nr  WHERE (asc(mid(nr,5,1)) > 64 AND len(nr) = 5)" );
            System.out.println(" Qualit fertig!");
            //
            try {
                stmt3.execute("DROP INDEX edvid on Qualit");  
            } catch (Exception e){ }
            try{
                stmt3.execute("CREATE INDEX edvid ON Qualit (edvid, auf) ");
            }catch(Exception e){}
        } catch(Exception e){
            System.out.println("Baumnummer setzen ");
            e.printStackTrace();
        } finally{
            try{
                if(stmt3 != null) stmt3.close();
            }catch(Exception e){e.printStackTrace();}
        }




        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
// Durchmesser berechnen
     this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
// Durchmesser (d) aus gemessenem Wert und der Messhöhe berechnen
     try{
           Statement stmt = dbconn.Connections[0].createStatement();
           int anz =stmt.executeUpdate("UPDATE Baum SET d = dmess  WHERE (dmess > 0 AND mh = 13) " );
           stmt.close();
// Buche           
           Statement stmt2 = dbconn.Connections[0].createStatement();
           anz = anz + stmt2.executeUpdate("UPDATE Baum SET d = 10.0*((dmess/10.0)^0.99098)*((mh/10.0)^0.096449)  WHERE (dmess > 0 AND mh <> 13 AND art < 500 ) " );
           stmt2.close();
// Fichte           
           Statement stmt3 = dbconn.Connections[0].createStatement();
           anz =anz + stmt3.executeUpdate("UPDATE Baum SET d = 10.0*((dmess/10.0)^0.98511)*((mh/10.0)^0.14025)  WHERE (dmess > 0 AND mh <> 13 AND art > 499 AND art <> 611) " );
           stmt3.close();
// Douglasie           
           Statement stmt4 = dbconn.Connections[0].createStatement();
           anz = anz + stmt4.executeUpdate("UPDATE Baum SET d = 10.0*((dmess/10.0)^0.98841)*((mh/10.)^0.12338)  WHERE (dmess > 0 AND mh <> 13 AND art = 611 ) " );
           stmt4.close();
           
           System.out.println("Durchmesser berechnen, Anzahl  :"+anz);
           stmt.close();
           
     }
           
           catch(Exception e){System.out.println("BHD berechen: "+e);}
     
     
// Dauerumfangmessungen: Anpassung an ertragskundliche Daten und Datenergänzung
     System.out.println("Anpassung der Dauerumfangmessungen");
     int maxauf = 0;
     int minauf = 1000;
     int nart = 0;
     boolean uAufsVorhanden = false;
     HashMap<Integer,Double> aufvegjahr = new HashMap<Integer,Double>();
     HashMap<Integer,String> auftyp = new HashMap<Integer,String>();
     Statement sm = null;
     Statement smin = null;
     ResultSet rset = null;
     ResultSet rset2 = null;
     ResultSet rset3 = null;
     ResultSet rset4 = null;
     
     // Anzahl Aufnahmen, Anzahl Baumarten feststellen
     try{
         sm = dbconn.Connections[0].createStatement();
         
         rset = sm.executeQuery("SELECT auf, jahr, monat, typ FROM Auf WHERE edvid = '" + idSelected +"' ORDER BY auf;");
         AltersDezimale aDez = new AltersDezimale();
         while(rset.next()){             
             maxauf = rset.getInt("auf");
             if(maxauf < minauf) minauf = maxauf;
             int jahr = rset.getInt("jahr");
             int monat = rset.getInt("monat");
             String typ = "";
             Object typobj = rset.getObject("typ");
             if(typobj != null) typ = typobj.toString().trim();
             aufvegjahr.put(maxauf, (jahr - 1 + aDez.getAltersdezimale(monat)));
             auftyp.put(maxauf, typ);
             if(typ.equals("U")) uAufsVorhanden = true;

         }
         rset2 = sm.executeQuery("SELECT DISTINCT art FROM Baum WHERE edvid = '" + idSelected + "' AND r <> 'R';");
         while(rset2.next()) nart++;
     } catch (Exception e){ e.printStackTrace();
     } finally{
         try{
             if(rset != null) rset.close();
             if(rset2 != null) rset2.close();
             if(sm != null) sm.close();              
         } catch (Exception e){}
     }
     
     // Berechnung der neuen Durchmesser
     if(uAufsVorhanden) try{
         String nr = "";    // "vor"-Variablen beziehen sich auf die letzte Aufnahme mit plausiblen Werten für die Zuwachsmittelung
         String vornr = "";      // 
         double ddauer = 0.0;
         double vorddauer = 0.0;
         double dek = 0.0;         // Durchmesser der ertragskundlichen Aufnahme
         int status = 0;
         int art = 0;
         int vorart = 0;
         int artnr = -1;        // Arten zählen
         int letzteart = -1;   // Art der letzten Aufnahme (egal ob plausibel oder nicht s.o.)
         int auf = 0;
         int vorauf = 0;
         String typ = "";
         String vortyp = "";
         int monat = 0;
         int vormonat = 0;
         int jahr = 0;
         int vorjahr = 0;      
         double mitaddiff[][] = new double[nart][maxauf+1];  // mittlerer jährlicher Durchmesserzuwachs nach Aufnahme u. Art (zunächst Summe)
         int nddiff[][] = new int[nart][maxauf+1];           // Anzahl aufsummierter Zuwachswerte -> Mittelwert
         DauerBaum db[] = new DauerBaum[10000];
         int arten[] = new int[nart];
         int ndb = -1;                                        // Anzahl anzupassender Durchmesser
         boolean angefuegt = false;

         AltersDezimale aDez = new AltersDezimale();
         BhdAusD bhd = new BhdAusD();
         
         // Berechnung des mittleren jährlichen Zuwachses
         // nebenbei: Durchmesser der ertragskundlichen Messungen speichern
         rset3 = sm.executeQuery("SELECT Baum.nr, Baum.art, Baum.auf, "
                 + "Auf.jahr, Auf.monat, Auf.typ, Baum.ddauer, Baum.mh, Baum.d, Baum.status "
                 + "FROM Baum LEFT JOIN Auf ON (Baum.edvid = Auf.edvid) AND (Baum.auf = Auf.auf) "
                 + "WHERE Baum.edvid = '" + idSelected + "' "
                 + "AND r <> 'R'"
                 //+ "AND Auf.typ <> 'U' OR "
                 //+ "(Auf.typ = 'U' AND Auf.monat < 8 AND Baum.status < 3) "
                 + "ORDER BY Baum.art, trim(Baum.nr), Baum.auf;");

         while(rset3.next()){
             nr = "";
             Object nrobj = rset3.getObject("nr");
             if(nrobj != null) nr = nrobj.toString().trim();
             art = rset3.getInt("art");
             auf = rset3.getInt("auf");
             //System.out.println(nr);
             monat = rset3.getInt("monat");
             jahr= rset3.getInt("jahr");
             Object typobj = rset3.getObject("typ");
             typ = "";
             if(typ != null) typ = typobj.toString().trim();
             ddauer = 0.0;
             status = 1;
             //System.out.println(nr + "("+auf+") - " +ndb);

             if(art != letzteart) {
                 letzteart = art;
                 artnr ++;
                 arten[artnr] = art;               
             }

             if(typ.equals("U")){
                 ddauer = rset3.getInt("ddauer")/100.0;    // cm
                 int mh = rset3.getInt("mh");
                 if(mh != 13 && ddauer > 0){
                     ddauer = bhd.getBhd(ddauer, mh, art);
                 }
                 status = rset3.getInt("status");

                 if(ndb < 0){
                     ndb ++;
                     db[ndb] = new DauerBaum(nr, artnr);
                 } else if(! (nr.equals(db[ndb].nr) && artnr == db[ndb].artnr) ){
                     ndb ++;
                     angefuegt = false;
                     db[ndb] = new DauerBaum(nr, artnr);
                 }
                 db[ndb].setDauerAuf(jahr, monat, auf, ddauer);
                 
                 // Anfügen auch für die letzte EK-EK Periode, wenn "Vegetationsjahr" gleich
                 if(ndb > 0 && db[ndb].vegJahrDauerAufs[db[ndb].nDauerAufs-1] == db[ndb-1].vegJahrEKAuf2 &&
                         nr.equals(db[ndb-1].nr) && artnr == db[ndb-1].artnr && ! angefuegt){
                     db[ndb-1].setDauerAuf(jahr, monat, auf, ddauer);
                     angefuegt = true;
                 } 
             }            

             // Für Mittelwerte: nur Frühjahrsaufnahmen und plausible Werten
             boolean plausibel = false;
             if(typ.equals("U") && ddauer > 0 && status < 3 && monat < 8){
                 plausibel = true;
                 if(nr.equals(vornr) && art == vorart && vortyp.equals("U")){
                     // Mittlere jährliche Zuwächse zwischen den U-Aufnahmen bestimmen (nicht für die erste Aufnahme)
                     double ddiff = ddauer - vorddauer;
                     double zeitdiff = jahr + aDez.getAltersdezimale(monat) - (vorjahr + aDez.getAltersdezimale(vormonat));
                     mitaddiff[artnr][auf] = mitaddiff[artnr][auf] + ddiff/zeitdiff;
                     nddiff[artnr][auf] ++;                       
                 }
             } 
             // ... für ertragskundliche Aufnahmen
             else if( ! typ.equals("U")){
                 //plausibel = true;
                 dek = rset3.getInt("d")/10.0;   // cm wie ddauer auch
                 db[ndb+1] = new DauerBaum(nr, artnr);
                 db[ndb+1].setEKAufnahme1(jahr, monat, dek, auf);
                 if(ndb >= 0 && nr.equals(db[ndb].nr) && artnr == db[ndb].artnr){
                     db[ndb].setEKAufnahme2(jahr, monat, dek, auf);
                     // Anfügen der vorhergehenden Dauer-Aufnahme, wenn "Vegetationsjahr" gleich
                     if(db[ndb+1].vegJahrEKAuf1 == (vorjahr-1+aDez.getAltersdezimale(vormonat)) && vortyp.equals("U")){
                         db[ndb].setDauerAuf(vorjahr, vormonat, vorauf, vorddauer);
                     }
                 }
                 ndb ++;
                 angefuegt = false;

             }

             if(plausibel){                 
                 vorart = art;
                 vorauf = auf;
                 vorddauer = ddauer;
                 vortyp = typ;
                 vornr = nr;
                 vormonat = monat;
                 vorjahr = jahr;
             }
         }

         // Mittelwerte berechnen        
         for(int i = 0; i < nart; i++){ // i = Artnummer (!= Artencode)
             mitaddiff[i][minauf] = 0.0;               // kein Zuwachs für die erste Aufnahme
             nddiff[i][minauf] = 1;
             for(int j = minauf; j < maxauf+1; j++){
                 mitaddiff[i][j] = mitaddiff[i][j] / nddiff[i][j];
                     
                 if(Double.isNaN(mitaddiff[i][j]) && j > 1){  // Falls zeitgleich mit ertragskundlicher Aufnahme -> 0.0
                     double vegjahr = aufvegjahr.get(j);
                     String auftypx = auftyp.get(j);
                     if(j < maxauf) if(vegjahr == aufvegjahr.get(j+1) && auftypx.equals("U")){
                         mitaddiff[i][j] = mitaddiff[i][j + 1] / nddiff[i][j + 1];
                         mitaddiff[i][j+1] = 0.0;
                     }
                     else if(j > 1) if (vegjahr == aufvegjahr.get(j - 1)) {
                         mitaddiff[i][j] = 0.0;
                     }
                 } else if (mitaddiff[i][j] < 0){    // Warnung bei negativem mittleren Zuwachs
                     String text = "Negativer mittlerer Zuwachs für Art " + arten[i] + " berechnet (Aufnahme " +j+ "). \n"
                             + "Bitte Durchmesser überprüfen.";
                     JOptionPane.showMessageDialog(this, text);
                 }
             }
         }
         
         // ggf. Werte aus anderer Baumart (-> häufigste andere Laub- bzw. Nadelbaumart) 
         int[] nProArt = new int[nart];
         for(int i = 0; i < nart; i++){ // i = Artnummer (!= Artencode)
             System.out.println("Baumart " + arten[i]);
             boolean nProArtBerechnet = false;
             int vonart = 0;
             for(int j = minauf; j < maxauf+1; j++){
                 if(nddiff[i][j] < 1 && Double.isNaN(mitaddiff[i][j])){
                     if(!nProArtBerechnet){
                         for(int ii = 0; ii < nart; ii++){
                            int anz = 0;
                            for(int jj = minauf; jj < maxauf + 1; jj ++) anz = anz + nddiff[ii][jj];
                            nProArt[ii] = anz;
                         }
                         int nmax = 0;
                         if(arten[i] < 500){
                             for(int ii = 0; ii < nart; ii++) if(arten[ii] < 500 && nProArt[ii] > nmax){
                                 nmax = nProArt[ii];
                                 vonart = ii;
                             }
                         } else {
                             for(int ii = 0; ii < nart; ii++) if(arten[ii] > 499 && nProArt[ii] > nmax){
                                 nmax = nProArt[ii];
                                 vonart = ii;
                             }
                         }
                         if(nmax < 2) for(int ii = 0; ii < nart; ii++) if(nProArt[ii] > nmax){
                             nmax = nProArt[ii];
                             vonart = ii;
                         }
                         nProArtBerechnet = true;
                     }
                      mitaddiff[i][j] = mitaddiff[vonart][j];                     
                 }
                 System.out.println("Mittlerer Zuwachs Aufnahme " + j + ": " + mitaddiff[i][j] +"");
             }
         }
            


         // Angepasste Durchmesser berechnen u. Datenbankaktualisierung
         smin = dbconn.Connections[0].createStatement();
         for(int i = 0; i <= ndb; i++){
             if( db[i].nDauerAufs > 0 ){ // db[i].d1 > 0 && db[i].d2 > 0 &&
                 int artnrx = db[i].artnr;
                 double ekdiff = 0.0;
                 double dauerZuwachsGesamt = 0.0;
                 double zuwachsGesamt = 0.0;
                 double dneu = 0.0;
                 if(db[i].d1 > 0 && db[i].d2 > 0){
                     ekdiff = db[i].d2 - db[i].d1;
                     // Summe der mittleren Zuwächse für diese EK-EK-Periode
                     for(int n = 0; n < db[i].nDauerAufs; n ++){
                         //System.out.println(n + " " + artnrx);
                         if(!Double.isNaN(mitaddiff[artnrx][db[i].dauerAuf[n]]) && mitaddiff[artnrx][db[i].dauerAuf[n]] != 0.0 && n>0){
                             //if(n == 0) System.out.println("Baum " + db[i].nr + " (" + arten[db[i].artnr]+ ") doppelt/fehlt?");
                             dauerZuwachsGesamt = dauerZuwachsGesamt
                                     + mitaddiff[artnrx][db[i].dauerAuf[n]] * (db[i].vegJahrDauerAufs[n] - db[i].vegJahrDauerAufs[n-1]);
                             //System.out.println("Zuwachs Art "+ artnrx + " Aufnahme "+ db[i].dauerAuf[n] + " :" + mitaddiff[artnrx][db[i].dauerAuf[n]] );
                     
                         }
                     }
                 }
                 
                 // Werte für U-Aufnahmen
                 for(int j = 0; j < db[i].nDauerAufs ; j ++){
                     int aufx = db[i].dauerAuf[j];
                     double zeitraum = 0.0;
                     if(j > 0) zeitraum = db[i].vegJahrDauerAufs[j] - db[i].vegJahrDauerAufs[j-1];
                     else zeitraum = db[i].vegJahrDauerAufs[j] - db[i].vegJahrEKAuf1;
                     if(db[i].d1 > 0 && db[i].d2 > 0){
                         if(dauerZuwachsGesamt == 0.0){
                             dneu = db[i].d1;
                         } else if(Double.isNaN(mitaddiff[artnrx][aufx])){
                             // Fall: erste U-Aufnahme ohne dass zur selben Zeit eine ertragskundliche Aufnahme stattfand
                             zuwachsGesamt = zuwachsGesamt + ekdiff * zeitraum/(db[i].vegJahrEKAuf2-db[i].vegJahrEKAuf1);
                             dneu = db[i].d1 + zuwachsGesamt;
                             ekdiff = ekdiff -  ekdiff * zeitraum/(db[i].vegJahrEKAuf2-db[i].vegJahrEKAuf1);
                         } else {
                             zuwachsGesamt = zuwachsGesamt + mitaddiff[artnrx][aufx] / dauerZuwachsGesamt * ekdiff * zeitraum;
                             dneu = db[i].d1 + zuwachsGesamt;
                         }                   
                         smin.executeUpdate("UPDATE Baum SET d = " + (int)Math.round(dneu*10) + " "
                             + "WHERE auf = " + db[i].dauerAuf[j] + " AND trim(nr) = '" + db[i].nr + "' AND art = " + arten[db[i].artnr] + ";");
                     } else if(db[i].d1 > 0 && !Double.isNaN(mitaddiff[artnrx][aufx])){
                         zuwachsGesamt = zuwachsGesamt + mitaddiff[artnrx][aufx] * zeitraum;
                         dneu = db[i].d1 + zuwachsGesamt;
                         smin.executeUpdate("UPDATE Baum SET d = " + (int)Math.round(dneu*10) + " "
                             + "WHERE auf = " + db[i].dauerAuf[j] + " AND trim(nr) = '" + db[i].nr + "' AND art = " + arten[db[i].artnr] + ";");                  
                     }
                     else {
                         //System.out.println("Keine EK-Differenz für " + db[i].nr + "("+aufx+")");
                         smin.execute("UPDATE Baum SET d = " + db[i].ddauer[j]*10 + " "
                             + "WHERE auf = " + db[i].dauerAuf[j] + " AND trim(nr) = '" + db[i].nr + "' AND art = " + arten[db[i].artnr] + " "
                             + "AND ddauer > 0;");
                         // Extrafall: keine abschließende/eröffnende EK-Aufnahme und kein ddauer, dann bleibt d wie es ist
                         //  -> Wert muss über Fehlstammberechnung gefunden werden
                     }    
                 }
             }
         }
 
         // Es kann Bäume ohne EK-Durchmesser geben -> Randbäume werden nicht ergänzt oder berechnet

     } catch (Exception e){
         e.printStackTrace();
     }
     finally{
         try{
             if(rset3 != null) rset3.close();
             if(rset4 != null) rset4.close();
         }catch (Exception e){
         }
     }
     jLabel9.setText("Durchmesserübernahme und -berechnung durchgeführt");
     jLabel9.setVisible(true);
     this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        jLabel9.setText("");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        EingabeDatenDialog edd = new EingabeDatenDialog(this, true, idSelected, datenPfad);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        jLabel9.setText("");
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
// Erzeuge die Eingabetabelle
        jLabel9.setText("");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Eingabetabelle egt = new Eingabetabelle(this, datenPfad);
        //String [] edvids = new String[jComboBox4.getItemCount()];
        //for(int i = 0; i < jComboBox4.getItemCount(); i++) edvids[i] = edvid6.concat(jComboBox4.getItemAt(i).toString());
        
        String text = "Tabelle konnte nicht angelegt werden, Tabelle vorhanden?";
        boolean erfolg = egt.eingabetabelleAnlegen(edvid6, true);   // alle Parzellen, Struktur und Daten
        if(erfolg) text = "Eingabetabelle erfolgreich angelegt.";
        jLabel9.setText(text);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
// TODO add your handling code here:
        try {
            if(dbconnOpen) dbconn.closeAll();
            
          java.io.PrintWriter ausgabe = new java.io.PrintWriter(new java.io.FileWriter("vis2006.ini"));
          ausgabe.println(jComboBox7.getSelectedIndex());
          ausgabe.println(jTextField1.getText());
          ausgabe.println(datenPfad);
          ausgabe.println(excelPfad);
          ausgabe.println(excelPfad2);
          ausgabe.println(stammvPfad);
          ausgabe.close();
            }
        catch(java.io.IOException e){System.out.println("Error! writing File vis2006.ini");}

        ex.CloseDB();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // Ergebnisbogen Versuch
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jLabel9.setText("");
        String filename="ergebnisbogen.pdf";
        if (ausgabePDF==false) filename="ergebnisbogen.htm";
        boolean ok = ergebnisPDF(filename, true, false); // ganzerVersuch true, keine BZE-Daten
        if (ok){
            try {
                Runtime.getRuntime().exec( " rundll32 url.dll,FileProtocolHandler "+filename);
            } catch ( Exception e2){ System.out.println(e2); }
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // Ergebnisbogen Parzelle
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jLabel9.setText("");
        String filename = "Ergebnisbogen";
        boolean bzedaten = false;
        if(jComboBox7.getSelectedIndex()==3) bzedaten=true;
        String endung=".pdf";
        if (ausgabePDF==false) endung=".htm";
        boolean ok = false;
        
        // BZE-Daten, alle Plots    
        if(bzedaten && jComboBox8.getSelectedIndex()==0){
            int fehler = 0;
            for(int i = 1; i < jComboBox8.getItemCount(); i++){
                System.out.println("------------------------------ " + i + " von " + (jComboBox8.getItemCount()-1));
                jComboBox8.setSelectedIndex(i);
                String plot = jComboBox8.getSelectedItem().toString().trim();
                filename = "Ergebnisbogen_" + plot + endung;
                ok = ergebnisPDF(filename, false, bzedaten);  // ganzerVersuch false
                if(!ok){
                    fehler = fehler + 1;
                    System.out.println("Fehler bei Plot " + plot);
                }
            }
            if(fehler > 0) jLabel9.setText("Fehler bei " + fehler + " Plots!");
            else jLabel9.setText("fertig");
        }
        // Ertragskunde-Daten oder BZE-Daten Einzelplot
        else{
            filename = filename+endung;
            ok = ergebnisPDF(filename, false, bzedaten);     // ganzerVersuch false, 
            if(ok){
                try{
                    Runtime.getRuntime().exec(" rundll32 url.dll,FileProtocolHandler " + filename);
                }catch (Exception e2) { System.out.println(e2);}
                jLabel9.setText("fertig");
            } else {
                jLabel9.setText("Fehler!");
            }
        } 
	 
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Ergebnisbogen
        boolean ausgabe;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jLabel9.setText("");
        int aufx = Integer.parseInt((String)(jComboBox5.getSelectedItem()));
        String filename="Aufnahme.pdf";
        if (ausgabePDF==false) filename="Aufnahme.htm";
        ausgabe = einzelBerechnung(filename,aufx,false);
        if(ausgabe){
            try {
                Runtime.getRuntime().exec( " rundll32 url.dll,FileProtocolHandler "+filename);}
            catch ( Exception e2){ System.out.println(e2); }  
        } else jLabel9.setText("Fehler!");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        jLabel9.setText("");
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        jLabel9.setText("");
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
// checkOutExperimentData
//      tempDatenSichern();
        jLabel9.setText("");        
        CopyExperimentData cde = new CopyExperimentData();
        String datnam ="tempdaten//"+idSelected.substring(0,6)+".mdb";
        File dat = new File(datnam);
        String s = "";
        
        if(dat.exists()){
            Object[] options = { "Ja", "Nein"};     
            int res = javax.swing.JOptionPane.showOptionDialog(this, "Die Datei ist bereits vorhanden. Überschreiben?", 
                "Warnung", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 1);
            if(res==0){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                dat.delete();
                s = cde.checkOutExperimentData(idSelected);
            }    
        }
        else{
           this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
           s=cde.checkOutExperimentData(idSelected);  
        }   
        
        // Eingabetabelle erstellen (für Aufnahmeformular)
        boolean eingabeOk = false;
        String zieldatei = cde.getZielDatei();
        if(zieldatei.length() > 0){               
            Eingabetabelle egt = new Eingabetabelle(this, zieldatei);
            eingabeOk = egt.eingabetabelleAnlegen(edvid6, true);  // Struktur und Daten
        }
        if(! eingabeOk) s = "Fehler! Eingabetabelle konnte nicht erstellt werden.";

        jLabel9.setText(s);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
    // Stammverzeichnis erstellen
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jLabel9.setText("");
        String filename="Stammverz.pdf";
        if (ausgabePDF==false) filename="Stammverz.htm";
        openDataBank();
        Stammverzeichnis Stverz = new Stammverzeichnis();
        Integer maxauf = Integer.parseInt((String)(jComboBox5.getItemAt(jComboBox5.getItemCount()-1)));
        boolean inklNralt = stammvNralt;
        boolean erfolgreich =Stverz.createStammverzeichnis(dbconn,idSelected,maxauf,filename, inklNralt, nachNrSortieren);
        if (erfolgreich) {
            try {
                Runtime.getRuntime().exec(" rundll32 url.dll,FileProtocolHandler " + filename);
            } catch (Exception e2) {System.out.println(e2);}
        } else jLabel9.setText("Fehler!");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
    // Höhenverzeichnis erstellen
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jLabel9.setText("");
        String filename="Hoehenverz.pdf";
        if (ausgabePDF==false) filename="Hoehenverz.htm";
        openDataBank();
        Hoehenverzeichnis hverz = new Hoehenverzeichnis();
        boolean ok = hverz.createHoehenverzeichnis(dbconn,idSelected,filename);
        if (ok){
            try {
                Runtime.getRuntime().exec( " rundll32 url.dll,FileProtocolHandler "+filename);}
            catch ( Exception e2){ System.out.println(e2); }
            jLabel9.setText("Höhenverzeichnis vollständig.");
        } else jLabel9.setText("Fehler: Höhenverzeichnis unvollständig!");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
       
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        if (jComboBox6.getItemCount() > 1) {
            edvid6=(String)(jComboBox6.getSelectedItem());
            setParzNeu();
            setAufNeu();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        if (jComboBox3.getItemCount() > 1) {
            abtSelected=(String)(jComboBox3.getSelectedItem());
            setFlaechenLabel();
            setIdNeu();
            setParzNeu();
            setAufNeu();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        if(jComboBox5.getItemCount()>0) setAufnahmeLabel();    
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        if (jComboBox2.getItemCount() > 1) {
            foaSelected=(String)(jComboBox2.getSelectedItem());
            setAbtNeu();
            setIdNeu();
            setParzNeu();
            setAufNeu();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getItemCount() > 1) {
            landSelected=(String)(jComboBox1.getSelectedItem());
            landSelected=landSelected.trim();
            setFoaNeu();
            setAbtNeu();
            setIdNeu();
            setParzNeu();
            setAufNeu();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
        if (jComboBox4.getItemCount() > 1) {
            parzSelected=(String)(jComboBox4.getSelectedItem());
            idSelected=edvid6+parzSelected;
            jTextField1.setText(idSelected);
            setAufNeu();
            setParzellenLabel();
        }
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       jLabel9.setText("");
       String suchId = jTextField1.getText().trim();
       String erg = loadID(suchId);
       jLabel9.setText(erg);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
    // Bestandes- und Baumartenwerte berechnen (alle Aufnahmen)
         this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
         jLabel9.setText("");       
         int zeilenTemp = 0;
         openWriteableDataBank();
         Vis2006.this.setEnabled(false);         
         try{
             dbconn.Connections[0].setReadOnly(false);
             Statement stmt = dbconn.Connections[0].createStatement();
             //Aufnahmen nach tempAuf kopieren
             stmt.executeUpdate("DELETE FROM tempAuf");
             zeilenTemp = stmt.executeUpdate("INSERT INTO tempAuf SELECT * FROM Auf "//);
                     +"WHERE LEFT(edvid,6) " +
                     "NOT IN ('464511') AND LEFT(edvid,1) NOT IN ('P', 'B')");
              //alte Werte löschen
             stmt.executeUpdate("DELETE FROM Bestandeswerte ");
             stmt.executeUpdate("DELETE FROM Baumartenwerte ");
             stmt.close();
         } catch(Exception e) {e.printStackTrace();}
         
         pm = new ProgressMonitor(this, "Bestandes- und Baumartenwerte berechen"
                 ,"", 0, zeilenTemp);
         // Thread anlegen und starten
         bestandeswerte = new Bestandeswerte();
         bestandeswerte.execute();

    }//GEN-LAST:event_jMenuItem30ActionPerformed

    
private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
//Daten einer Parzelle in lokale DB kopieren   
        jLabel9.setText("");        
        CopyExperimentData cde = new CopyExperimentData();
        String datnam ="tempdaten//"+idSelected+".mdb";
        String s = "";
        File dat = new File(datnam);
        
        if(dat.exists()){
            Object[] options = { "Ja", "Nein"};     
            int res = javax.swing.JOptionPane.showOptionDialog(this, "Die Datei ist bereits vorhanden. Überschreiben?", 
                "Warnung", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 1);
            if(res==0){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                dat.delete();
                s = cde.checkOutParzData(idSelected, parzSelected);
            }            
        }
        else{
           this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
           s=cde.checkOutParzData(idSelected, parzSelected);  
        }
        
        // Eingabetabelle erstellen (für Aufnahmeformular)
        boolean eingabeOk = false;
        String zieldatei = cde.getZielDatei();
        if(zieldatei.length() > 0){ 
            Eingabetabelle egt = new Eingabetabelle(this, zieldatei);
            eingabeOk = egt.eingabetabelleAnlegen(edvid6, true);   // Struktur und Daten 
        }
        if(!eingabeOk) s = "Fehler! Eingabetabelle konnte nicht erzeugt werden.";
        jLabel9.setText(s);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem31ActionPerformed

private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
jLabel9.setText("");
}//GEN-LAST:event_jMenu1MouseClicked

private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jMenu3ActionPerformed

private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
// Bestandes- und Baumartenwerte berechnen (aktueller Versuch)
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
    jLabel9.setText("");       
    int zeilenTemp = 0;
    openWriteableDataBank();
    Vis2006.this.setEnabled(false);         
    try{
        dbconn.Connections[0].setReadOnly(false);
        Statement stmt = dbconn.Connections[0].createStatement();
        //Aufnahmen nach tempAuf kopieren
        stmt.executeUpdate("DELETE FROM tempAuf"); 
        zeilenTemp = stmt.executeUpdate("INSERT INTO tempAuf SELECT * FROM Auf " +
                     "WHERE LEFT(edvid,6) = \'"+idSelected.substring(0,6)+"\'");
        //alte Werte löschen
        stmt.executeUpdate("DELETE FROM Bestandeswerte " +
                "WHERE LEFT(edvid,6) = \'"+idSelected.substring(0,6)+"\'");
        stmt.executeUpdate("DELETE FROM Baumartenwerte " +
                "WHERE LEFT(edvid,6) = \'"+idSelected.substring(0,6)+"\'");
        stmt.close();
    } catch(Exception e) {System.out.println(e);}
    
    pm = new ProgressMonitor(this, "Bestandes- und Baumartenwerte berechen"
            ,"", 0, zeilenTemp);
    // Thread anlegen und starten
    bestandeswerte = new Bestandeswerte();
    bestandeswerte.execute();     

}//GEN-LAST:event_jMenuItem32ActionPerformed

private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
// Excel-Felddaten-Check
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    jLabel9.setText("");
    String fehler = "";

    ExcelFelddatenCheck check = new ExcelFelddatenCheck(this, true, excelPfad);
    excelPfad = check.getPfad();
    fehler = check.getFehler();
     
    if (fehler.length()<5 && fehler.length()>0) jLabel9.setText(fehler+" Hinweis(e)");
    else if(fehler.equals("")) jLabel9.setText("gut");
    else {
        jLabel9.setText("Fehler beim Lesen!");
        JOptionPane.showMessageDialog (this, fehler, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
    }
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem35ActionPerformed

private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
    jLabel9.setText("");
}//GEN-LAST:event_jMenu4ActionPerformed

private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
    jLabel9.setText("");
    if (ausgabePDF == true){
        ausgabePDF=false; jMenuItem6.setText("PDF statt HTML");
    } else { ausgabePDF=true; jMenuItem6.setText("HTML statt PDF");}
}//GEN-LAST:event_jMenuItem6ActionPerformed

private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
    // Datenbankauswahl
    jLabel9.setText("");
    
    dbAuswahl();
    if(jComboBox7.getSelectedIndex() == 2){  // Lokale Datenbank
        if(datenPfad.length() > 0){
            String suchId = jTextField1.getText().trim();
            loadID(suchId);
        } else{
            loadID("");
        }
        
    }
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jComboBox7ActionPerformed

private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
    idSelected = jComboBox8.getSelectedItem().toString().trim();
    // TODO add your handling code here:
}//GEN-LAST:event_jComboBox8ActionPerformed

private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
    // BZE Datenauswertung
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jLabel9.setText("");
        // Zielordner
        idSelected = jComboBox8.getSelectedItem().toString().trim();
        boolean allePlots = false;
        if(idSelected.equals("alle")) allePlots = true;
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(localPath));
        fc.showOpenDialog(this);
        String filename = fc.getSelectedFile().toString();
        if(filename.lastIndexOf(".")<0) filename=filename+".txt";
        else if(!filename.substring(filename.lastIndexOf("."), filename.length()).equalsIgnoreCase(".txt"))
            filename=filename+".txt";
        boolean ok = auswertungBZE(filename, allePlots); // ganzerVersuch false,
        if (ok){
            try {
                Runtime.getRuntime().exec( " rundll32 url.dll,FileProtocolHandler "+filename);
            } catch ( Exception e2){ System.out.println(e2); }
        } else jLabel9.setText("Fehler!");

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));



}//GEN-LAST:event_jMenuItem36ActionPerformed

private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
// Stammverteilungsplan erstellen aus Topcon-Datei
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    jLabel9.setText("");
 
    StammvErstellen sve = new StammvErstellen(this, true, dbconn, idSelected, stammvPfad, true);
    stammvPfad = sve.getPfad();

    jLabel9.setText("fertig");
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem37ActionPerformed

private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
// Stammverteilungsplan: Nullpunkt neu festlegen und Koordinaten entsprechend umrechnen

    // Minimalen x- bzw. y-Wert bestimmen (Bäume und Punkte)
    double minx = 0.0;
    double miny = 0.0;

    Statement stmt = null;
    ResultSet rs = null;
    try{
        stmt = dbconn.Connections[0].createStatement();
        rs = stmt.executeQuery("SELECT min(x) AS minx, min(y) AS miny FROM Stammv WHERE edvid = '"+idSelected+"' "
                + " AND x > -999 AND y > -999;");
        if(rs.next()){
            minx = rs.getDouble("minx");
            miny = rs.getDouble("miny");
        }
    } catch (Exception e){e.printStackTrace();
    } finally {
        try{
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }catch (Exception e) {e.printStackTrace();}
    }
    // Keine Aktualisierung
    if(minx >= 0 && miny >= 0){
        System.out.println("Keine negativen Koordinaten vorhanden");
        String text = "Keine negativen Koordinaten vorhanden";
        JOptionPane.showMessageDialog (this, text, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
    }


    // Koordinaten aktualisieren
    if(minx < 0.0 || miny < 0.0){
        
        // Aufrunden und positive Werte auf 0, Betrag
        if(minx > 0) minx=0;
        if(miny > 0) miny=0;
        minx = Math.abs(Math.floor(minx));
        miny = Math.abs(Math.floor(miny));
        
        // Nachfrage
        String text = "Alle Punkte (Bäume, Ecken, Standpunkte o.ä.) werden um \n \n" +
                " "+minx+" bzw. "+miny+" \n \n in x- bzw. y-Richtung verschoben.\n" +
                " (Werte ggf. bitte in der Akte vermerken!)";
        int abbrechen = JOptionPane.showConfirmDialog(this, text, "Stammverteilungsplan verschieben?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if(abbrechen==0){
            System.out.println("Verschiebung in x-Richtung um: "+minx+" m");
            System.out.println("Verschiebung in y-Richtung um: "+miny+" m");
            Statement stmt2 = null;
            try{
                stmt2 = dbconn.Connections[0].createStatement();
                int update = stmt2.executeUpdate("UPDATE Stammv SET x = x + "+minx+", y = y + "+miny+
                        " WHERE edvid = '"+idSelected+"';");
                System.out.println(update + " Zeilen aktualisiert");
            } catch (Exception e){e.printStackTrace();
            } finally {
                try{
                    if(stmt != null) stmt2.close();
                }catch (Exception e) {e.printStackTrace();}
            }
        } 
    }
}//GEN-LAST:event_jMenuItem38ActionPerformed

private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
    // Sachsen-Anhalt-Daten einlesen
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    jLabel9.setText("");
    
    SachsenAnhaltDaten sad = new SachsenAnhaltDaten(this, true, localPath,
            idSelected.substring(0,6), dbconn);
    String text = sad.getMeldung();

    jLabel9.setText(text);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    
    
}//GEN-LAST:event_jMenuItem39ActionPerformed

private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
    // Stammverteilungsplan prüfen
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    jLabel9.setText("");
    String text = "";

    JFileChooser fc = new JFileChooser(stammvPfad);
    fc.setDialogTitle(" Topcon-Datei auswählen ");
    fc.setApproveButtonText("übernehmen");
    fc.showOpenDialog(this);
    File inputFile = fc.getSelectedFile();
    stammvPfad = inputFile.getAbsolutePath();
    
    StammvErstellen sve = new StammvErstellen(this, true, dbconn, idSelected, stammvPfad, false);
    boolean ok = sve.topconLesen(inputFile, true);  // lesen und Koordinaten in txt ablegen
    if(ok){
        sve.anzeigen();
    }



    jLabel9.setText(text);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem40ActionPerformed

private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
    // Eingabetabelle für Höhen erzeugen
        jLabel9.setText("");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        HöhenEingabetabelle het = new HöhenEingabetabelle(this, true, dbconn, false); // Dialog nicht zeigen
        String ergebnis = het.tabelleErstellen();
        jLabel9.setText(ergebnis);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem41ActionPerformed

private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem43ActionPerformed
    // Eingabetabelle für Höhen löschen
    jLabel9.setText("");
    HöhenEingabetabelle het = new HöhenEingabetabelle(this, true, dbconn, false);  // Dialog nicht zeigen
    String ergebnis = het.tabelleLöschen();
    jLabel9.setText(ergebnis);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem43ActionPerformed

private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem42ActionPerformed
    // Werte aus Eingabetabelle für Höhen nach Baum übernehmen
    jLabel9.setText("");
    HöhenEingabetabelle het = new HöhenEingabetabelle(this, true, dbconn, true);  // Dialog zeigen
    String ergebnis = het.werteÜbertragen();  
    jLabel9.setText(ergebnis);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem42ActionPerformed

private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
    // TODO add your handling code here:
    stammvNralt = false;
    if(jCheckBoxMenuItem1.isSelected()) stammvNralt = true;
}//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

private void jMenu10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu10ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jMenu10ActionPerformed

private void jMenu2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed1
    // TODO add your handling code here:
}//GEN-LAST:event_jMenu2ActionPerformed1

private void jCheckBoxMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem2ActionPerformed
    // TODO add your handling code here:
    uAufsAnzeigen = false;
    if(jCheckBoxMenuItem2.isSelected()) uAufsAnzeigen = true;
}//GEN-LAST:event_jCheckBoxMenuItem2ActionPerformed

private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
    // Excel-Daten in Eingabetabelle einlesen
    jLabel9.setText("");
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    String res = "Fehler!";
    String datei = "";   
    
    // Excel-Datei wählen (-> u.a. welche Parzelle soll bearbeitet werden)
    ExcelDateiWählen edw = new ExcelDateiWählen();
    datei = edw.wählen(this, excelPfad);
    excelPfad = datei; 
    String arbeitsEdvid = datei.substring(datei.lastIndexOf("\\")+1, datei.indexOf(".xls"));
    if(arbeitsEdvid.length() != 8){
        arbeitsEdvid = JOptionPane.showInputDialog(this, "Dateiname der Felddaten scheinbar keine edvid. \n"
                + "Welche Parzelle (8-stellige edvid) soll bearbeitet werden?");
        if(arbeitsEdvid == null) arbeitsEdvid = "";
    }
    
    // Eingabetabelle vorhanden?
    Eingabetabelle eing = new Eingabetabelle(this, datenPfad);
    boolean vorhanden = false;
    int zeilen = eing.getAnzZeilen(arbeitsEdvid);
    if(zeilen >= 0) vorhanden = true;
    
    // Eingabetabelle leeren
    if(vorhanden && zeilen > 0){
        Object text = "Eingabetabelle enthält " +zeilen+ " Datensätze für Parzelle " + arbeitsEdvid + ". \nErsetzen?";
        int ok = JOptionPane.showConfirmDialog(this, text, "Hinweis", JOptionPane.OK_CANCEL_OPTION);
        if(ok == JOptionPane.OK_OPTION){
            eing.parzelleLoeschen(arbeitsEdvid);
            zeilen = 0;
        } 
    }

// Eingabetabelle anlegen
    if(!vorhanden){
        vorhanden = eing.eingabetabelleAnlegen(edvid6, false); // nur leere Tabelle anlegen
        zeilen = 0;
    }
 

    if(vorhanden && zeilen==0 && jComboBox7.getSelectedIndex() == 2 && arbeitsEdvid.length()==8){
        ExcelFelddaten excelFelddaten = new ExcelFelddaten(this, idSelected);
        DBConnection lokalcon= new DBConnection(2);
        
        if(datei.length()>0){
            // Datenbankverbindung
            lokalcon.openDBConnection(DBConnection.ACCESS, 1, datenPfad, "", "", false, true);
            int letztAuf = 0;
            if(jComboBox5.getItemCount()>0) letztAuf = Integer.parseInt((String)(jComboBox5.getItemAt(jComboBox5.getItemCount()-1)));
            res = excelFelddaten.uebernehmen(datei, lokalcon.Connections[1], letztAuf);
            
        } else res = "Nur Excel Dateien können ausgewählt werden";
    } else{
        if(!vorhanden) res = "Eingabetabelle konnte nicht erstellt werden.";
        if(zeilen > 0) res = "Daten konnten nicht übernommen werden, Eingabetabelle nicht leer.";
        if(jComboBox7.getSelectedIndex() != 2) res = "Fehler: Datenübernahme nur in lokale Kopie!";
        if(arbeitsEdvid.length() != 8) res = "Ungültige edvid (Name der Felddaten-Datei).";
     }
    jLabel9.setText(res);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jMenuItem34ActionPerformed

private void jCheckBoxMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem3ActionPerformed
    // Eigenschaften: Stammverzeichnis nach Baumnummer sortieren (nicht nach Art und dann nach Nr.)
    jLabel9.setText("");
    nachNrSortieren = false;
    if(jCheckBoxMenuItem3.isSelected()) nachNrSortieren = true;
}//GEN-LAST:event_jCheckBoxMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Lokale Datenbank öffnen
        jLabel9.setText("");
        if(datenPfad.length()> 0){
            try {
                dbconn.closeAll();
                Process proc = Runtime.getRuntime().exec("cmd /c " + datenPfad);
                /* Zur Fehlersuche ...
                InputStream stderr = proc.getErrorStream();
                InputStreamReader isr = new InputStreamReader(stderr);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("<ERROR>");
                while ( (line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("</ERROR>");
                int exitVal = proc.waitFor();
                System.out.println("Process exitValue: " + exitVal);   // 0 wenn erfolgreich beendet
                 * 
                 */
                
                
            } 
            catch(Exception e) {e.printStackTrace(); }
        }
        else jLabel9.setText("Keine Datei geladen.");
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
        openDataBank();
    }//GEN-LAST:event_formWindowGainedFocus


    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        for (String s: args) {
            if (s.equals("full")) full = true; 
            if (s.equals("plus")) plus = true;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vis2006().setVisible(true);
            }
        });
    }
    

    public boolean ergebnisPDF(String filename, boolean ganzeVersuchsanlage, boolean bzedaten){
        boolean fertig = false;
        openDataBank();
        
        NumberFormat df=NumberFormat.getInstance();
         df=NumberFormat.getInstance(new Locale("en","US"));
         df.setMaximumFractionDigits(1);
         df.setMinimumFractionDigits(1);
         df.setGroupingUsed(false);
         NumberFormat df0=NumberFormat.getInstance();
         df0=NumberFormat.getInstance(new Locale("en","US"));
         df0.setMaximumFractionDigits(0);
         df0.setMinimumFractionDigits(0);
         df0.setGroupingUsed(false);
        try { 

        Document document = new Document();
        PdfWriter writerPDF = null;
        HtmlWriter writerHTML =null;
        
        if (ausgabePDF==true) writerPDF  = PdfWriter.getInstance(document, new FileOutputStream(filename));
        if (ausgabePDF==false) writerHTML = HtmlWriter.getInstance(document, new FileOutputStream(filename));

//
        document.addTitle("NW-FVA Ergebnisbogen für Versuchsparzelle");
        document.addAuthor("NW-FVA Abt. Waldwachstum, Grätzelstr.2, 37079 Göttingen");
        document.addSubject("Versuchsflächenauswertung");
        document.addKeywords("Forst, Versuchwesen");
        document.addCreator("Vis Version " + visdatum);
//
        document.open();
//  Überschrift des Ausdrucks als Tabelle mit 2 Spalten
        Image jpeg = Image.getInstance("ns-ross.jpg");
//        document.add(jpeg);
        Table datatable2 = new Table(2);
        datatable2.setPadding(1);
        datatable2.setSpacing(0);
        datatable2.setBorder(0);
        int headerwidths2[] = {15,85};
        datatable2.setWidths(headerwidths2);
        datatable2.setWidth(100);
        Cell cellBild = new Cell(jpeg);
        cellBild.setBorder(Rectangle.NO_BORDER);
        cellBild.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellBild.setRowspan(2);
        datatable2.addCell(cellBild);
        Cell cell1 = new Cell(new Phrase("Nordwestdeutsche Forstliche Versuchsanstalt - Abt. Waldwachstum ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        datatable2.addCell(cell1);
        Cell cell32 = new Cell(new Phrase("Grätzelstr.2,  37079 Göttingen; Tel. 0551-69401121, http://www.nw-fva.de ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
        cell32.setBorder(Rectangle.NO_BORDER);
        cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
        datatable2.addCell(cell32);
        document.add(datatable2);

        String flaechenName = "";
        String abtName = "";
        String artUndVerszweck = "";
        if (!bzedaten) {
            Statement stmt = dbconn.Connections[0].createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Versfl WHERE edv_id = \'" + idSelected.substring(0, 6) + "\'  ");
            if (rs.next() != true) System.out.print("Fehler: Ergebnisbogen");
            flaechenName = rs.getObject("forstamt").toString();
            abtName = rs.getObject("abt").toString();
            artUndVerszweck = rs.getObject("baumart").toString() + "  " + rs.getObject("vers_zweck").toString();
            rs.close();
            stmt.close();
        } else{
            flaechenName = "BZE-Plot "+idSelected;
            artUndVerszweck = "BZE II";
        }
        
        document.add(new Paragraph(""));
        document.add(new Paragraph("Versuchsfläche: "+flaechenName+"   "+abtName));
        document.add(new Paragraph(artUndVerszweck));

//
//Alle Parzellen einer 6-Stellingen ID bearbeiten
        int nParzellen=1;
        boolean alleAufnahmenOk = false;
        if (ganzeVersuchsanlage == true) nParzellen=jComboBox4.getItemCount(); 
        
        for (int iiii=0;iiii<nParzellen;iiii++){
          if (ganzeVersuchsanlage == true) idSelected=idSelected.substring(0,6)+jComboBox4.getItemAt(iiii);
          else {
              if(!bzedaten)idSelected=idSelected.substring(0,6)+jComboBox4.getSelectedItem();
              else idSelected = jComboBox8.getSelectedItem().toString().trim();
          }
          
          setAufNeu();
          if (ganzeVersuchsanlage == true) idSelected=idSelected.substring(0,6)+jComboBox4.getItemAt(iiii);
          else {
              if(!bzedaten)idSelected=idSelected.substring(0,6)+jComboBox4.getSelectedItem();
              else idSelected = jComboBox8.getSelectedItem().toString().trim();
          }

          String dfText = "";
          if(!bzedaten){
             dfText = "Behandlung:  "+getDurchforstungsartByID(idSelected)+"  "+getDurchforstungsstaerkeByID(idSelected); 
          }
          document.add(new Paragraph(dfText));     

          Table datatable = getTable();
        
// Schleife über alle Aufnahmen
        int nauf = 1;
        if(!bzedaten) nauf = jComboBox5.getItemCount();
        boolean aufAnzeigen[] = new boolean[nauf];
        for (int ii=0;ii<nauf;ii++){
            alleAufnahmenOk = false;
            int aufx = 1;
            aufAnzeigen[ii] = true;
            if(!bzedaten) aufx = Integer.parseInt((String)(jComboBox5.getItemAt(ii)));
            System.out.println("in Berechnung : "+idSelected+"  "+aufx);
            LoadStand lts = new  LoadStand();
            // Bestand ohne Randbäume und ohne Verjüngung laden
            if(!bzedaten){
                st=lts.loadFromDB(this, dbconn, st,idSelected,aufx,true, true, false);
            }
            else st = lts.loadBZE(this, dbconn, st, idSelected, true, true);
    
// Informationen zur Aufnahme extrahieren
            System.out.println("Ergebnisse berechnen...");
            Statement stmt2 = dbconn.Connections[0].createStatement();
            ResultSet rs2=null;
            String auftitel = String.valueOf(aufx);
            if(auftitel.length() < 2) auftitel = "  " + auftitel;
            auftitel =  "Aufnahme " + auftitel;
            String info= "" ;
            String reptext = "";
            int monatsnum = 0;
            if(!bzedaten){
                rs2 = stmt2.executeQuery("SELECT * FROM Auf WHERE edvid = \'"+idSelected+"\' AND auf ="+aufx );
                if (rs2.next()!=true)  System.out.print("Fehler: Ergebnisbogen Query 2");
                monatsnum = rs2.getInt("monat");          
            } else{
                SimpleDateFormat monat = new SimpleDateFormat("MM"); // nur das erste Datum
                rs2 = stmt2.executeQuery("SELECT aufdatum FROM BaumBZE WHERE plot = \'"+idSelected+"\'");
                java.util.Date datum = null;
                if(rs2.next()==true) datum = rs2.getDate("aufdatum");
                monatsnum = Integer.valueOf(monat.format(datum));
            }
            if(monatsnum < 10) auftitel = auftitel + "     " + monatsnum;
            else auftitel = auftitel + "   " + monatsnum;
            double zeit = st.year;
            double zeitraum =0.0;
            AltersDezimale aDez = new AltersDezimale();
            zeit = st.year + aDez.getAltersdezimale(monatsnum);

            auftitel = auftitel + "/"+st.year;
            auftitel = auftitel + "    "+st.size+" ha";

            String typ = "H";
            String bemerk = "";
            if(!bzedaten){
                // Nebenaufnahme ?
                typ = rs2.getObject("typ").toString().trim();
                if (typ.equals("N")) auftitel = auftitel + "  Nebenaufnahme  ";
                if (typ.equals("U")){             
                    if(!uAufsAnzeigen){ 
                        aufAnzeigen[ii] = false;
                        for(int i = 0; i < st.ntrees; i++) if(st.tr[i].out > 1 && st.tr[i].fac>0.0) aufAnzeigen[ii] = true; 
                        if(aufAnzeigen[ii]) auftitel = auftitel + " Ablesung Dauerumfangmessband (nur ausscheidender Bestand)";
                        else auftitel = auftitel + " Ablesung Dauerumfangmessband";
                    } else auftitel = auftitel + " Ablesung Dauerumfangmessband";
                }
                // Bemerkungen übernehmen
                Object bem = rs2.getObject("bemerk");
                if(bem!=null) bemerk = bem.toString().trim();
                if (bemerk.length()>0) info = info + bemerk + "  ";
                // Repräsentationsfaktoren
                reptext = "";
                Object rep = rs2.getObject("repfac");
                if(rep != null) reptext = rep.toString().trim();
                if(reptext.length() > 5){
                    System.out.println("Repräsentationsfaktoren: "+reptext);
                    String art = reptext.substring(0,2);
                    if(isInteger(art)) {
                        
                    }
                    // Weiter ...
                } else if (reptext.length() > 0){
                    // Fehlermeldung, Abbruch
                }
            }
            rs2.close();
            stmt2.close();

            if(aufAnzeigen[ii]){
// Zuwachsberechnung  
// Achtung der Volumenzuwachs wird in die Variable totalPrice geschrieben
// Die Schleife läuft solange bis keine Aufnahme mehr und eine Hauptaufnahmegefunden
// andernfalls Wert = -99 auch wenn Nebenaufnahme            
//  
            for (int i=0; i<st.nspecies; i++) st.sp[i].totalPrice=0.0;

            if (typ.equals("N") || (!uAufsAnzeigen  && typ.equals("U"))) { // wenn Nebenaufnahme
                for (int i=0; i<st.nspecies; i++) st.sp[i].totalPrice=-99.0;
            }
            else {  // wenn aktuelle Aufnahme Typ H (oder U)
                int vorAuf=ii-1;
                boolean vorTypH=false;     // Aufnahmetyp der Voraufnahme ist H (oder U) -> true
                double nebenVolumen[] = new double[50];
                for (int iii=0; iii<50;iii++) nebenVolumen[iii]=0.0;
                // Zuwachsberechnung (Volumen der aktuellen Aufnahme + vergangene Nebenaufnahmen - Volumen der letzten Hauptaufnahme)
                while ( vorAuf >= 0  && vorTypH==false ){//für alle Aufnahmen mind. 1x außer der ersten
                    Stand st2 = new Stand();
                    st2.setSDM(SDM);
                    int aufx2 = Integer.parseInt((String)(jComboBox5.getItemAt(vorAuf)));
                    lts.loadFromDB(this, dbconn, st2,idSelected,aufx2,true,true,false);
                    Statement stmt3 = dbconn.Connections[0].createStatement(); 
                    ResultSet rs3 = stmt3.executeQuery("SELECT * FROM Auf WHERE edvid = \'"+idSelected+"\' AND auf ="+aufx2  );
                    if (rs3.next()!=true)  System.out.print("Fehler: Ergebnisbogen Query 3");
                    String monatstr2 = rs3.getObject("monat").toString().trim();
                    double zeit2 = st2.year + aDez.getAltersdezimale(Integer.parseInt(monatstr2));
                    String typ2 = rs3.getObject("typ").toString().trim();
                    zeitraum=zeit-zeit2;
                    if (typ2.equals("H") || (typ2.equals("U") && uAufsAnzeigen)) vorTypH=true;
                    
                    for (int i=0; i<st.nspecies; i++) {
                        int merk=-9;
                        for (int ik=0; ik<st2.nspecies; ik++) if (st.sp[i].code==st2.sp[ik].code) merk=ik;
                        if (merk>=0 && vorTypH==true) //Art taucht in der Voraufnahme auf 
                            st.sp[i].totalPrice=st.sp[i].totalPrice+(st.sp[i].vol+st.sp[i].vhaout+nebenVolumen[i])-st2.sp[merk].vol ;
                        if (merk>=0 && vorTypH==false) nebenVolumen[i]=nebenVolumen[i]+st2.sp[merk].vhaout ;
                    }
                    vorAuf=vorAuf-1;
//                    System.out.println("JAHR: "+st.year+" "+st2.year+" "+typ+typ2);
                }
            }
            
            
// Test for repräsentativ
            Integer nrep =0;
            for (int l=0;l<st.ntrees;l++) if (st.tr[l].fac != 1.0 && st.tr[l].fac > 0.0) nrep = nrep+1;
            if (nrep>0) info = info + nrep.toString()+" Bäume repräsentativ*"+"   ";
// vorherige Höhenkurve
            Integer vorherigeHK=0;
            for (int l=0;l<st.ntrees;l++) if (st.tr[l].remarks.indexOf("vorherige HK") >= 0) 
                    vorherigeHK = vorherigeHK+1;
            if (vorherigeHK>0) info = info + vorherigeHK.toString()+" Bäume aus vorheriger Höhenkurve"+"   ";
// nächste Höhenkurve
            Integer naechsteHK=0;
            for (int l=0;l<st.ntrees;l++) if (st.tr[l].remarks.indexOf("nächste HK") >= 0) 
                naechsteHK = naechsteHK+1;
            if (naechsteHK>0) info = info+naechsteHK.toString()+" Bäume aus nächster Höhenkurve"+"   ";
// keine Höhenmessung
            boolean hmess = false;
            int tno = 0;
            while (hmess == false && tno < st.ntrees){
                if (st.tr[tno].hMeasuredValue>0) hmess = true;
                tno = tno+1;
            } 
            if (! hmess) info = info + " keine Höhenmessung verfügbar";
                        
            // the first cells spans 19 columns
            Cell cell = new Cell(new Phrase(auftitel , FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setLeading(20);
            cell.setColspan(19);
            cell.setBorder(Rectangle.NO_BORDER);
            if(info.length() > 0) info = "  - " + info.trim() + " -";
            Cell cell2 = new Cell(new Phrase(info, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setLeading(6);
            cell2.setColspan(19);
            cell2.setBorder(Rectangle.NO_BORDER);
            datatable.addCell(cell);
            datatable.addCell(cell2);

            int tablerows=2;
            double sumNha=0.0; double sumGha=0.0; double sumVha=0.0; double sumNhaa=0.0; double sumGhaa=0.0; double sumVhaa=0.0;
            double sumiV=0.0; double ivx=0.0;
            double sumNb=0; double sumNa=0; double sumNe=0;
            double bgrad=0.0;
 
// Für alle Baumarten        
            for (int i = 0; i < st.nspecies; i++) {
// Für die Kollektive
                boolean zfAnsprache = false;
                boolean ouAnsprache = false;
                for(int j = 0; j < st.ntrees; j++){
                    if(zfAnsprache == false && st.tr[j].code==st.sp[i].code && st.tr[j].fac>0.0 && st.tr[j].crop == true) zfAnsprache = true;
                    if(ouAnsprache == false && st.tr[j].code==st.sp[i].code && st.tr[j].fac>0.0 && st.tr[j].ou > 0) ouAnsprache = true;
                }
                
                java.util.List<String> kolls = new ArrayList<String>();
                kolls.add(st.sp[i].spDef.shortName);
                if(zfAnsprache){
                    kolls.add("Z");  // Z-Bäume (wenn OU nicht ausgefüllt)
                    kolls.add("F");  // F-Bäume (wenn OU nicht ausgefüllt)
                }
                if(ouAnsprache){
                    kolls.add("O");  // Oberstand (wenn ZF nicht ausgefüllt)
                    kolls.add("U");  // Unterstand (wenn ZF nicht ausgefüllt)
                }
                if(zfAnsprache && ouAnsprache){
                    kolls.add("ZO");  // Z-Bäume im Oberstand
                    kolls.add("ZU");  // Z-Bäume im Unterstand
                    kolls.add("FO");  // F-Bäume im Oberstand
                    kolls.add("FU");  // F-Bäume im Unterstand
                }

                // Schleife über alle Kollektive
                double gesamtNproArt = 0;
                for (ListIterator it = kolls.listIterator(); it.hasNext(); ) {
                    String koll = it.next().toString();
 // Alter 
                    Integer amin=999;
                    Integer amax=0;
                    for (int l = 0; l < st.ntrees; l++) {
                        if (st.tr[l].code == st.sp[i].code && st.tr[l].fac > 0.0) {
                            if (koll.equals(st.sp[i].spDef.shortName) && amin > st.tr[l].age)amin = st.tr[l].age;
                            if (koll.equals(st.sp[i].spDef.shortName) && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1 && amax < st.tr[l].age) amax = st.tr[l].age;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2 && amin > st.tr[l].age) amin = st.tr[l].age;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2 && amax < st.tr[l].age) amax = st.tr[l].age;
                        }
                    }
                    String alt=amin.toString();
                    if (amin < amax) alt=alt+"-"+amax.toString();
                  
// Anzahl vorhandenen der Bäume (alle Bäume)
                    double nb =0;
                    if(!(typ.equals("U") && !uAufsAnzeigen)) for (int l=0;l<st.ntrees;l++) {
                        if (st.tr[l].code==st.sp[i].code && st.tr[l].out < 1 && st.tr[l].fac>0.0){
                            if (koll.equals(st.sp[i].spDef.shortName)) nb=nb+st.tr[l].fac;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0) nb=nb+st.tr[l].fac;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0) nb=nb+st.tr[l].fac;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1) nb=nb+st.tr[l].fac;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2) nb=nb+st.tr[l].fac;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1) nb=nb+st.tr[l].fac;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2) nb=nb+st.tr[l].fac;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1) nb=nb+st.tr[l].fac;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2) nb=nb+st.tr[l].fac;
                        }
                    }
                    
// Anzahl der entfernten Bäume (alle Bäume)
                    double na =0;
                    for (int l=0;l<st.ntrees;l++) {
                        if (st.tr[l].code==st.sp[i].code && st.tr[l].out > 1 && st.tr[l].fac>0.0){
                            if (koll.equals(st.sp[i].spDef.shortName)) na=na+st.tr[l].fac;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0) na=na+st.tr[l].fac;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0) na=na+st.tr[l].fac;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1) na=na+st.tr[l].fac;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2) na=na+st.tr[l].fac;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1) na=na+st.tr[l].fac;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2) na=na+st.tr[l].fac;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1) na=na+st.tr[l].fac;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2) na=na+st.tr[l].fac;
                        }
                    }

// Anzahl  der einwachsenden Bäume (alle Bäume)
                    double ne = 0;
                    for (int l = 0; l < st.ntrees; l++) {
                        if (st.tr[l].code == st.sp[i].code && st.tr[l].remarks.indexOf("Einw") > -1 && st.tr[l].fac > 0.0) {
                            if (koll.equals(st.sp[i].spDef.shortName)) ne = ne + 1;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0) ne = ne + 1;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0) ne = ne + 1;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1) ne = ne + 1;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2) ne = ne + 1;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1) ne = ne + 1;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2) ne = ne + 1;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1) ne = ne + 1;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2) ne = ne + 1;
                        }
                    }
                       
// Zuwachs               
                    String ivStr = "";
                    ivx = 0.0;
                    if (koll.equals(st.sp[i].spDef.shortName) && st.sp[i].totalPrice >= 0.0 && (nb + na + ne > 0) && zeitraum > 0.9) {
                        ivx = Math.round(10.0 * st.sp[i].totalPrice / zeitraum) / 10.0;
                        ivStr = df.format(ivx);
                    } 
                
//verbleibenden Bestand berechen (nur Bäume mit d >= 7 cm)
                    double ghax = 0.0;
                    double nhax = 0.0;
                    double vhax = 0.0;
                    String ghaxStr = "";
                    String vhaxStr = "";
                    String dgxStr= "";
                    String hgxstr = "";
                    boolean baumtrue = true;
                    if(!(typ.equals("U") && !uAufsAnzeigen)) for (int l = 0; l < st.ntrees; l++) {
                        if (st.tr[l].code == st.sp[i].code && st.tr[l].out < 0 && st.tr[l].d>=7.0) {
                            baumtrue = false;
                            if (koll.equals(st.sp[i].spDef.shortName)) baumtrue = true;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0) baumtrue = true;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0) baumtrue = true;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1) baumtrue = true;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2) baumtrue = true;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1) baumtrue = true;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2) baumtrue = true;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1) baumtrue = true;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2) baumtrue = true;
                            if (baumtrue == true) {
                                nhax = nhax + st.tr[l].fac;
                                ghax = ghax + st.tr[l].fac * Math.PI * Math.pow((st.tr[l].d / 200.0), 2.0);
                                vhax = vhax + st.tr[l].fac * st.tr[l].v;
                            }
                        }
                    }

                    if (st.size > 0) {
                        nhax = nhax / st.size;
                        ghax = ghax / st.size;
                        vhax = vhax / st.size;
                    }  
                    if (nhax > 0){
                        double dgx = 200.0 * Math.sqrt(ghax / (nhax * Math.PI));
                        dgxStr = df.format(dgx);
                        ghaxStr = df.format(ghax);
                        vhaxStr = df0.format(vhax);
                    }
                    if(st.sp[i].hg <= 0.0) vhaxStr = "";   // keine Volumenangabe ohne Höhenwert
                    if (koll.equals(st.sp[i].spDef.shortName)  && st.sp[i].hg > 0 && nhax > 0) hgxstr = df.format(st.sp[i].hg);
                    if(vhax - ivx < 1){      // 13.10.2010: wenn keine Höhen in Voraufnahme, Vor-iV = 0 und ivx = Vorrat
                        ivStr = "";
                        ivx = 0;

                    }
//ausscheidenden Bestand berechen (nur Bäume mit d >= 7 cm)
                    double ghaax = 0.0;
                    double nhaax = 0.0;
                    double vhaax = 0.0;
                    String ghaaxStr = "";
                    String vhaaxStr = "";
                    String dgaxStr = "";
                    for (int l = 0; l < st.ntrees; l++) {
                        if (st.tr[l].code == st.sp[i].code && st.tr[l].out > 0 && st.tr[l].d>=7.0) {
                            baumtrue = false;
                            if (koll.equals(st.sp[i].spDef.shortName) ) baumtrue = true;
                            if (koll.equals("Z") && st.tr[l].crop == true && st.tr[l].ou == 0) baumtrue = true;
                            if (koll.equals("F") && st.tr[l].crop != true && st.tr[l].ou == 0) baumtrue = true;
                            if (koll.equals("O") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 1) baumtrue = true;
                            if (koll.equals("U") && !zfAnsprache && st.tr[l].crop != true && st.tr[l].ou == 2) baumtrue = true;
                            if (koll.equals("ZO") && st.tr[l].crop == true && st.tr[l].ou == 1) baumtrue = true;
                            if (koll.equals("ZU") && st.tr[l].crop == true && st.tr[l].ou == 2) baumtrue = true;
                            if (koll.equals("FO") && st.tr[l].crop != true && st.tr[l].ou == 1) baumtrue = true;
                            if (koll.equals("FU") && st.tr[l].crop != true && st.tr[l].ou == 2) baumtrue = true;
                            if (baumtrue == true) {
                                nhaax = nhaax + st.tr[l].fac;
                                ghaax = ghaax + st.tr[l].fac * Math.PI * Math.pow((st.tr[l].d / 200.0), 2.0);
                                vhaax = vhaax + st.tr[l].fac * st.tr[l].v;
                            }
                        }
                    }

                    if (st.size > 0) {
                        nhaax = nhaax / st.size;
                        ghaax = ghaax / st.size;
                        vhaax = vhaax / st.size;
                    }
                    if (nhaax > 0){
                        double dgax = 200.0 * Math.sqrt(ghaax / (nhaax * Math.PI));
                        dgaxStr = df.format(dgax);
                        ghaaxStr = df.format(ghaax);
                        if(vhaax > 0.0) vhaaxStr = df0.format(vhaax);
                    }
// Spaltensummen berechnen
                    if (koll.equals(st.sp[i].spDef.shortName) ) {
                        sumNha = sumNha + nhax;
                        sumGha = sumGha + ghax;
                        sumVha = sumVha + vhax;
                        sumNhaa = sumNhaa + nhaax;
                        sumGhaa = sumGhaa + ghaax;
                        sumVhaa = sumVhaa + vhaax;
                        sumiV = sumiV + ivx;
                        sumNb = sumNb + nb;
                        sumNa = sumNa + na;
                        sumNe = sumNe + ne;

                    }
// D100
                    String d100str = "";
                    // D100 bezieht sich auf den verbleibenden Bestand (ebenso H100, s.u.)
                    //if (koll.equals(st.sp[i].spDef.shortName)  && nhax + nhaax > 100) d100str = df.format(st.sp[i].d100);
                    if (koll.equals(st.sp[i].spDef.shortName)  && nhax > 100) d100str = df.format(st.sp[i].d100);
// H100            
                    String h100str = "";
                    if (koll.equals(st.sp[i].spDef.shortName)  && nhax > 100 && st.sp[i].h100 > 0.0) h100str = df.format(st.sp[i].h100);
// Ertragsklasse und Bestockungsgrad
                    String ekl = "";
                    String bgradStr = "";
                    if (koll.equals(st.sp[i].spDef.shortName)  && !typ.equals("N")) {     // nur für Baumarten insgesamt und Hauptaufnahmen
                        Ertragstafel et = new Ertragstafel(localPath);
                        int age = amax;     //! nur amax!
                        Ertragstafelzeile etz = et.werteBerechnen(st.sp[i], age);

                        if (etz.ekl != -9999.0) {
                            ekl = etz.getEkl().toString();
                            bgrad = bgrad + (ghax)/etz.gha;
                            if(st.nspecies == 1) bgradStr = df.format(bgrad);
                        } 
                    }           
                                  
// Ergebnis anzeigen                

                    if (ne + na + nb > 0 && ne+na+nb != gesamtNproArt) {
                        if(koll.equals(st.sp[i].spDef.shortName)) gesamtNproArt = ne+na+nb;
                        Cell cx1 = null;
                        cx1 = new Cell(new Phrase(koll, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx1);
                        Cell cx2 = new Cell(new Phrase(alt, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx2);
                        Cell cx3 = new Cell(new Phrase(df0.format(nb), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                        datatable.addCell(cx3);
                        Cell cx4 = new Cell(new Phrase(df0.format(na), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                        datatable.addCell(cx4);
                        Cell cx5 = new Cell(new Phrase(df0.format(ne), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                        datatable.addCell(cx5);
                        Cell cx6 = new Cell(new Phrase(d100str, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx6);
                        Cell cx7 = new Cell(new Phrase(h100str, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx7);
                        Cell cx8 = new Cell(new Phrase(dgxStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx8);
                        Cell cx9 = new Cell(new Phrase(hgxstr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx9);
                        Cell cx10 = new Cell(new Phrase(df0.format(nhax), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx10);
                        Cell cx11 = new Cell(new Phrase(ghaxStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx11);
                        Cell cx12 = new Cell(new Phrase(vhaxStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx12);
                        Cell cx13 = new Cell(new Phrase(bgradStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx13);
                        Cell cx14 = new Cell(new Phrase(dgaxStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx14);
                        Cell cx15 = new Cell(new Phrase(df0.format(nhaax), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx15);
                        Cell cx16 = new Cell(new Phrase(ghaaxStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx16);
                        Cell cx17 = new Cell(new Phrase(vhaaxStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx17);
                        Cell cx18 = new Cell(new Phrase(ekl, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx18);
                        Cell cx19 = new Cell(new Phrase(ivStr, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        datatable.addCell(cx19);
                        tablerows = tablerows + 1;
                    }

                } //Ende Kollektive
            } //Ende Baumarten
            

// Schreiben der Summen  
            if (st.nspecies > 1) {
                Cell cx1 = new Cell(new Phrase("SUM", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx1);
                Cell cx2 = new Cell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                datatable.addCell(cx2);
                Cell cx3 = new Cell(new Phrase(df0.format(sumNb), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                datatable.addCell(cx3);
                Cell cx4 = new Cell(new Phrase(df0.format(sumNa), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                datatable.addCell(cx4);
                Cell cx5 = new Cell(new Phrase(df0.format(sumNe), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                datatable.addCell(cx5);
                Cell cx6 = new Cell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                datatable.addCell(cx6);
                Cell cx7 = new Cell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                datatable.addCell(cx7);
                Cell cx8 = new Cell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                datatable.addCell(cx8);
                Cell cx9 = new Cell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                datatable.addCell(cx9);
                Cell cx10 = new Cell(new Phrase(df0.format(sumNha), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx10);
                Cell cx11 = new Cell(new Phrase(df.format(sumGha), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx11);
                Cell cx12 = new Cell(new Phrase(df0.format(sumVha), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx12);
                Cell cx13 = new Cell(new Phrase(df.format(bgrad), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx13);
                Cell cx14 = new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx14);
                Cell cx15 = new Cell(new Phrase(df0.format(sumNhaa), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx15);
                Cell cx16 = new Cell(new Phrase(df.format(sumGhaa), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx16);
                Cell cx17 = new Cell(new Phrase(df0.format(sumVhaa), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx17);
                Cell cx18 = new Cell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx18);
                Cell cx19 = new Cell(new Phrase(df.format(sumiV), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                datatable.addCell(cx19);
                if(nrep > 0){
                    Cell repcell = new Cell(new Phrase("* Repräsentationsfaktoren: "+reptext, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                    repcell.setColspan(19);
                    repcell.setBorder(Rectangle.NO_BORDER);
                    datatable.addCell(repcell);
                }
                
                tablerows = tablerows + 1;
            }

            if (ausgabePDF == true) {             
                if (!writerPDF.fitsPage(datatable,40)) { // es muss auch für den Hinweistext noch Platz sein
                    for (int kk = 0; kk < tablerows; kk++)datatable.deleteLastRow();
                    ii--;
                    document.add(datatable);
                    document.newPage();
                    datatable = getTable();
                    System.out.println("Neue Seite");
                }
            }
            } else{  // Wenn Aufnahme nicht angezeigt werden soll -> nur Info anzeigen
                Cell cell = new Cell(new Phrase(auftitel , FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setLeading(20);
                cell.setColspan(19);
                cell.setBorder(Rectangle.NO_BORDER);
                datatable.addCell(cell);
                
                if (ausgabePDF == true) {             
                if (!writerPDF.fitsPage(datatable,40)) { // es muss auch für den Hinweistext noch Platz sein
                    datatable.deleteLastRow();
                    ii--;
                    document.add(datatable);
                    document.newPage();
                    datatable = getTable();
                    System.out.println("Neue Seite");
                }
            }
                
            }

            alleAufnahmenOk = true;

        } // Ende der Schleife über alle Aufnahmen
            document.add(datatable);
// Datum
        HeuteString datum = new HeuteString();
        String datumStr = datum.get();

        Paragraph hinweis = new Paragraph(new Chunk("Die Verwendung der Daten ohne Genehmigung der NW-FVA ist nicht erlaubt!       "+datumStr, new Font(Font.ITALIC, 8)));
        hinweis.setLeading(14);
        document.add(hinweis);
        
        if (iiii < nParzellen-1 && ausgabePDF==true ) document.newPage();

        }  // Ende der Schleife über alle Parzellen
        document.close();
        if(alleAufnahmenOk) fertig = true;
        } catch ( Exception e){System.out.println("Fehler creating "+filename+" :");
            e.printStackTrace();
        }
        idSelected = idSelected.substring(0,6)+jComboBox4.getSelectedItem(); // zurücksetzen
        return fertig;
    }
    
    private Table getTable() throws BadElementException, DocumentException {
        Table datatable = new Table(19);
        datatable.setPadding(1);
        datatable.setSpacing(0);
        int headerwidths[] = {6, 6, 4, 4, 4,   5, 5, 5, 5, 6, 6, 6, 4,    5, 6, 6, 6,    5, 6};
        datatable.setWidths(headerwidths);
        datatable.setWidth(100);
        // first Row
        String parzStr="Parzelle ID "+((String)(idSelected));
        Cell cell1 = new Cell(new Phrase(parzStr, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setColspan(5);
        cell1.setRowspan(2);
        datatable.addCell(cell1);
        
        Cell cell1a = new Cell(new Phrase("Derbholz", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        cell1a.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1a.setColspan(14);
        datatable.addCell(cell1a);
        Cell cell2 = new Cell(new Phrase("verbleibend", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setColspan(8);
        datatable.addCell(cell2);
        Cell cell3 = new Cell(new Phrase("ausscheidend", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setColspan(4);
        datatable.addCell(cell3);
        Cell cell4 = new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setColspan(2);
        datatable.addCell(cell4);
//
        Cell ck1 = new Cell(new Phrase("Art", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        ck1.setRowspan(2);
        datatable.addCell(ck1);
        Cell ck2 = new Cell(new Phrase("Alter", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        ck2.setRowspan(2);
        datatable.addCell(ck2);
        Cell ck3 = new Cell(new Phrase("nV", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        ck3.setRowspan(2);
        datatable.addCell(ck3);
        Cell ck4 = new Cell(new Phrase("nA", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        ck4.setRowspan(2);
        datatable.addCell(ck4);
        Cell ck5 = new Cell(new Phrase("nE", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        ck5.setRowspan(2);
        datatable.addCell(ck5);
        Cell ck6 = new Cell(new Phrase("D100", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck6);
        Cell ck7 = new Cell(new Phrase("H100", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck7);
        Cell ck8 = new Cell(new Phrase("Dg", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck8);
        Cell ck9 = new Cell(new Phrase("Hg", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck9);
        Cell ck10 = new Cell(new Phrase("N", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck10);
        Cell ck11 = new Cell(new Phrase("G", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck11);
        Cell ck12 = new Cell(new Phrase("V", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck12);
        Cell ck13 = new Cell(new Phrase("B°", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck13);
        Cell ck14 = new Cell(new Phrase("Dg", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck14);
        Cell ck15 = new Cell(new Phrase("N", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck15);
        Cell ck16 = new Cell(new Phrase("G", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck16);
        Cell ck17 = new Cell(new Phrase("V", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck17);
        Cell ck18 = new Cell(new Phrase("Ekl", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck18);
        Cell ck19 = new Cell(new Phrase("iV", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
        datatable.addCell(ck19);

        Cell ck206 = new Cell(new Phrase("cm", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck206);
        Cell ck207 = new Cell(new Phrase("m", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck207);
        Cell ck208 = new Cell(new Phrase("cm", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck208);
        Cell ck209 = new Cell(new Phrase("m", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck209);
        Cell ck210 = new Cell(new Phrase("St/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck210);
        Cell ck211 = new Cell(new Phrase("m²/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck211);
        Cell ck212 = new Cell(new Phrase("m³/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck212);
        Cell ck213 = new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck213);
        Cell ck214 = new Cell(new Phrase("cm", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck214);
        Cell ck215 = new Cell(new Phrase("St/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck215);
        Cell ck216 = new Cell(new Phrase("m²/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck216);
        Cell ck217 = new Cell(new Phrase("m³/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck217);
        Cell ck218 = new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck218);
        Cell ck219 = new Cell(new Phrase("m³/ha", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        datatable.addCell(ck219);
        datatable.endHeaders();
   
        return datatable;
    }
    
    public void setLandNeu () {
         try{          

               Statement stmt = dbconn.Connections[0].createStatement(); 
               ResultSet rs = stmt.executeQuery("SELECT * FROM Land ORDER BY land");
               while(rs.next()){
                    String land = rs.getObject("landname").toString();
                    if (landNoSelected==0) landNoSelected=rs.getInt("land");
                    land=land.trim();
                    if (land==null || land.length()<1) break;
                    jComboBox1.addItem(land);
               }

         landSelected=(String)(jComboBox1.getSelectedItem());
        

         }
         catch(Exception e){System.out.println("Fehler in setLand: "+e);}

    }
    
    public void setFoaNeu() {
       jComboBox2.removeAllItems();
       try{
           Statement stmt = dbconn.Connections[0].createStatement();
           ResultSet rs = stmt.executeQuery("select * from Land where landname = \'"+landSelected+"\'  ");
           String landNoSelected = null;
           if (rs.next()) landNoSelected = rs.getObject("land").toString();
           int lno = new Integer(landNoSelected);
           Statement stmt2 = dbconn.Connections[0].createStatement();
           ResultSet rs2 = stmt2.executeQuery("select * from Forstaemter where land = "+lno+" ORDER BY  forstamt ");
//           rs = stmt.executeQuery("select * from Forstaemter where land = \'"+landNoSelected+"\' ORDER BY  forstamt ");
           while(rs2.next()){
                String foa = rs2.getObject("forstamt").toString();
                foa=foa.trim();
                if (foa==null || foa.length()<1) break;
                jComboBox2.addItem(foa);
                }
           foaSelected=(String)(jComboBox2.getSelectedItem());
         }
         catch(Exception e){System.out.println("setFoaNeu: "+e);}
    }

    public void setAbtNeu() {
       jComboBox3.removeAllItems();
       try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select abt, forstamt from Versfl where forstamt = \'"+foaSelected+"\' ORDER BY  abt ");
//           ResultSet rs = stmt.executeQuery("select * from Versfl where forstamt = \'"+foaSelected+"\' ORDER BY  abt ");
           while(rs.next()){
                String abt = rs.getObject("abt").toString();
                abt=abt.trim();
                if (abt==null || abt.length()<1) break;
                jComboBox3.addItem(abt);
           }
           abtSelected=(String)(jComboBox3.getSelectedItem());
      }
       catch(Exception e){System.out.println("setAbtNeu: "+e);}
    }
    
    public void setParzNeu() {
       jComboBox4.removeAllItems();
       try{          

               Statement stmt = dbconn.Connections[0].createStatement(); 
               ResultSet rs = stmt.executeQuery("select parzelle from Parz where edvid = \'"+edvid6+"\' ORDER BY  parzelle" );
               while(rs.next()){
                    String par = rs.getObject("parzelle").toString();
                    par=par.trim();
                    if (par==null || par.length()<1) break;
                    jComboBox4.addItem(par);
               }
        parzSelected=(String)(jComboBox4.getSelectedItem());
        idSelected=edvid6+parzSelected;
        jTextField1.setText(idSelected);
       }
       catch(Exception e){System.out.println("setParzNeu: "+e);}
       setParzellenLabel();
    }
    
    public void setAufNeu() {
       jComboBox5.removeAllItems();
       try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Auf where edvid = \'"+idSelected+"\' ORDER BY auf ");
           while(rs.next()){
                int aufn = rs.getInt("auf");
                if (aufn < 1) break;
                Integer aufni =aufn;
                jComboBox5.addItem(aufni.toString());
                jComboBox5.setSelectedIndex(jComboBox5.getItemCount()-1);
            }
            aufSelected=(String)(jComboBox5.getSelectedItem());
       }
       catch(Exception e){System.out.println("setAufNeu: "+e);}
    }    
    public void setIdNeu() {
       jComboBox6.removeAllItems();
       try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select forstamt, abt, edv_id from Versfl where forstamt = \'"+foaSelected+"\'  And abt = \'"+abtSelected+"\'");
           while(rs.next()){
                String edvidx = "";
                Object edvidobj = rs.getObject("edv_id");
                if(edvidobj != null) edvidx = edvidobj.toString();
                jComboBox6.addItem(edvidx);
                }
           edvid6=(String)(jComboBox6.getSelectedItem());
           jTextField1.setText(edvid6);
       }
       catch(Exception e){System.out.println("setIdNeu: "+e);}
       setFlaechenLabel();
    }    
    
    public boolean einzelBerechnung(String filename, int aufnahmeNr, boolean autoDatenergänzung){       
System.out.println("Start einzelBerechnung");
        boolean abbrechen = false;
        openDataBank();
        NumberFormat df=NumberFormat.getInstance();
        df=NumberFormat.getInstance(new Locale("en","US"));
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        df.setGroupingUsed(false);
        NumberFormat df0=NumberFormat.getInstance();
        df0=NumberFormat.getInstance(new Locale("en","US"));
        df0.setMaximumFractionDigits(0);
        df0.setMinimumFractionDigits(0);
        df0.setGroupingUsed(false);
        try { 
              Document document = new Document();
              PdfWriter writerPDF = null;
              HtmlWriter writerHTML =null;
        
              if (ausgabePDF==true) writerPDF  = PdfWriter.getInstance(document, new FileOutputStream(filename));
              if (ausgabePDF==false) writerHTML = HtmlWriter.getInstance(document, new FileOutputStream(filename));
//
              document.addTitle("NW-FVA Ergebnisbogen für Versuchsparzelle");
              document.addAuthor("NW-FVA Abt. Waldwachstum, Grätzelstr.2, 37079 Göttingen");
              document.addSubject("Versuchsflächenauswertung");
              document.addKeywords("Forst, Versuchwesen");
              document.addCreator("Vis Version " + visdatum);
//
              document.open();
//  Überschrift des Ausdrucks als Tabelle mit 2 Spalten
              Image jpeg = Image.getInstance("ns-ross.jpg");
//        document.add(jpeg);
              Table datatable2 = new Table(2);
              datatable2.setPadding(1);
              datatable2.setSpacing(0);
              datatable2.setBorder(0);
              int headerwidths2[] = {15,85};
              datatable2.setWidths(headerwidths2);
              datatable2.setWidth(100);
              Cell cellBild = new Cell(jpeg);
              cellBild.setBorder(Rectangle.NO_BORDER);
              cellBild.setHorizontalAlignment(Element.ALIGN_CENTER);
              cellBild.setRowspan(2);
              datatable2.addCell(cellBild);
              Cell cell1 = new Cell(new Phrase("Nordwestdeutsche Forstliche Versuchsanstalt - Abt. Waldwachstum ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
              cell1.setBorder(Rectangle.NO_BORDER);
              cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
              datatable2.addCell(cell1);
              Cell cell32 = new Cell(new Phrase("Grätzelstr.2,  37079 Göttingen; Tel. 0551-69401121, http://www.nw-fva.de ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
              cell32.setBorder(Rectangle.NO_BORDER);
              cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
              datatable2.addCell(cell32);
//
              Statement stmt3 = dbconn.Connections[0].createStatement(); 
              ResultSet rs3 = stmt3.executeQuery("select * from Versfl where edv_id = \'"+idSelected.substring(0,6)+"\'  "   );
              if (rs3.next()!=true)  System.out.print("Fehler: 3. Query");
              String flaechenName= rs3.getObject("forstamt").toString();
              String abtName=rs3.getObject("abt").toString();
              String artUndVerszweck="";
              artUndVerszweck = rs3.getObject("baumart").toString()+"  "+rs3.getObject("vers_zweck").toString();
              String dfText = "    Behandlung:  "+getDurchforstungsartByID(idSelected)+"  "+getDurchforstungsstaerkeByID(idSelected);

              Statement stmt2 = dbconn.Connections[0].createStatement(); 
              ResultSet rs2 = stmt2.executeQuery("select * from Auf where edvid = \'"+idSelected+"\' AND auf ="+aufnahmeNr);
              if (rs2.next()!=true)  System.out.print("Fehler: 2. Query");
              String info = rs2.getObject("monat").toString();
              double zeit = st.year;
              double zeitraum =0.0;
              int monat = Integer.parseInt(info);
              if (monat == 5) zeit=st.year+0.25;
              if (monat == 6) zeit=st.year+0.5;
              if (monat == 7) zeit=st.year+0.75;
              if (monat >= 8) zeit=st.year+1.0;
              String jahr = rs2.getObject("jahr").toString();
              String typ = rs2.getObject("typ").toString();
              Double flha =rs2.getDouble("flha");
// Bestand ohne Randbäume laden
              LoadStand lts = new  LoadStand();
              st=lts.loadFromDB(this, dbconn, st, idSelected,aufnahmeNr,false,true,false);
              abbrechen = lts.abbrechen;
              if(!abbrechen){ 
// Informationen zur Aufnahme extrahieren
                  System.out.println("in Berechnung : "+idSelected+"  "+aufnahmeNr);
                  for (int i=0; i<st.nspecies; i++)
                  {
// Wird nur erstellt, wenn gültige Bäume auf der Fläche, also keine Randbäume oder
// extra Höhen Bäume
                      double nGueltig =0.0;
                      for (int ik=0; ik<st.ntrees;ik++)
                          if (st.tr[ik].code==st.sp[i].code && st.tr[ik].fac > 0)
                              nGueltig = nGueltig+st.tr[ik].fac;
// Seitenkopf                      
                      if (nGueltig > 0){                  
                          document.add(datatable2);
                          document.add(new Paragraph(""));
                          document.add(new Paragraph("Versuchsfläche: "+flaechenName+"   "+abtName+"   Parzelle: "+idSelected.substring(6,8)));
                          document.add(new Paragraph("                       "+dfText));

                          Font textfont = new Font (Font.TIMES_ROMAN, 12, Font.NORMAL);
//              
                          Paragraph ph1 = new Paragraph();
                          ph1.add(new Chunk("ID: "+idSelected, new Font(Font.BOLD, 12)));
                          ph1.add(new Chunk("  Aufnahme: "+aufnahmeNr+"  Monat: "+monat+" Jahr: "+jahr+"  Flächengröße[ha]: "+flha, textfont));
                          document.add(ph1);
      
// Baumart
                          String botName = st.sp[i].spDef.latinName.substring(0, st.sp[i].spDef.latinName.indexOf("http")).trim();
                          document.add(new Paragraph(new Chunk(st.sp[i].spDef.shortName+"  "+st.sp[i].code+"   "+
                                  st.sp[i].spDef.longName+"   "+botName, textfont)));
// Referenzen                
                          double refBon=0.0; double n=0.0;
                          for (int kk=0;kk<st.ntrees;kk++)
                              if (st.tr[kk].code==st.sp[i].code) {
                                  refBon=refBon+st.tr[kk].si*st.tr[kk].fac;
                                  n=n+st.tr[kk].fac;
                              }
                          if (n>0) refBon=refBon/n;
                          NumberFormat df2=NumberFormat.getInstance();
                          df2=NumberFormat.getInstance(new Locale("en","US"));
                          df2.setMaximumFractionDigits(1);
                          df2.setMinimumFractionDigits(1);
                          document.add(new Paragraph(new Chunk("Grundfläche:" + df2.format(st.sp[i].percBA) +
                                  "% u. Kronenschirmfläche:" + df2.format(st.sp[i].percCSA) +
                                  "% vom Bestand, Bonität " + df2.format(refBon) + "m Alter 100", textfont)));
// Datenübersicht
                          int nbhd = 0;
                          int nhoh = 0;
                          int nka = 0;
                          int nkb = 0;
                          int nxy = 0;
                          int nbhdU7 = 0;
                          int nhohU7 = 0;
                          int nkaU7 = 0;
                          int nkbU7 = 0;
                          int nxyU7 = 0;
                          for (int j = 0; j < st.ntrees; j++) {
                              if (st.tr[j].code == st.sp[i].code) {
                                  if (st.tr[j].d > 0 && st.tr[j].fac > 0.0){
                                      if(st.tr[j].d >= 7.0) nbhd = nbhd + 1;
                                      else nbhdU7 = nbhdU7 + 1;
                                  }
                                  if (st.tr[j].hMeasuredValue > 0){
                                      if(st.tr[j].d >= 7.0) nhoh = nhoh + 1;
                                      else nhohU7 = nhohU7 + 1;
                                  }
                                  if (st.tr[j].remarks.indexOf("A") > -1){
                                      if(st.tr[j].d >= 7.0) nka = nka + 1;
                                      else nkaU7 = nkaU7 + 1;
                                  }
                                  if (st.tr[j].remarks.indexOf("B") > -1){
                                      if(st.tr[j].d >= 7.0) nkb = nkb + 1;
                                      else nkbU7 = nkbU7 + 1;
                                  }
                                  if (st.tr[j].remarks.indexOf("X") > -1){
                                      if(st.tr[j].d >= 7.0) nxy = nxy + 1;
                                      else nxyU7 = nxyU7 + 1;
                                  }
                              }
                          }
                          Paragraph messw1 = new Paragraph(new Chunk("Gemessene Werte ab BHD 7 cm (unter 7 cm): BHD=" + nbhd + " (" +nbhdU7+ ")   "+
                                  "Höhen=" + nhoh  + " (" +nhohU7+ ")", textfont));
                          document.add(messw1);
                          Paragraph messw2 = new Paragraph(new Chunk("            KAnsatz=" + nka  + " (" +nkaU7+ ")   " +
                                  "KBreite=" + nkb  + " (" +nkbU7+ ")   " + "Koord.=" + nxy + " (" +nxyU7+ ")", textfont));
                          messw2.setIndentationLeft(10);
                          document.add(messw2);
                          df2.setMaximumFractionDigits(4);
                          df2.setMinimumFractionDigits(4);
                          df2.setGroupingUsed(false);
                          HeightCurve m = new HeightCurve();
//???                st.sp[i].heightcurveUsed= m.getHeightCurveName(st.sp[i].spDef.heightCurve);
                          document.add(new Paragraph(new Chunk("Höhenkurve: " + st.sp[i].heightcurveUsed + "   " +
                                  "b0=" + df2.format(st.sp[i].heightcurveUsedP0) + "   " + "b1=" + df2.format(st.sp[i].heightcurveUsedP1) + "   " +
                                  "b2=" + df2.format(st.sp[i].heightcurveUsedP2) + "   ", textfont)));

                          //VolumeByNFV volFun = new VolumeByNFV();
                          String volfunktion = st.sp[i].spDef.volumeFunctionXML;
                          if(volfunktion.indexOf("/*")>0 && volfunktion.indexOf("*/")>0)
                                  volfunktion = volfunktion.substring(volfunktion.indexOf("/*")+2, volfunktion.lastIndexOf("*/"));
                          document.add(new Paragraph(new Chunk("Volumenfunktion: " + volfunktion , textfont)));
// Grafik  erzeugen (nur für pdf, sonst dürften die Grafiken für versch. Baumarten nicht überschrieben werden)
                          if(ausgabePDF){
                              VisGrafik visgrafik = new VisGrafik(st);
                              visgrafik.setGraphType(3, st.sp[i].code);
                              visgrafik.neuzeichnen();
                              visgrafik.saveToJPEG(null);
                              Image hGraf = Image.getInstance("grafik3.png");
                              visgrafik.setGraphType(1, st.sp[i].code);
                              visgrafik.neuzeichnen();
                              visgrafik.saveToJPEG(null);
                              Image dGraf = Image.getInstance("grafik1.png");

                              Table datatable4 = new Table(2);
                              datatable4.setPadding(0);
                              datatable4.setSpacing(0);
                              datatable4.setBorder(0);
                              int headerwidths4[] = {50, 50};
                              datatable4.setWidths(headerwidths4);
                              datatable4.setWidth(100);
                              Cell cellHGraf = new Cell(hGraf);
                              Cell celldGraf = new Cell(dGraf);
                              cellHGraf.setBorder(Rectangle.NO_BORDER);
                              //                cellHGraf.setHorizontalAlignment(Element.ALIGN_LEFT);
                              datatable4.addCell(cellHGraf);
                              datatable4.addCell(celldGraf);
                              document.add(datatable4);
                          }
// Mittelstämme  
//                document.add(new Paragraph("Mittelstämme"));

                          Table dataMittelst = new Table(15);
                          dataMittelst.setPadding(1);
                          dataMittelst.setSpacing(0);
                          int headerwidthsM[] = {10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
                          dataMittelst.setWidths(headerwidthsM);
                          dataMittelst.setWidth(100);
                          Font datafont = new Font (Font.HELVETICA, 10, Font.NORMAL);
                          Font headfont = new Font (Font.HELVETICA, 10, Font.BOLD);

        // Tabellenkopf

                          Cell titcell = new Cell(new Phrase("Derbholz", headfont));
                          titcell.setColspan(15);
                          titcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                          dataMittelst.addCell(titcell);
                          Cell cellm = new Cell(new Phrase(" ", headfont));
                          cellm.setHorizontalAlignment(Element.ALIGN_CENTER);
                          dataMittelst.addCell(cellm);
                          dataMittelst.addCell(new Phrase("DMin",headfont));
                          dataMittelst.addCell(new Phrase("HMin",headfont));
                          dataMittelst.addCell(new Phrase("DMax",headfont));
                          dataMittelst.addCell(new Phrase("HMax",headfont));
                          dataMittelst.addCell(new Phrase("Dar",headfont));
                          dataMittelst.addCell(new Phrase("Har",headfont));
                          dataMittelst.addCell(new Phrase("Dg",headfont));
                          dataMittelst.addCell(new Phrase("Hg",headfont));
                          dataMittelst.addCell(new Phrase("Dow",headfont));
                          dataMittelst.addCell(new Phrase("How",headfont));
                          dataMittelst.addCell(new Phrase("D200",headfont));
                          dataMittelst.addCell(new Phrase("H200",headfont));
                          dataMittelst.addCell(new Phrase("D100",headfont));
                          dataMittelst.addCell(new Phrase("H100",headfont));


                          StandDiameters standDiameters = new StandDiameters();
                          st.sortbyd();
// Je ein Durchlauf für verbleibend (kk = 0), ausscheidend (kk = 1), gesamt (kk = 2)                    
                          for (int kk = 0; kk < 3; kk++) {
                              int kkx = -1;
                              if (kk == 1) kkx = st.year;
                              if (kk == 2) kkx = 0;
                              
                              double dg = standDiameters.getDg(st, st.sp[i].code, kkx);
                              double dar = standDiameters.getDar(st, st.sp[i].code, kkx);
                              double dmin = standDiameters.getDmin(st, st.sp[i].code, kkx);
                              double dmax = standDiameters.getDmax(st, st.sp[i].code, kkx);
                              double dow = standDiameters.getDow(st, st.sp[i].code, kkx);
                              double d100 = standDiameters.getDx(st, st.sp[i].code, kkx, 100);
                              double d200 = standDiameters.getDx(st, st.sp[i].code, kkx, 200);
                              double hg = 0.0;
                              double har = 0.0;
                              double hmin = 0.0;
                              double hmax = 0.0;
                              double how = 0.0;
                              double h100 = 0.0;
                              double h200 = 0.0;
                              if (st.sp[i].heightcurveUsed.indexOf("Einheits") > -1) {
                                  /* UniformHeight uh = new UniformHeight();
                                  hg = uh.height(st.sp[i], dg,
                                          st.sp[i].dg, st.sp[i].hg, st);
                                  har = uh.height(st.sp[i], dar,
                                          st.sp[i].dg, st.sp[i].hg, st);
                                  hmin = uh.height(st.sp[i], dmin,
                                          st.sp[i].dg, st.sp[i].hg, st);
                                  hmax = uh.height(st.sp[i], dmax,
                                          st.sp[i].dg, st.sp[i].hg, st);
                                  how = uh.height(st.sp[i], dow,
                                          st.sp[i].dg, st.sp[i].hg, st);
                                  h100 = uh.height(st.sp[i], d100,
                                          st.sp[i].dg, st.sp[i].hg, st);
                                  h200 = uh.height(st.sp[i], d200,
                                          st.sp[i].dg, st.sp[i].hg, st); */
                                  
                                  Tree tree = new Tree();
                                  tree.sp = st.sp[i];
                                  tree.sp.dg=st.sp[i].dg;
                                  tree.sp.hg=st.sp[i].hg;

                                  FunctionInterpreter fi = new FunctionInterpreter();
                                  tree.d = dg;
                                  hg = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  tree.d = dar;
                                  har = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  tree.d = dmin;
                                  hmin = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  tree.d = dmax;
                                  hmax = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  tree.d = dow;
                                  how = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  tree.d = d100;
                                  h100 = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  tree.d = d200;
                                  h200 = fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                          
                                          
                              } else {
                                  HeightCurve hc = new HeightCurve();
                                  hg = hc.getHeight(st.sp[i].spDef.heightCurve, dg, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  har = hc.getHeight(st.sp[i].spDef.heightCurve, dar, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  hmin = hc.getHeight(st.sp[i].spDef.heightCurve, dmin, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  hmax = hc.getHeight(st.sp[i].spDef.heightCurve, dmax, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  how = hc.getHeight(st.sp[i].spDef.heightCurve, dow, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  h100 = hc.getHeight(st.sp[i].spDef.heightCurve, d100, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  h200 = hc.getHeight(st.sp[i].spDef.heightCurve, d200, st.sp[i].heightcurveUsedP0,
                                          st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                              }
                              if (kk == 0) dataMittelst.addCell(new Phrase("verbl.",headfont));
                              if (kk == 1) dataMittelst.addCell(new Phrase("aussch.",headfont));
                              if (kk == 2) dataMittelst.addCell(new Phrase("Gesamt ",headfont));
                              df2.setMaximumFractionDigits(1);
                              df2.setMinimumFractionDigits(1);

                              dataMittelst.addCell(new Phrase(df2.format(dmin),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(hmin),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(dmax),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(hmax),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(dar),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(har),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(dg),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(hg),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(dow),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(how),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(d200),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(h200),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(d100),datafont));
                              dataMittelst.addCell(new Phrase(df2.format(h100),datafont));

                          }
                          document.add(dataMittelst);
// Mittelstämme  
                          Table dataMassen = new Table(10);
                          dataMassen.setPadding(1);
                          dataMassen.setSpacing(0);
                          int headerMassen[] = {17, 8, 9, 8, 8, 8, 8, 11, 11, 12};
                          dataMassen.setWidths(headerMassen);
                          dataMassen.setWidth(100);
        // Tabellenkopf
                          Cell cellma = new Cell(new Phrase("Übersicht", headfont));
                          cellma.setRowspan(2);
                          dataMassen.addCell(cellma);
                          Cell titcell1 = new Cell(new Phrase("Gesamtbestand", headfont));
                          titcell1.setColspan(2);
                          titcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                          dataMassen.addCell(titcell1);
                          Cell titcell2 = new Cell(new Phrase("Derbholz", headfont));
                          titcell2.setColspan(7);
                          titcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                          dataMassen.addCell(titcell2);                     
                          cellm.setHorizontalAlignment(Element.ALIGN_CENTER);
                          dataMassen.addCell(new Phrase("Alter",headfont));
                          dataMassen.addCell(new Phrase("N",headfont));
                          dataMassen.addCell(new Phrase("G",headfont));
                          dataMassen.addCell(new Phrase("V",headfont));
                          dataMassen.addCell(new Phrase("Dg",headfont));
                          dataMassen.addCell(new Phrase("Hg",headfont));
                          dataMassen.addCell(new Phrase("N/ha",headfont));
                          dataMassen.addCell(new Phrase("G/ha",headfont));
                          dataMassen.addCell(new Phrase("V/ha",headfont));

                          for (int kk = 0; kk < 7; kk++) {
                              int oukk = 0;
                              int zfkk = 0;
                              int outkk = -1;
                              if (kk == 1) { //Oberstand verbleibend
                                  oukk = 1;
                              }
                              if (kk == 2) { //Unterstand verbleibend
                                  oukk = -1;
                              }
                              if (kk == 3) { //Z-Bäume verbleibend
                                  oukk = 0;
                                  zfkk = 1;
                              }
                              if (kk == 4) { //ausscheidend
                                  oukk = 0;
                                  zfkk = 0;
                                  outkk = st.year;
                              }
                              if (kk == 5) { //Einwuchs
                                  oukk = 0;
                                  zfkk = 0;
                                  outkk = -99;
                              }
                              if (kk == 6) { //Gesamt
                                  oukk = 0;
                                  zfkk = 0;
                                  outkk = 0;
                              }
                              VisStandInfo visStandInfo = new VisStandInfo();
                              visStandInfo.minMaxAlterRechnen(st, st.sp[i].code, outkk, oukk, zfkk);
                              double maxAlter = visStandInfo.getMaxAlter();
                              double minAlter = visStandInfo.getMinAlter();
                              String alter = df0.format(minAlter) + "-" + df0.format(maxAlter);
                              if(maxAlter - minAlter < 0.1) alter = df0.format(maxAlter);
                              double nMasse = visStandInfo.getNAlle(st, st.sp[i].code, outkk, oukk, zfkk);
                              double nMasseDerb = visStandInfo.getN(st, st.sp[i].code, outkk, oukk, zfkk);
                              double gMasse = visStandInfo.getG(st, st.sp[i].code, outkk, oukk, zfkk);
                              double vMasse = visStandInfo.getV(st, st.sp[i].code, outkk, oukk, zfkk);
                              if (nMasse > 0) {
                                  double dgm = 200.0 * Math.sqrt(gMasse / (Math.PI * nMasseDerb));
                                  double hgm = 0.0;
                                  if (st.sp[i].heightcurveUsed.indexOf("Einheits") > -1) {
                                      //UniformHeight uh = new UniformHeight();
                                      //hgm = uh.height(st.sp[i], dgm,
                                      //        st.sp[i].dg, st.sp[i].hg, st);
                                      Tree tree = new Tree();
                                      tree.d = dgm;
                                      tree.sp = st.sp[i];
                                      tree.sp.dg=st.sp[i].dg;
                                      tree.sp.hg=st.sp[i].hg;
                                      FunctionInterpreter fi = new FunctionInterpreter();
                                      hgm=fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                                  } else {
                                      HeightCurve hc = new HeightCurve();
                                      hgm = hc.getHeight(st.sp[i].spDef.heightCurve, dgm, st.sp[i].heightcurveUsedP0,
                                              st.sp[i].heightcurveUsedP1, st.sp[i].heightcurveUsedP2);
                                  }
                                  if (kk == 0)dataMassen.addCell(new Phrase("verbleibend",headfont));
                                  if (kk == 1) dataMassen.addCell(new Phrase("Oberstand",headfont));
                                  if (kk == 2) dataMassen.addCell(new Phrase("Unterstand",headfont));
                                  if (kk == 3) dataMassen.addCell(new Phrase("Z-Bäume",headfont));
                                  if (kk == 4) dataMassen.addCell(new Phrase("ausscheidend",headfont));
                                  if (kk == 5) dataMassen.addCell(new Phrase("Einwuchs",headfont));
                                  if (kk == 6) dataMassen.addCell(new Phrase("Gesamt",headfont));
                                  df2.setMaximumFractionDigits(0);
                                  df2.setMinimumFractionDigits(0);
                                  dataMassen.addCell(new Phrase(alter, datafont));
                                  dataMassen.addCell(new Phrase(df2.format(nMasse),datafont));
                                  df2.setMaximumFractionDigits(2);
                                  df2.setMinimumFractionDigits(2);
                                  dataMassen.addCell(new Phrase(df2.format(gMasse),datafont));
                                  df2.setMaximumFractionDigits(1);
                                  df2.setMinimumFractionDigits(1);
                                  dataMassen.addCell(new Phrase(df2.format(vMasse),datafont));
                                  dataMassen.addCell(new Phrase(df2.format(dgm),datafont));
                                  dataMassen.addCell(new Phrase(df2.format(hgm),datafont));
                                  df2.setMaximumFractionDigits(0);
                                  df2.setMinimumFractionDigits(0);
                                  dataMassen.addCell(new Phrase(df2.format(nMasseDerb / st.size),datafont));
                                  df2.setMaximumFractionDigits(1);
                                  df2.setMinimumFractionDigits(1);
                                  dataMassen.addCell(new Phrase(df2.format(gMasse / st.size),datafont));
                                  df2.setMaximumFractionDigits(0);
                                  df2.setMinimumFractionDigits(0);
                                  dataMassen.addCell(new Phrase(df2.format(vMasse / st.size),datafont));
                              }

                          }
                          document.add(dataMassen);
// Datum
                          HeuteString datum = new HeuteString();
                          String datumStr = datum.get();
                          document.add(new Paragraph(new Chunk("Die Verwendung der Daten ohne Genehmigung der NW-FVA ist nicht erlaubt!       " + datumStr, new Font(Font.ITALIC, 8))));



                          document.newPage();      
                      }  //nGueltig
                  }
              } //Ende if !abbrechen
              else{  //sonst Fehler
                  document.newPage(); 
                  document.add(new Paragraph(new Chunk("Keine Daten, Abbruch durch Benutzer")));
              }
               document.close();
        } catch ( Exception e){System.out.println("Fehler creating pdf: "+e);
            e.printStackTrace();}
        boolean ausgeben = true;
        if (abbrechen) ausgeben = false;
        return ausgeben;
    }

    private void openDataBank(){
          boolean erfolg = false;
          if (dbconnOpen) { 
              dbconn.closeDBConnection(0);
              dbconnOpen=false;
          }
          
          dbconn= new DBConnection(1);     // a class to manage the conection to a database
          if (jComboBox7.getSelectedIndex() == 1 | jComboBox7.getSelectedIndex() == 3){ // Waldwachstum oder Waldwachstum(BZE)
             erfolg = dbconn.openDBConnection(DBConnection.MYSQL, 0, "db/waldwachstum", "wwuser", "ww2005", true, true);
          }
          else{
              if(datenPfad!= null && datenPfad.length()> 0)erfolg = dbconn.openDBConnection(DBConnection.ACCESS, 0, datenPfad, "", "", false, true);
              
          }

          if(erfolg) dbconnOpen=true;

    }

    private void openWriteableDataBank(){
        boolean erfolg = false;
          if (dbconnOpen) {
              dbconn.closeDBConnection(0);
              dbconnOpen=false;
          }

          dbconn= new DBConnection(1);     // a class to manage the conection to a database
          if (jComboBox7.getSelectedIndex() == 1 | jComboBox7.getSelectedIndex() == 3) // Waldwachstum oder Waldwachstum(BZE)
             erfolg = dbconn.openDBConnection(DBConnection.MYSQL, 0, "db/waldwachstum", "waldwachstum", "ww2011", true, true);
          else {
              if(datenPfad.length()>0) erfolg = dbconn.openDBConnection(DBConnection.ACCESS, 0, datenPfad, "", "", false, true);
          }
          if(erfolg) dbconnOpen = true;


    }
        
    private void dbAuswahl() {
        jMenu1.setEnabled(true);  //bearbeiten und Untermenu bzw. Menueinträge
        jMenu7.setEnabled(true);
        jMenuItem12.setEnabled(true);
        jMenuItem13.setEnabled(true);
        jMenuItem25.setEnabled(true);
        jMenuItem34.setEnabled(true); 
        jMenu8.setEnabled(true);
        jMenuItem14.setEnabled(true);
        jMenuItem15.setEnabled(true);
        jMenuItem22.setEnabled(true);
        jMenuItem29.setEnabled(true);
        jMenu9.setEnabled(true);
        jMenuItem17.setEnabled(true);
        jMenuItem20.setEnabled(true);
        jMenuItem37.setEnabled(true);
        jMenu10.setEnabled(true);
        jMenuItem18.setEnabled(true);
        jMenuItem19.setEnabled(true);
        jMenu3.setEnabled(true);    // Prüfen
        jMenuItem21.setEnabled(true);
        jMenuItem35.setEnabled(true);
        jMenu2.setEnabled(true);    // kopieren
        jMenuItem28.setEnabled(true);
        jMenuItem31.setEnabled(true);
        jMenuItem9.setEnabled(true);
        jMenuItem16.setEnabled(true);
        jMenu4.setEnabled(true);    // Einstellung
        jMenuItem6.setEnabled(true);
        jMenu5.setEnabled(true);    // Ausgabe
        jMenuItem7.setEnabled(true);
        jMenuItem8.setEnabled(true);
        jMenuItem4.setEnabled(true);
        jMenuItem5.setEnabled(true);
        jMenuItem10.setEnabled(true);
        jMenuItem27.setEnabled(true);
        jMenuItem36.setEnabled(true);
        jMenu6.setEnabled(true);    // Dateneditor
        jMenuItem26.setEnabled(true);
        jMenu11.setEnabled(true);    // SonderFunktionen
        jMenuItem30.setEnabled(true);
        jMenuItem32.setEnabled(true);

        jButton2.setEnabled(true);  //speichern
        jButton3.setEnabled(true);  // laden

        if (jComboBox7.getSelectedIndex() > 0){
          jPanel12.setVisible(true);
          jPanel3.setVisible(true);
          jPanel4.setVisible(true);
          jButton2.setVisible(true);
          jButton3.setVisible(true);
          

          jTextField1.setVisible(true);
          jLabel1.setText("nach EDVID");
          jButton1.setVisible(true);
          jComboBox8.setVisible(false);
          jPanel12.validate();
          
          
          if (jComboBox7.getSelectedIndex() == 1){ // Waldwachstum
             openDataBank();
              setLandNeu();
             setFoaNeu();
             setAbtNeu();
             setIdNeu();
             setParzNeu();
             setAufNeu();
             jMenu1.setEnabled(false);
             jButton2.setEnabled(false);
             jButton3.setEnabled(false);
             jMenuItem36.setEnabled(false);  // BZE-Auswertung
          }
          if (jComboBox7.getSelectedIndex() == 2){ // Lokal
             if(datenPfad.length()> 0 ){
                 openDataBank();
                 setLandNeu();
                 setFoaNeu();
                 setAbtNeu();
                 setIdNeu();
                 setParzNeu();
                 setAufNeu();
                 jMenuItem36.setEnabled(false);  // BZE-Auswertung
                 //jMenu1.setEnabled(true);
                 //jButton2.setEnabled(true);
                 //jButton3.setEnabled(true);
                 try{          
                     Statement stmt = dbconn.Connections[0].createStatement(); 
                     ResultSet rs = stmt.executeQuery("SELECT edvid FROM Auf ");
                     if (rs.next()) jTextField1.setText(rs.getObject("edvid").toString());
                 }	catch (Exception e)  {	System.out.println(e); }
             }
             
          }
          if(jComboBox7.getSelectedIndex() == 3){ // BZE-Daten
             openDataBank();
              jMenu1.setEnabled(false);  //bearbeiten
             jButton2.setVisible(false);
             jButton3.setVisible(false);
             jMenu2.setEnabled(false);
             jMenu3.setEnabled(false);
             jMenu11.setEnabled(false);
             jMenuItem4.setEnabled(false);
             jMenuItem7.setEnabled(false);
             jMenuItem8.setEnabled(false);
             jMenuItem10.setEnabled(false);
             //jMenuItem36.setEnabled(true);
             jPanel3.setVisible(false);
             jPanel4.setVisible(false);
             jLabel1.setText("Plot");
             jButton1.setVisible(false);
             jTextField1.setVisible(false);
             jComboBox8.setVisible(true);
             jPanel12.validate();
             try{
                  Statement stmt = dbconn.Connections[0].createStatement();
                  ResultSet rs = stmt.executeQuery("SELECT DISTINCT Plot FROM BaumBZE ");
                  while(rs.next()){
                      Object plot = rs.getObject("Plot");
                      jComboBox8.addItem(plot);
                  }
                  rs.close();
                  stmt.close();
        	}	catch (Exception e)  {	System.out.println(e); }

          }
        }
        
        else{
          dbconn.closeDBConnection(0);
          jPanel12.setVisible(false);
          jPanel3.setVisible(false);
          jMenu5.setEnabled(false);

        }
            

    }   
    
    private void setFlaechenLabel(){
       try{          
                jTable1.setValueAt("Baumart: ",0,0);
                jTable1.setValueAt(" ",0,1);
                jTable1.setValueAt("Versuchszweck: ",1,0);
                jTable1.setValueAt(" ",1,1);
                jTable1.setValueAt("letzte Aufnahme: ",2,0);
                jTable1.setValueAt(" ",2,1);
                jTable1.setValueAt("nächste Aufnahme: ",3,0);
                jTable1.setValueAt(" ",3,1);
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("SELECT * FROM Versfl WHERE forstamt = \'"+foaSelected+"\'  AND abt = \'"+abtSelected+"\'");
           while (rs.next()) {
               jTable1.setValueAt("Baumart: ", 0, 0);
               String txt = "";
               Object txt1 = rs.getObject("baumart");
               if (txt1 != null) {
                   txt = txt1.toString().trim();
               }
               jTable1.setValueAt(txt, 0, 1);
               jTable1.setValueAt("Versuchszweck: ", 1, 0);
               txt = "";
               txt1 = rs.getObject("vers_zweck");
               if (txt1 != null) {
                   txt = txt1.toString().trim();
               }
               if (jComboBox7.getSelectedIndex() == 1) {// nur für Waldwachstums-DB
                   jTable1.setValueAt(txt, 1, 1);
                   jTable1.setValueAt("letzte Aufnahme: ", 2, 0);
                   
                   txt = "";
                   txt1 = rs.getObject("letzte_Auf");
                   if (txt1 != null) txt = txt1.toString().trim();
                   jTable1.setValueAt(txt, 2, 1);
                   
                   jTable1.setValueAt("nächste Aufnahme: ", 3, 0);
                   txt = "";
                   txt1 = rs.getObject("naechste_A");
                   if (txt1 != null) txt = txt1.toString().trim();
                   jTable1.setValueAt(txt, 3, 1);
               }    
           }
           edvid6=(String)(jComboBox6.getSelectedItem());
           jTextField1.setText(edvid6);
       }
       catch(Exception e){System.out.println("setFlaechenLabel: "+e);}
    }
    
    private void setParzellenLabel(){
        try {
            jTable1.setValueAt("Parzelle: ", 4, 0);
            jTable1.setValueAt("Durchforstungsart: ", 5, 0);
            jTable1.setValueAt(" ", 5, 1);
            jTable1.setValueAt("Durchforstungsstärke: ", 6, 0);
            jTable1.setValueAt(" ", 6, 1);
            jTable1.setValueAt("Höhenlage: ", 7, 0);
            jTable1.setValueAt(" ", 7, 1);
            jTable1.setValueAt("Exposition: ", 8, 0);
            jTable1.setValueAt(" ", 8, 1);
            jTable1.setValueAt("Hanglage: ", 9, 0);
            jTable1.setValueAt(" ", 9, 1);
            Statement stmt = dbconn.Connections[0].createStatement();
            String idx = (String) (jComboBox6.getSelectedItem());
            String idxx = (String) (jComboBox4.getSelectedItem());
            if (idx != null && idxx != null) {
                ResultSet rs = stmt.executeQuery("select * from ParzInfo where edvid = \'" + idx + "\' and PARZ = \'" + idxx + "\' ");
                while (rs.next()) {
                    jTable1.setValueAt("Parzelle: " + idxx, 4, 0);
                    jTable1.setValueAt("Durchforstungsart: ", 5, 0);
                    jTable1.setValueAt(getDurchforstungsartByID(idx + idxx), 5, 1);
                    jTable1.setValueAt("Durchforstungsstärke: ", 6, 0);
                    jTable1.setValueAt(getDurchforstungsstaerkeByID(idx + idxx), 6, 1);
                    jTable1.setValueAt("Höhenlage: ", 7, 0);
                    jTable1.setValueAt(rs.getObject("HuNN_mean").toString(), 7, 1);
                    jTable1.setValueAt("Exposition: ", 8, 0);
                    jTable1.setValueAt(rs.getObject("EXPO").toString(), 8, 1);
                    jTable1.setValueAt("Hanglage: ", 9, 0);
                    jTable1.setValueAt(rs.getObject("HANG").toString(), 9, 1);
                }
                edvid6 = (String) (jComboBox6.getSelectedItem());
                jTextField1.setText(edvid6);
            }
        } catch (Exception e) {
            System.out.println("setParzellenLabel: " + e);
        }
    }

    private void setAufnahmeLabel(){
       try{          
          jTable1.setValueAt("Aufnahme: ",10,0);
          jTable1.setValueAt("Zeitpunkt: ",11,0);
          jTable1.setValueAt(" ",11,1);
          jTable1.setValueAt("Flächengröße: ",12,0);
          jTable1.setValueAt(" ",12,1);
          jTable1.setValueAt("Aufnahmetyp: ",13,0);
          jTable1.setValueAt(" ",13,1);
           Statement stmt = dbconn.Connections[0].createStatement(); 
           String idx=(String)(jComboBox6.getSelectedItem())+(String)(jComboBox4.getSelectedItem());
           String aufsx=(String)(jComboBox5.getSelectedItem());
           int aufx = Integer.parseInt(aufsx);
           ResultSet rs = stmt.executeQuery("select * from Auf where edvid = \'"+idx+"\' and auf = "+aufx+" ");
           while(rs.next()){
                jTable1.setValueAt("Aufnahme: "+aufsx,10,0);
                jTable1.setValueAt("Zeitpunkt: ",11,0);
                jTable1.setValueAt(rs.getObject("monat").toString()+"/"+rs.getObject("jahr").toString(),11,1);
                jTable1.setValueAt("Flächengröße: ",12,0);
                jTable1.setValueAt(rs.getObject("flha").toString(),12,1);
                jTable1.setValueAt("Aufnahmetyp: ",13,0);
                jTable1.setValueAt(rs.getObject("typ").toString(),13,1);
           }
           edvid6=(String)(jComboBox6.getSelectedItem());
           jTextField1.setText(edvid6);
       }
       catch(Exception e){System.out.println("setAufnahmeLabel: "+e);}
    }

    
    private String loadID(String edvidCheck){
        String ergebnis="";
        String edvid6Check="";
        if (edvidCheck.length() > 5) edvid6Check=edvidCheck.substring(0,6);
        
        if(edvidCheck.length() == 0){
            jComboBox1.removeAllItems();
            jComboBox2.removeAllItems();
            jComboBox3.removeAllItems();
            jComboBox4.removeAllItems();
            jComboBox5.removeAllItems();
            jComboBox6.removeAllItems();
            
        }
        else if(dbconnOpen){
            try {
                Statement stmt = dbconn.Connections[0].createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Versfl WHERE edv_id = \'" + edvid6Check + "\'");
                String foaSelectedCheck = "";
                String abtSelectedCheck = "";
                String landSelectedCheck = "";
                if (rs.next()) {
                    foaSelectedCheck = rs.getObject("forstamt").toString();
                    abtSelectedCheck = rs.getObject("abt").toString();
                }
                Statement stmt2 = dbconn.Connections[0].createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Forstaemter WHERE forstamt = \'" + foaSelectedCheck + "\'");
                int landNoSelectedCheck = 0;
                if (rs2.next()) {
                    landNoSelectedCheck = rs2.getInt("land");
                } else {
                    ergebnis = "Forstamt in Forstaemter unbekannt";
                }
                
                Statement stmt3 = dbconn.Connections[0].createStatement();
                ResultSet rs3 = stmt3.executeQuery("SELECT * FROM Land WHERE land = " + landNoSelectedCheck);
                if (rs3.next()) {
                    landSelectedCheck = rs3.getObject("landname").toString();
                } else {
                    ergebnis = ergebnis + "Land nicht bekannt";
                }
                //        String landSelectedCheck= ex.getString("landname");
                jComboBox1.setSelectedItem(landSelectedCheck);
                jComboBox2.setSelectedItem(foaSelectedCheck);
                jComboBox3.setSelectedItem(abtSelectedCheck);
                jComboBox6.setSelectedItem(edvid6Check);
                if (edvidCheck.length() > 6) {
                    String parzSelectedCheck = edvidCheck.substring(6, 8);
                    jComboBox4.setSelectedItem(parzSelectedCheck);
                }
                jTextField1.setText(edvidCheck);
                foaSelected = foaSelectedCheck;
                abtSelected = abtSelectedCheck;
            } catch (Exception e) {
                System.out.println("Choose ID: " + e);
                e.printStackTrace();
            }
            setFlaechenLabel();
            
            
        }
        return ergebnis;
    }
    
    public String getDurchforstungsartByID(String selectedID){
       String durchforstungsart="";
       try{ 
          Statement stmt11 = dbconn.Connections[0].createStatement(); 
          ResultSet rs11 = stmt11.executeQuery("select * from ParzInfo where edvid = \'"+idSelected.substring(0,6)+
                                                "\' AND PARZ = \'"+idSelected.substring(6,8)+"\'  " );
          int dfArtNr=0;
          if (rs11.next()){
             dfArtNr= rs11.getInt("dfart1");
          } else dfArtNr=999;
          if(dfArtNr < 999 ){
            stmt11 = dbconn.Connections[0].createStatement(); 
            rs11 = stmt11.executeQuery("select * from PI_Durchforstungsart where code = "+dfArtNr+"  " );
            if (rs11.next()){
               durchforstungsart= rs11.getObject("inhalt").toString();
            }
          } 
          else durchforstungsart="nicht eingegeben";
       }  catch(Exception e){System.out.println("Durchforstungsart nicht gefunden: "+e);}
       return durchforstungsart;

    }
    public String getDurchforstungsstaerkeByID(String selectedID){
       String behandlungsart="";
       try{ 
          Statement stmt11 = dbconn.Connections[0].createStatement(); 
          ResultSet rs11 = stmt11.executeQuery("select * from ParzInfo where edvid = \'"+idSelected.substring(0,6)+
                                                "\' AND PARZ = \'"+idSelected.substring(6,8)+"\'  " );
          int dfArtNr=0;
          if (rs11.next()){
             dfArtNr= rs11.getInt("dfstaerke1");
          } else dfArtNr=999;
          if(dfArtNr < 999 ){
            stmt11 = dbconn.Connections[0].createStatement(); 
            rs11 = stmt11.executeQuery("select * from PI_Durchforstungsstaerke where id = "+dfArtNr+"  " );
            if (rs11.next()){
               behandlungsart= rs11.getObject("inhalt").toString();
            }
          }
          else behandlungsart="nicht eingegeben!";
       }  catch(Exception e){System.out.println("Behandlungsart nicht gefunden: "+e);}
       return behandlungsart;

    }

    public boolean tempDatenSichern(String pfad){
        boolean erfolg = false;
        VisCopyFile copyFile = new VisCopyFile();
        final String edvID = edvid6;
        dbconn.closeDBConnection(0);
        String sichOrdner = "tempdaten//sicher//";
        String snach = sichOrdner + edvID+"_1.mdb";
        
        // Filter -> nur mdb mit aktueller edvid
        FileFilter filefilter = new FileFilter() {
            public boolean accept(File file) {
                if (file.getName().startsWith(edvID) && 
                        file.getName().endsWith(".mdb")) {
                    return true;
                }
                return false;
            }
        };
        
        // Umbenennen und sichern  
        File fvon = new File(pfad);
        File fnach = new File(snach);
        try{
            File sichOrdnerf = new File(sichOrdner);
            File[] dats = sichOrdnerf.listFiles(filefilter);
            for(int i = 0; i < dats.length; i ++) if(dats[i].getName().contains("_3")) dats[i].delete();
            for(int i = 0; i < dats.length; i ++){
                if(dats[i].getName().contains("_2.mdb")) dats[i].renameTo(new File(dats[i].toString().replace("_2.mdb","_3.mdb")));
            }
            for(int i = 0; i < dats.length; i ++){
                if(dats[i].getName().contains("_1.mdb")) dats[i].renameTo(new File(dats[i].toString().replace("_1.mdb","_2.mdb")));
            }
            String pfvon= fvon.getCanonicalPath();
            String pfnach= fnach.getCanonicalPath();
            boolean ok = copyFile.copy(pfvon,pfnach); 
            if(ok) erfolg = true;
        } catch (Exception e){ e.printStackTrace();};
        return erfolg;
    }
    
// Korrigiert die Anordnung der Baumnummer    
    public String setBaumnummer(String nr){
        String z = nr.trim();
        int m = z.length();
//        z = z.toUpperCase();
        char c[] = z.toCharArray();
        int ascWert = (int) c[m-1];
        int k=4;
        if (ascWert <65 || ascWert > 90) { z=z+" "; m=m+1;}
        for (int i=m; i<5;i++) z=" "+z;
        return z;
    }
    
      
    class Bestandeswerte extends SwingWorker<Integer,Integer> {
        @Override
        public Integer doInBackground() {
            int aktzeile = 0;
            vis2006.LoadStand lts = new  vis2006.LoadStand();
            NumberFormat df=NumberFormat.getInstance();
            df=NumberFormat.getInstance(new Locale("en","US"));
            df.setMaximumFractionDigits(1);
            df.setMinimumFractionDigits(1);
            df.setGroupingUsed(false);            
            
                try{
                    Statement stmt = dbconn.Connections[0].createStatement();
                    Statement stmt2 = dbconn.Connections[0].createStatement();
                    dbconn.Connections[0].setReadOnly(false);
                    
                    ResultSet rs = stmt.executeQuery("SELECT * FROM tempAuf ORDER BY edvid, auf");
                    //              ResultSet rs = stmt.executeQuery("SELECT Auf.edvid, Auf.auf, Auf.monat, Auf.jahr, Auf.flha, Auf.qualitat, Bestandeswerte.dg, Bestandeswerte.hg "+
                    //                    "FROM Auf LEFT JOIN Bestandeswerte ON (Auf.auf = Bestandeswerte.auf) AND (Auf.edvid = Bestandeswerte.edvid) "+            
                    //                    " WHERE (((Bestandeswerte.dg) Is Null)) " );            
                    while(rs.next() && ! pm.isCanceled()){
                    double dfo = 0;
                    double tot = 0;
                    double wind = 0;
                    double sonst = 0;
                    String edvidx = rs.getObject("edvid").toString();
                    int aufx = rs.getInt("auf");
                    String aufid = String.valueOf(aufx);
                    if(aufid.length()==1) aufid = "0" + aufid;
                    aufid = edvidx + aufid;
                    System.out.println(edvidx+"   "+aufx);
                    treegross.base.Stand st = new Stand();
                    st.setSDM(SDM);
                    // Bestand ohne Randbäume laden
                    st=lts.loadFromDB(Vis2006.this, dbconn, st, edvidx, aufx, true, true, false);
                    st.descspecies();
                    //auscheidender Bestand:
                    for(int i=0; i<st.ntrees; i++){
                        int outr = 0;
                        if(st.tr[i].mortalityReason > 0 && st.tr[i].d >= 7){
                            outr = st.tr[i].mortalityReason;
                            if(outr == 1) dfo = dfo+st.tr[i].fac;
                            else if(outr == 2) tot = tot+st.tr[i].fac;
                            else if (outr == 3) wind = wind+st.tr[i].fac;
                            else sonst = sonst+st.tr[i].fac;
                        }
                    }
                    dfo = dfo/st.size;
                    tot = tot/st.size;
                    wind = wind/st.size;
                    sonst = sonst/st.size;
                    // Grundfläche berechnen (Fehler im Simulator? sonst gleich st.bha verwenden)
                    double ghaStand = 0.0;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].d >= 7 && st.tr[i].out < 0) {
                            ghaStand = ghaStand + st.tr[i].fac * Math.PI * (st.tr[i].d / 200.0) * (st.tr[i].d / 200.0);
                        }
                    }
                    ghaStand = ghaStand / st.size;
                    String d100Str = "0.0";
                    if(st.d100 > 0) d100Str = df.format(st.d100);
                    String h100Str = "0.0";
                    if(st.h100 > 0) h100Str = df.format(st.h100);
                    String hgStr = "0.0";
                    if(st.hg > 0) hgStr = df.format(st.hg);

                    try{
                        stmt2.execute("INSERT INTO Bestandeswerte (id, edvid, auf, dg, hg, d100, h100, nha, gha, vha, "
                                + "nhaa, nhaaD, nhaaT, nhaaW, ghaa, vhaa) VALUES ("+
                                "'"+aufid+"','"+edvidx+"', "+aufx
                                +","+df.format(st.dg)+","+hgStr+","+d100Str+","+ h100Str
                                +","+df.format(st.nha)+","+df.format(ghaStand)+","+df.format(st.getVhaResidual(0))+","+df.format(st.nhaout)
                                +","+df.format(dfo)+","+df.format(tot)+","+df.format(wind)+","+df.format(st.bhaout)+","+df.format(st.getVhaThinning(0))+" )");               
                        
                        for (int i=0; i< st.nspecies;i++){
                            dfo = 0.0;
                            tot = 0.0;
                            wind = 0.0;
                            sonst = 0.0;
                            for (int j=0; j<st.ntrees; j++){
                                int outr = 0;
                                if (st.tr[j].mortalityReason > 0 && st.tr[j].code == st.sp[i].code
                                         && st.tr[i].d >= 7){
                                    outr = st.tr[j].mortalityReason;
                                    if(outr == 1) dfo = dfo+st.tr[j].fac;
                                    else if(outr == 2) tot = tot+st.tr[j].fac;
                                    else if (outr == 3) wind = wind+st.tr[j].fac;
                                    else sonst = sonst+st.tr[j].fac;
                                }  
                            }
                            dfo = dfo/st.size;
                            tot = tot/st.size;
                            wind = wind/st.size;
                            sonst = sonst/st.size;
                            h100Str = "0.0";
                            if(st.sp[i].h100 > 0) h100Str = df.format(st.sp[i].h100);
                            hgStr = "0.0";
                            if(st.sp[i].hg > 0) hgStr = df.format(st.sp[i].hg);
                            stmt2.execute("INSERT INTO Baumartenwerte ( edvid, auf, art, alt, dg, hg, d100, h100, nha, gha, vha, nhaa, nhaaD, nhaaT, nhaaW, ghaa, vhaa ) VALUES ("+
                                    "'"+edvidx+"', "+aufx+", "+st.sp[i].code+", "+df.format(st.sp[i].h100age)
                                    +","+df.format(st.sp[i].dg)+","+ hgStr +","+df.format(st.sp[i].d100)+","+ h100Str
                                    +","+df.format(st.sp[i].nha)+","+df.format(st.sp[i].gha)+","+df.format(st.getVhaResidual(st.sp[i].code))+","+df.format(st.sp[i].nhaout)
                                    +","+df.format(dfo)+","+df.format(tot)+","+df.format(wind)
                                    +","+df.format(st.sp[i].ghaout)+","+df.format(st.getVhaThinning(st.sp[i].code)+st.getVhaTargetDiameter(st.sp[i].code))+" )");
                        }
                    }	catch (Exception e)  {e.printStackTrace(); }
                    aktzeile = aktzeile + 1;
                    publish(aktzeile);

                }
                rs.close();
                stmt.close();
                stmt2.close();
            } catch (Exception e)  {e.printStackTrace(); }
            return aktzeile;
        }
        
        @Override
        protected void process(java.util.List<Integer> status){
            //System.out.println(status);
            int letztstatus = status.get(status.size()-1);
            pm.setProgress(letztstatus);    }
        
        @Override  // läuft auf dem *Event Dispatch Thread*, wenn doInBackground beendet ist
        public void done() {
            int zeilenBestWerte = 0;
            try{
                zeilenBestWerte = get(); 
            }catch(Exception e){}
            Vis2006.this.setEnabled(true);
            if (zeilenBestWerte == pm.getMaximum())
                jLabel9.setText("Werte für "+zeilenBestWerte+" Aufnahmen berechnet!");
            else jLabel9.setText("Fehler! "+zeilenBestWerte+" von "+pm.getMaximum()+" Aufnahmen berechnet.");
            pm.close();
            Vis2006.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
            
        }
    } 
    

    public boolean auswertungBZE(String filename, boolean allePlots){
        boolean fertig = false;
        openDataBank();
        EklToAbsHAlter100 bonET = new EklToAbsHAlter100();

        NumberFormat df=NumberFormat.getInstance();
        df=NumberFormat.getInstance(new Locale("en","US"));
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        df.setGroupingUsed(false);
        NumberFormat df0=NumberFormat.getInstance();
        df0=NumberFormat.getInstance(new Locale("en","US"));
        df0.setMaximumFractionDigits(0);
        df0.setMinimumFractionDigits(0);
        df0.setGroupingUsed(false);

        PrintWriter ausgabe = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean alleAufnahmenOk = false;
        try{
             ausgabe = new PrintWriter(new FileWriter(filename));
             ausgabe.println("'Plot'"+";"+"'Art'"+";"+"'Schicht'"+";"+"'minAlter[Jahr]'"+";"+
                     "'maxAlter[Jahre]'"+";"+"'Stammzahl'"+";"+"'D100[cm]'"+";"+"'Hober[m]'"+";"+
                     "'Dg[cm]'"+";"+"'Hg[m]'"+";"+"'StammzahlProHa'"+";"+"'Grundfl[m2/ha]'"+";"+
                     "'Volumen[m3/ha]'"+";"+"'geschVolzuwachs[m3/ha/Jahr]'"+";"+"'Bestockungsgrad'"+";"+
                     "'Ertragsklasse'"+";"+"'Hoehenbonitaet'");
//Alle Plots bearbeiten
             int nPlot = 1; 
             if (allePlots) nPlot = jComboBox8.getItemCount()-1; //Element "alle" raus
             for (int iiii=0;iiii<nPlot;iiii++){  
                 if(allePlots) idSelected=jComboBox8.getItemAt(iiii+1).toString();
                 else idSelected = jComboBox8.getSelectedItem().toString();
        
// Schleife über alle Aufnahmen
                 int nauf = 1;  // für mehr Aufnahmen nicht umgesetzt
                 for (int ii=0;ii<nauf;ii++){
                     alleAufnahmenOk = false;
                     System.out.println("in Berechnung : "+idSelected);
                     LoadStand lts = new  LoadStand();
                     // Bestand laden
                     st = new Stand();
                     st.setSDM(SDM);

                     st = lts.loadBZE(this, dbconn, st, idSelected, true, true);
                     st.descspecies();
                     Stand st2 = st.clone(); // identischer Bestand für die Zuwachsschätzung
                     st.grow(1,false);  // ! st wächst, Probleme beim Wachsenlassen von Klonen
                     for(int t=0; t<st.ntrees; t++) st.tr[t].out = -1;
                     st.descspecies();
                     
                     // Informationen zur Aufnahme extrahieren
                     System.out.println("Ergebnisse berechnen...");
                     //int mon=0;
                     //SimpleDateFormat monat = new SimpleDateFormat("MM"); // nur das erste Datum
                     //stmt = dbconn.Connections[0].createStatement();
                     //rs = stmt.executeQuery("SELECT aufdatum FROM BaumBZE WHERE plot = \'"+idSelected+"\'");
                     //java.util.Date datum = null;
                     //if(rs.next()) datum = rs.getDate("aufdatum");
                     //mon = Integer.parseInt(monat.format(datum));
                     //double zeit = st.year;
                     //double zeitraum =0.0;
                     //AltersDezimale aDez = new AltersDezimale();
                     //zeit = st.year + aDez.getAltersdezimale(mon);

                     // Zuwachsberechnung (Achtung der Volumenzuwachs wird in die Variable totalPrice geschrieben)
                     for (int i=0; i<st2.nspecies; i++) st2.sp[i].totalPrice=0.0;
                     
                     double sumNha=0.0; double sumGha=0.0; double sumVha=0.0;  
                     double sumiVha=0.0;
                     double sumNb=0; 
                     double sumbgrad=0.0;
                     
                     // Für alle Baumarten
                     for (int i = 0; i < st2.nspecies; i++) {

                         // Für die Kollektive   0 gesamt, 1 Oberschicht, 2 Mittelschicht, 3 Unterschicht, 4 Überhalt, 5 leer
                         int nKoll = 6;
                         for (int iii = 0; iii < nKoll; iii++) {
 
                             // Alter 
                             Integer amin=999;
                             Integer amax=0;
                             for (int l = 0; l < st2.ntrees; l++) {
                                 if (st2.tr[l].code == st2.sp[i].code && st2.tr[l].fac > 0.0) {
                                     if (iii == 0 && amin > st2.tr[l].age) amin = st2.tr[l].age;
                                     if (iii == 0 && amax < st2.tr[l].age) amax = st2.tr[l].age;
                                     if (iii == 1 && st2.tr[l].layer == 1 && amin > st2.tr[l].age) amin = st2.tr[l].age;
                                     if (iii == 1 && st2.tr[l].layer == 1 && amax < st2.tr[l].age) amax = st2.tr[l].age;
                                     if (iii == 2 && st2.tr[l].layer == 2 && amin > st2.tr[l].age) amin = st2.tr[l].age;
                                     if (iii == 2 && st2.tr[l].layer == 2 && amax < st2.tr[l].age) amax = st2.tr[l].age;
                                     if (iii == 3 && st2.tr[l].layer == 3 && amin > st2.tr[l].age) amin = st2.tr[l].age;
                                     if (iii == 3 && st2.tr[l].layer == 3 && amax < st2.tr[l].age) amax = st2.tr[l].age;
                                     if (iii == 4 && st2.tr[l].layer == 4 && amin > st2.tr[l].age) amin = st2.tr[l].age;
                                     if (iii == 4 && st2.tr[l].layer == 4 && amax < st2.tr[l].age) amax = st2.tr[l].age;
                                     if (iii == 5 && st2.tr[l].layer == 0 && amin > st2.tr[l].age) amin = st2.tr[l].age;
                                     if (iii == 5 && st2.tr[l].layer == 0 && amax < st2.tr[l].age) amax = st2.tr[l].age;
                                 }
                             }
                             // Anzahl vorhandenen der Bäume (alle Bäume)
                             double nb =0;
                             for (int l=0;l<st2.ntrees;l++) {
                                 if (st2.tr[l].code==st2.sp[i].code && st2.tr[l].out < 1 && st2.tr[l].fac>0.0){
                                     if (iii == 0) nb=nb+st2.tr[l].fac;
                                     if (iii == 1 && st2.tr[l].layer == 1) nb=nb+st2.tr[l].fac;
                                     if (iii == 2 && st2.tr[l].layer == 2) nb=nb+st2.tr[l].fac;
                                     if (iii == 3 && st2.tr[l].layer == 3) nb=nb+st2.tr[l].fac;
                                     if (iii == 4 && st2.tr[l].layer == 4) nb=nb+st2.tr[l].fac;
                                     if (iii == 5 && st2.tr[l].layer == 0) nb=nb+st2.tr[l].fac;
                                 }
                             }
                             //verbleibenden Bestand pro ha berechen (nur Bäume mit d >= 7 cm)
                             double ghax = 0.0;
                             double nhax = 0.0;
                             double vhax = 0.0;
                             double ivhax = 0.0;
                             String ghaStr = "";
                             String vhaStr = "";
                             String ivhaStr = "";
                             String dgStr = "";
                             String hgStr = "";
                             boolean baumtrue = true;
                             for (int l = 0; l < st2.ntrees; l++) {
                                 if (st2.tr[l].code == st2.sp[i].code && st2.tr[l].out < 0 && st2.tr[l].d>=7.0) {
                                     baumtrue = false;
                                     if (iii == 0) baumtrue = true;
                                     if (iii == 1 && st2.tr[l].layer == 1) baumtrue = true;
                                     if (iii == 2 && st2.tr[l].layer == 2) baumtrue = true;
                                     if (iii == 3 && st2.tr[l].layer == 3) baumtrue = true;
                                     if (iii == 4 && st2.tr[l].layer == 4) baumtrue = true;
                                     if (iii == 5 && st2.tr[l].layer == 0) baumtrue = true;
                                     if (baumtrue == true) {
                                         nhax = nhax + st2.tr[l].fac;
                                         ghax = ghax + st2.tr[l].fac * Math.PI * Math.pow((st2.tr[l].d / 200.0), 2.0);
                                         vhax = vhax + st2.tr[l].fac * st2.tr[l].v;
                                         ivhax = ivhax + st.tr[l].fac * st.tr[l].v;
                                     }
                                 }
                             }           
                             if (st2.size > 0) {
                                 nhax = nhax / st2.size;
                                 ghax = ghax / st2.size;
                                 vhax = vhax / st2.size;
                                 ivhax = ivhax / st.size - vhax;
                             }  
                             if (nhax > 0){
                                 double dgx = 200.0 * Math.sqrt(ghax / (nhax * Math.PI));
                                 dgStr = df.format(dgx);
                                 ghaStr = df.format(ghax);
                                 vhaStr = df.format(vhax);
                                 ivhaStr = df.format(ivhax);
                             }
                             if(st2.sp[i].hg <= 0.0){   // keine Volumenangabe ohne Höhenwert
                                 vhaStr = "";
                                 ivhaStr = "";
                             }   
                             if (iii == 0 && st2.sp[i].hg > 1.31) hgStr = df.format(st2.sp[i].hg);
                             
                             // Spaltensummen berechnen
                             if (iii == 0) {
                                 sumNha = sumNha + nhax;
                                 sumGha = sumGha + ghax;
                                 sumVha = sumVha + vhax;  
                                 sumiVha = sumiVha + ivhax;
                                 sumNb = sumNb + nb;     
                             }
                             
                             // D100
                             String d100Str = "";
                             // D100 bezieht sich auf den verbleibenden Bestand (ebenso H100, s.u.)
                             //if (iii == 0 && nhax + nhaax > 100) d100str = df.format(st2.sp[i].d100);
                             if (iii == 0 && nhax > 100) d100Str = df.format(st2.sp[i].d100);
                             // H100     (statt dessen die gemessenen Werte der Oberschicht, s.u.)       
                             String h100Str = "";
                             //if (iii == 0 && nhax > 100 && st2.sp[i].h100 > 1.31) h100Str = df.format(st2.sp[i].h100);
                             // Gemessene Oberhöhe (Oberschicht und Höchstalter)
                             double h100mess = 0.0;
                             int nhmess = 0;
                             for (int l = 0; l < st2.ntrees; l++) {
                                if (st2.tr[l].code == st2.sp[i].code          //&& st2.tr[l].fac > 0.0 auch Höhenmessbäume berücksichtigen
                                     && st2.tr[l].layer == 1 && st2.tr[l].hMeasuredValue > 0.0
                                     && st2.tr[l].age == amax) {
                                    nhmess = nhmess+1;
                                    h100mess = h100mess + st2.tr[l].hMeasuredValue;
                                }
                             }
                             if(h100mess > 0.0) h100mess = h100mess/nhmess;
                             if (iii == 0 && h100mess > 1.31) h100Str = df.format(h100mess);
                             
                             // Ertragsklasse und Bestockungsgrad
                             String ekl = "";
                             Double bgrad = 0.0;
                             String hbon = "";
                             boolean hauptbaumart = false;
                             boolean bgradBerechnen = true;         // Unrealistische Bgrade bei großer Extrapolation vermeiden
                             if((st2.sp[i].code == 211)){  // Buche
                                 hauptbaumart = true;
                                 if(amax < 25) bgradBerechnen = false;
                             }
                             if(st2.sp[i].code>=511 && st2.sp[i].code<=519){     // Fichte
                                 hauptbaumart = true;
                                 if(amax < 15) bgradBerechnen = false;
                             }
                             if(st2.sp[i].code>=711 && st2.sp[i].code<=731){     //Kiefer
                                 hauptbaumart = true;
                                 if(amax < 20) bgradBerechnen = false;
                             }
                             if(st2.sp[i].code>=110 && st2.sp[i].code<=115){     // Eiche
                                 hauptbaumart = true;
                                 if(amax < 15) bgradBerechnen = false;
                             }
                             if (iii == 0){
                                 //Ertragsklasse nach gemessener Oberhöhe
                                 double h100merk = st2.sp[i].h100;
                                 Tree tree = new Tree();
                                 tree.age = amax;
                                 tree.sp = st2.sp[i];
                                 tree.sp.h100 = h100mess;
                                 Ertragstafel et = new Ertragstafel(localPath);     //! nur amax!
                                 Ertragstafelzeile etz = et.werteBerechnen(st2.sp[i], amax);
                                 st2.sp[i].h100 = h100merk;
                                 double eklDouble = etz.getEkl();
                                 if (eklDouble != -9999.0) {
                                     if(h100mess > 0) ekl = String.valueOf(eklDouble);
                                     if(etz.gha>0 & bgradBerechnen) bgrad = Math.round(ghax/etz.gha*10)/10.0;
                                 }
                                 // Bonität nach gemessener Oberhöhe
                                 if(h100mess > 0.0 && hauptbaumart)
                                     hbon = df.format(bonET.getAbsHAge100(st2.sp[i], eklDouble)); // Bonität (Höhe im Alter 100)

                             }
                             sumbgrad = sumbgrad + bgrad;
  
                             
                             // Ergebnis ausgeben
                             String art = String.valueOf(st2.sp[i].code);
                             String schicht = "ges";
                             if(iii == 1) schicht = "O";
                             if(iii == 2) schicht = "M";
                             if(iii == 3) schicht = "U";
                             if(iii == 4) schicht = "Ü";
                             if(iii == 5) schicht = " ";
                             String nbStr = df0.format(nb);
                             String nhaxStr = df0.format(nhax);
                             String bgradStr = "";
                             if(bgrad>0) bgradStr = df.format(bgrad);
                              String amaxStr = "";
                             if(amax != 0) amaxStr = amax.toString();
                             String aminStr = "";
                             if(amin != 999) aminStr = amin.toString();
                             if(nb>0) ausgabe.println("'"+idSelected+"';'"+art+"';'"+schicht+"';"+aminStr+";"+
                                     amaxStr+";"+nbStr+";"+d100Str+";"+h100Str+";"+dgStr+";"+
                                     hgStr+";"+nhaxStr+";"+ghaStr+";"+vhaStr+";"+ivhaStr+";"+bgradStr+
                                     ";"+ekl+";"+hbon);
                         } //Ende Kollektive
                     } //Ende Baumarten
                     //Schreiben der Summen
                     String sumNhaStr = df0.format(sumNha);
                     sumGha = Math.round(sumGha*10)/10.0;
                     sumVha = Math.round(sumVha*10)/10.0;
                     sumiVha = Math.round(sumiVha*10)/10.0;
                     String sumNbStr = df0.format(sumNb);
                     String sumbgradStr = df.format(sumbgrad);
                     ausgabe.println("'"+idSelected+"';"+"'ges'"+";"+"'ges'"+";"+""+";"+
                                     ""+";"+sumNbStr+";"+""+";"+""+";"+""+";"+
                                     ""+";"+sumNhaStr+";"+sumGha+";"+sumVha+";"+sumiVha+";"+
                                     sumbgradStr+";"+""+";"+"");
                     alleAufnahmenOk = true;
                 } // Ende der Schleife über alle Aufnahmen
             } // Ende der Schleife über alle Parzellen
         }
         catch(Exception e){e.printStackTrace();}
         finally{
             try{
                 if(rs != null) rs.close();
                 if(stmt != null) stmt.close();
                 if(ausgabe != null) ausgabe.close();
             } catch(Exception e){e.printStackTrace();}
         }
         if(alleAufnahmenOk) fertig = true;

        idSelected = jComboBox8.getSelectedItem().toString().trim(); // zurücksetzen
        return fertig;
    }
    
    
    public boolean isInteger(String in){
        boolean ergebnis;
        try{
            Integer.parseInt(in);
            ergebnis = true;
        } catch (NumberFormatException nfe) {
            ergebnis = false;
        }
        return ergebnis;
    }
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
}
