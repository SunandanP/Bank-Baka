import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Login implements Serializable {
    private static ObjectOutputStream oos = null;
    private static ObjectInputStream ois = null;
    static File file = new File("Accounts.bka");
    private ArrayList<Account> accounts;
    private static ArrayList<Object> all;
    private static Scanner sc = new Scanner(System.in);

    Login(){
        accounts = new ArrayList<>();
        accounts.add(new Account("a",19, "IFSC", 3000, "PAN", "7898 5666 2314", "9004138024", "PUNE", "Saving"));
    }
    public Account loginValidator() throws Exception {
        int input = -1;
        Account result = null;
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("1. Login to an Existing account");
        System.out.println("2. Create a new account");
        System.out.println("0. Exit");
        System.out.println("----------------------------------------------------------------------------");
        System.out.print("Enter the choice code : ");
        input = sc.nextInt();
        switch (input){
            case 0:
                break;
            case 1:
                result = accountLogin();
                break;
            case 2:
                result = createAccount();
                break;
        }
        return result;
    }

    public void runApp(Account account) throws Exception {
        int input = -1, tempAmount = 0;
        String name, ac;
        if(account != null){
        TransactionEngine e = new TransactionEngine(account);
        while (input != 0) {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("1. Check Balance");
            System.out.println("2. Account Info");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Show transactions");
            System.out.println("6. Log out");
            System.out.println("7. A/C Money Transfer");
            System.out.println("----------------------------------------------------------------------------");
            System.out.print("Enter the choice code : ");
            input = sc.nextInt();
            switch (input) {
                    case 6:
                        account = loginValidator();
                        if (!(account == null)){
                        e.setAccount(account);
                         }
                        else {
                            input = 0;
                        }
                        break;
                    case 1:
                        e.checkBalance();
                        break;
                    case 2:
                        e.accountInfo();
                        break;
                    case 3:
                        System.out.println("-------------- Deposit ----------------");
                        System.out.print("Enter the amount to be deposited : ");
                        tempAmount = sc.nextInt();
                        e.deposit(tempAmount);
                        break;
                    case 4:
                        System.out.println("-------------- Withdrawal ----------------");
                        System.out.print("Enter the amount to be withdrawn : ");
                        tempAmount = sc.nextInt();
                        e.withdraw(tempAmount);
                        break;
                    case 5:
                        e.showTransactions();
                        break;
                    case 7:
                        System.out.println("Enter the name : ");
                        name = sc.next();
                        System.out.println("Enter the Account Number : ");
                        ac = sc.next();
                        System.out.println("Enter the Amount : ");
                        tempAmount = sc.nextInt();
                        if (e.intraBank(tempAmount)){
                            completeTransfer(name, ac, tempAmount);
                            System.out.println("A/C Transfer Successful.");
                        }
                        else {
                            System.out.println("A/C Transfer Failed.");
                        }
                }

            }
        }
    }

    private Account createAccount() {
        Account result;
        String name, IFSC, PAN, aadhar, mobile, addr, accountType = selectAccountType();
        int age;
        double balance;
        System.out.print("Enter your Name : ");
        name = sc.next();
        System.out.print("Enter your Age : ");
        age = sc.nextInt();
        System.out.print("Enter Opening balance : ");
        balance = sc.nextDouble();
        System.out.print("Enter IFSC : ");
        IFSC = sc.next();
        System.out.print("Enter your PAN : ");
        PAN = sc.next();
        System.out.print("Enter your Aadhar : ");
        aadhar = sc.next();
        System.out.print("Enter your Mobile : ");
        mobile = sc.next();
        System.out.print("Enter your Address : ");
        addr = sc.next();
        result = new Account(name, age, IFSC, balance, PAN, aadhar, mobile, addr, accountType);
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Logging in...");
        System.out.println("----------------------------------------------------------------------------");
        accounts.add(result);

        return result;
    }

    public String selectAccountType(){
        int input = -1;
        String type = null;
        while (input != 0) {
            System.out.println("-------------------- Select the type of the Account ------------------------");
            System.out.println("1. Saving");
            System.out.println("2. Current");
            System.out.println("3. Salary");
            System.out.println("----------------------------------------------------------------------------");
            System.out.print("Enter the choice code : ");
            input = sc.nextInt();
            switch (input) {
                case 1:
                    input = 0;
                    type = "Saving";
                    break;
                case 2:
                    input = 0;
                    type =  "Current";
                    break;
                case 3:
                    input = 0;
                    type = "Salary";
                    break;
                default:
                    System.out.println("Invalid Selection!");
                    System.out.println("----------------------------------------------------------------------------");
            }
        }
        return type;
    }

    private Account accountLogin() {
        String temp1;
        String temp2;
        Account result = null;
        boolean found = false;
        System.out.print("Enter the Bank account number : ");
        temp1 = sc.next();
        System.out.print("Enter the IFSC code : ");
        temp2 = sc.next();

        for (int i = 0; i < accounts.size(); i++){
            if (accounts.get(i).getAccountNum().equals(temp1) && accounts.get(i).getIFSC().equals(temp2)){
                found = true;
                result =  accounts.get(i);
            }
        }
        if (found){
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Logging in...");
            System.out.println("----------------------------------------------------------------------------");
        }
        else {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Account not found, Try again...");
            System.out.println("----------------------------------------------------------------------------");
        }
        return result;
    }

    public void completeTransfer(String name, String accNo, double amount) throws Exception {
        boolean found = false;
        for (int i = 0; i < accounts.size(); i++) {
            if (accNo.equals(accounts.get(i).getAccountNum()) &&  name.equals(accounts.get(i).getName())){
                accounts.get(i).setBalance(accounts.get(i).getBalance() + amount);
                accounts.get(i).createTransaction(new Transaction(amount, LocalDateTime.now(), "Deposit"));
                found = true;
                writeFile();
            }
        }
        if (!found){
            System.out.println("Account not found!");
        }

    }

    public void writeFile() throws Exception {
        file.delete();
        oos = new ObjectOutputStream(new FileOutputStream(file,false));
        oos.writeObject(accounts);
        oos.close();
    }

    public void readFile() throws Exception {
        ois = new ObjectInputStream(new FileInputStream(file));
        accounts = (ArrayList<Account>) ois.readObject();
        ois.close();
    }
}
