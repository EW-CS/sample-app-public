package functional.commonUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CucumberEnvironment {

    private static final String SERVICE_HOST = "SERVICE_HOSTNAME";
    private static final String LOCALHOST = "localhost";

    public static String getServiceHost(){
        Optional<String> serviceHostName = Optional.ofNullable(System.getenv(SERVICE_HOST));
        return serviceHostName.orElse(LOCALHOST);
    }
}
