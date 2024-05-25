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
        this.role = role; //
    }

    public Account(String id, String password, Integer mode) {
        this.id = id;
        this.password = password;
        this.role = "tester";
        if (mode > 0) mode += 1; // 나중에 수정
    }

    @Id
    private String id;

    private String password;
    private String role;

    public Account() {}
}
