package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.config.ServiceConfiguration;
import com.github.guninigor75.social_media.config.IntegrationSuite;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@WebMvcTest(TestController.class)
@Import(ServiceConfiguration.class)
class AuthControllerTest extends IntegrationSuite {
//
//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserMapper userMapper;
//
//
//    @MockBean
//    private  MessageService messageService;
//
//    @Autowired
//    private  RoleService roleService;
//
//    @Autowired
//    private  PasswordEncoder passwordEncoder;
//
//
//    @Test
//    void registerTestStatus201() throws Exception {
//        JSONObject object = givenUserDtoJson();
//
//        mvc.perform(MockMvcRequestBuilders
//                .post("/api/v1/auth/register")
//                .content(object.toString())
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().is(201));
//
//    }
//    private static JSONObject givenUserDtoJson() throws JSONException {
//        JSONObject object = new JSONObject();
//        object.put("user","user");
//        object.put("username","user@gmail.ru");
//        object.put("password","100");
//        return object;
//    }

}