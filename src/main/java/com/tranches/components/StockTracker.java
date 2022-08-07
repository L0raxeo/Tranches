package com.tranches.components;

import com.tranches.imgui.PortfolioWindow;
import com.tranches.imgui.TImGui;
import imgui.ImGui;

import java.io.IOException;

public class StockTracker extends Component
{

    private String ticker = "JPM";

    private int currentPosition = 0;

    // Determines whether Stock Tracker should record following data
    private boolean priceQuotes;
    private boolean dividends;
    private boolean priceHistory;

    public StockTracker()
    {
        super ("Stock Tracker");

        setTags("stock_tracker");
    }

    @Override
    public void imgui()
    {
        setTicker(TImGui.inputText("Ticker",getTicker()));

        try
        {
            setCurrentPosition(Integer.parseInt(TImGui.inputText("# of shares for " + getTicker(), String.valueOf(getCurrentPosition()))));
        }
        catch (NumberFormatException ignore)
        {
            setCurrentPosition(0);
        }

        ImGui.sameLine();
        if (ImGui.button("-") && getCurrentPosition() != 0)
            setCurrentPosition(getCurrentPosition() - 1);
        ImGui.sameLine();
        if (ImGui.button("+"))
            setCurrentPosition(getCurrentPosition() + 1);

        ImGui.text("Select the data you wish to track.");
        if (ImGui.checkbox("Stock Quotes", isTrackingPriceQuotes()))
            trackPriceQuotes(!isTrackingPriceQuotes());

        if (ImGui.checkbox("Dividend History", isTrackingDividends()))
            trackDividends(!isTrackingDividends());

        if (ImGui.checkbox("Price History", isTrackingPriceHistory()))
            trackPriceHistory(!isTrackingPriceHistory());

        if (ImGui.button("Open live stock info in new window"))
        {
            try {
                PortfolioWindow.getInstance().addComponent(new LiveStockInfo(this));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getTicker()
    {
        return this.ticker;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public boolean isTrackingDividends()
    {
        return this.dividends;
    }

    public boolean isTrackingPriceHistory()
    {
        return this.priceHistory;
    }

    public boolean isTrackingPriceQuotes()
    {
        return this.priceQuotes;
    }

    public void trackDividends(boolean track)
    {
        this.dividends = track;
    }

    public void trackPriceHistory(boolean track)
    {
        this.priceHistory = track;
    }

    public void trackPriceQuotes(boolean track)
    {
        this.priceQuotes = track;
    }

    public int getCurrentPosition()
    {
        return this.currentPosition;
    }

    public void setCurrentPosition(int position)
    {
        this.currentPosition = position;
    }

}
