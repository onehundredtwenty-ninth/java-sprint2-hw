package ru.tasktracker.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import ru.tasktracker.node.Node;
import ru.tasktracker.tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final TasksLinkedList viewedTasks = new TasksLinkedList();
    private static final int HISTORY_LENGTH = 10;
    private final Map<Integer, Node<Task>> nodesMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (viewedTasks.size() == HISTORY_LENGTH && !nodesMap.containsKey(task.getId())) {
            viewedTasks.removeFirst();
            viewedTasks.linkLast(task);
        }

        if (nodesMap.containsKey(task.getId())) {
            viewedTasks.remove(task);
        }
        viewedTasks.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks.getTasks();
    }

    @Override
    public void remove(int id) {

    }

    class TasksLinkedList {

        public Node<Task> head;
        public Node<Task> tail;
        private int size;

        public void linkLast(Task task) {
            final Node<Task> l = tail;
            final Node<Task> newNode = new Node<>(l, task, null);
            tail = newNode;
            if (l == null) {
                head = newNode;
            } else {
                l.next = newNode;
            }
            size++;
            nodesMap.put(task.getId(), tail);
        }

        public List<Task> getTasks() {
            List<Task> result = new ArrayList<>();
            for (Node<Task> x = head; x != null; x = x.next) {
                result.add(x.item);
            }
            return result;
        }

        public void remove(Task task) {
            removeNode(nodesMap.get(task.getId()));
        }

        public void removeFirst() {
            final Node<Task> f = head;
            if (f == null) {
                throw new NoSuchElementException();
            }
            removeNode(f);
        }

        public void removeNode(Node<Task> x) {
            final Node<Task> next = x.next;
            final Node<Task> prev = x.prev;
            nodesMap.remove(x.item.getId());

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.item = null;
            size--;
        }

        public int size() {
            return size;
        }
    }

}
