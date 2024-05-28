package team9.issue_manage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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

    public Account(String id, String password) {
        this.id = id;
        this.password = password;
        this.role = "tester";
        //if (mode > 0) mode += 1; // 나중에 수정
    }

    @Id
    private String id;

    private String password;
    private String role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Issue> issues;

    public Account() {}
}
