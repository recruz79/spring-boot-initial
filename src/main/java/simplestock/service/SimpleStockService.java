package simplestock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplestock.exception.MarketException;
import simplestock.model.StockInformation;
import simplestock.model.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class SimpleStockService {

    public static final long FIVE_MINUTES = 5 * 60 * 1000;
    public static final String COMMON_TYPE = "Common";
    public static final String PREFERRED_TYPE = "Preferred";

    @Autowired
    SimpleStockRepository simpleStockRepository;

    public BigDecimal getDividendYield(String stockName, Double price) throws MarketException {
        StockInformation stockInformation = simpleStockRepository.getStockInformationChart().get(stockName);
        Double dividendYield = 0D;
        if (stockInformation == null) {
            throw new MarketException("No stock found");
        }

        if (COMMON_TYPE.equals(stockInformation.getType())) {
            dividendYield = stockInformation.getLastDividend() / price;
        } else if (PREFERRED_TYPE.equals(stockInformation.getType())) {
            dividendYield = stockInformation.getFixedDividen() / price;
        }

        return new BigDecimal(dividendYield);
    }

    public BigDecimal getPERatio(String stockName, Double price) throws MarketException {
        BigDecimal dividendYield = getDividendYield(stockName, price);
        if (dividendYield.compareTo(BigDecimal.ZERO) == 0) {
            throw new MarketException("Could not calculate PE Ratio since dividendYield is zero");
        }

        BigDecimal peRatio = new BigDecimal(price / dividendYield.doubleValue());

        return peRatio;
    }

    public BigDecimal getStockPrice(String stockName) throws MarketException {
        ArrayList<Trade> tradeList = simpleStockRepository.getMarketTradeList().get(stockName);
        if (null == tradeList || tradeList.isEmpty()) {
            throw new MarketException("Stock list is empty");
        }

        Long fiveMinutesAgo = System.currentTimeMillis() - FIVE_MINUTES;
        Double sumPricePerQuantity = tradeList.stream().filter(o -> o.getTimestamp().getTime() > fiveMinutesAgo)
                .mapToDouble(o -> o.getQuantity() * o.getPrice())
                .sum();

        Double sumQuantity = tradeList.stream().filter(o -> o.getTimestamp().getTime() > fiveMinutesAgo)
                .mapToDouble(o -> o.getQuantity())
                .sum();

        return new BigDecimal(sumPricePerQuantity / sumQuantity);
    }

    public BigDecimal getMarketAllShareIndex() {
        Double marketAllShareIndex = 1D;
        Double count = 0D;
        for (String key : simpleStockRepository.getMarketTradeList().keySet()) {
            ArrayList<Trade> stockTradelist = simpleStockRepository.getMarketTradeList().get(key);
            for (Trade trade : stockTradelist) {
                marketAllShareIndex *= trade.getPrice();
                count++;
            }
        }

        return new BigDecimal(Math.pow(marketAllShareIndex, (1 / count)));
    }

    public void addTradeList(Trade trade) {
        ArrayList tradeList = simpleStockRepository.getMarketTradeList().get(trade.getStockName());
        if (tradeList == null) {
            tradeList = new ArrayList();
        }

        tradeList.add(trade);
    }

}