package ru.tasktracker.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.tasktracker.node.Node;
import ru.tasktracker.tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final TasksLinkedList viewedTasks = new TasksLinkedList();

    @Override
    public void add(Task task) {
        viewedTasks.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks.getTasks();
    }

    @Override
    public void remove(int id) {
        viewedTasks.removeNode(id);
    }

    class TasksLinkedList {

        public Node<Task> head;
        public Node<Task> tail;
        private final Map<Integer, Node<Task>> nodesMap = new HashMap<>();

        public void linkLast(Task task) {
            if (nodesMap.containsKey(task.getId())) {
                viewedTasks.removeNode(nodesMap.get(task.getId()));
            }

            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            nodesMap.put(task.getId(), tail);
        }

        public List<Task> getTasks() {
            List<Task> result = new ArrayList<>();
            for (Node<Task> x = head; x != null; x = x.next) {
                result.add(x.item);
            }
            return result;
        }

        public void removeNode(Node<Task> nodeForRemove) {
            final Node<Task> next = nodeForRemove.next;
            final Node<Task> prev = nodeForRemove.prev;
            nodesMap.remove(nodeForRemove.item.getId());

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                nodeForRemove.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                nodeForRemove.next = null;
            }

            nodeForRemove.item = null;
        }

        public void removeNode(int id) {
            if (nodesMap.containsKey(id)) {
                removeNode(nodesMap.get(id));
            }
        }
    }

}
