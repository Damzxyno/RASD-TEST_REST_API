package com.damzxyno.salesportaltest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.damzxyno.rasdspringapi.models.Operation;
import com.damzxyno.rasdspringapi.models.PathItem;
import com.damzxyno.rasdspringapi.models.RASD;
import com.damzxyno.rasdspringapi.models.SecureModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

    private final String LOGIN_ENDPOINT = "/api/v1/login";
    private final String LOG_OUT_ENDPOINT = "/api/v1/logout";
    private final String REGISTER_ENDPOINT = "/api/v1/register";
    private final String CUSTOMER_ENDPOINT = "/api/v1/customers";
    private final String SALES_ENDPOINT = "/api/v1/sales";
    private final String PRODUCT_ENDPOINT = "/api/v1/products";
    private final String RASD_API_ENDPOINT = "/rasd/authorisation-report";
    private final String RASD_UI_ENDPOINT = "/rasd/ui";


    @BeforeEach
    public void setUp(){
        try {
            var rasdStr = mockMvc.perform(get("/rasd/authorisation-report"))  // Use the correct URL
                    .andExpect(status().isOk())
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
    public void specificationContainsAllEndpoints(){
        Assertions.assertNotNull(rasd, "RASD object should not be null");

        // Check if each individual endpoint is present in the paths
        Assertions.assertTrue(rasd.getPaths().containsKey(LOGIN_ENDPOINT),
                "Endpoint " + LOGIN_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(LOG_OUT_ENDPOINT),
                "Endpoint " + LOG_OUT_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(REGISTER_ENDPOINT),
                "Endpoint " + REGISTER_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(CUSTOMER_ENDPOINT),
                "Endpoint " + CUSTOMER_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(SALES_ENDPOINT),
                "Endpoint " + SALES_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(PRODUCT_ENDPOINT),
                "Endpoint " + PRODUCT_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(RASD_API_ENDPOINT),
                "Endpoint " + RASD_API_ENDPOINT + " is missing from the paths.");

        Assertions.assertTrue(rasd.getPaths().containsKey(RASD_UI_ENDPOINT),
                "Endpoint " + RASD_UI_ENDPOINT + " is missing from the paths.");
    }

    @Test
    public void specificationDoesNotContainInvalidEndpoints() {
        // Arrange
        final String INVALID_ENDPOINT_1 = "/api/v1/invalid1";
        final String INVALID_ENDPOINT_2 = "/api/v1/invalid2";
        final String INVALID_ENDPOINT_3 = "/api/v1/invalid3";

        // Ensure that rasd is properly initialized
        Assertions.assertNotNull(rasd, "RASD object should not be null");

        // Check if each invalid endpoint is NOT present in the paths
        Assertions.assertFalse(rasd.getPaths().containsKey(INVALID_ENDPOINT_1),
                "Endpoint " + INVALID_ENDPOINT_1 + " should not be present in the paths.");

        Assertions.assertFalse(rasd.getPaths().containsKey(INVALID_ENDPOINT_2),
                "Endpoint " + INVALID_ENDPOINT_2 + " should not be present in the paths.");

        Assertions.assertFalse(rasd.getPaths().containsKey(INVALID_ENDPOINT_3),
                "Endpoint " + INVALID_ENDPOINT_3 + " should not be present in the paths.");
    }

    @Test
    public void specificationShouldContainAllRBACSpecificationForCustomerManagement(){
        PathItem compoundPathItem = rasd.getPaths().get(CUSTOMER_ENDPOINT);
        PathItem specificItemPathItem = rasd.getPaths().get(CUSTOMER_ENDPOINT + "/{id}");

        // Test For Authentication
        Assertions.assertTrue(compoundPathItem.getGet().isAuthenticated());
        Assertions.assertTrue(specificItemPathItem.getPut().isAuthenticated());

        // Test For Roles And Permission [GET]
        List<SecureModel> getCompoundSecureModels = compoundPathItem.getGet().getAuthorisationMod().getRelativeMod();
        SecureModel getSecureModel1 = new SecureModel();
        getSecureModel1.getRoles().add("'CUSTOMER'");

        SecureModel getSecureModel2 = new SecureModel();
        getSecureModel2.getRoles().add("'CUSTOMER_RELATION'");

        SecureModel getSecureModel3 = new SecureModel();
        getSecureModel3.getRoles().add("'ADMIN'");

        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel2));
        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel3));

        // Test For Roles And Permission [PUT]
        List<SecureModel> putCompoundSecureModels = specificItemPathItem.getPut().getAuthorisationMod().getRelativeMod();
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel2));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel3));
    }

    public boolean secureModelListContainThisSecureModel (List<SecureModel> secureModels,  SecureModel secureModelB){
        for (SecureModel secureModelA : secureModels){
            boolean isPresent = false;
            if (secureModelA.equalsThisSecureModel(secureModelB)){
                return true;
            }
        }
        return false;
    };

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
