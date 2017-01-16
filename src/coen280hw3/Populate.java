/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coen280hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author akashmalla
 */
public class Populate {

    private static Connection con;
    private static ArrayList<String> hm;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Populate c = new Populate();
        c.run();
    }

    /**
     * This is the skeleton code of database access
     */
    public void run() {

        hm = new ArrayList<String>();
        hm.add("Active Life");
        hm.add("Arts & Entertainment");
        hm.add("Automotive");
        hm.add("Car Rental");
        hm.add("Cafes");
        hm.add("Beauty & Spas");
        hm.add("Convenience Stores");
        hm.add("Dentists");
        hm.add("Doctors");
        hm.add("Drugstores");
        hm.add("Department Stores");
        hm.add("Education");
        hm.add("Event Planning & Services");
        hm.add("Flowers & Gifts");
        hm.add("Food");
        hm.add("Health & Medical");
        hm.add("Home Services");
        hm.add("Home & Garden");
        hm.add("Hospitals");
        hm.add("Hotels & Travel");
        hm.add("Hardware Stores");
        hm.add("Grocery");
        hm.add("Medical Centers");
        hm.add("Nurseries & Gardening");
        hm.add("Nightlife");
        hm.add("Restaurants");
        hm.add("Shopping");
        hm.add("Transportation");

        ResultSet result = null;
        String businessJSONFile = "/Users/akashmalla/Documents/COEN_280_Winter_2016/Assignment_3/YelpDataset-CptS451/yelp_business.json";
        String userJSONFile = "/Users/akashmalla/Documents/COEN_280_Winter_2016/Assignment_3/YelpDataset-CptS451/yelp_user.json";
        String reviewJSONFile = "/Users/akashmalla/Documents/COEN_280_Winter_2016/Assignment_3/YelpDataset-CptS451/yelp_review.json";
        String checkinJSONFile = "/Users/akashmalla/Documents/COEN_280_Winter_2016/Assignment_3/YelpDataset-CptS451/yelp_checkin.json";
        try {
            con = openConnection();
            System.out.println("Connected to database");
            //dropTables();
            System.out.println("Old tables dropped");
            createBusinessTable();
            createBCategoryTable();
            createHoursTable();
            //createBtoBCategoryTable();
            //createSubCategoryTable();
            //createBCatToSubCategoryTable();
            createUserTable();
            createReviewTable();
            createGetTotalVotesFunction();
            createCheckinTable();
            System.out.println("All tables created");
            //populateMainCategories();
            ReadJSON(new File(businessJSONFile), "UTF-8");
            System.out.println("Business data populated");
            ReadUserJSON(new File(userJSONFile), "UTF-8");
            System.out.println("User data populated");
            ReadReviewJSON(new File(reviewJSONFile), "UTF-8");
            ReadCheckinJSON(new File(checkinJSONFile), "UTF-8");
            //showTableContent(con); 
            //result = searchAllTuples(con); 
            //showMetaDataOfResultSet(result);
            //showResultSet(result); 
        } catch (SQLException e) {
            System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the database driver");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            // Never forget to close database connection 
            closeConnection(con);
        }
    }

    private void dropTables() {
        try {
            //Drop old tables
            PreparedStatement dropCheckinStatement = con.prepareStatement("" + "DROP TABLE Checkin" + "");
            dropCheckinStatement.execute();
            dropCheckinStatement.close();
            PreparedStatement dropReviewStatement = con.prepareStatement("" + "DROP TABLE Review" + "");
            dropReviewStatement.execute();
            dropReviewStatement.close();
            PreparedStatement dropUserStatement = con.prepareStatement("" + "DROP TABLE Yelp_User" + "");
            dropUserStatement.execute();
            dropUserStatement.close();
            /*PreparedStatement dropBCatToSubCategoryStatement = con.prepareStatement("" + "DROP TABLE B_category_To_Sub_Category"+ "");
             dropBCatToSubCategoryStatement.execute();
             dropBCatToSubCategoryStatement.close();
             PreparedStatement dropSubCategoryStatement = con.prepareStatement("" + "DROP TABLE Sub_Category"+ "");
             dropSubCategoryStatement.execute();
             dropSubCategoryStatement.close();
             PreparedStatement dropBtoBCategoryStatement = con.prepareStatement("" + "DROP TABLE Business_To_Business_Category"+ "");
             dropBtoBCategoryStatement.execute();
             dropBtoBCategoryStatement.close();*/
            PreparedStatement dropHoursStatement = con.prepareStatement("" + "DROP TABLE Hours" + "");
            dropHoursStatement.execute();
            dropHoursStatement.close();
            System.out.println("drop hours table");
            PreparedStatement dropBCategoryStatement = con.prepareStatement("" + "DROP TABLE Business_category" + "");
            dropBCategoryStatement.execute();
            dropBCategoryStatement.close();
            System.out.println("drop business_category table");
            PreparedStatement dropBusinessStatement = con.prepareStatement("" + "DROP TABLE Business" + "");
            dropBusinessStatement.execute();
            dropBusinessStatement.close();
            System.out.println("drop business table");
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBusinessTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Business\n"
                    + "(\n"
                    + "BusinessID varchar(25),\n"
                    + "Street varchar(100),\n"
                    + "City varchar(50),\n"
                    + "State varchar(50),\n"
                    + "Stars number,\n"
                    + "review_count int,\n"
                    + "BusinessName varchar(100),\n"
                    + "PRIMARY KEY(BusinessID)\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBCategoryTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Business_Category\n"
                    + "(\n"
                    + "BusinessID varchar(25),\n"
                    + "CategoryName varchar(200),\n"
                    + "SubCategoryName varchar(200),\n"
                    + "FOREIGN KEY(BusinessID) REFERENCES Business(BusinessID) ON DELETE CASCADE\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createHoursTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Hours\n"
                    + "(\n"
                    + "day varchar(50),\n"
                    + "open varchar(10),\n"
                    + "close varchar(10),\n"
                    + "BusinessID varchar(25),\n"
                    + "PRIMARY KEY(day,BusinessID),\n"
                    + "FOREIGN KEY(BusinessID) REFERENCES Business(BusinessID) ON DELETE CASCADE\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSubCategoryTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Sub_Category\n"
                    + "(\n"
                    + "SubCategoryID varchar(25),\n"
                    + "SubCategoryName varchar(250),\n"
                    + "PRIMARY KEY(SubCategoryID)\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBCatToSubCategoryTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE B_Category_To_Sub_Category\n"
                    + "(\n"
                    + "BusinessCategoryID varchar(25),\n"
                    + "SubCategoryID varchar(25),\n"
                    + "PRIMARY KEY(BusinessCategoryID,SubCategoryID),\n"
                    + "FOREIGN KEY(BusinessCategoryID) REFERENCES Business_category(BusinessCategoryID) ON DELETE CASCADE,\n"
                    + "FOREIGN KEY(SubCategoryID) REFERENCES sub_category(SubCategoryID) ON DELETE CASCADE\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBtoBCategoryTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Business_to_Business_Category\n"
                    + "(\n"
                    + "BusinessID varchar(25),\n"
                    + "BusinessCategoryID varchar(25),\n"
                    + "PRIMARY KEY(BusinessID,BusinessCategoryID),\n"
                    + "FOREIGN KEY(BusinessID) REFERENCES Business(BusinessID) ON DELETE CASCADE,\n"
                    + "FOREIGN KEY(BusinessCategoryID) REFERENCES Business_category(BusinessCategoryID) ON DELETE CASCADE\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUserTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Yelp_User\n"
                    + "(\n"
                    + "UserID varchar(25),\n"
                    + "Name varchar(255),\n"
                    + "YelpingSince date,\n"
                    + "AvgStars number,\n"
                    + "funnyVotes number,\n"
                    + "usefulVotes number,\n"
                    + "coolVotes number,\n"
                    + "NumOfFriends number,\n"
                    + "PRIMARY KEY(UserID)\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createGetTotalVotesFunction() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE OR REPLACE function getTotalVotes (funny in number, useful in number, cool in number)\n" +
                    "   RETURN NUMBER IS\n" +
                    "   BEGIN \n" +
                    "      RETURN funny + useful + cool;\n" +
                    "    END;\n" +
                    "/"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createReviewTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE Review\n"
                    + "(\n"
                    + "ReviewID varchar(25),\n"
                    + "FunnyVotes number,\n"
                    + "UsefulVotes number,\n"
                    + "CoolVotes number,\n"
                    + "PublishDate date,\n"
                    + "Stars number,\n"
                    + "TextualContent clob,\n"
                    + "UserID varchar(25),\n"
                    + "BusinessID varchar(25),\n"
                    + "PRIMARY KEY(ReviewID),\n"
                    + "FOREIGN KEY(UserID) REFERENCES yelp_user(UserID),\n"
                    + "FOREIGN KEY(BusinessID) REFERENCES Business(BusinessID) ON DELETE CASCADE\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCheckinTable() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(""
                    + "CREATE TABLE checkin\n"
                    + "(\n"
                    + "DayAndTime varchar(255),\n"
                    + "CheckinCount number,\n"
                    + "BusinessID varchar(25),\n"
                    + "FOREIGN KEY(BusinessID) REFERENCES Business(BusinessID) ON DELETE CASCADE\n"
                    + ")"
                    + "");
            preparedStatement.execute();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateHoursTable(JSONObject jsonObj, String t, String id) {
        //Set keysOfObject = jsonObj.keySet();
        int count = 0;
        for (Object key : jsonObj.keySet()) {
            try {
                //based on you key types
                String keyStr = (String) key;
                Object keyvalue = jsonObj.get(keyStr);

                //Print key and value
                System.out.println("key: " + keyStr + " value: " + keyvalue);
                String close = "";
                String open = "";

                //for hours values in business table
                if (keyvalue instanceof JSONObject) {
                    close = (String) ((JSONObject) keyvalue).get("close");
                    open = (String) ((JSONObject) keyvalue).get("open");
                }

                PreparedStatement preparedHoursStatement = con.prepareStatement("INSERT INTO Hours VALUES(?,?,?,?)");
 
                preparedHoursStatement.setString(1, keyStr);
                preparedHoursStatement.setString(2, open);
                preparedHoursStatement.setString(3, close);
                preparedHoursStatement.setString(4, id);
                preparedHoursStatement.execute();
                con.commit();
                preparedHoursStatement.close();
                //System.out.println("Hours table populated");
            } catch (SQLException ex) {
                Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("hours table error");
                System.out.println(Populate.class.getName());
            }
        }
    }

    public static void populateVotes(JSONObject jsonObj, PreparedStatement preparedUserStatement) {
        //Set keysOfObject = jsonObj.keySet();
        for (Object key : jsonObj.keySet()) {
            try {
                //based on you key types
                String keyStr = (String) key;
                Object keyvalue = jsonObj.get(keyStr);

                //Print key and value
                System.out.println("key: " + keyStr + " value: " + keyvalue);

                if (keyStr.equals("funny")) {
                    long funny = (long) keyvalue;
                    preparedUserStatement.setLong(5, funny);
                }
                if (keyStr.equals("useful")) {
                    long useful = (long) keyvalue;
                    preparedUserStatement.setLong(6, useful);
                }
                if (keyStr.equals("cool")) {
                    long cool = (long) keyvalue;
                    preparedUserStatement.setLong(7, cool);
                }
                //System.out.println("Hours table populated");
                con.commit();
            } catch (SQLException ex) {
                Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("vote fields error");
                System.out.println(Populate.class.getName());
            }
        }
    }

    public static void populateReviewVotes(JSONObject jsonObj, PreparedStatement preparedUserStatement) {
        //Set keysOfObject = jsonObj.keySet();
        for (Object key : jsonObj.keySet()) {
            try {
                //based on you key types
                String keyStr = (String) key;
                Object keyvalue = jsonObj.get(keyStr);

                //Print key and value
                System.out.println("key: " + keyStr + " value: " + keyvalue);

                if (keyStr.equals("funny")) {
                    long funny = (long) keyvalue;
                    preparedUserStatement.setLong(2, funny);
                }
                if (keyStr.equals("useful")) {
                    long useful = (long) keyvalue;
                    preparedUserStatement.setLong(3, useful);
                }
                if (keyStr.equals("cool")) {
                    long cool = (long) keyvalue;
                    preparedUserStatement.setLong(4, cool);
                }
                //System.out.println("Hours table populated");
                con.commit();
            } catch (SQLException ex) {
                Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("vote fields error");
                System.out.println(Populate.class.getName());
            }
        }
    }

    private static String getStreet(String s) {
        int l = s.indexOf("\n");
        if (l > 0) {
            return s.substring(0, l);
        }
        return "";
    }

    /*private static void populateMainCategories(){
     //Populate Business_category table
     String insertBcategoryQuery = "INSERT INTO Business_Category (BusinessID,CategoryName,SubCategoryName) VALUES (?,?,?)";
     PreparedStatement preparedBcategoryStatement = null;
        
     for (String mainCat : hm) {
     try {
                    
     preparedBcategoryStatement = con.prepareStatement(insertBcategoryQuery);
     preparedBcategoryStatement.setString(1, businessId);
     preparedBcategoryStatement.setString(2, mainCat);
     preparedBcategoryStatement.setString(3, subCat);
     preparedBcategoryStatement.execute();
     con.commit();
     preparedBcategoryStatement.close();
     } catch (SQLException e) {
     e.printStackTrace();
     }
     }
        
     Set set = hm.entrySet();
     Iterator iterator = set.iterator();
     for(String val: hm) {
     Map.Entry mentry = (Map.Entry)iterator.next();
     String businessID = (String) mentry.getKey();
     String businessCat = (String) mentry.getValue();
     System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
     System.out.println(mentry.getValue());
     try {	
     preparedBcategoryStatement = con.prepareStatement(insertBcategoryQuery);
     preparedBcategoryStatement.setString(1, businessID);
     preparedBcategoryStatement.setString(2, businessCat);
     preparedBcategoryStatement.setString(2, businessCat);
     preparedBcategoryStatement.execute();
     con.commit();
     preparedBcategoryStatement.close();
     } catch (SQLException e) {
     e.printStackTrace();
     }
     }    
     }*/
    private static synchronized void ReadJSON(File MyFile, String Encoding) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(MyFile, Encoding);
        ArrayList<JSONObject> json = new ArrayList<JSONObject>();

        while (scan.hasNext()) {
            JSONObject obj = (JSONObject) new JSONParser().parse(scan.nextLine());
            json.add(obj);
        }

        //Insert Business Json data into tables
        String insertBusinessQuery = "INSERT INTO Business (BusinessID,Street,City,State,Stars,review_count,BusinessName) VALUES (?,?,?,?,?,?,?)";
        String insertBcategoryQuery = "INSERT INTO Business_category (BusinessID,CategoryName,SubCategoryName) VALUES (?,?,?)";
        PreparedStatement preparedBusinessStatement = null;
        PreparedStatement preparedBcategoryStatement = null;
        //Statement s=null;

        int count = 0;
        int count1 = 0;
        //Here loop every Json Object
        for (JSONObject obj : json) {

            String businessId = (String) obj.get("business_id");
            String businessName = (String) obj.get("name");
            String fullAddress = (String) obj.get("full_address");
            String street = getStreet(fullAddress);
            String city = (String) obj.get("city");
            String state = (String) obj.get("state");
            double stars = (double) obj.get("stars");
            DecimalFormat onedigit = new DecimalFormat("#,##0.0");
            stars = Double.parseDouble(onedigit.format(stars));
            int reviewCount = (int) (long) obj.get("review_count");
            JSONObject daysOfOperation = (JSONObject) obj.get("hours");
            
            try {
                //Populate Business table
                preparedBusinessStatement = con.prepareStatement(insertBusinessQuery);
                preparedBusinessStatement.setString(1, businessId);
                preparedBusinessStatement.setString(2, street);
                preparedBusinessStatement.setString(3, city);
                preparedBusinessStatement.setString(4, state);
                preparedBusinessStatement.setDouble(5, stars);
                preparedBusinessStatement.setInt(6, reviewCount);
                preparedBusinessStatement.setString(7, businessName);
                
                System.out.println(businessId);
                preparedBusinessStatement.execute();
                //String query="INSERT INTO Business (BusinessID,Street,City,State,review_count,BusinessName) VALUES (\'"+businessId+"\',\'"+street+"\',\'"+city+"\',\'"+state+"\',"+reviewCount+",\'"+businessName+"\');";
                //System.out.println(query);
                //s.execute(query);
                
                System.out.println("Business DONE!");
                preparedBusinessStatement.close();
                populateHoursTable(daysOfOperation, "hours", businessId);
                con.commit();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ArrayList<String> catList = new ArrayList<String>();
            ArrayList<String> mainCategories = new ArrayList<String>();
            JSONArray jsonArray = (JSONArray) (obj.get("categories"));
            if (jsonArray != null) {
                int len = jsonArray.size();
                for (int i = 0; i < len; i++) {
                    if (hm.contains(jsonArray.get(i).toString())) {
                        mainCategories.add(jsonArray.get(i).toString());
                    } else {
                        catList.add(jsonArray.get(i).toString());
                    }
                }
            }

            //Populate Business_Category table
            try {

                for (String mainCat : mainCategories) {
                    for (String subCat : catList) {
                        preparedBcategoryStatement = con.prepareStatement(insertBcategoryQuery);
                        preparedBcategoryStatement.setString(1, businessId);
                        preparedBcategoryStatement.setString(2, mainCat);
                        preparedBcategoryStatement.setString(3, subCat);
                        preparedBcategoryStatement.execute();
                        preparedBcategoryStatement.close();
                        con.commit();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private static synchronized void ReadUserJSON(File MyFile, String Encoding) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(MyFile, Encoding);
        ArrayList<JSONObject> json = new ArrayList<JSONObject>();

        while (scan.hasNext()) {
            JSONObject obj = (JSONObject) new JSONParser().parse(scan.nextLine());
            json.add(obj);
        }

        //Insert Business Json data into tables
        String insertUserQuery = "INSERT INTO Yelp_User (UserID,Name,YelpingSince,AvgStars,funnyVotes,usefulVotes,coolVotes,NumOfFriends) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedUserStatement = null;

        try {
            PreparedStatement preparedSessionStatement = con.prepareStatement("Alter session set NLS_DATE_FORMAT = 'YYYY-MM'");
            preparedSessionStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        int count = 0;
        //Here loop every Json Object
        for (JSONObject obj : json) {

            String userId = (String) obj.get("user_id");
            String userName = (String) obj.get("name");
            double stars = (double) obj.get("average_stars");
            DecimalFormat onedigit = new DecimalFormat("#,##0.0");
            stars = Double.parseDouble(onedigit.format(stars));
            String yelpingSince = (String) obj.get("yelping_since");
            JSONObject votes = (JSONObject) obj.get("votes");
            JSONArray friends = (JSONArray) (obj.get("friends"));

            try {
                //Populate User table
                preparedUserStatement = con.prepareStatement(insertUserQuery);
                preparedUserStatement.setString(1, userId);
                preparedUserStatement.setString(2, userName);
                preparedUserStatement.setString(3, yelpingSince);
                preparedUserStatement.setDouble(4, stars);
                populateVotes(votes, preparedUserStatement);
                preparedUserStatement.setInt(8, friends.size());
                System.out.println("User ID: " + userId);
                preparedUserStatement.execute();
                preparedUserStatement.close();
                con.commit();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static synchronized void ReadReviewJSON(File MyFile, String Encoding) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(MyFile, Encoding);
        ArrayList<JSONObject> json = new ArrayList<JSONObject>();

        while (scan.hasNext()) {
            JSONObject obj = (JSONObject) new JSONParser().parse(scan.nextLine());
            json.add(obj);
        }

        //Insert Business Json data into tables
        String insertReviewQuery = "INSERT INTO Review (ReviewID,FunnyVotes,UsefulVotes,CoolVotes,PublishDate,Stars,TextualContent,UserID,BusinessID) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedReviewStatement = null;

        //Here loop every Json Object
        try {
            PreparedStatement preparedSession1Statement = con.prepareStatement("Alter session set NLS_DATE_FORMAT = 'YYYY-MM-DD'");
            preparedSession1Statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int count = 0;
        for (JSONObject obj : json) {

            String reviewId = (String) obj.get("review_id");
            String userId = (String) obj.get("user_id");
            String publishDate = (String) obj.get("date");
            String textualContent = (String) obj.get("text");

            long stars = (long) obj.get("stars");
            String businessId = (String) obj.get("business_id");
            JSONObject votes = (JSONObject) obj.get("votes");

            try {
                //Populate Business table
                if (preparedReviewStatement == null) {
                    preparedReviewStatement = con.prepareStatement(insertReviewQuery);
                }

                preparedReviewStatement.setString(1, reviewId);
                populateReviewVotes(votes, preparedReviewStatement);
                preparedReviewStatement.setString(5, publishDate);
                preparedReviewStatement.setDouble(6, stars);
                Clob c = con.createClob();
                c.setString(1, textualContent);
                preparedReviewStatement.setClob(7, c);
                preparedReviewStatement.setString(8, userId);
                preparedReviewStatement.setString(9, businessId);
                System.out.println("Inserted Review id " + reviewId + " record!");
                count++;
                preparedReviewStatement.addBatch();
                if (count > 200) {
                    count = 0;
                    preparedReviewStatement.executeBatch();
                    con.commit();
                    if (preparedReviewStatement != null) {
                        preparedReviewStatement.close();
                    }
                    preparedReviewStatement = null;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            preparedReviewStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void ReadCheckinJSON(File MyFile, String Encoding) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(MyFile, Encoding);
        ArrayList<JSONObject> json = new ArrayList<JSONObject>();

        while (scan.hasNext()) {
            JSONObject obj = (JSONObject) new JSONParser().parse(scan.nextLine());
            json.add(obj);
        }

        //Insert Business Json data into tables
        String insertCheckinQuery = "INSERT INTO Checkin (DayAndTime,CheckinCount,BusinessID) VALUES (?,?,?)";
        PreparedStatement preparedCheckinStatement = null;

        int count = 0;
        //Here loop every Json Object
        for (JSONObject obj : json) {

            JSONObject dayAndTime = (JSONObject) obj.get("checkin_info");
            System.out.println(dayAndTime);
            Set<String> keys = dayAndTime.keySet();
            for (String temp : keys) {
                try {
                    //System.out.println(temp);
                    //System.out.println(dayAndTime.get(temp));
                    preparedCheckinStatement = con.prepareStatement(insertCheckinQuery);
                    preparedCheckinStatement.setString(2, dayAndTime.get(temp).toString());
                    String[] hourday = temp.split("-");
                    Float fHrsDay = Integer.parseInt(hourday[1]) + Integer.parseInt(hourday[0]) / 48F;
                    preparedCheckinStatement.setFloat(1, fHrsDay);
                    preparedCheckinStatement.setString(3, obj.get("business_id").toString());
                    preparedCheckinStatement.execute();
                    con.commit();
                    preparedCheckinStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*private static String getKeyByValue(String value) {
     Set set = hm.entrySet();
     Iterator iterator = set.iterator();
     while(iterator.hasNext()) {
     Map.Entry mentry = (Map.Entry)iterator.next();
     if(value.equals((String) mentry.getValue())){
     return (String) mentry.getKey();
     }
     }
     return null;
     }*/
    private void navigateResult(Connection c) throws SQLException {
        /* 
         This will create a Statement that return ResultSets which is: 
         1. Scrollablle (can use ResultSet.previous() or ResultSet.absolute()) 
         2. Read-only (cannot call ResultSet.updateXXX() to change the content) 
         */
        Statement stmt = c.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        ResultSet r = stmt.executeQuery("SELECT * FROM info");
        ResultSetMetaData meta = r.getMetaData();

        System.out.print("\nFirst Tuple: ");
        r.next();
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            System.out.print("\"" + r.getString(col) + "\",");
        }

        System.out.print("\nNext Tuple: ");
        r.next();
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            System.out.print("\"" + r.getString(col) + "\",");
        }

        System.out.print("\nPrev. Tuple: ");
        r.previous();
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            System.out.print("\"" + r.getString(col) + "\",");
        }

        System.out.print("\n3rd Tuple: ");
        r.absolute(3);
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            System.out.print("\"" + r.getString(col) + "\",");
        }
    }

    private void showTableContent(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM info");

        /* 
         We use ResultSetMetaData.getColumnCount() to know the number columns 
         that are contained. 
         */
        ResultSetMetaData meta = result.getMetaData();
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            System.out.println("Column" + col + ": " + meta.getColumnName(col)
                    + "\t, Type: " + meta.getColumnTypeName(col));
        }

        /* 
         Every time we call ResultSet.next(), its internal cursor will advance 
         one tuple. By calling this method continuously, we can iterate through 
         the whole ResultSet. 
         */
        while (result.next()) {
            for (int col = 1; col <= meta.getColumnCount(); col++) {
                System.out.print("\"" + result.getString(col) + "\",");
            }
            System.out.println();
        }

        /* 
         It is always a good practice to close a statement as soon as we 
         no longer use it. 
         */
        stmt.close();
    }

    private ResultSet searchAllTuples(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        return stmt.executeQuery("SELECT * FROM info");
    }

    private void showMetaDataOfResultSet(ResultSet result) throws SQLException {
        ResultSetMetaData meta = result.getMetaData();
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            System.out.println("Column: " + meta.getColumnName(col)
                    + "\t, Type: " + meta.getColumnTypeName(col));
        }
    }

    private void showResultSet(ResultSet result) throws SQLException {
        ResultSetMetaData meta = result.getMetaData();
        int tupleCount = 1;
        while (result.next()) {
            System.out.print("Tuple " + tupleCount++ + " : ");
            for (int col = 1; col <= meta.getColumnCount(); col++) {
                System.out.print("\"" + result.getString(col) + "\",");
            }
            System.out.println();
        }
    }

    /**
     *
     * @return a database connection
     * @throws SQLException when there is an error when trying to connect
     * database
     * @throws ClassNotFoundException when the database driver is not found.
     */
    public static Connection openConnection() throws SQLException, ClassNotFoundException {
        // Load the Oracle database driver 
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

        String host = "akash-VirtualBox";
        String port = "1521";
        String dbName = "xe";
        String userName = "test";
        String password = "test";

        // Construct the JDBC URL 
        String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        return DriverManager.getConnection(dbURL, userName, password);
    }

    /**
     * Close the database connection
     *
     * @param con
     */
    public static void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }

}
