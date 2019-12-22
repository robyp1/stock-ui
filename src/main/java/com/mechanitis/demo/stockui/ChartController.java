package com.mechanitis.demo.stockui;

import com.mechanitis.demo.stockclient.StockPrice;
import com.mechanitis.demo.stockclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

import static java.lang.String.*;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * This is component call from fxml, it set data in the line charts
 */
@Component
public class ChartController implements Consumer<StockPrice> {
    @FXML
    public LineChart<String, Double> chart;
    @FXML
    public Label lastPriceLbl;
    private WebClientStockClient stockClientProxy;
    private ObservableList<XYChart.Data<String, Double>> seriesData = observableArrayList();
    private static final Logger log = LogManager.getLogger(ChartController.class);


    public ChartController(WebClientStockClient stockClientProxy) {
        this.stockClientProxy = stockClientProxy;
    }

    @FXML
    public void initialize() {
        String symbol = "EUR";
        ObservableList<XYChart.Series<String, Double>> data = observableArrayList();
        data.add(new Series<>(symbol,seriesData));
        chart.setData(data);
        stockClientProxy.pricesFor(symbol).subscribe(this);//handler is method accept in this class
        //log.info(take.blockFirst().getTime());

    }

    @Override
    public void accept(StockPrice stockPrice) {
        //tell the ui thread to update chart every time new data accepted
        Platform.runLater(() -> {
            seriesData.add(new Data<String, Double>(valueOf(stockPrice.getTime().getSecond()),
                    stockPrice.getPrice()));
            Locale locale = Locale.getDefault();
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(locale);
            df.setParseBigDecimal(true);
            df.setMaximumFractionDigits(10);
            String price =  df.format(BigDecimal.valueOf(stockPrice.getPrice()));
            lastPriceLbl.setText(price);
        });
    }
}
