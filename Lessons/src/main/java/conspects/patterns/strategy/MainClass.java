package conspects.patterns.strategy;

public class MainClass {
    public static void main(String[] args) {
        ShoppingCart shoppingCart = new ShoppingCart();
        Item milk = new Item("565", 54);
        Item bread = new Item("234", 43);
        shoppingCart.add(milk);
        shoppingCart.add(bread);

        PaymentStrategy paymentStrategy = new CreditCard("DAVID LEVI", "5412294020127317", "555", "12/2050");
        //PaymentStrategy paymentStrategy = new PayPal("davidlevi@gmal.com","Nj3Mk5Lk6j8H9n0dsV5f");
        shoppingCart.pay(paymentStrategy);
    }
}