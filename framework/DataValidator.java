package framework;

import java.util.regex.Pattern;

public class DataValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^\\d{8,16}$");
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.replaceAll("-", "")).matches();
    }
    
    public static boolean isValidAccountNumber(String account) {
        if (account == null || account.trim().isEmpty()) {
            return false;
        }
        return ACCOUNT_PATTERN.matcher(account).matches();
    }
    
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 1000000; // Max 1 million per transaction
    }
    
    public static boolean isValidTransaction(String fromAccount, String toAccount, double amount) {
        return isValidAccountNumber(fromAccount) && 
               isValidAccountNumber(toAccount) && 
               isValidAmount(amount) && 
               !fromAccount.equals(toAccount);
    }
    
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
