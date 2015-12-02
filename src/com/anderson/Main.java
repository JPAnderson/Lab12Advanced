package com.anderson;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Main {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/vet";
    static final String USER = "root";
    static final String PASS = "team13";

    public static void main(String[] args) {

        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;

        PreparedStatement preparedInsert = null;
        LinkedList<Statement> allStatements = new LinkedList<Statement>();

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe){
            System.out.println("Can't find SQL driver");
            System.exit(-1);
        }

        try {
            connection = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASS);
            statement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS bee_hives (hive_ID int, year_collected int, honey_harvested int)";
            statement.executeUpdate(createTable);
        } catch(SQLException sqle){
            System.out.println("Something went wrong");
            sqle.printStackTrace();
        }

        try {
            String preparedStatement = "INSERT INTO bee_hives VALUES (?, ?, ? )";
            preparedInsert = connection.prepareStatement(preparedStatement);
            allStatements.add(preparedInsert);
        } catch (SQLException sqle){
            System.out.println("Something went wrong");
        }
        boolean continuing = true;

        /*****************Start of menu***********************/

        System.out.println("Bee Hive Database Manager");
        while(continuing) {
            try {
                System.out.println("Main Menu. Please choose a selection");
                System.out.println("1. Make a new entry");
                System.out.println("2. Display total honey collected for x year");
                System.out.println("3. Display x hive's all time honey");
                System.out.println("4. Display x hive's best collection year");
                System.out.println("5. Display differences from last year");
                System.out.println("6. Display the collection years in ranked order");
                System.out.println("7. Display the Highest Producing hive");
                System.out.println("8. Display all entries");
                System.out.println("9. DELETE ALL ENTRIES");
                System.out.println("10. Exit");
                Scanner s = new Scanner(System.in);
                int selection = s.nextInt();


                switch (selection) {

                    /***************Add new Entry****************/
                    case 1:

                        System.out.println("Please enter the hive number");
                        int hiveID = s.nextInt();
                        System.out.println("Please enter the year of honey collection");
                        int honeyCollectionDate = s.nextInt();
                        System.out.println("Please enter the lbs of honey collected");
                        double weightOfHoneyCollected = s.nextDouble();
                        String addToTable = "INSERT INTO bee_hives VALUES (" + hiveID + ", '" + honeyCollectionDate + "' ," + weightOfHoneyCollected + ")";
                        statement.executeUpdate(addToTable);

                        break;

                    /***************Display hive's total honey for one year*****************/
                    case 2:
                        int totalHoney = 0;

                        System.out.println("Please select a year");
                        int yearPicked = s.nextInt();
                        String tableSearch = "SELECT * FROM bee_hives WHERE year_collected=" + yearPicked;
                        try {
                            resultSet = statement.executeQuery(tableSearch);
                            while (resultSet.next()) {
                                int honeyToAdd = resultSet.getInt("honey_harvested");
                                totalHoney = totalHoney + honeyToAdd;
                            }
                        } catch (SQLException sqle) {
                            System.out.println("Something went wrong");
                            sqle.printStackTrace();
                        }
                        System.out.println("The total honey for " + yearPicked + " is " + totalHoney + "lbs.");
                        s.reset();
                        break;

                    /*****************Display x hive's all time honey***********/
                    case 3:
                        int hivesHoney = 0;

                        System.out.println("Select a hive's honey to view");
                        int hivePicked = s.nextInt();
                        String hiveSearch = "SELECT * FROM bee_hives WHERE hive_ID=" + hivePicked;

                            resultSet = statement.executeQuery(hiveSearch);
                            while (resultSet.next()) {
                                int honey = resultSet.getInt("honey_harvested");
                                hivesHoney += honey;
                            }

                        System.out.println("the total honey for " + hivePicked + " is " + hivesHoney + " lbs");
                        break;

                    /****************Display x hive's best collection year**************/
                    case 4:

                        System.out.println("Select a hive to view");
                        int hivePicked2 = s.nextInt();
                        String hiveSearch2 = "SELECT MAX(honey_harvested) FROM bee_hives WHERE hive_ID=" + hivePicked2;

                        resultSet = statement.executeQuery(hiveSearch2);
                        while(resultSet.next()){
                            int maxReturned = resultSet.getInt(1);
                            String getBestYear = "SELECT year_collected FROM bee_hives WHERE honey_harvested=" + maxReturned;
                            resultSet = statement.executeQuery(getBestYear);
                            while(resultSet.next()){
                                int finalBestYear = resultSet.getInt("year_collected");
                                System.out.println("Hive " + hivePicked2 + "'s best year was " + finalBestYear);
                            }

                        }

                        break;

                    /**************Display differences from last year*****************/
                //parse ints instead
                    case 5:
                        try {
                            System.out.println("Select a hive for comparison");
                            String parseToHiveCompare1 = s.nextLine();
                            int hiveCompare1 = Integer.parseInt(parseToHiveCompare1);

                            System.out.println("Select a second hive for comparison");
                            String pareToHiveCompare2 = s.nextLine();
                            int hiveCompare2 = Integer.parseInt(pareToHiveCompare2);

                            System.out.println("Select a year for the first hive");
                            String parseToHive1Year = s.nextLine();
                            int hive1Year = Integer.parseInt(parseToHive1Year);

                            System.out.println("Select a year for the second hive");
                            String parseToHive2Year = s.nextLine();
                            int hive2year = Integer.parseInt(parseToHive2Year);

                            String systemSearch = "SELECT * FROM bee_hives WHERE hive_ID = " + hiveCompare1 + " AND year_collected=" + hive1Year;
                            resultSet = statement.executeQuery(systemSearch);
                            if (resultSet.next()) {
                                int year1Result = resultSet.getInt("honey_harvested");
                                System.out.println("First hive's honey output for " + hive1Year + ": " + year1Result);
                            }

                            String systemSearch2 = "SELECT * FROM bee_hives WHERE hive_ID=" + hiveCompare2 + " AND year_collected=" + hive2year;
                            resultSet = statement.executeQuery(systemSearch2);
                            if (resultSet.next()) {
                                int year2Result = resultSet.getInt("honey_harvested");
                                System.out.println("Second hive's honey output for " + hive2year + ": " + year2Result + "\n");
                            }
                            s.close();
                            break;
                        } catch (NoSuchElementException nsee){
                            System.out.println("");
                            break;
                        }

                    /**************Display collection years in ranked order*****************/
                    case 6:
                        String searchRanked = "SELECT COUNT(year_collected) FROM bee_hives";
                        resultSet =statement.executeQuery(searchRanked);
                        while(resultSet.next()){

                        }
                        break;

                    case 8:
                        System.out.println("All entries: ");
                        String getAllEntries = "SELECT * FROM bee_hives ";
                        resultSet = statement.executeQuery(getAllEntries);
                        while(resultSet.next()){
                            int hive = resultSet.getInt("hive_ID");
                            System.out.println("Hive: " + hive);

                            int yearCollected = resultSet.getInt("year_collected");
                            System.out.println("Year: " + yearCollected);

                            int honeyCollected = resultSet.getInt("honey_harvested");
                            System.out.println("Pounds collected: " + honeyCollected + "\n");
                        }
                        break;


                    /*************Delete all entries*****************/
                    case 9:
                        System.out.println("This will clear all entries, irreversibly. Are you positive? [y/n]");
                        Scanner scanner = new Scanner(System.in);
                        String confirm = scanner.nextLine();

                        if(confirm.equals("y")){
                            String deleteAll = "DELETE FROM bee_hives";
                            statement.executeUpdate(deleteAll);
                            System.out.println("All entries deleted");
                            scanner.close();
                            break;
                        } else {
                            System.out.println("No entries deleted");
                            scanner.close();
                            break;

                        }

                    /********************Quit Program*******************/
                    case 10:
                        System.out.println("Goodbye!");
                        System.exit(0);

                        /*********************** End of menu and programs*****************/

                }
            } catch (SQLException sqle){
                System.out.println("Something went wrong");
                sqle.printStackTrace();
            } catch (NoSuchElementException nsee){
                System.out.println("");
                Scanner exceptionScanner = new Scanner(System.in);
                exceptionScanner.next();
            }
        }
    }
}