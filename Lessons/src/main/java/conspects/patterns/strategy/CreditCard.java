package conspects.patterns.strategy;

/**
 * Оплата с помощью CreditCard
 */
public class CreditCard implements PaymentStrategy {
    private String charholderName;
    private String cardNumber;
    private String cvv;
    private String expirationDate;

    /* Конструктор */
    CreditCard(String charholderName, String cardNumber, String cvv, String expirationDate) {
        this.charholderName = charholderName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
    }

    @Override
    public void pay(int amount) {
        System.out.println("Paid with CreditCard");
    }
}
