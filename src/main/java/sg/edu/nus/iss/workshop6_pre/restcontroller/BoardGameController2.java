package sg.edu.nus.iss.workshop6_pre.restcontroller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.service.BoardGameService;
import sg.edu.nus.iss.workshop6_pre.util.Util;

@RestController
@RequestMapping("/api2")
public class BoardGameController2 {

    @Autowired
    BoardGameService boardGameService;
    // for the case where i copy paste an array of jsonobjects containingn the boardgames, instead of uploading file ( list of boardgames)
    @PostMapping("/boardgame") 
    public ResponseEntity<String> postBoardGame(@RequestBody String boardGameJsonArrayString) {
        
        //convert the string formatted json into json object
        InputStream is = new ByteArrayInputStream(boardGameJsonArrayString.getBytes());
        JsonReader reader = Json.createReader(is);
        
        JsonArray boardGamesJsonArray = reader.readArray();
        List<BoardGame> boardGames = new ArrayList<>();
        

        for (int i = 0; i<boardGamesJsonArray.size(); i++) {
            JsonObject individualGameJson = boardGamesJsonArray.getJsonObject(i);
            BoardGame boardGame = new BoardGame();
            Integer gid = individualGameJson.getInt("gid");
            String name = individualGameJson.getString("name");
            Integer year = individualGameJson.getInt("year");
            Integer ranking = individualGameJson.getInt("ranking");
            Integer users_rated = individualGameJson.getInt("users_rated");
            String url = individualGameJson.getString("url");
            String image = individualGameJson.getString("image");
            boardGame.setGid(gid);
            boardGame.setName(name);
            boardGame.setRanking(ranking);
            boardGame.setYear(year);
            boardGame.setUsers_rated(users_rated);
            boardGame.setUrl(url);
            boardGame.setImage(image);
            boardGames.add(boardGame);
            boardGameService.saveBoardGame(boardGame);
        }
        
        
        JsonObject responseJson  = Json.createObjectBuilder()
                                        .add("insert_count",1)
                                        .add("id",Util.redisKey)
                                        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson.toString());
    
    }
    @PostMapping("/boardgameindividual") // for the case where i add one boardgame only at a time -> putmapping
    public ResponseEntity<String> postBoardGame2(@RequestBody String boardGameJsonString) {
        
        //convert the string formatted json into json object
        InputStream is = new ByteArrayInputStream(boardGameJsonString.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject boardGameJson = reader.readObject();
        
        BoardGame boardGame = new BoardGame();
        Integer gid = boardGameJson.getInt("gid");
        String name = boardGameJson.getString("name");
        Integer year = boardGameJson.getInt("year");
        Integer ranking = boardGameJson.getInt("ranking");
        Integer users_rated = boardGameJson.getInt("users_rated");
        String url = boardGameJson.getString("url");
        String image = boardGameJson.getString("image");
        boardGame.setGid(gid);
        boardGame.setName(name);
        boardGame.setRanking(ranking);
        boardGame.setYear(year);
        boardGame.setUsers_rated(users_rated);
        boardGame.setUrl(url);
        boardGame.setImage(image);
    
        boardGameService.saveBoardGame(boardGame);
        
        JsonObject responseJson  = Json.createObjectBuilder()
                                        .add("insert_count",1)
                                        .add("id",boardGame.getGid())
                                        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson.toString());
    
    }
}
