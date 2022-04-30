package ru.tasktracker.tasks;

import java.time.Duration;
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
        setDuration();
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
        setDuration();
    }

    public void addSubtask(SubTask subTask) {
        subTasks.add(subTask);
        setEpicStatus();
        setStartTime();
        setEndTime();
        setDuration();
    }

    public void removeSubtasks() {
        subTasks.clear();
        setEpicStatus();
        setStartTime();
        setEndTime();
        setDuration();
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

    public void setDuration() {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        for (SubTask subTask : subTasks) {
            if (subTask.duration != null) {
                duration = duration.plusHours(subTask.getDuration());
            }
        }
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
        List<Integer> subtasksIds = new ArrayList<>();
        for (SubTask subTask : subTasks) {
            subtasksIds.add(subTask.getId());
        }
        return "Epic{" +
                "subTasks=" + subtasksIds +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration.toHours() +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public String toCsvString() {
        String taskType = this.getClass().getName()
                .substring(this.getClass().getName().lastIndexOf(".") + 1);
        if (startTime != null) {
            return String.join(",", String.valueOf(id), taskType, name, String.valueOf(status), description,
                    getStartTime().format(dateTimeFormatter), String.valueOf(getDuration()));
        } else {
            return String.join(",", String.valueOf(id), taskType, name, String.valueOf(status), description,
                    " ", " ");
        }
    }
}
