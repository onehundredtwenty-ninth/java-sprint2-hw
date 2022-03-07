package ru.tasktracker.manager;

import java.util.ArrayList;
import java.util.List;
import ru.tasktracker.tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> viewedTasks = new ArrayList<>();
    private static final int HISTORY_LENGTH = 10;

    @Override
    public void add(Task task) {
        if (viewedTasks.size() == HISTORY_LENGTH) {
            viewedTasks.remove(0);
        }
        viewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
