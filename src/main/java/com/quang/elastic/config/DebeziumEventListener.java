package com.quang.elastic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quang.elastic.entity.Product;
import com.quang.elastic.repository.ProductRepository;
import io.debezium.engine.RecordChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebeziumEventListener {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        log.info("Received change event: {}", sourceRecordRecordChangeEvent);
        
        try {
            SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
            Struct sourceRecordValue = (Struct) sourceRecord.value();

            if (sourceRecordValue == null) {
                log.info("Source record value is null");
                return;
            }

            // Get operation type
            String operationType = sourceRecordValue.getString("op");
            
            // Process only if it's a create, update, or delete operation
            if (operationType == null) {
                log.info("Operation type is null");
                return;
            }

            // Handling based on operation type
            switch (operationType) {
                case "c":  // Create
                case "r":  // Read (during snapshot)
                    handleCreateOperation(sourceRecordValue);
                    break;
                case "u":  // Update
                    handleUpdateOperation(sourceRecordValue);
                    break;
                case "d":  // Delete
                    handleDeleteOperation(sourceRecordValue);
                    break;
                default:
                    log.info("Unsupported operation type: {}", operationType);
            }
            
        } catch (Exception e) {
            log.error("Error handling change event", e);
        }
    }

    private void handleCreateOperation(Struct sourceRecordValue) {
        try {
            // Extract 'after' data (the new record state)
            Struct after = sourceRecordValue.getStruct("after");
            if (after != null) {
                // Extract fields from the 'after' struct
                String name = after.getString("name");
                String description = after.getString("description");
                Double price = after.getFloat64("price");
                Long id = after.getInt64("id");
                
                // Create a new Elasticsearch document
                Product product = Product.builder()
                        .id(UUID.randomUUID().toString())  // Generate a new ID for Elasticsearch
                        .name(name)
                        .description(description)
                        .price(price)
                        .build();
                
                productRepository.save(product);
                log.info("Created new product in Elasticsearch: {}", product);
            }
        } catch (Exception e) {
            log.error("Error handling create operation", e);
        }
    }

    private void handleUpdateOperation(Struct sourceRecordValue) {
        try {
            // Extract 'after' data (the updated record state)
            Struct after = sourceRecordValue.getStruct("after");
            if (after != null) {
                // Extract fields from the 'after' struct
                String name = after.getString("name");
                String description = after.getString("description");
                Double price = after.getFloat64("price");
                
                // Find and update the document in Elasticsearch
                productRepository.findByNameContaining(name)
                        .stream()
                        .findFirst()
                        .ifPresent(existingProduct -> {
                            existingProduct.setDescription(description);
                            existingProduct.setPrice(price);
                            productRepository.save(existingProduct);
                            log.info("Updated product in Elasticsearch: {}", existingProduct);
                        });
            }
        } catch (Exception e) {
            log.error("Error handling update operation", e);
        }
    }

    private void handleDeleteOperation(Struct sourceRecordValue) {
        try {
            // Extract 'before' data (the record state before deletion)
            Struct before = sourceRecordValue.getStruct("before");
            if (before != null) {
                // Extract name from the 'before' struct
                String name = before.getString("name");
                
                // Find and delete the document in Elasticsearch
                productRepository.findByNameContaining(name)
                        .stream()
                        .findFirst()
                        .ifPresent(product -> {
                            productRepository.delete(product);
                            log.info("Deleted product from Elasticsearch: {}", product);
                        });
            }
        } catch (Exception e) {
            log.error("Error handling delete operation", e);
        }
    }
}
