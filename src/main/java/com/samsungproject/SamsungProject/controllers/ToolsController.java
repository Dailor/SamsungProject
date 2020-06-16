package com.samsungproject.SamsungProject.controllers;

import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequestMapping("/tools")
@Controller
public class ToolsController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/check_exists")
    @ResponseBody
    public HashMap<String, Boolean> checkExists(@RequestParam("username") String username) {
        HashMap<String, Boolean> result = new HashMap<>();

        User user = userRepository.findByUsername(username);
        if (user != null) {
            result.put("result", true);
        } else {
            result.put("result", false);
        }
        return result;
    }
}