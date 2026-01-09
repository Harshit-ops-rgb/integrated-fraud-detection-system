package frauddetection;

import framework.*;
import java.util.*;

public class FraudDetector extends FraudDetectorBase {
    private double velocityThreshold;
    private double amountThreshold;
    private Map<String, Integer> transactionCounts;
    
    public FraudDetector() {
        super("VelocityDetector", 0.6);
        this.velocityThreshold = 5; // Transactions per minute
        this.amountThreshold = 5000; // Amount in currency units
        this.transactionCounts = new HashMap<>();
    }
    
    @Override
    public boolean detectFraud(Map<String, Object> transactionData) {
        try {
            if (!enabled) return false;
            
            // Validate data
            if (!DataValidator.isValidTransaction(
                (String) transactionData.get("fromAccount"),
                (String) transactionData.get("toAccount"),
                ((Number) transactionData.getOrDefault("amount", 0)).doubleValue()
            )) {
                logDetectionAttempt(transactionData, 0.9);
                return true;
            }
            
            // Check velocity (number of transactions)
            String accountId = (String) transactionData.get("fromAccount");
            int count = transactionCounts.getOrDefault(accountId, 0) + 1;
            transactionCounts.put(accountId, count);
            
            // Check amount
            double amount = ((Number) transactionData.getOrDefault("amount", 0)).doubleValue();
            if (amount > amountThreshold) {
                publishFraudEvent(transactionData, 0.8);
                return true;
            }
            
            // Check velocity
            if (count > velocityThreshold) {
                publishFraudEvent(transactionData, 0.75);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            ErrorHandler.handleException(e, "FraudDetector.detectFraud");
            return false;
        }
    }
    
    public void setVelocityThreshold(double threshold) {
        this.velocityThreshold = threshold;
    }
    
    public void setAmountThreshold(double threshold) {
        this.amountThreshold = threshold;
    }
    
    public void reset() {
        transactionCounts.clear();
    }
}
