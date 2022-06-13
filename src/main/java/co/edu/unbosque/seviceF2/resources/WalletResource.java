package co.edu.unbosque.seviceF2.resources;

import co.edu.unbosque.serviciosf.model.UserApp;
import co.edu.unbosque.serviciosf.services.UserService;
import co.edu.unbosque.serviciosf.services.WalletService;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


@Path("/wallet")
public class WalletResource {
    @Context
    ServletContext context;


    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/Laschiquistriquis";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "admin";
    @PUT
    @Path("/buy")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyArt(@FormParam("userBuyer") String userBuyer,
                           @FormParam("price") String price,
                           @FormParam("userSeller") String userSeller,
                           @FormParam("name_art") String name_art) {

        //cambiar metodo
        boolean buy = new WalletService().sell(userBuyer, price, name_art).get();

        Connection conn = null;

        if (buy) {
            new WalletService().sold(userSeller, price);

            List<UserApp> users = null;
            try {
                Class.forName(JDBC_DRIVER);

                // Opening database connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                users = new UserService().getUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                // Cleaning-up environment
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }

            UserApp userfound = users.stream().filter(user -> userBuyer.equals(user.getName())).findFirst().orElse(null);
            if (userfound != null) {
                return Response.ok().entity(userfound).build();
            }

        }
        return Response.serverError().build();
    }
}
