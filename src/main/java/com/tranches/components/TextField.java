package com.tranches.components;

import com.tranches.imgui.TImGui;

public class TextField extends Component
{

    private String textFieldContent = "";

    public TextField()
    {
        super("Text Field");
    }

    @Override
    public void imgui()
    {
        setTextFieldContent(TImGui.inputText(getName(), getTextFieldContent()));
    }

    public String getTextFieldContent()
    {
        return this.textFieldContent;
    }

    public void setTextFieldContent(String text)
    {
        this.textFieldContent = text;
    }

}
