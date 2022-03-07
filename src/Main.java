import ru.tasktracker.manager.InMemoryTaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        inMemoryTaskManager.createTask(buyTask);
        inMemoryTaskManager.createTask(keyTask);

        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        inMemoryTaskManager.createEpic(workEpic);
        inMemoryTaskManager.createEpic(relaxEpic);

        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
             TaskStatus.NEW, workEpic.getId());
        SubTask didWorkSubTask= new SubTask("Создать видимость работы", "Желательно правдоподобно",
            TaskStatus.NEW, workEpic.getId());
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "",
            TaskStatus.NEW, relaxEpic.getId());
        inMemoryTaskManager.createSubTask(pathToWorkSubTask);
        inMemoryTaskManager.createSubTask(didWorkSubTask);
        inMemoryTaskManager.createSubTask(relaxSubTask);

        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getTaskById(relaxSubTask.getId()));
        System.out.println(inMemoryTaskManager.getSubTasksFromEpic(workEpic.getId()));

        pathToWorkSubTask.setStatus(TaskStatus.IN_PROGRESS);
        keyTask.setDescription("Только от аппартов");
        workEpic.setName("Любимая работка");
        inMemoryTaskManager.updateSubTask(pathToWorkSubTask);
        inMemoryTaskManager.updateTask(keyTask);
        inMemoryTaskManager.updateEpic(workEpic);
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());

        inMemoryTaskManager.removeAllSubTasks();
        inMemoryTaskManager.removeTaskById(relaxEpic.getId());
        System.out.println(inMemoryTaskManager.getAllEpics());
        inMemoryTaskManager.removeAllEpics();
        inMemoryTaskManager.removeAllTasks();
    }
}
