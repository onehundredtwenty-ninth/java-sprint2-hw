package ru.tasktracker.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.tasktracker.exception.ManagerSaveException;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final Path storageFile;

    public FileBackedTasksManager(Path storageFile) {
        super();
        this.storageFile = storageFile;
    }

    public FileBackedTasksManager(Map<Integer, Epic> epics, Map<Integer, Task> tasks, Map<Integer, SubTask> subTasks,
        HistoryManager historyManager, Path storageFile) {
        super(epics, tasks, subTasks, historyManager);
        this.storageFile = storageFile;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    private void save() {
        List<String> tasksAsCsv = new ArrayList<>();
        tasksAsCsv.addAll(getListCsvOfTasks(tasks));
        tasksAsCsv.addAll(getListCsvOfTasks(epics));
        tasksAsCsv.addAll(getListCsvOfTasks(subTasks));

        tasksAsCsv.add(0, "id,type,name,status,description,epic");
        StringBuilder lastElement = new StringBuilder(tasksAsCsv.get(tasksAsCsv.size() - 1));
        lastElement.deleteCharAt(lastElement.length() - 1);
        tasksAsCsv.set(tasksAsCsv.size() - 1, lastElement.toString());
        tasksAsCsv.add("");

        tasksAsCsv.add(historyManagerToString(historyManager));

        try {
            Files.write(storageFile, tasksAsCsv);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    private List<String> getListCsvOfTasks(Map<Integer, ? extends Task> tasks) {
        List<String> tasksAsCsv = new ArrayList<>();

        for (Task task : tasks.values()) {
            String taskAsCsvString = task.toCsvString();
            tasksAsCsv.add(taskAsCsvString + ",");
            System.out.println("taskAsCsvString " + taskAsCsvString);
        }
        return tasksAsCsv;
    }

    private static String historyManagerToString(HistoryManager manager) {
        List<String> ids = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join(",", ids);
    }

    public static FileBackedTasksManager loadFromFile(Path storageFile) {
        Map<Integer, Epic> epics = new HashMap<>();
        Map<Integer, Task> tasks = new HashMap<>();
        Map<Integer, SubTask> subTasks = new HashMap<>();
        HistoryManager historyManager = Managers.getDefaultHistory();

        String storageFileText = "";
        String[] tasksAsString;

        try {
            storageFileText = Files.readString(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tasksAsString = storageFileText.split("\r\n");
        for (int i = 1; i < tasksAsString.length - 2; i++) {
            Task task = getTaskFromCsvString(tasksAsString[i]);
            if (task instanceof Epic) {
                epics.put(task.getId(), (Epic) task);
            } else if (task instanceof SubTask) {
                subTasks.put(task.getId(), (SubTask) task);
            } else {
                tasks.put(task.getId(), task);
            }
        }

        List<Integer> ids = getHistoryManagerFromString(tasksAsString[tasksAsString.length - 1]);

        for (Integer id : ids) {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subTasks.containsKey(id)) {
                historyManager.add(subTasks.get(id));
            }
        }

        return new FileBackedTasksManager(epics, tasks, subTasks, historyManager, storageFile);
    }

    private static Task getTaskFromCsvString(String task) {
        String[] taskAttributes = task.split(",");
        switch (taskAttributes[1]) {
            case ("Task"):
                return new Task(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    TaskStatus.valueOf(taskAttributes[3]),
                    taskAttributes[4]);
            case ("Epic"):
                return new Epic(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    TaskStatus.valueOf(taskAttributes[3]),
                    taskAttributes[4]);
            case ("SubTask"):
                return new SubTask(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    TaskStatus.valueOf(taskAttributes[3]),
                    taskAttributes[4], Integer.parseInt(taskAttributes[5]));
            default:
                throw new IllegalArgumentException("Задача имеет некорректный тип: " + taskAttributes[1]);
        }
    }

    private static List<Integer> getHistoryManagerFromString(String value) {
        String[] idsArray = value.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String idAsString : idsArray) {
            ids.add(Integer.parseInt(idAsString));
        }

        return ids;
    }

}
