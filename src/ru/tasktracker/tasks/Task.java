package ru.tasktracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, TaskStatus status, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofHours(duration);
        this.startTime = LocalDateTime.parse(startTime, dateTimeFormatter);
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, TaskStatus status, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, TaskStatus status, String description, long duration, String startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofHours(duration);
        this.startTime = LocalDateTime.parse(startTime, dateTimeFormatter);
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

    public long getDuration() {
        return duration.toHours();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plusHours(duration.toHours());
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public String toCsvString() {
        String taskType = this.getClass().getName()
                .substring(this.getClass().getName().lastIndexOf(".") + 1);
        if (startTime != null && duration != null) {
            return String.join(",", String.valueOf(id), taskType, name, String.valueOf(status), description,
                    startTime.format(dateTimeFormatter), String.valueOf(duration.toHours()));
        } else {
            return String.join(",", String.valueOf(id), taskType, name, String.valueOf(status), description,
                    " ", " ");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(dateTimeFormatter, task.dateTimeFormatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, duration, startTime, dateTimeFormatter);
    }
}
