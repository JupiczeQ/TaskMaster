package Models;

public enum TaskPriority {
    LOW("Niski"),
    MEDIUM("Średni"),
    HIGH("Wysoki");

    private final String displayName;

    TaskPriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TaskPriority fromDisplayName(String displayName) {
        for (TaskPriority priority : values()) {
            if (priority.displayName.equals(displayName)) {
                return priority;
            }
        }
        return MEDIUM;
    }

    @Override
    public String toString() {
        return displayName;
    }
}