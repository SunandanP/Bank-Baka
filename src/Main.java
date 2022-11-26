public class Main {

    public static void main(String[] args) throws Exception {
        Login login = new Login();
        if (Login.file.isFile()){
            login.readFile();
            System.out.println("-------------------------- Welcome to HDFC Bank ---------------------------");
            Account account = null;
            while (account == null) {
                account = login.loginValidator();
            }
            login.runApp(account);
        login.writeFile();
        }
        else {
            System.out.println("-------------------------- Welcome to HDFC Bank ---------------------------");
            Account account = null;
            while (account == null) {
                account = login.loginValidator();
            }
            login.runApp(account);
            login.writeFile();
        }

    }
}