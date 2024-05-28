package team9.issue_manage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectNum;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plId", referencedColumnName = "id")
    // private String accountId; // plId -> 위의 왜래키 관계를 통해 issue ta  ble에 accountId 자동으로 생성
    private Account projectLeader;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    public Project(String title) {
        this.title = title;
    }

    public Project() {};
}
