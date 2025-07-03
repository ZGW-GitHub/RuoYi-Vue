package com.ruoyi;

import com.ruoyi.common.core.domain.model.LoginUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Snow
 */
@SpringBootTest
public class RuoYiApplicationTest {

    @Test
    void contextTest() {
    }

    @BeforeAll
    static void beforeAll() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(105L);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @AfterAll
    static void afterAll() {
        SecurityContextHolder.clearContext();
    }

}
