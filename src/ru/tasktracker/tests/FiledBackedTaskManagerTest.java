package ru.tasktracker.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tasktracker.manager.FileBackedTasksManager;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class FiledBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void createTaskManager() {
        taskManager = new FileBackedTasksManager(Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));
    }

    @Test
    public void shouldReturnEmptyListOfTasksTest() {
        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));

        assertEquals(0, loadedTaskManager.getAllTasks().size());
    }

    @Test
    public void shouldReturnEpicWithoutSubtasksListTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        taskManager.getTaskById(workEpic.getId());
        taskManager.getTaskById(relaxEpic.getId());

        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));

        assertEquals(2, loadedTaskManager.getAllEpics().size());
    }

    @Test
    public void shouldReturnEpicWithSubtasksListTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);
        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId(), 7L, "17.05.2022 16:59");
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
                TaskStatus.NEW, workEpic.getId(), 9L, "19.05.2022 16:59");
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "Интересный",
                TaskStatus.NEW, relaxEpic.getId(), 18L, "28.05.2022 16:59");
        taskManager.createSubTask(pathToWorkSubTask);
        taskManager.createSubTask(didWorkSubTask);
        taskManager.createSubTask(relaxSubTask);
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW, 18L,
                "28.05.2022 16:59");
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW, 18L,
                "28.05.2022 16:59");
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        taskManager.getTaskById(workEpic.getId());
        taskManager.getTaskById(relaxEpic.getId());

        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));

        assertEquals(2, loadedTaskManager.getAllEpics().size());
    }

    @Test
    public void shouldReturnEmptyHistoryListTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));

        assertEquals(0, loadedTaskManager.getAllTasks().size());
    }

}
