package net.uqcloud.infs7202.project.restaurant.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.exception.DuplicateFoundException;
import net.uqcloud.infs7202.project.restaurant.repository.CategoryRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Category;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant/{restaurantId}/category")
public class CategoryController {
    private @NonNull RestaurantRepository restaurantRepository;
    private @NonNull CategoryRepository categoryRepository;

    @PostMapping("/add")
    public String addCategory(@PathVariable("restaurantId") int restaurantId,
                              @RequestParam("name") String name,
                              RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));

        if (categoryRepository.existsByRestaurantAndName(restaurant, name)) {
            throw new DuplicateFoundException("Category already exists!");
        }

        Category category = new Category();
        category.setRestaurant(restaurant);
        category.setName(name);

        categoryRepository.save(category);

        redirectAttributes.addFlashAttribute("message", "Category is added!");
        return "redirect:/owner/menu";
    }
}
