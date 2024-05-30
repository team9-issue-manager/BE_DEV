package team9.issue_manage_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Data
// @Data 존재하니까 @Getter, @Setter 생략.
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueNum;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    @JsonBackReference
    // private String accountId; //writer -> 위의 왜래키 관계를 통해 issue table에 accountId 자동으로 생성
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devId", referencedColumnName = "id")
    @JsonBackReference
    private Account developer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectNum", referencedColumnName = "projectNum")
    @JsonBackReference
    private Project project;

    private Integer state = 0; // 0:new, 1:assigned, 2:fixed, 3:resolved, 4:closed

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public String tag;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Comment> comments;

    public Issue() {}

    public Issue(String title, String content){
        this.title = title;
        this.content = content;
    }

    public Issue(String title, String content, String tag){
        this.title = title;
        this.content = content;
        this.tag = tag;
    }
}
