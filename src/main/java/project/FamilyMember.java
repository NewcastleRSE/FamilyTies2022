package project;

import java.io.Serializable;
import java.util.ArrayList;

 


 
public class FamilyMember implements Serializable {
	
	
	 @Override
	    public String toString() {
	        //displays a nice string representation of a person. the () means they have
	        
	        String s = null;
	        if (this.gender == Gender.MALE){
	            s = "♂ ";
	        }else if (this.gender == Gender.FEMALE){
	            s = "♀ ";
	        }
	        else
	        	s="";
	        s += this.getFirstName() + " " + this.getSurname(); 
	     
	        return s;
	    }

	 
	 // Adding the personal details 
	public FamilyMember(String firstName, String lastName ,Gender gender,String Birthday, String Death, String Profession, String PlaceOfLiving, String MentalHealth, String Bio) {
        this.setFirstName(firstName);
        this.setSurname(lastName);
        this.setGender(gender);
        this.setDOB(Birthday) ;
        this.setDOD(Death);
        this.setProfession(Profession);
        this.setPlaceOfLiving(PlaceOfLiving);
        this.setMentalHealth(MentalHealth);
        this.setBio(Bio);
        
        this.mother = null;
        this.father = null;
        this.children = new ArrayList<FamilyMember>();
    }
	
	
	
	public FamilyMember() {
		
	}
	 
	private Integer NameId;  
	private String  FirstName;  
	private String Surname;  
	private Gender gender;
	private  String  DOB;
	private  String  DOD;
	private  String Profession;
	private  String PlaceOfLiving;
	private  String MentalHealth;
	private  String Bio;
	
  	 //NameRegex validates the name for not having symbols on the name
	private final String nameRegex = "^[\\p{L} .'-]+$";
	 // DateRegex validates the date including numeric day and month with slash "/"
	private final String dateRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
	
	 private FamilyMember mother;
	 private FamilyMember father;
	 private ArrayList<FamilyMember> children;  
	 
   // defining getter and setter methods  
    public Integer getNameId() {  
       return NameId;  
    }  
       
    public void setNameId(Integer id) {  
       this.NameId = id;  
    }  
       
    public String getFirstName() {  
       return FirstName;  
    }  
       
     
       
    /**
     * @param firstName the firstName to set
     */
    public final void setFirstName(String firstName) {
        if (firstName.trim().matches(nameRegex)) {
            this.FirstName = firstName.trim();
        }else{
            throw new IllegalArgumentException("Invalid First Name (only characters)");
        }
        
    }
    
    
    public String getSurname() {  
           return Surname;  
        }  
       
    
    /**
     * sets the last name and validates against the regex
     * @param lastName the lastName to set
     */
    public final void setSurname(String lastName) {
        if (lastName.trim().matches(nameRegex)) {
            this.Surname = lastName.trim();
        }else{
        	   throw new IllegalArgumentException("Invalid Surname Name");
        }
    }

    
      //  to set the get the setters of DOB
    
    public String getDOB() {  
        return DOB;  
     }  
    
    
    public final void setDOB(String Birthday) {
    	if (Birthday.trim().matches(dateRegex) |  Birthday.length()  ==  0 ) {
    		this.DOB = Birthday.trim();
        }else{
         
            throw new IllegalArgumentException("Invalid Birth day (format is dd/mm/yyyy");
        }
        
         
            
         
    }
 
 //  to set the get the setters of DOD
    
    public String getDOD() {  
        return DOD;  
     }  
    
    
    public final void setDOD(String Death) {
    	
    	if (Death.trim().matches(dateRegex) | Death.length()  ==  0 ) {
    		  this.DOD = Death.trim();
    	}else{
            
            throw new IllegalArgumentException("Invalid Death day (format is dd/mm/yyyy");
        }
    	
    		String yeardod    ;
    		String yeardob  ;
    		
    		yeardod = "0" ;
    		
    		if (  Death.length()  > 0) {
    	      	yeardod = Death.substring(6,10);
    	      	yeardob= this.DOB.substring(6,10);
    		 
    	      // check the year 
    		  if (    Integer.parseInt(yeardod) - Integer.parseInt(yeardob)  > 150 )
				{ 
    			this.DOD = "";
    			throw new IllegalArgumentException("Invalid Death Year. Greater than 150 years!!!!"); 
    			}
			  	else
    	     	{ 		
    		      this.DOD = Death.trim();
          	   }
        
    		  if (    Integer.parseInt(yeardod) < Integer.parseInt(yeardob)    )
				{ 
  			this.DOD = "";
  			throw new IllegalArgumentException("Invalid Death Year. Less than Birth date!!!!"); 
  			}
			  	else
  	     	{ 		
  		      this.DOD = Death.trim();
        	   }		  
    		  
    	}
    }
    
//  to set the get the setters of Profession
    
    public String getProfession() {  
        return Profession;  
     }  
    
    
    public final void setProfession(String Profession) {
         
            this.Profession = Profession.trim();
         
    }
    
//  to set the get the setters of PlaceOfLiving
    
    public String getPlaceOfLiving() {  
        return PlaceOfLiving;  
     }  
    
    
    public final void setPlaceOfLiving(String PlaceOfLiving) {
         
            this.PlaceOfLiving = PlaceOfLiving.trim();
         
    }
    
//  to set the get the setters of MentalHealth
    
    public String getMentalHealth() {  
        return MentalHealth;  
     }  
    
    
    public final void setMentalHealth(String MentalHealth) {
         
            this.MentalHealth = MentalHealth.trim();
         
    }
    
//  to set the get the setters of Bio
    
    public String getBio() {  
        return Bio;  
     }  
    
    
    public final void setBio(String Bio) {
         
            this.Bio = Bio.trim();
         
    }
    
    
    
    
       
    /**
     * Attribute types used to check if a family member has any of these attributes
     */
    public enum Attribute {
        FATHER,
        MOTHER,
        CHILDREN,
       PARENTS;
    }

    /**
     * Relative types used to add relatives to a family member 
     */
    public enum RelativeType {
        FATHER,
        MOTHER,
        CHILD,
        STEPSIBLING,
        HALFSIBLING,
       
   //     SPOUSE;
    } 

    /**
     * Gender types to ensure only two genders
     */
    public enum Gender {
        MALE,
        FEMALE;
    }

    
 
    
    
    
    /**
     * @return the mother
     */
    public FamilyMember getMother() {
        return mother;
    }
    
    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public final void setGender(Gender gender) {
        this.gender = gender;
    }

    
    /**
     * sets the mother and makes sure it is a female. it also adds the current m
     * member as a child to the mother 
     * A member can only have one mother
     * @param mother the mother to set
     */
    public void setMother(FamilyMember mother) {
        if (!this.has(Attribute.MOTHER)) {
        
                this.mother = mother;
           
        }else{
            throw new IllegalArgumentException("Mother already added");
        }
        
    }

    
    
    /**
     * checks if the member has a specific type of attribute
     * @param type the attribute type to check
     * @return true if the conditions are met 
     */
    public boolean has(FamilyMember.Attribute type){
        switch(type){
            case FATHER:
                return this.getFather() != null;
                 
            case CHILDREN:
                return !this.getChildren().isEmpty();
            case MOTHER:
                return this.getMother() != null;
            case PARENTS:
                return this.has(Attribute.FATHER) || this.has(Attribute.MOTHER);
                 
        }
        return false;
    }
   
    /**
     * @return the father
     */
    public FamilyMember getFather() {
        return father;
    }

    /**
     * sets the father and makes sure it is a male. it also adds the current 
     * member as a child to the father 
     * A member can only have one father
     * @param father the father to set
     */
    public void setFather(FamilyMember father) {
       if (!this.has(Attribute.FATHER)) {
    	
        	
                this.father = father;
           } 
                 
        
    } 
    
    /**
     * adds a child to the family member. Consequently adding the spouse and the current 
     * family member as the parents if they exist
     * @param child the child to add to set of children
     */
    public void addChild(FamilyMember child) {
        //father
        if (this.gender == Gender.MALE) {
            //if the child doesnt have a father set it
            if (!child.has(Attribute.FATHER)) {
                child.setFather(this);
            }
     
        //mother
        }else if (this.gender == Gender.FEMALE){
            //if the child doesnt have a mother set it
            if (!child.has(Attribute.MOTHER)) {
                child.setMother(this);
            }
           
        }
        //make sure no dupicate children objects 
        if(!this.getChildren().contains(child)){
            this.getChildren().add(child);
        }
        
    }

    /**
     * retuns the number of children for this member 
     * @return
     */
    
    public int numChildren(){
        return this.getChildren().size();
    }
    
    /**
     * @return the children
     */
    public ArrayList<FamilyMember> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(ArrayList<FamilyMember> children) {
        this.children = children;
    }
    
    
    /**
     * adds a relative based on the specified type variable. Basically a convenience method
     * @param type the type of the added member
     * @param member the member to add
     */
    public void addRelative(FamilyMember.RelativeType type, FamilyMember member){
        switch(type){
            case FATHER:
                this.setFather(member);
                return;
            case CHILD:
                this.addChild(member);
               return;
            case MOTHER:
                this.setMother(member);
                return;
            case STEPSIBLING:
                this.addChild(member);
               return;
            case HALFSIBLING:
                this.addChild(member);
               return;
        
        }
    }
    
    

}
