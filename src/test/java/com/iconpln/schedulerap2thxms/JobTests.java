package com.iconpln.schedulerap2thxms;

import com.iconpln.schedulerap2thxms.proxy.LoginProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JobTests {


    @Autowired
    private LoginProxy loginProxy;

    @Test
    public void testLogin() throws Exception {
        loginProxy.login()
                .doOnSuccess(token -> System.out.println("JWT Token: " + token))
                .doOnError(error -> System.err.println("Login failed: " + error.getMessage()))
                .block(); // block() to execute mono synchronously for testing
    }
}
