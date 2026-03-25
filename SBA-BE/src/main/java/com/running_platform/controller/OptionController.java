package com.running_platform.controller;

import com.running_platform.enums.ChallengeStatus;
import com.running_platform.enums.ChallengeType;
import com.running_platform.enums.VisibilityEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    // 1. Visibility
    @GetMapping("/visibility")
    public List<VisibilityEnum> getVisibilityOptions() {
        return Arrays.asList(VisibilityEnum.values());
    }

    // 2. Challenge Status
    @GetMapping("/challenge-status")
    public List<ChallengeStatus> getChallengeStatusOptions() {
        return Arrays.asList(ChallengeStatus.values());
    }

    // 3. Challenge Type
    @GetMapping("/challenge-type")
    public List<ChallengeType> getChallengeTypeOptions() {
        return Arrays.asList(ChallengeType.values());
    }
}
