package com.app.billing.controller;

import com.app.billing.model.User;
import com.app.billing.model.Business;
import com.app.billing.repository.UserRepository;
import com.app.billing.repository.BusinessRepository;
import com.app.billing.dto.SignupRequest;
import com.app.billing.dto.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BusinessRepository businessRepo;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {

        // Check if user already exists
        if (userRepo.findByEmail(request.getEmail()) != null) {
            return "User already exists!";
        }

        // Save user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        user = userRepo.save(user);

        // Save business
        Business business = new Business();
        business.setBusinessName(request.getBusinessName());
        business.setOwnerName(request.getOwnerName());
        business.setAddress(request.getAddress());
        business.setContact(request.getContact());
        business.setGstNumber(request.getGstNumber());
        business.setLogoPath(request.getLogoPath());
        business.setQrPath(request.getQrPath());
        business.setUser(user);

        businessRepo.save(business);

        return "Signup successful!";
    }
    @PostMapping("/login")
public String login(@RequestBody LoginRequest request) {

    User user = userRepo.findByEmail(request.getEmail());

    if (user == null) {
        return "User not found!";
    }

    if (!user.getPassword().equals(request.getPassword())) {
        return "Invalid password!";
    }

    return "Login successful!";
}

}