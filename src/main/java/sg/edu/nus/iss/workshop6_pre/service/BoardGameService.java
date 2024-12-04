package sg.edu.nus.iss.workshop6_pre.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.repo.BoardGameRepo;
import sg.edu.nus.iss.workshop6_pre.util.Util;

@Service
public class BoardGameService {
    
    @Autowired
    BoardGameRepo boardGameRepo;

    


    public List<BoardGame> readFile(String jsonContent) throws IOException {
        // method 1 -> where events.json in your resource folder
        // ClassPathResource resource = new ClassPathResource(fileName);
        // InputStream is = resource.getInputStream();
        // JsonReader reader = Json.createReader(is);
        // JsonArray boardGamesJsonArray = reader.readArray();

        //method 2 --> how i usually get the bytes
        InputStream is = new ByteArrayInputStream(jsonContent.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonArray boardGamesJsonArray = reader.readArray();



        //method 3 --> darryl's method
        // JsonReader reader = Json.createReader(new StringReader(jsonContent));
        // JsonArray boardGamesJsonArray = reader.readArray();
        

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
        }
        return boardGames;
    }

    public void saveBoardGame(BoardGame boardGame) {
        boardGameRepo.rightPush(Util.redisKey, boardGame.toString());
    }

    // private Integer gid;
    // private String name;
    // private Integer year;
    // private Integer ranking;
    // private Integer users_rated;
    // private String url;
    // private String image;

    public List<BoardGame> getAllBoardGames() {
        List<String> boardGameRawData = boardGameRepo.getList(Util.redisKey);
        List<BoardGame> boardGameList = new ArrayList<>();
        for (String individualRawData : boardGameRawData) {
            
            String[] rawData = individualRawData.split(",");
            
            Integer gid = Integer.valueOf(rawData[0]);
            String name = rawData[1];
            Integer year = Integer.valueOf(rawData[2]);
            Integer ranking = Integer.valueOf(rawData[3]);
            Integer users_rated = Integer.valueOf(rawData[4]);
            String url = rawData[5];
            String image = rawData[6];
            BoardGame boardGame = new BoardGame();
            boardGame.setGid(gid);
            boardGame.setName(name);
            boardGame.setRanking(ranking);
            boardGame.setYear(year);
            boardGame.setUsers_rated(users_rated);
            boardGame.setUrl(url);
            boardGame.setImage(image);
            boardGameList.add(boardGame);


        }
        return boardGameList;
    }

    public BoardGame getBoardGameById(String boardGameId) {

        List<BoardGame> boardGameList = this.getAllBoardGames();
        for (BoardGame boardGame : boardGameList) {
            if (boardGame.getGid() == Integer.valueOf(boardGameId)) {
                return boardGame;
            }
        }
        return null;
 
    }

    public Boolean checkIfBoardGameExists(String boardGameId) {
        List<BoardGame> boardGameList = this.getAllBoardGames();
        for (BoardGame boardGame : boardGameList) {
            if (boardGame.getGid() == Integer.valueOf(boardGameId)) {
                return true;
            }
        }
        return false;
    }
    
    public void updateBoardGame(BoardGame boardGameNew) {
       
        
        List<String> boardGameRawData = boardGameRepo.getList(Util.redisKey);
        BoardGame boardGame = new BoardGame();
        for (int i = 0; i<boardGameRawData.size();i++) {
            
            String[] rawData = boardGameRawData.get(i).split(",");
            Integer gid = Integer.valueOf(rawData[0]);
            

            if (gid == boardGameNew.getGid()){
        
                boardGame.setGid(gid);
                boardGame.setName(boardGameNew.getName());
                boardGame.setRanking(boardGameNew.getRanking());
                boardGame.setYear(boardGameNew.getYear());
                boardGame.setUsers_rated(boardGameNew.getUsers_rated());
                boardGame.setUrl(boardGameNew.getUrl());
                boardGame.setImage(boardGameNew.getImage());
                boardGameRepo.updateListValue(Util.redisKey,i,boardGame.toString());
            }
            
        }
    }


    public Boolean deleteBoardGame(String boardGameId) {
        BoardGame boardGame = this.getBoardGameById(boardGameId);

        return boardGameRepo.delete(Util.redisKey, 0, boardGame.toString());
    }
    

}
