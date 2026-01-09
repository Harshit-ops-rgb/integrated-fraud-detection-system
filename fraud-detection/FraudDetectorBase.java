package frauddetection;

import framework.*;
import java.util.*;

public class FraudDetectorBase {
    protected String detectorName;
    protected double threshold;
    protected boolean enabled;
    
    public FraudDetectorBase(String name, double threshold) {
        this.detectorName = name;
        this.threshold = threshold;
        this.enabled = true;
    }
    
    public boolean detectFraud(Map<String, Object> transactionData) {
        try {
            // Validate input data
            if (!DataValidator.isNotNull(transactionData)) {
                ErrorHandler.handleException(
                    new IllegalArgumentException("Transaction data is null"),
                    "FraudDetectorBase.detectFraud"
                );
                return false;
            }
            
            // Calculate risk score
            double riskScore = CoreFeatures.analyzeTransaction(transactionData);
            
            // Log detection attempt
            logDetectionAttempt(transactionData, riskScore);
            
            // Publish event if fraud detected
            if (riskScore >= threshold) {
                publishFraudEvent(transactionData, riskScore);
                return true;
            }
            return false;
        } catch (Exception e) {
            ErrorHandler.handleException(e, "FraudDetectorBase.detectFraud");
            return false;
        }
    }
    
    protected void publishFraudEvent(Map<String, Object> data, double riskScore) {
        String transactionId = (String) data.getOrDefault("transactionId", "UNKNOWN");
        Map<String, Object> details = new HashMap<>(data);
        
        EventHandler.FraudEvent event = new EventHandler.FraudEvent(
            transactionId,
            detectorName + "_FRAUD_DETECTED",
            riskScore,
            details
        );
        
        EventHandler.publishEvent(event);
    }
    
    protected void logDetectionAttempt(Map<String, Object> data, double score) {
        System.out.println("[" + detectorName + "] Risk Score: " + score + 
                          " Threshold: " + threshold + " Status: " + 
                          (score >= threshold ? "FRAUD" : "NORMAL"));
    }
    
    public void enable() { this.enabled = true; }
    public void disable() { this.enabled = false; }
    public boolean isEnabled() { return this.enabled; }
    public void setThreshold(double newThreshold) { this.threshold = newThreshold; }
}
