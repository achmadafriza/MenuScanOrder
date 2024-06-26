package net.uqcloud.infs7202.project.restaurant.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.RestaurantDTO;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import net.uqcloud.infs7202.project.restaurant.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private @NonNull RestaurantRepository restaurantRepository;

    private @NonNull RestaurantService restaurantService;

    @GetMapping("/{id}/edit")
    public String editRestaurantForm(@PathVariable("id") int id,
                                     Model model) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Restaurant doesn't exist"));
        model.addAttribute("restaurant", restaurant);

        RestaurantDTO dto = new RestaurantDTO();
        dto.setRestaurantName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setTableNumber(restaurant.getTables().size());
        dto.setProfilePictureUrl(restaurant.getProfilePic());
        dto.setBackgroundPictureUrl(restaurant.getBackgroundPic());

        if (restaurant.getProfilePic() != null) {
            String profileUrl = restaurant.getProfilePic();

            String profilePictureName = profileUrl.substring(profileUrl.lastIndexOf('/') + 1);
            dto.setProfilePictureName(profilePictureName);
        }

        if (restaurant.getBackgroundPic() != null) {
            String backgroundUrl = restaurant.getBackgroundPic();

            String backgroundPictureName = backgroundUrl.substring(backgroundUrl.lastIndexOf('/') + 1);
            dto.setBackgroundPictureName(backgroundPictureName);
        }

        model.addAttribute("restaurantDto", dto);

        return "edit-restaurant";
    }

    @PostMapping("/{id}/edit")
    public String editRestaurant(@PathVariable("id") int id,
                                 @ModelAttribute("restaurant") Restaurant restaurant,
                                 @Valid @ModelAttribute("restaurantDto") RestaurantDTO restaurantDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "edit-restaurant";
        }

        restaurantService.editRestaurant(id, restaurantDto);

        redirectAttributes.addFlashAttribute("message", "Restaurant has been updated!");
        return "redirect:/admin";
    }

    @PostMapping("/{id}/table/regenerate")
    public String regenerateTableUUID(@PathVariable("id") int id,
                                      @RequestParam("uuid") String uuid,
                                      RedirectAttributes redirectAttributes) {
        restaurantService.regenerateTable(id, uuid);

        redirectAttributes.addFlashAttribute("message", "Table has been regenerated!");
        return "redirect:/owner/table";
    }
}
