package net.uqcloud.infs7202.project.restaurant.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.auth.repository.MenuRepository;
import net.uqcloud.infs7202.project.auth.repository.UserRepository;
import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import net.uqcloud.infs7202.project.auth.repository.model.Menu;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.repository.MenuItemRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.MenuItem;
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
@RequestMapping("/owner")
public class OwnerController {
    private @NonNull UserRepository userRepository;

    private @NonNull MenuItemRepository menuRepository;

    @GetMapping("/menu")
    @PreAuthorize("hasAuthority('MANAGE_MENU')")
    public String manageMenu(HttpServletRequest request,
                             Model model,
                             @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "25") int size,
                             @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("currentUser", user);

        if (page < 1) {
            return String.format("redirect:/owner/menu?page=1&size=%d", size);
        }

        AuthUser authUser = userRepository.findByEmailAndIsActiveTrue(user.getUsername());
        if (authUser.getRestaurant() == null) {
            throw new DataNotFoundException("User doesn't have a restaurant!");
        }

        Pageable pageable = PageRequest.of(
                page-1, size,
                Sort.by("category.name").ascending()
                        .and(Sort.by("name").ascending())
        );
        Page<MenuItem> menus = menuRepository.findAllByRestaurant(authUser.getRestaurant(), pageable);

        if (menus.getTotalPages() != 0 && page > menus.getTotalPages()) {
            return String.format("redirect:/owner/menu?page=%d&size=%d", menus.getTotalPages(), size);
        }

        model.addAttribute("menus", menus);
        model.addAttribute("restaurant", authUser.getRestaurant());

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String message = null;
        if (inputFlashMap != null) {
            message = (String) inputFlashMap.getOrDefault("message", null);
        }

        model.addAttribute("message", message);

        return "manage-menu";
    }

    /* TODO: update page */
    @GetMapping("/table")
    @PreAuthorize("hasAuthority('MANAGE_TABLE')")
    public String manageTable() {
        return "manage-table";
    }

    /* TODO: update page */
    @GetMapping("/order")
    @PreAuthorize("hasAuthority('MANAGE_ORDER')")
    public String manageOrder() {
        return "manage-order";
    }
}
