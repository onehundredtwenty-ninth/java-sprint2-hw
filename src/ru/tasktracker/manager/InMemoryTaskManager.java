package ru.tasktracker.manager;

import java.util.*;

import ru.tasktracker.comparators.TaskPriorityComparator;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

public class InMemoryTaskManager implements TaskManager {

    protected static int tasksCounter = 0;
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> prioritizedTasks = new TreeSet<>(new TaskPriorityComparator());

    public int getNextTaskId() {
        tasksCounter++;
        return tasksCounter;
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
        prioritizedTasks.removeAll(subTasks.values());
        subTasks.clear();
    }

    @Override
    public void removeAllTasks() {
        prioritizedTasks.removeAll(tasks.values());
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        prioritizedTasks.removeAll(subTasks.values());
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
        prioritizedTasks.add(task);
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
            prioritizedTasks.add(subTask);
        } else {
            System.out.printf("Невозможно создать подзадачу, так как эпик с id %s отсутствует\n",
                subTask.getEpicId());
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
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
            prioritizedTasks.add(subTask);
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
            prioritizedTasks.removeIf(task -> task.getId() == id);
        } else if (epics.containsKey(id)) {
            for (SubTask subTask : epics.get(id).getSubTasks()) {
                historyManager.remove(subTask.getId());
                subTasks.remove(subTask.getId());
                prioritizedTasks.removeIf(task -> task.getId() == id);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).removeSubTask(id);
            subTasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.removeIf(task -> task.getId() == id);
        } else {
            System.out.printf("Не удалось удалить задачу, так как задача с id %s отсутствует\n", id);
        }
    }

    protected boolean hasIntersections(Task newTask) {
        for (Task task : prioritizedTasks) {
            if (newTask.getStartTime().isBefore(task.getEndTime())
                    || newTask.getEndTime().isAfter(task.getStartTime())) {
                return true;
            }
        }
        return false;
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

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}
