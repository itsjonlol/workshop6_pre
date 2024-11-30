package sg.edu.nus.iss.workshop6_pre.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.repo.BoardGameRepo;
import sg.edu.nus.iss.workshop6_pre.util.Util;

@Service
public class BoardGameService {
    
    @Autowired
    BoardGameRepo boardGameRepo;

    public void saveBoardGame(BoardGame boardGame) {
        boardGameRepo.rightPush(Util.redisKey, boardGame.toString());
    }

    public List<BoardGame> getAllBoardGames() {
        List<String> boardGameRawData = boardGameRepo.getList(Util.redisKey);
        List<BoardGame> boardGameList = new ArrayList<>();
        for (String individualRawData : boardGameRawData) {
            String[] rawData = individualRawData.split(",");
            String id = rawData[0];
            String name = rawData[1];
            Integer players = Integer.parseInt(rawData[2]);
            String genre = rawData[3];
            BoardGame boardGame = new BoardGame(id,name,players,genre);
            boardGameList.add(boardGame);
        }
        return boardGameList;
    }

    public BoardGame getBoardGameById(String boardGameId) {

        List<BoardGame> boardGameList = this.getAllBoardGames();
        for (BoardGame boardGame : boardGameList) {
            if (boardGame.getId().equals(boardGameId)) {
                return boardGame;
            }
        }
        return null;
 
    }

    public Boolean checkIfBoardGameExists(String boardGameId) {
        List<BoardGame> boardGameList = this.getAllBoardGames();
        for (BoardGame boardGame : boardGameList) {
            if (boardGameId.equals(boardGame.getId())) {
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
            String id = rawData[0];
            

            if (id.equals(boardGameNew.getId())) {
                boardGame.setId(id);
                boardGame.setName(boardGameNew.getName());   
                boardGame.setPlayers(boardGameNew.getPlayers());
                boardGame.setGenre(boardGameNew.getGenre());
                boardGameRepo.updateListValue(Util.redisKey,i,boardGame.toString());
            }
            
        }
    }


    public Boolean deleteBoardGame(String boardGameId) {
        BoardGame boardGame = this.getBoardGameById(boardGameId);

        return boardGameRepo.delete(Util.redisKey, 0, boardGame.toString());
    }
    

}
