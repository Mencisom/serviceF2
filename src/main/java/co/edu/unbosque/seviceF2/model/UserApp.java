package co.edu.unbosque.seviceF2.model;

public class UserApp {

    private String name;
    private String email;
    private String password;
    private String role;
    private float fcoins;
    public UserApp(){}

    public UserApp(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserApp(String name, String email, String password, String role, float fcoins) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fcoins = fcoins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public float getFcoins() {
        return fcoins;
    }

    public void setFcoins(float fcoins) {
        this.fcoins = fcoins;
    }

    @Override
    public String toString() {
        return "UserApp{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", fcoins=" + fcoins +
                '}';
    }
}
