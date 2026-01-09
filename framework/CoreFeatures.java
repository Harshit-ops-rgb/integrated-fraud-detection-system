package framework;

import java.util.HashMap;
import java.util.Map;

public class CoreFeatures {
    private static final Map<String, Double> featureWeights = new HashMap<>();
    
    static {
        featureWeights.put("transaction_amount", 0.25);
        featureWeights.put("transaction_frequency", 0.20);
        featureWeights.put("merchant_category", 0.15);
        featureWeights.put("geographic_location", 0.20);
        featureWeights.put("time_of_day", 0.10);
        featureWeights.put("device_fingerprint", 0.10);
    }
    
    public static double analyzeTransaction(Map<String, Object> transactionData) {
        try {
            double riskScore = 0.0;
            
            for (Map.Entry<String, Double> entry : featureWeights.entrySet()) {
                double featureScore = calculateFeatureScore(entry.getKey(), transactionData);
                riskScore += featureScore * entry.getValue();
            }
            
            return Math.min(riskScore, 1.0); // Normalize to 0-1 range
        } catch (Exception e) {
            ErrorHandler.handleException(e, "CoreFeatures.analyzeTransaction");
            return 0.5; // Default medium risk on error
        }
    }
    
    private static double calculateFeatureScore(String feature, Map<String, Object> data) {
        Object value = data.get(feature);
        if (value == null) {
            return 0.5; // Unknown feature gets medium score
        }
        
        switch (feature) {
            case "transaction_amount":
                return scoreAmount((Double) value);
            case "transaction_frequency":
                return scoreFrequency((Integer) value);
            case "merchant_category":
                return scoreCategory((String) value);
            case "geographic_location":
                return scoreLocation((String) value);
            default:
                return 0.5;
        }
    }
    
    private static double scoreAmount(Double amount) {
        if (amount > 50000) return 0.9;
        if (amount > 10000) return 0.7;
        if (amount > 5000) return 0.5;
        return 0.2;
    }
    
    private static double scoreFrequency(Integer count) {
        if (count > 100) return 0.8;
        if (count > 50) return 0.6;
        if (count > 20) return 0.4;
        return 0.2;
    }
    
    private static double scoreCategory(String category) {
        if (category.equals("GAMBLING") || category.equals("ADULT")) return 0.9;
        if (category.equals("INTERNATIONAL")) return 0.6;
        return 0.3;
    }
    
    private static double scoreLocation(String location) {
        if (location.contains("UNKNOWN")) return 0.8;
        if (location.contains("INTERNATIONAL")) return 0.5;
        return 0.2;
    }
}
