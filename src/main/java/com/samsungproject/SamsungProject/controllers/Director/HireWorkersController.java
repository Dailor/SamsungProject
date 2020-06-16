package com.samsungproject.SamsungProject.controllers.Director;

import com.samsungproject.SamsungProject.models.*;
import com.samsungproject.SamsungProject.repo.EventNotificationRepository;
import com.samsungproject.SamsungProject.repo.InviteRepository;
import com.samsungproject.SamsungProject.repo.UserRepository;
import com.samsungproject.SamsungProject.repo.WorkerRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/hire_worker")
@PreAuthorize("hasAuthority('DIRECTOR')")
public class HireWorkersController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private EventNotificationRepository eventNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getPage(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();

        if(directorNow.getCompanies().size() == 0){
            redirectAttributes.addFlashAttribute("danger_msg", "Чтобы нанять работника нужно создать компанию");
            return "redirect:/company/create_company";
        }

        Company companyNow = directorNow.getCompanies().iterator().next();

        if (userNow.getDirector().getCompanies().size() == 0) {
            redirectAttributes.addFlashAttribute("danger_msg", "Чтобы нанять работника, сначало создайте компанию");
            return "redirect:/company";
        }
//        Set<Worker> workers = workerRepository.findWorkersByCompanyIsNull();
//        model.addAttribute("workers", workers);
        Set<User> users = userRepository.findAllByWorkerIsNullAndInviteCompany(companyNow);
        model.addAttribute("users", users);
        return "hire_worker";
    }

    @GetMapping("/add_user")
    public String HireWorker(@RequestParam("id") Worker worker, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();

        Company CompanyDirectorNow = directorNow.getCompanies().iterator().next();
        Company CompanyWorker = worker.getCompany();

        Invite inviteFromDirectorNow = inviteRepository.findByWorkerAndCompanyOrderByIdDesc(worker, CompanyDirectorNow);


        if (CompanyWorker == CompanyDirectorNow) {
            redirectAttributes.addFlashAttribute("danger_msg", "Он работает на вас");
        } else if (CompanyWorker != null) {
            redirectAttributes.addFlashAttribute("danger_msg", "Этот работник уже в компании");
        } else if (inviteFromDirectorNow != null && inviteFromDirectorNow.getStatus() != "SUCCESS") {
            redirectAttributes.addFlashAttribute("danger_msg", "Вы его уже приглашали");
        } else {
            redirectAttributes.addFlashAttribute("success_msg", "Приглашение отправлено");
            Invite invite = new Invite();
            invite.setCompany(CompanyDirectorNow);
            invite.setStatus("WAITING");
            invite.setWorker(worker);
            inviteRepository.save(invite);

            String msg = "Вам пришло приглашение в компанию от " + userNow.getFullName();

            EventNotification eventNotification = new EventNotification(worker.getUser(), msg);
            eventNotificationRepository.save(eventNotification);
        }

        return "redirect:/hire_worker";
    }
}
