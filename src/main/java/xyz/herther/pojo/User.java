package xyz.herther.pojo;

public class User {
    private int id;
    private String username;
    private String password;
    private String perms;

    public User(int id) {

    }

    public User(int id, String username, String password, String perms) {

    }

    public User(String username, String password, String perms) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", perms='" + perms + '\'' +
                '}';
    }
}
