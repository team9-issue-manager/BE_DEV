package team9.issue_manage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
// @Data 존재하니까 @Getter, @Setter 생략.
public class Issue {
    public enum Tag{
        PL, DEV, TESTER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueNum;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    // private String accountId; //writer -> 위의 왜래키 관계를 통해 issue table에 accountId 자동으로 생성
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devId", referencedColumnName = "id")
    private Account developer;
    private Integer state = 0; // 0:new, 1:assigned, 2:fixed, 3:resolved, 4:closed

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ElementCollection(targetClass = Tag.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "issue_tags")
    @Column(name = "tag")
    public Set<Tag> tags;


    public Issue() {}

    public Issue(String title, String content){
        this.title = title;
        this.content = content;
    }

    public Issue(String title, String content, String id, Set<Tag> tags){
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    @Data
    public static class IssueSearchRequest {
        private String title;
    }
}
