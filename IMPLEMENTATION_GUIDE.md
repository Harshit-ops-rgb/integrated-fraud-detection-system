# Implementation Guide - Integrated Fraud Detection System

## Overview
This guide demonstrates how to integrate and use the Integrated Fraud Detection System components in your application.

## Quick Start Example

### 1. Basic Usage

```java
import frauddetection.*;
import framework.*;
import java.util.*;

public class FraudDetectionDemo {
    public static void main(String[] args) {
        // Initialize the fraud detector
        FraudDetector detector = new FraudDetector();
        
        // Configure thresholds
        detector.setAmountThreshold(5000.0);
        detector.setVelocityThreshold(10);
        
        // Create sample transaction
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("transactionId", "TXN_2025_001");
        transaction.put("fromAccount", "12345678");
        transaction.put("toAccount", "87654321");
        transaction.put("amount", 2500.0);
        transaction.put("timestamp", System.currentTimeMillis());
        
        // Detect fraud
        boolean isFraud = detector.detectFraud(transaction);
        System.out.println("Transaction is fraudulent: " + isFraud);
    }
}
```

## Integration Patterns

### Pattern 1: Direct Detector Integration

```java
// Single detector for specific fraud type
FraudDetector velocityDetector = new FraudDetector();
velocityDetector.setThreshold(0.7);

if (velocityDetector.detectFraud(transactionData)) {
    // Handle fraud
    logFraudAlert(transactionData);
    blockTransaction(transactionData);
}
```

### Pattern 2: Multiple Detectors (Ensemble)

```java
FraudDetector detector1 = new FraudDetector();
FraudDetector detector2 = new FraudDetector();

int fraudVotes = 0;
if (detector1.detectFraud(transaction)) fraudVotes++;
if (detector2.detectFraud(transaction)) fraudVotes++;

if (fraudVotes >= 2) {
    // Unanimous fraud decision
    blockTransaction(transaction);
}
```

### Pattern 3: Event-Driven Architecture

```java
// Set up fraud alert listener
EventHandler.addListener(fraudEvent -> {
    if (fraudEvent.riskScore > 0.8) {
        // Critical risk - immediate action
        notifySecurityTeam(fraudEvent);
        blockTransaction(fraudEvent);
    } else if (fraudEvent.riskScore > 0.6) {
        // Medium risk - review required
        queueForReview(fraudEvent);
    }
});

// Fraud detector automatically publishes events
FraudDetector detector = new FraudDetector();
detector.detectFraud(transactionData); // Events published automatically
```

### Pattern 4: Validation-First Approach

```java
// Always validate before processing
if (!DataValidator.isValidTransaction(
    transaction.get("fromAccount"),
    transaction.get("toAccount"),
    transaction.get("amount")
)) {
    ErrorHandler.handleException(
        new IllegalArgumentException("Invalid transaction data"),
        "processTransaction"
    );
    return false;
}

// Safe to process
FraudDetector detector = new FraudDetector();
return detector.detectFraud(transaction);
```

## Framework Component Usage

### ErrorHandler Integration

```java
try {
    // Business logic
    boolean isFraud = detector.detectFraud(transaction);
} catch (Exception e) {
    // Centralized error handling
    ErrorHandler.handleException(e, "FraudDetectionService.process");
    // Error automatically logged and counted
}

// Monitor error statistics
Map<String, Integer> errors = ErrorHandler.getErrorCounts();
for (String error : errors.keySet()) {
    if (errors.get(error) > 5) {
        System.out.println("Alert: High error rate for " + error);
    }
}
```

### DataValidator Integration

```java
// Validate email addresses
if (DataValidator.isValidEmail(customerEmail)) {
    processCustomer(customerEmail);
}

// Validate transaction amounts
if (DataValidator.isValidAmount(amount)) {
    processPayment(amount);
}

// Validate account numbers
if (DataValidator.isValidAccountNumber(account)) {
    transferFunds(account);
}
```

### CoreFeatures Integration

```java
// Get risk score for any transaction
Map<String, Object> transactionData = new HashMap<>();
transactionData.put("transaction_amount", 5000.0);
transactionData.put("transaction_frequency", 3);
transactionData.put("merchant_category", "GAMBLING");
transactionData.put("geographic_location", "UNKNOWN");

double riskScore = CoreFeatures.analyzeTransaction(transactionData);
System.out.println("Risk Score: " + riskScore); // 0.0 to 1.0

if (riskScore > 0.7) {
    // High risk transaction
    requireAdditionalVerification(transaction);
}
```

### EventHandler Integration

```java
// Create custom event listener
EventHandler.addListener(event -> {
    // Log to file
    logEventToFile(event);
    
    // Send to monitoring service
    sendToMonitoring(event);
    
    // Update dashboard
    updateDashboard(event.riskScore);
});

// Manually publish events
Map<String, Object> details = new HashMap<>();
details.put("reason", "Suspicious pattern detected");
EventHandler.FraudEvent event = new EventHandler.FraudEvent(
    "TXN123",
    "CUSTOM_FRAUD_DETECTED",
    0.85,
    details
);
EventHandler.publishEvent(event);
```

## Common Implementation Scenarios

### Scenario 1: Real-Time Transaction Screening

```java
public class TransactionProcessor {
    private FraudDetector fraudDetector = new FraudDetector();
    
    public void processTransaction(Transaction tx) {
        // Validate
        if (!DataValidator.isValidTransaction(
            tx.getFromAccount(),
            tx.getToAccount(),
            tx.getAmount()
        )) {
            tx.setStatus("REJECTED_INVALID");
            return;
        }
        
        // Analyze
        Map<String, Object> txData = tx.toMap();
        double riskScore = CoreFeatures.analyzeTransaction(txData);
        
        // Detect
        if (fraudDetector.detectFraud(txData)) {
            tx.setStatus("FRAUD_DETECTED");
            // EventHandler automatically notifies listeners
        } else {
            tx.setStatus("APPROVED");
        }
    }
}
```

### Scenario 2: Batch Processing with Error Handling

```java
public class BatchFraudDetection {
    public void processBatch(List<Transaction> transactions) {
        FraudDetector detector = new FraudDetector();
        int fraudCount = 0;
        
        for (Transaction tx : transactions) {
            try {
                Map<String, Object> data = tx.toMap();
                if (detector.detectFraud(data)) {
                    fraudCount++;
                    handleFraud(tx);
                }
            } catch (Exception e) {
                // Errors are automatically handled and logged
                ErrorHandler.handleException(e, "BatchProcessor.process");
            }
        }
        
        System.out.println("Processed " + transactions.size() + 
                          " transactions, " + fraudCount + " fraud detected");
    }
}
```

### Scenario 3: Custom Detector Extension

```java
// Extend FraudDetectorBase for custom logic
public class GeoLocationDetector extends FraudDetectorBase {
    public GeoLocationDetector() {
        super("GeoLocationDetector", 0.75);
    }
    
    @Override
    public boolean detectFraud(Map<String, Object> transactionData) {
        try {
            String currentLocation = (String) transactionData.get("current_location");
            String previousLocation = (String) transactionData.get("previous_location");
            long timeDelta = (long) transactionData.get("time_delta");
            
            // Check if travel is possible
            if (isImpossibleTravel(currentLocation, previousLocation, timeDelta)) {
                publishFraudEvent(transactionData, 0.9);
                return true;
            }
            return false;
        } catch (Exception e) {
            ErrorHandler.handleException(e, "GeoLocationDetector.detectFraud");
            return false;
        }
    }
    
    private boolean isImpossibleTravel(String loc1, String loc2, long timeDelta) {
        // Implementation specific to your geography data
        return false;
    }
}
```

## Configuration Best Practices

1. **Threshold Tuning**: Adjust thresholds based on false positive rate
2. **Listener Management**: Always remove listeners when no longer needed
3. **Error Monitoring**: Regularly check ErrorHandler statistics
4. **Event Queue**: Monitor EventHandler queue size for memory leaks
5. **Validation**: Always validate input before processing

## Testing Your Implementation

```java
public class IntegrationTest {
    @Test
    public void testFraudDetectionFlow() {
        FraudDetector detector = new FraudDetector();
        
        // Test normal transaction
        Map<String, Object> normalTx = createNormalTransaction();
        assertFalse(detector.detectFraud(normalTx));
        
        // Test fraud transaction
        Map<String, Object> fraudTx = createFraudTransaction();
        assertTrue(detector.detectFraud(fraudTx));
    }
}
```

## Performance Considerations

- **Batch Processing**: Process transactions in batches for better throughput
- **Detector Reuse**: Reuse detector instances across multiple transactions
- **Memory Management**: Monitor event queue size and clear periodically
- **Parallel Processing**: Use thread pools for concurrent transaction processing

## Troubleshooting

| Issue | Solution |
|-------|----------|
| High false positives | Lower detector threshold or adjust feature weights |
| Missed fraud | Increase threshold or enhance detection logic |
| Memory issues | Reduce EventHandler queue size or clear events |
| Validation failures | Review DataValidator regex patterns for your domain |

For detailed API documentation, see individual file comments in the codebase.
