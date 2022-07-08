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
    
    public void InsertMember(FamilyMember newMember,String TypeofRelative) {
         
    	Integer  MaxID=0 ;

        Connection connection = null;
        try
        {
           // create a database connection
           connection = DriverManager.getConnection("jdbc:sqlite:Family.db");

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);  // set timeout to 30 sec.

 
           statement.executeUpdate("INSERT INTO NameTable( FirstName  ,SurName,DOB, DOD, Profession ,PlaceOfLiving,MentalHealth,Bio,RelType) values(' "+newMember.getFirstName()  +"', '"+ newMember.getSurname()+"', '"+ newMember.getDOB()+"', '"+ newMember.getDOD()+"', '"+ newMember.getProfession()+"', '"+ newMember.getPlaceOfLiving()+"', '"+ newMember.getMentalHealth()+"', '"+ newMember.getBio()+ "', '"+ TypeofRelative +"'  )");    
           
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

         //       updcmd = "UPDATE  NameTable SET  DOB   =  '" + ExistMember.getDOB() + "' , DOD = '" + ExistMember.getDOD() + "' , Profession = '" + ExistMember.getProfession() +"'    where TRIM(FirstName) = '" + ExistMember.getFirstName()+ "'" ;
           
         //   updcmd = "UPDATE  NameTable SET  DOB   =  '" + ExistMember.getDOB() + "' , DOD = '" + ExistMember.getDOD() + "' , Profession = '" + ExistMember.getProfession() +"',  PlaceOfLiving = '" + ExistMember.getPlaceOfLiving() +"' ,  MentalHealth = '" + ExistMember.getMentalHealth() +"' , Bio = '" + ExistMember.getBio() +"'  where TRIM(FirstName) = '" + ExistMember.getFirstName()+ "'" ;
         
           updcmd = "UPDATE  NameTable SET  DOB   =  '" + ExistMember.getDOB() + "' , DOD = '" + ExistMember.getDOD() + "' , Profession = '" + ExistMember.getProfession() +"',  PlaceOfLiving = '" + ExistMember.getPlaceOfLiving() +"' ,  MentalHealth = '" + ExistMember.getMentalHealth() +"' , Bio = '" + ExistMember.getBio() +"'  where TRIM(FirstName) = '" + ExistMember.getFirstName()+ "'" ;
              
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

           
     // cmd ="SELECT MAX(id) maxID from NameTable";
   
           cmd = "select n.Firstname , n.SurName ,ifnull(n.DOB,'') DOB,ifnull(n.DOD,'') DOD,  ifnull(n2.Firstname ,'') father  , ifnull(n2.SurName,'') fathersurname, ifnull(n2.DOB,'') DOBfather,ifnull(n2.DOD,'') DODfather,  ifnull(n3.Firstname,'')  mother , ifnull(n3.SurName ,'') mothername ,   ifnull(n3.DOB,'') DOBmother,ifnull(n3.DOD,'') DODmother,  ifnull(n4.Firstname,' ') son , ifnull(n4.SurName, ' ')  sonsurname , ifnull(n4.DOB,'') DOBson,ifnull(n4.DOD,'') DODson ,n4.RelType	from NameTable n inner join   FamilyTable f  on f.Childid = n.id    left join   NameTable n2 on n2.id = f.Fatherid	  	 left join   NameTable n3 on n3.id = f.Motherid   left join ChildTable c on c.Familyid = f.Familyid   left join   NameTable n4 on n4.id = c.Childid";
     
    ResultSet resultSet = statement.executeQuery(cmd);
    while(resultSet.next())
    {
     //  iterate & read the result set
 	 //  RelativeID = resultSet.getInt("maxID");
    	 j=0;
    	 System.out.println(  resultSet.getString("Firstname") +     System.getProperty("line.separator") + resultSet.getString("Surname") +","+  resultSet.getString("DOB")  +" has Parents (Father)"+ "\t"  +
    			         resultSet.getString("father") + " "  + resultSet.getString("fathersurname")    + " And Mother "  +  resultSet.getString("mother") + " "  + resultSet.getString("mothername") +   "\t"  +
    			 " Parents  children are :"+  resultSet.getString("son") + " "  + resultSet.getString("sonsurname") 
    			 );
    	 
    	 txtline = resultSet.getString("Firstname").toString()  + " "  + resultSet.getString("Surname")+ " " +   "("+  resultSet.getString("DOB")  + "-"+  resultSet.getString("DOD")  + ")";  
    	 System.out.println( txtline);
    	 data[i][j] =  txtline ;  	 j++;
      /// cell for father 
    	 txtline ="";
           txtline = resultSet.getString("father").toString()  + " "  + resultSet.getString("fathersurname")+  " " + "("+  resultSet.getString("DOBfather")  + "-"+  resultSet.getString("DODfather")  + ")";  
    	  data[i][j] =  txtline ;  	 j++;
    	  //resultSet.getString("father").toString() + " "  + resultSet.getString("fathersurname") + ",( "  + resultSet.getString("DOBfather")  + "-"+  resultSet.getString("DODfather") + ")" ;  	 j++;
    	  
    	  
    	  txtline ="";
    	  txtline = resultSet.getString("mother").toString()  + " "  + resultSet.getString("mothername") +" " + "("+  resultSet.getString("DOBmother")  + "-"+  resultSet.getString("DODmother")  + ")";  
    	  data[i][j] =  txtline ;  	 j++;
    	
    	  
    	//  data[i][j] =  resultSet.getString("mother").toString() + " "  + resultSet.getString("mothername") ; j++;
    	  
    	  txtline ="";
    	//  txtline = resultSet.getString("son").toString()  + " "  + resultSet.getString("sonsurname")+ " " +   "("+  resultSet.getString("DOBson")  + "-"+  resultSet.getString("DODson")  + ")";  
    	  txtline = resultSet.getString("son").toString()  + " "  + resultSet.getString("sonsurname")+ " " +   "("+  resultSet.getString("DOBson")  + "-"+  resultSet.getString("DODson") + ")"    + resultSet.getString("Reltype")  ;  
    	    
    	  data[i][j] =  txtline ;  	 j++;
    	
    	  
         // data[i][j] =  resultSet.getString("son").toString() + " "  + resultSet.getString("sonsurname") ; j++;
    	
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
       
        jT.setBounds(300, 40, 1200, 800);
 
        jT.setGridColor( Color.YELLOW); 
      //  jT.setGridColor(4, Color.RED);
     // set a Background color to the Jtable
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

          
           // to get the last pesron that we have and is th erelative that we have just added
           ResultSet resultSet = statement.executeQuery("SELECT MAX(id) maxID from NameTable");
           while(resultSet.next())
           {
              // iterate & read the result set
        	   RelativeID = resultSet.getInt("maxID");
           }
           
           
           // we get the member id of the person that we act using firstname/surnmae/dob
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
        		   
        		// nneed to consider also mother id in the query
        		//   ResultSet resultSet2mc = statement.executeQuery("SELECT   Familyid  childfamily  from FamilyTable  where (fatherid =  '" +Memberid +"' or montherid =  '" + Memberid +"') ");
        	          
        		   while(resultSet2mc.next())
                   {
                      // iterate & read the result set
                	    Childfamilyid = resultSet2mc.getInt("childfamily");
                   }
                   
       		        		   
        		   cmd = "UPDATE ChildTable  SET Familyid= '"+Childfamilyid +"'  where trim(RelParentid) = '" +Memberid +"'";
            	   statement.executeUpdate(cmd) ; 
            	   
            	   
        	   }
        	 }          
           else // there are child  already  and records in the familyTable
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
        		   // to check if is another father --step father
        		   cmd = "SELECT  Familyid    from FamilyTable where trim(Childid) =  '" +  Memberid    + "'  and trim(fatherid) is  not null  "  ;
        		   ResultSet resultSet2father = statement.executeQuery(cmd);
                   
                   while(resultSet2father.next())
                   {
                      // iterate & read the result set
                	   familyidfather = resultSet2father.getInt("Familyid");
                   }
        		   
                   
                   if  (familyidfather == 0) {   // insert  new relation of father - child in a family table   {familyid,
        		     	   
             	  // cmd = "UPDATE FamilyTable  SET  Fatherid  =  '" + RelativeID +"'  where trim(Childid) = '" +Memberid +"'";
            	 //  statement.executeUpdate(cmd) ; 
            	   
            	   
               /// 	   cmd = "UPDATE PersonTable  SET  sex =0,   Parentid  =  '" + RelativeID +"' ,Familyid= '" +  familyid    + "'  where trim(Personid) = '" +Memberid +"'";
            	//   statement.executeUpdate(cmd) ; 
            	   
            	   
            	   statement.executeUpdate("INSERT INTO FamilyTable (Fatherid  ,Motherid   ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
            	   
            	   
            	      }
                   else {
                	   // need to insert a   father  --  child Relation  
                        statement.executeUpdate("INSERT INTO FamilyTable ( Fatherid  ,Motherid   ,Childid) values(' "+RelativeID +"','"+ 0 +"','"+ Memberid +"')");  
                     
                                    
                    //   cmd = "UPDATE PersonTable  SET  sex =0, Familyid ='"+maxfamilyid +"'+1 , Parentid  =  '" + RelativeID +"'  where trim(Personid) = '" +Memberid +"'";
                	//   statement.executeUpdate(cmd) ; 
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