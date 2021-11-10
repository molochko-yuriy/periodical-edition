package by.epamtc.periodical_edition.entity;

public class User extends BaseEntity<Long> {
    private String lastName;
    private String firstName;
    private String login;
    private String password;
    private String mobilePhone;
    private String email;
    private int balance;

    public User() {}

    public User(Long id, String lastName, String firstName, String login, String password, String mobilePhone,
                String email, int balance) {
        super.setId(id);
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.balance = balance;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        User aThat = (User) object;
        if (getId() == null) {
            if (aThat.getId()  != null) { return false;}
        } else if (!getId().equals(aThat.getId())) { return false;}

        if (getLastName() == null) {
            if (aThat.getLastName()  != null) { return false;}
        } else if (!getLastName().equals(aThat.getLastName())) { return false;}

        if (getFirstName() == null) {
            if (aThat.getFirstName()  != null) { return false;}
        } else if (!getFirstName().equals(aThat.getFirstName())) { return false;}

        if (getLogin() == null) {
            if (aThat.getLogin()  != null) { return false;}
        } else if (!getLogin().equals(aThat.getLogin())) { return false;}

        if (getPassword() == null) {
            if (aThat.getPassword()  != null) { return false;}
        } else if (!getPassword().equals(aThat.getPassword())) { return false;}

        if (getMobilePhone() == null) {
            if (aThat.getMobilePhone()  != null) { return false;}
        } else if (!getMobilePhone().equals(aThat.getMobilePhone())) { return false;}

        if (getEmail() == null) {
            if (aThat.getEmail()  != null) { return false;}
        } else if (!getEmail().equals(aThat.getEmail())) { return false;}

        return getBalance() == aThat.getBalance();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = prime * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = prime * result + (getLogin() != null ? getLogin().hashCode() : 0);
        result = prime * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = prime * result + (getMobilePhone() != null ? getMobilePhone().hashCode() : 0);
        result = prime * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = prime * result + getBalance();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", lastName='" + getLastName() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", login='" + getLogin() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", mobilePhone='" + getMobilePhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", balance=" + getBalance() +
                '}';
    }
}