package functional.commonUtil;

import com.clearsense.Application;
import functional.containers.TestContainersListeners;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.config.LogConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Application.class, ScenarioContext.class})
@TestExecutionListeners(listeners = TestContainersListeners.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles(profiles = {"acceptance-test"})
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8080)
@CucumberContextConfiguration
public class ScenarioRunner {

    private @Autowired MockMvc mockMvc;
    private @Autowired ScenarioContext context;

    @Before
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvcConfig config = RestAssuredMockMvc.config()
                .logConfig(new LogConfig(
                        context.getReport().getRestLogPrintStream(),
                        true
                ));
    }

    @After
    public void teardown(Scenario scenario) throws IOException {
        context.getReport().write(scenario);
    }
}
