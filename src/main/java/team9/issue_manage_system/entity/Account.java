package team9.issue_manage_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Account {

    public Account(String id, String password, String role){
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public Account(String id, String password) {
        this.id = id;
        this.password = password;
        this.role = "tester";
    }

    // 미완
    public boolean changeRole(String id, String role)
    {
        return false;
    }

    @Id
    private String id;

    private String password;
    private String role;

    public Account() {}
}
