package Models;

public enum TaskStatus {
    TODO("Do zrobienia"),
    IN_PROGRESS("W trakcie"),
    COMPLETED("Ukończono");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TaskStatus fromDisplayName(String displayName) {
        for (TaskStatus status : values()) {
            if (status.displayName.equals(displayName)) {
                return status;
            }
        }
        return TODO;
    }

    @Override
    public String toString() {
        return displayName;
    }
}