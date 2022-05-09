package ru.tasktracker.manager;

import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager(Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
