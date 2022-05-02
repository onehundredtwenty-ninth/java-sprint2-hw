package ru.tasktracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tasktracker.manager.HistoryManager;
import ru.tasktracker.manager.InMemoryHistoryManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void createTaskManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void shouldReturnHistoryWithoutDuplicatesTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        historyManager.add(workEpic);
        historyManager.add(relaxEpic);

        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId());
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
                TaskStatus.NEW, workEpic.getId());
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "Интересный",
                TaskStatus.NEW, relaxEpic.getId());
        historyManager.add(pathToWorkSubTask);
        historyManager.add(didWorkSubTask);
        historyManager.add(relaxSubTask);

        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    public void shouldDeleteHistoryTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        Task workTask = new Task("workTask", "workTask desc", TaskStatus.NEW);
        Task jobTask = new Task("jobTask", "jobTask desc", TaskStatus.NEW);
        Task enoughTask = new Task("enoughTask", "enoughTask desc", TaskStatus.NEW);
        Task finalTask = new Task("finalTask", "finalTask desc", TaskStatus.NEW);
        historyManager.add(buyTask);
        historyManager.add(keyTask);
        historyManager.add(workTask);
        historyManager.add(jobTask);
        historyManager.add(enoughTask);
        historyManager.add(finalTask);

        historyManager.remove(buyTask.getId());
        historyManager.remove(workTask.getId());
        historyManager.remove(finalTask.getId());

        assertEquals(3, historyManager.getHistory().size());
    }


}
