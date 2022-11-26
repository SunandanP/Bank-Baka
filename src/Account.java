import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Account implements Serializable {

    static int no = 0;
    private static double minBalance = 2000;
    private final double dailyLimit;
    private double todaysWithdrawable;
    private String name;
    private int age;
    private String IFSC;
    private double balance;
    private  String PAN;
    private  String aadhar;
    private  String mobile;
    private String addr;
    private String accountType;
    private String accountNum;

    private ArrayList<Transaction> transactions;

    private int ptr = -1;
    public Account(String name,
                   int age,
                   String IFSC,
                   double balance,
                   String PAN,
                   String aadhar,
                   String mobile,
                   String addr,
                   String accountType
    ) {
        this.name = name;
        this.age = age;
        this.IFSC = IFSC;
        this.balance = balance;
        this.PAN = PAN;
        this.aadhar = aadhar;
        this.mobile = mobile;
        this.addr = addr;
        this.accountType = accountType;
        no += 1;
        this.accountNum = ((""+no).length() < 10 )? "0000"+no : "000"+no;
        transactions = new ArrayList<>();
        transactions.add(new Transaction(balance, LocalDateTime.now(), "Deposit"));
        if (accountType.equals("Saving")){
            dailyLimit = 10000;
        } else if (accountType.equals("Current")) {
            dailyLimit = 50000;
        }
        else{
            dailyLimit = 80000;
        }
        this.todaysWithdrawable = dailyLimit;
    }

    void getInfo(){
        System.out.println(MessageFormat.format("""
                Name : {0}
                Age: {1}
                IFSC: {2}
                Balance : {3}
                PAN : {4}
                Aadhar : {5}
                Mobile No : {6}
                Address : {7}
                Account Type : {8}
                Account Number : {9}
                """, name, age, IFSC, balance, PAN, aadhar, mobile, addr, accountType, accountNum));
    }

    public void setBalance(double amount){
        this.balance = amount;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public double getBalance() {
        return balance;
    }
    public void createTransaction(Transaction transaction){
        transactions.add(transaction);

    }

    public double getTodaysWithdrawable() {
        return todaysWithdrawable;
    }

    public void setTodaysWithdrawable(){
        for (int i = ptr+1; i < transactions.size(); i++) {
            if (LocalDateTime.now().getDayOfYear() == transactions.get(i).dateTime.getDayOfYear()) {
                if (transactions.get(i).type.equals("Withdrawal")) {
                    todaysWithdrawable -= transactions.get(i).amount;
                }
                ptr++;
            }
        }
    }

    public ArrayList<Transaction> getTransactions(){
        return transactions;
    }

    public String getIFSC() {
        return IFSC;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getName() {
        return name;
    }
}
