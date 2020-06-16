package com.samsungproject.SamsungProject.controllers.Director;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.Director;
import com.samsungproject.SamsungProject.models.Photo;
import com.samsungproject.SamsungProject.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
@RequestMapping("/works/photo_get")
@PreAuthorize("hasAuthority('DIRECTOR')")
public class WorkPhotoController {

    @GetMapping()
    public void showImage(@RequestParam("id") Photo photo, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();

        if(photo.getCompany().getId() != companyNow.getId()){
            return;
        }

        response.setContentType("image/*");

        response.getOutputStream().write(photo.getFileimage());

        response.getOutputStream().close();
    }
}
