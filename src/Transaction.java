import java.io.Serializable;
import java.security.SecureRandomParameters;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    double amount;
    LocalDateTime dateTime;
    String type;

    Transaction(double amount, LocalDateTime dateTime, String type){
        this.amount = amount;
        this.dateTime = dateTime;
        this.type = type;
    }
}
