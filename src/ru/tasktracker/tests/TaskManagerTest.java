package ru.tasktracker.tests;

import org.junit.jupiter.api.Test;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;

    @Test
    public void shouldGetAllEpicsTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        assertEquals(2, taskManager.getAllEpics().size());
    }

    @Test
    public void shouldGetEmptyEpicsListTest() {
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    public void shouldGetAllTasksTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW,
                17L, "19.06.2022 16:59");
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW,
                27L, "27.05.2022 16:59");
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    public void shouldGetEmptyTasksListTest() {
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void shouldGetAllSubtasksTest() {
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

        assertEquals(3, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldGetEmptySubTasksListTest() {
        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldRemoveAllEpicsTest() {
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

        taskManager.removeAllEpics();

        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldRemoveAllSubtasksTest() {
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

        taskManager.removeAllSubTasks();

        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldRemoveAllTasksTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        taskManager.removeAllTasks();

        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void shouldGetEpicByIdTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        Task actualEpic = taskManager.getTaskById(workEpic.getId());

        assertEquals(workEpic.getId(), actualEpic.getId());
    }

    @Test
    public void shouldGetSubtaskByIdTest() {
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

        Task actualSubtask = taskManager.getTaskById(pathToWorkSubTask.getId());

        assertEquals(pathToWorkSubTask.getId(), actualSubtask.getId());
    }

    @Test
    public void shouldGetTaskByIdTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        Task actualTask = taskManager.getTaskById(buyTask.getId());

        assertEquals(buyTask.getId(), actualTask.getId());
    }

    @Test
    public void shouldReturnNullForEpicWithIncorrectIdTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        Task actualEpic = taskManager.getTaskById(436);

        assertNull(actualEpic);
    }

    @Test
    public void shouldReturnNullForSubtaskWithIncorrectIdTest() {
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

        Task actualSubtask = taskManager.getTaskById(436);

        assertNull(actualSubtask);
    }

    @Test
    public void shouldReturnNullForTaskWithIncorrectIdTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        Task actualTask = taskManager.getTaskById(436);

        assertNull(actualTask);
    }

    @Test
    public void shouldReturnNullForEmptyTasksListTest() {
        Task actualTask = taskManager.getTaskById(436);
        assertNull(actualTask);
    }

    @Test
    public void shouldRemoveTaskByIdTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);
        taskManager.removeTaskById(buyTask.getId());

        Task actualTask = taskManager.getTaskById(buyTask.getId());

        assertNull(actualTask);
    }

    @Test
    public void shouldReturnAllEpicSubtasksTest() {
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

        assertEquals(2, taskManager.getSubTasksFromEpic(workEpic.getId()).size());
    }

    @Test
    public void shouldPrioritizedTasksSetTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);
        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId(), 7L, "17.05.2022 16:59");
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
                TaskStatus.NEW, workEpic.getId(), 9L, "19.05.2022 16:59");
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "Интересный",
                TaskStatus.NEW, relaxEpic.getId(), 8L, "28.05.2022 16:59");
        SubTask relaxAndChillSubTask = new SubTask("Глянуть сериальчик да", "Интересный",
                TaskStatus.NEW, relaxEpic.getId(), 8L, "28.05.2022 19:59");
        taskManager.createSubTask(pathToWorkSubTask);
        taskManager.createSubTask(didWorkSubTask);
        taskManager.createSubTask(relaxSubTask);
        taskManager.createSubTask(relaxAndChillSubTask);
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW, 18L,
                "30.07.2022 16:59");
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW, 18L,
                "30.06.2022 16:59");
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        List<Task> manuallyPrioritizedTasks = new ArrayList<>();
        manuallyPrioritizedTasks.add(pathToWorkSubTask);
        manuallyPrioritizedTasks.add(didWorkSubTask);
        manuallyPrioritizedTasks.add(relaxSubTask);
        manuallyPrioritizedTasks.add(keyTask);
        manuallyPrioritizedTasks.add(buyTask);

        Task[] priorityTasksArray = taskManager.getPrioritizedTasks().toArray(Task[]::new);

        for (int i = 0; i < manuallyPrioritizedTasks.size(); i++) {
            assertEquals(manuallyPrioritizedTasks.get(i), priorityTasksArray[i]);
        }
    }

    @Test
    public void shouldCreateEpicTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);

        Epic actualEpic = (Epic) taskManager.getTaskById(workEpic.getId());
        assertEquals(workEpic, actualEpic);
    }

    @Test
    public void shouldCreateSubtaskTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);
        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId(), 7L, "17.05.2022 16:59");
        taskManager.createSubTask(pathToWorkSubTask);

        SubTask actualSubTask = (SubTask) taskManager.getTaskById(pathToWorkSubTask.getId());
        assertEquals(pathToWorkSubTask, actualSubTask);
    }

    @Test
    public void shouldCreateTaskTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW, 18L,
                "30.07.2022 16:59");
        taskManager.createTask(buyTask);

        Task actualTask = taskManager.getTaskById(buyTask.getId());
        assertEquals(buyTask, actualTask);
    }

    @Test
    public void shouldUpdateEpicTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);

        Epic updatedWorkEpic = new Epic(workEpic.getId(), "Работка", "За еду");
        taskManager.updateEpic(updatedWorkEpic);

        Epic actualEpic = (Epic) taskManager.getTaskById(updatedWorkEpic.getId());
        assertEquals(updatedWorkEpic, actualEpic);
    }

    @Test
    public void shouldUpdateSubtaskTest() {
        Epic workEpic = new Epic("Работка", "За денюжку");
        taskManager.createEpic(workEpic);
        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
                TaskStatus.NEW, workEpic.getId(), 7L, "17.05.2022 16:59");
        taskManager.createSubTask(pathToWorkSubTask);

        SubTask updatedPathToWorkSubTask = new SubTask(pathToWorkSubTask.getId(), "Доползти до офиса", TaskStatus.NEW,
                "Желательно без сильных опозданий", workEpic.getId(), 7L, "17.05.2022 16:59");
        taskManager.updateSubTask(updatedPathToWorkSubTask);

        SubTask actualSubTask = (SubTask) taskManager.getTaskById(updatedPathToWorkSubTask.getId());
        assertEquals(updatedPathToWorkSubTask, actualSubTask);
    }

    @Test
    public void shouldUpdateTaskTest() {
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW, 18L,
                "30.07.2022 16:59");
        taskManager.createTask(buyTask);

        Task updatedBuyTask = new Task(buyTask.getId(), "Метнуться до круглосутки", TaskStatus.NEW,
                "Взять пару - тройку пив", 18L, "30.07.2022 16:59");
        taskManager.updateTask(updatedBuyTask);

        Task actualTask = taskManager.getTaskById(updatedBuyTask.getId());
        assertEquals(updatedBuyTask, actualTask);
    }

}
