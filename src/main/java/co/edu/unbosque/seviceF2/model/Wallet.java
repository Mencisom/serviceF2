package co.edu.unbosque.seviceF2.model;

public class Wallet {

    private Integer idNumer;
    private String userApp;
    private String type;
    private Float fcois;

    public Wallet() {
    }

    public Wallet(String userApp, String type, Float fcois) {
        this.idNumer = idNumer;
        this.userApp = userApp;
        this.type = type;
        this.fcois = fcois;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "idNumer=" + idNumer +
                ", userApp='" + userApp + '\'' +
                ", type='" + type + '\'' +
                ", fcois=" + fcois +
                '}';
    }

    public Integer getIdNumer() {
        return idNumer;
    }

    public void setIdNumer(Integer idNumer) {
        this.idNumer = idNumer;
    }

    public String getUserApp() {
        return userApp;
    }

    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getFcois() {
        return fcois;
    }

    public void setFcois(Float fcois) {
        this.fcois = fcois;
    }

}


