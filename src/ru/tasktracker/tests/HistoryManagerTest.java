package ru.tasktracker.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tasktracker.manager.InMemoryTaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class HistoryManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void createTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(0, taskManager.history().size());
    }

    @Test
    public void shouldReturnHistoryWithoutDuplicatesTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId());
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
                TaskStatus.NEW, workEpic.getId());
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "Интересный",
                TaskStatus.NEW, relaxEpic.getId());
        taskManager.createSubTask(pathToWorkSubTask);
        taskManager.createSubTask(didWorkSubTask);
        taskManager.createSubTask(relaxSubTask);

        taskManager.getTaskById(workEpic.getId());
        taskManager.getTaskById(workEpic.getId());
        taskManager.getTaskById(relaxEpic.getId());

        assertEquals(2, taskManager.history().size());
    }

    @Test
    public void shouldDeleteHistoryTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        Task workTask = new Task("workTask", "workTask desc", TaskStatus.NEW);
        Task jobTask = new Task("jobTask", "jobTask desc", TaskStatus.NEW);
        Task enoughTask = new Task("enoughTask", "enoughTask desc", TaskStatus.NEW);
        Task finalTask = new Task("finalTask", "finalTask desc", TaskStatus.NEW);
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);
        taskManager.createTask(workTask);
        taskManager.createTask(jobTask);
        taskManager.createTask(enoughTask);
        taskManager.createTask(finalTask);

        taskManager.getTaskById(buyTask.getId());
        taskManager.getTaskById(keyTask.getId());
        taskManager.getTaskById(workTask.getId());
        taskManager.getTaskById(jobTask.getId());
        taskManager.getTaskById(enoughTask.getId());
        taskManager.getTaskById(finalTask.getId());

        taskManager.removeTaskById(buyTask.getId());
        taskManager.removeTaskById(workTask.getId());
        taskManager.removeTaskById(finalTask.getId());

        assertEquals(3, taskManager.history().size());
    }

}
