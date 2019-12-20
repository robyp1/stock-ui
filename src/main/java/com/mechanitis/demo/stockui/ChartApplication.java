package com.mechanitis.demo.stockui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class ChartApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    /**
     * 2. quando pringboot è attivo lancia un evento a JavaFX che è genera la finestra widget base
     * (l'evento viene intercettato dalla classe StageInitializer che si occuap di disegnare la finestra widget)
     * il disegno degli oggetti nella finestra si basa su file di risorsa .fxml
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }

    /**
     * 1. lancia springBoot come SpringApplication.run(StockUiApplication.class, args)
     * ma con il suo bilder
     */
    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(StockUiApplication.class).run();
    }

    /**
     * quando si chiude la finestra widget creata o si chiude l'ultima padre
     */
    @Override
    public void stop() {
        applicationContext.stop(); //ferma springboot
        Platform.exit(); //esce da JavaFX chiudendo il widget
    }
}
