package ru.tasktracker.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.tasktracker.exception.ManagerSaveException;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final Path storageFile;

    public FileBackedTasksManager(Path storageFile) {
        super();
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

    public void save() {
        List<String> tasksAsCsv = new ArrayList<>();
        tasksAsCsv.addAll(getListCsvOfTasks(tasks));
        tasksAsCsv.addAll(getListCsvOfTasks(epics));
        tasksAsCsv.addAll(getListCsvOfTasks(subTasks));

        tasksAsCsv.add(0, "id,type,name,status,description,epic");
        StringBuilder lastElement = new StringBuilder(tasksAsCsv.get(tasksAsCsv.size() - 1));
        lastElement.setCharAt(lastElement.length() - 1, '\n');
        tasksAsCsv.set(tasksAsCsv.size() - 1, lastElement.toString());

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

    static String historyManagerToString(HistoryManager manager) {
        List<String> ids = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join(",", ids);
    }

}
