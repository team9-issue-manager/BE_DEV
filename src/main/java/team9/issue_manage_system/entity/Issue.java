package team9.issue_manage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
// @Data 존재하니까 @Getter, @Setter 생략.

public class Issue {
    @Id
    public String issueNum;

    public String title;
    public String content;
    public String id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date date;
    @ElementCollection
    public List<String> tags;


    public Issue() {}

    public Issue(String issueNum, String title, String content, String id, List<String> tags){
        this.issueNum = issueNum;
        this.title = title;
        this.content = content;
        this.id = id;
        this.tags = tags;
    }
}
