package conspects.patterns.strategy;

/**
 * Оплата с помощью PayPal
 */
public class PayPal implements PaymentStrategy {
    private String email;
    private String password;

    /* Конструктор */
    PayPal(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void pay(int amount) {
        System.out.println("Paid with PayPal");
    }
}
