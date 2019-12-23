package com.mechanitis.demo.stockui;

import com.mechanitis.demo.stockclient.StockPrice;
import com.mechanitis.demo.stockclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * This is component call from fxml, it set data in the line charts
 */
@Component
public class ChartController  {
    private static final String SYMBOL2 = "UBER";
    @FXML
    public LineChart<String, Double> chart;
    @FXML
    public Label lastPriceLbl;
    private WebClientStockClient stockClientProxy;
    private final ObservableList<Series<String, Double>> data = observableArrayList();
    private static final Logger log = LogManager.getLogger(ChartController.class);

    private final Map<String,String> mapLastSeriesPoint = new ConcurrentHashMap();

    private static final String SYMBOL1 = "Apple Inc.";

    public ChartController(WebClientStockClient stockClientProxy) {
        this.stockClientProxy = stockClientProxy;
    }

    @FXML
    public void initialize() {
        try {
            chart.getXAxis().setLabel("Time"); //label x axis
            chart.getYAxis().setLabel("Prices"); //label y axis
            final PriceSubscriber priceSubscriber1 = new PriceSubscriber(SYMBOL1,mapLastSeriesPoint);
            stockClientProxy.pricesFor(SYMBOL1).subscribe(priceSubscriber1);//handler is method accept in  class
            final PriceSubscriber priceSubscriber2 = new PriceSubscriber(SYMBOL2,mapLastSeriesPoint);
            stockClientProxy.pricesFor(SYMBOL2).subscribe(priceSubscriber2);//handler is method accept in  class
            data.add(priceSubscriber1.getSeries());
            data.add(priceSubscriber2.getSeries());
            chart.setData(data);
        } catch (Exception e) {
            log.error("Error on load JavaFX components: " + e.getMessage(), e);
        }
        //log.info(take.blockFirst().getTime());

    }



    private class PriceSubscriber implements Consumer< StockPrice>{

        private final ObservableList<Data<String, Double>> seriesData = observableArrayList();
        private final Series<String, Double> series;
        private final Map<String, String> mapLastSeriesPoint;

        public PriceSubscriber(String symbol, Map<String, String> mapLastSeriesPoint) {
            series = new Series<>(symbol, seriesData);
            this.mapLastSeriesPoint = mapLastSeriesPoint;
        }

        public Series<String, Double> getSeries() {
            return series;
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
                mapLastSeriesPoint.put(stockPrice.getSymbol(), price);
                final StringBuilder sbPrices = new StringBuilder();
                mapLastSeriesPoint.forEach((k,v) -> sbPrices.append(v).append(" ; ") );
                String prices = sbPrices.substring(0, sbPrices.lastIndexOf(";"));
                lastPriceLbl.setText(prices);
            });
        }
    }
}
