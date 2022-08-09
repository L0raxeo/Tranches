package com.tranches.components;

import com.tranches.imgui.PortfolioWindow;
import com.tranches.imgui.TImGui;
import imgui.ImGui;

import java.io.IOException;

public class StockTracker extends Component
{

    private String ticker = "JPM";

    // Determines whether Stock Tracker should record following data
    private boolean priceQuotes;
    private boolean dividends;
    private boolean eps;
    private boolean pe;
    private boolean bvps;
    private boolean pb;
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

        ImGui.text("Select the data you wish to track.");
        if (ImGui.checkbox("Stock Quotes", isTrackingPriceQuotes()))
            trackPriceQuotes(!isTrackingPriceQuotes());

        if (ImGui.checkbox("Dividend History", isTrackingDividends()))
            trackDividends(!isTrackingDividends());

        if (ImGui.checkbox("Earnings Per Share - EPS", isTrackingEarningsPerShare()))
            trackEarningsPerShare(!isTrackingEarningsPerShare());

        if (ImGui.checkbox("Price to Earnings - P/E", isTrackingPriceToEarnings()))
            trackPriceToEarnings(!isTrackingPriceToEarnings());

        if (ImGui.checkbox("Book Value Per Share - BVPS", isTrackingBookValuePerShare()))
            trackBookValuePerShare(!isTrackingBookValuePerShare());

        if (ImGui.checkbox("Price to Book - P/B", isTrackingPriceToBook()))
            trackPriceToBook(!isTrackingPriceToBook());

        if (ImGui.checkbox("Price History", isTrackingPriceHistory()))
            trackPriceHistory(!isTrackingPriceHistory());

        if (ImGui.button("Open live stock info in new window"))
        {
            try {
                LiveStockInfo lsi = new LiveStockInfo(this);
                PortfolioWindow.getInstance().addComponent(lsi);
                lsi.setParentUid(getUid());
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

    public boolean isTrackingEarningsPerShare()
    {
        return this.eps;
    }

    public boolean isTrackingPriceToEarnings()
    {
        return this.pe;
    }

    public boolean isTrackingBookValuePerShare()
    {
        return this.bvps;
    }

    public boolean isTrackingPriceToBook()
    {
        return pb;
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

    public void trackEarningsPerShare(boolean track)
    {
        this.eps = track;
    }

    public void trackPriceToEarnings(boolean track)
    {
        this.pe = track;
    }

    public void trackBookValuePerShare(boolean track)
    {
        this.bvps = track;
    }

    public void trackPriceToBook(boolean track)
    {
        this.pb = track;
    }

}
