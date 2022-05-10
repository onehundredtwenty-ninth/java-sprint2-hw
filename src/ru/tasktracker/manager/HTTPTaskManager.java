package ru.tasktracker.manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.tasktracker.clients.KVTaskClient;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(String serverUrl) {
        super(serverUrl);
        kvTaskClient = new KVTaskClient(serverUrl);
    }

    @Override
    protected void save() {
        JsonObject jsonTasksManager = new JsonObject();
        Gson gson = new Gson();

        JsonArray jsonTasksArray = new JsonArray();
        for (Task task : getAllTasks().values()) {
            jsonTasksArray.add(gson.toJson(task));
        }

        JsonArray jsonEpicsArray = new JsonArray();
        for (Epic epic : getAllEpics().values()) {
            jsonEpicsArray.add(gson.toJson(epic));
        }

        JsonArray jsonSubtasksArray = new JsonArray();
        for (SubTask subTask : getAllSubTasks().values()) {
            jsonSubtasksArray.add(gson.toJson(subTask));
        }

        JsonArray jsonHistoryArray = new JsonArray();
        for (Task task : history()) {
            jsonHistoryArray.add(task.getId());
        }

        jsonTasksManager.add("tasks", jsonTasksArray);
        jsonTasksManager.add("epics", jsonEpicsArray);
        jsonTasksManager.add("subtasks", jsonSubtasksArray);
        jsonTasksManager.add("history", jsonHistoryArray);
        kvTaskClient.put("manager", jsonTasksManager.toString());
    }

    protected HTTPTaskManager loadFromServer(String serverUrl) {
        Gson gson = new Gson();
        HTTPTaskManager httpTaskManager = new HTTPTaskManager(serverUrl);

        String returnedTaskManager = kvTaskClient.load(serverUrl);
        JsonObject returnedJsonTasksManager = gson.fromJson(returnedTaskManager, JsonObject.class);

        JsonArray jsonTasksArray = returnedJsonTasksManager.getAsJsonArray("tasks");
        for (JsonElement jsonTask : jsonTasksArray) {
            httpTaskManager.createTask(gson.fromJson(jsonTask, Task.class));
        }

        JsonArray jsonEpicsArray = returnedJsonTasksManager.getAsJsonArray("epics");
        for (JsonElement jsonEpic : jsonEpicsArray) {
            httpTaskManager.createEpic(gson.fromJson(jsonEpic, Epic.class));
        }

        JsonArray jsonSubtasksArray = returnedJsonTasksManager.getAsJsonArray("subtasks");
        for (JsonElement jsonSubtask : jsonSubtasksArray) {
            httpTaskManager.createSubTask(gson.fromJson(jsonSubtask, SubTask.class));
        }

        JsonArray jsonHistoryArray = returnedJsonTasksManager.getAsJsonArray("history");
        for (JsonElement jsonHistory : jsonHistoryArray) {
            httpTaskManager.historyManager.add(httpTaskManager.getTaskById(jsonHistory.getAsInt()));
        }
        return httpTaskManager;
    }
}
