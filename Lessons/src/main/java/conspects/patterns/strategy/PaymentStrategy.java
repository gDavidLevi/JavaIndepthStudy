package conspects.patterns.strategy;

/**
 * Интерфейс PaymentStrategy (чем именно будем производить оплату товара(ов))
 */
public interface PaymentStrategy {
    /**
     * Оплатить счёт
     *
     * @param amount сумма
     */
    void pay(int amount);
}
