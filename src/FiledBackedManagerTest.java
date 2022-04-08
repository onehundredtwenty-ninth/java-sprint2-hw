import java.nio.file.Paths;
import ru.tasktracker.manager.FileBackedTasksManager;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class FiledBackedManagerTest {

    public static void main(String[] args) {
        TaskManager initialTaskManager = new FileBackedTasksManager(
            Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));

        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        initialTaskManager.createTask(buyTask);
        initialTaskManager.createTask(keyTask);

        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        initialTaskManager.createEpic(workEpic);
        initialTaskManager.createEpic(relaxEpic);

        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
            TaskStatus.NEW, workEpic.getId());
        SubTask didWorkSubTask = new SubTask("Создать видимость работы", "Желательно правдоподобно",
            TaskStatus.NEW, workEpic.getId());
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "Интересный",
            TaskStatus.NEW, relaxEpic.getId());
        initialTaskManager.createSubTask(pathToWorkSubTask);
        initialTaskManager.createSubTask(didWorkSubTask);
        initialTaskManager.createSubTask(relaxSubTask);

        System.out.println(initialTaskManager.getTaskById(relaxSubTask.getId()));
        System.out.println(initialTaskManager.getSubTasksFromEpic(workEpic.getId()));
        initialTaskManager.getTaskById(keyTask.getId());
        initialTaskManager.getTaskById(buyTask.getId());
        initialTaskManager.getTaskById(buyTask.getId());
        initialTaskManager.getTaskById(buyTask.getId());
        initialTaskManager.getTaskById(workEpic.getId());
        initialTaskManager.getTaskById(workEpic.getId());
        initialTaskManager.getTaskById(relaxEpic.getId());

        pathToWorkSubTask.setStatus(TaskStatus.IN_PROGRESS);
        keyTask.setDescription("Только от аппартов");
        workEpic.setName("Любимая работка");
        initialTaskManager.updateSubTask(pathToWorkSubTask);
        initialTaskManager.updateTask(keyTask);
        initialTaskManager.updateEpic(workEpic);

        initialTaskManager.removeTaskById(workEpic.getId());
        System.out.println("Initial history: " + initialTaskManager.history());

        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(
            Paths.get("src/ru/tasktracker/storagefiles/StorageFile.txt"));

        if (!loadedTaskManager.getAllTasks().toString().equals(initialTaskManager.getAllTasks().toString())) {
            throw new AssertionError("Список задач выгруженный из файла не совпадает со списком задач в памяти");
        }

        if (!loadedTaskManager.getAllSubTasks().toString().equals(initialTaskManager.getAllSubTasks().toString())) {
            throw new AssertionError("Список сабтасок выгруженный из файла не совпадает со списком сабтасок в памяти");
        }

        System.out.println("Epics loaded from file: " + loadedTaskManager.getAllEpics());
        System.out.println("Tasks loaded from file: " + loadedTaskManager.getAllTasks());
        System.out.println("Subtasks loaded from file: " + loadedTaskManager.getAllSubTasks());
        System.out.println("History loaded from file: " + loadedTaskManager.history());
    }
}
