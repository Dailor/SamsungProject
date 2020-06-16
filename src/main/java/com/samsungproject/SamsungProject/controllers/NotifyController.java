package com.samsungproject.SamsungProject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samsungproject.SamsungProject.models.EventNotification;
import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.repo.EventNotificationRepository;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/notify")
@PreAuthorize("hasAnyAuthority('DIRECTOR', 'WORKER')")
public class NotifyController {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    EventNotificationRepository eventNotificationRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/getCountUnread")
    @ResponseBody
    public HashMap<String, Integer> getCountUnread() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();
        HashMap<String, Integer> result = new HashMap<>();
        result.put("count", eventNotificationRepository.countEventNotificationByUserAndStatusIsFalse(userNow));
        return result;
    }

    @GetMapping
    public String getNotifyPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = (User) authentication.getPrincipal();

        Set<EventNotification> eventNotifications = eventNotificationRepository.findAllByUserAndStatusIsFalse(userNow);
        for (EventNotification event : eventNotifications
        ) {
            event.setStatus(true);
        }
        userRepository.save(userNow);

        model.addAttribute("notifications", eventNotifications);
        return "notify_page.html";
    }

}
