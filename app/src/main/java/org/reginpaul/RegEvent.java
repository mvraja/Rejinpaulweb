package org.reginpaul;

public class RegEvent {
    private String title;
    private String type;
    private String date;
    private String loc;
    private String image;

    public RegEvent(String title, String type, String date, String loc, String image) {
        this.title = title;
        this.type = type;
        this.date = date;
        this.loc = loc;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getLoc() {
        return loc;
    }

    public String getImage() {
        return image;
    }
}
