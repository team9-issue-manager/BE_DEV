package team9.issue_manage_system.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Entity
@Data
public class Comment {

    public Comment(String title, String content, Issue issue){
        this.title = title;
        this.content = content;
        this.issue = issue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 일단 long으로 해뒀는데, 만약 issue처럼 string으로 할거면 수정 필요
    private Long commentId;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false) // 일단 이슈 보고 판단해야할듯
    private Issue issue; // 이거 맞나

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Comment() {};
}
