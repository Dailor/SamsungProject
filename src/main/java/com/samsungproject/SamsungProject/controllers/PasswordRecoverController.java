package com.samsungproject.SamsungProject.controllers;

import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.repo.UserRepository;
import com.samsungproject.SamsungProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/forgot_password")
public class PasswordRecoverController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;


    @GetMapping
    public String getPage(Model model) {
        return "forgot_password";
    }

    @GetMapping("/get_question")
    @ResponseBody
    public HashMap<String, String> getQuestionByLogin(@RequestParam("username") String username, HttpServletResponse response) {
        HashMap<String, String> result = new HashMap<>();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            result.put("error", "Пользователя с таким логином не существует");
        } else {
            result.put("question", user.getSecret_question());
        }
        return result;
    }

    @GetMapping("/check_question")
    @ResponseBody
    public HashMap<String, String> checkQuestionByLogin(@RequestParam("username") String username, @RequestParam("answer") String answer) {
        HashMap<String, String> result = new HashMap<>();

        User user = userRepository.findByUsername(username);
        if (user.checkSecretQuestion(answer)) {
            result.put("old_password_hash", user.getPassword());
            result.put("username", user.getUsername());
        } else {
            result.put("error", "Не правильный ответ");
        }

        return result;
    }

    @PostMapping
    public String setNewPassword(
            @RequestParam("old-hashed-password") String old_hashed_password,
            @RequestParam("password") String newPassword,
            @RequestParam("username") String username,
            @RequestParam("password-repeat") String password_repeat,
            RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(username);


        if (user != null) {
            if (user.getPassword().equals(old_hashed_password)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                try {
                    for (Object principal : sessionRegistry.getAllPrincipals()) {
                        User userCheck = (User) principal;
                        if (user.getId() == user.getId()) {
                            for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
                                sessionRegistry.getSessionInformation(session.getSessionId()).expireNow();
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                }
                redirectAttributes.addFlashAttribute("success_msg", "Вы успешно сменили пароль");
            }
        }

        return "redirect:/login";
    }
}
