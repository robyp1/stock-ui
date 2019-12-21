package com.mechanitis.demo.stockui;

import com.mechanitis.demo.stockclient.StockPrice;
import com.mechanitis.demo.stockclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * This is component call from fxml, it set data in the line charts
 */
@Component
public class ChartController implements Consumer<StockPrice> {
    @FXML
    public LineChart<String, Double> chart;
    private WebClientStockClient stockClientProxy;
    private ObservableList<XYChart.Data<String, Double>> seriesData = FXCollections.observableArrayList();


    public ChartController(WebClientStockClient stockClientProxy) {
        this.stockClientProxy = stockClientProxy;
    }

    @FXML
    public void initialize() {
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
        data.add(new XYChart.Series<>(seriesData));
        chart.setData(data);
        stockClientProxy.pricesFor("SYMBOL").subscribe(this);
    }

    @Override
    public void accept(StockPrice stockPrice) {
        //tell the ui thread to update chart every time new data accepted
        Platform.runLater(() -> {
            seriesData.add(new XYChart.Data<String, Double>(String.valueOf(stockPrice.getTime().getSecond()),
                    stockPrice.getPrice()));
        });
    }
}
