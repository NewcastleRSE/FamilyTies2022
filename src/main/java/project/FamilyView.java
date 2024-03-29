package project;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;

import java.io.Serializable;

import project.FamilyMember.RelativeType;

/**
 * This program handle the view part of the MVC model
 * create the menus controls and gui screens 
 * 
 * @author Filippos Pikrides
 */
 
public class FamilyView {

	
	 public Integer IDPerson ;
	 
	public FamilyView() {
	       
	       currentFamilyTree = new FamilyTree();
	       currentFile = null;
	       tree = new JTree();
	       createGUI();
	   }
	   private JFrame mainFrame;
	   private JPanel controlPanel;
	   private JPanel infoPanel;
	   private final JLabel statusLabel = new JLabel("Program loaded");
	   private File currentFile;
	   private JTree tree;

	   private FamilyTree currentFamilyTree;
	   
	   
	   // Create windows from GUI
	   private void createGUI() {

	       mainFrame = new JFrame("Family Tree");
	       mainFrame.setSize(800, 900);
	       mainFrame.setLayout(new BorderLayout());
	       mainFrame.getContentPane().setBackground(Color.WHITE);
	       //do nothing on close to give the user the option to save their work before quitting
	       mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	       
	       //sets up the menu bar
	       initMenuBar();

	       //sets up the header section
	       initHeaderPanel();

	       //sets up the control section (main part where data is displayed)
	       initControlPanel();

	       //sets up the status bar
	       initStatusBar();

	       //displays the empty tree
	       displayTree(currentFamilyTree);

	       //check if user wants to continue using checkUserCOntinue function
	       mainFrame.addWindowListener(new WindowAdapter() {
	      //     @Override
	           public void windowClosing(WindowEvent windowEvent) {
	               if (checkUserContinue()) {
	                   System.exit(0);
	               }
	           }
	       });

	       mainFrame.setVisible(true);
	   }
	   
	   /**
	    * Initializes the header panel
	    */
	   private void initHeaderPanel() {
	       
	       JLabel headerLabel = new JLabel("Welcome to the Family Ties Application", JLabel.LEFT);
	       headerLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));

	       JButton open = new JButton("Load Tree");
	       open.addActionListener(new openAction());

	       JButton create = new JButton("Create New Tree");
	       create.addActionListener(new createTreeAction());

	       JButton saveTree = new JButton("Save Tree");
	       saveTree.addActionListener(new saveAction());
	       
	       JButton initdb = new JButton("Initialize Database");
	       initdb.addActionListener(new InitializeDatabaseAction());
	       

	       JButton Rptdb = new JButton("Report of FamilyTie");
	       Rptdb.addActionListener(new ReportAction());
	       
	       
	       JPanel headPanel = new JPanel();
	       headPanel.setLayout(new GridBagLayout());
	       headPanel.setOpaque(false);
	       headPanel.setBorder(new EmptyBorder(0,10,10,10));
	       
	       //using a grid layout to position each element
	       //grid bag constraints specifies where the element will go inside the grid
	       GridBagConstraints gbc = new GridBagConstraints();
	       gbc.gridx = 0;
	       gbc.gridy = 0;
	       gbc.fill = GridBagConstraints.BOTH;
	       gbc.weightx = 1.0;
	       gbc.weighty = 1.0;
	       headPanel.add(headerLabel, gbc);

	       //flow layout for the buttons (next to each other)
	       JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
	       container.setOpaque(false);
	       container.add(open);
	       container.add(saveTree);
	       container.add(create);
	       container.add(initdb);
	       container.add(Rptdb);

	       gbc.gridx = 0;
	       gbc.gridy = 1;
	       headPanel.add(container, gbc);
	       
	       mainFrame.add(headPanel, BorderLayout.NORTH);
	   }

	   /**
	    * Initializes the control panel where the bulk of data is showed 
	    */
	   private void initControlPanel() {
	       controlPanel = new JPanel();
	       
	       //used to show white background from mainFrame
	       controlPanel.setOpaque(false);
	       controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
	       
	       mainFrame.add(controlPanel, BorderLayout.CENTER);
	   }

	   /**
	    * Initialize the menu bar which contains menu actions such as save load new and exit
	    */
	   private void initMenuBar() {
	       JMenuBar menuBar;
	       menuBar = new JMenuBar();
	       mainFrame.setJMenuBar(menuBar);

	       JMenu fileMenu = new JMenu("File");
//	       JMenu editMenu = new JMenu("Edit");
	       menuBar.add(fileMenu);
//	       menuBar.add(editMenu);

	       JMenuItem newAction = new JMenuItem("New");
	       fileMenu.add(newAction);
	       newAction.addActionListener(new createTreeAction());
	       
	       JMenuItem openAction = new JMenuItem("Open");
	       fileMenu.add(openAction);
	       openAction.addActionListener(new openAction());
	       
	       fileMenu.addSeparator();

	       JMenuItem saveAction = new JMenuItem("Save");
	       fileMenu.add(saveAction);
	       saveAction.addActionListener(new saveAction());
	       
	       JMenuItem saveAsAction = new JMenuItem("Save As");
	       fileMenu.add(saveAsAction);
	       saveAsAction.addActionListener(new saveAsAction());
	       
	       
	       JMenuItem exitAction = new JMenuItem("Exit");
	       fileMenu.addSeparator();
	       fileMenu.add(exitAction);
	       //anonymous function as there is no need to have this as a fully encapsulated
	       //actionlistner class
	       exitAction.addActionListener(new ActionListener() {
	         //  @Override
	           public void actionPerformed(ActionEvent e) {
	               if (checkUserContinue()) {
	                   System.exit(0);
	               }
	           }
	       });       
	   }

	   /**
	    * Initialises the status bar where information such as messages are 
	    * displayed to the user right at the botton of the screen
	    */
	   private void initStatusBar() {
	       JPanel statusPanel = new JPanel();
	       statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
	       
	       mainFrame.add(statusPanel, BorderLayout.SOUTH);
	       
	       //set size to the mainframe
	       statusPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 18));
	       statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
	       
	       //align text to the left
	       statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
	       //this is where the status message will be displayed
	       statusPanel.add(statusLabel);
	   }

	   /**
	    * Convenience method to edit the status. Basically sets the text of the 
	    * label inside the status bar
	    * @param status the message to display
	    */
	   private void editStatus(String status) {
	       statusLabel.setText(status);
	   }
	   
	   
	   /**
	    * Action class that implements ActionListner
	    * Used to display the add relative function after clicking a button for a specified 
	    * family member 
	    */
	   private class addRelativeAction implements ActionListener {

	       private FamilyMember member;
	       //because we can call this actionlistner on any relative
	       //we need to pass the member we would like to edit as a parameter
	       //this then catches that parameter and does the correct actions
	       public addRelativeAction(FamilyMember member) {
	           this.member = member;
	       }

	     //  @Override
	       public void actionPerformed(ActionEvent e) {
	           //display the add relative form for the current member
	           displayAddRelativeInfo(member);
	       }
	   }
	   
	   
	   
	   /**
	    * Edit member action which implements ActionListner to display
	    * the edit member form when a button is clicked for a specified family member
	    */
	   private class editMemberAction implements ActionListener {

	       private FamilyMember member;
	       //because we can call this actionlistner on any relative
	       //we need to pass the member we would like to edit as a parameter
	       //this then catches that parameter and does the correct actions
	       public editMemberAction(FamilyMember member) {
	           this.member = member;
	       }

	      //  @Override
	       public void actionPerformed(ActionEvent e) {
	           //displays the edit member info form
	           displayEditMemberInfo(member);
	       }
	   }

	   /**
	    * create tree action implements actionlistner to show the create tree form 
	    * for a specified family member 
	    */
	   private class createTreeAction implements ActionListener {

	    //   @Override
	       public void actionPerformed(ActionEvent e) {
	           
	           if (checkUserContinue()) {
	               //check if tree is not saved and reset the main variables
	               currentFamilyTree = new FamilyTree();
	               currentFile = null;
	               //display the new (empty) tree
	               displayTree(currentFamilyTree);
	               editStatus("Blank tree created");
	           }
              
	       }
	   }
	   
	   /**
	    * create tree action implements actionlistner to show the create tree form 
	    * for a specified family member 
	    */
	   private class InitializeDatabaseAction implements ActionListener {

	    //   @Override
	       public void actionPerformed(ActionEvent e) {
	           
	           if (checkUserContinue()) {
	               //check if tree is not saved and reset the main variables
	               currentFamilyTree = new FamilyTree();
	               currentFile = null;
	               //display the new (empty) tree
	               displayTree(currentFamilyTree);
	               editStatus("Blank tree created");
	           }

	           int answer = JOptionPane.showConfirmDialog(mainFrame, 
	        			"Do you want to Initialize Database", "Your title goes here", 
	        	        JOptionPane.YES_NO_OPTION);

	        	if (answer == JOptionPane.NO_OPTION) {
	        		// do something
	        		
	        	} else if(answer == JOptionPane.YES_OPTION) {
	        	  	// do something else
	        		FamilyController familyController = new FamilyController();
		            familyController.createDB();
	        	}
	        	           
	       }
	   }
	   
	   
	   
	   
	   /**
	    * create tree action implements actionlistener to show the create tree form 
	    * for a specified family member 
	    */
	   private class ReportAction implements ActionListener {

	    //   @Override
	       public void actionPerformed(ActionEvent e) {
	           
	     
	        	  	// do something else
	        		FamilyController familyController = new FamilyController();
		            familyController.Displayreport();
	        	}
	           
	           
	      
	   }
	   
	   
	   
	   /**
	    * Open action implements actionlistner which invokes a jDialogBox such that
	    * the user can select a file to open within the application 
	    */
	   private class openAction implements ActionListener {

	    //   @Override
	       public void actionPerformed(ActionEvent e) {
	    	   String Filename;
	           if (checkUserContinue()) {
	               JFileChooser jFileChooser = new JFileChooser();
	               //set file filters
	               jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.gd)", "gd"));
	               jFileChooser.setAcceptAllFileFilterUsed(true);
	               
	               int result = jFileChooser.showOpenDialog(mainFrame);
	               //process jfilechooser result
	               if (result == JFileChooser.APPROVE_OPTION) {
	                   try {
	                       //try to open the file, display the family tree
	                       openFile(jFileChooser.getSelectedFile());
	                       displayTree(currentFamilyTree);
	                       editStatus("File opened from: " + (jFileChooser.getSelectedFile().getAbsolutePath()));
	                       Filename= jFileChooser.getSelectedFile().getAbsolutePath();
	                   } catch (Exception j) {
	                       //error
	                       showErrorDialog(j);
	                       editStatus("Error: " + j.getMessage());
	                   }
	               }
	           }

	       }
	   }

	   
	   /**
	    * Convenience method to check if the tree is loaded. Used to check if the user
	    * wants to continue despite the tree being loaded
	    * @return true if the tree does not have a root or if the user wishes to continue
	    */
	   private boolean checkUserContinue() {
	       if (currentFamilyTree.hasRoot()) {
	           int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you wish to continue? Any unsaved changes will be lost", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
	           return dialogResult == JOptionPane.YES_OPTION;
	       }
	       return true;
	   }

	   /**
	    * displays the family tree object through a jTree.
	    * @param familyTree the family tree to display
	    */
	   private void displayTree(FamilyTree familyTree) {

	       //create the root node
	       DefaultMutableTreeNode main = new DefaultMutableTreeNode("Main");
	       //last selected path to keep track of the last person the user selected. 
	       //Used when adding or canceling an action
	       TreePath lastSelectedNode = null;
	       
	       //mutable tree node allowing objects as nodes
	       DefaultMutableTreeNode top;
	       
	       //no data loaded in the tree
	       if (!familyTree.hasRoot()) {
	           top = new DefaultMutableTreeNode("No tree data found.");

	       } else {
	           //add the root person
	    	   FamilyMember t1 = familyTree.getRoot();
	           top = new DefaultMutableTreeNode(familyTree.getRoot());
	           //call the recursive method to populate the entire tree with all the 
	           //details from the root family member
	           createTree(top, familyTree.getRoot());
	           //if the user selected a member, set the last selected path
	           lastSelectedNode = tree.getSelectionPath();

	       }
	       //Create the tree and allow one selection at a time and hide the root node
	       tree = new JTree(main);
	       main.add(top);
	       tree.setRootVisible(false);
	       tree.setShowsRootHandles(true);
	       tree.setEnabled(true);
	       tree.expandPath(new TreePath(main.getPath()));
	       tree.getSelectionModel().addTreeSelectionListener(new treeSelectorAction());
	       tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	       tree.setBorder(new EmptyBorder(0, 30, 0, 30));

	       //expand all the tree nodes
	       for (int i = 0; i < tree.getRowCount(); i++) {
	           tree.expandRow(i);
	       }
	       
	       //have a custom renderer for the nodes of the tree
	       //dim the text nodes and allow selection of the familymember object nodes
	       tree.setCellRenderer(new DefaultTreeCellRenderer() {
	           @Override
	           public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {

	               DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	               Object nodeInfo = node.getUserObject();
	               if (nodeInfo instanceof FamilyMember) {
	                   setTextNonSelectionColor(Color.BLACK);
	                   setBackgroundSelectionColor(Color.LIGHT_GRAY);
	                   setTextSelectionColor(Color.BLACK);
	                   setBorderSelectionColor(Color.WHITE);
	               } else {
	                   setTextNonSelectionColor(Color.GRAY);
	                   setBackgroundSelectionColor(Color.WHITE);
	                   setTextSelectionColor(Color.GRAY);
	                   setBorderSelectionColor(Color.WHITE);
	               }
	               setLeafIcon(null);
	               setClosedIcon(null);
	               setOpenIcon(null);
	               super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
	               return this;
	           }
	       });

	       //add the tree to a scrolepane so the user can scroll 
	       JScrollPane treeScrollPane = new JScrollPane(tree);
	       treeScrollPane.setPreferredSize(new Dimension(300, 0));

	       //create the info panel to be displayed in the control panel
	       infoPanel = new JPanel();
	       infoPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
	       infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

	       JLabel promptInfo;
	       JButton addNewRoot = new JButton("Add root person");
	       addNewRoot.addActionListener(new addRelativeAction(null));
	       if (!familyTree.hasRoot()) {
	           promptInfo = new JLabel("<html>Load a tree or add a new root person</html>");
	           infoPanel.add(addNewRoot);
	       } else {
	           promptInfo = new JLabel("<html>Select a family member to view information</html>");
	       }

	       promptInfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
	       infoPanel.add(promptInfo, BorderLayout.NORTH);
	       infoPanel.setOpaque(false);

	       controlPanel.removeAll();

	       JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
//	       container.setOpaque(false);
	       controlPanel.add(container);

	       container.setLayout(new BorderLayout());
	       container.add(treeScrollPane, BorderLayout.WEST);
	       container.add(infoPanel, BorderLayout.CENTER);
	       
	       controlPanel.add(container);
	       controlPanel.validate();
	       controlPanel.repaint();
	       
	       //scroll the tree to the last selected path
	       tree.setSelectionPath(lastSelectedNode);
	   }

	   /**
	    * cancels the edit by returning to display member info form
	    */
	   private class cancelEditMemberAction implements ActionListener {

	       FamilyMember member;

	       public cancelEditMemberAction(FamilyMember member) {
	           this.member = member;
	       }

	     //  @Override
	       public void actionPerformed(ActionEvent e) {
	           displayMemberInfo(member);
	           editStatus("Action canceled");
	       }
	   }
	   /**
	    * if a file exists, propmpt to overwrite the saved file. If not, initiate save as action
	    */
	   private class saveAction implements ActionListener {

	   //    @Override
	       public void actionPerformed(ActionEvent e) {
	           try {
	               if (currentFile != null) {
	                   int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Would You Like to overwrite the current tree?", "Warning", JOptionPane.YES_NO_OPTION);
	                   if (dialogResult == JOptionPane.YES_OPTION) {
	                       //save the file
	                       saveToFile(currentFile);
	                       editStatus("File saved to: " + currentFile.getPath());
	                   }
	               } else {
	                   editStatus("File not loaded");
	                   //save as instead
	                   ActionListener listner = new saveAsAction();
	                   listner.actionPerformed(e);

	               }

	           } catch (Exception j) {
	               showErrorDialog(j);
	               editStatus("Error: "+ j.getMessage());
	           }
	       }
	   }

	   /**
	    * save the current tree as another file
	    */
	   private class saveAsAction implements ActionListener {

	   //    @Override
	       public void actionPerformed(ActionEvent e) {
	           JFileChooser jFileChooser = new JFileChooser() {
	               //check if file already exists, as to overwrite
	               @Override
	               public void approveSelection() {
	                   File selectedFile = getSelectedFile();
	                   if (selectedFile.exists() && getDialogType() == SAVE_DIALOG) {
	                       int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
	                       switch (result) {
	                           case JOptionPane.YES_OPTION:
	                               super.approveSelection();
	                               return;
	                           case JOptionPane.NO_OPTION:
	                               return;
	                           case JOptionPane.CLOSED_OPTION:
	                               return;
	                           case JOptionPane.CANCEL_OPTION:
	                               cancelSelection();
	                               return;
	                       }
	                   }
	                   super.approveSelection();
	               }
	           };
	           jFileChooser.setSelectedFile(new File("Family Tree.gd"));
	           //Set an extension filter, so the user sees other gd files
	           jFileChooser.setFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.gd)", "gd"));
	           //propmpt to save
	           int result = jFileChooser.showSaveDialog(mainFrame);
	           if (result == JFileChooser.APPROVE_OPTION) {
	               try {
	                   String filename = jFileChooser.getSelectedFile().toString();
	                   if (!filename.endsWith(".gd")) {
	                       filename += ".gd";
	                   }
	                   File file = new File(filename);

	                   saveToFile(file);
	                   displayTree(currentFamilyTree);
	                   editStatus("File saved to: " + (file.getAbsolutePath()));
	               } catch (Exception j) {
	                   showErrorDialog(j);
	                   editStatus("Error: "+ j.getMessage());
	               }
	           }
	       }
	   }

	   /**
	    * action invoked when the user selects a node from the tree
	    */
	   private class treeSelectorAction implements TreeSelectionListener {

	       public void valueChanged(TreeSelectionEvent event) {
	           DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

	           //no selection
	           if (node == null) {
	               return;
	           }

	           //if the selection is a familymember object
	           Object nodeInfo = node.getUserObject();
	           if (nodeInfo instanceof FamilyMember) {
	               //display details
	               displayMemberInfo((FamilyMember) nodeInfo);
	               editStatus("Display details for: " + ((FamilyMember) nodeInfo));
	           }
	       }
	   }

	   /**
	    * Saves the object to a file by using serialization 
	    * @param file the file to save to
	    */
	   private void saveToFile(File file) {
	       // save the object to file
	       FileOutputStream fos = null;
	       ObjectOutputStream out = null;
	       try {
	           //set the output streams 
	           fos = new FileOutputStream(file);
	           out = new ObjectOutputStream(fos);
	           //write the object to the file
	           out.writeObject(this.currentFamilyTree);

	           out.close();
	           currentFile = file;
	           // call the controller to get the file ID 
	           FamilyController familyController = new FamilyController();
 	           familyController.SaveFileMember(currentFile.getPath(),this.currentFamilyTree);
	           
	           
	       } catch (Exception ex) {
	    	   System.out.println(ex);
	           throw new IllegalArgumentException("File could not be saved");
	       }
	   }

	   /**
	    * Opens a file and load the data to the existing variables
	    * @param file the file to open
	    */
	   private void openFile(File file) {
	       // read the object from file
	       FileInputStream fis = null;
	       ObjectInputStream in = null;
	       FamilyTree ft = null;
	       try {
	           //set the input streams 
	           fis = new FileInputStream(file);
	           in = new ObjectInputStream(fis);

	           //try to assign the object
	           ft = (FamilyTree) in.readObject();
	           in.close();

	           currentFamilyTree.setRoot(ft.getRoot());
	           currentFile = file;
	           tree = new JTree();
	       } catch (Exception ex) {
	           throw new IllegalArgumentException("File could not be read.");
	       }

	   }
	   
	   /**
	    * Displays a specified member details 
	    * @param member the member details to display
	    */
	   private void displayMemberInfo(FamilyMember member) {
	       tree.setEnabled(true);
	       
	//reset the info panel
	       infoPanel.removeAll();
	       infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	       // Create the gridbag layout for the components 
	       infoPanel.setLayout(new GridBagLayout());
	       GridBagConstraints gbc = new GridBagConstraints();

	       JPanel container = new JPanel();
	       gbc.gridx = 0;
	       gbc.gridy = 0;
	       gbc.weightx = 1.0;
	       gbc.weighty = 1.0;
	       gbc.fill = GridBagConstraints.BOTH;

	       infoPanel.add(container, gbc);
	       
	       //set another layout for the details 
	       GroupLayout layout = new GroupLayout(container);
	       container.setLayout(layout);
	       //dynamic gaps 
	       layout.setAutoCreateGaps(true);

	       //form components possibly split these into seperate functions 
	       JLabel memberInfoLabel = new JLabel("Person Info: ");
	       memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
	       JLabel nameLabel = new JLabel("Name");
	       JLabel nameTextField = new JLabel(member.getFirstName(), 10);
	       JLabel lastnameLabel = new JLabel("Surname");
	       JLabel lastnameTextField = new JLabel(member.getSurname(), 10);
	     ////  JLabel maidennameLabel = new JLabel("Maiden Name");
	      //  JLabel maidennameTextField = new JLabel();
	       JLabel BirthdayLabel = new JLabel("Birth Date");
	       JLabel BirthdayTextField = new JLabel(member.getDOB(), 10);
	       JLabel DeathLabel = new JLabel("Death Date");
	       JLabel DeathTextField = new JLabel(member.getDOD(), 10);
	       JLabel ProfessionLabel = new JLabel("Profession");
	       JLabel ProfessionTextField = new JLabel(member.getProfession(), 10);
	       JLabel PlaceOfLivingLabel = new JLabel("PlaceOfLiving");
	       JLabel PlaceOfLivingTextField = new JLabel(member.getPlaceOfLiving(), 10);
	       JLabel MentalHealthLabel = new JLabel("MentalHealth");
	       JLabel MentalHealthTextField = new JLabel(member.getMentalHealth(), 10);
	       JLabel BioLabel = new JLabel("Bio");
	       JLabel BioTextField = new JLabel(member.getBio(), 10);
	   
	       JLabel genderLabel = new JLabel("Gender");
	       JLabel genderComboBox = new JLabel(member.getGender().toString());
	       
	   
	       JLabel relativeInfoLabel = new JLabel("Relative Info: ");
	       relativeInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

	       JLabel fatherLabel = new JLabel("Father");
	       JLabel fatherTextField = new JLabel();
	        if (member.has(FamilyMember.Attribute.FATHER)) {
	           fatherTextField.setText(member.getFather().toString());
	        } else {
	           fatherTextField.setText("No father on record");
	       }
	       JLabel motherLabel = new JLabel("Mother");
	       JLabel motherTextField = new JLabel();
	        if (member.has(FamilyMember.Attribute.MOTHER)) {
	           motherTextField.setText(member.getMother().toString());
	        } else {
	            motherTextField.setText("No mother on record");
	        }
	 
	       JLabel lineLabel = new JLabel("  ");
	       String lines = "<html>";
	       lines += "<br>=======================================<br>";
	       lines += "</html>";
	       
	       JLabel lineTextField = new JLabel(lines);
	      // lineTextField.setText(" ");
	       
	       JLabel childrenLabel = new JLabel("Children/Siblings");
	       String children = "<html>";
	       if (member.has(FamilyMember.Attribute.CHILDREN)) {
	           for (FamilyMember child : member.getChildren()) {
	               children += child.toString() + "<br>";
	           }
	           children += "</html>";
	       } else {
	           children = "No children on record";
	       }
	       JLabel childrenTextField = new JLabel(children);

	       JLabel grandChildrenLabel = new JLabel("Grand Children");
	       String grandChildren = "<html>";
	       if (member.has(FamilyMember.Attribute.CHILDREN)) {
	           for (FamilyMember child : member.getChildren()) {
	               if (child.has(FamilyMember.Attribute.CHILDREN)) {
	                   for (FamilyMember grandChild : child.getChildren()) {
	                       grandChildren += grandChild.toString() + "<br>";
	                   }
	               }

	           }
	           grandChildren += "</html>";
	       } else {
	           grandChildren = "No grand children on record";
	       }
	       JLabel grandChildrenTextField = new JLabel(grandChildren);
	       //
  
	       // Allign all the components using the group layout notation
	       //horizontl alignment 
	       layout.setHorizontalGroup(layout.createSequentialGroup()
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                       .addComponent(memberInfoLabel)
	                       .addComponent(nameLabel)
	                       .addComponent(lastnameLabel)
	                       .addComponent(genderLabel)
	                       .addComponent(BirthdayLabel)
	                       .addComponent(DeathLabel)   
	                       .addComponent(ProfessionLabel)
	                       .addComponent(PlaceOfLivingLabel)
	                       .addComponent(MentalHealthLabel)
	                       .addComponent(BioLabel)
	                       .addComponent(lineLabel)
	                       .addComponent(relativeInfoLabel)
	                       .addComponent(fatherLabel)
	                       .addComponent(motherLabel)
	                       .addComponent(childrenLabel)
	                       .addComponent(grandChildrenLabel)
	               )
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                       .addComponent(nameTextField)
	                       .addComponent(lastnameTextField)
                           .addComponent(genderComboBox)
	                       .addComponent(BirthdayTextField)
	                       .addComponent(DeathTextField) 
	                       .addComponent(ProfessionTextField)
	                       .addComponent(PlaceOfLivingTextField)
	                       .addComponent(MentalHealthTextField)
	                       .addComponent(BioTextField)
	                       .addComponent(lineTextField)
	                       .addComponent(fatherTextField)
	                       .addComponent(motherTextField)
	                        .addComponent(childrenTextField)
	                       .addComponent(grandChildrenTextField)
	                       )
	       );

	       // verticle alignmnet 
	       layout.setVerticalGroup(layout.createSequentialGroup()
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(memberInfoLabel))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(nameLabel)
	                       .addComponent(nameTextField))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(lastnameLabel)
	                       .addComponent(lastnameTextField))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                        .addComponent(genderLabel)
	                        .addComponent(genderComboBox))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        	             .addComponent(BirthdayLabel)
	        	             .addComponent(BirthdayTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) 
	        	             .addComponent(DeathLabel)
	        	             .addComponent(DeathTextField))   
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) 
	        	             .addComponent(ProfessionLabel)
	        	             .addComponent(ProfessionTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) 
	        	             .addComponent(PlaceOfLivingLabel)
	        	             .addComponent(PlaceOfLivingTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) 
	        	             .addComponent(MentalHealthLabel)
	        	             .addComponent(MentalHealthTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) 
	        	             .addComponent(BioLabel)
	        	             .addComponent(BioTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                     .addComponent(lineLabel)
		                      .addComponent(lineTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(relativeInfoLabel))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(fatherLabel)
	                         .addComponent(fatherTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(motherLabel)
	                        .addComponent(motherTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                      .addComponent(childrenLabel)
	                       .addComponent(childrenTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                        .addComponent(grandChildrenLabel)
	                        .addComponent(grandChildrenTextField)     
	                 )
	      );

	       JButton editMember = new JButton("Edit Details");
	       editMember.addActionListener(new editMemberAction(member));
	       JButton addRelative = new JButton("Add Relative");
	       addRelative.addActionListener(new addRelativeAction(member));

	       JPanel btncontainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
	       btncontainer.add(editMember);
	       btncontainer.add(addRelative);

	       gbc.gridx = 0;
	       gbc.gridy = 1;
	       gbc.fill = GridBagConstraints.HORIZONTAL;
	       infoPanel.add(btncontainer, gbc);
	       infoPanel.validate();
	       infoPanel.repaint();
	   }

	   /**
	    * Displays the edit member form
	    * @param member he member to edit
	    */
	   private void displayEditMemberInfo(final FamilyMember member) {
		   
		  
	       tree.setEnabled(false);
	       
	       //reset the info panel
	       infoPanel.removeAll();
	       infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	       // Create the layout
	       JPanel info = new JPanel();
	       GridBagConstraints gbc = new GridBagConstraints();
	       gbc.gridx = 0;
	       gbc.gridy = 0;
	       gbc.fill = GridBagConstraints.BOTH;
	       gbc.weightx = 1.0;
	       gbc.weighty = 1.0;

	       infoPanel.add(info, gbc);
	       GroupLayout layout = new GroupLayout(info);
	       info.setLayout(layout);
	       layout.setAutoCreateGaps(true);
             
	      // IDPerson = member.getNameId();
 	       
	       // Create the components to put in the form
	       JLabel memberInfoLabel = new JLabel("Person Info: ");
	       memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
	       JLabel nameLabel = new JLabel("Name");
	       final JTextField nameTextField = new JTextField(member.getFirstName(), 10);
	       JLabel lastnameLabel = new JLabel("Surname");
	       final JTextField lastnameTextField = new JTextField(member.getSurname(), 10);
	       JLabel genderLabel = new JLabel("Gender");
	       //gender combobox
	       DefaultComboBoxModel<FamilyMember.Gender> genderList = new DefaultComboBoxModel<FamilyMember.Gender>();
	       genderList.addElement(FamilyMember.Gender.FEMALE);
	       genderList.addElement(FamilyMember.Gender.MALE);
	       final JComboBox<FamilyMember.Gender> genderComboBox = new JComboBox<FamilyMember.Gender>(genderList);
	       genderComboBox.setSelectedItem(member.getGender());
	       //no edits allowed, see documentation
	       JLabel BirthdayLabel = new JLabel("Birth Date");	   
		   final JTextField BirthdayTextField = new JTextField(member.getDOB(), 10);
		   JLabel DeathLabel = new JLabel("Death Date");	   
		   final JTextField DeathTextField = new JTextField(member.getDOD(), 10);   
		   JLabel ProfessionLabel = new JLabel("Profession");	   
		   final JTextField ProfessionTextField = new JTextField(member.getProfession(), 10);
		   JLabel PlaceOfLivingLabel = new JLabel("PlaceOfLiving");	   
		   final JTextField PlaceOfLivingTextField = new JTextField(member.getPlaceOfLiving(), 10);
		   JLabel MentalHealthLabel = new JLabel("MentalHealth");	   
		   final JTextField MentalHealthTextField = new JTextField(member.getMentalHealth(), 10);
		   JLabel BioLabel = new JLabel("Bio");	   
		   final JTextField BioTextField = new JTextField(member.getBio(), 10);
 	       // horizontal alignment
	       layout.setHorizontalGroup(layout.createSequentialGroup()
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                       .addComponent(memberInfoLabel)
	                       .addComponent(nameLabel)
	                       .addComponent(lastnameLabel)
	                        .addComponent(genderLabel)
	                        .addComponent(BirthdayLabel)
	                        .addComponent(DeathLabel) 
	                        .addComponent(ProfessionLabel)
	                        .addComponent(PlaceOfLivingLabel)
	                        .addComponent(MentalHealthLabel)
	                        .addComponent(BioLabel)
	               )
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                       .addComponent(nameTextField)
	                       .addComponent(lastnameTextField)
	                        .addComponent(genderComboBox)
	                        .addComponent(BirthdayTextField)
	                        .addComponent(DeathTextField)  
	                        .addComponent(ProfessionTextField) 
	                        .addComponent(PlaceOfLivingTextField) 
	                        .addComponent(MentalHealthTextField) 
	                        .addComponent(BioTextField) 
	               )
	       );

	       // vertical alignment
	       layout.setVerticalGroup(layout.createSequentialGroup()
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(memberInfoLabel))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(nameLabel)
	                       .addComponent(nameTextField))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(lastnameLabel)
	                       .addComponent(lastnameTextField))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                      .addComponent(genderLabel)
	                       .addComponent(genderComboBox))
	               	               
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(BirthdayLabel)
	                      .addComponent(BirthdayTextField))
	               
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(DeathLabel)
	                      .addComponent(DeathTextField))   
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(ProfessionLabel)
	                      .addComponent(ProfessionTextField))
	               
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(PlaceOfLivingLabel)
	                      .addComponent(PlaceOfLivingTextField))
	               
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(MentalHealthLabel)
	                      .addComponent(MentalHealthTextField))
	               
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(BioLabel)
	                      .addComponent(BioTextField))
	       );
	       JButton saveMember = new JButton("Save Details");
	       //this anonymous actionlistner has access to all the above fields making it easy to use without passing as parameters.
	       saveMember.addActionListener(new ActionListener() {
	       //    @Override
	           public void actionPerformed(ActionEvent e) {
	               try {
	                   //try to save the details
	                   member.setFirstName(nameTextField.getText().trim());
	                   member.setSurname(lastnameTextField.getText().trim());
	                    member.setGender((FamilyMember.Gender) genderComboBox.getSelectedItem());
	                    member.setDOB(BirthdayTextField.getText().trim());
	                    member.setDOD(DeathTextField.getText().trim());
	                    member.setProfession(ProfessionTextField.getText().trim());
	                    member.setPlaceOfLiving(PlaceOfLivingTextField.getText().trim());
	                    member.setMentalHealth(MentalHealthTextField.getText().trim());
	                    member.setBio(BioTextField.getText().trim());
	                   displayTree(currentFamilyTree);
	                   editStatus("Member "+member.toString()+" added");
	                   /*
		                   * 
		                   * Update a a record 
		                   * * call a function from controller to update the data in table NameTable
		                   */
	                   
	                   FamilyController familyController = new FamilyController();    
	                   familyController.UpdateMember(member, IDPerson);
		   	      
	                   
	                   
	               } catch (Exception d) {
	                   //any errors such as incorrect names, etc will show up here informing the user
	                   showErrorDialog(d);
	               }
	           }
	       });
	       JButton cancel = new JButton("Cancel");
	       cancel.addActionListener(new cancelEditMemberAction(member));
	       
	       JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
	       container.setOpaque(false);
	       container.add(saveMember);
	       container.add(cancel);

	       gbc.gridx = 0;
	       gbc.gridy = 1;
	       gbc.fill = GridBagConstraints.HORIZONTAL;
	       infoPanel.add(container, gbc);

	       infoPanel.validate();
	       infoPanel.repaint();
	   }

	   /**
	    * display the add relative form for a member 
	    * @param member the member to add a relative
	    */
	   private void displayAddRelativeInfo(final FamilyMember member) {
	       tree.setEnabled(false);
	       
	       //reset the info panel
	       infoPanel.removeAll();
	       infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

	       JPanel info = new JPanel();
	       // if thr tree is empty add a root person otherwise add any relative 
	       JLabel memberInfoLabel = new JLabel("Add new root person", SwingConstants.LEFT);
	       if (member != null) {
	           memberInfoLabel.setText("Add Relative for " + member.toString());
	       }

	       memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

//	           infoPanel = new JPanel();
	       infoPanel.setLayout(new GridBagLayout());
//	       headPanel.setBorder(new EmptyBorder(10,10,10,10));
	       GridBagConstraints gbc = new GridBagConstraints();
	       gbc.gridx = 0;
	       gbc.gridy = 0;
	       gbc.fill = GridBagConstraints.HORIZONTAL;
	       gbc.weightx = 1.0;
	       gbc.weighty = 1.0;
	       infoPanel.add(memberInfoLabel, gbc);

	       gbc.gridx = 0;
	       gbc.gridy = 1;
	       gbc.fill = GridBagConstraints.BOTH;
	       infoPanel.add(info, gbc);
	       // Create the layout
	       GroupLayout layout = new GroupLayout(info);
	       info.setLayout(layout);
	       layout.setAutoCreateGaps(true);

	       // Create the components to put in the form
	       JLabel relativeTypeLabel = new JLabel("Relative Type");
	       DefaultComboBoxModel<FamilyMember.RelativeType> relativeTypeList = new DefaultComboBoxModel<FamilyMember.RelativeType>();

	       relativeTypeList.addElement(FamilyMember.RelativeType.MOTHER);
	       relativeTypeList.addElement(FamilyMember.RelativeType.FATHER);
	       relativeTypeList.addElement(FamilyMember.RelativeType.CHILD);
	       relativeTypeList.addElement(FamilyMember.RelativeType.STEPSIBLING);
	       relativeTypeList.addElement(FamilyMember.RelativeType.HALFSIBLING);
	       final JComboBox<FamilyMember.RelativeType> relativeTypeComboBox = new JComboBox<FamilyMember.RelativeType>(relativeTypeList);
	       
	       //if empty tree, no relative type selection
	     if (member == null) {

	           relativeTypeComboBox.removeAllItems();
	          relativeTypeComboBox.setEnabled(false);

	       }

	       JLabel nameLabel = new JLabel("Name");
	       final JTextField nameTextField = new JTextField("Filippos", 10);
	       JLabel lastnameLabel = new JLabel("Surname");
	       final JTextField lastnameTextField = new JTextField("Pikrides", 10);

	    //   JLabel maidennameLabel = new JLabel("Maiden Name");
	    //   final JTextField maidennameTextField = new JTextField(10);

	       JLabel genderLabel = new JLabel("Gender");
	       DefaultComboBoxModel<FamilyMember.Gender> genderList = new DefaultComboBoxModel<FamilyMember.Gender>();
	       genderList.addElement(FamilyMember.Gender.FEMALE);
	        genderList.addElement(FamilyMember.Gender.MALE);
	      final JComboBox<FamilyMember.Gender> genderComboBox = new JComboBox<FamilyMember.Gender>(genderList);

	        JLabel BirthdayLabel = new JLabel("Birth Date");
		    final JTextField BirthdayTextField = new JTextField(10);
		    JLabel DeathLabel = new JLabel("Death Date");
		    final JTextField DeathTextField = new JTextField(10);  
		    JLabel ProfessionLabel = new JLabel("Profession");
		    final JTextField ProfessionTextField = new JTextField(10);
		    JLabel PlaceOfLivingLabel = new JLabel("Place of living");
		    final JTextField PlaceOfLivingTextField = new JTextField(10);
		    JLabel MentalHealthLabel = new JLabel("Mental Health");
		    final JTextField MentalHealthTextField = new JTextField(10);
		    JLabel BioLabel = new JLabel("Bio");
		    final JTextField BioTextField = new JTextField(10);
	      
	       //anonymous actionlistner has access to all the above varaiables making it easier to use
	       JButton saveMember = new JButton("Add Member");
	       saveMember.addActionListener(new ActionListener() {
	    //        @Override
	           public void actionPerformed(ActionEvent e) {
	        	   
                  String TypeofRelative ;
	        	   
	               try {
	                   //create the objects 
	            	             	   
	            	   
	                   FamilyMember newMember = new FamilyMember(
	                            nameTextField.getText(),
	                            lastnameTextField.getText(),
	                         (FamilyMember.Gender) genderComboBox.getSelectedItem()
	                         ,BirthdayTextField.getText(), DeathTextField.getText(),  
	                         ProfessionTextField.getText(), PlaceOfLivingTextField.getText(),
	                         MentalHealthTextField.getText(), BioTextField.getText()
		                   )  ;
	                   
	                   	                  
	                   
	                  /*
	                   * 
	                   *  Insert a record 
	                   */
	                   FamilyController familyController = new FamilyController();
	   	              
	                   //if no root
	                //   print  BirthdayTextField.getText().length()
	                   if (member == null) {
	                       currentFamilyTree.setRoot(newMember);
	                       
	                       if  ( ( BirthdayTextField.getText().length()  == 0 ) | ( nameTextField.getText().length()  == 0 )  ) 
	            	       {   
	                    	   throw new IllegalArgumentException("FirstName, Surname and Birthday must be completed !!!");
	                        }
	       
	                        else	                       {   
	                        // call controller to add a new member
	                       familyController.InsertMember(newMember,"root" );
	                                             
	                       editStatus("Root member added");
	                       displayTree(currentFamilyTree);
	                       }
	                       
	                   } else {
	                       //add the relative 
	                	   if  ( ( BirthdayTextField.getText().length()  == 0 ) | ( nameTextField.getText().length()  == 0 )  ) 
	            	       {   
	                    	   throw new IllegalArgumentException("FirstName, Surname and Birthday must be completed !!!");
	                        }
	       
	                        else	                       { 
	                         member.addRelative((FamilyMember.RelativeType) relativeTypeComboBox.getSelectedItem(), newMember);
	                         TypeofRelative=  relativeTypeComboBox.getSelectedItem().toString();
	                          // call theh controller to insert a member and add a relative 
	                         familyController.InsertMember(newMember,TypeofRelative);
	 	                     familyController.InsertRelativeMember(member , newMember,TypeofRelative );
	                         
	                        editStatus("New member added");
	                        displayTree(currentFamilyTree);
	                        }
	                   }
	                
	               } catch (Exception d) {
	                   showErrorDialog(d);
	               }
	           }

		private int length(String string) {
			// TODO Auto-generated method stub
			return 0;
		}
	       });
	       JButton cancel = new JButton("Cancel");
	       cancel.addActionListener(new cancelEditMemberAction(member));
	       
	       
	      
	       //just a way to make some QoL changes to the user experience.
	       //Set the appropriate contraints based on the relative type selecgtion
	       relativeTypeComboBox.addActionListener(new ActionListener() {//add actionlistner to listen for change
	         //  @Override
	           public void actionPerformed(ActionEvent e) {

	               switch ((FamilyMember.RelativeType) relativeTypeComboBox.getSelectedItem()) {//check for a match
	                   case FATHER:
	                         genderComboBox.setSelectedItem(FamilyMember.Gender.MALE);
	                       lastnameTextField.setText(member.getSurname());
	                       break;
	                   case MOTHER:
	                        genderComboBox.setSelectedItem(FamilyMember.Gender.FEMALE);
	                       lastnameTextField.setText(member.getSurname());
	                       break;
	                   case CHILD:
	                       lastnameTextField.setText(member.getSurname());
	                       break;
	                   case STEPSIBLING:
	                       lastnameTextField.setText(member.getSurname());
	                     
	                       break;
	                   case HALFSIBLING:
	                       lastnameTextField.setText(member.getSurname());
	                    
	                       break;
	                        
	               }
	           }
	       });
	       // horizontal alignment 
	       layout.setHorizontalGroup(layout.createSequentialGroup()
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                       .addComponent(relativeTypeLabel)
	                       .addComponent(nameLabel)
	                       .addComponent(lastnameLabel)
	                  //     .addComponent(maidennameLabel)
	                       .addComponent(genderLabel)
	                       .addComponent(BirthdayLabel)
	                       .addComponent(DeathLabel) 
	                       .addComponent(ProfessionLabel)
	                       .addComponent(PlaceOfLivingLabel)
	                       .addComponent(MentalHealthLabel)
	                       .addComponent(BioLabel)
	               )
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                       .addComponent(nameTextField)
	                       .addComponent(relativeTypeComboBox)
	                       .addComponent(lastnameTextField)
	                       .addComponent(genderComboBox)
	                       .addComponent(BirthdayTextField)
	                       .addComponent(DeathTextField)  
	                       .addComponent(ProfessionTextField)
	                       .addComponent(PlaceOfLivingTextField)
	                       .addComponent(MentalHealthTextField)
	                       .addComponent(BioTextField)
	               )
	       );

	       // verticle alignment 
	       layout.setVerticalGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(relativeTypeLabel)
	                       .addComponent(relativeTypeComboBox))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(nameLabel)
	                       .addComponent(nameTextField))
	               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(lastnameLabel)
	                       .addComponent(lastnameTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                       .addComponent(genderLabel)
	                       .addComponent(genderComboBox))
	                	                
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            	        .addComponent(BirthdayLabel)
	            	       .addComponent(BirthdayTextField))
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            	        .addComponent(DeathLabel)
	            	       .addComponent(DeathTextField))  
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            	        .addComponent(ProfessionLabel)
	            	       .addComponent(ProfessionTextField)) 
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            	        .addComponent(PlaceOfLivingLabel)
	            	       .addComponent(PlaceOfLivingTextField)) 
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            	        .addComponent(MentalHealthLabel)
	            	       .addComponent(MentalHealthTextField)) 
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            	        .addComponent(BioLabel)
	            	       .addComponent(BioTextField)) 
	                );
	                		

	       JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
	       container.setOpaque(false);
	       container.add(saveMember);
	       container.add(cancel);

	       gbc.gridx = 0;
	       gbc.gridy = 2;
	       infoPanel.add(container, gbc);
	       infoPanel.validate();
	       infoPanel.repaint();
	   }
	   /**
	    * Recursive method to populate the jtree object for each family member of the root person
	    * @param top the node to populate
	    * @param root the member to get the detils from
	    */

	   private void createTree(DefaultMutableTreeNode top, FamilyMember root) {
	       DefaultMutableTreeNode parents = null;
	       DefaultMutableTreeNode father = null;      
	       DefaultMutableTreeNode mother = null;
	       DefaultMutableTreeNode children = null;
	       DefaultMutableTreeNode child = null;
	       DefaultMutableTreeNode father2 = null;
	       DefaultMutableTreeNode father22 = null;
	       DefaultMutableTreeNode father23 = null;
        
	       DefaultMutableTreeNode mother2 = null;
	       DefaultMutableTreeNode mother22 = null;
	       DefaultMutableTreeNode mother23 = null;
	       DefaultMutableTreeNode motherfmm = null;
	       DefaultMutableTreeNode motherfmf = null;
	       
	       
	       
	       Integer i=0,j=0;
	       
	       
	       
	       if (root.has(FamilyMember.Attribute.PARENTS) && root == currentFamilyTree.getRoot()) {
	    	  if ((root.has(FamilyMember.Attribute.FATHER) )   & (root.has(FamilyMember.Attribute.MOTHER) ))  {
	           parents = new DefaultMutableTreeNode("Parents");
	           //add parent node
	           top.add(parents);
	       }
	           if (root.has(FamilyMember.Attribute.FATHER)) {
	        	  
	        	   father   = new DefaultMutableTreeNode("Father");
	        	   DefaultMutableTreeNode fathernext = new DefaultMutableTreeNode(root.getFather());
	        	   father.add(fathernext);
	        	   
	        	   FamilyMember f = root.getFather();
	        	  
	        	   while (f.getFather() != null  | f.getMother() != null) {    //->father-->father
	        		   if (i == 0 ) {
	        		      father2= new DefaultMutableTreeNode("Grand Father");
	        	 	      DefaultMutableTreeNode fathernext2 = new DefaultMutableTreeNode(f.getFather() );
	        		      father2.add(fathernext2);
	        	          father.add(father2);
	        	          i = i+1;
	        	          
	        	          if ( f.getMother() !=null) {                        // father-->mother 
	        	        	  mother2= new DefaultMutableTreeNode("Grand Mother");
			        	      //createTree(fathernext, root.getFather());
			        		   DefaultMutableTreeNode mothernext2 = new DefaultMutableTreeNode(f.getMother() );
			        		   mother2.add(mothernext2);
			        		   father.add(mother2);        	        	  
	        	          }
	        	          
	        	               	          
	        	          
	        	          
	        		   }
	        		   else if ( i == 1 ) {
	        			
	        			        
	        			     
	        			      father22= new DefaultMutableTreeNode("Grand grand Father");     // ->parent->father->father
		        	 	      DefaultMutableTreeNode fathernext22 = new DefaultMutableTreeNode(f.getFather() );
		        		      father22.add(fathernext22);
		        	          father2.add(father22);
		        	          i = i+1;
		        	          
		        	          if ( f.getMother() !=null) {
		        	        	  mother22= new DefaultMutableTreeNode("Grand grand Mother");   //->parent->father->mother
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode mothernext22 = new DefaultMutableTreeNode(f.getMother() );
				        		   mother22.add(mothernext22);
				        		   father2.add(mother22);				        		
		        	          }
		        	          
		        	          if (root.getFather().has(FamilyMember.Attribute.MOTHER) ) {
		        	          FamilyMember fm=root.getFather().getMother();         //-->parent-->father-->monther   (PEPA)
	        			      
		        	          
		        	          if ( fm.getFather() !=null) {                 // -->parent-->father-->monther->father     
		        	        	  motherfmf= new DefaultMutableTreeNode("Grand grand Father");
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode mothernext22 = new DefaultMutableTreeNode(fm.getFather() );
				        		   motherfmf.add(mothernext22);
				        		   mother2.add(motherfmf);				        		
		        	          }
		        	          if ( fm.getMother() !=null) {      // -->parent-->father-->monther->mother
		        	        	  motherfmm= new DefaultMutableTreeNode("Grand grand Mother");
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode mothernextfmm = new DefaultMutableTreeNode(fm.getMother() );
				        		   motherfmm.add(mothernextfmm);
				        		   mother2.add(motherfmm);	
				        	//	   father.add(mother2);  
		        	          }
		        	          }  /// if has mother
		        	          
	        	      }
	        		   else if ( i == 2 ) {
	        			      father23= new DefaultMutableTreeNode("Grand grand Father");    // father-->father-->father-->father  ( filippod-->pambos--> JOHN--->STAUROS
		        	 	      DefaultMutableTreeNode fathernext23 = new DefaultMutableTreeNode(f.getFather() );
		        		      father23.add(fathernext23);
		        	          father22.add(father23);
		        	          
		        	          i = i+1;
		        	          if ( f.getMother() !=null) {                               // father-->father-->mother   
		        	        	  mother22= new DefaultMutableTreeNode("Grand grand Mother");
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode mothernext22 = new DefaultMutableTreeNode(f.getMother() );
				        		   mother22.add(mothernext22);
				        		   father22.add(mother22);				        		
		        	          }
		        	          
		        	          
	        			   
	        		        }
	        		    
	        		     
	        		   f= f.getFather();
	        		 //  if ( f.getMother() !=null) {   fm= f.getMother();}
	        	      }
	        	    
	        	   if ((root.has(FamilyMember.Attribute.FATHER) )   & (root.has(FamilyMember.Attribute.MOTHER) ))  { 
	                  parents.add(father);   }
	        	   else
	        		   top.add(father);
	       }   
	       
	       
	       if (root.has(FamilyMember.Attribute.MOTHER)) {
	    	   if ((!root.has(FamilyMember.Attribute.FATHER) )   & (root.has(FamilyMember.Attribute.MOTHER) ))  {
		           parents = new DefaultMutableTreeNode("ONLY MOTHER");
		           //add parent node
		           top.add(parents);
		       }
	    	   
	    	   
        	   mother = new DefaultMutableTreeNode("Mother");
        	   DefaultMutableTreeNode mothernext = new DefaultMutableTreeNode(root.getMother());
        	   mother.add(mothernext);
        	   
        	   FamilyMember m = root.getMother();
        	   // change 3/7/2022
        	   while (m.getMother() != null | m.getFather() !=null) {
        		   if (j == 0 ) {
        			    if ( m.getMother() !=null) {
		        		   mother2= new DefaultMutableTreeNode("Grand Mother");
		        	      //createTree(fathernext, root.getFather());
		        		   DefaultMutableTreeNode mothernext2 = new DefaultMutableTreeNode(m.getMother() );
		        		   mother2.add(mothernext2);
		        		   mother.add(mother2);
		        		   
        			    } 
		        		    if ( m.getFather() !=null) {
		        	        	  father2= new DefaultMutableTreeNode("Grand Father");
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode fathernext2 = new DefaultMutableTreeNode(m.getFather() );
				        		   father2.add(fathernext2);
				        		   mother.add(father2);
				        		  
		        	          } 
		        		    j++;   
        		   }
        		   else if ( j== 1) {
        			         
        			     
        			      
        			         mother22= new DefaultMutableTreeNode("Grand grand Mother");   // root->mother->mother->mother
			     	 	      DefaultMutableTreeNode mothernext22 = new DefaultMutableTreeNode(m.getMother() );
			     	 	       mother22.add(mothernext22);
			     	 	      mother2.add(mother22);
			     	 	      j++;
			     	 	      
			     	 	    if ( m.getFather() !=null) {
		        	        	  mother22= new DefaultMutableTreeNode("Grand grand Father");   //->parent->father->mother
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode mothernext223 = new DefaultMutableTreeNode(m.getFather() );
				        		   mother22.add(mothernext223);
				        		   mother2.add(mother22);				        		
		        	          }
			     	 	      
			     	 	  if (root.getFather().has(FamilyMember.Attribute.FATHER) ) {
			     	 	    FamilyMember fem=root.getMother().getFather();         //-->parent-->mother-->father    
			     	 	  
			     	 	  if ( fem.getFather() !=null) {                 // -->parent-->monther->father -->father    
	        	        	  motherfmf= new DefaultMutableTreeNode("Grand grand Father");
			        	      //createTree(fathernext, root.getFather());
			        		   DefaultMutableTreeNode mothernext222 = new DefaultMutableTreeNode(fem.getFather() );
			        		   motherfmf.add(mothernext222);
			        		   mother.add(motherfmf);				        		
	        	          }
	        	          if ( fem.getMother() !=null) {      // -->parent-->monther-->father->mother
	        	        	  motherfmm= new DefaultMutableTreeNode("Grand grand Mother");
			        	      //createTree(fathernext, root.getFather());
			        		   DefaultMutableTreeNode mothernextfmm = new DefaultMutableTreeNode(fem.getMother() );
			        		   motherfmm.add(mothernextfmm);
			        		   mother.add(motherfmm);	
			        	//	   father.add(mother2);  
	        	          }
			     	 	  }
			     	 	    
			 	      }
			 		   else if ( j == 2 ) {
			 			  mother23= new DefaultMutableTreeNode("Grand grand Mother");
			     	 	      DefaultMutableTreeNode mothernext23 = new DefaultMutableTreeNode(m.getMother() );
			     	 	      mother23.add(mothernext23);
			     	 	      mother22.add(mother23);
			     	          j++;
			     	         if ( m.getFather() !=null) {
		        	        	  mother22= new DefaultMutableTreeNode("Grand grand Father");
				        	      //createTree(fathernext, root.getFather());
				        		   DefaultMutableTreeNode mothernext22 = new DefaultMutableTreeNode(m.getFather() );
				        		   mother22.add(mothernext22);
				        		 //  father22.add(mother22);				        		
		        	          }
 			   	        }
        			   
        			   
        		   if ( m.getMother() !=null) { 
        		     m= m.getMother();
        		   }
        		   else {
        			   m= m.getFather();
        		   }
        	   }
               
           
             parents.add(mother);  
          }   
	       
	    }
	       
	       
	 ///   draw the children       
	       
	     if (root.getFather()  != null ) {
		        if (root.getFather().has(FamilyMember.Attribute.CHILDREN)) {
		           children = new DefaultMutableTreeNode("Children");
		           for (FamilyMember f : root.getFather().getChildren()) {
		               child = new DefaultMutableTreeNode(f);
		               //for each child, call create tree to populate their subtree nodes 
		              // createTree(child, f);
		               //ad that child to the top node 
		               children.add(child);
		           }
		           top.add(children);
		        }
		       }
		       else if (root.getMother()  != null ) {
			        if (root.getMother().has(FamilyMember.Attribute.CHILDREN)) {
				           children = new DefaultMutableTreeNode("Children");
				           for (FamilyMember f : root.getMother().getChildren()) {
				               child = new DefaultMutableTreeNode(f);
				               //for each child, call create tree to populate their subtree nodes 
				              // createTree(child, f);
				               //ad that child to the top node 
				               children.add(child);
				           }
				           top.add(children);
				        }
				}
	     
	     if (root.getFather()  != null ) {
	     if (root.getFather().getFather() != null ) {	     
	     FamilyMember fc = root.getFather().getFather();
	     if (fc.has(FamilyMember.Attribute.CHILDREN)) {
	           children = new DefaultMutableTreeNode("Children");
	           for (FamilyMember f : fc.getChildren()) {
	               child = new DefaultMutableTreeNode(f);
	               //for each child, call create tree to populate their subtree nodes 
	              // createTree(child, f);
	               //ad that child to the top node 
	               children.add(child);
	           }
	           father.add(children);
	        }
	     }
	     
	     }
	     
	  //   stepfather
	      
		     if (root.has(FamilyMember.Attribute.CHILDREN)) {
		           children = new DefaultMutableTreeNode("Children");
		           for (FamilyMember f : root.getChildren()) {
		               child = new DefaultMutableTreeNode(f);
		               //for each child, call create tree to populate their subtree nodes 
		              // createTree(child, f);
		               //ad that child to the top node 
		               children.add(child);
		           }
		           top.add(children);
		        }
		     
	     
	     
	     
	     
	     
	       
	 }

	   /**
	    * shows a error dialog containing an error message from a exception 
	    * @param e the exception to get the message from
	    */
	   private void showErrorDialog(Exception e) {

		   
		   
		   JOptionPane.showMessageDialog(mainFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	   }

	
}
