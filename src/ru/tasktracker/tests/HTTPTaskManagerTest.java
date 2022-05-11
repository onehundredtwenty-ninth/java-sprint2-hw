package ru.tasktracker.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tasktracker.manager.HTTPTaskManager;
import ru.tasktracker.manager.Managers;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.servers.HttpTaskServer;
import ru.tasktracker.servers.KVServer;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    @BeforeAll
    public static void startServer() throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }

    @BeforeEach
    public void createTaskManager() {
        taskManager = (HTTPTaskManager) Managers.getDefault();
    }

    @Test
    public void shouldReturnEmptyListOfTasksTest() {
        TaskManager loadedTaskManager = HTTPTaskManager.loadFromServer(
                "http://localhost:8078");
        assertEquals(0, loadedTaskManager.getAllTasks().size());
    }

    @Test
    public void shouldReturnEpicWithoutSubtasksListTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);

        TaskManager loadedTaskManager = HTTPTaskManager.loadFromServer(
                "http://localhost:8078");
        Epic epic = (Epic) loadedTaskManager.getTaskById(workEpic.getId());

        assertEquals(0, epic.getSubTasks().size());
    }

    @Test
    public void shouldReturnEpicWithSubtasksListTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);
        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId(), 7L, "17.05.2022 16:59");
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
                TaskStatus.NEW, workEpic.getId(), 9L, "19.05.2022 16:59");
        taskManager.createSubTask(pathToWorkSubTask);
        taskManager.createSubTask(didWorkSubTask);

        TaskManager loadedTaskManager = HTTPTaskManager.loadFromServer(
                "http://localhost:8078");
        Epic epic = (Epic) loadedTaskManager.getTaskById(workEpic.getId());

        assertEquals(2, epic.getSubTasks().size());
    }

    @Test
    public void shouldReturnEmptyHistoryListTest() {
        TaskManager loadedTaskManager = HTTPTaskManager.loadFromServer(
                "http://localhost:8078");
        System.out.println(loadedTaskManager.getAllTasks());
        System.out.println(loadedTaskManager.getAllEpics());
        System.out.println(loadedTaskManager.getAllSubTasks());
        System.out.println(loadedTaskManager.history());
        assertEquals(0, loadedTaskManager.history().size());
    }

    @Test
    public void shouldReturnAllKindsOfTasksTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);
        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId(), 7L, "17.05.2022 16:59");
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
                TaskStatus.NEW, workEpic.getId(), 9L, "19.05.2022 16:59");
        taskManager.createSubTask(pathToWorkSubTask);
        taskManager.createSubTask(didWorkSubTask);
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW, 18L,
                "28.05.2022 16:59");
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW, 18L,
                "28.05.2022 16:59");
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);
        taskManager.getTaskById(workEpic.getId());

        TaskManager loadedTaskManager = HTTPTaskManager.loadFromServer(
                "http://localhost:8078");

        Epic loadedEpic = (Epic) loadedTaskManager.getTaskById(workEpic.getId());
        Task loadedTask = loadedTaskManager.getTaskById(buyTask.getId());
        SubTask loadedSubtask = (SubTask) loadedTaskManager.getTaskById(pathToWorkSubTask.getId());

        assertEquals(workEpic.getId(), loadedEpic.getId());
        assertEquals(buyTask.getId(), loadedTask.getId());
        assertEquals(pathToWorkSubTask.getId(), loadedSubtask.getId());
    }

}
