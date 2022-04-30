package ru.tasktracker.comparators;

import ru.tasktracker.tasks.Task;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime() == null) {
            return -1;
        } else if (o2.getStartTime() == null) {
            return 1;
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }
}
