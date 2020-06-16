package com.samsungproject.SamsungProject.controllers.Worker;

import com.samsungproject.SamsungProject.models.*;
import com.samsungproject.SamsungProject.repo.EventNotificationRepository;
import com.samsungproject.SamsungProject.repo.InviteRepository;
import com.samsungproject.SamsungProject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/invites")
@PreAuthorize("hasAuthority('WORKER')")
public class WorkerInvitesController {
    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventNotificationRepository eventNotificationRepository;

    @GetMapping
    public String getPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Worker workerNow = userNow.getWorker();
        Set<User> users = userRepository.findUserByWorkerAndInviteStatus(workerNow, "WAITING");
        model.addAttribute("users", users);
        return "worker_invites";
    }

    @GetMapping("/answer_invite")
    public String getPage(@RequestParam("answer") String answer, @RequestParam("user_id") User user,
                          RedirectAttributes model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User userNow = (User) authentication.getPrincipal();
        Worker workerNow = userNow.getWorker();

        Director director = user.getDirector();
        Company company = director.getCompanies().iterator().next();

        Invite invite = inviteRepository.findByWorkerAndCompanyOrderByIdDesc(workerNow, company);

        if (invite == null || answer == null) {
            model.addFlashAttribute("danger_msg", "Ошибка отправки ответа");
        } else if (workerNow.getCompany() != null) {
            model.addFlashAttribute("danger_msg", "Вы уже находитесь в компании");
        } else if (!invite.getStatus().equals("WAITING")) {
            model.addFlashAttribute("danger_msg", "Вы уже давали ответ");
        } else {
            invite.setStatus(answer);
            if (answer.equals("ACCEPT") || answer.equals("DENIED")) {
                EventNotification eventNotification = new EventNotification();
                eventNotification.setUser(user);

                if (answer.equals("ACCEPT")) {
                    workerNow.setCompany(company);
                    eventNotification.setMessage(userNow.getFullName() + " принял ваше приглашение в компанию");
                    model.addFlashAttribute("success_msg", "Теперь вы работаете в компании " + director.getCompanies().iterator().next().getName());

                } else {
                    eventNotification.setMessage(userNow.getFullName() + " отклонил ваше приглашение в компанию");
                    model.addFlashAttribute("success_msg", "Вы откланили предложение от " + user.getFullName());
                }

                invite.setStatus(answer);

                inviteRepository.save(invite);
                eventNotificationRepository.save(eventNotification);
                userRepository.save(userNow);

            } else {
                model.addAttribute("danger_msg", "Ошибка отправки ответа");
            }
        }
        return "redirect:/invites";
    }
}
