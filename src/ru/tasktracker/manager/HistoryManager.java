package ru.tasktracker.manager;

import java.util.List;
import ru.tasktracker.tasks.Task;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
