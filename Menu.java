package banking;

import java.util.Scanner;
import java.util.Random;
import java.util.stream.Collectors;

public class Menu {
    String information = "1. Create an account \n2. Log into account \n3. Show all data \n0. Exit";
    Database database = new Database();
    Scanner scanner = new Scanner(System.in);

    public void startProgram() {
        database.createDatabaseWithTable();

        int input = -1;
        while (input != 0) {
            System.out.println(information);
            input = scanner.nextInt();
            if (input == 1) {
                this.createAccount();
            } else if (input == 2) {
                this.logIntoAccount();
            } else if (input == 3) {
                database.showAllData();
            }
        }
        System.out.println("Bye!");
    }

    public void createAccount() {
        Random random = new Random();
        String accountIdentifier = random.ints(9, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(""));
        String bin = "400000";
        String checksum = Checksum.getChecksum( bin + accountIdentifier);
        String cardNumber = bin + accountIdentifier + checksum;
        String pinNumber = random.ints(4, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(""));


        database.insert(cardNumber, pinNumber);
        System.out.println("Your card has been created\n" +
                "Your card number: \n" + cardNumber + "\nYour card PIN: \n" + pinNumber);
    }


    public void logIntoAccount() {
        System.out.println("Enter your card number:\n");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:\n");
        String pinNumber = scanner.next();

        boolean result = database.checkIfExistsWithPin(cardNumber, pinNumber);
        if (result == true) {
            System.out.println("You have successfully logged in!");
            int input = -1;

            while (input != 0) {
                System.out.println("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit");
                input = scanner.nextInt();
                if (input == 1) {
                    System.out.println("Balance: " + database.getBalance(cardNumber));
                } else if (input == 2) {
                   this.addIncome(cardNumber);
                } else if (input == 3) {
                    this.doTransfer(cardNumber);
                } else if (input == 4) {
                    this.closeAccount(cardNumber);
                } else if (input == 5) {
                    System.out.println("You have successfully logged out!");
                    return;
                } else if (input == 0){
                    System.out.println("Bye!");
                    System.exit(0);
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    private void addIncome(String cardNumber) {
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        database.addIncome(income, cardNumber);
        System.out.println("Income was added!\n");
    }

    private void doTransfer(String cardNumber) {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String receiver = scanner.next();
        if (!Checksum.getChecksum(receiver.substring(0, receiver.length() - 1)).equals(String.valueOf(receiver.charAt(receiver.length() - 1)))) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
        } else if (!database.checkIfExists(receiver)) {
            System.out.println("Such a card does not exist.\n");
        } else if (receiver.equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account!");
        } else {
            System.out.println("Enter how much money you want to transfer:\n");
            int amount = scanner.nextInt();
            if (database.getBalance(cardNumber) < amount) {
                System.out.println("Not enough money!");
            } else {
                database.doTransfer(cardNumber, amount, receiver);
                System.out.println("Success!");
            }
        }
    }

    private void closeAccount(String cardNumber) {
        database.closeAccount(cardNumber);
        System.out.println("The account has been closed!\n");
    }


}
