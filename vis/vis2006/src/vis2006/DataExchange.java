package vis2006;

/** open and close databases 
 * get single Elements of Databases*/

public class DataExchange {
    DataBase DB = new DataBase ();
    String url;
    String sql;   
    
/** Opens PostgreSQL Database    */
    public void OpenMySQLDB(String dbname, String username, String passwort){
       url="jdbc:mysql://db/"+dbname;
       String driver = "org.gjt.mm.mysql.Driver";
       DB.Open(driver, url, username, passwort);        
    }
    
/** Close Database any kind of Database*/
    public void CloseDB (){
       DB.Close();        
    }

/** get Number of Rows*/
    public void GetMeta (String table){      
       DB.GetMeta(table);
   }
    
    
/** returns true if there ist a next resultset and gets next ResultSet*/
    public boolean FindResultNext (){
        boolean nextdata=true;
        try{DB.Result.next();}
        catch (Exception Ex){nextdata=false;}       
        return nextdata;
    }  
    
/** get next resultset*/
    public void ResultNext (){
        
        try{DB.Result.next();}
        catch (Exception Ex){
            System.out.println(DB.GetFehler(Ex, "no data or end of file"));
        }       
    } 
/** get next resultset Plus*/
    public boolean ResultNextPlus (){
        boolean mehrDaten = true;
        try{ mehrDaten=DB.Result.next();}
        catch (Exception Ex){
            System.out.println(DB.GetFehler(Ex, "no data or end of file"));
        } 
       return mehrDaten; 
    }     
/** returns boolean=true if Query returns Integer
 * make a Query first*/
   public boolean FindInt(String columname){
       boolean answer=true;
       int testint=0;
               
       try{
           testint=DB.Result.getInt(columname);
           if (testint==0) {answer=false;}
       }
       catch (Exception Ex){answer=false;}

       return answer;
   }
   
    
/** returns boolean=true if Query returns Double    
 * Query may only query single number*/
   public boolean FindDouble(String columname){
       boolean answer=true;
       double testdouble=0;

       try{
           DB.Result.next();
           testdouble=DB.Result.getDouble(columname);
           if (testdouble==0) {answer=false;}
       }
       catch (Exception Ex){answer=false;}       
       
       return answer;
   }
 
/** returns boolean=true if Query returns Double    
 * Query may only query single number*/
   public boolean FindString(String columname){
       boolean answer=true;
       String teststring="";

       try{
           teststring=DB.Result.getString(columname);
           if (teststring=="") {answer=false;}
       }
       catch (Exception Ex){answer=false;}       
       
       return answer;
   }   

/** returns Integer from DB 
  * */
   
   public String getString(String columnname){
       String stringout="";
       try{stringout=DB.Result.getString(columnname);}
       catch (Exception Ex){
           DB.GetFehler(Ex,"getString");
       }
       return stringout;
   }

   public String getStringByColumn(int columnnumber){
       String stringout="";
       try{stringout=DB.Result.getString(columnnumber);}
       catch (Exception Ex){
           DB.GetFehler(Ex,"getString");
       }
       return stringout;
   }
   
   
/** returns Integer from DB 
 * */
   public int getInt (String columnname){
       int intout=0;
       try{intout=DB.Result.getInt(columnname);}
       catch (Exception Ex){DB.GetFehler(Ex,"getInt");}   
       return intout;
   }
   

/**returns double from DB   
 * */
   public double getDouble (String columnname){
       double doubleout=0;
       try{doubleout= DB.Result.getDouble(columnname);}
       catch (Exception Ex){DB.GetFehler(Ex,"getDouble");}
       return doubleout;
   }
   
/**returns float from DB   
 * */
   public float getFloat (String columnname){
       float floatout=0;
       try{floatout= DB.Result.getFloat(columnname);}
       catch (Exception Ex){DB.GetFehler(Ex,"getDouble");}
       return floatout;
   }   
  
/** inserts Values into DBtable*/
   public void InsertValues (String sql){           
       DB.SetData(sql); 
   }   
   
/** Querys the DB   */
   public void Query (String query){               
       DB.GetQuery(query);
   }    

/** Querys select * from table   */
   public void QueryAll (String table){               
       DB.GetQuery("select * from "+table);
   }
   
/** create table to save information from stand item*/
   public void CreateStandTable (String table){
        sql="Create table "+table+
        " (n int4, id text , year int4, ha float8, ntrees int4, ncnpt int4,cnpt1_x float8, cnpt1_y float8, cnpt1_z float8, cnpt2_x float8, cnpt2_y float8, cnpt2_z float8, cnpt3_x float8, cnpt3_y float8, cnpt3_z float8, cnpt4_x float8, cnpt4_y float8, cnpt4_z float8)";

        DB.SetData(sql);  
   }       
   
/** create table to save information from tree array */
   public void CreateTreesTable (String table){
        sql="Create table "+table+
        " (id text, Code int4, N int4, No int4, Age int4, DBH float8, Height float8, Siteindex float8, Crownbase float8, Crownwidth float8, Alive int4, Removalcode int4, xCoord float8, yCoord float8, zCoord float8, Croptree float8, layer int4)";

        DB.SetData(sql);   
    }  
   
    /** create table to save species information*/
    public void CreateSpeciesTable (String table){
        sql="Create table "+table+" (id text, code int4, h100age float8, dg float8, hg float8, d100 float8, h100 float8, hbon float8, dgout float8, vol float8, gha float8, nha int4, vhaout float8, ghaout float8, nhaout int4, percBA float8, percCSA float8, size float8)";
        DB.SetData(sql); 

    }  
    
/** create table to save ingrowth species information*/
    public void CreateIngrowthTable (String table){
        String sql;
        int n=0;
        sql="Create table "+table+" (n int4, id text, species int4, age int4, percent float8, siteindex float8)";
        DB.SetData(sql); 

    }       

       
}   


