import java.util.HashMap;
import java.util.Scanner;

// ---------------- BANK ACCOUNT CLASS ----------------
class BankAccount {

    private String accountNumber;
    private String accountHolderName;
    private String password;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName,
                       String password, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.password = password;
        this.balance = initialDeposit;
    }

    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(">> Deposit successful!");
            System.out.printf("Updated Balance: ₹%.2f%n", balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println(">> Withdrawal successful!");
            System.out.printf("Updated Balance: ₹%.2f%n", balance);
        } else {
            System.out.println("Insufficient or invalid amount.");
        }
    }

    public boolean transferTo(BankAccount recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recipient.balance += amount;
            return true;
        }
        return false;
    }

    public void checkBalance() {
        System.out.println("------------------------------");
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Account Number: " + accountNumber);
        System.out.printf("Balance: ₹%.2f%n", balance);
        System.out.println("------------------------------");
    }
}


// ---------------- MAIN APPLICATION ----------------
public class BankApp {

    private static HashMap<String, BankAccount> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== Welcome to Java Bank ===");

        int choice = 0;

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input!");
                scanner.next();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> System.out.println("Thank you for using Java Bank.");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 3);

        scanner.close();
    }


    // -------- CREATE ACCOUNT --------
    private static void createAccount() {

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();

        if (accounts.containsKey(accNum)) {
            System.out.println("Account already exists.");
            return;
        }

        System.out.print("Create Password: ");
        String password = scanner.nextLine();

        System.out.print("Initial Deposit: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount.");
            scanner.next();
            return;
        }

        double deposit = scanner.nextDouble();
        scanner.nextLine();

        if (deposit < 0) {
            System.out.println("Deposit cannot be negative.");
            return;
        }

        accounts.put(accNum, new BankAccount(accNum, name, password, deposit));
        System.out.println(">> Account created successfully!");
    }


    // -------- LOGIN --------
    private static void login() {

        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();

        BankAccount account = accounts.get(accNum);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (!account.checkPassword(password)) {
            System.out.println("Incorrect password.");
            return;
        }

        int choice = 0;

        do {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Money");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input!");
                scanner.next();
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {

                case 1 -> account.checkBalance();

                case 2 -> {
                    System.out.print("Enter amount: ₹");
                    if (scanner.hasNextDouble()) {
                        account.deposit(scanner.nextDouble());
                    } else {
                        System.out.println("Invalid amount.");
                        scanner.next();
                    }
                    scanner.nextLine();
                }

                case 3 -> {
                    System.out.print("Enter amount: ₹");
                    if (scanner.hasNextDouble()) {
                        account.withdraw(scanner.nextDouble());
                    } else {
                        System.out.println("Invalid amount.");
                        scanner.next();
                    }
                    scanner.nextLine();
                }

                case 4 -> {
                    scanner.nextLine();
                    System.out.print("Enter recipient account number: ");
                    String recAcc = scanner.nextLine();

                    BankAccount recipient = accounts.get(recAcc);

                    if (recipient == null) {
                        System.out.println("Recipient account not found.");
                    } else {
                        System.out.print("Enter amount: ₹");

                        if (scanner.hasNextDouble()) {
                            double amt = scanner.nextDouble();

                            if (account.transferTo(recipient, amt)) {
                                System.out.println(">> Transfer successful!");
                                System.out.printf("Your Balance: ₹%.2f%n",
                                        account.getBalance());
                            } else {
                                System.out.println("Transfer failed (insufficient funds).");
                            }
                        } else {
                            System.out.println("Invalid amount.");
                            scanner.next();
                        }
                        scanner.nextLine();
                    }
                }

                case 5 -> System.out.println("Logged out.");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 5);
    }
}
