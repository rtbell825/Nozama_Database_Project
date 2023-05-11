package Entity;

public class Admin extends User {

    private PrivilegeLevel level;
    public enum PrivilegeLevel{
        ONE, TWO, THREE, FOUR, FIVE
    }

    public PrivilegeLevel getLevel() {
        return level;
    }

    public void setLevel(PrivilegeLevel level) {
        this.level = level;
    }

    public Admin(String lastName, String firstName, String username, String password, PrivilegeLevel privilegeLevel) {
        super(lastName, firstName, username, password, true);
        level = privilegeLevel;
    }
    public Admin(User user, PrivilegeLevel privilegeLevel) {
        this(user.lastName, user.firstName, user.username, user.password, privilegeLevel);
    }
    public Admin(String username, PrivilegeLevel privilegeLevel){
        this.username = username;
        this.level = privilegeLevel;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "level=" + level +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
