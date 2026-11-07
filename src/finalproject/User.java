package finalproject;

public class User {
    private static int userCounter = 1;
    private static int staffCounter = 1;
    private static int adminCounter = 1;
    
    private String userId;
    private String username;
    private String password;
    private String role;
    private String phoneNumber;

    public User(String username, String password, String role, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        
        if(this.role.equals("Staff")){
            this.userId = String.format("STF-%04d", staffCounter++);
        } else if (this.role.equals("Admin")) {
            this.userId = String.format("ADM-%04d", adminCounter++);
        } else {
            this.userId = String.format("USR-%04d", userCounter++);
        }
    }
    
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getRole(){
        return role;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public String toString(){
        return userId + " [" + role + "] " + username;
     }
}
