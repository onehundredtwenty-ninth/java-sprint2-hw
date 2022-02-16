package ru.tasktracker.tasks;

public enum TaskStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS "),
    DONE("DONE ");

    TaskStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
