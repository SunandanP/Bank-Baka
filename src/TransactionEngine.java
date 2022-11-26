import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class TransactionEngine implements Serializable {
    Account account;

    public TransactionEngine(Account account) {
        this.account = account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean withdraw(double amount) {
        boolean successful = false;
        String message = "";
        boolean insufficientFunds = (account.getBalance() - amount) < account.getMinBalance();
        boolean dailyLimitReached = (account.getTodaysWithdrawable()) <= 0;
        boolean beyondDailyLimit = (account.getTodaysWithdrawable() - amount) < 0;

        if (!insufficientFunds && !dailyLimitReached) {
            if (!beyondDailyLimit) {
            account.createTransaction(new Transaction(amount, LocalDateTime.now(), "Withdrawal"));
            account.setBalance(account.getBalance() - amount);
            System.out.println("Withdrawal Successful!");
            System.out.println("----------------------------------------------------------------------------");
            successful = true;
            }
            else {
                System.out.println("----------------------------------------------------------------------------");
                System.out.println( "Withdrawal is beyond daily limit.\nPlease enter a amount less than or equal to "+account.getTodaysWithdrawable());
                System.out.println("----------------------------------------------------------------------------");

            }

        }
        else{
            System.out.println("Withdrawal failed!");
            message = (insufficientFunds)? "Insufficient funds" : dailyLimitReached ? "Daily limit reached.": !beyondDailyLimit ? "Withdrawal is beyond daily limit." :  "";
            System.out.println("Error : "+ message);
            System.out.println("----------------------------------------------------------------------------");

        }
        account.setTodaysWithdrawable();
        return successful;

    }

    public void deposit(double amount){
        account.createTransaction(new Transaction(amount, LocalDateTime.now(), "Deposit"));
        account.setBalance(account.getBalance() + amount);
        System.out.println("Deposit Successful!");
        System.out.println("----------------------------------------------------------------------------");
    }
    public void checkBalance(){
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Total Balance : "+ account.getBalance());
        System.out.println("Available Balance : "+ (account.getBalance() - account.getMinBalance()));
        System.out.println("Today's remaining Withdrawal Balance : "+ account.getTodaysWithdrawable());
        System.out.println("----------------------------------------------------------------------------");

    }
    public void accountInfo(){
        System.out.println("----------------------------------------------------------------------------");
        account.getInfo();
        System.out.println("----------------------------------------------------------------------------");
    }

    public void showTransactions(){
        ArrayList<Transaction> transactions = account.getTransactions();
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Date\t\t\tTime\t\t\tAmount\t\t\tTransaction Type");
        System.out.println("----------------------------------------------------------------------------");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction temp = transactions.get(i);
            System.out.println(temp.dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))+"\t\t"+temp.dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "\t\t" + temp.amount + "\t\t\t" + temp.type);
            System.out.println("----------------------------------------------------------------------------");

        }
    }
    
    public void beautify(){
        System.out.println("----------------------------------------------------------------------------");

    }

    public boolean intraBank(double amount){
        return withdraw(amount);
    }
}