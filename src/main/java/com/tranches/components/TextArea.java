package com.tranches.components;

import com.tranches.imgui.TImGui;

public class TextArea extends Component
{

    private String textEntryContent = "";

    public TextArea()
    {
        super("Text Area");
    }

    @Override
    public void imgui()
    {
        setTextEntryContent(TImGui.inputTextMultiline(getName(), getTextEntryContent()));
    }

    public String getTextEntryContent()
    {
        return this.textEntryContent;
    }

    public void setTextEntryContent(String text)
    {
        this.textEntryContent = text;
    }

}
