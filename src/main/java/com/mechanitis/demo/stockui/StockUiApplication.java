package com.mechanitis.demo.stockui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockUiApplication {

	/**
	 * lancia l'applicazione JavaFx ChartApplication.class la quale a sua volta nel metodo
	 * init() lancia questa  StockUiApplication come springBoot application
	 * @param args
	 */
	public static void main(String[] args) {
//		SpringApplication.run(StockUiApplication.class, args); -> viene lanciato dalla init() in
		//using Java FX launch
		Application.launch(ChartApplication.class, args);
	}

}
