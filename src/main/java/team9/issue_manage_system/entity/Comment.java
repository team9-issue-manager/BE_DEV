package team9.issue_manage_system.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Entity
@Data
@EqualsAndHashCode(exclude = {"issue", "account"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 일단 long으로 해뒀는데, 만약 issue처럼 string으로 할거면 수정 필요
    private Long commentNum;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JsonBackReference
    @JoinColumn(name = "issueNum", referencedColumnName = "issueNum")
    private Issue issue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Comment() {}
}
