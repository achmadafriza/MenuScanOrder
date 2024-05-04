package net.uqcloud.infs7202.project.restaurant.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.auth.repository.MenuRepository;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.MenuDTO;
import net.uqcloud.infs7202.project.restaurant.repository.CategoryRepository;
import net.uqcloud.infs7202.project.restaurant.repository.MenuItemRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Category;
import net.uqcloud.infs7202.project.restaurant.repository.model.MenuItem;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import net.uqcloud.infs7202.project.restaurant.service.MenuService;
import net.uqcloud.infs7202.project.service.CurrencyParser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant/{restaurantId}/menu")
public class MenuController {
    private @NonNull RestaurantRepository restaurantRepository;
    private @NonNull CategoryRepository categoryRepository;
    private @NonNull MenuItemRepository menuRepository;

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
                          @ModelAttribute("categories") List<Category> categories,
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

    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable("restaurantId") int restaurantId,
                               @PathVariable("id") int menuId,
                               Model model) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));
        model.addAttribute("restaurant", restaurant);

        List<Category> categories = categoryRepository.findAllByRestaurant(restaurant);
        model.addAttribute("categories", categories);

        MenuItem menu = menuRepository.findById(menuId)
                        .orElseThrow(() -> new DataNotFoundException("Menu not found!"));

        MenuDTO menuDto = new MenuDTO();
        menuDto.setCategoryId(menu.getCategory().getId());
        menuDto.setName(menu.getName());
        menuDto.setDescription(menu.getDescription());
        menuDto.setPrice(CurrencyParser.toCurrency(menu.getPrice(), Locale.US));
        menuDto.setMenuPictureUrl(menu.getMenuPic());

        if (menu.getMenuPic() != null) {
            String menuUrl = menu.getMenuPic();

            String menuPictureName = menuUrl.substring(menuUrl.lastIndexOf('/') + 1);
            menuDto.setMenuPictureName(menuPictureName);
        }

        model.addAttribute("menuDto", menuDto);

        return "edit-menu";
    }

    @PostMapping("/edit/{id}")
    public String editMenu(@PathVariable("restaurantId") int restaurantId,
                           @PathVariable("id") int menuId,
                           @ModelAttribute("restaurant") Restaurant restaurant,
                           @ModelAttribute("categories") ArrayList<Category> categories,
                           @Valid @ModelAttribute("menuDto") MenuDTO menuDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (!menuRepository.existsByRestaurantAndId(restaurantId, menuId)) {
            throw new DataNotFoundException("Menu not found!");
        }

        if (bindingResult.hasErrors()) {
            return "edit-menu";
        }

        menuService.editMenu(menuId, menuDto);

        redirectAttributes.addFlashAttribute("message", "Menu has been updated!");
        return "redirect:/owner/menu";
    }

    @PostMapping("/delete")
    public String deleteMenu(@PathVariable("restaurantId") int restaurantId,
                             @RequestParam("id") int menuId,
                             RedirectAttributes redirectAttributes) {
        if (!menuRepository.existsByRestaurantAndId(restaurantId, menuId)) {
            throw new DataNotFoundException("Menu not found!");
        }

        menuRepository.deleteById(menuId);

        redirectAttributes.addFlashAttribute("message", "Menu has been updated!");
        return "redirect:/owner/menu";
    }
}
