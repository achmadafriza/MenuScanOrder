package net.uqcloud.infs7202.project.restaurant.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.MenuDTO;
import net.uqcloud.infs7202.project.restaurant.repository.CategoryRepository;
import net.uqcloud.infs7202.project.restaurant.repository.MenuItemRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Category;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import net.uqcloud.infs7202.project.restaurant.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant/{restaurantId}/menu")
public class MenuController {
    private @NonNull RestaurantRepository restaurantRepository;
    private @NonNull CategoryRepository categoryRepository;

    private @NonNull MenuService menuService;

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADD_MENU')")
    public String addMenuForm(@PathVariable("restaurantId") int restaurantId,
                              Model model) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));
        model.addAttribute("restaurant", restaurant);

        List<Category> categories = categoryRepository.findAllByRestaurant(restaurant);
        model.addAttribute("categories", categories);

        model.addAttribute("menuDto", new MenuDTO());
        return "create-menu";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADD_MENU')")
    public String addMenu(@PathVariable("restaurantId") int restaurantId,
                          @ModelAttribute("restaurant") Restaurant restaurant,
                          @Valid @ModelAttribute("menuDto") MenuDTO menuDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "create-menu";
        }

        menuService.createMenu(restaurantId, menuDto);

        redirectAttributes.addFlashAttribute("message", "Menu has been created!");
        return "redirect:/owner/menu";
    }

    /* TODO: update page */
    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable("restaurantId") int restaurantId) {
        return "create-menu";
    }

    /* TODO: update page */
    @PostMapping("/edit/{id}")
    public String editMenu(@PathVariable("restaurantId") int restaurantId) {
        return "create-menu";
    }

    /* TODO: update page */
    @PostMapping("/delete")
    public String deleteMenu(@PathVariable("restaurantId") int restaurantId) {
        return "create-menu";
    }
}
