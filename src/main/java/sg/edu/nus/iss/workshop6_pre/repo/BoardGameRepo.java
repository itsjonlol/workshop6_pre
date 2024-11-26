package sg.edu.nus.iss.workshop6_pre.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop6_pre.model.BoardGame;
import sg.edu.nus.iss.workshop6_pre.util.Util;

@Repository
public class BoardGameRepo {
    
    @Autowired
    @Qualifier(Util.template01)
    RedisTemplate<String,String> template;

    public void createValue(String key, BoardGame boardGame) {
        template.opsForValue().set(key,boardGame.toString());
    }

    public BoardGame getValue(String key) {
        String rawData = template.opsForValue().get(key);

        String[] data = rawData.split(",");
        BoardGame boardGame = new BoardGame(key,data[0],Integer.parseInt(data[1]),data[2]);
        return boardGame;

    }

    public Boolean checkIfBoardGameExists(String key) {
        return template.hasKey(key);
    }

    public void updateValue(String key,BoardGame boardGame) {
        template.opsForValue().set(key,boardGame.toString());
    }

    public Boolean deleteValue(String key) {
        return template.delete(key);
    }
    public List<BoardGame> getAllValues() {
        Set<String> keysSet = template.keys("*");
        List<String> keysList = new ArrayList<>(keysSet);
        List<BoardGame> boardGamesList = new ArrayList<>();

        for (String key : keysList) {
            String rawData = template.opsForValue().get(key);
            String[] data = rawData.split(",");
            BoardGame boardGame = new BoardGame(key,data[0],Integer.parseInt(data[1]),data[2]);
            boardGamesList.add(boardGame);
        }
        return boardGamesList;
    }
}
