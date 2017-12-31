package conspects.patterns.adapter;

public class MainClass {
    public static void main(String[] args) {
        /* Китайская разетка */
        ChinaSocket chinaSocket = new ChinaSocket();

        /* Китайская вилка */
        ChinaConnector chinaConnector = () -> {
        };

        /* Вставляем китайскую вилку */
        chinaSocket.plugin(chinaConnector);

        /* Русская вилка */
        RussianConnector russianConnector = () -> {
        };

        /* Адаптер русских вилок для китайских розеток */
        Adapter adapter = new Adapter(russianConnector);

        /* Вставляем русскую вилку через адаптер */
        chinaSocket.plugin(adapter);
    }
}
