package ru.tasktracker.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.TaskStatus;

public class EpicStatusTest {

  @Test
  public void epicStatusWithoutSubtasksTest() {
    Epic workEpic = new Epic("Работка", "За денюжку");

    assertEquals(TaskStatus.NEW, workEpic.getStatus());
  }

  @Test
  public void epicStatusWithSubtasksInNewStatusTest() {
    Epic workEpic = new Epic("Работка", "За денюжку");
    SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
        TaskStatus.NEW, workEpic.getId());
    SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
        TaskStatus.NEW, workEpic.getId());

    ArrayList<SubTask> subtasks = new ArrayList<>();
    subtasks.add(pathToWorkSubTask);
    subtasks.add(didWorkSubTask);
    workEpic.setSubTasks(subtasks);

    assertEquals(TaskStatus.NEW, workEpic.getStatus());
  }

  @Test
  public void epicStatusWithSubtasksInDoneStatusTest() {
    Epic workEpic = new Epic("Работка", "За денюжку");
    SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
        TaskStatus.DONE, workEpic.getId());
    SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
        TaskStatus.DONE, workEpic.getId());

    ArrayList<SubTask> subtasks = new ArrayList<>();
    subtasks.add(pathToWorkSubTask);
    subtasks.add(didWorkSubTask);
    workEpic.setSubTasks(subtasks);

    assertEquals(TaskStatus.DONE, workEpic.getStatus());
  }

  @Test
  public void epicStatusWithSubtasksInDoneAndNewStatusTest() {
    Epic workEpic = new Epic("Работка", "За денюжку");
    SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
        TaskStatus.NEW, workEpic.getId());
    SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
        TaskStatus.DONE, workEpic.getId());

    ArrayList<SubTask> subtasks = new ArrayList<>();
    subtasks.add(pathToWorkSubTask);
    subtasks.add(didWorkSubTask);
    workEpic.setSubTasks(subtasks);

    assertEquals(TaskStatus.IN_PROGRESS, workEpic.getStatus());
  }

  @Test
  public void epicStatusWithSubtasksInProgressStatusTest() {
    Epic workEpic = new Epic("Работка", "За денюжку");
    SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
        TaskStatus.IN_PROGRESS, workEpic.getId());
    SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
        TaskStatus.IN_PROGRESS, workEpic.getId());

    ArrayList<SubTask> subtasks = new ArrayList<>();
    subtasks.add(pathToWorkSubTask);
    subtasks.add(didWorkSubTask);
    workEpic.setSubTasks(subtasks);

    assertEquals(TaskStatus.IN_PROGRESS, workEpic.getStatus());
  }

}
