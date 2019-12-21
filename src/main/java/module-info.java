module stock.ui {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires stock.client;

    //opens ->  needed for reflection calls from spring core
    //and exports -> neeede because springboot use StockUiApplication
    //with launch()
    exports com.mechanitis.demo.stockui;
    opens com.mechanitis.demo.stockui to spring.core;

}