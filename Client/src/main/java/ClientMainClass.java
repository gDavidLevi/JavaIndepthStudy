import javafx.application.Application;
import javafx.stage.Stage;
import ru.davidlevi.jis.client.FxContext;

import java.io.IOException;

public class ClientMainClass extends Application {
    /* Точка входа */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        new FxContext(getClass(), primaryStage);
    }
}
