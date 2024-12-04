package sg.edu.nus.iss.workshop6_pre.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.service.BoardGameService;
import sg.edu.nus.iss.workshop6_pre.util.Util;


@RestController
@RequestMapping("/api")
public class BoardGameController {

    @Autowired
    BoardGameService boardGameService;

    int updateCount = 0;
    
    @PostMapping("/boardgame")
    public ResponseEntity<String> postBoardGame(@RequestParam("file") MultipartFile jsonFile) throws IOException {
        
        //added to account for file upload
        //convert file content to string
        String fileContent = new String(jsonFile.getBytes());
        List<BoardGame> boardGames = boardGameService.readFile(fileContent);

        // Parse JSON content -> if i want to do it straight in the controller
        // JsonReader reader = Json.createReader(new StringReader(fileContent)); // dont need the bytes like darryl's method
        // JsonArray boardGamesJsonArray = reader.readArray();
        
        // BoardGame boardGame = new BoardGame(boardGameJson.getString("name"),boardGameJson.getInt("players"),boardGameJson.getString("genre"));
        for (BoardGame boardGame : boardGames) {
            boardGameService.saveBoardGame(boardGame);

        }
        
        JsonObject responseJson  = Json.createObjectBuilder()
                                        .add("insert_count",1)
                                        .add("id",Util.redisKey)
                                        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson.toString());
        
            // return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
            // JsonReader reader = Json.createReader(is);
            // body = reader.readObject();
        // } catch (Exception ex) {
        //     // JsonObject errorJson = Json.createObjectBuilder()
        //     //                             .add("error","Failed to insert board game")
        //     //                             .add("details", ex.getMessage())
        //     //                             .build();
        //     // return ResponseEntity.status(500).body(errorJson.toString());
        //     //notes
        //     JsonObject errorJson = Json.createObjectBuilder()
        //                                .add("error",ex.getMessage()).build();
        //     return ResponseEntity.internalServerError().body(errorJson.toString());
        // }
    }


    @GetMapping("/boardgame/{boardgameid}")
    public ResponseEntity<String> getBoardGameById(@PathVariable("boardgameid") String boardGameId) {
        

        if (!boardGameService.checkIfBoardGameExists(boardGameId)) {
            JsonObject errorJson = Json.createObjectBuilder()
                                       .add("error","BoardGame not found")
                                       .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorJson.toString());
            
        }
        BoardGame boardGame = boardGameService.getBoardGameById(boardGameId);
        JsonObject responseJson = Json.createObjectBuilder()
                              .add("gid",boardGame.getGid())
                              .add("name",boardGame.getName())
                              .add("year",boardGame.getYear())
                              .add("ranking",boardGame.getRanking())
                              .add("users_rated",boardGame.getUsers_rated())
                              .add("url",boardGame.getUrl())
                              .add("image",boardGame.getImage())
                              .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
        
    }

    @PutMapping("/boardgame/{boardgameid}") //http://localhost:4000/api/boardgame/10e55?upsert=true
    public ResponseEntity<String> updateBoardGame(@PathVariable("boardgameid") String boardGameId, @RequestBody String boardGameJsonString
                                                    , @RequestParam(value = "upsert",required = false,defaultValue="false") Boolean upsert) {

                                                        
        if (!boardGameService.checkIfBoardGameExists(boardGameId)) {
            if (upsert==false) {
                JsonObject errorJson = Json.createObjectBuilder()
                                       .add("error","BoardGame does not exist. Bad request.Try writing ?upsert=true")
                                       .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorJson.toString());
            } else {

                InputStream is = new ByteArrayInputStream(boardGameJsonString.getBytes());
                JsonObject boardGameJson;
                JsonReader reader = Json.createReader(is);
                boardGameJson = reader.readObject();
                // BoardGame boardGame = new BoardGame(boardGameId,boardGameJson.getString("name"),boardGameJson.getInt("players"),boardGameJson.getString("genre"));
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
                return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
                    
            }
            
        } else {
            JsonObject boardGameJson;
        
            InputStream is = new ByteArrayInputStream(boardGameJsonString.getBytes()); 
            JsonReader reader = Json.createReader(is);
            boardGameJson = reader.readObject();
            
            // BoardGame boardGame = new BoardGame(boardGameId,boardGameJson.getString("name"),boardGameJson.getInt("players"),boardGameJson.getString("genre"));
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
            boardGameService.updateBoardGame(boardGame);
            updateCount++;
            JsonObject responseJson  = Json.createObjectBuilder()
                                            .add("update_count",updateCount)
                                            .add("id",boardGame.getGid())
                                            .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
                
        }

    }

    @DeleteMapping("/boardgame/{boardgameid}")
    public ResponseEntity<String> deleteBoardGame(@PathVariable("boardgameid") String boardGameId) {
        //DELETE regardless whether id even existed in the first place
        if (!boardGameService.checkIfBoardGameExists(boardGameId)) {
            JsonObject errorJson = Json.createObjectBuilder()
                                       .add("error","BoardGame not found")
                                       .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorJson.toString());
            
        }
        boardGameService.deleteBoardGame(boardGameId);
        JsonObject responseJson = Json.createObjectBuilder()
                              .add("message","BoardGame has been deleted")
                              .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
        
    }
  

    
    @GetMapping("/boardgames")
    public ResponseEntity<String> showAllGames() {
        List<BoardGame> boardGamesList = boardGameService.getAllBoardGames();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        for (BoardGame boardGame : boardGamesList) {
            JsonObject boardGameJson = Json.createObjectBuilder()
                                            .add("gid",boardGame.getGid())
                                            .add("name",boardGame.getName())
                                            .add("year",boardGame.getYear())
                                            .add("ranking",boardGame.getRanking())
                                            .add("users_rated",boardGame.getUsers_rated())
                                            .add("url",boardGame.getUrl())
                                            .add("image",boardGame.getImage())
                                            .build();
             jab.add(boardGameJson);
        }

        String boardGameJsonArrayString = jab.build().toString();
        return ResponseEntity.status(HttpStatus.OK).body(boardGameJsonArrayString);

    }

    @GetMapping("/boardgames2") //same as /boardgames, just easier to read. can wrap around a responseentity too
    public ResponseEntity<List<BoardGame>> showAllGames2() {
        List<BoardGame> boardGamesList = boardGameService.getAllBoardGames();
        return ResponseEntity.status(HttpStatus.OK).body(boardGamesList);

    }

}
