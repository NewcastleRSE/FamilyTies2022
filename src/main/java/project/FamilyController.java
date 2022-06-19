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
    
    public void InsertMember(FamilyMember newMember) {
         
    	Integer  MaxID=0 ;

        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.


      //     statement.executeUpdate("DROP TABLE IF EXISTS person");
      //     statement.executeUpdate("CREATE TABLE person (id INTEGER, name STRING)");

       //    int ids [] = {1,2,3,4,5};
      //     String names [] = {"Peter","Pallar","William","Paul","James Bond"};

       //    for(int i=0;i<ids.length;i++){
      //          statement.executeUpdate("INSERT INTO person values(' "+ids[i]+"', '"+names[i]+"')");   
           
           statement.executeUpdate("INSERT INTO NameTable( FirstName  ,SurName,DOB, DOD, Profession ,PlaceOfLiving,MentalHealth,Bio) values(' "+newMember.getFirstName()  +"', '"+ newMember.getSurname()+"', '"+ newMember.getDOB()+"', '"+ newMember.getDOD()+"', '"+ newMember.getProfession()+"', '"+ newMember.getPlaceOfLiving()+"', '"+ newMember.getMentalHealth()+"', '"+ newMember.getBio()+"'  )");   
           
           ResultSet resultSet = statement.executeQuery("SELECT MAX(id) maxID from NameTable");
           
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

         //  updcmd = "UPDATE  NameTable SET  DOB   =  '" + ExistMember.getDOB() + "'  DOD = '" + ExistMember.getDOD() +"'    where TRIM(FirstName) = '" + ExistMember.getFirstName()+ "'" ;
           
           updcmd = "UPDATE  NameTable SET  DOB   =  '" + ExistMember.getDOB() + "' , DOD = '" + ExistMember.getDOD() + "' , Profession = '" + ExistMember.getProfession() +"'    where TRIM(FirstName) = '" + ExistMember.getFirstName()+ "'" ;
           statement.executeUpdate( updcmd ) ;
          
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
    
    
    
    
    
    public void Displayreport(){
    
    	String NAME;
    	String PARENTS;
    	String CHILDREN;
    	Integer i=0,j=0;
    	
    	String cmd ;
    	String[][] data = new String [10][4];
   	 
        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.

           
     // cmd ="SELECT MAX(id) maxID from NameTable";
     cmd = "select n.Firstname , n.SurName ,ifnull(n.DOB,'') DOB,  ifnull(n2.Firstname ,'') father  ,  ifnull(n2.SurName,'') fathersurname,  ifnull(n3.Firstname,'')  mother , ifnull(n3.SurName ,'') mothername , ifnull(n4.Firstname,' ') son , ifnull(n4.SurName, ' ')  sonsurname 	from NameTable n inner join   FamilyTable f  on f.Childid = n.id    left join   NameTable n2 on n2.id = f.Fatherid	  	 left join   NameTable n3 on n3.id = f.Motherid   left join ChildTable c on c.Familyid = f.Familyid   left join   NameTable n4 on n4.id = c.Childid";
     
    ResultSet resultSet = statement.executeQuery(cmd);
    while(resultSet.next())
    {
     //  iterate & read the result set
 	 //  RelativeID = resultSet.getInt("maxID");
    	 j=0;
    	 System.out.println(  resultSet.getString("Firstname") + " "  + resultSet.getString("Surname") +","+  resultSet.getString("DOB")  +" has Parents (Father)"+ "\t"  +
    			         resultSet.getString("father") + " "  + resultSet.getString("fathersurname")    + " And Mother "  +  resultSet.getString("mother") + " "  + resultSet.getString("mothername") +   "\t"  +
    			 " Parents  children are :"+  resultSet.getString("son") + " "  + resultSet.getString("sonsurname") 
    			 );
    	 
    	  data[i][j] =  resultSet.getString("Firstname").toString()  + " "  + resultSet.getString("Surname")  +",(Birthday:)"+  resultSet.getString("DOB") ;  	 j++;
    	  data[i][j] =   resultSet.getString("father").toString() + " "  + resultSet.getString("fathersurname")  ;  	 j++;
    	  data[i][j] =  resultSet.getString("mother").toString() + " "  + resultSet.getString("mothername") ; j++;
          data[i][j] =  resultSet.getString("son").toString() + " "  + resultSet.getString("sonsurname") ; j++;
    	
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
        String[] columnNames = { "root", "Father", "Mother","Other Children" };
 
        // Initializing the JTable
        jT = new JTable(data, columnNames);
       
        jT.setBounds(30, 40, 500, 600);
 
        jT.setGridColor( Color.YELLOW); 
      //  jT.setGridColor(4, Color.RED);
     // set a Background color to the Jtable
        jT.setBackground(Color.decode("#058dc7"));
        // set Font To table
        jT.setFont(new Font("", 1, 12));
        
        // set height to the table rows
        jT.setRowHeight(50);
        
        // set color to the JTable Font
        jT.setForeground(Color.white);
        
        
        
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(jT);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);
        
        
    }
    		 
    		 
    public void InsertRelativeMember(FamilyMember   Member ,FamilyMember NewMember ,String typeofRelative) {
    	Integer  RelativeID=0 ;
        Integer Memberid =0;
    	Integer familyid =0; 
    	Integer maxfamilyid =0;
    	Integer Childfamilyid=0;
    	
    	String cmd ;
    	
    	 
        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.


      //     statement.executeUpdate("DROP TABLE IF EXISTS person");
      //     statement.executeUpdate("CREATE TABLE person (id INTEGER, name STRING)");
      //
       //    int ids [] = {1,2,3,4,5};
      //     String names [] = {"Peter","Pallar","William","Paul","James Bond"};
       //
       //    for(int i=0;i<ids.length;i++){
      //          statement.executeUpdate("INSERT INTO person values(' "+ids[i]+"', '"+names[i]+"')");   
           
          // statement.executeUpdate("INSERT INTO FamilyTable ( Familyid  ,SurName) values(' "+newMember.getFirstName()  +"', '"+ Memberid+"')");   
           
           ResultSet resultSet = statement.executeQuery("SELECT MAX(id) maxID from NameTable");
           while(resultSet.next())
           {
              // iterate & read the result set
        	   RelativeID = resultSet.getInt("maxID");
           }
           
               
           ResultSet resultSet1 = statement.executeQuery("SELECT  id    from NameTable where trim(FirstName) =  '" +  Member.getFirstName()  +"' and  trim(Surname) = '" +  Member.getSurname() + "' ");
           
           while(resultSet1.next())
           {
              // iterate & read the result set
        	   Memberid = resultSet1.getInt("id");
           }
           
           
           
          ResultSet resultSet2m = statement.executeQuery("SELECT  max(Familyid) maxfamily  from FamilyTable  ");
           
           while(resultSet2m.next())
           {
              // iterate & read the result set
        	   maxfamilyid = resultSet2m.getInt("maxfamily");
           }
           
           
           
           ResultSet resultSet2 = statement.executeQuery("SELECT  Familyid    from FamilyTable where trim(Childid) =  '" +  Memberid    + "' ");
           
           while(resultSet2.next())
           {
              // iterate & read the result set
        	   familyid = resultSet2.getInt("Familyid");
           }
           
           // if exist family for this child 
           if(familyid ==0) {
        	   if (typeofRelative == "FATHER") {
                       statement.executeUpdate("INSERT INTO FamilyTable ( Fatherid  ,Motherid   ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
                     
                       cmd = "UPDATE PersonTable  SET  sex =0, Familyid ='"+maxfamilyid +"'+1 , Parentid  =  '" + RelativeID +"'  where trim(Personid) = '" +Memberid +"'";
                	   statement.executeUpdate(cmd) ; 
                
        	   } 
        	   else if (typeofRelative == "MOTHER") {
        		   statement.executeUpdate("INSERT INTO FamilyTable ( Motherid,Fatherid     ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
        		 
        		   cmd = "UPDATE PersonTable  SET  sex =1, Familyid= '"+maxfamilyid +"' +1, Parentid  =  '" + RelativeID +"'  where trim(Personid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
        	   }
        	   else if (typeofRelative == "CHILD") {
        		   
        		   statement.executeUpdate("INSERT INTO ChildTable (  Childid,Familyid,RelParentid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
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
           else // there are parents already
           {
        	     
        	   if (typeofRelative == "MOTHER") 
        	   {
        	   cmd = "UPDATE FamilyTable  SET  Motherid  =  '" + RelativeID +"'  where trim(Childid) = '" +Memberid +"'";
        	   statement.executeUpdate(cmd) ; 
        	//   statement.executeUpdate("UPDATE FamilyTable  SET  Motherid  =  '+ RelativeID +'  where trim(Childid) = '+ Memberid +") ; 
        	   
        	   cmd = "UPDATE PersonTable  SET  sex =1,   Parentid  =  '" + RelativeID +"' ,Familyid= '" +  familyid    + "'  where trim(Personid) = '" +Memberid +"'";
        	   statement.executeUpdate(cmd) ; 
        	   
        	   
        	   }
        	   else  if (typeofRelative == "FATHER") 
            	   {
            	   cmd = "UPDATE FamilyTable  SET  Fatherid  =  '" + RelativeID +"'  where trim(Childid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
            	//   statement.executeUpdate("UPDATE FamilyTable  SET  Motherid  =  '+ RelativeID +'  where trim(Childid) = '+ Memberid +") ; 
            	   
            	   cmd = "UPDATE PersonTable  SET  sex =0,   Parentid  =  '" + RelativeID +"' ,Familyid= '" +  familyid    + "'  where trim(Personid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
            	   
            	   
            	   } 
                else if (typeofRelative == "CHILD") {
        		   
        		   statement.executeUpdate("INSERT INTO ChildTable (  Childid,Familyid,RelParentid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
        		        
        		   
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
           
      //     if( MaxID == 1 ) {
      //     statement.executeUpdate("INSERT INTO PersonTable( Personid  ,Familyid,ParentID) values(' "+MaxID  +"','"+ 0 +"','"+ 0 +"')");   
      //    }else {
      //          	  
     //   	  statement.executeUpdate("INSERT INTO PersonTable( Personid  ,Familyid,ParentID) values(' "+MaxID  +"','"+ 0 +"','"+ 0 +"')"); 
     //     }
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

 
             ResultSet resultSet2 = statement.executeQuery("SELECT  fuid    from FileTable where trim(path) =  '" +  fielnamepath    + "' ");
             
             while(resultSet2.next())
             {
                // iterate & read the result set
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