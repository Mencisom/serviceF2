package co.edu.unbosque.seviceF2.model;

public class Collection {
    private String description;
    private String email;
    private String titulo;
    private int colecctionid;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getColecctionid() {
        return colecctionid;
    }

    public void setColecctionid(int colecctionid) {
        this.colecctionid = colecctionid;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", titulo='" + titulo + '\'' +
                ", colecctionid=" + colecctionid +
                '}';
    }
}
