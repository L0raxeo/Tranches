package com.tranches.components;

import imgui.ImGui;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
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

    private transient BigDecimal dayLow;
    private transient BigDecimal dayHigh;
    private transient StockDividend stockDividend;
    private transient BigDecimal eps;
    private transient BigDecimal pe;
    private transient BigDecimal bvps;
    private transient BigDecimal pb;
    private transient List<HistoricalQuote> priceHistory;

    public LiveStockInfo(StockTracker tracker) throws IOException
    {
        init(tracker);
    }

    public void init(StockTracker tracker) throws IOException
    {
        this.tracker = tracker;
        this.ticker = tracker.getTicker();
        this.stock = YahooFinance.get(ticker);
        this.stockName =  stock.getName();

        this.dayLow = stock.getQuote(true).getDayLow();
        this.dayHigh = stock.getQuote(true).getDayHigh();
        this.stockDividend = stock.getDividend(true);
        this.eps = stock.getStats(true).getEps();
        this.pe = stock.getStats(true).getPe();
        this.bvps = stock.getStats(true).getBookValuePerShare();
        this.pb = stock.getStats(true).getPriceBook();
        this.priceHistory = new ArrayList<>(stock.getHistory());

        setName("Ticker: " + ticker + " | " + "Name: " + stockName);
        separateWindow(true);
        showInPortfolio(false);
    }

    @Override
    public void defaultImGui()
    {
        if (tracker.isTrackingPriceQuotes())
            ImGui.text("Today's Price Quote Range: $" + dayLow + " - $" + dayHigh);

        if (tracker.isTrackingDividends())
            ImGui.text(String.valueOf(stockDividend));

        if (tracker.isTrackingEarningsPerShare())
            ImGui.text("EPS: " + eps);

        if (tracker.isTrackingPriceToEarnings())
            ImGui.text("P/E: " + pe);

        if (tracker.isTrackingBookValuePerShare())
            ImGui.text("Book Value Per Share: " + bvps);

        if (tracker.isTrackingPriceToBook())
            ImGui.text("Price/Book: " + pb);

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
