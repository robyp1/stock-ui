package com.mechanitis.demo.stockui;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockUiApplication {

	public static void main(String[] args) {
//		SpringApplication.run(StockUiApplication.class, args);
		//using Java FX launch
		Application.launch(ChartApplication.class, args);
	}

}
