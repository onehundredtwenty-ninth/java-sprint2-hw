package ru.tasktracker.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        setEpicStatus();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubtask(SubTask subTask) {
        subTasks.add(subTask);
        setEpicStatus();
    }

    public void removeSubtasks() {
        subTasks.clear();
        setEpicStatus();
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
