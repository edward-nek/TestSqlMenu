package ru.nekrasoved.testsqlmenu;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Site {

    private int id;
    private String name, alias;
    private int parent;
    public ArrayList<Site> childrens;
    int level = 0;
    String path = "";

    public Site(int id, String name, String alias, int parent) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.parent = parent;
    }

    public Site() {
        childrens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public int getParent() { return parent; }

    public List<Site> getChildrens() { return childrens; }

    public String getString() { return  "id"+"="+id+"&&"+"name"+"="+name+"&&"+"alias"+"="+alias+"&&"+
            "parent"+"="+parent; }
}
