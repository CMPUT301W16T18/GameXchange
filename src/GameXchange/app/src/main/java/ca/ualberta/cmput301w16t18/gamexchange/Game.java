package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class Game {
    private String id;
    private String status;
    private String title;
    private String developer;
    private String platform;
    private ArrayList<String> genres;
    private String description;

    public Game() {}

    public Game(String id, String status, String title, String developer, String platform,
                ArrayList<String> genres, String description) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.developer = developer;
        this.platform = platform;
        this.genres = genres;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
