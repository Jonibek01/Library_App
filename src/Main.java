import db.DataSource;
import services.AuthService;

public class Main {
    public static void main(String[] args) {
        while (true){
            DataSource.refreshScanner();
            try {
                AuthService.service();
            }catch (Exception e){

            }
        }
        
    }
}