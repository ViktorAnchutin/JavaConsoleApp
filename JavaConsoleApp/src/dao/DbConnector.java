package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {

    private static final String DEFAULT_DB_FILE = "db.sqlite";

    private static final String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys = ON;";

    private static Connection con = null;

    private static String fileName = DEFAULT_DB_FILE;

    public static void setDbFile(String fileName)
    {
        try{
            if(con!=null) con.close();
        } catch (SQLException e)
        {
            System.out.println(e);
        }
        DbConnector.fileName = fileName;
        con = null; // reset connection
    }


    static Connection getConnection() throws SQLException {
        if(con!=null)
            return con;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            System.exit(-1);
        }

        con = DriverManager.getConnection( String.format("jdbc:sqlite:%s", fileName));

        //Enable foreign keys
        try
        {
            Statement stm = con.createStatement();
            stm.execute(ENABLE_FOREIGN_KEYS);
        } catch (SQLException e)
        {
            System.out.println(e);
        }

        return con;
    }

    private DbConnector() { }
}