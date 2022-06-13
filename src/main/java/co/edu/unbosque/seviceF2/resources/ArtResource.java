package co.edu.unbosque.seviceF2.resources;

import co.edu.unbosque.serviciosf.model.Art;
import co.edu.unbosque.serviciosf.model.ExceptionMessage;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Map;



@Path("/users/arts")
public class ArtResource {
        @Context
        ServletContext context;
        Connection conn = null;

        PreparedStatement prestmt = null;
        Statement stmt = null;

        Art obra = null;
        List<Art> listaObras;
        static final String JDBC_DRIVER = "org.postgresql.Driver";
        static final String DB_URL = "jdbc:postgresql://localhost:5432/Laschiquistriquis";

        // Database credentials
        static final String USER = "postgres";
        static final String PASS = "admin";

        private final String UPLOAD_DIRECTORY= File.separator;

    @POST
    @Path("/{email}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(MultipartFormDataInput input,
                               @PathParam("email") String email
    ) {

        String fileName = "";
        Response response = null;
        System.out.println("entro");
        String rutaFinal="";
        try{

            Map<String, List<InputPart>> formParts = input.getFormDataMap();
            String title = formParts.get("nombre").get(0).getBodyAsString();
            String price = formParts.get("precio").get(0).getBodyAsString();
            String currentCollection = formParts.get("colectionid").get(0).getBodyAsString();
            List<InputPart> inputParts = formParts.get("imagen");

            String theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvwxyz";
            StringBuilder builder;

            builder = new StringBuilder(16);

            for (int m = 0; m < 16; m++) {
                int myindex = (int)(theAlphaNumericS.length() * Math.random());

                builder.append(theAlphaNumericS.charAt(myindex));
            }
            String newFileName = builder.toString();

            for (InputPart inputPart : inputParts) {
                if (fileName.equals("") || fileName == null) {
                    // Retrieving headers and reading the Content-Disposition header to file name
                    MultivaluedMap<String, String> headers= (MultivaluedMap<String, String>)
                            inputPart.getHeaders();

                    fileName = parseFileName(headers);
                }

                String format = fileName.split("\\.")[1];

                newFileName += "." + format;
            }

            for (InputPart inputPart : inputParts){
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                saveFile(inputStream,newFileName,currentCollection,context);
            }
            System.out.println(rutaFinal);
            obra = new Art(currentCollection,title,email,Integer.parseInt(price),0,currentCollection+File.separator+newFileName,0) ;
            System.out.println(obra.getTitle());

            if (obra != null){
                response = agregarABase();

            }



        }catch (IOException e) {
            e.printStackTrace();
            response = Response.status(404)
                    .entity(new ExceptionMessage(404, "Error creando obra"))
                    .build();
        }

        return response;
    }

    private String parseFileName(MultivaluedMap<String, String> headers) {
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                String fileName = tmp[1].trim().replaceAll("\"","");
                return fileName;
            }
        }

        return "unknown";
    }

    private String saveFile(InputStream uploadedInputStream, String fileName, String currentCollection, ServletContext context) {
        int read = 0;
        byte[] bytes = new byte[1024];
        String uploadPath = "";
        try {
            // Complementing servlet path with the relative path on the server
            uploadPath = context.getRealPath("") + UPLOAD_DIRECTORY+currentCollection;

            // Creating the upload folder, if not exist
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) uploadDir.mkdir();

            // Persisting the file by output stream
            OutputStream outpuStream = new FileOutputStream(uploadPath + File.separator+fileName);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }

            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadPath;
    }
    public Response agregarABase(){
        Response response = null;
        String email_encontrado = "";
        int id_collection = -1;
        PreparedStatement prestmt2=null;
        PreparedStatement prestmt3=null;
        PreparedStatement prestmt4=null;



        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("intento conectarme a la base de datos");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("=> consulting user..."+obra.getAuthor());


            String sql = "SELECT * FROM userapp u WHERE u.email = '" + obra.getAuthor() + "'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                email_encontrado = rs.getString("email");
            }
            System.out.println(email_encontrado);

            if (email_encontrado != ""){
                System.out.println("entro a crear colleccion");
                String sql2="INSERT INTO collection (name,userapp,description ,category) VALUES(?,?,?,?)";
                prestmt2 = conn.prepareStatement(sql2);
                prestmt2.setString(1,obra.getCollection());
                prestmt2.setString(4,"default");
                prestmt2.setString(3,"default");
                prestmt2.setString(2,email_encontrado);
                prestmt2.executeUpdate();
                prestmt2.close();
            }
            else {
                response = Response.status(404)
                        .entity(new ExceptionMessage(404, "User not found"))
                        .build();
            }
            rs.close();
            //consulting collection
            System.out.println("consulto collection");
            String sql4 = "SELECT * FROM collection u WHERE u.name = ? AND u.userapp = ?";
            prestmt4 = conn.prepareStatement(sql4);
            prestmt4.setString(1,obra.getCollection());
            prestmt4.setString(2,email_encontrado);
            rs = prestmt4.executeQuery();
            while (rs.next()){
                id_collection = rs.getInt("id");
            }
            if (id_collection!= -1){
                String sql3 = "INSERT INTO art (name,price,imagepath,forsale,collection)" +
                        "VALUES (?,?,?,?,?)";
                prestmt3 = conn.prepareStatement(sql3);
                prestmt3.setString(1, obra.getTitle());
                prestmt3.setInt(2,obra.getPrice());
                prestmt3.setString(3, obra.getFile());
                prestmt3.setBoolean(4,true);
                prestmt3.setInt(5,id_collection);
                prestmt3.executeUpdate();
                prestmt3.close();
                response = Response.ok().entity(obra).build();
            }
            else{
                response = Response.status(404)
                        .entity(new ExceptionMessage(404, "no cree coleccion"))
                        .build();
            }
            rs.close();
            prestmt4.close();
            stmt.close();
            conn.close();
            //creating art
//            String sql3 = "INSERT INTO arts_table(art_name,price,file,collection_name,id_user)" +
//                    "VALUES (?,?,?,?,?)";
//            prestmt3 = conn.prepareStatement(sql3);
//            prestmt3.setString(1, obra.getTitle());
//            prestmt3.setInt(2,obra.getPrice());
//            prestmt3.setString(3, obra.getFile());
//            prestmt3.setString(4,obra.getCollection());
//            prestmt3.setInt(5,id_encontrado);
//            prestmt3.executeUpdate();
//            prestmt3.close();
//            conn.close();
        }catch (SQLException se) {
            se.printStackTrace();
            try {
                String sql3 = "INSERT INTO art (name,price,imagepath,forsale,collection)" +
                        "VALUES (?,?,?,?,?)";
                prestmt3 = conn.prepareStatement(sql3);
                prestmt3.setString(1, obra.getTitle());
                prestmt3.setInt(2,obra.getPrice());
                prestmt3.setString(3, obra.getFile());
                prestmt3.setBoolean(4,true);
                prestmt3.setInt(5,id_collection);
                prestmt3.executeUpdate();
                prestmt3.close();
                conn.close();
                response = Response.ok().entity(obra).build();
            } catch (SQLException e) {
                e.printStackTrace();
                response = Response.serverError().build();
            }

        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
            response = Response.serverError().build();

        }finally {
            try {
                if (prestmt != null) prestmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Art> getArtsImage(){
        System.out.println("entro a obtener obras");
        String autor = "";
        String collection_name = "";
        Response response = null;
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("intento conectarme a la base de datos");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("=> consulting arts...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM art";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Extracting row values by column name
                String art_name = rs.getString("name");
                int price = rs.getInt("price");
                String file = rs.getString("imagepath");
                boolean forsale = rs.getBoolean("forsale");
                int id_collection = rs.getInt("id_collection");
                int id_art = rs.getInt("id_art");
                System.out.println(art_name);

                String sql2 = "SELECT * FROM collection u WHERE u.id_collection =?";
                prestmt = conn.prepareStatement(sql2);
                prestmt.setInt(1,id_collection);
                ResultSet rs2 = prestmt.executeQuery();
                while(rs2.next()){
                    autor = rs2.getString("email");
                    collection_name = rs2.getString("name");
                }
                System.out.println("lei autor");
                // Creating a new UserApp class instance and adding it to the array list
                prestmt.close();
                rs2.close();
                Art agregarObra = new Art(collection_name,art_name,autor,price,0,file,id_art);
                listaObras.add(agregarObra);
                System.out.println(agregarObra.getTitle());
            }
            rs.close();
            stmt.close();
            conn.close();
            if (listaObras != null){
                response = Response.ok().entity(listaObras).build();
            }
            response = Response.status(404)
                    .entity(new ExceptionMessage(404, "User not found"))
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.serverError().build();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response = Response.serverError().build();
        }
        finally {
            try {
                if (prestmt != null) prestmt.close();
                if (conn != null) conn.close();
                if(stmt!=null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return listaObras;
    }


  }