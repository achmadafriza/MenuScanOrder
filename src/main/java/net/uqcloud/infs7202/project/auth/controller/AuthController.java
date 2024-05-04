package net.uqcloud.infs7202.project.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.uqcloud.infs7202.project.auth.controller.dto.SignupUserDTO;
import net.uqcloud.infs7202.project.auth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AuthController {
    private @NonNull UserService userService;

    @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String login(HttpServletRequest request,
                        Model model) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String message = null;
        if (inputFlashMap != null) {
            message = (String) inputFlashMap.getOrDefault("message", null);
        }

        model.addAttribute("message", message);

        return "login";
    }

    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else if (request.isUserInRole("ROLE_OWNER")) {
            return "redirect:/owner/menu";
        } else if (request.isUserInRole("ROLE_STAFF")) {
            return "redirect:/owner/order";
        }

        throw new UnsupportedOperationException("Unknown Role for the User");
    }

    @GetMapping("/signup")
    @PreAuthorize("permitAll()")
    public ModelAndView signup() {
        return new ModelAndView("signup", "userDto", new SignupUserDTO());
    }

    @PostMapping(
            value = "/signup",
            consumes = { "multipart/form-data" }
    )
    @PreAuthorize("permitAll()")
    public String handleSignup(@Valid @ModelAttribute("userDto") SignupUserDTO userDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        userService.signup(userDto);

        redirectAttributes.addFlashAttribute("message", "User has been created!");
        return "redirect:/login";
    }
}
