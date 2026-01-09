package framework;

import java.time.LocalDateTime;
import java.util.*;

public class EventHandler {
    private static final Queue<FraudEvent> eventQueue = new LinkedList<>();
    private static final List<EventListener> listeners = new ArrayList<>();
    private static final int MAX_QUEUE_SIZE = 1000;
    
    public static class FraudEvent {
        public String transactionId;
        public String eventType;
        public double riskScore;
        public LocalDateTime timestamp;
        public Map<String, Object> details;
        
        public FraudEvent(String transactionId, String eventType, double riskScore, Map<String, Object> details) {
            this.transactionId = transactionId;
            this.eventType = eventType;
            this.riskScore = riskScore;
            this.timestamp = LocalDateTime.now();
            this.details = new HashMap<>(details);
        }
    }
    
    public interface EventListener {
        void onFraudEventDetected(FraudEvent event);
    }
    
    public static void publishEvent(FraudEvent event) {
        try {
            if (eventQueue.size() >= MAX_QUEUE_SIZE) {
                eventQueue.poll(); // Remove oldest event
            }
            eventQueue.offer(event);
            notifyListeners(event);
            logEvent(event);
        } catch (Exception e) {
            ErrorHandler.handleException(e, "EventHandler.publishEvent");
        }
    }
    
    public static void addListener(EventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public static void removeListener(EventListener listener) {
        listeners.remove(listener);
    }
    
    private static void notifyListeners(FraudEvent event) {
        for (EventListener listener : listeners) {
            try {
                listener.onFraudEventDetected(event);
            } catch (Exception e) {
                ErrorHandler.handleException(e, "EventHandler.notifyListeners");
            }
        }
    }
    
    private static void logEvent(FraudEvent event) {
        System.out.println("[" + event.timestamp + "] EVENT: " + event.eventType + " - Transaction: " + 
                          event.transactionId + " Risk: " + event.riskScore);
    }
    
    public static Queue<FraudEvent> getEventQueue() {
        return new LinkedList<>(eventQueue);
    }
}
