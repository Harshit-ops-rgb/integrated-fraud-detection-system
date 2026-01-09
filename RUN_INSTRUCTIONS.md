# Run Instructions - Integrated Fraud Detection System

## Overview
This document provides comprehensive instructions for running, testing, and deploying the Integrated Fraud Detection System with all framework utilities and fraud detection components.

## System Architecture

### Framework Components (Under `framework/` folder)
- **ErrorHandler.java**: Centralized error handling and logging
- **DataValidator.java**: Input validation and data quality checks
- **CoreFeatures.java**: Transaction feature analysis and risk scoring
- **EventHandler.java**: Event publishing and listener management

### Fraud Detection Components (Under `fraud-detection/` folder)
- **FraudDetectorBase.java**: Abstract base class for all fraud detectors
- **FraudDetector.java**: Velocity and amount-based fraud detection
- Additional specialized detectors for various fraud patterns

## Prerequisites
- Java 8 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Compilation Steps

### 1. Compile Framework Classes
```bash
# Navigate to project root
cd integrated-fraud-detection-system

# Create output directory
mkdir -p bin

# Compile framework classes
javac -d bin framework/*.java
```

### 2. Compile Fraud Detection Classes
```bash
# Add framework classes to classpath and compile fraud detection
javac -cp bin -d bin fraud-detection/*.java
```

## Testing

### 1. Unit Tests for Framework
```bash
# Create a simple test file to verify framework functionality
cat > TestFramework.java << 'EOF'
import framework.*;
import java.util.*;

public class TestFramework {
    public static void main(String[] args) {
        // Test DataValidator
        System.out.println("Testing DataValidator...");
        String testEmail = "test@example.com";
        System.out.println("Email validation: " + DataValidator.isValidEmail(testEmail));
        
        // Test CoreFeatures
        System.out.println("\nTesting CoreFeatures...");
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("transaction_amount", 1000.0);
        transactionData.put("transaction_frequency", 5);
        double riskScore = CoreFeatures.analyzeTransaction(transactionData);
        System.out.println("Risk Score: " + riskScore);
        
        // Test EventHandler
        System.out.println("\nTesting EventHandler...");
        EventHandler.addListener(event -> 
            System.out.println("Event detected: " + event.eventType)
        );
        System.out.println("Framework tests completed successfully!");
    }
}
EOF
```

### 2. Run Framework Tests
```bash
javac -cp bin TestFramework.java
java -cp bin:. TestFramework
```

### 3. Integration Tests with Fraud Detection
```bash
# Create integration test
cat > TestFraudDetection.java << 'EOF'
import frauddetection.*;
import framework.*;
import java.util.*;

public class TestFraudDetection {
    public static void main(String[] args) {
        System.out.println("Testing Fraud Detection System...");
        
        // Create detector instance
        FraudDetector detector = new FraudDetector();
        
        // Test Case 1: Normal transaction
        System.out.println("\nTest 1: Normal Transaction");
        Map<String, Object> normalTx = new HashMap<>();
        normalTx.put("transactionId", "TXN001");
        normalTx.put("fromAccount", "12345678");
        normalTx.put("toAccount", "87654321");
        normalTx.put("amount", 500.0);
        normalTx.put("timestamp", System.currentTimeMillis());
        
        boolean isFraud = detector.detectFraud(normalTx);
        System.out.println("Fraud detected: " + isFraud);
        
        // Test Case 2: High amount transaction
        System.out.println("\nTest 2: High Amount Transaction");
        Map<String, Object> highAmountTx = new HashMap<>();
        highAmountTx.put("transactionId", "TXN002");
        highAmountTx.put("fromAccount", "12345678");
        highAmountTx.put("toAccount", "87654321");
        highAmountTx.put("amount", 50000.0); // Above threshold
        highAmountTx.put("timestamp", System.currentTimeMillis());
        
        isFraud = detector.detectFraud(highAmountTx);
        System.out.println("Fraud detected: " + isFraud);
        
        // Test Case 3: Invalid transaction
        System.out.println("\nTest 3: Invalid Transaction");
        Map<String, Object> invalidTx = new HashMap<>();
        invalidTx.put("transactionId", "TXN003");
        invalidTx.put("fromAccount", "12345"); // Invalid format
        invalidTx.put("toAccount", "87654321");
        invalidTx.put("amount", 1000.0);
        
        isFraud = detector.detectFraud(invalidTx);
        System.out.println("Fraud detected: " + isFraud);
        
        System.out.println("\nIntegration tests completed!");
    }
}
EOF
```

### 4. Run Integration Tests
```bash
javac -cp bin TestFraudDetection.java
java -cp bin:. TestFraudDetection
```

## Component Linking & Integration

All fraud detection classes automatically integrate with the framework:

### Data Flow
1. **Input Validation**: DataValidator validates transaction data
2. **Risk Analysis**: CoreFeatures analyzes transaction characteristics
3. **Fraud Detection**: FraudDetector checks patterns using framework validation
4. **Event Publishing**: EventHandler publishes fraud events for alerting
5. **Error Handling**: ErrorHandler manages exceptions throughout pipeline

### Code Integration Points
```java
// FraudDetectorBase uses DataValidator
if (!DataValidator.isNotNull(transactionData)) { ... }

// FraudDetectorBase uses CoreFeatures
double riskScore = CoreFeatures.analyzeTransaction(transactionData);

// FraudDetectorBase uses EventHandler
EventHandler.publishEvent(event);

// ErrorHandler wraps all operations
ErrorHandler.handleException(e, "FraudDetector.detectFraud");
```

## Deployment

### 1. Create JAR Files
```bash
mkdir -p dist
jar cf dist/fraud-detection-system.jar -C bin framework/ fraud-detection/
```

### 2. Use in Production
```bash
# Add JAR to classpath for your application
java -cp dist/fraud-detection-system.jar:your-app.jar YourMainClass
```

## Configuration

### Adjust Thresholds
```java
FraudDetector detector = new FraudDetector();
detector.setAmountThreshold(10000); // Set amount threshold
detector.setVelocityThreshold(10);  // Set velocity threshold
```

### Add Custom Event Listeners
```java
EventHandler.addListener(fraudEvent -> {
    // Custom alert logic
    System.out.println("Alert: " + fraudEvent.eventType);
    // Send to database, email, Slack, etc.
});
```

## Monitoring & Logging

### View Error Statistics
```java
Map<String, Integer> errors = ErrorHandler.getErrorCounts();
errors.forEach((k, v) -> System.out.println(k + ": " + v));
```

### Monitor Events
```java
Queue<EventHandler.FraudEvent> events = EventHandler.getEventQueue();
for (EventHandler.FraudEvent event : events) {
    System.out.println("Transaction: " + event.transactionId + 
                      " Risk: " + event.riskScore);
}
```

## Troubleshooting

### Compilation Errors
- Ensure Java 8+ is installed: `java -version`
- Check classpath includes all source files
- Verify package names match directory structure

### Runtime Issues
- Check ErrorHandler logs for detailed error information
- Verify input data meets DataValidator requirements
- Review EventHandler listener implementation

## Performance Optimization

1. **Batch Processing**: Process multiple transactions in parallel
2. **Caching**: Cache validation results for repeated patterns
3. **Threshold Tuning**: Adjust detector thresholds based on false positive rates
4. **Event Queue Management**: Monitor queue size to prevent memory issues

## Next Steps

1. Extend FraudDetector with additional pattern detection
2. Implement custom EventListeners for your platform
3. Add database persistence for audit trails
4. Create REST API wrapper for microservices architecture
5. Integrate with machine learning models for advanced detection

## Support & Documentation

Refer to `SYSTEM_ARCHITECTURE.md` for detailed design documentation.
See individual file headers for API documentation.
