package bcu.cmp5332.bookingsystem.auth;

/**
 * Represents a system user.
 */
public class User {

    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    // Only for CUSTOMER users. For ADMIN it stays null.
    private Integer customerId;

    public User(int id, String firstName, String middleName,
                String lastName, String email, String password, Role role, Integer customerId) {

        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        if (middleName == null || middleName.isBlank()) {
            return firstName + " " + lastName;
        }
        return firstName + " " + middleName + " " + lastName;
    }
}
