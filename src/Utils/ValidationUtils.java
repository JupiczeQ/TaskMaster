package Utils;

public class ValidationUtils {
    public static ValidationResult validateRegistration(String login, String password, String confirmPassword) {
        if (login.trim().isEmpty()) {
            return ValidationResult.error("Pole loginu nie może być puste!");
        }

        if (password.trim().isEmpty()) {
            return ValidationResult.error("Pole hasła nie może być puste!");
        }

        if (confirmPassword.trim().isEmpty()) {
            return ValidationResult.error("Pole powtórz hasło nie może być puste!");
        }

        if (login.trim().length() < 3) {
            return ValidationResult.error("Login musi mieć co najmniej 3 znaki!");
        }

        if (login.trim().length() > 20) {
            return ValidationResult.error("Login nie może mieć więcej niż 20 znaków!");
        }

        // Sprawdzenie czy login zawiera tylko dozwolone znaki
        if (!login.trim().matches("^[a-zA-Z0-9_]+$")) {
            return ValidationResult.error("Login może zawierać tylko litery, cyfry i podkreślenia!");
        }

        // Sprawdzenie minimalnej długości hasła
        if (password.length() < 6) {
            return ValidationResult.error("Hasło musi mieć co najmniej 6 znaków!");
        }

        // Sprawdzenie czy hasła się zgadzają
        if (!password.equals(confirmPassword)) {
            return ValidationResult.error("Hasła nie są identyczne!");
        }

        // Sprawdzenie czy hasło zawiera co najmniej jedną cyfrę
        if (!password.matches(".*\\d.*")) {
            return ValidationResult.error("Hasło musi zawierać co najmniej jedną cyfrę!");
        }

        // Sprawdzenie czy hasło zawiera co najmniej jedną literę
        if (!password.matches(".*[a-zA-Z].*")) {
            return ValidationResult.error("Hasło musi zawierać co najmniej jedną literę!");
        }
        return ValidationResult.success();
    }

    public static ValidationResult validateLogin(String login, String password) {
        if (login.trim().isEmpty()) {
            return ValidationResult.error("Pole loginu nie może być puste!");
        }

        if (password.trim().isEmpty()) {
            return ValidationResult.error("Pole hasła nie może być puste!");
        }
        return ValidationResult.success();
    }
}
