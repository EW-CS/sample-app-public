package com.clearsense.providerintegration.containers;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class Postgres extends PostgreSQLContainer<Postgres> {

    private static final String IMAGE_VERSION = "postgres:14.4-alpine3.16";
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse(IMAGE_VERSION);
    private static Postgres container;

    private Postgres() {
        super(DOCKER_IMAGE_NAME);
    }

    public static Postgres getInstance(){
        if(container == null)
            container = new Postgres()
                    .withFileSystemBind("src/e2e/resources/db/changelog",
                            "/docker-entrypoint-initdb.d",
                            BindMode.READ_ONLY);

        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
