package huyvu.identityservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import huyvu.identityservice.dto.request.UserCreationRequest;
import huyvu.identityservice.dto.response.UserResponse;
import huyvu.identityservice.service.UserService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Date;

@SpringBootTest // nó làm all

@AutoConfigureMockMvc // tạo mock request trong test
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    //mock bean
    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;


    @BeforeEach // chạy trước khi các test  được chạy
    void initDate() {
        dob = LocalDate.of(1990, 1, 1);
        request = UserCreationRequest.builder()
                .username("johnny")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .id("hfuieuhfuiehuifhjdo")
                .username("johnny")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

    }

    @Test
    //convention
    void createUser_validRequest_success() throws Exception {
        //GIVEN
        ObjectMapper  objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        //WHEN

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);


//         khi gọi api nên tạo requets
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
        ;


        //THEN  -- expect
    }


}
