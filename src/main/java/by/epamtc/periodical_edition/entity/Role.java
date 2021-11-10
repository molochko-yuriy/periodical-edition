package by.epamtc.periodical_edition.entity;

public class Role extends BaseEntity<Long>{
    private String roleName;

    public Role (){};

    public Role(Long id, String roleName) {
        super.setId(id);
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Role aThat = (Role) object;

        if (getId() == null) {
            if (aThat.getId()  != null) { return false;}
        } else if (!getId().equals(aThat.getId())) { return false;}

        if (getRoleName() == null) {
            if (aThat.getRoleName()  != null) { return false;}
        } else if (!getRoleName().equals(aThat.getRoleName())) { return false;}

        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getRoleName() != null ? getRoleName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", roleName='" + getRoleName() + '\'' +
                '}';
    }
}
