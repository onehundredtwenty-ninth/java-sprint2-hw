package ru.tasktracker.tasks;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", id=" + id +
            ", status=" + status +
            '}';
    }

    public String toCsvString() {
        String taskType = this.getClass().getName()
            .substring(this.getClass().getName().lastIndexOf(".") + 1);
        return String.join(",", String.valueOf(id), taskType, name, String.valueOf(status), description);
    }
}
