package com.aetna.demo.controller;

import com.aetna.demo.repository.UserRepository;
import com.aetna.demo.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @MockBean
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(com.aetna.demo.controller.UserControllerTest.class);

    @BeforeEach
    public void beforeTest(TestInfo testInfo) {
        LOGGER.info("***********************************************************");
        LOGGER.info("STARTING TEST: {}", testInfo.getDisplayName());
        LOGGER.info("***********************************************************");
    }

    @AfterEach
    public void afterTest(TestInfo testInfo) {
        LOGGER.info("***********************************************************");
        LOGGER.info("FINISHED TEST: {}", testInfo.getDisplayName());
        LOGGER.info("***********************************************************");
    }


}
