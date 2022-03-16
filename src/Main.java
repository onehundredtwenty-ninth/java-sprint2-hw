import ru.tasktracker.manager.Managers;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW);
        taskManager.createTask(buyTask);
        taskManager.createTask(keyTask);

        Epic workEpic = new Epic("Работка", "За денюжку");
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!");
        taskManager.createEpic(workEpic);
        taskManager.createEpic(relaxEpic);

        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
             TaskStatus.NEW, workEpic.getId());
        SubTask didWorkSubTask= new SubTask("Создать видимость работы", "Желательно правдоподобно",
            TaskStatus.NEW, workEpic.getId());
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "",
            TaskStatus.NEW, relaxEpic.getId());
        taskManager.createSubTask(pathToWorkSubTask);
        taskManager.createSubTask(didWorkSubTask);
        taskManager.createSubTask(relaxSubTask);

        System.out.println(taskManager.getTaskById(relaxSubTask.getId()));
        System.out.println(taskManager.getSubTasksFromEpic(workEpic.getId()));
        taskManager.getTaskById(keyTask.getId());
        taskManager.getTaskById(buyTask.getId());
        taskManager.getTaskById(buyTask.getId());
        taskManager.getTaskById(buyTask.getId());
        taskManager.getTaskById(workEpic.getId());
        taskManager.getTaskById(workEpic.getId());
        taskManager.getTaskById(relaxEpic.getId());

        pathToWorkSubTask.setStatus(TaskStatus.IN_PROGRESS);
        keyTask.setDescription("Только от аппартов");
        workEpic.setName("Любимая работка");
        taskManager.updateSubTask(pathToWorkSubTask);
        taskManager.updateTask(keyTask);
        taskManager.updateEpic(workEpic);

        System.out.println("History: " + taskManager.history());

        taskManager.removeTaskById(buyTask.getId());
        taskManager.removeTaskById(workEpic.getId());
        System.out.println("History: " + taskManager.history());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
    }
}
