import com.google.gson.Gson;
import ru.tasktracker.clients.KVTaskClient;
import ru.tasktracker.servers.HttpTaskServer;
import ru.tasktracker.servers.KVServer;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
        new KVServer().start();

        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");
        Task buyTask = new Task("Метнуться до круглосутки", "Взять пару пив", TaskStatus.NEW, 18L,
                "28.05.2022 16:59");
        Task keyTask = new Task("Сделать дубликат ключа", "От апартов и подъездный", TaskStatus.NEW, 18L,
                "28.05.2022 16:59");
        Gson gson = new Gson();
        kvTaskClient.put("1", gson.toJson(buyTask));
        kvTaskClient.put("2", gson.toJson(keyTask));
        System.out.println(kvTaskClient.load("1"));
        System.out.println(kvTaskClient.load("2"));
    }
}
