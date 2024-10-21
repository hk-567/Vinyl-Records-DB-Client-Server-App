/*
 * RecordsDatabaseService.java
 *
 * The service threads for the records database server.
 * This class implements the database access service, i.e. opens a JDBC connection
 * to the database, makes and retrieves the query, and sends back the result.
 *
 * author: <YOUR STUDENT ID HERE>
 *
 */

import java.io.*;
//import java.io.OutputStreamWriter;

import java.net.Socket;

import java.sql.*;
import javax.sql.rowset.*;
//Direct import of the classes CachedRowSet and CachedRowSetImpl will fail becuase
//these clasess are not exported by the module. Instead, one needs to impor
//javax.sql.rowset.* as above.



public class RecordsDatabaseService extends Thread{

    private Socket serviceSocket = null;
    private String[] requestStr  = new String[2]; //One slot for artist's name and one for recordshop's name.
    private ResultSet outcome  = null;

    //JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL      = Credentials.URL;



    //Class constructor
    public RecordsDatabaseService(Socket aSocket){

        //TO BE COMPLETED
        this.serviceSocket= aSocket;

    }


    //Retrieve the request from the socket
    public String[] retrieveRequest()
    {
        this.requestStr[0] = ""; //For artist
        this.requestStr[1] = ""; //For recordshop

        String tmp = "";
        try {

            //TO BE COMPLETED
            InputStream is = serviceSocket.getInputStream();
            StringBuilder message = new StringBuilder();
            int character;
            while ((character = is.read())!= -1){
                if(character=='#'){
                    break; // found termin char
                }
                message.append((char) character);
            }
            String[] parts = message.toString().split(";");
            if (parts.length == 2){
                this.requestStr[0]=parts[0];
                this.requestStr[1]=parts[1];

            }

        }catch(IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        return this.requestStr;
    }


    //Parse the request command and execute the query
    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;

        this.outcome = null;

        String sql = "SELECT r.title, r.label, r.genre, r.rrp, COUNT(rc.copyID) AS copies " +
                "FROM record r " +
                "JOIN artist a ON r.artistID = a.artistID " +
                "JOIN recordcopy rc ON r.recordID = rc.recordID " +
                "JOIN recordshop rs ON rc.recordshopID = rs.recordshopID " +
                "WHERE (a.lastname = ? OR a.lastname IS NULL) " +
                "AND rs.city = ? " +
                "GROUP BY r.title, r.label, r.genre, r.rrp " +
                "HAVING COUNT(rc.copyID) > 0"; //TO BE COMPLETED- Update this line as needed.


        try {

            //Connet to the database
            //TO BE COMPLETED
            Connection connection = DriverManager.getConnection(URL, USERNAME,PASSWORD );
            //Make the query
            //TO BE COMPLETED
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, requestStr[0]); // artist's last name
            statement.setString(2, requestStr[1]); // record shop's city)

            ResultSet resultSet = statement.executeQuery();
            //Process query
            //TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();
            cachedRowSet.populate(resultSet);
            cachedRowSet.beforeFirst();
            this.outcome = cachedRowSet;
            //Clean up
            //TO BE COMPLETED
            resultSet.close();
            statement.close();
            connection.close();

        }
        catch (Exception e)
        { System.out.println(e); }

        return flagRequestAttended;
    }



    //Wrap and return service outcome
    public void returnServiceOutcome(){
        try {
            //Return outcome
            //TO BE COMPLETED
            ObjectOutputStream oos = new ObjectOutputStream(serviceSocket.getOutputStream());
            oos.writeObject(outcome);

            oos.flush();
            oos.close();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket
            //TO BE COMPLETED
            serviceSocket.close();



        }catch (IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }


    //The service thread run() method
    public void run()
    {
        try {
            System.out.println("\n============================================\n");
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
                    + "artist->" + this.requestStr[0] + "; recordshop->" + this.requestStr[1]);

            //Attend the request
            boolean tmp = this.attendRequest();

            //Send back the outcome of the request
            if (!tmp)
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            this.returnServiceOutcome();

        }catch (Exception e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
