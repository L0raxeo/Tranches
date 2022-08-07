package com.tranches.imgui;

import imgui.ImGui;

public class OverviewWindow
{

    private static OverviewWindow instance;

    public void imgui()
    {
        ImGui.begin("Overview");

        ImGui.end();
    }

    public static OverviewWindow getInstance()
    {
        if (instance == null)
            instance = new OverviewWindow();

        return instance;
    }

}