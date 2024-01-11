package com.clearsense.providerintegration.domain;

import com.clearsense.providerintegration.config.EventServiceProperties;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableConfigurationProperties(EventServiceProperties.class)
public class PushEventHandler {

    public static String cloneDirectoryPath = "C:\\Users\\Prasad Revanaki\\Office\\docs\\productization\\testws";
    public static String cloneDirectorySshPath = "C:\\Users\\Prasad Revanaki\\Office\\docs\\productization\\testsshws";
    public static String publicRepoHttpUrl = "https://github.com/EW-CS/sample-app-public.git";
    public static String privateRepoSshUrl = "git@github.com:EW-CS/ocs-calm-manager.git";

    public static String privateKey = "C:\\Users\\Prasad Revanaki\\.sshprasadirevanaki@github.pem";

    private final EventServiceProperties properties;

    public void handle(final String payload, final String event) {
        // 1. Identify repo url
        var repoUrl = extractRepoUrl(payload);

        // 2. Identify branch
        JSONObject json = new JSONObject(payload);
        var ref = json.get("ref");

        // 3. clone the repo
        clonePublicRepo(repoUrl, ref.toString());
    }

    private String extractRepoUrl(final String payload) {
        JSONObject json = new JSONObject(payload);
        var repository = json.get("repository");
        JSONObject repositoryJson = new JSONObject(repository.toString());
        var url = repositoryJson.get("url");
        log.info("url: {}", url);
        return url.toString();
    }

    private void clonePublicRepo(final String publicRepoHttpUrl, final String ref) {

//        var cloneCommand = Git.cloneRepository()
//                .setURI(publicRepoHttpUrl)
//                .setBranchesToClone(Arrays.asList(ref))
//                .setDirectory(Paths.get(cloneDirectoryPath).toFile())
//                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("PrasadIRevanaki", "Sydney10.0"))
//                ;

        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(publicRepoHttpUrl);
        cloneCommand.setBranch(ref);
        cloneCommand.setDirectory(Paths.get(cloneDirectoryPath).toFile());
        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(properties.getUsername(), properties.getPassword()));

        var cloneResponseTry = Try.of(cloneCommand::call);
        if (cloneResponseTry.isFailure()) {
            cloneResponseTry.getCause().printStackTrace();
            return;
        }

        log.info("Successful");

    }
}
