import java.util.*;

public class AVLTree {
    private static class Node {
        Client client;
        Node left, right;
        int height;

        public Node(Client client) {
            this.client = client;
            this.height = 1;
        }
    }

    private Node root;

    public AVLTree() {
        this.root = null;
    }

    // Метод для добавления клиента в дерево
    public void insert(String driverLicenseNumber, String fullName, String passportData, String address) {
        this.root = insert(this.root, new Client(driverLicenseNumber, fullName, passportData, address));
    }
    private Node insert(Node node, Client client) {
        if (node == null) {
            return new Node(client);
        }
        // Вставка в соответствии с порядком номеров водительских удостоверений
        if (client.getDriverLicenseNumber().compareTo(node.client.getDriverLicenseNumber()) < 0) {
            node.left = insert(node.left, client);
        } else {
            node.right = insert(node.right, client);
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1; // Обновление высоты узла
        int balance = getBalance(node); // Балансировка дерева
        if (balance > 1 && client.getDriverLicenseNumber().compareTo(node.left.client.getDriverLicenseNumber()) < 0) {
            return rightRotate(node);
        }
        if (balance < -1 && client.getDriverLicenseNumber().compareTo(node.right.client.getDriverLicenseNumber()) > 0) {
            return leftRotate(node);
        }
        if (balance > 1 && client.getDriverLicenseNumber().compareTo(node.left.client.getDriverLicenseNumber()) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && client.getDriverLicenseNumber().compareTo(node.right.client.getDriverLicenseNumber()) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    // Метод для поиска клиента по фрагментам ФИО или адреса
    public List<Client> reverseSearch(String fragment) {
        List<Client> result = new ArrayList<>();
        search(root, fragment, result);
        return result;
    }

    private void search(Node node, String fragment, List<Client> result) {
        if (node == null) return;
        search(node.left, fragment, result); // Сначала обходим левое поддерево
        // Проверяем, содержит ли полное имя или адрес клиента заданный фрагмент
        if(StringContains.containsSubstring(node.client.getDriverLicenseNumber(), fragment)
                || StringContains.containsSubstring(node.client.getFullName(), fragment)
                || StringContains.containsSubstring(node.client.getAddress(), fragment)){
            result.add(node.client);
        }
        search(node.right, fragment, result); // Затем обходим правое поддерево
    }

    // Метод для удаления клиента из дерева по номеру водительского удостоверения
    public void delete(String driverLicenseNumber) {
        // Вызываем приватный метод delete, передавая корень дерева и номер водительского удостоверения для удаления
        this.root = delete(this.root, driverLicenseNumber);
    }

    // Приватный метод для удаления узла с заданным номером водительского удостоверения из поддерева с заданным корнем
    private Node delete(Node node, String driverLicenseNumber) {
        // Если узел пуст, выводим сообщение и возвращаем его
        if (node == null) {
            System.out.println("Узел с номером водительского удостоверения " + driverLicenseNumber + " не найден.");
            return node;
        }
        // Рекурсивно удаляем узел из левого или правого поддерева, если его ключ меньше или больше ключа узла соответственно
        if (driverLicenseNumber.compareTo(node.client.getDriverLicenseNumber()) < 0) {
            node.left = delete(node.left, driverLicenseNumber);
        } else if (driverLicenseNumber.compareTo(node.client.getDriverLicenseNumber()) > 0) {
            node.right = delete(node.right, driverLicenseNumber);
        } else { // Если ключ узла равен ключу для удаления
            // Если у узла нет одного из потомков или обоих
            if (node.left == null || node.right == null) {
                Node temp = (node.left == null) ? node.right : node.left;
                // Находим потомка, который существует
                if (temp == null) {
                    temp = node;
                    node = null;
                } else { node = temp; }
            } else { // Если у узла есть оба потомка
                // Находим узел с наименьшим ключом в правом поддереве
                Node temp = minValueNode(node.right);
                // Заменяем ключ текущего узла ключом найденного узла
                node.client = temp.client;
                // Рекурсивно удаляем узел с наименьшим ключом в правом поддереве
                node.right = delete(node.right, temp.client.getDriverLicenseNumber());
            }
        }
        // Если узел оказался null после удаления, возвращаем его
        if (node == null) { return node; }
        // Обновляем высоту узла
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        // Проверяем баланс и балансируем дерево при необходимости
        int balance = getBalance(node);
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    // Метод для вывода всех клиентов
    public void printAllClients() {
        System.out.println("Все клиенты:");
        printAllClients(root);
    }

    private void printAllClients(Node node) {
        if (node != null) {
            printAllClients(node.left); // Сначала обходим левое поддерево
            System.out.println("Номер водительского удостоверения: " + node.client.getDriverLicenseNumber());
            System.out.println("ФИО: " + node.client.getFullName());
            System.out.println("Паспортные данные: " + node.client.getPassportData());
            System.out.println("Адрес: " + node.client.getAddress() + "\n");
            printAllClients(node.right); // Затем обходим правое поддерево
        }
    }

    // Метод для поиска клиента по номеру водительского удостоверения
    public Client searchByDriverLicenseNumber(String driverLicenseNumber) {
        return searchByDriverLicenseNumber(root, driverLicenseNumber);
    }

    private Client searchByDriverLicenseNumber(Node node, String driverLicenseNumber) {
        if (node == null) return null;
        // Сначала обходим левое поддерево
        Client found = searchByDriverLicenseNumber(node.left, driverLicenseNumber);
        if (found != null) {
            return found; // Если клиент найден в левом поддереве, возвращаем его
        }
        // Проверяем текущий узел
        if (node.client.getDriverLicenseNumber().equals(driverLicenseNumber)) {
            return node.client;
        }
        // Затем обходим правое поддерево
        return searchByDriverLicenseNumber(node.right, driverLicenseNumber);
    }

    // Вспомогательные методы для балансировки AVL-дерева
    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
}


