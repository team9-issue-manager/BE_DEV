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
    public String issueNum;
    @Id
    public String title;
    public String content;
    public String id; //writer 같은 느낌.
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date date;
    @ElementCollection(targetClass = Tag.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "issue_tags")
    @Column(name = "tag")
    public Set<Tag> tags;


    public Issue() {}

    public Issue(String issueNum, String title, String content, String id, Set<Tag> tags){
        this.issueNum = issueNum;
        this.title = title;
        this.content = content;
        this.id = id;
        this.tags = tags;
    }

    @Data
    public static class IssueSearchRequest {
        private String title;
    }
}
