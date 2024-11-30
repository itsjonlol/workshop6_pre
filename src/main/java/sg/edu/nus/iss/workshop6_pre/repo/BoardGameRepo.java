package sg.edu.nus.iss.workshop6_pre.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop6_pre.util.Util;

@Repository
public class BoardGameRepo {
    
    @Autowired
    @Qualifier(Util.template01)
    RedisTemplate<String,String> template;


    public void leftPush(String redisKey, String value) { //lpush cart apple
        template.opsForList().leftPush(redisKey, value); //template.opsForList().leftpush("cart","apple");
    }
    public void rightPush(String redisKey, String value) {
        template.opsForList().rightPush(redisKey, value);
    }
    //can pop by count or by key. // can i get the string directly?
    public void leftPop(String redisKey) { //lpop cart
        template.opsForList().leftPop(redisKey,1); //template.opsForList().leftpop("cart");
    }
    public void rightPop(String redisKey) {
        template.opsForList().leftPop(redisKey,1);
    }

    //get value at index
    public String getValueAtIndex(String redisKey, Integer index) { //lindex cart 1
        return template.opsForList().index(redisKey,index); // String item = tempalte.opsForList().index("cart",1L);

    }
    //update value at index
    public void updateListValue(String redisKey, Integer index, String newValue) {
        template.opsForList().set(redisKey, index, newValue); //-LSET cart 1 "grape"

    } 
    
    //size
    public Long size(String redisKey) { //lllreen cart
        return template.opsForList().size(redisKey); //long cartLen = template.opsForlist().size("cart");
    }
    //key is the list
    public List<String> getList(String redisKey) {
        List<String> list = template.opsForList().range(redisKey,0,-1); //lrange redisKey 0 -1
        return list;
    }
    //Delete
    public Boolean delete(String redisKey,Integer count, String value) {//lrem cart 1 apple or "apple"
        Boolean isDeleted = false;
        Long iFound = template.opsForList().remove(redisKey,count,value);
        /*
         * Removes elements equal to the value:
         * - count > 0: Remove `count` occurrences from the head (left).
         * - count < 0: Remove `count` occurrences from the tail (right).
         * - count == 0: Remove all occurrences of the value.
         */
        
        
        if (iFound>0) {
            isDeleted = true;
        }
        return isDeleted;
        
        
    }

}



//     public void createValue(String key, BoardGame boardGame) {
//         template.opsForValue().set(key,boardGame.toString());
//     }

//     public BoardGame getValue(String key) {
//         String rawData = template.opsForValue().get(key);

//         String[] data = rawData.split(",");
//         BoardGame boardGame = new BoardGame(key,data[0],Integer.parseInt(data[1]),data[2]);
//         return boardGame;

//     }

//     public Boolean checkIfBoardGameExists(String key) {
//         return template.hasKey(key);
//     }

//     public void updateValue(String key,BoardGame boardGame) {
//         template.opsForValue().set(key,boardGame.toString());
//     }

//     public Boolean deleteValue(String key) {
//         return template.delete(key);
//     }
//     public List<BoardGame> getAllValues() {
//         Set<String> keysSet = template.keys("*");
//         List<String> keysList = new ArrayList<>(keysSet);
//         List<BoardGame> boardGamesList = new ArrayList<>();

//         for (String key : keysList) {
//             String rawData = template.opsForValue().get(key);
//             String[] data = rawData.split(",");
//             BoardGame boardGame = new BoardGame(key,data[0],Integer.parseInt(data[1]),data[2]);
//             boardGamesList.add(boardGame);
//         }
//         return boardGamesList;
//     }
// }
