package ru.tasktracker.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

public class InMemoryTaskManager implements TaskManager {

    protected static int tasksCounter = 0;
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    public int getNextTaskId() {
        tasksCounter++;
        return tasksCounter;
    }

    public InMemoryTaskManager() {}

    public InMemoryTaskManager(Map<Integer, Epic> epics, Map<Integer, Task> tasks, Map<Integer, SubTask> subTasks,
        HistoryManager historyManager) {
        this.epics = epics;
        this.tasks = tasks;
        this.subTasks = subTasks;
        this.historyManager = historyManager;
    }

    @Override
    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    @Override
    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, SubTask> getAllSubTasks() {
        return subTasks;
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            System.out.printf("Задача с id %s отсутствует в списке\n", id);
            return null;
        }
    }

    @Override
    public void createTask(Task task) {
        task.setId(getNextTaskId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNextTaskId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(getNextTaskId());
        if (epics.containsKey(subTask.getEpicId())) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.addSubtask(subTask);
            subTasks.put(subTask.getId(), subTask);
        } else {
            System.out.printf("Невозможно создать подзадачу, так как эпик с id %s отсутствует\n",
                subTask.getEpicId());
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.printf("Не удалось обновить задачу, так как задача с id %s отсутствует\n", task.getId());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            ArrayList<SubTask> subtasks = epics.get(epic.getId()).getSubTasks();
            epic.setSubTasks(subtasks);
            epics.put(epic.getId(), epic);
        } else {
            System.out.printf("Не удалось обновить эпик, так как эпик с id %s отсутствует\n", epic.getId());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            epics.get(subTask.getEpicId()).updateSubTask(subTask);
        } else {
            System.out.printf("Не удалось обновить эпик, так как эпик с id %s отсутствует\n", subTask.getId());
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else if (epics.containsKey(id)) {
            for (SubTask subTask : epics.get(id).getSubTasks()) {
                historyManager.remove(subTask.getId());
            }
            epics.remove(id);
            historyManager.remove(id);
        } else if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).removeSubTask(id);
            subTasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.printf("Не удалось удалить задачу, так как задача с id %s отсутствует\n", id);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksFromEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId).getSubTasks();
        } else {
            System.out.printf("Эпик с id %s отсутствует в списке\n", epicId);
        }
        return null;
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }
}
