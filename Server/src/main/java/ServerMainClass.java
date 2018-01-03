import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.davidlevi.jis.server.database.HibernateFactory;

/* Точка входа */
public class ServerMainClass extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JIS Server");
        primaryStage.setOnCloseRequest(eventCloseClientWindows);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("view/basic.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Событие "Закрытие окна сервера"
     */
    private EventHandler<WindowEvent> eventCloseClientWindows = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            HibernateFactory.shutdown();
            Platform.exit();
        }
    };
}
