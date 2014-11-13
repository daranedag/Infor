package vis2006;


import java.sql.*; 
import java.io.*;

import javax.swing.table.*;
import java.util.*;

/** basic database comands*/
public class DataBase
{   
    Connection Con;   
  
    ResultSetMetaData Metadata;
    ResultSet Result;
    
    Statement Stmnt;    
    
    int n_Cols, n_Rows; //Spaltenzähler, Zeilenzähler

    String columnname[];
      
/**returns SQLerror */
    public String GetFehler (Exception ex, String identifier){
        
        StringBuffer SB =new StringBuffer();
        SB.append(identifier+"\n");
        SB.append(ex + "\n");
        if (ex instanceof SQLException){
            SB.append("SQLState: " + ((SQLException)ex).getSQLState() + "\n" );
            SQLException e;
            while ((e = ((SQLException)ex).getNextException()) != null)
                    SB.append(e.getSQLState() + "\n");
        }
            
        return SB.toString();
     }
     
       
/**Connect to DB*/
    public void Open(String driver, String url, String user, String passwort){
            try{
                Class.forName(driver);
                Con = DriverManager.getConnection (url,user,passwort);
            }
        
            catch (Exception ex){
                System.out.println(GetFehler(ex, "connect to database"));
            }
        }
    
/** Disconnect from DB*/
    public void Close(){   
            try{
                Con.close();
            }
            
            catch (Exception ex){
                System.out.println(GetFehler(ex, "disconnect from database"));
            }            
            
        }
    
/**Query DB   */
    public void GetQuery(String query){
        try{
            Stmnt = Con.createStatement();
            Result = Stmnt.executeQuery(query);                 
        }
        catch (Exception ex){
            System.out.println(GetFehler(ex, query));
        }
    }
    
/**Insert Data into DB  */
    public void SetData (String sql){
        try{ 
            Stmnt = Con.createStatement();
            Stmnt.executeUpdate(sql);
            Stmnt.close();
               
        }
        catch (Exception ex){
            System.out.println(GetFehler(ex, sql));
        }       
        
    }
    
 
/** get Metadata from table: Number of Colums, Columnnames, Nuber of Rows*/
    public void GetMeta (String table){
        try{
            Stmnt= Con.createStatement();            
            Result = Stmnt.executeQuery("select * from "+table);
            
        // read Metadata
            Metadata = Result.getMetaData();                 

        //get Number of Columns from Metadata
            n_Cols = Metadata.getColumnCount();
                
        //get Columnnames
            columnname = new String[1000];
            for (int i=1;i<=n_Cols;i++)
            { columnname[i] = Metadata.getColumnName(i);}     
 
        //Zeilenanzahl feststellen
           Result.last();
           n_Rows=Result.getRow();
           
        //Cursor an den Anfang stellen
           System.out.println("Number of Rows = "+n_Rows);            
        }
        catch (Exception ex){
            System.out.println(GetFehler(ex, "get metadata"));
        }        
         
    }
}   
   



        

    
    
    

        
    


   

