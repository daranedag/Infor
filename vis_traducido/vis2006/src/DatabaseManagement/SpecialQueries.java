/*
 * SpecialQueries.java
 *
 * Created on 25. Januar 2006, 11:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package DatabaseManagement;
import java.sql.*;
import java.util.Vector;
import treegross.base.*;
import java.util.StringTokenizer;

/**
 *
 * @author jhansen
 */
public class SpecialQueries extends BasicQueries {
    //private Connection dbcon;
        
    /** Creates a new instance of SpecialQueries */
    public SpecialQueries() {     
    }   
    
    public SpecialQueries(Connection con) {
        this.setConnection(con);
    }       
    
    /* makes a treegross stand form single tree data*/
/*    public Stand makeTGS(String table, String field, String cond, String userpath){
        Stand st=new Stand();

        st.setProgramDir(userpath);
        try{          
            //dbcon.setReadOnly(true);
            //dbcon.setAutoCommit(false);
            Statement stmt = dbcon.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ " WHERE "+field+" = '"+cond+"'");
            // hier das Einlesen noch einfügen
            st.addName(cond);
            int year=0;
            //String STPN="";
            double area=0;                    
            String no="";
            int species=0;
            int age=0;
            double x=0;
            double y=0;
            double z=0;
            double dbh=0;
            double height=0;
            double crownbase=0;
            double crownwidth=0;
            double si=0;
            int out=0;
            int outtype=0;
            double factor=0;      
            int counter=0;
            while(rs.next()){
                if(counter==0) area =(Double)rs.getObject("Area"); // read only once
                year       =(Integer)rs.getObject("Jahr");
                no         =rs.getObject("BNo").toString();
                species    =rs.getInt("Art");
                age        =rs.getInt("Age");
                x          =rs.getDouble("X");
                y          =rs.getDouble("Y");
                
                dbh        =rs.getDouble("BHD");
                height     =rs.getDouble("Hoehe");
                crownbase  =rs.getDouble("Kronenansatz");
                crownwidth =rs.getDouble("Kronenbreite");
                out        =rs.getInt("Out");
                outtype    =rs.getInt("Outtype");
                factor     =rs.getDouble("Factor");
                si         =rs.getDouble("SI");
                st.addtreefac(species,no,age,out,dbh,height,crownbase,crownwidth,
			                  si,x,y,z,0,0,0,factor);    
                counter++;
            } 
            rs.close();
            stmt.close();
            
            st.addsize(area);
            double radius=Math.sqrt((area*10000.0)/Math.PI);
            st.year=year;
            //st.ageclass=aklx;
            //st.bt=btx;
            for (int i=0;i<20;i++) {
                double winkelbogenmass=(i*360/20.0)*Math.PI/180;
                double exp=radius+(radius*Math.sin(winkelbogenmass));
                double eyp=radius+(radius*Math.cos(winkelbogenmass));
                Integer nrx= new Integer(i+1);
                st.addcornerpoint(("Eck"+nrx.toString()),exp,eyp,0.0);
            }               
         }
         catch(Exception e){System.out.println("SpecialQueries (creating Stand): "+e);}
        
        st.descspecies();
        return st;
    }
*/
      /* Erstellt einen TreeGross-Bestand aus einer BWI Datenbank die bereits geöffnet ist */
/*
    public Stand makeTGStandBWI(String table, String Feld, String Bedingung, String userpath){
         Stand st=new Stand();
         //String userpath= "C:\\Projekte\\KSPSimulator\\user"; // noch zentralisieren !!
         st.setProgramDir(userpath);
         try{          
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(false);
            Statement stmt = dbcon.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ " WHERE "+Feld+" = "+Bedingung);
                      
           // Einzelbaum einlesen 
           st.ntrees=0;
	   st.nspecies=0;
	   st.ncpnt=0;
           int aklx =0;
           int btx =0;
           String treeNo ="";
           int treeCode =0;
           double standSize =0.0; //[ha]
           String standName ="";
           String standFoa ="";
           int  standJahr =0;
           double treeFac =0.0;
           double treeGon =0.0;
           double treeEnt =0.0;  //[m]
           double treeD1 =0.0;   //[cm]
           double treeD2 =0.0;   //[cm]
           double treeH =0.0;    //[m]
           double treeCb =0.0;   //[m]
           int treeAlt=0;
           int treeAlive=0;
           String treeWert="";
           String treeSchaeden="";
           String treeNaturschutz="";
            
           while(rs.next()){
                standName = rs.getString(1);
                standFoa = rs.getString(2);
                standJahr = rs.getInt(3);
                aklx = rs.getInt(4);
                btx = rs.getInt(5);
                treeNo = rs.getString(6);
                treeCode = rs.getInt(7);
                standSize = rs.getDouble(8);
                treeFac = rs.getDouble(9);
                treeAlt = rs.getInt(10);
                treeGon = rs.getDouble(11);
                treeEnt = rs.getDouble(12);
                treeD1 = rs.getDouble(13);
                treeD2 = rs.getDouble(14);
                treeH = rs.getDouble(15);
                treeCb = rs.getDouble(16);
                treeWert = rs.getString(17);
                treeSchaeden = rs.getString(18);
                treeNaturschutz = rs.getString(19);
                treeAlive=-1;
                              
                if (treeCode==100) {treeCode=111; treeAlive=standJahr;}
                if (treeCode==200) {treeCode=211; treeAlive=standJahr;}
                if (treeCode==300) {treeCode=311; treeAlive=standJahr;}
                if (treeCode==400) {treeCode=411; treeAlive=standJahr;}
                if (treeCode==500) {treeCode=511; treeAlive=standJahr;}
                if (treeCode==600) {treeCode=611; treeAlive=standJahr;}
                if (treeCode==700) {treeCode=711; treeAlive=standJahr;}
                if (treeCode==800) {treeCode=811; treeAlive=standJahr;}
                              
                int treeAnz = (int) treeFac;  
                treeFac=treeFac/treeAnz;
                if (treeD2>0) treeD1=(treeD1+treeD2)/2.0;
                double winkelbogenmass=(treeGon)*Math.PI/180;
                double radius=Math.sqrt((standSize*10000.0)/Math.PI);
                double xp=radius+(treeEnt*Math.sin(winkelbogenmass));
                double yp=radius+(treeEnt*Math.cos(winkelbogenmass));
                if (treeGon>400) {
                    xp=-9;
                    yp=-9;
                }
                if (st.ntrees==0){
                                  st.addName(Bedingung);
                                  st.addsize(standSize);
                                  st.year=standJahr;
                                  st.ageclass=aklx;
                                  st.bt=btx;
                                  for (int i=0;i<20;i++) {
                                       winkelbogenmass=(i*360/20.0)*Math.PI/180;
                                       double exp=radius+(radius*Math.sin(winkelbogenmass));
                                       double eyp=radius+(radius*Math.cos(winkelbogenmass));
                                       Integer nrx= new Integer(i+1);
                                       st.addcornerpoint(("Eck"+nrx.toString()),exp,eyp,0.0);
                                    }
                }
                String treeNoAlt="";
                treeNo=treeNo.trim();
               
                for (int i=0;i<treeAnz;i++){
                        if (i==0){
                            if (treeNo.length()==0) {
                                Integer nrx= new Integer(st.ntrees+1);
                                treeNo= nrx.toString();
                               
                            }
                            treeNoAlt=treeNo;// geänder aus if bedingung raus !!!!
   		            st.addtreefac(treeCode,treeNo,treeAlt,treeAlive,treeD1,treeH,treeCb,0.0,
			                  -9.0,xp,yp,-9.0,0,0,0,treeFac);
                        }
                        else {
                            Integer nrx= new Integer(i);       
                            String nn;
                            nn=treeNoAlt+"_"+nrx;
                            treeNo=nn;
                            st.addtreefac(treeCode,treeNo,treeAlt,treeAlive,treeD1,treeH,treeCb,0.0,
			                  -9.0,-9.0,-9.0,-9.0,0,0,0,treeFac); 
                        }
                        st.tr[st.ntrees-1].remarks=treeWert+";"+treeSchaeden+";"+treeNaturschutz;
                }
                
                             
            }
            // Ende Einzelbaum einlesen
            rs.close();
            stmt.close();
            
            //
            // update missing data
            //
            //mark the dead trees
            for (int i=0; i<st.ntrees;i++) if (st.tr[i].out>0) st.tr[i].outtype=1;
                  st.sortbyd();
                  st.missingData();
                  st.descspecies();
                  GenerateXY gxy =new GenerateXY();
                  gxy.zufall(st); 
            return st;
            }
            catch (Exception e){
                System.out.println(e);
                return st;
            }
     }
 */
     
     public boolean makeResultTableIndices(String table){
        return this.makeTable(table, "ID INT, FOA INT, REF INT, ABT INT, UABT CHAR(3), UFL INT, Jahr INT, Vol_ha FLOAT, dg FLOAT, G_ha FLOAT, N_ha FLOAT, N_Arten FLOAT");
     }
     
     public boolean makeResultTableTrees(String table){
        return this.makeTable(table, "Jahr INT, ID CHAR(30), Area FLOAT, BNo CHAR(30), " +
                "Art INT, Age INT, X FLOAT, Y FLOAT, BHD FLOAT, Hoehe FLOAT, Kronenansatz FLOAT, " +
                "Kronenbreite FLOAT, Volumen FLOAT, Out INT, Outtype INT, Factor Float, SI Float, Layer INT");
     } 
     
     public boolean makeResultTableSpecies(String table){
        return this.makeTable(table, "Jahr INT, ID INT, Art INT, Volumen FLOAT, dg FLOAT, Gha FLOAT, Nha Float");
     }
     
     public boolean saveTreesToDB(String table, Stand st){
        boolean ok=false;
        try{
            dbcon.setReadOnly(false);
            if(dbcon.getAutoCommit())dbcon.setAutoCommit(false);
            PreparedStatement ps = dbcon.prepareStatement
                                ("INSERT INTO "+table+" (Jahr, ID, Area, BNo, Art, Age, X, Y, BHD, Hoehe, Kronenansatz, Kronenbreite, Volumen, Out, Outtype, Factor, SI, Layer)"+
                                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i=0; i< st.ntrees; i++) {
                ps.setInt    (1, 2000);
                ps.setString (2, st.standname);
                ps.setDouble (3, st.size);
                ps.setString (4, st.tr[i].no );
                ps.setInt    (5, st.tr[i].sp.code);
                ps.setInt    (6, st.tr[i].age);
                ps.setDouble (7, st.tr[i].x);
                ps.setDouble (8, st.tr[i].y);
                ps.setDouble (9, st.tr[i].d);
                ps.setDouble (10, st.tr[i].h);
                ps.setDouble (11, st.tr[i].cb);
                ps.setDouble (12, st.tr[i].cw);
                ps.setDouble (13, st.tr[i].v);
                ps.setInt    (14, st.tr[i].out);
                ps.setInt    (15, st.tr[i].outtype);
                ps.setDouble (16, st.tr[i].fac);
                ps.setDouble (17, st.tr[i].si);
                ps.setInt    (18, st.tr[i].layer);
                ps.executeUpdate();
            }
            ps.close();
            dbcon.commit();
            dbcon.setAutoCommit(true);
            ok=true;    
            System.out.println("Stand "+ st.standname+" saved to "+table);
            return ok;
        }
        catch (Exception e){
            System.out.println(e);
            ok=false;
            return ok;
        }       
    }
     
    public void saveIndicesToDB( String table, Stand st){
        try{
            dbcon.setReadOnly(false);
            dbcon.setAutoCommit(false);
            PreparedStatement ps = dbcon.prepareStatement
                                ("INSERT INTO "+table+" (ID,FOA, REF, ABT, UABT, UFL, Jahr, Vol_ha, dg, G_ha, N_ha, N_Arten)"+
                                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
           
            ps.setInt    (1, Integer.valueOf(st.standname));
            ps.setInt    (2, 99);
            ps.setInt    (3, 12);
            ps.setInt    (4, 1);
            ps.setString (5, "a");
            ps.setInt    (6, 1);
            ps.setInt    (7, st.year);
            ps.setDouble (8, 99);
            ps.setDouble (9, st.dg);
            ps.setDouble (10, st.bha);
            ps.setDouble (11, st.nha);
            ps.setDouble (12, st.nspecies);
            ps.executeUpdate();          
            ps.close();
            dbcon.commit();
            System.out.println("Stand "+ st.standname+" saved to "+table+".");
          
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    
    public void saveSpeciesToDB(String table, Stand st){
        try{
            dbcon.setReadOnly(false);
            dbcon.setAutoCommit(false);
            PreparedStatement ps = dbcon.prepareStatement
                                ("INSERT INTO "+table+" (Jahr, ID, Art, Volumen, dg, Gha, Nha)"+
                                                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            for (int i=0; i< st.nspecies; i++) {
                ps.setInt    (1, 2000);
                ps.setInt    (2, Integer.valueOf(st.standname));
                ps.setInt    (3, st.sp[i].code);
                ps.setDouble (4, st.sp[i].vol);
                ps.setDouble (5, st.sp[i].dg);
                ps.setDouble (6, st.sp[i].gha);
                ps.setDouble (7, st.sp[i].nha);
                ps.executeUpdate();
           }
           ps.close();
           dbcon.commit();
           System.out.println("Stand "+ st.standname+" saved to "+table+".");
          
       }
       catch (Exception e){
            System.out.println(e);
         }
       
    }
}
