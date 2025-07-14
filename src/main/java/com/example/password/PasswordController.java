package com.example.password;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
public class PasswordController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam String name, @RequestParam String dob, Model model) {
    String password = generatePassword(name, dob);
    int strength = calculateStrength(password);
    
    model.addAttribute("password", password);
    model.addAttribute("strength", strength);
    model.addAttribute("name", name); // add this
    model.addAttribute("dob", dob);   // add this
    
    return "index";
}


    @GetMapping("/success")
    public String success() {
        return "success";
    }

    private String generatePassword(String name, String dob) {
        String base = name.toLowerCase().substring(0, Math.min(3, name.length())) + dob.replace("-", "");
        String special = "!@#$%^&*";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();

        String randomSpecial = String.valueOf(special.charAt(rand.nextInt(special.length())));
        String randomUpper = String.valueOf(upper.charAt(rand.nextInt(upper.length())));
        String randomDigit = String.valueOf(rand.nextInt(100));

        String rawPassword = base + randomSpecial + randomUpper + randomDigit;
        return shuffle(rawPassword);
    }

    private String shuffle(String input) {
        char[] a = input.toCharArray();
        Random rnd = new Random();
        for (int i = a.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            char tmp = a[index];
            a[index] = a[i];
            a[i] = tmp;
        }
        return new String(a);
    }

    private int calculateStrength(String pwd) {
        int score = 0;
        if (pwd.length() >= 8) score += 25;
        if (pwd.matches(".*[A-Z].*") && pwd.matches(".*[a-z].*")) score += 20;
        if (pwd.matches(".*\\d.*")) score += 20;
        if (pwd.matches(".*[!@#$%^&*].*")) score += 20;
        if (pwd.length() > 12) score += 15;
        return Math.min(score, 100);
    }
}