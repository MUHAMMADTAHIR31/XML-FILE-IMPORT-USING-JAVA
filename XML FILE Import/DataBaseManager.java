import java.util.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseManager{
	
    private static Connection con;
    
    static{
		
        try{
            init(); 
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
	private static void init() throws Exception{
	
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        String path=new java.io.File("dbmsfile\\school.accdb").getAbsolutePath();
        String url="jdbc:ucanaccess://"+path;
        con=DriverManager.getConnection(url);
       
    } 
}	 