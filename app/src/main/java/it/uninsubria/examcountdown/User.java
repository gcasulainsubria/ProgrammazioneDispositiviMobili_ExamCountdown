package it.uninsubria.examcountdown;

public class User {
    private String uid;
    private String email;

    public User(){
        //firebase
    }

    public User(String id, String email){
        this.uid = id;
        this.email = email;
    }

    public String getId() {
        return uid;
    }

    public void setId(String id) {
        this.uid = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
