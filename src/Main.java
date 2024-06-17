import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        ConnectToDataBase connect = new ConnectToDataBase();
        List<Client> clientResult = new ArrayList<>();
        List<Car> carResult = new ArrayList<>();
        List<RentalInfo> rentalResult = new ArrayList<>();
        CarHashTable carHashTable = new CarHashTable();
        AVLTree avlTree = new AVLTree();
        DoublyLinkedList doublyLinkedList = new DoublyLinkedList();
        boolean menu = true;
        while (menu){
            System.out.println("\nВыбор функции:");
            System.out.println("1.Работа с клиентами");
            System.out.println("2.Работа с автомобилями");
            System.out.println("3.Завершение работы");
            if(clientResult.isEmpty()){
                connect.executeQuery("SELECT * FROM rental_cars.clients;",0, clientResult);
                for(Client client : clientResult){
                    avlTree.insert(client.getDriverLicenseNumber(), client.getFullName(), client.getPassportData(), client.getAddress());
                }
            }
            if(carResult.isEmpty()){
                connect.executeQuery("SELECT * FROM rental_cars.cars;",1, carResult);
                for(Car car : carResult){
                    carHashTable.put(car.getRegistrationNumber(), car.getMake(), car.getColor(), car.getYear(), car.isAvailability());
                }
            }
            if(rentalResult.isEmpty()){
                connect.executeQuery("SELECT * FROM rental_cars.rental_information;",2, rentalResult);
                for(RentalInfo rentalInfo : rentalResult){
                    doublyLinkedList.addElement(rentalInfo.getNumber(), rentalInfo.getDriversLicense(), rentalInfo.getDateOfIssue(), rentalInfo.getReturnDate());
                }
            }
            doublyLinkedList.selectionSortByNumber();
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("\nВыбор функции при работе с клиентами:");
                    System.out.println("1.Регистрация нового клиента");
                    System.out.println("2.Снятие с обслуживания клиента");
                    System.out.println("3.Просмотр всех зарегистрированных клиентов");
                    System.out.println("4.Поиск по номеру В/У");
                    System.out.println("5.Поиск по фрагменту ФИО или Адреса");
                    System.out.println("6.Назад");
                    switch (scanner.nextInt()) {
                        case 1:
                            newClient(scannerString,avlTree,connect);
                            break;
                        case 2:
                            deleteClient(scannerString,avlTree,connect, doublyLinkedList);
                            break;
                        case 3:
                            viewClient(avlTree);
                            break;
                        case 4:
                            searchDriveLicense(scannerString,avlTree,true, doublyLinkedList);
                            break;
                        case 5:
                            searchDriveLicense(scannerString,avlTree,false, doublyLinkedList);
                            break;
                    }
                    break;
                case 2:
                    System.out.println("\nВыбор функции при работе с автомобилями:");
                    System.out.println("1.Добавление нового автомобиля");
                    System.out.println("2.Удаление сведений об автомобиле");
                    System.out.println("3.Просмотр автомобилей");
                    System.out.println("4.Поиск по ГРЗ");
                    System.out.println("5.Поиск по бренду");
                    System.out.println("6.Отправка авто в ремонт");
                    System.out.println("7.Принятия авто из ремонта");
                    System.out.println("8.Выдача авто клиенту");
                    System.out.println("9.Возрат авто от клиента");
                    System.out.println("10.Назад");
                    switch (scanner.nextInt()) {
                        case 1:
                            newCar(scannerString, carHashTable, scanner, connect);
                            break;
                        case 2:
                            deleteCar(scannerString, carHashTable, connect, doublyLinkedList);
                            break;
                        case 3:
                            viewCar(carHashTable);
                            break;
                        case 4:
                            searchCarNumber(scannerString, carHashTable, doublyLinkedList, avlTree);
                            break;
                        case 5:
                            searchBrand(scannerString, carHashTable);
                            break;
                        case 6:
                            sendToWorkShop(scannerString, doublyLinkedList, carHashTable, connect);
                            break;
                        case 7:
                            backFromWorkShop(scannerString, doublyLinkedList, carHashTable, connect);
                            break;
                        case 8:
                            getCarToClient(scannerString, avlTree, carHashTable,doublyLinkedList, connect);
                            break;
                        case 9:
                            returnCar(scannerString, doublyLinkedList, connect, carHashTable);
                            break;
                    }
                    break;
                default: menu = false; break;
            }
        }
    }

    public static void newClient(Scanner scanner_String, AVLTree avlTree, ConnectToDataBase connect){
        System.out.println("\nРегистрация нового клиента");
        System.out.println("Введите данные клиента:");
        String License = null;
        do{
            License = regex("Введите данные В/У: Формат: 11 АА 111111", scanner_String, "\\d{2}\\s[АВЕКМНОРСТУХ]{2}\\s\\d{6}");
            if(!avlTree.reverseSearch(License).isEmpty()) System.out.println("Водитель с таким В/У уже есть");
        }
        while(!avlTree.reverseSearch(License).isEmpty());
        System.out.println("ФИО:");
        String FIO = scanner_String.nextLine();
        String Passport = regex("Паспортные данные: Формат: 1111 111111", scanner_String, "\\d{4}\\s\\d{6}");
        System.out.println("Адрес прописки:");
        String Address = scanner_String.nextLine();
        avlTree.insert(License, FIO, Passport , Address);
        connect.executeUpdate("INSERT INTO rental_cars.clients (`Driver's_license`, `FIO`, `Passport`, `Address`) " +
                "VALUES ('" + License + "','" + FIO + "','" + Passport + "','" + Address + "')");
        System.out.println("Новый клиент добавлен");
    }

    public static void deleteClient(Scanner scanner_String, AVLTree avlTree, ConnectToDataBase connect, DoublyLinkedList doublyLinkedList){
        System.out.println("\nСнятие с обслуживания клиента");
        String license = regex("Введите данные В/У: Формат: 11 АА 111111", scanner_String, "\\d{2}\\s[АВЕКМНОРСТУХ]{2}\\s\\d{6}");
        if(doublyLinkedList.searchByDriverLicense(license) == null){
           avlTree.delete(license);
           connect.executeUpdate("DELETE FROM rental_cars.clients WHERE (`Driver's_license` = '" + license + "');");
        }
        else System.out.println("У данного клиента находится автомобиль, \nдля удаления необходимо вернуть автомобиль");
    }

    public static void viewClient(AVLTree avlTree){
        System.out.println("\nПросмотр всех клиентов");
        avlTree.printAllClients();
    }

    public static void searchDriveLicense(Scanner scanner_String, AVLTree avlTree, boolean DL,DoublyLinkedList doublyLinkedList){
        String fragment;
        if(DL){
            System.out.println("\nПоиск по B/У");
            fragment = regex("Введите данные В/У: Формат: 11 АА 111111", scanner_String, "\\d{2}\\s[АВЕКМНОРСТУХ]{2}\\s\\d{6}");
        }
        else{
            System.out.println("\nПоиск по ФИО или Адресу");
            System.out.println("Введите ФИО или Адрес");
            fragment = scanner_String.nextLine();
        }
        List<Client> searchResult = avlTree.reverseSearch(fragment);
        if (!searchResult.isEmpty()) {
            System.out.println("Результаты поиска:");
            for (Client client : searchResult) {
                System.out.println("Номер водительского удостоверения: " + client.getDriverLicenseNumber());
                System.out.println("ФИО: " + client.getFullName());
                System.out.println("Паспортные данные: " + client.getPassportData());
                System.out.println("Адрес: " + client.getAddress());
                System.out.println();

                if(doublyLinkedList.searchByDriverLicense(client.getDriverLicenseNumber()) != null){
                    Node node = doublyLinkedList.searchByDriverLicense(client.getDriverLicenseNumber());
                    System.out.println("Номер выданной машины: " + node.number);
                }
            }
        } else {
            System.out.println("Клиенты с такими данными не найдены.");
        }
    }

    public static void newCar(Scanner scanner_String, CarHashTable carHashTable, Scanner scanner, ConnectToDataBase connect){
        System.out.println("\nДобавление нового автомобиля");
        String Number;
        do{
            Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                    "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
            if(carHashTable.searchByRegistrationNumber(Number) != null) System.out.println("ТС с таким номером уже в базе");
        }
        while(carHashTable.searchByRegistrationNumber(Number) != null);
        System.out.println("Введите Марку и Модель ТС:");
        String BrandModel = scanner_String.nextLine();
        System.out.println("Введите цвет:");
        String Color = scanner_String.nextLine();
        System.out.println("Введите год выпуска ТС:");
        int Year = scanner.nextInt();
        carHashTable.addCar(Number,BrandModel, Color, Year, true);
        connect.executeUpdate("INSERT INTO rental_cars.cars (`Number`, `Brand`, `Color`, `Year_Of_Manufacture`, `Enable`) " +
                "VALUES ('" + Number + "','" + BrandModel + "','" + Color + "','" + Year + "', '" + 1 + "')");
        System.out.println("Машина на базе");
    }

    public static void deleteCar(Scanner scanner_String, CarHashTable carHashTable, ConnectToDataBase connect, DoublyLinkedList doublyLinkedList){
        System.out.println("\nУдаление ТС");
        String Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
        if(doublyLinkedList.searchByNumber(Number) == null){
            carHashTable.removeCar(Number);
            connect.executeUpdate("DELETE FROM rental_cars.cars WHERE (`Number` = '" + Number + "');");
        }
        else System.out.println("Данный автомобиль находится у клиента, для удаления необходимо вернуть автомобиль");
    }

    public static void viewCar(CarHashTable carHashTable){
        System.out.println("\nПросмотр автомобилей");
        carHashTable.viewCars();
    }

    public static void searchCarNumber(Scanner scanner_String, CarHashTable carHashTable, DoublyLinkedList doublyLinkedList, AVLTree avlTree){
        System.out.println("\nПоиск по ГРЗ");
        String Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
        Car car = carHashTable.searchByRegistrationNumber(Number);
        if (car != null){
            carHashTable.PrintCars(car);
            if(doublyLinkedList.searchByNumber(car.getRegistrationNumber()) != null){
                Node node = doublyLinkedList.searchByNumber(car.getRegistrationNumber());
                List<Client> searchResult = avlTree.reverseSearch(node.driverLicense);
                if (!searchResult.isEmpty()) {
                    System.out.println("\nРезультаты поиска:");
                    for (Client client : searchResult) {
                        System.out.println("Номер водительского удостоверения: " + client.getDriverLicenseNumber());
                        System.out.println("ФИО: " + client.getFullName());
                    }
                }
            }
        }
        else System.out.println("Автомобиль не найден");
    }

    public static void searchBrand(Scanner scanner_String, CarHashTable carHashTable){
        System.out.println("\nПоиск по бренду");
        System.out.println("Введите марку ТС:");
        String brand = scanner_String.nextLine();
        carHashTable.searchByBrand(brand);
    }

    public static void sendToWorkShop(Scanner scanner_String, DoublyLinkedList doublyLinkedList, CarHashTable carHashTable, ConnectToDataBase connect){
        System.out.println("\nОтправлка в ремонт");
        String Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
        if(doublyLinkedList.searchByNumber(Number) == null && carHashTable.searchByRegistrationNumber(Number) != null){
            if(carHashTable.searchByRegistrationNumber(Number).isAvailability()){
                carHashTable.searchByRegistrationNumber(Number).setAvailability(false);
                connect.executeUpdate("UPDATE rental_cars.cars SET `Enable` = '0' WHERE (`Number` = '" + Number + "');");
                System.out.println("Автомобиль отправлен в ремонт");
            }
            else System.out.println("Автомобиль недоступен");
        }
        else System.out.println("Возникла проблема с отправкой");
    }

    public static void backFromWorkShop(Scanner scanner_String, DoublyLinkedList doublyLinkedList, CarHashTable carHashTable, ConnectToDataBase connect){
        System.out.println("\nПолучение автомобиля из ремонта");
        String Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
        if(doublyLinkedList.searchByNumber(Number) == null && carHashTable.searchByRegistrationNumber(Number) != null) {
            if (!carHashTable.searchByRegistrationNumber(Number).isAvailability()) {
                carHashTable.searchByRegistrationNumber(Number).setAvailability(true);
                connect.executeUpdate("UPDATE rental_cars.cars SET `Enable` = '1' WHERE (`Number` = '" + Number + "');");
                System.out.println("Автомобиль возвращен из ремонта");
            } else System.out.println("Автомобиль уже у нас");
        }  else System.out.println("Возникла проблема с возвратом");
    }

    public static void getCarToClient(Scanner scanner_String, AVLTree avlTree, CarHashTable carHashTable, DoublyLinkedList doublyLinkedList, ConnectToDataBase connect){
        System.out.println("\nВыдача автомобиля клиенту");
        String License = regex("Введите данные В/У: Формат: 11 АА 111111", scanner_String, "\\d{2}\\s[АВЕКМНОРСТУХ]{2}\\s\\d{6}");
        if(avlTree.reverseSearch(License).isEmpty()) System.out.println("Клиент не найден");
        else{
            String Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                    "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
            if(carHashTable.searchByRegistrationNumber(Number) == null ) System.out.println("Автомобиль не найден");
            else{
                if(!carHashTable.searchByRegistrationNumber(Number).isAvailability()){
                    if(doublyLinkedList.searchByNumber(Number) == null)System.out.println("Автомобиль в ремонте");
                    else System.out.println("Автомобиль у клиента");
                }
                else{
                    LocalDate currentDate = LocalDate.now();
                    System.out.println("Насколько дней?");
                    int Day = scanner_String.nextInt();
                    carHashTable.searchByRegistrationNumber(Number).setAvailability(false);
                    connect.executeUpdate("UPDATE rental_cars.cars SET `Enable` = '0' WHERE (`Number` = '" + Number + "');");
                    connect.executeUpdate("INSERT INTO `rental_cars`.`rental_information` (`Number`, `Driver's_License`, `Date_Of_Issue`, `Return_Date`) " +
                            "VALUES ('" + Number + "','" + License + "','" + currentDate.toString() + "','" + currentDate.plusDays(Day).toString() + "')");
                    doublyLinkedList.addElement(Number, License, currentDate.toString(), currentDate.plusDays(Day).toString());
                }
            }
        }
    }

    public static void returnCar(Scanner scanner_String, DoublyLinkedList doublyLinkedList, ConnectToDataBase connect, CarHashTable carHashTable){
        System.out.println("\nВозврат авто от клиента");
        String Number = regex("Введите номер ТС: Формат: A111AA-78", scanner_String ,
                "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}-\\d{2}");
        if(doublyLinkedList.searchByNumber(Number) == null) System.out.println("Авто не найден");
        else{
            connect.executeUpdate("DELETE FROM rental_cars.rental_information WHERE (`Number` = '" + Number + "');");
            doublyLinkedList.removeElement(Number);
            carHashTable.searchByRegistrationNumber(Number).setAvailability(true);
            connect.executeUpdate("UPDATE rental_cars.cars SET `Enable` = '1' WHERE (`Number` = '" + Number + "');");
            System.out.println("Запись удалена");
        }
    }

    public static String regex(String message, Scanner scanner_String, String regex){
        String fragment;
        do{
            System.out.println(message);
            fragment = scanner_String.nextLine();
        }
        while(!Pattern.compile(regex).matcher(fragment).matches());
        return fragment;
    }
}
