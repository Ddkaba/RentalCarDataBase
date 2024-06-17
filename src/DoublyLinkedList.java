class Node {
    String number;
    String driverLicense;
    String dateOfIssue;
    String returnDate;
    Node prev;
    Node next;

    public Node(String number, String driverLicense, String dateOfIssue, String returnDate) {
        this.number = number;
        this.driverLicense = driverLicense;
        this.dateOfIssue = dateOfIssue;
        this.returnDate = returnDate;
        this.prev = null;
        this.next = null;
    }
}

public class DoublyLinkedList {
    private Node head;

    public void addElement(String number, String driverLicense, String dateOfIssue, String returnDate) {
        // Создаем новый узел
        Node newNode = new Node(number, driverLicense, dateOfIssue, returnDate);

        // Если список пуст, новый узел становится головным
        if (head == null) {
            head = newNode;
            return;
        }
        // Начинаем с головы списка
        Node current = head;
        // Проходим по списку до последнего элемента
        while (current.next != null) {
            current = current.next;
        }
        // Добавляем новый узел в конец списка
        current.next = newNode;
        newNode.prev = current;
    }


    public void removeElement(String number) {
        Node current = head;
        // Проходим по списку, пока не найдем элемент с указанным номером или не достигнем конца списка
        while (current != null) {
            if (current.number.equals(number)) {
                // Если элемент найден, удаляем его из списка
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                }
                // После удаления элемента, сортируем список
                return;
            }
            current = current.next;
        }
    }

    public Node searchByNumber(String number) {
        Node current = head;
        while (current != null) {
            if (current.number.equals(number)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public Node searchByDriverLicense(String driverLicense) {
        Node current = head;
        while (current != null) {
            if (current.driverLicense.equals(driverLicense)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public void selectionSortByNumber() {
        if (head == null || head.next == null) {
            // Если список пуст или содержит только один элемент, нет необходимости в сортировке
            return;
        }

        Node current = head;
        while (current.next != null) {
            Node minNode = current;
            Node compareNode = current.next;
            // Находим узел с минимальным номером, сравнивая текущий узел с оставшимися
            while (compareNode != null) {
                if (compareNode.number.compareTo(minNode.number) < 0) {
                    minNode = compareNode;
                }
                compareNode = compareNode.next;
            }
            // Перемещаем узел с минимальным номером перед текущим узлом
            if (minNode != current) {
                swapNodes(current, minNode);
            }
            current = current.next;
        }
    }

    private void swapNodes(Node node1, Node node2) {
        // Обмениваем значения узлов между собой
        String tempNumber = node1.number;
        String tempDriverLicense = node1.driverLicense;
        String tempDateOfIssue = node1.dateOfIssue;
        String tempReturnDate = node1.returnDate;

        node1.number = node2.number;
        node1.driverLicense = node2.driverLicense;
        node1.dateOfIssue = node2.dateOfIssue;
        node1.returnDate = node2.returnDate;

        node2.number = tempNumber;
        node2.driverLicense = tempDriverLicense;
        node2.dateOfIssue = tempDateOfIssue;
        node2.returnDate = tempReturnDate;
    }
}
