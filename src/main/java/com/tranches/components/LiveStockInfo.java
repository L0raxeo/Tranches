package com.tranches.components;

import imgui.ImGui;
import yahoofinance.YahooFinance;

import java.io.IOException;

public class LiveStockInfo extends Component
{

    // All variables will be transient. This is a live tracker. Nothing will be saved.
    private transient StockTracker tracker;
    private transient String ticker;
    private transient String stockName;

    public LiveStockInfo(StockTracker tracker) throws IOException
    {
        this.tracker = tracker;
        this.ticker = tracker.getTicker();
        this.stockName = YahooFinance.get(ticker).getName();

        setName("Ticker: " + ticker + " | " + "Name: " + stockName);
        separateWindow(true);
        doSerialization(false);
        showInPortfolio(false);
    }

    @Override
    public void defaultImGui()
    {


        if (ImGui.button("Close Popout"))
        {
            die();
        }
    }

}
