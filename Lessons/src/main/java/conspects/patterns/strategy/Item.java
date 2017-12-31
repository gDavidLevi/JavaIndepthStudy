package conspects.patterns.strategy;

/**
 * Класс Item (товар)
 */
public class Item {
    private String id;
    private int price;

    /* Конструктор */
    Item(String id, int price) {
        this.id = id;
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        if (price != item.price) return false;
        return id != null ? id.equals(item.id) : item.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + price;
        return result;
    }
}
