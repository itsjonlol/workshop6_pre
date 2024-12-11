package sg.edu.nus.iss.workshop6_pre.service;

import java.net.URI;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class BoardGameRestService {

    RestTemplate restTemplate = new RestTemplate();

    public String deleteBoardGame(String id) throws Exception {
        //delete doesn't have a payload
        //aplication properties -> my.api.pass-key=abc123

        //export MY_API_PASS_KEY = "this is my api key"
        String url = "http://localhost:4000/api/boardgame/" + id;
        HttpHeaders headers = new HttpHeaders();
        
        
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        RequestEntity<Void> requestEntity = RequestEntity.delete(URI.create(url)).headers(headers).build();
        
         try {
        // Perform the exchange and get the response
        ResponseEntity<String> responseResult = restTemplate.exchange(requestEntity, String.class);
        
        // Handle the response
        if (responseResult.getStatusCode() == HttpStatus.NOT_FOUND) { // useless here, because it will throw an exception regardless
            //just to be shown as demonstration
            return "Error: " + responseResult.getBody(); // Response body will contain the error message from the server
        } else {
            // Handle success or other status codes if needed
            return responseResult.getBody();
        }
    }catch (HttpClientErrorException ex) { // this is the one that is caught
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) { // can choose to split httpclienterrorexception further 
            //instead of using catch (HttpClientErrorException.BadRequest ex)
            return "Condition 1 met: " + ex.getStatusCode() + ex.getResponseBodyAsString();
        }
        return "HTTP Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();

    } catch (Exception ex) {
        return "Some other error occurred while processing the request.";
    } 
        

    }

    
    
    public String updateBoardGame(String id) throws Exception{
        // http://localhost:4000/api/boardgame/

        String url = "http://localhost:4000/api/boardgame/" + id;


        JsonObject boardGameJson = Json.createObjectBuilder()
                                    .add("gid",1)
                                    .add("name","testboardgame1")
                                    .add("year", 2024)
                                    .add("ranking",1337)
                                    .add("users_rated",13337)
                                    .add("url","https://www.boardgamegeek.com/boardgame/1/die-macher")
                                    .add("image","https://cf.geekdo-images.com/micro/img/JFMB8ORpxWo-VfPrEePfMluBkGs=/fit-in/64x64/pic159509.jpg\"")
                                    .build();
        String studentJsonString = boardGameJson.toString();

        HttpHeaders headers = new HttpHeaders();
        //this ensures what you are receiving and sending is application/json
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // without this, its text/plain

        RequestEntity<String> requestEntity = RequestEntity.put(url)
                                                           .headers(headers)
                                                           .body(studentJsonString);
        //assuming the postmapping doesnt manually take care of it
        try {
            ResponseEntity<String> responseResult = restTemplate.exchange(requestEntity,String.class);
            return responseResult.getBody();

    
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) { // can choose to split httpclienterrorexception further 
                //instead of using catch (HttpClientErrorException.BadRequest ex)
                return "Condition 1 met: " + ex.getStatusCode() + ex.getResponseBodyAsString();
            } if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) { // can choose to split httpclienterrorexception further 
                //instead of using catch (HttpClientErrorException.BadRequest ex)
                return "Condition 2 met: " + ex.getStatusCode() + ex.getResponseBodyAsString();
            }
            return "Condition 3 met: " + ex.getStatusCode() + ex.getResponseBodyAsString();

        } catch (Exception ex) {
            return "Some other error occurred while processing the request.";
        } 
        

        
    }

    //in the case of void
    public void updateBoardGame2(String id,Boolean upsert) throws Exception{
        // http://localhost:4000/api/boardgame/
        String url = "";
        if (upsert ==false) {
            url = "http://localhost:4000/api/boardgame/" + id;
        } else {
            url = "http://localhost:4000/api/boardgame/" + id + "?upsert=true";
        }
        // String url = "http://localhost:4000/api/boardgame/" + id;
        


        JsonObject boardGameJson = Json.createObjectBuilder()
                                    .add("gid",Integer.parseInt(id))
                                    .add("name","testboardgame1")
                                    .add("year", 2024)
                                    .add("ranking",1337)
                                    .add("users_rated",13337)
                                    .add("url","https://www.boardgamegeek.com/boardgame/1/die-macher")
                                    .add("image","https://cf.geekdo-images.com/micro/img/JFMB8ORpxWo-VfPrEePfMluBkGs=/fit-in/64x64/pic159509.jpg\"")
                                    .build();
        String studentJsonString = boardGameJson.toString();

        HttpHeaders headers = new HttpHeaders();
        //this ensures what you are receiving and sending is application/json
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // without this, its text/plain

        RequestEntity<String> requestEntity = RequestEntity.put(url)
                                                           .headers(headers)
                                                           .body(studentJsonString);
        //assuming the postmapping doesnt manually take care of it
        try {
            ResponseEntity<String> responseResult = restTemplate.exchange(requestEntity,String.class);
            System.out.println(responseResult.getBody());

    
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) { // can choose to split httpclienterrorexception further 
                //instead of using catch (HttpClientErrorException.BadRequest ex)
               System.out.println("first case");
            } if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) { // can choose to split httpclienterrorexception further 
                //instead of using catch (HttpClientErrorException.BadRequest ex)
                throw new Exception("Second case. Bad request. try again with ?upsert=true");
            }
           
        } catch (Exception ex) {
            throw new Exception("Second case. Bad request. try again");
        } 
        

        
    }

    
}
