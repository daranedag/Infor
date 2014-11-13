/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HöheneingabeTabelle.java
 *
 * Created on 20.04.2010, 16:43:07
 */

package vis2006;
import DatabaseManagement.*;
import java.sql.*;

/**
 *
 * @author sprauer
 */
public class HöhenEingabetabelle extends javax.swing.JDialog {

    DBConnection dbconn;
    String edvid;
    int auf;
    String ergebnis = "";

    /** Creates new form HöheneingabeTabelle */
    public HöhenEingabetabelle(java.awt.Frame parent, boolean modal, DBConnection dbcon, boolean dialogZeigen) {
        super(parent, modal);
        initComponents();
        dbconn = dbcon;
        if(dialogZeigen){
            Statement stmt = null;
            ResultSet rs = null;
            try{
                stmt = dbconn.Connections[0].createStatement();
                rs = stmt.executeQuery("SELECT DISTINCT edvid FROM Baum;");
                while(rs.next()){
                    String edvidx = "";
                    Object edvidt = rs.getObject("edvid");
                    if(edvidt != null) edvidx = edvidt.toString().trim();
                    jComboBox1.addItem(edvidx);
                }
            } catch (Exception e){e.printStackTrace();
            } finally{
                try{
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                } catch (Exception e) {e.printStackTrace();}
            }
            setVisible(true);
        }
    }


    /**
     * Erstellt eine Tabelle mit Spalten zur Eingabe der Baumnummer, Baumart, Höhe und Kronenansatz
     */
    public String tabelleErstellen(){
        ergebnis="Tabelle konnte nicht angelegt werden, Tabelle vorhanden ?";
        BasicQueries basicQueries= new BasicQueries(dbconn.Connections[0]);

        String tabellenKopf="nr CHAR(6), art INT, h INT, ka INT";
        boolean tabelleErzeugt=basicQueries.makeTable("HöhenEingabe", tabellenKopf);

        if(tabelleErzeugt) ergebnis = "Höheneingabetabelle angelegt.";
        return ergebnis;
    }


    /**
     * Aktualisieren der Baumtabelle mit den Werten der Höheneingabetabelle
     * @return Erfolgs- bzw. Fehlermeldung als String
     */
    public String werteÜbertragen(){
        ergebnis = "Fehler";

        // Kontrollieren, ob schon Werte vorhanden sind
        Statement stmt = null;
        ResultSet rs = null;
        int nvorhanden = 0;
        try{
            Statement stmt2 = dbconn.Connections[0].createStatement();
            stmt2.executeUpdate("UPDATE HöhenEingabe SET ka = 0 WHERE ka IS NULL");
            stmt2.close();
            stmt = dbconn.Connections[0].createStatement();
            rs = stmt.executeQuery("SELECT trim(Baum.nr) as nr, Baum.art, Baum.h, Baum.k FROM Baum " +
                    "INNER JOIN HöhenEingabe ON trim(Baum.nr) = trim(HöhenEingabe.nr) AND Baum.art = HöhenEingabe.art " +
                    "WHERE edvid = '"+edvid+"' AND auf = "+auf+" " +
                    "AND (Baum.h > 0 OR Baum.k > 0);");
            while(rs.next()){
                nvorhanden++;
                if(nvorhanden==1){
                    System.out.println("Werte für folgende Baumnummern bereits vorhanden:");
                }
                String nrx = rs.getString("nr");
                System.out.print(nrx+",");
            }
        } catch (Exception e){e.printStackTrace();
        } finally{
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            } catch (Exception e) {e.printStackTrace();}
        }
        if(nvorhanden > 0) ergebnis = "Keine Übertragung in Baumtabelle. Es sind bereits Werte vorhanden!";
        else{
            Statement stmt2 = null;
            try{
                stmt2 = dbconn.Connections[0].createStatement();
                int n = stmt2.executeUpdate("UPDATE Baum INNER JOIN HöhenEingabe " +
                        "ON trim(Baum.nr) = trim(HöhenEingabe.nr) AND Baum.art = HöhenEingabe.art " +
                        "SET Baum.h = HöhenEingabe.h, Baum.k = HöhenEingabe.ka " +
                        "WHERE edvid = '"+edvid+"' AND auf = "+auf+";");
                if(n==1) ergebnis  = "1 Datensatz aktualisiert!";
                else ergebnis  =n + " Datensätze aktualisiert";

            }catch(Exception e){e.printStackTrace();
            } finally{
                try{
                    if(stmt2 != null) stmt2.close();
                }catch(Exception e){e.printStackTrace();}
            }
            
            // Prüfen, ob alle übertragen wurden
            Statement stmt3 = null;
            ResultSet rs3 = null;
            try{
                stmt3 = dbconn.Connections[0].createStatement();
                rs = stmt3.executeQuery("SELECT Höheneingabe.nr, HöhenEingabe.art FROM HöhenEingabe LEFT JOIN Baum " +
                        "ON trim(Baum.nr) = trim(HöhenEingabe.nr) AND Baum.art = HöhenEingabe.art " +
                        "WHERE (edvid = '"+edvid+"' OR edvid IS NULL) AND (auf = "+auf+" OR auf IS NULL) " +
                        "AND Baum.nr IS NULL;");
                while(rs.next()){
                    String nrx = "";
                    Object nrxobj = rs.getObject("nr");
                    if(nrxobj != null) nrx = nrxobj.toString().trim();
                    int artx = rs.getInt("art");
                    ergebnis = "Fehler! Nicht alle Werte konnten zugeordnet werden.  "+ergebnis;
                    System.out.println("Keine Entsprechung in Baum für "+nrx+"(Art "+artx+")");
                }


            }catch(Exception e){e.printStackTrace();
            } finally{
                try{
                    if(rs3 != null) rs3.close();
                    if(stmt3 != null) stmt3.close();
                }catch(Exception e){e.printStackTrace();}
            }
        }
        
        

        return ergebnis;
    }


    /**
    * Löscht eine evtl. vorhandene Höheneingabetabelle
    */
    public String tabelleLöschen(){
        ergebnis = "Fehler beim Löschen!";
        Statement stmt = null;
        try{
             stmt = dbconn.Connections[0].createStatement();
             stmt.execute("DROP TABLE HöhenEingabe;");
             ergebnis = "Tabelle gelöscht.";
        } catch (Exception e){
            e.printStackTrace();
            ergebnis="Tabelle konnte nicht gelöscht werden, Tabelle nicht vorhanden!";
        } finally{
            try{
                if(stmt != null) stmt.close();
            }catch (Exception e){ e.printStackTrace();}
        }
        dbconn.closeAll();
        return ergebnis;
    }

    public String getErgebnis(){
        return ergebnis;
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Übernahme von Höhe und Kronenansatz in die Baumtabelle");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Parzelle (edvid)");

        jLabel2.setText("Aufnahme");

        jButton1.setText("ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 87, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(241, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    // ComboBox2 (Aufnahmen) aktualisieren
        if(jComboBox1.getItemCount()>0){
            edvid = jComboBox1.getSelectedItem().toString();
            Statement stmt = null;
            ResultSet rs = null;
            try{
                stmt = dbconn.Connections[0].createStatement();
                rs = stmt.executeQuery("SELECT DISTINCT auf FROM Baum WHERE edvid = '"+edvid+"';");
                while(rs.next()){
                    int aufx = rs.getInt("auf");
                    jComboBox2.addItem(Integer.toString(aufx));
                }
            } catch (Exception e){e.printStackTrace();
            } finally{
                try{
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                } catch (Exception e) {e.printStackTrace();}
            }
        }

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Button "ok"
        auf = Integer.parseInt(jComboBox2.getSelectedItem().toString());
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

}
