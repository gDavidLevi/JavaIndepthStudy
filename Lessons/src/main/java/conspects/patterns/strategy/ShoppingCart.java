package conspects.patterns.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс ShoppingCart (корзина)
 */
class ShoppingCart {
    private List<Item> items;

    /* Конструктор */
    ShoppingCart() {
        items = new ArrayList<>();
    }

    /**
     * Добавить товар в корзину
     *
     * @param item товар
     */
    void add(Item item) {
        items.add(item);
    }

    /**
     * Сумма всех товаров в корзине
     *
     * @return int
     */
    private int getSum() {
        int sum = 0;
        for (Item i : items)
            sum += i.getPrice();
        return sum;
    }

    /**
     * Оплата
     *
     * @param wallet чем именно будем производить оплату
     */
    void pay(PaymentStrategy wallet) {
        wallet.pay(getSum());
    }
}
