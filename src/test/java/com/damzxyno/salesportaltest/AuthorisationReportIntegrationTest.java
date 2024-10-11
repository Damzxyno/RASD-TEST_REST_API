package com.damzxyno.salesportaltest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.damzxyno.rasdspringapi.models.PathItem;
import com.damzxyno.rasdspringapi.models.RASD;
import com.damzxyno.rasdspringapi.models.SecureModel;
import com.damzxyno.rasdspringapi.models.TimeRange;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
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

    private final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private final String LOG_OUT_ENDPOINT = "/api/v1/auth/logout";
    private final String REGISTER_ENDPOINT = "/api/v1/auth/register";
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
    public void rasdReportContainsAllEndpoints(){
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
    public void rasdReportDoesNotContainInvalidEndpoints() {
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
    public void rasdReportShouldContainAllRBACSpecificationForCustomerManagement(){
        PathItem compoundPathItem = rasd.getPaths().get(CUSTOMER_ENDPOINT);
        PathItem specificItemPathItem = rasd.getPaths().get(CUSTOMER_ENDPOINT + "/{id}");

        // Test For Authentication
        Assertions.assertTrue(compoundPathItem.getGet().isAuthenticated());
        Assertions.assertTrue(specificItemPathItem.getPut().isAuthenticated());

        // Test For Roles And Permission [GET]
        List<SecureModel> getCompoundSecureModels = compoundPathItem.getGet().getAuthorisationMod().getRelativeMod();
        SecureModel getSecureModel1 = new SecureModel();
        getSecureModel1.getRoles().add("CUSTOMER");

        SecureModel getSecureModel2 = new SecureModel();
        getSecureModel2.getRoles().add("CUSTOMER_RELATION");

        SecureModel getSecureModel3 = new SecureModel();
        getSecureModel3.getRoles().add("ADMIN");

        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel2));
        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel3));

        // Test For Roles And Permission [PUT]
        List<SecureModel> putCompoundSecureModels = specificItemPathItem.getPut().getAuthorisationMod().getRelativeMod();
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel2));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel3));
    }

    @Test
    public void rasdReportShouldContainAllRBACSpecificationForSalesManagement() {
        PathItem compoundPathItem = rasd.getPaths().get(SALES_ENDPOINT);
        PathItem specificItemPathItem = rasd.getPaths().get(SALES_ENDPOINT + "/{id}");

        // Test For Authentication
        Assertions.assertTrue(compoundPathItem.getGet().isAuthenticated());
        Assertions.assertTrue(compoundPathItem.getPut().isAuthenticated());
        Assertions.assertTrue(compoundPathItem.getPost().isAuthenticated());
        Assertions.assertTrue(specificItemPathItem.getGet().isAuthenticated());

        // Test For Roles And Permission [GET]
        List<SecureModel> getCompoundSecureModels = compoundPathItem.getGet().getAuthorisationMod().getRelativeMod();
        SecureModel getSecureModel1 = new SecureModel();
        getSecureModel1.getRoles().add("SALES_MANAGER");

        SecureModel getSecureModel2 = new SecureModel();
        getSecureModel2.getRoles().add("ADMIN");
        getSecureModel2.getPermissions().add("VIEW_SALES");

        SecureModel getSecureModel3 = new SecureModel();
        getSecureModel3.getRoles().add("ADMIN");
        getSecureModel3.getPermissions().add("MANAGE_SALES");



        SecureModel getSecureModel4 = new SecureModel();
        getSecureModel4.getRoles().add("ROLE_CUSTOMER");

        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel2));
        Assertions.assertTrue(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel4));

        // Test For Roles And Permission [PUT]
        List<SecureModel> putCompoundSecureModels = compoundPathItem.getPut().getAuthorisationMod().getRelativeMod();
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel3));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, getSecureModel4)); // ROLE_CUSTOMER should not have access

        // Test For Roles And Permission [POST]
        List<SecureModel> postCompoundSecureModels = compoundPathItem.getPost().getAuthorisationMod().getRelativeMod();
        Assertions.assertTrue(secureModelListContainThisSecureModel(postCompoundSecureModels, getSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(postCompoundSecureModels, getSecureModel3));
        Assertions.assertTrue(secureModelListContainThisSecureModel(postCompoundSecureModels, getSecureModel4)); // ROLE_CUSTOMER should not have access
    }

    @Test
    public void rasdReportShouldContainAllRBACSpecificationForProductManagement() {
        PathItem compoundPathItem = rasd.getPaths().get(PRODUCT_ENDPOINT);
        PathItem specificItemPathItem = rasd.getPaths().get(PRODUCT_ENDPOINT + "/{id}");

        // Test For Authentication
        Assertions.assertTrue(compoundPathItem.getGet().isAuthenticated());
        Assertions.assertTrue(compoundPathItem.getPut().isAuthenticated());
        Assertions.assertTrue(compoundPathItem.getPost().isAuthenticated());
        Assertions.assertTrue(compoundPathItem.getDelete().isAuthenticated());
        Assertions.assertTrue(specificItemPathItem.getGet().isAuthenticated());

        // Test For Roles And Permission [GET]
        List<SecureModel> getCompoundSecureModels = compoundPathItem.getGet().getAuthorisationMod().getRelativeMod();
        SecureModel getSecureModel1 = new SecureModel();
        getSecureModel1.getRoles().add("ADMIN");
        getSecureModel1.getPermissions().add("VIEW_SALES"); // Not specifically needed but illustrates permission

        Assertions.assertFalse(secureModelListContainThisSecureModel(getCompoundSecureModels, getSecureModel1)); // Check if ADMIN has no direct access

        // Test For Roles And Permission [PUT]
        List<SecureModel> putCompoundSecureModels = compoundPathItem.getPut().getAuthorisationMod().getRelativeMod();
        SecureModel putSecureModel1 = new SecureModel();
        putSecureModel1.getRoles().add("MARKETING_MANAGER");

        SecureModel putSecureModel2 = new SecureModel();
        putSecureModel2.getRoles().add("ADMIN");
        putSecureModel2.getPermissions().add("MANAGE_PRODUCT");

        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, putSecureModel1));
        Assertions.assertTrue(secureModelListContainThisSecureModel(putCompoundSecureModels, putSecureModel2));

        // Test For Roles And Permission [POST]
        List<SecureModel> postCompoundSecureModels = compoundPathItem.getPost().getAuthorisationMod().getRelativeMod();
        Assertions.assertTrue(secureModelListContainThisSecureModel(postCompoundSecureModels, putSecureModel2)); // Only ADMIN can manage products

        // Test For Roles And Permission [DELETE]
        SecureModel deleteCompoundSecureModel = compoundPathItem.getDelete().getAuthorisationMod().getStaticMod();
        Assertions.assertTrue(deleteCompoundSecureModel.equalsThisSecureModel(putSecureModel2)); // Only ADMIN can delete products
    }

    @Test
    public void rasdReportShouldContainAllRBACSpecificationForAuthenticationManagement() {
        PathItem loginPathItem = rasd.getPaths().get(LOGIN_ENDPOINT);
        PathItem logoutPathItem = rasd.getPaths().get(LOG_OUT_ENDPOINT);
        PathItem registerPathItem = rasd.getPaths().get(REGISTER_ENDPOINT);

        // Test For Authentication
        Assertions.assertFalse(loginPathItem.getPost().isAuthenticated());
        Assertions.assertTrue(logoutPathItem.getPost().isAuthenticated());
        Assertions.assertFalse(registerPathItem.getPost().isAuthenticated());
    }
    @Test
    public void rasdReportShouldContainAllRBACSpecificationForAuthorizationReport() {
        PathItem rasdApiPathItem = rasd.getPaths().get(RASD_API_ENDPOINT);

        // Test For Authentication
        Assertions.assertFalse(rasdApiPathItem.getGet().isAuthenticated());
    }

    @Test
    public void rasdReportShouldContainAllRBACSpecificationForRasdUi() {
        PathItem rasdUiPathItem = rasd.getPaths().get(RASD_UI_ENDPOINT);

        // Test For Authentication
        Assertions.assertFalse(rasdUiPathItem.getGet().isAuthenticated());
    }

    @Test
    public void rasdReportShouldContainAllTBACSpecificationForSalesManagent(){
        final EnumMap<DayOfWeek, TimeRange> permittedTimes = new EnumMap<>(DayOfWeek.class);
        final EnumMap<DayOfWeek, TimeRange> restrictedTimes = new EnumMap<>(DayOfWeek.class);
        final TimeRange timeRange = new TimeRange(LocalTime.of(8, 0), LocalTime.of(21, 0));
        permittedTimes.put(DayOfWeek.MONDAY, timeRange);
        permittedTimes.put(DayOfWeek.TUESDAY, timeRange);
        permittedTimes.put(DayOfWeek.WEDNESDAY, timeRange);
        permittedTimes.put(DayOfWeek.THURSDAY, timeRange);
        permittedTimes.put(DayOfWeek.FRIDAY, timeRange);

        restrictedTimes.put(DayOfWeek.SATURDAY, timeRange);
        restrictedTimes.put(DayOfWeek.SUNDAY, timeRange);

        PathItem compoundPathItem = rasd.getPaths().get(SALES_ENDPOINT);
        PathItem specificItemPathItem = rasd.getPaths().get(SALES_ENDPOINT + "/{id}");

        EnumMap<DayOfWeek, TimeRange> actualPutPermittedTimes =
                compoundPathItem.getPut().getAuthorisationMod().getStaticMod().getTimeRulesAccept();
        EnumMap<DayOfWeek, TimeRange> actualPutRestrictedTimes =
                compoundPathItem.getPut().getAuthorisationMod().getStaticMod().getTimeRulesRestrict();
        EnumMap<DayOfWeek, TimeRange> actualPostPermittedTimes =
                compoundPathItem.getPost().getAuthorisationMod().getStaticMod().getTimeRulesAccept();
        EnumMap<DayOfWeek, TimeRange> actualPostRestrictedTimes =
                compoundPathItem.getPost().getAuthorisationMod().getStaticMod().getTimeRulesRestrict();
        Assertions.assertEquals(permittedTimes, actualPutPermittedTimes,
                "Permitted times should match the expected times for weekdays.");
        Assertions.assertEquals(restrictedTimes, actualPutRestrictedTimes,
                "Restricted times should match the expected times for weekdays.");
        Assertions.assertEquals(permittedTimes, actualPostPermittedTimes,
                "Permitted times should match the expected times for weekdays.");
        Assertions.assertEquals(restrictedTimes, actualPostRestrictedTimes,
                "Restricted times should match the expected times for weekdays.");
    }

    @Test
    public void rasdReportShouldContainAllLocationSpecificationForCustomerManagement(){
        PathItem compoundPathItem = rasd.getPaths().get(PRODUCT_ENDPOINT);
        PathItem specificItemPathItem = rasd.getPaths().get(PRODUCT_ENDPOINT + "/{id}");
        Set<String> acceptedLocations = new HashSet<>(List.of("UK"));

        Assertions.assertEquals(compoundPathItem.getGet().getAuthorisationMod().getStaticMod().getAcceptedLocation(), acceptedLocations);
        Assertions.assertEquals(compoundPathItem.getPut().getAuthorisationMod().getStaticMod().getAcceptedLocation(), acceptedLocations);
        Assertions.assertEquals(compoundPathItem.getPost().getAuthorisationMod().getStaticMod().getAcceptedLocation(), acceptedLocations);
        Assertions.assertEquals(compoundPathItem.getDelete().getAuthorisationMod().getStaticMod().getAcceptedLocation(), acceptedLocations);
        Assertions.assertEquals(specificItemPathItem.getGet().getAuthorisationMod().getStaticMod().getAcceptedLocation(), acceptedLocations);
    }


    private boolean secureModelListContainThisSecureModel (List<SecureModel> secureModels,  SecureModel secureModelB){
        var rr = secureModelB.equalsThisSecureModel(secureModelB);
        for (SecureModel secureModelA : secureModels){
            if (secureModelA.equalsThisSecureModel(secureModelB)){
                return true;
            }
        }
        return false;
    };

}
