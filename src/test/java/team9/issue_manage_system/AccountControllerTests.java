package team9.issue_manage_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import team9.issue_manage_system.controller.AccountController;
import team9.issue_manage_system.entity.Account;

@WebMvcTest(AccountController.class)
public class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void
}
