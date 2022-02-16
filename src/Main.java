import ru.tasktracker.manager.Manager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", manager.getNextTaskId(),
            TaskStatus.NEW);
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный",
            manager.getNextTaskId(), TaskStatus.NEW);
        manager.createTask(buyTask);
        manager.createTask(keyTask);

        Epic workEpic = new Epic("Работка", "За денюжку", manager.getNextTaskId());
        Epic relaxEpic = new Epic("Отдохнуть нормально", "Нормально!", manager.getNextTaskId());
        manager.createEpic(workEpic);
        manager.createEpic(relaxEpic);

        SubTask pathToWorkSubTask = new SubTask("Доползти до офиса", "Желательно без опозданий",
            manager.getNextTaskId(), TaskStatus.NEW, workEpic.getId());
        SubTask didWorkSubTask= new SubTask("Создать видимость работы", "Желательно правдоподобно",
            manager.getNextTaskId(), TaskStatus.NEW, workEpic.getId());
        SubTask relaxSubTask = new SubTask("Глянуть сериальчик", "", manager.getNextTaskId(),
            TaskStatus.NEW, relaxEpic.getId());
        manager.createSubTask(pathToWorkSubTask);
        manager.createSubTask(didWorkSubTask);
        manager.createSubTask(relaxSubTask);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getTaskById(relaxSubTask.getId()));
        System.out.println(manager.getSubTasksFromEpic(workEpic.getId()));

        pathToWorkSubTask.setStatus(TaskStatus.IN_PROGRESS);
        keyTask.setDescription("Только от аппартов");
        workEpic.setName("Любимая работка");
        manager.updateSubTask(pathToWorkSubTask);
        manager.updateTask(keyTask);
        manager.updateEpic(workEpic);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubTasks());

        manager.removeAllSubTasks();
        manager.removeTaskById(relaxEpic.getId());
        System.out.println(manager.getAllEpics());
        manager.removeAllEpics();
        manager.removeAllTasks();
    }
}
