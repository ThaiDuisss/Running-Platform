package com.laundry.controller;

import com.laundry.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL_VALIDATE_CONTROLLER")
public class EmailValidateController {
    UserServiceImpl userService;
    @GetMapping("/validateEmail/{emailToken}")
    public String validateEmailToken(@PathVariable("emailToken") String emailToken) {
        log.info("akkk{}",userService.verifyEmail(emailToken) );
        if(userService.verifyEmail(emailToken)) {
            return "success";
        } else {
            return "error";
        }

    }
}
