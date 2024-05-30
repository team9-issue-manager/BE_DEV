package team9.issue_manage_system.entity;

import jakarta.persistence.*;
import lombok.Data;

//Admin이 받아서 처리할 권한 수정 알림 엔티티
@Entity
@Data
public class AdminAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestNum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account requestAccount;

    private String role;

    public AdminAuth() {}
}
