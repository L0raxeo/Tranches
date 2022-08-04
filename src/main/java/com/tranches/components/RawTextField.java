package com.tranches.components;

import com.tranches.imgui.TImGui;
import imgui.ImGui;

public class RawTextField extends Component
{

    private String textFieldContent = "";

    public RawTextField()
    {
        setTags(" ");
    }

    @Override
    public void defaultImGui()
    {
        if (getTextFieldContent().contains(":"))
            setTags(getTextFieldContent().split(":")[0]);

        setTextFieldContent(TImGui.inputText(getTags(), getTextFieldContent()));
        ImGui.sameLine();
        if (ImGui.button("Delete"))
        {
            die();
        }
    }

    public String getTextFieldContent()
    {
        return textFieldContent;
    }

    public void setTextFieldContent(String text)
    {
        this.textFieldContent = text;
    }

}
