package com.example.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.MediaType;
import java.util.List;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;



@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/users", produces = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
    public String showUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

@PostMapping("/users/add")
public String addUser(@RequestParam String username, @RequestParam String role) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode("default123")); //仮パスワードを設定
    user.setRole("ROLE_"+role);
    userRepository.save(user);
    return "redirect:/admin/users";
    }
}
