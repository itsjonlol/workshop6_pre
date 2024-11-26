package sg.edu.nus.iss.workshop6_pre.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.repo.BoardGameRepo;

@Service
public class BoardGameService {
    
    @Autowired
    BoardGameRepo boardGameRepo;

    public void createValue(String redisKey,BoardGame boardGame) {
        boardGameRepo.createValue(redisKey, boardGame);
    }
    public BoardGame getValue(String redisKey) {
        return boardGameRepo.getValue(redisKey);
    }
    public Boolean checkIfBoardGameExists(String redisKey) {
        return boardGameRepo.checkIfBoardGameExists(redisKey);
    }
    public void updateValue(String redisKey, BoardGame boardGame) {
        boardGameRepo.updateValue(redisKey, boardGame);
    }
    public Boolean deleteValue(String redisKey) {
        return boardGameRepo.deleteValue(redisKey);
    }
    public List<BoardGame> getAllValues() {
        return boardGameRepo.getAllValues();
    }

}
