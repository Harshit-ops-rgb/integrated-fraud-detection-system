package framework;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorHandler {
    private static final Map<String, Integer> errorCounts = new HashMap<>();
    private static final int ERROR_THRESHOLD = 5;
    private static final int THRESHOLD_TIME_WINDOW = 60000; // 1 minute
    
    public static void handleException(Exception e, String context) {
        try {
            logError(e, context);
            String errorKey = context + ":" + e.getClass().getName();
            
            errorCounts.put(errorKey, errorCounts.getOrDefault(errorKey, 0) + 1);
            
            if (errorCounts.get(errorKey) >= ERROR_THRESHOLD) {
                alertAdministrator(errorKey, errorCounts.get(errorKey));
                errorCounts.put(errorKey, 0);
            }
        } catch (Exception ex) {
            System.err.println("Critical: Error handler failure - " + ex.getMessage());
        }
    }
    
    private static void logError(Exception e, String context) {
        System.err.println("[" + LocalDateTime.now() + "] ERROR in " + context + ": " + e.getMessage());
        e.printStackTrace();
    }
    
    private static void alertAdministrator(String errorKey, int count) {
        System.err.println("[ALERT] Threshold exceeded for " + errorKey + " (" + count + " occurrences)");
    }
    
    public static Map<String, Integer> getErrorCounts() {
        return new HashMap<>(errorCounts);
    }
}
