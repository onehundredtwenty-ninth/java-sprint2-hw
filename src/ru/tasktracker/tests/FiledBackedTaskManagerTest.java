package ru.tasktracker.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tasktracker.manager.FileBackedTasksManager;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.tasks.Epic;

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
