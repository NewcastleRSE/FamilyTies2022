package project;


import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.attribute.IntegerSyntax;






//Packages to import
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;
 
/*
 * This module handle the business logic 
 *  It include logic for insert new member, add relative , present table form 
 * Author Pikrides filippos
 */

public class FamilyController {

	
	private FamilyDB  modelDB = new FamilyDB();  
	private FamilyModel  familyModel = new FamilyModel();  
	// method to update view   
    public void createDB() {                  
          
    	try {
    		modelDB.createDB() ;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
    	
     }   
    
    // Insert Member to database
    public void InsertMember(FamilyMember newMember,String TypeofRelative) {
         
    	Integer  MaxID=0 ;

        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.

           // create the insert command
           statement.executeUpdate("INSERT INTO NameTable( FirstName  ,SurName,DOB, DOD, Profession ,PlaceOfLiving,MentalHealth,Bio,RelType) values(' "+newMember.getFirstName()  +"', '"+ newMember.getSurname()+"', '"+ newMember.getDOB()+"', '"+ newMember.getDOD()+"', '"+ newMember.getProfession()+"', '"+ newMember.getPlaceOfLiving()+"', '"+ newMember.getMentalHealth()+"', '"+ newMember.getBio()+ "', '"+ TypeofRelative +"'  )");    
           
           ResultSet resultSet = statement.executeQuery("SELECT MAX(id) maxID from NameTable");
            // get the maximum numbers of members to update the person table 
           
           while(resultSet.next())
           {
              // iterate & read the result set
             	   MaxID = resultSet.getInt("maxID");
           }
           if( MaxID == 1 ) {
                  statement.executeUpdate("INSERT INTO PersonTable( Personid  ,Familyid,Parentid) values(' "+MaxID  +"','"+ 0 +"','"+ 0 +"')");   
          }else {          
          	    statement.executeUpdate("INSERT INTO PersonTable( Personid  ,sex,Parentid,Familyid) values(' "+MaxID  +"','"+ 0 +"','"+ 0 +"','"+ 0 +"')"); 
          }
          }  
    	 // in case of error or exception a message is displayed
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
    
    // This function Update member fileds. It update the Nametable from the database Family.DB
    
    public void UpdateMember(FamilyMember ExistMember) {
        
    	Integer  MaxID=0 ;
        String updcmd ="";
    	
    	
        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.

         // create the update command     
           updcmd = "UPDATE  NameTable SET  DOB   =  '" + ExistMember.getDOB() + "' , DOD = '" + ExistMember.getDOD() + "' , Profession = '" + ExistMember.getProfession() +"',  PlaceOfLiving = '" + ExistMember.getPlaceOfLiving() +"' ,  MentalHealth = '" + ExistMember.getMentalHealth() +"' , Bio = '" + ExistMember.getBio() +"'  where TRIM(FirstName) = '" + ExistMember.getFirstName()+ "'" ;
           // excute the update stmt    
           statement.executeUpdate( updcmd ) ;
          
          }  
    	 // in case of error a message is displayed 
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
    
    
    // Creating  a table form report of geneological data of a family
    // It shows the child with his parents and other  children if exist.
    
    public void Displayreport(){
    
    	String NAME;
    	String PARENTS;
    	String CHILDREN;
    	Integer i=0,j=0;
    	String txtline;
    	
    	String cmd ;
    	String[][] data = new String [10][4];
   	 
        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.

           
          // the select stmt using the join command to connect diferent table with the same field 
   
           cmd = "select n.Firstname , n.SurName ,ifnull(n.DOB,'') DOB,ifnull(n.DOD,'') DOD,  ifnull(n2.Firstname ,'') father  , ifnull(n2.SurName,'') fathersurname, ifnull(n2.DOB,'') DOBfather,ifnull(n2.DOD,'') DODfather,  ifnull(n3.Firstname,'')  mother , ifnull(n3.SurName ,'') mothername ,   ifnull(n3.DOB,'') DOBmother,ifnull(n3.DOD,'') DODmother,  ifnull(n4.Firstname,' ') son , ifnull(n4.SurName, ' ')  sonsurname , ifnull(n4.DOB,'') DOBson,ifnull(n4.DOD,'') DODson ,n4.RelType	from NameTable n inner join   FamilyTable f  on f.Childid = n.id    left join   NameTable n2 on n2.id = f.Fatherid	  	 left join   NameTable n3 on n3.id = f.Motherid   left join ChildTable c on c.Familyid = f.Familyid   left join   NameTable n4 on n4.id = c.Childid";
     
    ResultSet resultSet = statement.executeQuery(cmd);
    while(resultSet.next())
    {
     // 
    	 j=0;
    	 System.out.println(  resultSet.getString("Firstname") +     System.getProperty("line.separator") + resultSet.getString("Surname") +","+  resultSet.getString("DOB")  +" has Parents (Father)"+ "\t"  +
    			         resultSet.getString("father") + " "  + resultSet.getString("fathersurname")    + " And Mother "  +  resultSet.getString("mother") + " "  + resultSet.getString("mothername") +   "\t"  +
    			 " Parents  children are :"+  resultSet.getString("son") + " "  + resultSet.getString("sonsurname") 
    			 );
    	 
    	 txtline = resultSet.getString("Firstname").toString()  + " "  + resultSet.getString("Surname")+ " " +   "("+  resultSet.getString("DOB")  + "-"+  resultSet.getString("DOD")  + ")";  
    	 System.out.println( txtline);
    	 data[i][j] =  txtline ;  	 j++;
         // cell info for father 
    	 txtline ="";
           txtline = resultSet.getString("father").toString()  + " "  + resultSet.getString("fathersurname")+  " " + "("+  resultSet.getString("DOBfather")  + "-"+  resultSet.getString("DODfather")  + ")";  
    	  data[i][j] =  txtline ;  	 j++;
    	   
    	   // cell info for mother 
    	  txtline ="";
    	  txtline = resultSet.getString("mother").toString()  + " "  + resultSet.getString("mothername") +" " + "("+  resultSet.getString("DOBmother")  + "-"+  resultSet.getString("DODmother")  + ")";  
    	  data[i][j] =  txtline ;  	 j++;
    	
    	   // cell info for children	  
    	  txtline ="";
    	  txtline = resultSet.getString("son").toString()  + " "  + resultSet.getString("sonsurname")+ " " +   "("+  resultSet.getString("DOBson")  + "-"+  resultSet.getString("DODson") + ")"    + resultSet.getString("Reltype")  ;  
    	    
    	  data[i][j] =  txtline ;  	 j++;
    	
    	    	
    	i++;
    }
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
    
        
        // frame
        JFrame f;
        // Table
        JTable jT;
        TableColumn col;
        
                
        // Frame initialization
        f = new JFrame();
 
        // Frame Title
        f.setTitle("FamilyTies  ");
         // Column Names
        String[] columnNames = { "Person", "Father", "Mother","Other Children" };
 
        // Initializing the JTable
        jT = new JTable(data, columnNames);
        jT.setBounds(300, 40, 1200, 800);
        jT.setGridColor( Color.YELLOW); 
        jT.setBackground(Color.decode("#058dc7"));
        // set Font To table
        jT.setFont(new Font("", 1, 16));
        
        // set height to the table rows
        jT.setRowHeight(50);
        
        // set color to the JTable Font
        jT.setForeground(Color.white);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(jT);
        f.add(sp);
        // Frame Size
        f.setSize(1200, 800);
        // Frame Visible = true
        f.setVisible(true);
        
        
    }
    		 
    // Function to add a relative member.
    // this main fuction aprt from the insertion of Nametable , it also insert record in the familytable (keeps the relations)
    public void InsertRelativeMember(FamilyMember   Member ,FamilyMember NewMember ,String typeofRelative) {
    	Integer  RelativeID=0 ;
        Integer Memberid =0;
    	Integer familyid =0; 
    	Integer maxfamilyid =0;
    	Integer Childfamilyid=0;
    	Integer familyidfather =0;
    	String cmd ;
    	
    	 
        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.

          
           // to get the last person that we have and is the relative that we have just added
           ResultSet resultSet = statement.executeQuery("SELECT MAX(id) maxID from NameTable");
           while(resultSet.next())
           {
              // iterate & read the result set
        	   RelativeID = resultSet.getInt("maxID");
           }
           
           
           // we get the unique  member id of the person that we act using firstname/surnmae/dob
           ResultSet resultSet1 = statement.executeQuery("SELECT  id    from NameTable where trim(FirstName) =  '" +  Member.getFirstName()  +"' and  trim(Surname) = '" +  Member.getSurname() +"' and  trim(DOB) = '" +  Member.getDOB() + "' ");
           
           while(resultSet1.next())
           {
              // iterate & read the result set
        	   Memberid = resultSet1.getInt("id");
           }
           
           
          // we get the maximum number of family table
          ResultSet resultSet2m = statement.executeQuery("SELECT  max(Familyid) maxfamily  from FamilyTable  ");
           
           while(resultSet2m.next())
           {
              // iterate & read the result set
        	   maxfamilyid = resultSet2m.getInt("maxfamily");
           }
           
           
           // we get the family of the person we act and check if it has child. 
           ResultSet resultSet2 = statement.executeQuery("SELECT  Familyid    from FamilyTable where trim(Childid) =  '" +  Memberid    + "' ");
           
           while(resultSet2.next())
           {
              // iterate & read the result set
        	   familyid = resultSet2.getInt("Familyid");
           }
           
           // it means that no family with child exist 
           if(familyid ==0) {
        	   if (typeofRelative == "FATHER") {   // insert a father in family for the first time 
                       statement.executeUpdate("INSERT INTO FamilyTable ( Fatherid  ,Motherid   ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
                     
                       
                       
                       cmd = "UPDATE PersonTable  SET  sex =0, Familyid ='"+maxfamilyid +"'+1 , Parentid  =  '" + RelativeID +"'  where trim(Personid) = '" +Memberid +"'";
                	   statement.executeUpdate(cmd) ; 
                
        	   } 
        	   else if (typeofRelative == "MOTHER") {   // insert a mother  in family for the first time 
        		   statement.executeUpdate("INSERT INTO FamilyTable ( Motherid,Fatherid     ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
        		 
        		   cmd = "UPDATE PersonTable  SET  sex =1, Familyid= '"+maxfamilyid +"' +1, Parentid  =  '" + RelativeID +"'  where trim(Personid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
        	   }
        	   else if (typeofRelative == "CHILD"  | typeofRelative == "STEPSIBLING" | typeofRelative == "HALFSIBLING" ) {
        		   
        		   statement.executeUpdate("INSERT INTO ChildTable (  Childid,Familyid,RelParentid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
               
        		       		   
        		 ResultSet resultSet2mc = statement.executeQuery("SELECT   Familyid  childfamily  from FamilyTable  where fatherid =  '" +Memberid +"'");
        		   
        		// need to consider also mother id in the query
        	       	          
        		   while(resultSet2mc.next())
                   {
                      // iterate & read the result set
                	    Childfamilyid = resultSet2mc.getInt("childfamily");
                   }
                   
       		        		   
        		   cmd = "UPDATE ChildTable  SET Familyid= '"+Childfamilyid +"'  where trim(RelParentid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
            	   
            	   
        	   }
        	 }          
           else // there is child  already  and records in the familyTable
           {
        	     
        	   if (typeofRelative == "MOTHER") 
        	   {
        	   cmd = "UPDATE FamilyTable  SET  Motherid  =  '" + RelativeID +"'  where trim(Childid) = '" +Memberid +"'";
        	   statement.executeUpdate(cmd) ; 
        	    
        	   cmd = "UPDATE PersonTable  SET  sex =1,   Parentid  =  '" + RelativeID +"' ,Familyid= '" +  familyid    + "'  where trim(Personid) = '" +Memberid +"'";
        	   statement.executeUpdate(cmd) ; 
        	   
        	   
        	   }
        	   else  if (typeofRelative == "FATHER") 
            	   {
        		   // to check if is another father --step father
        		   cmd = "SELECT  Familyid    from FamilyTable where trim(Childid) =  '" +  Memberid    + "'  and trim(fatherid) is  not null  "  ;
        		   ResultSet resultSet2father = statement.executeQuery(cmd);
                   
                   while(resultSet2father.next())
                   {
                      // iterate & read the result set
                	   familyidfather = resultSet2father.getInt("Familyid");
                   }
        		   
                   
                   if  (familyidfather == 0) {   // insert  new relation of father - child in a family table   {familyid,
              	   statement.executeUpdate("INSERT INTO FamilyTable (Fatherid  ,Motherid   ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
                	   
            	      }
                   else {
                	   // need to insert a   father  --  child Relation  
                        statement.executeUpdate("INSERT INTO FamilyTable ( Fatherid  ,Motherid   ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
                      
                   }
                	   
                   
            	   } 
                else if (typeofRelative == "CHILD" | typeofRelative == "STEPSIBLING" | typeofRelative == "HALFSIBLING") {
        		   
        		   statement.executeUpdate("INSERT INTO ChildTable (  Childid,Familyid,RelParentid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
        		        
        		   // to get the family id in order toinsert it into th etable ChildTable
         		   ResultSet resultSet2mc = statement.executeQuery("SELECT   Familyid  childfamily  from FamilyTable  where fatherid =  '" +Memberid +"'");
                   
                   while(resultSet2mc.next())
                   {
                      // iterate & read the result set
                	    Childfamilyid = resultSet2mc.getInt("childfamily");
                   }
                   
        		      		   cmd = "UPDATE ChildTable  SET    Familyid= '"+Childfamilyid +"'  where trim(RelParentid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
        	   }
           }
           
     
          }  
    	  // catch and display any exceptions or errors 
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

   // this function saves a unique file ID for each family tree  
   // so 2 different families , will have different file Id. 
   // This is how we know which data to update during the importing of a  geneological file  (gd) 
    public void SaveFileMember( String fielnamepath,FamilyTree thisMember) {
        
    	Integer  FUID=0 ;
    	String cmd  ;
    	Integer fileid=0; 
    	
    	  Connection connection = null;
          try
          {
             // create a database connection
             connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

             Statement statement = connection.createStatement();
             statement.setQueryTimeout(30);  // set timeout to 30 sec.

             // get the file id regarding the filename 
             ResultSet resultSet2 = statement.executeQuery("SELECT  fuid    from FileTable where trim(path) =  '" +  fielnamepath    + "' ");
             
             while(resultSet2.next())
             {
                // set the value to the variable fileid
            	 fileid = resultSet2.getInt("fuid");
             }
             
             if (fileid == 0) {
                statement.executeUpdate("INSERT INTO FileTable( path,Firstname  ,SurName) values(' "+ fielnamepath  +"', '"+ thisMember.getRoot().getFirstName()+"', '"+ thisMember.getRoot().getSurname()+"')");   
             
                           
             ResultSet resultSet = statement.executeQuery("SELECT MAX(fuid) maxID from FileTable");
             while(resultSet.next())
             {
                // iterate & read the result set
          	   FUID = resultSet.getInt("maxID");
             }
             }
             else {
            	 FUID =fileid;       	 
             }
            
            // update the tables with the correct file id, if there are new insertions.
             
            cmd = "UPDATE FamilyTable  SET  fuid  =  '" + FUID +"'   where fuid is null ";
      	    statement.executeUpdate(cmd) ; 
             
      	    cmd = "UPDATE NameTable  SET  fuid  =  '" + FUID +"'   where fuid is null ";
    	    statement.executeUpdate(cmd) ; 
           
    	    cmd = "UPDATE ChildTable  SET  fuid  =  '" + FUID +"'   where fuid is null ";
    	    statement.executeUpdate(cmd) ; 
    	    
    	    cmd = "UPDATE PersonTable  SET  fuid  =  '" + FUID +"'   where fuid is null ";
    	    statement.executeUpdate(cmd) ; 
    	    
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