package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class FamilyDB {
	
	 public FamilyDB() {
	              
	    }
	
 public	void createDB() throws ClassNotFoundException{
		
		  // load the sqlite-JDBC driver using the current class loader
	      Class.forName("org.sqlite.JDBC");

	      Connection connection = null;
	      try
	      {
	         // create a database connection
	         connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

	         Statement statement = connection.createStatement();
	         statement.setQueryTimeout(30);  // set timeout to 30 sec.


	       statement.executeUpdate("DROP TABLE IF EXISTS NameTable");
	       statement.executeUpdate("CREATE TABLE NameTable (id INTEGER PRIMARY KEY AUTOINCREMENT, FirstName STRING,SurName STRING , DOB STRING, DOD STRING, Profession STRING ,fuid INTEGER, PlaceOfLiving String, MentalHealth String, Bio String)");  // gender,DOB,DOD,adrees 

	       
	       statement.executeUpdate("DROP TABLE IF EXISTS PersonTable");
	       statement.executeUpdate("CREATE TABLE PersonTable (Personid INTEGER PRIMARY KEY  , sex INT,Parentid  INT ,Familyid INTEGER,fuid INTEGER)");
	         
	       statement.executeUpdate("DROP TABLE IF EXISTS FamilyTable");
	       statement.executeUpdate("CREATE TABLE FamilyTable (Familyid INTEGER PRIMARY KEY AUTOINCREMENT, Fatherid INTEGER,Motherid  INT,Childid INTEGER ,fuid INTEGER)");
	     
	       statement.executeUpdate("DROP TABLE IF EXISTS ChildTable");
	       statement.executeUpdate("CREATE TABLE ChildTable (id INTEGER PRIMARY KEY AUTOINCREMENT,Childid INTEGER ,Familyid INTEGER, RelParentid INTEGER ,fuid INTEGER)");
	  
	       statement.executeUpdate("DROP TABLE IF EXISTS FileTable");
	       statement.executeUpdate("CREATE TABLE FileTable (fuid INTEGER PRIMARY KEY AUTOINCREMENT, path STRING , Firstname STRING,SurName  STRING)");
	  
	       
	       
	       
	       
	       
          }

     catch(SQLException e){  System.err.println(e.getMessage()); }       
      finally {         
            try {
                  if(connection != null)
                     connection.close();
                  }
            catch(SQLException e) {  // Use SQLException class instead.          
               System.err.println(e); 
             }
      }
  }

}
