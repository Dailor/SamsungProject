package com.samsungproject.SamsungProject.controllers.Director;

import com.samsungproject.SamsungProject.models.*;
import com.samsungproject.SamsungProject.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/company")
@PreAuthorize("hasAuthority('DIRECTOR')")
public class CompanyController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private EventNotificationRepository eventNotificationRepository;

    @Autowired
    private WorkRepository workRepository;

    @GetMapping
    public String getPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();

        if (directorNow.getCompanies().size() == 0) {
            return "redirect:/company/create_company";
        }

        String companyNowName = directorNow.getCompanies().iterator().next().getName();
        Company companyNow = companyRepository.findByName(companyNowName);
        Set<Work> works = workRepository.findAllByCompany(companyNow, Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("works", works);


        return "company";
    }

    @GetMapping("/create_company")
    public String CreateCompanyGetPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();

        if (userNow.getDirector().getCompanies().size() != 0) {
            return "redirect:/company";
        }
        model.addAttribute("company_model", new Company());
        return "create_company";
    }

    @PostMapping("/create_company")
    public String CreateCompanyHandlePost(Model model, Company company) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyFromDB = companyRepository.findByName(company.getName());

        if (companyFromDB != null) {
            model.addAttribute("company_model", new Company());
            model.addAttribute("errorName", "Компания с таким именем уже существует");
            return "create_company";
        }
        directorNow.addCompany(company);
        companyRepository.save(company);

        return "redirect:/company";
    }

    @GetMapping("/workers")
    public String getWorkers(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();

        if(directorNow.getCompanies().size() == 0){
            redirectAttributes.addFlashAttribute("danger_msg", "Сначало создайте компанию");
            return "redirect:/company/create_company";
        }

        Company companyNow = directorNow.getCompanies().iterator().next();
        Set<User> workersToUser = userRepository.findUsersByCompany(companyNow);

        if (workersToUser.size() == 0) {
            redirectAttributes.addFlashAttribute("danger_msg", "У вас еще нет работников, чтобы взаимодействовать с ними, сначало наймите их");
            return "redirect:/hire_worker";
        }

        model.addAttribute("users", workersToUser);

        return "workers";
    }

    @GetMapping("/dismiss_worker")
    public String dismissWorker(RedirectAttributes redirectAttributes, @RequestParam("worker_id") Worker worker) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();
        Set<Work> unFinishedWork = workRepository.findAllByWorkerAndCompanyAndStatus(worker, companyNow, "Выполняется");

        if (worker == null || worker.getCompany().getId() != companyNow.getId()) {
            redirectAttributes.addFlashAttribute("danger_msg", "Ошибка во время удаления");
        } else if (unFinishedWork.size() != 0) {
            redirectAttributes.addFlashAttribute("danger_msg", "У " + worker.getUser().getFullName() + " есть не завершенные работы в вашей компании");
        } else {
            companyNow.getWorkers().remove(worker);

            worker.setCompany(null);
            workerRepository.save(worker);

            EventNotification eventNotification = new EventNotification(worker.getUser(), "Вы были уволены из компании " + companyNow.getName());
            eventNotificationRepository.save(eventNotification);

            redirectAttributes.addFlashAttribute("success_msg", "Вы уволили " + worker.getUser().getFullName());
        }
        return "redirect:/company/workers";
    }

    @GetMapping("/add_work")
    public String addWorkPage(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();

        Set<Worker> workers = companyRepository.findByName(companyNow.getName()).getWorkers();
        if (workers.size() == 0) {
            redirectAttributes.addFlashAttribute("danger_msg", "Чтобы добавить работу сначала наймите работника");
            return "redirect:/hire_worker";
        }

        model.addAttribute("workers", workers);

        model.addAttribute("work", new Work());

        return "add_work";
    }

    @PostMapping("/add_work")
    public String addWorkHandle(RedirectAttributes redirectAttributes, @ModelAttribute Work work) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();


        work.setDate_given(java.time.Instant.now().getEpochSecond());
        work.setCompany(companyNow);
        work.setStatus("Выполняется");
        work.setAddress(work.getAddress());

        if (work.getAddress().replace(" ", "").length() == 0) {
            work.setAddress("Не указан");
        }

        Worker worker = work.getWorker();


        if (worker.getCompany() == null || (worker.getCompany().getId() != companyNow.getId())) {
            redirectAttributes.addFlashAttribute("danger_msg", "Этот работник больше на вас не работет");
            return "redirect:/company/add_work";
        }

        workRepository.save(work);

        EventNotification eventNotification = new EventNotification(worker.getUser(), "Вам была добавлена работа");
        eventNotificationRepository.save(eventNotification);

        return "redirect:/company";
    }

    @GetMapping("/delete")
    @Transactional
    public String deleteWork(@RequestParam("work_id") Work work, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        String companyNowName = directorNow.getCompanies().iterator().next().getName();
        Company companyNow = companyRepository.findByName(companyNowName);

        if (work.getCompany().getId() != companyNow.getId()) {
            redirectAttributes.addFlashAttribute("danger_msg", "Произошла ошибка во время удаления");
        } else {

            workRepository.deleteByIdAndCompany(work.getId(), companyNow);

            EventNotification eventNotification = new EventNotification(work.getWorker().getUser(), "Работа " + work.getName() + " была удалена");
            eventNotificationRepository.save(eventNotification);

            redirectAttributes.addFlashAttribute("success_msg", "Работа успешно удалена");
        }

        return "redirect:/company";
    }

    @GetMapping("/change")
    public String changeWorkGet(@RequestParam("work_id") Work work, RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();

        if (companyNow.getId() != work.getCompany().getId() || !work.getStatus().equals("Выполняется")) {
            redirectAttributes.addFlashAttribute("danger_msg", "Произошла ошибка");
            return "redirect:/company";
        }

        Set<Worker> workers = companyRepository.findByName(companyNow.getName()).getWorkers();

        model.addAttribute("workers", workers);
        model.addAttribute("work", work);


        return "add_work";
    }

    @PostMapping("/change")
    public String changeWorkHandle(@ModelAttribute Work work, RedirectAttributes redirectAttributes, @RequestParam("work_id") Work workNeedChange) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        Director directorNow = userNow.getDirector();
        Company companyNow = directorNow.getCompanies().iterator().next();

        if (workNeedChange.getCompany().getId() != companyNow.getId() || work.getWorker().getCompany().getId() != companyNow.getId()) {
            redirectAttributes.addFlashAttribute("danger_msg", "Произошла ошибка");
            return "redirect:/company";
        }

        EventNotification eventNotificationOldWorker = new EventNotification();

        if (workNeedChange.getWorker().getId() != work.getWorker().getId()) {
            EventNotification eventNotificationNewWorker = new EventNotification(work.getWorker().getUser(), "Вам была добавлена работа");

            eventNotificationOldWorker.setUser(workNeedChange.getWorker().getUser());
            eventNotificationOldWorker.setMessage("Работу " + workNeedChange.getName() + " больше исполняете не вы");

            workNeedChange.setWorker(work.getWorker());
            eventNotificationRepository.save(eventNotificationNewWorker);

        } else {
            eventNotificationOldWorker.setUser(workNeedChange.getWorker().getUser());
            eventNotificationOldWorker.setMessage("В работе (старое название: " + workNeedChange.getName() + ", сейчас: " + work.getName() +  ") что-то поменялось");
        }
        eventNotificationRepository.save(eventNotificationOldWorker);

        workNeedChange.setName(work.getName());
        workNeedChange.setDescription(work.getDescription());
        workNeedChange.setAddress(work.getAddress());
        workNeedChange.setPrice(work.getPrice());


        workRepository.save(work);
        return "redirect:/company";
    }

    @GetMapping("/result")
    public String getPageResult(@RequestParam("work_id") Work work, Model model){
        model.addAttribute("work", work);
        return "result_show";
    }
}
