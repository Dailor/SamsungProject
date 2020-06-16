package com.samsungproject.SamsungProject.controllers;

import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.security.Principal;

@Controller
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/settings")
    public String GetSettingPage(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user_model", user);
        return "settings";
    }

    @PostMapping("/settings")
    public String UpdateUserData(Model model, User user, @RequestParam("photo") MultipartFile filePhoto) throws Exception {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        if (userFromDB != null && !userNow.getUsername().equals(user.getUsername())) {
            model.addAttribute("user_model", user);
            model.addAttribute("errorUsername", "Такой логин уже существует");
            return "settings";
        }

        userNow.setName(user.getName());
        userNow.setSurname(user.getSurname());
        userNow.setUsername(user.getUsername());
        userNow.setSecret_question(user.getSecret_question());
        userNow.setAnswer_question(user.getAnswer_question());
        userNow.setAboutMe(user.getAboutMe());

        if (filePhoto.getBytes().length != 0) {
            userNow.setImage(filePhoto.getBytes());
        }

        userRepository.save(userNow);

        return "redirect:/";
    }

    @GetMapping("photo")
    public void showImage(@RequestParam("id") User user, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("image/*");
        if (user.getImage() != null && user.getImage().length != 0) {
            response.getOutputStream().write(user.getImage());
        } else {
            InputStream NoPhotoImg = getClass().getResourceAsStream("/static/img/nophoto.jpg");
            response.getOutputStream().write(NoPhotoImg.readAllBytes());
        }

        response.getOutputStream().close();
    }

}
