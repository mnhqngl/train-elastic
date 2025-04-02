package com.quang.elastic.config;

import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class DebeziumConnectorConfig {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Bean
    public DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine(DebeziumEventListener eventListener) {
        final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(createConnectorConfiguration().asProperties())
                .notifying(eventListener::handleChangeEvent)
                .build();

        // Run the engine asynchronously
        executor.execute(debeziumEngine);

        // Shutdown hook to ensure proper shutdown of the engine
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                debeziumEngine.close();
            } catch (IOException e) {
                log.error("Error shutting down Debezium engine", e);
            }
        }));

        return debeziumEngine;
    }

    private Configuration createConnectorConfiguration() {
        String hostname = "localhost";
        String port = "3306";
        
        // Extract port and hostname from JDBC URL
        String jdbcUrl = dbUrl;
        if (jdbcUrl.contains("jdbc:mysql://")) {
            String urlWithoutPrefix = jdbcUrl.replace("jdbc:mysql://", "");
            String[] parts = urlWithoutPrefix.split(":");
            if (parts.length >= 2) {
                hostname = parts[0];
                String portAndDatabase = parts[1];
                port = portAndDatabase.split("/")[0];
            }
        }

        return Configuration.create()
                .with("name", "mysql-connector")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("database.hostname", hostname)
                .with("database.port", port)
                .with("database.user", dbUsername)
                .with("database.password", dbPassword)
                .with("database.dbname", "train_elasticsearch")
                .with("database.include.list", "train_elasticsearch")
                .with("table.include.list", "train_elasticsearch.products")
                .with("database.server.id", "1")
                .with("topic.prefix", "train_elasticsearch")
                .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
                .with("schema.history.internal.kafka.topic", "schema-changes.train_elasticsearch")
                .with("database.history.kafka.bootstrap.servers", "localhost:9092")
                .with("database.history.kafka.topic", "dbhistory.train_elasticsearch")
                .with("include.schema.changes", "true")
                .with("snapshot.mode", "initial")
                .with("tombstones.on.delete", "false")
                .build();
    }
}
