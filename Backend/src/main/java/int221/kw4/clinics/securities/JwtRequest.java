package int221.kw4.clinics.securities;

public class JwtRequest{

    private String username;
    private String password;

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JwtRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}