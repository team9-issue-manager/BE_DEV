package team9.issue_manage_system.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Account {

    public Account(String id, String password, String role){
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public Account(String id, String password) {
        this.id = id;
        this.password = password;
        //this.role = null;
    }

    @Id
    private String id;

    private String password;
    private String role;


    // getter setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return password;
    }

    public void setName(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Account() {}
}
