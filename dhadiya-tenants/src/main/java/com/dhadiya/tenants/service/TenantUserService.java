package com.dhadiya.tenants.service;

import com.dhadiya.tenants.dto.TenantUserRequest;
import com.dhadiya.tenants.dto.TenantUserResponse;
import com.dhadiya.tenants.entity.TenantUser;
import com.dhadiya.tenants.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantUserService {

    private final TenantUserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TenantUserResponse> getAllUsers(String realmCode) {
        List<TenantUser> users = (realmCode != null && !realmCode.isBlank())
                ? userRepository.findByRealmCode(realmCode)
                : userRepository.findAll();

        return users.stream()
                .map(TenantUserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TenantUserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(TenantUserResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Showroom user not found with ID: " + id));
    }

    @Transactional
    public TenantUserResponse createUser(TenantUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        TenantUser user = TenantUser.builder()
                .realmCode(request.getRealmCode())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole())
                .locationCode(request.getLocationCode())
                .showroomLocation(request.getShowroomLocation())
                .phoneNumber(request.getPhoneNumber())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .lastLoginAt(LocalDateTime.now())
                .build();

        return TenantUserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public TenantUserResponse updateUserStatus(Long id, String status) {
        TenantUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setStatus(status);
        return TenantUserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
