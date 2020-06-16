package com.samsungproject.SamsungProject.controllers;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.Director;
import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.models.Worker;
import com.samsungproject.SamsungProject.repo.DirectorRepository;
import com.samsungproject.SamsungProject.repo.UserRepository;
import com.samsungproject.SamsungProject.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sign_up")
public class SignUpController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("user_model", new User());
        return "SignUp";
    }

    @PostMapping
    public String addUser(@ModelAttribute User user, Model model, @RequestParam("photo") MultipartFile filePhoto) throws Exception {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            model.addAttribute("user_model", user);
            model.addAttribute("errorUsername", "Такой логин уже существует");
            return "SignUp";
        }
        if (this.CheckUserFields(user) == false) {
            model.addAttribute("aboutMe","Произошла какая-то ошибка");
            return "SignUp";
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String role = user.getRoles().iterator().next().name();
        if (filePhoto.getBytes().length != 0) {
            user.setImage(filePhoto.getBytes());
        }


        userRepository.save(user);

        if (role.equals("DIRECTOR")) {
            Director director = new Director();

            director.setUser(user);

            directorRepository.save(director);
        } else if (role.equals("WORKER")) {
            Worker worker = new Worker();

            worker.setUser(user);

            workerRepository.save(worker);
        }

        return "redirect:/login";
    }

    private boolean CheckUserFields(User user) {
        if (user.getName().length() == 0) {
            return false;
        }
        if (user.getSurname().length() == 0) {
            return false;
        }
        if (user.getUsername().length() == 0) {
            return false;
        }
        if (user.getPassword().length() == 0) {
            return false;
        }
        if (user.getAnswer_question().length() == 0) {
            return false;
        }
        if (user.getAnswer_question().length() == 0) {
            return false;
        }
        Integer ans_s_len = user.getAnswer_question().length();
        if (ans_s_len == 0 || ans_s_len > 500) {
            return false;
        }
        return true;
    }
}
