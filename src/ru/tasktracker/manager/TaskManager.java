package ru.tasktracker.manager;

import java.util.ArrayList;
import java.util.Map;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

public interface TaskManager {

    int getNextTaskId();

    Map<Integer, Epic> getAllEpics();

    Map<Integer, Task> getAllTasks();

    Map<Integer, SubTask> getAllSubTasks();

    void removeAllEpics();

    void removeAllTasks();

    void removeAllSubTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void removeTaskById(int id);

    ArrayList<SubTask> getSubTasksFromEpic(int epicId);
}
