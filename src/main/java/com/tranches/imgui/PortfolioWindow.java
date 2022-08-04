package com.tranches.imgui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tranches.components.*;
import com.tranches.utils.ComponentSerializer;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PortfolioWindow
{

    public Map<Class<? extends Component>, String> componentTypes = new HashMap<>();
    private final List<Component> components = new ArrayList<>();

    private final String saveFilepath = "assets/saves/save.txt";
    private static PortfolioWindow instance;

    // Search bar
    private String searchFilter = "";

    public PortfolioWindow()
    {
        load();
        loadComponentTypes();
    }

    private void loadComponentTypes()
    {
        componentTypes.put(EmptyComponent.class, "Empty Profile");
        componentTypes.put(TextArea.class, "Text Area");
        componentTypes.put(TextField.class, "Text Field");
        componentTypes.put(RawTextField.class, "Raw Text Field");
    }

    public void imgui()
    {
        ImGui.begin("Portfolio");

        // Menu Bar
        if (ImGui.beginMainMenuBar())
        {
            if (ImGui.beginMenu("Edit Portfolio"))
            {
                if (ImGui.beginMenu("Create Profile"))
                {
                    for (Map.Entry<Class<? extends Component>, String> e : componentTypes.entrySet())
                        if (ImGui.button(e.getValue()))
                        {
                            addComponent(Component.create(e.getKey()));
                        }

                    ImGui.endMenu();
                }

                ImGui.endMenu();
            }

            ImGui.endMainMenuBar();
        }

        // Search bar
        searchFilter = TImGui.inputText("Search", searchFilter);
        List<String> parsedSearchFilter = List.of(searchFilter.split(","));

        // Portfolio content
        try
        {
            for (Component c : components)
            {
                if (!c.isAlive())
                    removeComponent(c);

                List<Component> componentsWithSearchTags = c.getComponentsWithTag(parsedSearchFilter);

                if (componentsWithSearchTags.size() == 0)
                {
                    c.disable();
                    System.out.println(componentsWithSearchTags);
                }
                else
                    for (Component c1 : componentsWithSearchTags)
                        c1.enable();

                if (c.getParentUid() == -1 && !c.isDisabled())
                    c.defaultImGui();
            }
        }
        catch (ConcurrentModificationException ignore) {}

        ImGui.end();

        // Popouts
        try
        {
            for (Component c : components)
            {
                if (c.isSeparateWindow())
                {
                    ImGui.begin(c.getName());
                    c.defaultImGui();
                    ImGui.end();
                }
            }
        }
        catch (ConcurrentModificationException ignore) {}
    }

    public void addComponent(Component c)
    {
        this.components.add(c);
    }

    public void removeComponent(Component c)
    {
        this.components.remove(c);
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            FileWriter writer = new FileWriter(saveFilepath);
            List<Component> compsToSerialize = new ArrayList<>(this.components);
            Type typeOfSrc = new TypeToken<List<Component>>(){}.getType();
            writer.write(gson.toJson(compsToSerialize, typeOfSrc));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        ImGui.saveIniSettingsToDisk("assets/saves/imgui.ini");
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .enableComplexMapKeySerialization()
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(saveFilepath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxCompId = -1;
            Component[] comps = gson.fromJson(inFile, Component[].class);
            for (Component comp : comps) {
                addComponent(comp);

                if (comp.getUid() > maxCompId)
                    maxCompId = comp.getUid();
            }

            maxCompId++;
            Component.init(maxCompId);
        }
    }

    public List<Component> getComponents()
    {
        return this.components;
    }

    public boolean componentExists(int uid)
    {
        for (Component c : getComponents())
        {
            if (c.getUid() == uid)
            {
                return c.isAlive();
            }
        }

        return false;
    }

    public static PortfolioWindow getInstance()
    {
        if (instance == null)
            instance = new PortfolioWindow();

        return instance;
    }

}
