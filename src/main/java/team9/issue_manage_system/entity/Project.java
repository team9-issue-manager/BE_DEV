package team9.issue_manage_system.entity;

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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Issue> issues;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Project() {}
}
