package co.edu.unbosque.seviceF2.services;

import co.edu.unbosque.serviciosf.model.UserApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserService {

    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String URL = "jdbc:postgresql://localhost/Laschiquistriquis";
    static final String USER = "postgres";
    static final String PASSWORD = "admin";

    public UserService(){}

    public UserApp addElement(String name, String email, String password, String role){
        UserApp response = new UserApp(name, email, password, role,0);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement()){
            Class.forName(JDBC_DRIVER);
            System.out.println("name = " + name + ", email = " + email + ", password = " + password + ", role = " + role);
            String query = "INSERT INTO userApp(name, email, password, role) VALUES ('" + name +"', '" + email +"', '" + password +"', '" + role +"')";
            String type="created";
            Date date = new Date();
            long mili = date.getTime();
            Timestamp time = new Timestamp(mili);
            String query2 = "INSERT INTO walletHistory(userapp, type, fcoins, registeredat) VALUES ('" + email +"', '" + type +"', '" + 0 +"', '" + time + "')";
            statement.execute(query);
            statement.executeQuery(query2);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
        return response;
    }



    public UserApp login(String username, String password){
        List <UserApp> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement()){
            Class.forName(JDBC_DRIVER);
            String query = "SELECT  * FROM userApp";
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                list.add(new UserApp(result.getString("name"),
                                     result.getString("email"),
                                     result.getString("password"),
                                     result.getString("role")));
            }
            return list.stream()
                       .peek(x -> System.out.println(x))
                       .filter(x -> x.getName().equals(username) && x.getPassword().equals(password))
                       .findFirst()
                       .orElse(new UserApp());

        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
        return new UserApp();
    }

    public List<UserApp> getUsers() {
        List <UserApp> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement()){
            Class.forName(JDBC_DRIVER);
            String query = "SELECT  * FROM userApp";
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                list.add(new UserApp(result.getString("name"),
                       result.getString("email"),
                        result.getString("password"),
                        result.getString("role")));
            }
            } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return list;
    }


}
