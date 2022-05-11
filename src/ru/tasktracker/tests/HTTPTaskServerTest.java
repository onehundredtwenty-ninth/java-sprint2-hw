package ru.tasktracker.tests;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tasktracker.servers.HttpTaskServer;
import ru.tasktracker.servers.KVServer;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;
import ru.tasktracker.tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private final String serverUrl = "http://localhost:8080";
    private final HttpClient client = HttpClient.newHttpClient();
    private KVServer kvServer;
    private final Gson gson = new Gson();
    private final Task initialFirstTask = new Task(1, "Метнуться до круглосутки", TaskStatus.NEW,
            "Взять пару пив", 7L, "19.06.2022 16:59");
    private final Task initialSecondTask = new Task(2, "Сделать дубликат ключа", TaskStatus.NEW,
            "От апартов и подъездный", 7L, "27.05.2022 16:59");
    private final Epic initialFirstEpic = new Epic(3, "Работка", "За денюжку");
    private final Epic initialSecondEpic = new Epic(4, "Отдохнуть нормально", "Нормально!");
    private SubTask initialFirstSubtask = new SubTask(5, "Доползти до офиса", TaskStatus.NEW,
            "Желательно без опозданий", initialFirstEpic.getId(), 7L, "17.05.2022 16:59");;
    private SubTask initialSecondSubtask = new SubTask(6, "Создать видимость работы", TaskStatus.NEW,
            "Желательно правдоподобно", initialFirstEpic.getId(), 9L, "19.05.2022 16:59");

    @BeforeEach
    public void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        URI url = URI.create(serverUrl + "/tasks/task/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Task successfully created", response.body());
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/task/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        HttpRequest POSTRequest = HttpRequest.newBuilder().uri(urlForPostRequest).POST(body).build();
        HttpResponse<String> responseOfPostRequest = client.send(POSTRequest, HttpResponse.BodyHandlers.ofString());
        Task expectedTask = initialFirstTask;

        assertEquals(201, responseOfPostRequest.statusCode());
        assertEquals("Task successfully created", responseOfPostRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/task/?id=" + expectedTask.getId());
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        Task actualTask = gson.fromJson(responseOfGetRequest.body(), Task.class);
        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        URI url = URI.create(serverUrl + "/tasks/task/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Task successfully updated", response.body());
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/task/");
        final HttpRequest.BodyPublisher bodyOfFirstTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        final HttpRequest.BodyPublisher bodyOfSecondTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialSecondTask));
        HttpRequest POSTRequestForFirstTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfFirstTask).build();
        HttpRequest POSTRequestForSecondTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfSecondTask).build();
        client.send(POSTRequestForFirstTask, HttpResponse.BodyHandlers.ofString());
        client.send(POSTRequestForSecondTask, HttpResponse.BodyHandlers.ofString());

        Task[] expectedTasks = new Task[]{initialFirstTask, initialSecondTask};

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/task/");
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        Task[] actualTasks = gson.fromJson(responseOfGetRequest.body(), Task[].class);

        assertEquals(200, responseOfGetRequest.statusCode());
        Assertions.assertArrayEquals(expectedTasks, actualTasks);
    }

    @Test
    public void getDeleteTaskById() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/task/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        HttpRequest POSTRequest = HttpRequest.newBuilder().uri(urlForPostRequest).POST(body).build();
        HttpResponse<String> responseOfPostRequest = client.send(POSTRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseOfPostRequest.statusCode());
        assertEquals("Task successfully created", responseOfPostRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/task/?id=" + initialFirstTask.getId());
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).DELETE().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals("Task successfully deleted", responseOfGetRequest.body());
    }

    @Test
    public void deleteAllTasks() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/task/");
        final HttpRequest.BodyPublisher bodyOfFirstTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        final HttpRequest.BodyPublisher bodyOfSecondTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstTask));
        HttpRequest POSTRequestForFirstTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfFirstTask).build();
        HttpRequest POSTRequestForSecondTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfSecondTask).build();
        client.send(POSTRequestForFirstTask, HttpResponse.BodyHandlers.ofString());
        client.send(POSTRequestForSecondTask, HttpResponse.BodyHandlers.ofString());

        URI urlForDeleteRequest = URI.create(serverUrl + "/tasks/task/");
        HttpRequest DeleteRequest = HttpRequest.newBuilder().uri(urlForDeleteRequest).DELETE().build();
        HttpResponse<String> responseOfDeleteRequest = client.send(DeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfDeleteRequest.statusCode());
        assertEquals("All tasks deleted", responseOfDeleteRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/task/");
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        Task[] actualTasks = gson.fromJson(responseOfGetRequest.body(), Task[].class);

        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals(0, actualTasks.length);
    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        URI url = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Epic successfully created", response.body());
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest POSTRequest = HttpRequest.newBuilder().uri(urlForPostRequest).POST(body).build();
        HttpResponse<String> responseOfPostRequest = client.send(POSTRequest, HttpResponse.BodyHandlers.ofString());
        Epic expectedTask = initialFirstEpic;

        assertEquals(201, responseOfPostRequest.statusCode());
        assertEquals("Epic successfully created", responseOfPostRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/epic/?id=" + expectedTask.getId());
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        Epic actualTask = gson.fromJson(responseOfGetRequest.body(), Epic.class);
        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void updateEpic() throws IOException, InterruptedException {
        URI url = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Epic successfully updated", response.body());
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfFirstTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        final HttpRequest.BodyPublisher bodyOfSecondTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialSecondEpic));
        HttpRequest POSTRequestForFirstTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfFirstTask).build();
        HttpRequest POSTRequestForSecondTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfSecondTask).build();
        client.send(POSTRequestForFirstTask, HttpResponse.BodyHandlers.ofString());
        client.send(POSTRequestForSecondTask, HttpResponse.BodyHandlers.ofString());

        Epic[] expectedTasks = new Epic[]{initialFirstEpic, initialSecondEpic};

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/epic/");
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        Epic[] actualTasks = gson.fromJson(responseOfGetRequest.body(), Epic[].class);

        assertEquals(200, responseOfGetRequest.statusCode());
        Assertions.assertArrayEquals(expectedTasks, actualTasks);
    }

    @Test
    public void getDeleteEpicById() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest POSTRequest = HttpRequest.newBuilder().uri(urlForPostRequest).POST(body).build();
        HttpResponse<String> responseOfPostRequest = client.send(POSTRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseOfPostRequest.statusCode());
        assertEquals("Epic successfully created", responseOfPostRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/epic/?id=" + initialFirstEpic.getId());
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).DELETE().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals("Epic successfully deleted", responseOfGetRequest.body());
    }

    @Test
    public void deleteAllEpics() throws IOException, InterruptedException {
        URI urlForPostRequest = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfFirstTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        final HttpRequest.BodyPublisher bodyOfSecondTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest POSTRequestForFirstTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfFirstTask).build();
        HttpRequest POSTRequestForSecondTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfSecondTask).build();
        client.send(POSTRequestForFirstTask, HttpResponse.BodyHandlers.ofString());
        client.send(POSTRequestForSecondTask, HttpResponse.BodyHandlers.ofString());

        URI urlForDeleteRequest = URI.create(serverUrl + "/tasks/epic/");
        HttpRequest DeleteRequest = HttpRequest.newBuilder().uri(urlForDeleteRequest).DELETE().build();
        HttpResponse<String> responseOfDeleteRequest = client.send(DeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfDeleteRequest.statusCode());
        assertEquals("All epics deleted", responseOfDeleteRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/epic/");
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        Epic[] actualTasks = gson.fromJson(responseOfGetRequest.body(), Epic[].class);

        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals(0, actualTasks.length);
    }

    @Test
    public void createSubtask() throws IOException, InterruptedException {
        URI urlForEpicCreation = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest requestForEpicCreation = HttpRequest.newBuilder().uri(urlForEpicCreation).POST(bodyOfEpic).build();
        client.send(requestForEpicCreation, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create(serverUrl + "/tasks/subtask/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstSubtask));
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("SubTask successfully created", response.body());
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        URI urlForEpicCreation = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest requestForEpicCreation = HttpRequest.newBuilder().uri(urlForEpicCreation).POST(bodyOfEpic).build();
        client.send(requestForEpicCreation, HttpResponse.BodyHandlers.ofString());

        URI urlForPostRequest = URI.create(serverUrl + "/tasks/subtask/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialSecondSubtask));
        HttpRequest POSTRequest = HttpRequest.newBuilder().uri(urlForPostRequest).POST(body).build();
        HttpResponse<String> responseOfPostRequest = client.send(POSTRequest, HttpResponse.BodyHandlers.ofString());
        SubTask expectedTask = initialSecondSubtask;

        assertEquals(201, responseOfPostRequest.statusCode());
        assertEquals("SubTask successfully created", responseOfPostRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/subtask/?id=" + expectedTask.getId());
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        SubTask actualTask = gson.fromJson(responseOfGetRequest.body(), SubTask.class);
        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void updateSubtask() throws IOException, InterruptedException {
        URI urlForEpicCreation = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest requestForEpicCreation = HttpRequest.newBuilder().uri(urlForEpicCreation).POST(bodyOfEpic).build();
        client.send(requestForEpicCreation, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create(serverUrl + "/tasks/subtask/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstSubtask));
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("SubTask successfully updated", response.body());
    }

    @Test
    public void getAllSubtasks() throws IOException, InterruptedException {
        URI urlForEpicCreation = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest requestForEpicCreation = HttpRequest.newBuilder().uri(urlForEpicCreation).POST(bodyOfEpic).build();
        client.send(requestForEpicCreation, HttpResponse.BodyHandlers.ofString());

        URI urlForPostRequest = URI.create(serverUrl + "/tasks/subtask/");
        final HttpRequest.BodyPublisher bodyOfFirstTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstSubtask));
        final HttpRequest.BodyPublisher bodyOfSecondTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialSecondSubtask));
        HttpRequest POSTRequestForFirstTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfFirstTask).build();
        HttpRequest POSTRequestForSecondTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfSecondTask).build();
        client.send(POSTRequestForFirstTask, HttpResponse.BodyHandlers.ofString());
        client.send(POSTRequestForSecondTask, HttpResponse.BodyHandlers.ofString());

        SubTask[] expectedTasks = new SubTask[]{initialFirstSubtask, initialSecondSubtask};

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/subtask/");
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        SubTask[] actualTasks = gson.fromJson(responseOfGetRequest.body(), SubTask[].class);

        assertEquals(200, responseOfGetRequest.statusCode());
        Assertions.assertArrayEquals(expectedTasks, actualTasks);
    }

    @Test
    public void getDeleteSubtaskById() throws IOException, InterruptedException {
        URI urlForEpicCreation = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest requestForEpicCreation = HttpRequest.newBuilder().uri(urlForEpicCreation).POST(bodyOfEpic).build();
        client.send(requestForEpicCreation, HttpResponse.BodyHandlers.ofString());

        URI urlForPostRequest = URI.create(serverUrl + "/tasks/subtask/");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstSubtask));
        HttpRequest POSTRequest = HttpRequest.newBuilder().uri(urlForPostRequest).POST(body).build();
        HttpResponse<String> responseOfPostRequest = client.send(POSTRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseOfPostRequest.statusCode());
        assertEquals("SubTask successfully created", responseOfPostRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/subtask/?id=" + initialFirstEpic.getId());
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).DELETE().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals("SubTask successfully deleted", responseOfGetRequest.body());
    }

    @Test
    public void deleteAllSubtasks() throws IOException, InterruptedException {
        URI urlForEpicCreation = URI.create(serverUrl + "/tasks/epic/");
        final HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstEpic));
        HttpRequest requestForEpicCreation = HttpRequest.newBuilder().uri(urlForEpicCreation).POST(bodyOfEpic).build();
        client.send(requestForEpicCreation, HttpResponse.BodyHandlers.ofString());

        URI urlForPostRequest = URI.create(serverUrl + "/tasks/subtask/");
        final HttpRequest.BodyPublisher bodyOfFirstTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstSubtask));
        final HttpRequest.BodyPublisher bodyOfSecondTask = HttpRequest.BodyPublishers.ofString(gson.toJson(initialFirstSubtask));
        HttpRequest POSTRequestForFirstTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfFirstTask).build();
        HttpRequest POSTRequestForSecondTask = HttpRequest.newBuilder().uri(urlForPostRequest).POST(bodyOfSecondTask).build();
        client.send(POSTRequestForFirstTask, HttpResponse.BodyHandlers.ofString());
        client.send(POSTRequestForSecondTask, HttpResponse.BodyHandlers.ofString());

        URI urlForDeleteRequest = URI.create(serverUrl + "/tasks/subtask/");
        HttpRequest DeleteRequest = HttpRequest.newBuilder().uri(urlForDeleteRequest).DELETE().build();
        HttpResponse<String> responseOfDeleteRequest = client.send(DeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfDeleteRequest.statusCode());
        assertEquals("All subtasks deleted", responseOfDeleteRequest.body());

        URI urlForGetRequest = URI.create(serverUrl + "/tasks/subtask/");
        HttpRequest GETRequest = HttpRequest.newBuilder().uri(urlForGetRequest).GET().build();
        HttpResponse<String> responseOfGetRequest = client.send(GETRequest, HttpResponse.BodyHandlers.ofString());
        SubTask[] actualTasks = gson.fromJson(responseOfGetRequest.body(), SubTask[].class);

        assertEquals(200, responseOfGetRequest.statusCode());
        assertEquals(0, actualTasks.length);
    }

}
