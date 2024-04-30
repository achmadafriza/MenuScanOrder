package net.uqcloud.infs7202.project.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.auth.repository.UserRepository;
import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private @NonNull UserRepository userRepository;

    @GetMapping("")
    @PreAuthorize("hasAuthority('MANAGE_SUBSCRIPTION')")
    public String adminPage(HttpServletRequest request,
                            Model model,
                            @RequestParam(required = false, defaultValue = "1") int page,
                            @RequestParam(required = false, defaultValue = "25") int size,
                            @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("currentUser", user);

        if (page < 1) {
            return String.format("redirect:/admin?page=1&size=%d", size);
        }

        Pageable pageable = PageRequest.of(page-1, size, Sort.by("email").ascending());
        Page<AuthUser> users = userRepository.findAllByIsActiveTrue(pageable);

        if (users.getTotalPages() != 0 && page > users.getTotalPages()) {
            return String.format("redirect:/admin?page=%d&size=%d", users.getTotalPages(), size);
        }

        model.addAttribute("users", users);

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String message = null;
        if (inputFlashMap != null) {
            message = (String) inputFlashMap.getOrDefault("message", null);
        }

        model.addAttribute("message", message);

        return "admin";
    }

    @GetMapping("/archived")
    @PreAuthorize("hasAuthority('MANAGE_SUBSCRIPTION')")
    public String archivedPage(HttpServletRequest request,
                            Model model,
                            @RequestParam(required = false, defaultValue = "1") int page,
                            @RequestParam(required = false, defaultValue = "25") int size,
                            @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("currentUser", user);

        if (page < 1) {
            return String.format("redirect:/archived?page=1&size=%d", size);
        }

        Pageable pageable = PageRequest.of(page-1, size, Sort.by("email").ascending());
        Page<AuthUser> users = userRepository.findAllByIsActiveFalse(pageable);

        if (page > users.getTotalPages()) {
            return String.format("redirect:/archived?page=%d&size=%d", users.getTotalPages(), size);
        }

        model.addAttribute("users", users);

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String message = null;
        if (inputFlashMap != null) {
            message = (String) inputFlashMap.getOrDefault("message", null);
        }

        model.addAttribute("message", message);

        return "admin-archived";
    }
}
