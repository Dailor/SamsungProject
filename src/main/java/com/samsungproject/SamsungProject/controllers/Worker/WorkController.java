package com.samsungproject.SamsungProject.controllers.Worker;

import com.samsungproject.SamsungProject.models.*;
import com.samsungproject.SamsungProject.repo.EventNotificationRepository;
import com.samsungproject.SamsungProject.repo.PhotoRepository;
import com.samsungproject.SamsungProject.repo.WorkRepository;
import com.samsungproject.SamsungProject.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.Set;

@RequestMapping("/works")
@Controller
@PreAuthorize("hasAuthority('WORKER')")
public class WorkController {
    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    WorkRepository workRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    EventNotificationRepository eventNotificationRepository;

    @GetMapping
    public String getPage(Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Long workerNowID = userNow.getWorker().getId();
        Worker workerNow = workerRepository.findById(workerNowID).orElseThrow(() -> new Exception(workerNowID + ""));
        Company companyNow = workerNow.getCompany();

        Set<Work> works = workRepository.findAllByWorkerAndCompanyAndStatus(workerNow, companyNow, "Выполняется");

        model.addAttribute("works", works);
        return "company";
    }

    @GetMapping("/send_result")
    public String getPageSendResult(@RequestParam("id") Work work, Model model, RedirectAttributes redirectAttributes) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Long workerNowID = userNow.getWorker().getId();
        Worker workerNow = workerRepository.findById(workerNowID).orElseThrow(() -> new Exception(workerNowID + ""));
        Company companyNow = workerNow.getCompany();

        if (work.getWorker().getId() != workerNowID) {
            redirectAttributes.addFlashAttribute("danger_msg", "Произошла ошибка");
            return "redirect:/works";
        }
        model.addAttribute("work", work);

        return "send_result";
    }

    @PostMapping("/send_result")
    public String handleSendResult(@ModelAttribute Work work, @RequestParam("id") Work workNeedChange, @RequestParam("photo") MultipartFile[] photos, RedirectAttributes redirectAttributes) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Long workerNowID = userNow.getWorker().getId();
        Worker workerNow = workerRepository.findById(workerNowID).orElseThrow(() -> new Exception(workerNowID + ""));

        if (workerNow.getId() != workNeedChange.getWorker().getId()) {
            redirectAttributes.addFlashAttribute("danger_msg", "Исполнителя на это работу поменяли");
        }

        for(MultipartFile el : photos){
            Photo photo = new Photo();
            photo.setCompany(workNeedChange.getCompany());
            photo.setFileimage(el.getBytes());
            photo.setWork(workNeedChange);
            photoRepository.save(photo);
        }

        workNeedChange.setResult(work.getResult());
        workNeedChange.setStatus(work.getStatus());
        workNeedChange.setDate_end(java.time.Instant.now().getEpochSecond());

        Director director = workNeedChange.getCompany().getDirector();

        EventNotification eventNotification = new EventNotification();
        eventNotification.setUser(director.getUser());
        eventNotification.setMessage("Пришёл результат по работе " + workNeedChange.getName());

        eventNotificationRepository.save(eventNotification);

        redirectAttributes.addFlashAttribute("success_msg", "Работа отправлена");
        return "redirect:/works";
    }

}
