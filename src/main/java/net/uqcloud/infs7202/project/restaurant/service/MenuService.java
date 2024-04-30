package net.uqcloud.infs7202.project.restaurant.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.MenuDTO;
import net.uqcloud.infs7202.project.restaurant.repository.CategoryRepository;
import net.uqcloud.infs7202.project.restaurant.repository.MenuItemRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Category;
import net.uqcloud.infs7202.project.restaurant.repository.model.MenuItem;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import net.uqcloud.infs7202.project.service.CurrencyParser;
import net.uqcloud.infs7202.project.service.file.FileStorageService;
import net.uqcloud.infs7202.project.service.file.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MenuService {
    private @NonNull RestaurantRepository restaurantRepository;
    private @NonNull CategoryRepository categoryRepository;
    private @NonNull MenuItemRepository menuRepository;

    private @NonNull @Qualifier("staticFileStorage") FileStorageService fileService;

    @Transactional
    public MenuItem createMenu(int restaurantId, MenuDTO dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found!"));

        MenuItem menu = new MenuItem();
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menu.setName(dto.getName());
        menu.setDescription(dto.getDescription());

        try {
            if (dto.getMenuPicture() != null && !dto.getMenuPicture().isEmpty()) {
                String pictureUrl = fileService.saveFile(dto.getMenuPicture());
                menu.setMenuPic(pictureUrl);
            }

            double price = CurrencyParser.parse(dto.getPrice(), Locale.US)
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();

            menu.setPrice(price);
        } catch (IOException e) {
            throw new FileUploadException(e);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad currency format");
        }

        return menuRepository.save(menu);
    }
}
