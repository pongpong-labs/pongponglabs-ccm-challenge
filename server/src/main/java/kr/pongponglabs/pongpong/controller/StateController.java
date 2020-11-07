package kr.pongponglabs.pongpong.controller;

import kr.pongponglabs.pongpong.message.Response;
import kr.pongponglabs.pongpong.sql.User;
import kr.pongponglabs.pongpong.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Controller
public class StateController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/state")
    @ResponseBody
    public Response<Serializable> uploadFile(@RequestParam("fileName") String fileName) {
        List<User> users = userRepository.findByFileName(fileName);

        for(var user : users) {
            return new Response<>(true, user.getState(), user.getResult());
        }

        return new Response<>(false, "Ready", "");
    }
}
