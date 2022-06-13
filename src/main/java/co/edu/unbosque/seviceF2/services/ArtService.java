package co.edu.unbosque.seviceF2.services;

import co.edu.unbosque.serviciosf.model.Art;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtService {

    private Connection conn;

    public ArtService(Connection conn) {
        this.conn = conn;
    }
    public Connection connect() throws SQLException {
        String url="jdbc:postgresql://localhost/Laschisquistriquis";
        String user="postgres";
        String password="admin";
        return DriverManager.getConnection(url, user, password);
    }

    public void updateobra(Art obra) {
        // Object for handling SQL statement
        PreparedStatement stmt = null;

        try {

            // Executing a SQL query
            System.out.println("=> Updating obra...");
            stmt = this.conn.prepareStatement("UPDATE obra  SET owner=? WHERE pieceid = ?");

            stmt.setInt(2, obra.getId_art());

            stmt.setString(1, obra.getAuthor());


            System.out.println(obra.getId_art()+ " linae 78");

            System.out.println(obra.getAuthor()+ " linae 82");




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
    }

    public List<Art> listaobra(){
        Statement stmt=null;

        List<Art> art=new ArrayList<Art>();

        try {
            // Executing a SQL query
            System.out.println("=> Listing obra...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Art";
            ResultSet rs = stmt.executeQuery(sql);

            // Reading data from result set row by row
            while (rs.next()) {
                // Extracting row values by column name
                Integer colecctionid=rs.getInt("colecctionid");
                Integer pieceid= rs.getInt("pieceid");
                String  imagen= rs.getString("imagen");
                String titulo=rs.getString("titulo");
                String precio=rs.getString("precio");
                String owner=rs.getString("owner");


                // Creating a new UserApp class instance and adding it to the array list
                Art obra_n=new Art();
                obra_n.setAuthor(owner);
                obra_n.setPrice(Integer.parseInt(precio));
                obra_n.setCollection(String.valueOf(colecctionid));
                obra_n.setFile(imagen);
                obra_n.setTitle(titulo);
                obra_n.setId_art(pieceid);
                art.add(obra_n);

            }

            // Printing results
            System.out.println("colcctionid | precio |imgen|titulo");
            for (Art obra : art) {
               // System.out.println(obra.getCollection()+" | "+obra.getId()+" | "+obra.getImg()+" | "+obra.getTitle());
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
    public void updateobra1(Art obra) {
        // Object for handling SQL statement
        PreparedStatement stmt = null;

        try {

            // Executing a SQL query
            System.out.println("=> Updating obra...");
            stmt = this.conn.prepareStatement("UPDATE Art  SET price=? WHERE pieceid = ?");

            stmt.setDouble(2, Double.parseDouble(String.valueOf(obra.getPrice())));

            stmt.setString(1, obra.getAuthor());


            System.out.println(obra.getPrice()+ " linae 78");

            System.out.println(obra.getAuthor()+ " linae 82");




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
    }

    public Art  listaobra1(String name) throws SQLException {
        Statement stmt = null;

        List<Art> art = new ArrayList<Art>();

        ResultSet rs = null;
        try {
            // Executing a SQL query
            System.out.println("=> Listing obra...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Art";
            rs = stmt.executeQuery(sql);

            // Reading data from result set row by row
            while (rs.next()) {
                // Extracting row values by column name
                Integer colecctionid = rs.getInt("colecctionid");
                Integer pieceid = rs.getInt("pieceid");
                String imagen = rs.getString("imagen");
                String titulo = rs.getString("titulo");
                String precio = rs.getString("precio");
                String owner = rs.getString("owner");


                // Creating a new UserApp class instance and adding it to the array list
                Art obra_n = new Art();
                obra_n.setAuthor(owner);
                obra_n.setPrice(Integer.parseInt(precio));
                obra_n.setCollection(String.valueOf(colecctionid));
                obra_n.setFile(imagen);
                obra_n.setTitle(titulo);
                obra_n.setId_art(pieceid);
                art.add(obra_n);

            }
            return art.stream()
                    .peek(x -> System.out.println(x))
                    .filter(x -> x.getTitle().equals(name))
                    .findFirst()
                    .orElse(new Art());

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
                rs.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
         return new Art();
    }

}
