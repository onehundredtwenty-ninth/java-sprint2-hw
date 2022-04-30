package ru.tasktracker.tasks;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, int epicId, long duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, TaskStatus status, String description, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, TaskStatus status, String description, int epicId, long duration,
                   String startTime) {
        super(id, name, status, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public String toCsvString() {
        return super.toCsvString() + "," + epicId;
    }
}
