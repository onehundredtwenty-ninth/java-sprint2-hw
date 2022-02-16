package ru.tasktracker.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

public class Manager {

    private static int tasksCounter = 0;
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    public int getNextTaskId() {
        tasksCounter++;
        return tasksCounter;
    }

    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    public Map<Integer, SubTask> getAllSubTasks() {
        return subTasks;
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubTasks() {
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
        }
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else {
            System.out.printf("Задача с id %s отсутствует в списке\n", id);
            return null;
        }
    }

    public void createTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Невозможно создать задачу, так как задача с таким id уже существует");
        }
    }

    public void createEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Невозможно создать эпик, так как эпик с таким id уже существует");
        }
    }

    public void createSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            if (epics.containsKey(subTask.getEpicId())) {
                Epic epic = epics.get(subTask.getEpicId());
                epic.addSubtask(subTask);
                subTasks.put(subTask.getId(), subTask);
            } else {
                System.out.printf("Невозможно создать подзадачу, так как эпик с id %s отсутствует\n",
                    subTask.getEpicId());
            }
        } else {
            System.out.printf("Невозможно создать подзадачу, так как подзадача с id %s уже существует\n",
                subTask.getId());
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.printf("Не удалось обновить задачу, так как задача с id %s отсутствует\n", task.getId());
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.printf("Не удалось обновить эпик, так как эпик с id %s отсутствует\n", epic.getId());
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            epics.get(subTask.getEpicId()).updateSubTask(subTask);
        } else {
            System.out.printf("Не удалось обновить эпик, так как эпик с id %s отсутствует\n", subTask.getId());
        }
    }

    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).removeSubTask(id);
            subTasks.remove(id);
        } else {
            System.out.printf("Не удалось удалить задачу, так как задача с id %s отсутствует\n", id);
        }
    }

    public ArrayList<SubTask> getSubTasksFromEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId).getSubTasks();
        } else {
            System.out.printf("Эпик с id %s отсутствует в списке\n", epicId);
        }
        return null;
    }
}
