package team9.issue_manage_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

//Admin이 받아서 처리할 권한 수정 알림 엔티티
@Entity
@Data
public class AdminAuth {

    public AdminAuth(String id, String role){
        this.id = id;
        this.role = role;
    };

    @Id
    private String id;

    private String role;

    public AdminAuth() {};
}
