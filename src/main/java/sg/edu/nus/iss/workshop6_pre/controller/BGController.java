package sg.edu.nus.iss.workshop6_pre.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sg.edu.nus.iss.workshop6_pre.service.BoardGameRestService;


@Controller

public class BGController {

    @Autowired
    BoardGameRestService boardGameRestService;

    @GetMapping("delete/{boardgameid}")
    @ResponseBody
    public String deleteBG(@PathVariable("boardgameid") String id) throws Exception {
        
        String responseString = boardGameRestService.deleteBoardGame(id);

        return responseString;
    }

    @GetMapping("update/{boardgameid}")
    @ResponseBody
    public String updateBG(@PathVariable("boardgameid") String id,
    @RequestParam(name = "upsert",defaultValue="false",required=false) String upsert) throws Exception {
        
        String responseString = boardGameRestService.updateBoardGame(id);

        return responseString;
    }

    @GetMapping("update2/{boardgameid}")
    public String updateBG2(@PathVariable("boardgameid") String id,
    @RequestParam(name = "upsert",defaultValue="false",required=false) Boolean upsert,Model model) throws Exception {
        System.out.println(upsert);
        try {
            boardGameRestService.updateBoardGame2(id,upsert);
            model.addAttribute("updated",true);
        } catch (Exception ex) {
            model.addAttribute("errorMessage",ex.getMessage());
        }
        

        return "update2";
    }
    
    
}
