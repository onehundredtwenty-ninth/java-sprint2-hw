package ru.tasktracker.tests;

import org.junit.jupiter.api.BeforeEach;
import ru.tasktracker.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

}
