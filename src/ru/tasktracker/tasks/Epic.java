package ru.tasktracker.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        setEpicStatus();
        setStartTime();
        setEndTime();
    }

    public Epic(int id, String name, TaskStatus status, String description) {
        super(id, name, status, description);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
        setEpicStatus();
        setStartTime();
        setEndTime();
    }

    public void addSubtask(SubTask subTask) {
        subTasks.add(subTask);
        setEpicStatus();
        setStartTime();
        setEndTime();
    }

    public void removeSubtasks() {
        subTasks.clear();
        setEpicStatus();
        setStartTime();
        setEndTime();
    }

    public void removeSubTask(int subTaskId) {
        subTasks.removeIf(subTask -> subTask.getId() == subTaskId);
    }

    public void updateSubTask(SubTask subTask) {
        removeSubTask(subTask.getId());
        addSubtask(subTask);
    }

    public void setEpicStatus() {
        long doneSubTasksQuantity = subTasks.stream().filter(s -> s.status.equals(TaskStatus.DONE)).count();
        long newSubTasksQuantity = subTasks.stream().filter(s -> s.status.equals(TaskStatus.NEW)).count();

        if (subTasks.size() == 0 || subTasks.size() == newSubTasksQuantity) {
            this.status = TaskStatus.NEW;
        } else if (subTasks.size() == doneSubTasksQuantity) {
            this.status = TaskStatus.DONE;
        } else {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public long getDuration() {
        for (SubTask subTask : subTasks) {
            duration = duration.plusHours(subTask.getDuration());
        }
        return duration.toHours();
    }

    public void setEndTime() {
        List<LocalDateTime> subtasksEndTime = new ArrayList<>();
        for (SubTask subTask : subTasks) {
            if (subTask.startTime != null) {
                subtasksEndTime.add(subTask.getEndTime());
            }
        }
        Optional<LocalDateTime> maxEndTime = subtasksEndTime.stream().max(LocalDateTime::compareTo);
        endTime = maxEndTime.orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime() {
        List<LocalDateTime> subtasksStartTime = new ArrayList<>();
        for (SubTask subTask : subTasks) {
            if (subTask.startTime != null) {
                subtasksStartTime.add(subTask.startTime);
            }
        }
        Optional<LocalDateTime> minStartTime = subtasksStartTime.stream().min(LocalDateTime::compareTo);
        startTime = minStartTime.orElse(null);
    }

    @Override
    public void setStatus(TaskStatus status) {
        System.out.println("Невозможна установка статуса эпика вручную. Необходимо использовать метод setEpicStatus()");
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
