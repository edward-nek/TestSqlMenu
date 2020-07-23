package ru.nekrasoved.testsqlmenu;

import java.util.List;

public class Answer {

    List<Site> menu_site;
    Integer succes;

    public Answer(List<Site> menu_site, Integer succes) {
        this.menu_site = menu_site;
        this.succes = succes;
    }
}
