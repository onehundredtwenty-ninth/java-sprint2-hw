package ru.tasktracker.servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.tasktracker.manager.Managers;
import ru.tasktracker.manager.TaskManager;
import ru.tasktracker.tasks.Epic;
import ru.tasktracker.tasks.SubTask;
import ru.tasktracker.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {

    private HttpServer httpTaskServer;
    private static final int PORT = 8080;
    private static final TaskManager taskManager = Managers.getDefault();
    private static final Gson gson = new Gson();

    public HttpTaskServer() {
        try {
            httpTaskServer = HttpServer.create();
            httpTaskServer.bind(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpTaskServer.createContext("/tasks/task", new TaskHandler());
        httpTaskServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpTaskServer.createContext("/tasks/epic", new EpicHandler());
        httpTaskServer.createContext("/tasks/subtask/epic", new EpicSubtaskHandler());
        httpTaskServer.createContext("/tasks/history", new HistoryHandler());
        httpTaskServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String response;

            switch (method) {
                case "GET":
                    if (query != null) {
                        response = gson.toJson(taskManager.getTaskById(getIdFromQueryString(query)));
                    } else {
                        response = gson.toJson(taskManager.getAllTasks());
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    InputStream bodyIo = httpExchange.getRequestBody();
                    String body = new String(bodyIo.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.createTask(task);
                    response = "Task successfully created";
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "PUT":
                    InputStream bodyForUpdateIo = httpExchange.getRequestBody();
                    String bodyForUpdate = new String(bodyForUpdateIo.readAllBytes(), StandardCharsets.UTF_8);
                    Task updatedTask = gson.fromJson(bodyForUpdate, Task.class);
                    taskManager.updateTask(updatedTask);
                    response = "Task successfully updated";
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "DELETE":
                    if (query != null) {
                        taskManager.removeTaskById(getIdFromQueryString(query));
                        response = "Task successfully deleted";
                    } else {
                        taskManager.removeAllTasks();
                        response = "All tasks deleted";
                    }

                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + method);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String response;

            switch (method) {
                case "GET":
                    if (query != null) {
                        response = gson.toJson(taskManager.getTaskById(getIdFromQueryString(query)));
                    } else {
                        response = gson.toJson(taskManager.getAllSubTasks());
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    InputStream bodyIo = httpExchange.getRequestBody();
                    String body = new String(bodyIo.readAllBytes(), StandardCharsets.UTF_8);
                    SubTask subtask = gson.fromJson(body, SubTask.class);
                    taskManager.createSubTask(subtask);
                    response = "SubTask successfully created";
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "PUT":
                    InputStream bodyForUpdateIo = httpExchange.getRequestBody();
                    String bodyForUpdate = new String(bodyForUpdateIo.readAllBytes(), StandardCharsets.UTF_8);
                    SubTask updatedTask = gson.fromJson(bodyForUpdate, SubTask.class);
                    taskManager.updateSubTask(updatedTask);
                    response = "SubTask successfully updated";
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "DELETE":
                    if (query != null) {
                        taskManager.removeTaskById(getIdFromQueryString(query));
                        response = "SubTask successfully deleted";
                    } else {
                        taskManager.removeAllSubTasks();
                        response = "All subtasks deleted";
                    }

                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + method);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String response;

            switch (method) {
                case "GET":
                    if (query != null) {
                        response = gson.toJson(taskManager.getTaskById(getIdFromQueryString(query)));
                    } else {
                        response = gson.toJson(taskManager.getAllEpics());
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    InputStream bodyIo = httpExchange.getRequestBody();
                    String body = new String(bodyIo.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);
                    taskManager.createEpic(epic);
                    response = "Epic successfully created";
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "PUT":
                    InputStream bodyForUpdateIo = httpExchange.getRequestBody();
                    String bodyForUpdate = new String(bodyForUpdateIo.readAllBytes(), StandardCharsets.UTF_8);
                    Epic updatedEpic = gson.fromJson(bodyForUpdate, Epic.class);
                    taskManager.updateEpic(updatedEpic);
                    response = "Epic successfully updated";
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "DELETE":
                    if (query != null) {
                        taskManager.removeTaskById(getIdFromQueryString(query));
                        response = "Epic successfully deleted";
                    } else {
                        taskManager.removeAllEpics();
                        response = "All epics deleted";
                    }

                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + method);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = gson.toJson(taskManager.history());
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class EpicSubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            String response = gson.toJson(taskManager.getSubTasksFromEpic(getIdFromQueryString(query)));
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static int getIdFromQueryString(String query) {
        String[] queryParams = query.split("=");
        return Integer.parseInt(queryParams[1]);
    }
}
