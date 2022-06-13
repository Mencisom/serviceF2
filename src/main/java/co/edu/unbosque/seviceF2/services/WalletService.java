package co.edu.unbosque.seviceF2.services;

import co.edu.unbosque.serviciosf.model.Art;
import co.edu.unbosque.serviciosf.model.UserApp;
import co.edu.unbosque.serviciosf.model.Wallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WalletService {
    public WalletService() {
    }

    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String URL = "jdbc:postgresql://localhost/Laschiquistriquis";
    static final String USER = "postgres";
    static final String PASSWORD = "admin";

    public Wallet wlist(String email) {
        List<Wallet> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement()) {
            Class.forName(JDBC_DRIVER);
            String query = "SELECT  * FROM wallethistory";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                list.add(new Wallet(result.getString("userapp"),
                        result.getString("type"),
                        result.getFloat("focins")));
            }
            return list.stream()
                    .peek(x -> System.out.println(x))
                    .filter(x -> x.getUserApp().equals(email))
                    .findFirst()
                    .orElse(new Wallet());

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return new Wallet();
    }

    public Optional<Boolean> sell(String username, String fcoins, String nameart) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            List<UserApp> users = new UserService().getUsers();
            UserApp user1 = users.stream().filter(user -> username.equals(user.getName())).findFirst().orElse(null);
            String email = user1.getEmail();
            Wallet wallets = wlist(email);
            float Fcoins = wallets.getFcois() - Float.parseFloat(fcoins);
            String type = "buy";
            Date date = new Date();
            long mili = date.getTime();
            Timestamp time = new Timestamp(mili);
            if (Fcoins > 0) {
                String query = "UPDATE wallethistory SET fcoins = '" + Fcoins + "' WHERE userapp = '" + email + "'";
                stmt.execute(query);
                ArtService service = new ArtService(conn);
                Art arts = service.listaobra1(nameart);
                service.updateobra(arts);

            } else {
                return Optional.of(false);
            }

            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Optional.of(true);

    }

    public void sold(String artname, String Fcoins) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            List<Art> arts = new ArtService(conn).listaobra();
            Art user1 = arts.stream().filter(user -> artname.equals(user.getTitle())).findFirst().orElse(null);
            String User1 = user1.getAuthor();
            List<UserApp> user = new UserService().getUsers();
            UserApp us = user.stream().filter(use -> User1.equals(use.getName())).findFirst().orElse(null);
            String email = us.getEmail();
            Wallet wallets = wlist(email);
            float numFcoins = Float.parseFloat(Fcoins);
            String type = "sale";
            Date date = new Date();
            long mili = date.getTime();
            Timestamp time = new Timestamp(mili);
            float fcoins = numFcoins + wallets.getFcois();
            if (fcoins >= 0) {
                String query = "UPDATE wallethistory SET fcoins = '" + fcoins + "' WHERE userapp = '" + email + "'";
                stmt.execute(query);
            } else {

                stmt.executeUpdate();

                stmt.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}