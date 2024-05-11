package net.uqcloud.infs7202.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.uqcloud.infs7202.project.auth.repository.UserRepository;
import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.OrderDTO;
import net.uqcloud.infs7202.project.restaurant.repository.OrderRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantTableRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.*;
import net.uqcloud.infs7202.project.restaurant.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private @NonNull RestaurantTableRepository tableRepository;

    private @NonNull UserRepository userRepository;

    private @NonNull OrderService orderService;

    @GetMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    public String orderForm(HttpServletRequest request,
                            @PathVariable("uuid") String uuid,
                            Model model) {
        RestaurantTable table = tableRepository.findByUuid(uuid)
                .orElseThrow(() -> new DataNotFoundException("Link Invalid!"));

        Restaurant restaurant = table.getKey().getRestaurant();
        if (!restaurant.isActive()) {
            throw new DataNotFoundException("Link Invalid!");
        }

        Map<Category, List<MenuItem>> categorizedMenu = new HashMap<>();
        for (Category category : restaurant.getCategories()) {
            categorizedMenu.put(category, new ArrayList<>());
        }

        for (MenuItem menuItem : restaurant.getMenuItems()) {
            categorizedMenu.get(menuItem.getCategory()).add(menuItem);
        }

        for (Map.Entry<Category, List<MenuItem>> entry : categorizedMenu.entrySet()) {
            if (entry.getValue().isEmpty()) {
                categorizedMenu.remove(entry.getKey());
            }
        }

        model.addAttribute("table", table);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("categorizedMenu", categorizedMenu);
        model.addAttribute("orderDto", new OrderDTO());

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String message = null;
        if (inputFlashMap != null) {
            message = (String) inputFlashMap.getOrDefault("message", null);
        }

        model.addAttribute("message", message);

        return "user-order";
    }

    @PostMapping(value = "/{uuid}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PreAuthorize("permitAll()")
    public String order(@PathVariable("uuid") String uuid,
                        @ModelAttribute("table") RestaurantTable table,
                        @ModelAttribute("restaurant") Restaurant restaurant,
                        @ModelAttribute("categorizedMenu") HashMap<Category, MenuItem> categorizedMenu,
                        @Valid @ModelAttribute("orderDto") OrderDTO orderDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user-order";
        }

        orderService.submitOrder(uuid, orderDto);

        redirectAttributes.addFlashAttribute("message", "Order has been submitted!");
        return String.format("redirect:/order/%s", uuid);
    }

    @PostMapping(value = "/{id}/status")
    @PreAuthorize("hasAuthority('MANAGE_ORDER')")
    public String adjustStatus(@PathVariable("id") int orderId,
                               @RequestParam("status") String status,
                               @AuthenticationPrincipal UserDetails user,
                               RedirectAttributes redirectAttributes) {
        AuthUser authUser = userRepository.findByEmailAndIsActiveTrue(user.getUsername());
        if (authUser.getRestaurant() == null || !authUser.getRestaurant().isActive()) {
            throw new DataNotFoundException("User doesn't have a restaurant!");
        }

        orderService.updateStatus(authUser.getRestaurant(), orderId, status);

        redirectAttributes.addFlashAttribute("message", "Order has been updated!");
        return "redirect:/owner/order";
    }
}
