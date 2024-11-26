package sg.edu.nus.iss.workshop6_pre.model;

import java.util.UUID;

public class BoardGame {

    private String id;
    private String name;
    private Integer players;
    private String genre;

    public BoardGame() {
        this.id = UUID.randomUUID().toString().substring(0,4);
    }

    public BoardGame(String name, Integer players, String genre) {
        this.name = name;
        this.players = players;
        this.genre = genre;
        this.id = UUID.randomUUID().toString().substring(0,4);
    }

    
    

    public BoardGame(String id, String name, Integer players, String genre) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPlayers() {
        return players;
    }
    public void setPlayers(Integer players) {
        this.players = players;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return name + "," + players + "," + genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    
    
    
}
