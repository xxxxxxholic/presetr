package com.example.presetr.model;

import java.util.ArrayList;
import java.util.List;

public class FilterPackageBean {
    private int order;
    private String name;
    private int type;
    private short isCharged;
    private float price;
    private String displayName;
    private String abbreviation;
    private String description;
    private String cover;
    private List<FilterBean> filters = new ArrayList<>(15);

    public FilterPackageBean() {
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public short getIsCharged() {
        return isCharged;
    }

    public void setIsCharged(short isCharged) {
        this.isCharged = isCharged;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<FilterBean> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterBean> filters) {
        this.filters = filters;
    }
}
