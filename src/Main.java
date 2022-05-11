import ru.tasktracker.manager.HTTPTaskManager;
import ru.tasktracker.manager.Managers;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.servers.HttpTaskServer;
import ru.tasktracker.servers.KVServer;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();

        TaskManager taskManager = Managers.getDefault();
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

        HTTPTaskManager httpTaskManager = (HTTPTaskManager) Managers.getDefault();
        System.out.println(httpTaskManager.getAllTasks());
        System.out.println(httpTaskManager.getAllSubTasks());
        System.out.println(httpTaskManager.getAllEpics());
    }
}
