package com.samsungproject.SamsungProject.controllers.Director;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.Director;
import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.repo.CompanyRepository;
import com.samsungproject.SamsungProject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/company_works")
@PreAuthorize("hasAuthority('DIRECTOR')")
public class CompanyWorksController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/add_work")
    public String AddWork(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();
        model.addAttribute("workers", companyNow.getWorkers());
        return "add_work";
    }

}
