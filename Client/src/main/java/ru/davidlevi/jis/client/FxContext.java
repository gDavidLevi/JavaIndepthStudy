package ru.davidlevi.jis.client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.davidlevi.jis.client.core.Client;
import ru.davidlevi.jis.client.core.interfaces.ClientInterface;
import ru.davidlevi.jis.client.gui.Layout;
import ru.davidlevi.jis.client.gui.controller.Authorization;
import ru.davidlevi.jis.client.gui.controller.Explorer;
import ru.davidlevi.jis.client.gui.controller.Registration;

import java.io.IOException;

/**
 * Класс FxContext
 * Основной FX-контекст.
 */
public class FxContext {
    private final Class mainClass;
    private final Stage stage;
    private BorderPane rootLayout;
    private Explorer controllerExplorer;
    private ClientInterface clientInterface;

    {
        clientInterface = new Client();
    }

    /* Конструктор */
    public FxContext(Class mainClass, Stage stage) throws IOException {
        this.mainClass = mainClass;
        this.stage = stage;
        init();
    }

    /**
     * Метод вызвращает ссылку на главную сцену
     *
     * @return Stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     *
     * @return Class
     */
    public Class getMainClass() {
        return mainClass;
    }

    /**
     * Возвращает ссылку на контроллер Explorer для того, чтобы из класса Client можно было вызвать метод update().
     *
     * @return Explorer
     */
    public Explorer getControllerExplorer() {
        return controllerExplorer;
    }

    /**
     * Инициализация сцены
     *
     * @throws IOException layoutRoot();
     */
    private void init() throws IOException {
        stage.setTitle("JIS Client");
        stage.setResizable(true);
        stage.setWidth(400);
        stage.setHeight(10);
        stage.setOnCloseRequest(eventCloseClientWindows);
        layoutRoot();
        setContentView(Layout.AUTHORIZATION);
    }

    /**
     * Событие "Закрытие окна клиента"
     */
    private EventHandler<WindowEvent> eventCloseClientWindows = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            clientInterface.quit();
            Platform.exit();
        }
    };

    /**
     * Устанавливает слой отображения
     *
     * @param layout Layout
     */
    public void setContentView(Layout layout) {
        try {
            switch (layout) {
                case AUTHORIZATION:
                    layoutAuthorization();
                    break;
                case REGISTRATION:
                    layoutRegistration();
                    break;
                case EXPLORER:
                    layoutExplorer();
                    break;
                default:
                    throw new RuntimeException(getClass().getName() + ".setContentView() - Не указан Layout.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Слой "Базовый"
     *
     * @throws IOException rootLayout = loader.load();
     */
    private void layoutRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainClass.getResource("view/root.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * Слой "Авторизация"
     *
     * @throws IOException rootLayout = loader.load();
     */
    private void layoutAuthorization() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainClass.getResource("view/authorization.fxml"));
        AnchorPane anchorPane = loader.load();
        rootLayout.setCenter(anchorPane);
        Authorization controller = loader.getController();
        controller.setFxContext(this);
    }

    /**
     * Слой "Регистрация"
     *
     * @throws IOException rootLayout = loader.load();
     */
    private void layoutRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainClass.getResource("view/registration.fxml"));
        AnchorPane anchorPane = loader.load();
        rootLayout.setCenter(anchorPane);
        Registration controller = loader.getController();
        controller.setFxContext(this);
    }

    /**
     * Слой "Обозреватель"
     *
     * @throws IOException rootLayout = loader.load();
     */
    private void layoutExplorer() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainClass.getResource("view/explorer.fxml"));
        AnchorPane anchorPane = loader.load();
        rootLayout.setCenter(anchorPane);
        /* Переменная controllerExplorer объявлена как поле класса.
         * Необходима для доступа к данному контроллеру из класса Client. */
        controllerExplorer = loader.getController();
        controllerExplorer.setFxContext(this);
    }
}
