package com.damzxyno.salesportaltest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.damzxyno.rasdspringapi.models.Operation;
import com.damzxyno.rasdspringapi.models.PathItem;
import com.damzxyno.rasdspringapi.models.RASD;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorisationReportIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    private RASD rasd;

    @BeforeEach
    public void setUp(){
        try {
            var rasdStr = mockMvc.perform(get("/rasd/authorisation-report"))  // Use the correct URL
                    .andExpect(status().isOk())                 // Expect HTTP 200 status
                    .andExpect(content().contentType("application/json"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            rasd = mapper.readValue(rasdStr, com.damzxyno.rasdspringapi.models.RASD.class);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void specContainsAllEndpoints(){
        String [] allEndpoints = new String[]{
            "/api/v1/login",
            "/api/v1/sales"
        };
        for (String endpoint : allEndpoints){
            Assertions.assertTrue(rasd.getPaths().containsKey(endpoint));
        }
    }

    @Test
    public void specDoesNotContainUnKnownEndpoints(){
        String [] allEndpoints = new String[]{
                "/api/v1/login/fake",
                "/api/v1/sales/fake"
        };
        for (String endpoint : allEndpoints){
            Assertions.assertFalse(rasd.getPaths().containsKey(endpoint));
        }
    }

    @Test
    public void specContainsAllCompulsoryRolesAndPermission(){
        //ENDPOINT 1
        String endpoint1Verb = "POST";
        String endpoint1 = "/api/v1/login";
        Set<String> endpoint1Permissions = new HashSet<>();
        Set<String> endpoint1Roles = new HashSet<>();

        // ENDPOINT 2
        String endpoint2Verb = "GET";
        String endpoint2 = "/api/v1/sales";
        Set<String> endpoint2Permissions = new HashSet<>();
        Set<String> endpoint2Roles = new HashSet<>();

        // ENDPOINT 2
        String endpoint3Verb = "POST";
        String endpoint3 = "/api/v1/sales";
        Set<String> endpoint3Permissions = new HashSet<>();
        Set<String> endpoint3Roles = new HashSet<>(Arrays.asList("ADMIN"));

        Operation endpoint1Operation = rasd.getPaths().get(endpoint1).getPost();
        Assertions.assertFalse(endpoint1Operation.isAuthenticated());
        Assertions.assertTrue(endpoint1Operation.getAuthorisationMod().getStaticMod().getRoles().isEmpty());
        Assertions.assertTrue(endpoint1Operation.getAuthorisationMod().getStaticMod().getPermissions().isEmpty());

        Operation endpoint2Operation = rasd.getPaths().get(endpoint2).getGet();
        Assertions.assertTrue(endpoint2Operation.isAuthenticated());
        Assertions.assertTrue(endpoint2Operation.getAuthorisationMod().getStaticMod().getRoles().isEmpty());
        Assertions.assertTrue(endpoint2Operation.getAuthorisationMod().getStaticMod().getPermissions().isEmpty());

        var endpoint3Operation = rasd.getPaths().get(endpoint3).getPost();
        Assertions.assertTrue(endpoint3Operation.isAuthenticated());
        for (String role : endpoint3Roles){
            Assertions.assertTrue(endpoint3Operation.getAuthorisationMod().getStaticMod().getRoles().contains(role));
        }
        Assertions.assertTrue(endpoint3Operation.getAuthorisationMod().getStaticMod().getPermissions().isEmpty());
    }
}
