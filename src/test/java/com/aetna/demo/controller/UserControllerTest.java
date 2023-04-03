package com.aetna.demo.controller;

import com.aetna.demo.model.User;
import com.aetna.demo.repository.UserRepository;
import com.aetna.demo.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(com.aetna.demo.controller.UserControllerTest.class);

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

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
    @Test
    public void testLogin() throws Exception {

        User user1 = new User("1","Tamzid", "Chowdhury", "tamzid123@gmail.com", "3475998377","testPassword!");
        Mockito.when(userService.getUserById("tamzid123@gmail.com")).thenReturn(user1);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/login").
                        contentType(MediaType.APPLICATION_JSON).
                        content("{ \"email\": \"tamzid.chowdhury@gmail.com\", \"password\": \"testPassword!\" }")).
                        andReturn().
                        getResponse();

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    public void testGetAllUsers() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/").
                        contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    public void testGetUserById() throws Exception {

        Mockito.when(userService.getUserById("1234")).thenReturn(new User());

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/1234").
                        contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }


}
