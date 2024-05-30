package team9.issue_manage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import team9.issue_manage_system.repository.AccountRepository;

//Admin이 받아서 처리할 권한 수정 알림 엔티티
@Entity
@Data
public class AdminAuth {

    @Id
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Account requestAccount;

    private String role;

    public AdminAuth() {}
}
