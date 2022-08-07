package com.tranches.components;

import imgui.ImGui;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes2.HistoricalDividend;
import yahoofinance.quotes.stock.StockDividend;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LiveStockInfo extends Component
{

    // All variables will be transient. This is a live tracker. Nothing will be saved.
    private transient StockTracker tracker;
    private transient Stock stock;
    private transient String ticker;
    private transient String stockName;

    private BigDecimal dayLow;
    private BigDecimal dayHigh;
    private StockDividend stockDividend;
    private List<HistoricalQuote> priceHistory;

    public LiveStockInfo(StockTracker tracker) throws IOException
    {
        this.tracker = tracker;
        this.ticker = tracker.getTicker();
        this.stock = YahooFinance.get(ticker);
        this.stockName =  stock.getName();

        this.dayLow = stock.getQuote(true).getDayLow();
        this.dayHigh = stock.getQuote(true).getDayHigh();
        this.stockDividend = stock.getDividend(true);
        this.priceHistory = new ArrayList<>(stock.getHistory());

        setName("Ticker: " + ticker + " | " + "Name: " + stockName);
        separateWindow(true);
        showInPortfolio(false);
    }

    @Override
    public void defaultImGui()
    {
        if (tracker.isTrackingPriceQuotes())
        {
            ImGui.text("Today's Price Quote Range: $" + dayLow + " - $" + dayHigh);
        }

        if (tracker.isTrackingDividends())
        {
            ImGui.text(String.valueOf(stockDividend));
        }

        if (tracker.isTrackingPriceHistory())
        {
            if (ImGui.treeNode("Price History"))
            {
                for (HistoricalQuote hq : priceHistory)
                    ImGui.text("$" + hq.getHigh() + " | " + (hq.getDate().get(Calendar.MONTH) + 1) + "/" + hq.getDate().get(Calendar.DAY_OF_MONTH) + "/" + hq.getDate().get(Calendar.YEAR));

                ImGui.treePop();
            }
        }

        if (ImGui.button("Close Popout"))
        {
            die();
        }
    }

}
