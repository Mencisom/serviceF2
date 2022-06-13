package co.edu.unbosque.seviceF2.services;


import co.edu.unbosque.serviciosf.model.Collection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionService {
    private Connection conn;



    public CollectionService(Connection conn) {
        this.conn = conn;
    }

    /*public void updatecolection(Collection colecction) {
        // Object for handling SQL statement
        PreparedStatement stmt = null;

        try {

            // Executing a SQL query
            System.out.println("=> Updating owner...");
            stmt = this.conn.prepareStatement("UPDATE colecction SET precio = ? WHERE email = ?");
            stmt.setString(2, colecction.getEmail());
            System.out.println(colecction.getEmail()+ " linae 29");
            int rowsUpdated = stmt.executeUpdate(); // executeUpdate is also used for inserting records

            // Printing results
            System.out.println("Rows updated: " + rowsUpdated + "\n");

            // Closing resources
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace(); // Handling errors from database
        } finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }*/
    public Connection connect() throws SQLException {
        String url="jdbc:postgresql://localhost/Laschiquistriquis";
        String user="postgres";
        String password="admin";
        return DriverManager.getConnection(url, user, password);
    }
    public void crearcoleccion (Collection coleccion){
        String SQL= "INSERT INTO colecction(id,name,userapp,description,category) VALUES(?,?,?,?)";
        long id=0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, coleccion.getColecctionid());
            pstmt.setString(2, coleccion.getTitulo());
            pstmt.setString(3, coleccion.getEmail());
            pstmt.setString(4,null);
            pstmt.setString(5,null);
            int affectedRows = pstmt.executeUpdate();

            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public List<Collection> listacolection(){
        Statement stmt=null;

        List<Collection> art=new ArrayList<Collection>();

        try {
            // Executing a SQL query
            System.out.println("=> Listing colection...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM colecction";
            ResultSet rs = stmt.executeQuery(sql);

            // Reading data from result set row by row
            while (rs.next()) {
                // Extracting row values by column name
                int colecctionid= rs.getInt("id");
                String titulo = rs.getString("name");
                String email= rs.getString("userapp");
                String desciption= rs.getString("desciption");

                // Creating a new UserApp class instance and adding it to the array list
                Collection coleccion_n=new Collection();
                coleccion_n.setColecctionid(colecctionid);
                coleccion_n.setEmail(email);
                coleccion_n.setTitulo(titulo);
                coleccion_n.setDescription(desciption);
                art.add(coleccion_n);

            }

            // Printing results
            System.out.println("titulo| fcoins| email| colection");
            for (Collection col : art) {
                System.out.println(col.getTitulo()+" |  | "+col.getEmail()+" | "+col.getColecctionid());
            }

            // Printing total rows
            System.out.println("Total of users retrieved: " + art.size() + "\n");

            // Closing resources
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace(); // Handling errors from database
        } finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return art;
    }
}

