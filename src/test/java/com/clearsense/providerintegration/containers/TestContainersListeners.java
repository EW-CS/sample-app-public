package com.clearsense.providerintegration.containers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class TestContainersListeners implements TestExecutionListener, Ordered {

    private static final PostgreSQLContainer postgresContainer;
    private static final KafkaContainer kafkaContainer;

    static {
        /* Postgres Test Container Initialization */
        postgresContainer = Postgres.getInstance();
        postgresContainer.start();
      //  log.info(" PostgresContainer ::: State Running - {}",postgresContainer.isRunning());

        /* Kafka Test Container Initialization */
        kafkaContainer = Kafka.getInstance();
        kafkaContainer.start();
       // log.info(" KafkaContainer ::: State Running - {}",kafkaContainer.isRunning());

    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestClass(testContext);
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        TestExecutionListener.super.prepareTestInstance(testContext);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestMethod(testContext);
    }

    @Override
    public void beforeTestExecution(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestExecution(testContext);
    }

    @Override
    public void afterTestExecution(TestContext testContext) throws Exception {
        TestExecutionListener.super.afterTestExecution(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        TestExecutionListener.super.afterTestMethod(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        TestExecutionListener.super.afterTestClass(testContext);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
