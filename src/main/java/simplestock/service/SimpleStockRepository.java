package simplestock.service;

import org.springframework.stereotype.Component;
import simplestock.model.StockInformation;
import simplestock.model.Trade;
import simplestock.model.TradeType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleStockRepository {

    private Map<String, StockInformation> stockInformationChart;
    private Map<String, ArrayList<Trade>> marketTradeList;

    public  Map<String, ArrayList<Trade>> getMarketTradeList() {
        if(marketTradeList == null) {
            marketTradeList = new HashMap<>();
            ArrayList<Trade> list1 = new ArrayList();
            list1.add(new Trade("TEA", new Timestamp(System.currentTimeMillis()), 102, TradeType.SELL, new BigDecimal(1)));
            list1.add(new Trade("TEA", new Timestamp(System.currentTimeMillis()), 102, TradeType.SELL, new BigDecimal(2)));
            list1.add(new Trade("TEA", new Timestamp(System.currentTimeMillis()), 102, TradeType.SELL, new BigDecimal(3)));

            ArrayList<Trade> list2 = new ArrayList();
            list2.add(new Trade("POP", new Timestamp(System.currentTimeMillis()), 102, TradeType.SELL, new BigDecimal(4)));
            list2.add(new Trade("POP", new Timestamp(System.currentTimeMillis()), 102, TradeType.SELL, new BigDecimal(5)));
            list2.add(new Trade("POP", new Timestamp(System.currentTimeMillis()), 102, TradeType.SELL, new BigDecimal(6)));

            marketTradeList.put("TEA", list1);
            marketTradeList.put("POP", list2);
        }

        return marketTradeList;
    }

    public Map<String, StockInformation> getStockInformationChart() {
        if (stockInformationChart == null) {
            stockInformationChart = new HashMap<>();
            stockInformationChart.put("TEA", new StockInformation("TEA", "Common", new BigDecimal(0), new BigDecimal(0), new BigDecimal(100)));
            stockInformationChart.put("POP", new StockInformation("POP", "Common", new BigDecimal(8), new BigDecimal(0), new BigDecimal(100)));
            stockInformationChart.put("ALE", new StockInformation("ALE", "Common", new BigDecimal(23), new BigDecimal(0), new BigDecimal(100)));
            stockInformationChart.put("GIN", new StockInformation("GIN", "Preferred", new BigDecimal(8), new BigDecimal(2), new BigDecimal(100)));
            stockInformationChart.put("JOE", new StockInformation("JOE", "Common", new BigDecimal(13), new BigDecimal(0), new BigDecimal(250)));
        }
        return stockInformationChart;
    }
}
