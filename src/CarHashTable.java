public class CarHashTable {
    private static final int TABLE_SIZE = 100; // Размер хеш-таблицы
    private Car[] table; // Хеш-таблица для хранения данных об автомобилях

    public CarHashTable() {
        table = new Car[TABLE_SIZE];
    }

    // Метод для вычисления первой хеш-функции
    private int hash1(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = 31 * hash + key.charAt(i);
        }
        return Math.abs(hash) % TABLE_SIZE;
    }

    // Метод для вычисления второй хеш-функции
    private int hash2(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = 37 * hash + key.charAt(i);
        }
        return Math.abs(hash) % (TABLE_SIZE - 2) + 1; // Вторая хеш-функция не должна возвращать 0
    }

    // Метод для добавления автомобиля в хеш-таблицу
    public void put(String registrationNumber, String make, String color, int year, boolean availability) {
        int hash = hash1(registrationNumber);
        int stepSize = hash2(registrationNumber);
        // Если текущий слот занят, применяем двойное хеширование для нахождения нового места
        while (table[hash] != null) {
            hash = (hash + stepSize) % TABLE_SIZE;
        }
        // Вставляем автомобиль
        table[hash] = new Car(registrationNumber, make, color, year, availability);
    }

    // Метод для получения информации об автомобиле по его номеру
    public Car get(String registrationNumber) {
        int hash = hash1(registrationNumber);
        int stepSize = hash2(registrationNumber);

        // Поиск автомобиля по номеру в хеш-таблице
        while (table[hash] != null && !table[hash].getRegistrationNumber().equals(registrationNumber)) {
            hash = (hash + stepSize) % TABLE_SIZE;
        }

        // Возвращение информации об автомобиле, если найден
        if (table[hash] != null && table[hash].getRegistrationNumber().equals(registrationNumber)) {
            return table[hash];
        } else {
            return null; // Автомобиль не найден
        }
    }

    //Добавление нового автомобиля
    public void addCar(String registrationNumber, String make, String color, int year, boolean availability) {
        put(registrationNumber, make, color, year, availability);
    }

    //Удаление сведений об автомобиле
    public void removeCar(String registrationNumber) {
        int hash = hash1(registrationNumber);
        int stepSize = hash2(registrationNumber);
        // Поиск автомобиля по номеру в хеш-таблице
        while (table[hash] != null && !table[hash].getRegistrationNumber().equals(registrationNumber)) {
            hash = (hash + stepSize) % TABLE_SIZE;
        }
        // Если автомобиль найден, удаляем его
        if (table[hash] != null && table[hash].getRegistrationNumber().equals(registrationNumber)) {
            table[hash] = null;
            System.out.println("Авто удалено из базы");
        }
        else System.out.println("Автомобиль не найден");
    }

    //Просмотр автомобилей
    public void viewCars() {
        for (Car car : table) {
            if (car != null) {
               PrintCars(car);
            }
        }
    }

    public void PrintCars(Car car){
        System.out.println("\nНомерной знак: " + car.getRegistrationNumber());
        System.out.println("Марка и модель: " + car.getMake());
        System.out.println("Цвет: " + car.getColor());
        System.out.println("Год: " + car.getYear());
        System.out.println("Доступен: " + car.isAvailability());
    }

    //Поиск по ГРЗ
    public Car searchByRegistrationNumber(String registrationNumber) {
        return get(registrationNumber);
    }

    //Поиск по бренду
    public void searchByBrand(String brand) {
        for (Car car : table) {
            if (car != null && StringContains.containsSubstring(car.getMake(), brand)) {
                PrintCars(car); // Предполагается, что класс Car имеет переопределенный метод toString()
            }
        }
    }
}
