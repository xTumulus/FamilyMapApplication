package com.kitchdevelopment.familymapclient.utils;

public class ListItem {
    private String id;
    private String displayText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public ListItem (String i_id, String i_text) {
        id = i_id;
        displayText = i_text;
    }
}
