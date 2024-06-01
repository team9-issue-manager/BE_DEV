package team9.issue_manage_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"issues"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectNum;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plId", referencedColumnName = "id")
    private Account projectLeader;
    // private String accountId; // plId -> 위의 왜래키 관계를 통해 issue ta  ble에 accountId 자동으로 생성

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //@JsonManagedReference
    private Set<Issue> issues;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Project() {}
}
