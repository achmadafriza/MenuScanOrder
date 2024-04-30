package net.uqcloud.infs7202.project.auth.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.uqcloud.infs7202.project.auth.controller.dto.CreateUserDTO;
import net.uqcloud.infs7202.project.auth.controller.dto.EditUserDTO;
import net.uqcloud.infs7202.project.auth.repository.RoleRepository;
import net.uqcloud.infs7202.project.auth.repository.UserRepository;
import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import net.uqcloud.infs7202.project.auth.repository.model.Role;
import net.uqcloud.infs7202.project.auth.service.UserService;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Log4j2
public class UserController {
    private @NonNull UserRepository userRepository;
    private @NonNull RoleRepository roleRepository;
    private @NonNull RestaurantRepository restaurantRepository;

    private @NonNull UserService service;

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADD_USER')")
    public String addUserForm(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        List<Restaurant> restaurants = restaurantRepository.findAll();
        model.addAttribute("restaurants", restaurants);

        model.addAttribute("userDto", new CreateUserDTO());
        return "add-user";
    }

    @PostMapping(value = "/add", consumes = { "multipart/form-data" })
    @PreAuthorize("hasAuthority('ADD_USER')")
    public String addUser(@Valid @ModelAttribute("userDto") CreateUserDTO userDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "add-user";
        }

        service.createUser(userDto);

        redirectAttributes.addFlashAttribute("message", "User has been created!");
        return "redirect:/admin";
    }


    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    public String editUserForm(@PathVariable("id") int id,
                               Model model) {
        AuthUser user = userRepository.findByIdAndIsActiveTrue(id);
        model.addAttribute("user", user);

        EditUserDTO userDto = new EditUserDTO();
        userDto.setEmail(user.getEmail());

        model.addAttribute("userDto", userDto);
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    public String editUser(@PathVariable("id") int id,
                           @ModelAttribute("user") AuthUser user,
                           @Valid @ModelAttribute("userDto") EditUserDTO userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "edit-user";
        }

        service.editUser(id, userDto);

        redirectAttributes.addFlashAttribute("message", "User has been updated!");
        return "redirect:/admin";
    }

    @PostMapping("/deactivate")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String deactivateUser(@RequestParam("id") int id,
                                 RedirectAttributes redirectAttributes) {
        service.updateUserActive(id, false);

        redirectAttributes.addFlashAttribute("message", "User has been deactivated!");
        return "redirect:/admin";
    }

    @PostMapping("/activate")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String activateUser(@RequestParam("id") int id,
                               RedirectAttributes redirectAttributes) {
        service.updateUserActive(id, true);

        redirectAttributes.addFlashAttribute("message", "User has been activated!");
        return "redirect:/admin/archived";
    }
}
