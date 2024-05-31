package team9.issue_manage_system.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@EqualsAndHashCode(exclude = {"issues"})
public class Account {

    public Account(String id, String password, String role){
        this.id = id;
        this.password = password;
        this.role = role; //
    }

    @Id
    private String id;

    private String password;
    private String role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Issue> issues;

//    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JsonManagedReference
//    private Set<Issue> assignedIssues;

    public Account() {}
}
