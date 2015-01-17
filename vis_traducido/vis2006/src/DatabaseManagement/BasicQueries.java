/*
 * BasicQueries.java
 *
 * Created on 25. Januar 2006, 10:40
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package DatabaseManagement;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Hashtable;

/**
 *
 * @author jhansen
 * this class hold several usefull sql-queries
 */
public class BasicQueries {
    public Connection dbcon;

    /**
     * Creates a new instance of BasicQueries
     */
    public BasicQueries() {
    }

    public BasicQueries(Connection con) {
        dbcon=con;
    }

    public void setConnection(Connection con){
        dbcon=con;
    }

    public void setConnection(String db, String user, String password){
        DBConnection con =new DBConnection(1);
        con.openDBConnection(DBConnection.UNKNOWN, 0, db, user, password, false, true);
        dbcon=con.Connections[0];
    }

    public void closeConnection(){
        try {
            dbcon.close();
            System.out.println("AbstractQueries: connection[0] closed successfully.");
        }
        catch(Exception e) {System.out.println(e);}
    }

//TableOperations:

    public void deleteTable(String table){
        boolean tableexists=false;
        try{
            dbcon.setReadOnly(false);
            dbcon.setAutoCommit(true);
            DatabaseMetaData dbm= dbcon.getMetaData();
            String lv_tab_type[] = { "TABLE" };
            // Liste aller Tabellen ermitteln
            ResultSet lv_table_set = dbm.getTables(null, null, "%", lv_tab_type);
            while(lv_table_set.next()) {
                String lv_tab_name = lv_table_set.getString(3);
                if(lv_tab_name.compareToIgnoreCase(table)== 0 ){tableexists=true; System.out.println("table "+table+" exists");}
            }
            if (tableexists){
                Statement stmt = dbcon.createStatement();
                stmt.execute("DROP TABLE "+table);
                System.out.println(table+" sucsessfully deleted.");
                stmt.close();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     *creates an ne table in the given db-connection
     *Tables = the name of the new table
     *Columns = a String of columnnames and sql datatypes separated by a comma: ID INT, Name CHAR(30),...
    */
    public boolean makeTable(String table, String columns){
        boolean tableexists=false;
        boolean created=false;
        try{
            dbcon.setReadOnly(false);
            dbcon.setAutoCommit(true);
            DatabaseMetaData dbm= dbcon.getMetaData();
            String lv_tab_type[] = { "TABLE" };
            // Liste aller Tabellen ermitteln
            ResultSet lv_table_set = dbm.getTables(null, null, "%", lv_tab_type);
            while(lv_table_set.next()) {
                String lv_tab_name = lv_table_set.getString(3);
                if(lv_tab_name.compareToIgnoreCase(table)== 0 ){tableexists=true; System.out.println("Table "+table+" already exists!");}
            }
            lv_table_set.close();
            if (tableexists==false){
                Statement stmt = dbcon.createStatement();
                String statement="CREATE TABLE "+table+" ("+ columns+")";
                String nativesmt=dbcon.nativeSQL(statement);
                System.out.println(nativesmt);
                stmt.execute(nativesmt);
                System.out.println(table+" sucsessfully created.");
                stmt.close();
                created=true;
           }
            else{created=false;}
       }
       catch (Exception e){
            System.out.println(e);
            created= false;
       }
       return created;
    }

/*  Wird nicht verwendet
    public boolean copyTable(Connection sc, String st, Connection dc, String dt){
        boolean ok=false;
        //get metadata
        setConnection(sc);
        Vector names=getHeader(st);
//        Vector types=getTypes(st);
        Vector typenames= getTypeNames(st);
        Vector data=getTable(st);
        String statement=names.get(0).toString()+" "+typenames.get(0).toString();
        String onlynames=names.get(0).toString();
        String questionmarks="?";
        for(int i=1; i < names.size(); i++){
            statement=statement+", "+names.get(i).toString()+" "+typenames.get(i).toString();
            onlynames=onlynames+", "+names.get(i).toString();
            questionmarks=questionmarks+", ?";
        }
        //crate new table
        setConnection(dc);
        makeTable(dt, statement);
        //copy data in new table
        try{
            dbcon.setReadOnly(false);
            if(dbcon.getAutoCommit())dbcon.setAutoCommit(false);

            PreparedStatement ps = dbcon.prepareStatement("INSERT INTO "+dt+" ("+onlynames+")"+"VALUES ("+questionmarks+")");
            for (int i=0; i< data.size(); i++) {
                for(int j=0; j< names.size(); j++){
                     Vector datarow=(Vector)data.get(i);
                     ps.setObject(j+1,datarow.get(j)); //, (Integer)types.get(j)
                }
                ps.executeUpdate();
            }
            ps.close();
            dbcon.commit();
            dbcon.setAutoCommit(true);
            ok=true;
            System.out.println("BasicQueries : table"+ st+" copied to "+dt+" successfully");
            return ok;
        }
        catch (Exception e){
            System.out.println(e);
            ok=false;
            return ok;
        }
    }
*/
/* copyLargeTable geht wesentlich schneller, keine Probleme mit NULL
public boolean copyPartOfTable(Connection sc, String st, Connection dc, String dt, String sql){
        boolean ok=false;
        //get metadata
        setConnection(sc);
        Vector names=getHeaderOfPart(st, sql);
        //Vector types=getTypes(st);
        Vector typenames= getTypeNamesOfPart(st, sql);
        System.out.println("BasicQueries : getPartOfTabel vor");

        Vector data=getPartOfTable(sql);
        System.out.println("BasicQueries : getPartOfTabel nach");
        String statement=names.get(0).toString()+" "+typenames.get(0).toString();
        String onlynames=names.get(0).toString();
        String questionmarks="?";
        String spalte = "";
        for(int i=1; i < names.size(); i++){
            spalte = names.get(i).toString();
            statement=statement+", "+names.get(i).toString()+" "+typenames.get(i).toString();
            onlynames=onlynames+", "+names.get(i).toString();
            questionmarks=questionmarks+", ?";
        }
        System.out.println("CopyPartOfTable Typename:"+typenames);
        //crate new table
        setConnection(dc);
        if(makeTable(dt, statement)){
            //copy data in new table
            try{
                dbcon.setReadOnly(false);
                if(dbcon.getAutoCommit())dbcon.setAutoCommit(false);
                PreparedStatement ps = dbcon.prepareStatement("INSERT INTO "+dt+" ("+onlynames+")"+"VALUES ("+questionmarks+")");
                for (int i=0; i< data.size(); i++) {
                    for(int j=0; j< names.size(); j++){
                        Vector datarow=(Vector)data.get(i);
                        ps.setObject(j+1,datarow.get(j)); // ,(Integer)types.get(j));
                    }
                    ps.executeUpdate();
                }
                ps.close();
                System.out.println("BasicQueries : vor commit CopyPartOftable "+ st+" copied to "+dt+" successfully.");
                dbcon.commit();
                Statement stmt = dbcon.createStatement();
                dbcon.setAutoCommit(true);
                ok=true;
                System.out.println("BasicQueries : CopyPartOftable "+ st+" copied to "+dt+" successfully.");
                return ok;
            }
            catch (Exception e){ System.out.println("BasicQueries : copy faild! "+e);
                ok=false;
                return ok;
           }
        }
        else{
             System.out.println("BasicQueries : copy faild! table "+dt+" already exists!");
             return false;
        }
    }
*/
    public boolean copyLargeTable(Connection sc, String st, Connection dc, String dt, String selectsql) {
        boolean result = false;
        //get metadata
        setConnection(sc);
        Vector<String> names = getHeaderOfPart(st, selectsql);
        Vector<String> typenames = getTypeNamesOfPart(st, selectsql);
        String statement = names.get(0).toString() + " " + typenames.get(0).toString();
        String onlynames = names.get(0).toString();
        for (int i = 1; i < names.size(); i++) {
            statement = statement + ", " + names.get(i).toString() + " " + typenames.get(i).toString();
            onlynames = onlynames + ", " + names.get(i).toString();
        }

        //crate new table
        setConnection(dc);
        if (makeTable(dt, statement)) {
            Statement smt = null;
            ResultSet rs = null;
            Statement ws = null;
            try {
                smt = sc.createStatement();
                rs = smt.executeQuery(selectsql);
                ws = dbcon.createStatement();
                dc.setAutoCommit(false);
                String wsql = "";
                Object value;
                int counter = 0;
                while (rs.next()) {
                    wsql = "INSERT  INTO " + dt + " (" + onlynames + ")" + " VALUES (";
                    for (int j = 0; j < names.size(); j++) {
                        value = rs.getObject(j + 1);
                        if (value instanceof String || value instanceof java.sql.Date || value instanceof java.sql.Timestamp) {
                            if (value instanceof java.sql.Timestamp) {
                                String ts = value.toString();
                                ts = ts.substring(0, ts.length() - 2);
                                wsql = wsql + "'" + ts + "',";
                            } else {
                                wsql = wsql + "'" + value + "',";
                            }
                        } else {
                            wsql = wsql + value + ",";
                        }
                    }
                    wsql = wsql.substring(0, wsql.length() - 1) + ")";
                    //ws.executeUpdate(wsql);
                    ws.addBatch(wsql);
                    counter++;
                    if (counter == 2000) {
                        counter = 0;
                        ws.executeBatch();
                        ws.clearBatch();
                        dc.commit();
                    }
                }
                ws.executeBatch();
                dc.commit();
                dc.setAutoCommit(true);
                result = true;
            } catch (SQLException ex) { ex.printStackTrace();}
            finally {
                try {
                    if (ws != null) ws.close();
                    if (rs != null) rs.close();
                    if (smt != null) smt.close();
                } catch (SQLException ex) {ex.printStackTrace();}
            }
        }
        return result;
    }


  /* returns the header of a given sql-statement as vector*/
    public Vector<String> getHeaderOfPart(String table, String sql){
        Vector<String> tableHeader = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            String dummy="FROM "+table;
            int index =sql.indexOf(dummy)+dummy.length();
            String statement=sql.substring(0, index)+" WHERE 1=0";
            System.out.println(statement);
            // an empty result set where 1=0 can never be true
            ResultSet rs = stmt.executeQuery(statement);
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            /*hier code für die abfrage der hederinformationen*/
            for(int i=1; i<=maxCols; i++){
                tableHeader.addElement(meta.getColumnLabel(i));
            }
            rs.close();
            stmt.close();
            return tableHeader;
        }
        catch (Exception e){
            System.out.println(e);
            return tableHeader;
        }
    }

 public Vector<String> getTypeNamesOfPart(String table, String sql){
        Vector<String> types = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            String dummy="FROM "+table;
            int index =sql.indexOf(dummy)+dummy.length();
            String statement=sql.substring(0, index)+" WHERE 1=0";
            ResultSet rs = stmt.executeQuery(statement);
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            String name = "";
            String label = "";
            for(int i=1; i<=maxCols; i++){
                label = meta.getColumnLabel(i);
                name=meta.getColumnTypeName(i);
                if(name.equals("YEAR")) name="INTEGER";
                if(name.equals("DECIMAL")) name="FLOAT";
                if(name.equals("VARCHAR")){
                    int lang = meta.getPrecision(i);
                    name = "VARCHAR("+lang+")";
                }
                if(name.equals("CHAR")){
                    int lang = meta.getPrecision(i);
                    name = "CHAR("+lang+")";
                }
                if (meta.isAutoIncrement(i)) name = "AUTOINCREMENT";
                if (meta.isNullable(i)== 0 && label.equalsIgnoreCase("Stempel")==false
                        && label.equalsIgnoreCase("datum")==false) name = name + " NOT NULL";
                types.addElement(name);
            }
            rs.close();
            stmt.close();
            return types;
        }
        catch (Exception e){
            System.out.println(e);
            return types;
        }
    }
// MetaData:

    /* returns the header of a table as vector*/
    public Vector getHeader(String table){
        Vector tableHeader = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+" WHERE 1=0");
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            /*hier code für die abfrage der hederinformationen*/
            for(int i=1; i<=maxCols; i++){
                tableHeader.addElement(meta.getColumnLabel(i));
            }
            rs.close();
            stmt.close();
            return tableHeader;
        }
        catch (Exception e){
            System.out.println(e);
            return tableHeader;
        }
    }

    /* returns the SQL Types of all columns of a table*/
/*    public Vector getTypes(String table){
        Vector types = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+" WHERE 1=0");
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            //hier code für die abfrage der hederinformationen
            for(int i=1; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
                int type=meta.getColumnType(i);
                if(name.equals("YEAR")) type=java.sql.Types.INTEGER;
                if(name.equals("DECIMAL")) type=java.sql.Types.FLOAT;
                types.addElement(type);
            }
            rs.close();
            stmt.close();
            return types;
        }
        catch (Exception e){
            System.out.println(e);
            return types;
        }

    } */

    public Vector getTypeNames(String table){
        Vector types = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+" WHERE 1=0");
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            /*hier code für die abfrage der hederinformationen*/
            for(int i=1; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
                if(name.equals("YEAR")) name="INTEGER";
                if(name.equals("DECIMAL")) name="FLOAT";
                types.addElement(name);
            }
            rs.close();
            stmt.close();
            return types;
        }
        catch (Exception e){
            System.out.println(e);
            return types;
        }

    }

    public Vector getClassNames(String table){
        Vector types = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+" WHERE 1=0");
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            /*hier code für die abfrage der hederinformationen*/
            for(int i=1; i<=maxCols; i++){
                types.addElement(meta.getColumnClassName(i));
            }
            rs.close();
            stmt.close();
            return types;
        }
        catch (Exception e){
            System.out.println(e);
            return types;
        }

    }


    /* returns all tables (names) contained in the db as vector*/
     public Vector getTables(){
        Vector liste= new Vector();
        try {
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            DatabaseMetaData dbm= dbcon.getMetaData();
            String lv_tab_type[] = { "TABLE" };
            // Liste aller Tabellen ermitteln
            ResultSet lv_table_set = dbm.getTables(null, null, "%", lv_tab_type);
            // Tabellen ausgeben
            while(lv_table_set.next()) {
                String lv_tab_name = lv_table_set.getString(3);
                liste.add(lv_tab_name);
            }
            return liste;
        }
        catch (Exception e){
            System.out.println(e);
            return liste;
        }
     }

// TableData:

    /* returns all data of a table as vector*/
    public  Vector getTable(String table){
        Vector tableData = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+table);
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            while(rs.next()){
                Vector tableRow = new Vector();
                for (int i=1; i <= maxCols; i++){
                    tableRow.addElement(rs.getObject(i)); //getString(i)
                }
                tableData.addElement(tableRow);
            }
            rs.close();
            stmt.close();
            return tableData;
            }
            catch (Exception e){
                System.out.println(e);
                return tableData;
            }
    }

    public Vector<String> getPartOfTable(String table, String column, String cond){
        String statement="SELECT * FROM "+table+ " WHERE "+column+" = "+cond;
        return getDataByStatement(statement, true);
    }

    public Vector getPartOfTable(String table, String column, Object[] cond){
        String s1 ="SELECT * FROM "+table+ " WHERE "+column+" = "+cond[0].toString();
        String s2="";
        for (int j=1; j < cond.length; j++){
            s2=s2+" OR "+column+" = "+cond[j].toString();
        }
        String statement=s1+s2;
        return getDataByStatement(statement, true);
    }

    public Vector getPartOfTable(String statement){
        return getDataByStatement(statement, true);
    }

    public Vector getColOfTable(String table, String column){
        String statement="Select "+column+" FROM "+table;
        return getDataByStatement(statement, false);
    }

     /* returns a list of different values in a database table columnas as a vector*/
     public Vector getDiffObs(String table, String column){
        Vector liste= new Vector();
        String newfield;
        try {
                dbcon.setReadOnly(true);
                dbcon.setAutoCommit(true);
                Statement stmt = dbcon.createStatement();
                String sqlanw="SELECT DISTINCT "+ column +" FROM "+table+" ORDER BY "+ column +" ASC";
                System.out.println(sqlanw);
                ResultSet rs = stmt.executeQuery(sqlanw); //

                while(rs.next()){
                    newfield=rs.getString(column);// nur einmal Zugreifen auf ein feld!
                    liste.addElement(newfield);
                }
                rs.close();
                stmt.close();
                return liste;
        }
        catch (Exception e){
            System.out.println(e);
            return liste;
        }
    }

     /** returns the result of the given statement as a vector of vectors
      * or a vector of objects
     */
     public  Vector getDataByStatement(String statement, boolean forceVector){
        Vector tableData = new Vector();
        int maxCols=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            ResultSetMetaData meta = rs.getMetaData();
            maxCols= meta.getColumnCount();
            while(rs.next()){
                Vector tableRow = new Vector();
                if (maxCols >1 || forceVector){
                    for (int i=1; i <= maxCols; i++){
                        tableRow.addElement(rs.getObject(i)); //getString(i)
                    }
                    tableData.addElement(tableRow);
                }
                else{
                    tableData.addElement(rs.getObject(1));
                }
            }
            rs.close();
            stmt.close();
            return tableData;
            }
            catch (Exception e){
                System.out.println(e);
                return tableData;
            }
    }


    private String loadStatementFromFile(String file){
        String statement="";
        String s="";
        boolean hasline=true;
        try{
            BufferedReader in= new BufferedReader(new InputStreamReader( new FileInputStream(file)));
            while(hasline){
                s=in.readLine();
                if(s!=null)statement=statement+" "+s;
                else hasline=false;
            }
            in.close();
        }
        catch (Exception e){
            System.out.println("AbstractQueries: "+e);
        }
        return statement;
    }

    public Vector getDataByStatementFile(String file, boolean forceVector){
        String statement=loadStatementFromFile(file);
        System.out.println(statement);
        return getDataByStatement(statement, forceVector);
    }

     /** 1. makes a hashtable from a jdbc-connection
     *  db= Databasepath, user= username, pw=password
     *  table = table with the 2 informations: ids and values
     *  idfield, valuefields the fields that contain the ids and values
     *  all of String
     */
    public Hashtable makeHashfromDB(String table, String idfield, String valuefield){
        Hashtable ht = new Hashtable();
        int maxCols=0;
        int TableRows=0;
        try{
            dbcon.setReadOnly(true);
            dbcon.setAutoCommit(true);
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + idfield + ", [" + valuefield + "] AS valuefield FROM "+table);
            while(rs.next()){
               ht.put(rs.getInt(idfield), rs.getObject("valuefield"));// Id-/key-field must be of Integer/value of Double or String
            }
            rs.close();
            stmt.close();
            return ht;
        }
        catch (Exception e){
                System.out.println(e);
                return ht;
        }
   }

// simple math:

    public double calcMeanOfCol(String table, String column){
        double result=Double.NaN;
        String statement="SELECT Avg("+column+") FROM "+table;
        Vector v= getDataByStatement(statement, false);
        if(v.size()>0){
            if(v.get(0).getClass()==Double.class) result=(Double)v.get(0);
            if(v.get(0).getClass()==Integer.class) result=(Integer)v.get(0);
        }
        return result;
    }

     public double calcMaxOfCol(String table, String column){
        double result=Double.NaN;
        String statement="SELECT Max("+column+") FROM "+table;
        Vector v= getDataByStatement(statement, false);
        if(v.size()>0){
            if(v.get(0).getClass()==Double.class) result=(Double)v.get(0);
            if(v.get(0).getClass()==Integer.class) result=(Integer)v.get(0);
        }
        return result;
    }

     public double calcMinOfCol(String table, String column){
        double result=Double.NaN;
        String statement="SELECT Min("+column+") FROM "+table;
        Vector v= getDataByStatement(statement, false);
        if(v.size()>0){
            if(v.get(0).getClass()==Double.class) result=(Double)v.get(0);
            if(v.get(0).getClass()==Integer.class) result=(Integer)v.get(0);
        }
        return result;
    }

    public double calcVarOfCol(String table, String column){
        double result=Double.NaN;
        String statement="SELECT Var("+column+") FROM "+table;
        Vector v= getDataByStatement(statement, false);
        if(v.size()>0){
            if(v.get(0).getClass()==Double.class) result=(Double)v.get(0);
            if(v.get(0).getClass()==Integer.class) result=(Integer)v.get(0);
        }
        return result;
    }




    //without sql
     /* private double calcMeanOfColumn(String table, String column){
        double result =0;
        double sum=0;
        Vector v= new Vector();
        v= queries.getColOfTable(table, column);
        for(int j=0; j < v.size(); j++){
        try{
            if(v.get(j).getClass()==Double.class){sum=sum+(Double)v.get(j);}
            if(v.get(j).getClass()==Integer.class){sum=sum+(Integer)v.get(j);}
        }
        catch(Exception e){}
        }
        result= sum/v.size();
        return result;
    }*/

     /*private double calcMaxOfColumn(String table, String column){
        double merker=0;
        Vector v= new Vector();
        v= queries.getColOfTable(table, column);
        try{
            if(v.get(0).getClass()==Double.class){merker=(Double)v.get(0);}
            if(v.get(0).getClass()==Integer.class){merker=(Integer)v.get(0);}
        }
        catch(Exception e){}
        for(int j=1; j < v.size(); j++){
            try{
                if(v.get(j).getClass()==Double.class){if((Double)v.get(j)>merker)   merker=(Double)v.get(j);}
                if(v.get(j).getClass()==Integer.class){if((Integer)v.get(j)>merker) merker=(Integer)v.get(j);}
            }
            catch(Exception e){}
        }
        return merker;
    }*/


// jTableModels:

     /* makes a tablemodel from two vectors to use with jTable*/
     public DefaultTableModel makeTableModelVector(Vector data, Vector header){
         DefaultTableModel model = new DefaultTableModel();
         model.setDataVector(data, header);
         return model;
     }

     /* makes a tablemodel from a given database table to use with jTable*/
     public DefaultTableModel makeTableModel(String table){
        Vector data=getTable(table);
        Vector header=getHeader(table);
        return makeTableModelVector(data, header);
     }

     /* Erstellt ein Modell für eine Werte-Liste eines Feldes*/
     public DefaultTableModel makeTableModelList(String table,String column){
         DefaultTableModel model = new DefaultTableModel();
         model.addColumn(column,getDiffObs(table, column));
         return model;
     }

     /* Erstellt ein Modell für eine Untermenge einer Tabelle aus den  einzelnen Vectoren*/
     public DefaultTableModel makeTableModelSelection(String table,String column, Object[] cond){
         DefaultTableModel model = new DefaultTableModel();
         model.setDataVector(getPartOfTable(table,column, cond),getHeader(table));
         return model;
     }

}
