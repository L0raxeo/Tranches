package com.tranches.components;

import com.tranches.imgui.PortfolioWindow;
import com.tranches.imgui.TImGui;
import com.tranches.utils.TMath;
import imgui.ImGui;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Component
{

    private String name;
    private String tags;

    private List<Integer> childrenUids;
    private int parentUid = -1;

    private static int ID_COUNTER = 0;
    private int uid = -1;

    private boolean separateWindow = false;

    private transient  boolean isDisabled = false;
    private transient boolean isAlive = true;

    private transient String nameModifiers = "";

    public Component()
    {
        this.name = "Default Name";
        this.childrenUids = new ArrayList<>();
        this.tags = "";
        this.uid = ID_COUNTER++;
    }

    public Component(String name)
    {
        this.name = name;
        this.childrenUids = new ArrayList<>();
        this.tags = "";
        this.uid = ID_COUNTER++;
    }

    public Component(String name, String tags)
    {
        this.name = name;
        this.childrenUids = new ArrayList<>();
        this.tags = tags;
        this.uid = ID_COUNTER++;
    }

    public void imgui()
    {

    }

    public void defaultImGui()
    {
        if (ImGui.treeNode(getName() + getNameModifiers()))
        {
            imgui();

            // display children
            try
            {

                for (int childUid : getChildrenUids())
                {
                    try
                    {
                        if (!Objects.requireNonNull(Component.getComponentFromUid(childUid)).isDisabled)
                           Objects.requireNonNull(Component.getComponentFromUid(childUid)).defaultImGui();
                    }
                    catch (NullPointerException e)
                    {
                        getChildrenUids().remove(Integer.valueOf(childUid));
                    }
                }
            }
            catch (ConcurrentModificationException ignore)
            {}

            // Edit menu
            if (!getName().equals("Default Name") && ImGui.treeNode("Edit " + getName()))
            {
                    // name
                    if (ImGui.beginMenu("Change Name"))
                    {
                        TImGui.setSavedInputText1(TImGui.inputText("Name", TImGui.getSavedInputText1()));

                        if (ImGui.button("Save"))
                        {
                            setName(TImGui.getSavedInputText1());
                            TImGui.setSavedInputText1("");
                        }

                        ImGui.endMenu();
                    }

                    // tags
                    setTags(TImGui.inputText("Tags", getTags()));
                    if (getTags().length() > 0 && getTags().substring(getTags().length() - 1).equals(","))
                        setTags(TMath.removeLastChar(getTags()));


                if (ImGui.beginMenu("Create/Add"))
                {
                    for (Map.Entry<Class<? extends Component>, String> e : PortfolioWindow.getInstance().componentTypes.entrySet())
                        if (ImGui.button(e.getValue()))
                        {
                            Component child = Component.create(e.getKey());
                            child.setParentUid(getUid());
                            addChildUid(child.getUid());
                            PortfolioWindow.getInstance().addComponent(child);
                        }

                    ImGui.endMenu();
                }

                if (ImGui.beginMenu("Delete"))
                {
                    if (ImGui.beginMenu("Are you sure?"))
                    {
                        if (ImGui.button("Yes I am sure"))
                        {
                            die();
                        }

                        ImGui.endMenu();
                    }

                    ImGui.endMenu();
                }

                if (!isSeparateWindow() && ImGui.button("Popout"))
                    separateWindow(true);

                ImGui.treePop();
            }

            if (isSeparateWindow() && ImGui.button("Close Popout Window"))
                separateWindow(false);

            ImGui.treePop();
        }
    }

    public List<Component> getComponentsWithTag(List<String> tags)
    {
        List<Component> components = new ArrayList<>();

        for (String tag : getTagsAsList())
        {
            if (tags.contains(tag))
            {
                components.add(this);
                break;
            }
        }

        for (Component child : getChildren())
        {
            components.addAll(child.getComponentsWithTag(tags));
        }

        return components;
    }

    public String getName()
    {
        if (name == null || name.equals(""))
            return "Default Name";

        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public static void init(int maxId)
    {
        ID_COUNTER = maxId;
    }

    public int getUid()
    {
        return this.uid;
    }

    public int getParentUid()
    {
        return this.parentUid;
    }

    public List<Integer> getChildrenUids()
    {
        return this.childrenUids;
    }

    public void addChildUid(int uid)
    {
        this.getChildrenUids().add(uid);
    }

    public void setParentUid(int uid)
    {
        this.parentUid = uid;
    }

    public Component getParent()
    {
        return getComponentFromUid(getParentUid());
    }

    public List<Component> getAllParents()
    {
        List<Component> allParents = new ArrayList<>();

        allParents.add(this);

        if (getParentUid() != -1)
            allParents.addAll(getParent().getAllParents());

        return allParents;
    }

    public List<Component> getAllChildren()
    {

        List<Component> allChildren = new ArrayList<>(getChildren());

        for (Component child : getChildren())
            allChildren.addAll(child.getAllChildren());

        return allChildren;
    }

    public List<String> getTagsAsList()
    {
        return Arrays.asList(getTags().split(","));
    }

    public String getTags()
    {
        return tags.toString();
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public static Component create(Class<? extends Component> c)
    {
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Component getComponentFromUid(int uid)
    {
        for (Component c : PortfolioWindow.getInstance().getComponents())
            if (c.uid == uid)
                return c;

        return null;
    }

    public List<Component> getChildren()
    {
        List<Component> cChildren = new ArrayList<>();

        for (int uid : getChildrenUids())
        {
            cChildren.add(getComponentFromUid(uid));
        }

        return cChildren;
    }

    public void separateWindow(boolean separate)
    {
        this.separateWindow = separate;
    }

    public boolean isSeparateWindow()
    {
        return this.separateWindow;
    }

    public boolean isAlive()
    {
        return this.isAlive;
    }

    public void die()
    {
        // destroys children components
        for (Component child : getChildren())
            child.die();

        // destroy component
        this.isAlive = false;
    }

    public boolean isDisabled()
    {
        return isDisabled;
    }

    public void disable()
    {
        this.isDisabled = true;
    }

    public void enable()
    {
        this.isDisabled = false;
    }

    public String getNameModifiers()
    {
        return this.nameModifiers;
    }

    public void setNameModifiers(String modifiers)
    {
        this.nameModifiers = modifiers;
    }

}
