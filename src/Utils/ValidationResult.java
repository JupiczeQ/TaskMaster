package Utils;

public class ValidationResult {
    private boolean isValid;
    private String errorMessage;

    public ValidationResult(boolean isValid, String errorMessage){
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public ValidationResult(boolean isValid){
        this(isValid,"");
    }

    public boolean isValid() { return isValid; }

    public String getErrorMessage() { return errorMessage; }

    public static ValidationResult success() {
        return new ValidationResult(true);
    }

    public static ValidationResult error(String message){
        return new ValidationResult(false, message);
    }
}
