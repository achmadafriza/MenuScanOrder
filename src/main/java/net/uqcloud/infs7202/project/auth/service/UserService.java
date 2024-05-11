package net.uqcloud.infs7202.project.auth.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.auth.controller.dto.EditUserDTO;
import net.uqcloud.infs7202.project.auth.controller.dto.SignupUserDTO;
import net.uqcloud.infs7202.project.auth.controller.dto.CreateUserDTO;
import net.uqcloud.infs7202.project.auth.repository.RoleRepository;
import net.uqcloud.infs7202.project.auth.repository.UserRepository;
import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import net.uqcloud.infs7202.project.auth.repository.model.Role;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.exception.DuplicateFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.RestaurantDTO;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import net.uqcloud.infs7202.project.restaurant.service.RestaurantService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private @NonNull UserRepository userRepository;
    private @NonNull RoleRepository roleRepository;
    private @NonNull RestaurantRepository restaurantRepository;
    private @NonNull PasswordEncoder encoder;

    private @NonNull RestaurantService restaurantService;

    @Transactional
    public AuthUser signup(@Valid SignupUserDTO dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateFoundException("User already exists");
        }

        AuthUser user = new AuthUser();

        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));

        Role ownerRole = roleRepository.findByName("OWNER");
        user.setRole(ownerRole);

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantName(dto.getRestaurantName());
        restaurantDTO.setDescription(dto.getDescription());
        restaurantDTO.setTableNumber(dto.getTableNumber());
        restaurantDTO.setProfilePicture(dto.getProfilePicture());
        restaurantDTO.setBackgroundPicture(dto.getBackgroundPicture());

        Restaurant restaurant = restaurantService.createRestaurant(restaurantDTO);
        user.setRestaurant(restaurant);

        return userRepository.save(user);
    }

    @Transactional
    public AuthUser createUser(@Valid CreateUserDTO dto) {
        AuthUser user = new AuthUser();

        Role role = roleRepository.findByName(dto.getRole());
        switch (role.getName()) {
            case "OWNER":
                return signup(convertDTO(dto));
            case "STAFF":
                Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                        .orElseThrow(() -> new DataNotFoundException("Restaurant not found!"));
                user.setRestaurant(restaurant);
                break;
            case "ADMIN":
                break;
            default:
                throw new UnsupportedOperationException();
        }

        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    private static SignupUserDTO convertDTO(CreateUserDTO dto) {
        SignupUserDTO signupDto = new SignupUserDTO();
        signupDto.setEmail(dto.getEmail());
        signupDto.setPassword(dto.getPassword());
        signupDto.setConfirmPassword(dto.getConfirmPassword());
        signupDto.setRestaurantName(dto.getRestaurantName());
        signupDto.setDescription(dto.getDescription());
        signupDto.setTableNumber(dto.getTableNumber());
        signupDto.setProfilePicture(dto.getProfilePicture());
        signupDto.setBackgroundPicture(dto.getBackgroundPicture());

        return signupDto;
    }

    @Transactional
    public AuthUser editUser(int id, @Valid EditUserDTO dto) {
        AuthUser user = userRepository.findByIdAndIsActiveTrue(id);
        user.setPassword(encoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public AuthUser updateUserActive(int id, boolean isActive) {
        AuthUser user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found!"));

        if (user.getRestaurant() != null) {
            restaurantRepository.setRestaurantActive(user.getRestaurant().getId(), isActive);
        }

        userRepository.setUserActive(id, isActive);

        return user;
    }
}
