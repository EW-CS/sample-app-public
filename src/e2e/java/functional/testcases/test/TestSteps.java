package functional.testcases.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import functional.commonUtil.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestSteps {

    private @Autowired ScenarioContext context;

    @When("^I make a GET call on ([^\"]*)$")
    public void iMakeAGETCallOn(String path){
        context.invokeHttpGet(path);
    }

    @Then("^I should receive (\\d+) response status code")
    public void iShouldReceiveStatusCodeResponse(int code){
        context.response.then().statusCode(code);
    }

    @Then("^should receive a non-empty body")
    public void shouldRecieveANonEmptyBody(){
        context.response.then().body(Matchers.notNullValue());
    }


    @Given("^Mock (GET|POST|DELETE|PATCH) ([^\\s]+) returns response with status (\\d+) and body")
    public void mockExternalCall(String method, String path, String statusCode, String responseBody ){

        WireMock.stubFor(get(urlEqualTo(path)).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(Integer.parseInt(statusCode))
                .withBody(responseBody)));
    }

    @Then("^verify the response with")
    public void veryTheResponseWithBody(String expectedResponse) throws JSONException {
        String actualResponse = context.response.then()
                .extract()
                .asString();

        JSONAssert.assertEquals(expectedResponse, actualResponse, false);
    }
}
