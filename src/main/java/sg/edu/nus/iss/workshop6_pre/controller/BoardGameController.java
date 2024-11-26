package sg.edu.nus.iss.workshop6_pre.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.service.BoardGameService;


@RestController
@RequestMapping("/api")
public class BoardGameController {

    @Autowired
    BoardGameService boardGameService;

    int updateCount = 0;
    
    @PostMapping("/boardgame")
    public ResponseEntity<String> postBoardGame(@RequestBody String boardGameJson) {
        JsonObject body;
        
        try (InputStream is = new ByteArrayInputStream(boardGameJson.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
            
            BoardGame boardGame = new BoardGame(body.getString("name"),body.getInt("players"),body.getString("genre"));
            String redisKey = boardGame.getId();
            boardGameService.createValue(redisKey, boardGame);
            JsonObject responseJson  = Json.createObjectBuilder()
                                            .add("insert_count",1)
                                            .add("id",redisKey)
                                            .build();
            return ResponseEntity.status(201).body(responseJson.toString());
            // return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
            // JsonReader reader = Json.createReader(is);
            // body = reader.readObject();
        } catch (Exception ex) {
            // JsonObject errorJson = Json.createObjectBuilder()
            //                             .add("error","Failed to insert board game")
            //                             .add("details", ex.getMessage())
            //                             .build();
            // return ResponseEntity.status(500).body(errorJson.toString());
            //notes
            JsonObject errorJson = Json.createObjectBuilder()
                                       .add("error",ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(errorJson.toString());
        }
    }

    @GetMapping("/boardgame/{boardgameid}")
    public ResponseEntity<String> getBoardGameById(@PathVariable("boardgameid") String boardGameId) {
        

        if (!boardGameService.checkIfBoardGameExists(boardGameId)) {
            JsonObject errorJson = Json.createObjectBuilder()
                                       .add("error","BoardGame not found")
                                       .build();
            return ResponseEntity.status(404).body(errorJson.toString());
            
        }
        BoardGame boardGame = boardGameService.getValue(boardGameId);
        JsonObject responseJson = Json.createObjectBuilder()
                              .add("id",boardGame.getId())
                              .add("name",boardGame.getName())
                              .add("players",boardGame.getPlayers())
                              .add("genre",boardGame.getGenre())
                              .build();
        return ResponseEntity.status(200).body(responseJson.toString());
        
        
    }

    @PutMapping("/boardgame/{boardgameid}")
    public ResponseEntity<String> updateBoardGame(@PathVariable("boardgameid") String boardGameId, @RequestBody String boardGameJson
                                                    , @RequestParam(value = "upsert",required = false,defaultValue="false") Boolean upsert) {

                                                        
        if (!boardGameService.checkIfBoardGameExists(boardGameId)) {
            if (upsert==false) {
                JsonObject errorJson = Json.createObjectBuilder()
                                       .add("error","BoardGame does not exist. Bad request.")
                                       .build();
                return ResponseEntity.status(400).body(errorJson.toString());
            } else {
                try (InputStream is = new ByteArrayInputStream(boardGameJson.getBytes())) {
                    JsonObject body;
                    JsonReader reader = Json.createReader(is);
                    body = reader.readObject();
                    BoardGame boardGame = new BoardGame(boardGameId,body.getString("name"),body.getInt("players"),body.getString("genre"));
                    String redisKey = boardGameId;
                    boardGameService.createValue(redisKey, boardGame);
                    JsonObject responseJson  = Json.createObjectBuilder()
                                                    .add("insert_count",1)
                                                    .add("id",redisKey)
                                                    .build();
                    return ResponseEntity.status(201).body(responseJson.toString());
                    
                } catch (Exception ex) {
                   
                    JsonObject errorJson = Json.createObjectBuilder()
                                               .add("error",ex.getMessage()).build();
                    return ResponseEntity.internalServerError().body(errorJson.toString());
                }
            }
            
        } else {
            JsonObject body;
        
            try (InputStream is = new ByteArrayInputStream(boardGameJson.getBytes())) {
                JsonReader reader = Json.createReader(is);
                body = reader.readObject();
                
                BoardGame boardGame = new BoardGame(boardGameId,body.getString("name"),body.getInt("players"),body.getString("genre"));
                String redisKey = boardGame.getId();
                boardGameService.updateValue(redisKey, boardGame);
                updateCount++;
                JsonObject responseJson  = Json.createObjectBuilder()
                                                .add("update_count",updateCount)
                                                .add("id",redisKey)
                                                .build();
                return ResponseEntity.status(200).body(responseJson.toString());
                
            } catch (Exception ex) {
                
                JsonObject errorJson = Json.createObjectBuilder()
                                        .add("error",ex.getMessage()).build();
                return ResponseEntity.internalServerError().body(errorJson.toString());
            }

        }

        

        
    }

    @DeleteMapping("/boardgame/{boardgameid}")
    public ResponseEntity<String> deleteBoardGame(@PathVariable("boardgameid") String boardGameId) {
        //DELETE regardless whether id even existed in the first place
        boardGameService.deleteValue(boardGameId);
        JsonObject responseJson = Json.createObjectBuilder()
                              .add("message","BoardGame has been deleted")
                              .build();
        return ResponseEntity.status(200).body(responseJson.toString());
        
    }
    @GetMapping("/boardgames")
    public ResponseEntity<List> showAllBoardGames() {
        List<BoardGame> boardGamesList = boardGameService.getAllValues();
        return ResponseEntity.ok(boardGamesList);

    }


}
