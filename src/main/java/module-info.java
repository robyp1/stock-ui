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
    requires transitive stock.client; //transitive because it use webflux..
    requires com.fasterxml.jackson.databind;//required because webflux doesnt export faster jackson..
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.classmate;
    requires org.apache.logging.log4j;
    requires reactor.core;

    //opens ->  needed for reflection calls from spring core
    //and exports -> neeede because springboot use StockUiApplication
    //with launch()
    exports com.mechanitis.demo.stockui;
    opens com.mechanitis.demo.stockui to spring.core;

}