import java.util.*;

 class Car {
    private String licensePlate;
    private String model;
    private boolean isRented;

    public Car(String licensePlate, String model) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.isRented = false;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getModel() { return model; }
    public boolean isRented() { return isRented; }

    public void rent() { isRented = true; }
    public void returnCar() { isRented = false; }
}

class Customer {
    private String id;
    private String name;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
}

class RentalTransaction {
    private Car car;
    private Customer customer;
    private Date rentalDate;
    private Date returnDate;

    public RentalTransaction(Car car, Customer customer, Date rentalDate) {
        this.car = car;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.returnDate = null;
    }

    public void returnCar(Date returnDate) {
        this.returnDate = returnDate;
        car.returnCar();
    }

    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public Date getRentalDate() { return rentalDate; }
    public Date getReturnDate() { return returnDate; }
}

class RentalAgency {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<RentalTransaction> transactions = new ArrayList<>();

    public void addCar(Car car) { cars.add(car); }
    public void addCustomer(Customer customer) { customers.add(customer); }

    public Car findAvailableCar(String model) {
        for (Car car : cars) {
            if (car.getModel().equalsIgnoreCase(model) && !car.isRented()) {
                return car;
            }
        }
        return null;
    }

    public Customer findCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public RentalTransaction rentCar(String customerId, String model) {
        Customer customer = findCustomerById(customerId);
        Car car = findAvailableCar(model);
        if (customer != null && car != null) {
            car.rent();
            RentalTransaction transaction = new RentalTransaction(car, customer, new Date());
            transactions.add(transaction);
            return transaction;
        }
        return null;
    }

    public boolean returnCar(String licensePlate) {
        for (RentalTransaction t : transactions) {
            if (t.getCar().getLicensePlate().equals(licensePlate) && t.getReturnDate() == null) {
                t.returnCar(new Date());
                return true;
            }
        }
        return false;
    }

    public List<Car> getCars() { return cars; }
    public List<Customer> getCustomers() { return customers; }
    public List<RentalTransaction> getTransactions() { return transactions; }
}

class Main {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tries = 3;
        boolean loggedIn = false;

        while (tries > 0 && !loggedIn) {
            System.out.print("Enter username: ");
            String user = scanner.nextLine();
            System.out.print("Enter password: ");
            String pass = readPassword();
            System.out.println();

            if (user.equals(USERNAME) && pass.equals(PASSWORD)) {
                System.out.println("Login successful!");
                loggedIn = true;
            } else {
                tries--;
                System.out.println("Incorrect username or password. Tries left: " + tries);
            }
        }

        if (!loggedIn) {
            System.out.println("Access denied.");
            scanner.close();
            return;
        }

    
        RentalAgency agency = new RentalAgency();
        agency.addCar(new Car("KAA123A", "Toyota"));
        agency.addCar(new Car("KBB456B", "Honda"));
        agency.addCustomer(new Customer("C001", "Alice"));
        agency.addCustomer(new Customer("C002", "Bob"));

        RentalTransaction tx = agency.rentCar("C001", "Toyota");
        if (tx != null) {
            System.out.println("Car rented: " + tx.getCar().getModel() + " by " + tx.getCustomer().getName());
        } else {
            System.out.println("Car rental failed.");
        }

        boolean returned = agency.returnCar("KAA123A");
        System.out.println("Car returned: " + returned);
        
        runTests();
        scanner.close();
    }

    
    private static String readPassword() {
        try {
            java.io.Console console = System.console();
            if (console != null) {
                char[] pwd = console.readPassword();
                return new String(pwd);
            } else {
                
                Scanner sc = new Scanner(System.in);
                String input = sc.nextLine();
                for (int i = 0; i < input.length(); i++) System.out.print("*");
                
                
                return input;
            }
        } catch (Exception e) {
            return "";
        }
    }

    
    private static void runTests() {
        RentalAgency agency = new RentalAgency();
        Car car = new Car("TEST123", "TestModel");
        agency.addCar(car);
        Customer customer = new Customer("T001", "TestUser");
        agency.addCustomer(customer);

        assert agency.findAvailableCar("TestModel") == car : "Car search failed";
        assert agency.findCustomerById("T001") == customer : "Customer search failed";

        RentalTransaction tx = agency.rentCar("T001", "TestModel");
        assert tx != null : "Car rental failed";
        assert car.isRented() : "Car should be marked as rented";

        boolean returned = agency.returnCar("TEST123");
        assert returned : "Car return failed";
        assert !car.isRented() : "Car should be marked as not rented";

        System.out.println("All tests passed.");
    }
}
