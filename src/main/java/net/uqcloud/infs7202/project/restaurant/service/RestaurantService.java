package net.uqcloud.infs7202.project.restaurant.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.RestaurantDTO;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantTableRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import net.uqcloud.infs7202.project.restaurant.repository.model.RestaurantTable;
import net.uqcloud.infs7202.project.service.SequenceGenerator;
import net.uqcloud.infs7202.project.service.file.FileStorageService;
import net.uqcloud.infs7202.project.service.file.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private @NonNull RestaurantRepository restaurantRepository;
    private @NonNull RestaurantTableRepository tableRepository;

    private @NonNull @Qualifier("staticFileStorage") FileStorageService fileService;
    private @NonNull SequenceGenerator sequenceGenerator;

    @Transactional
    public Restaurant createRestaurant(@Valid RestaurantDTO dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getRestaurantName());
        restaurant.setDescription(dto.getDescription());

        try {
            if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
                String pictureUrl = fileService.saveFile(dto.getProfilePicture());
                restaurant.setProfilePic(pictureUrl);
            }

            if (dto.getBackgroundPicture() != null && !dto.getBackgroundPicture().isEmpty()) {
                String pictureUrl = fileService.saveFile(dto.getBackgroundPicture());
                restaurant.setBackgroundPic(pictureUrl);
            }
        } catch (IOException e) {
            throw new FileUploadException(e);
        }

        List<RestaurantTable> tables = new ArrayList<>();
        for(int i = 1; i <= dto.getTableNumber(); i++) {
            RestaurantTable.Key key = new RestaurantTable.Key();
            key.setRestaurant(restaurant);
            key.setTableNumber(i);

            RestaurantTable table = new RestaurantTable();
            table.setKey(key);
            table.setUuid(sequenceGenerator.nextId());

            tables.add(table);
        }

        restaurant.setTables(tables);

        restaurant = restaurantRepository.save(restaurant);
        tables = tableRepository.saveAll(tables);

        restaurant.setTables(tables);

        return restaurant;
    }

    @Transactional
    public Restaurant editRestaurant(int id, @Valid RestaurantDTO dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));

        try {
            if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
                String pictureUrl = fileService.saveFile(dto.getProfilePicture());
                restaurant.setProfilePic(pictureUrl);
            }

            if (dto.getBackgroundPicture() != null && !dto.getBackgroundPicture().isEmpty()) {
                String pictureUrl = fileService.saveFile(dto.getBackgroundPicture());
                restaurant.setBackgroundPic(pictureUrl);
            }
        } catch (IOException e) {
            throw new FileUploadException(e);
        }

        restaurant.setName(dto.getRestaurantName());
        restaurant.setDescription(dto.getDescription());
        restaurant = restaurantRepository.save(restaurant);

        int currentTableNumber = restaurant.getTables().size();
        if (currentTableNumber > dto.getTableNumber()) {
            tableRepository.updateIsActiveByTableNumberRange(id, dto.getTableNumber()+1, currentTableNumber, false);
        } else {
            tableRepository.updateIsActiveByTableNumberRange(id, currentTableNumber, dto.getTableNumber(), true);

            int existingTable = tableRepository.countTablesForRestaurant(id);
            List<RestaurantTable> tables = new ArrayList<>();
            for (int i = existingTable+1; i <= dto.getTableNumber(); i++) {
                RestaurantTable.Key key = new RestaurantTable.Key();
                key.setRestaurant(restaurant);
                key.setTableNumber(i);

                RestaurantTable table = new RestaurantTable();
                table.setKey(key);
                table.setUuid(sequenceGenerator.nextId());

                tables.add(table);
            }

            tableRepository.saveAll(tables);
        }

        return restaurant;
    }

    @Transactional
    public RestaurantTable regenerateTable(int restaurantId, String oldUUID) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));

        RestaurantTable table = tableRepository.findByKey_RestaurantAndUuid(restaurant, oldUUID)
                .orElseThrow(() -> new DataNotFoundException("Table not found!"));

        table.setUuid(sequenceGenerator.nextId());

        return tableRepository.save(table);
    }
}
