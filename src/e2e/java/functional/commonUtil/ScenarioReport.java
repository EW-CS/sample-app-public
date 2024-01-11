package functional.commonUtil;

import io.cucumber.java.Scenario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ScenarioReport {

    public List<String> messages = new ArrayList<>();
    private ByteArrayOutputStream restLogOutputStream = new ByteArrayOutputStream();

    public void addMessage(String message){
        messages.add(message);
    }

    public PrintStream getRestLogPrintStream(){
        return new PrintStream(restLogOutputStream);
    }

    public void write(Scenario scenario) throws IOException {
        for (String msg: messages)
            scenario.log(msg);
        scenario.log(restLogOutputStream.toString());
        restLogOutputStream.close();
    }
}
