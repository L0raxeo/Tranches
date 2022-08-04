package com.tranches.imgui;

import imgui.ImGui;
import imgui.type.ImString;

public class TImGui
{

    public static String inputText(String label, String text)
    {
        ImString outString = new ImString(text, 256);

        if (ImGui.inputText(label, outString))
        {
            return outString.get();
        }

        return text;
    }

    public static String inputTextMultiline(String label, String text)
    {
        ImString outString = new ImString(text, 1920);

        if (ImGui.inputTextMultiline(label, outString))
        {
            return outString.get();
        }

        return text;
    }

    // WORKAROUND BULLSHIT

    private static String savedInputText1 = "";
    private static String savedInputText2 = "";

    public static void setSavedInputText1(String text)
    {
        savedInputText1 = text;
    }

    public static String getSavedInputText1()
    {
        return savedInputText1;
    }

    public static void setSavedInputText2(String text)
    {
        savedInputText2 = text;
    }

    public static String getSavedInputText2()
    {
        return savedInputText2;
    }

}
