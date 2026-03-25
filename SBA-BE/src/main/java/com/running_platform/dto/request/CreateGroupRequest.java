package com.running_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CreateGroupRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotEmpty
    @Size(min = 2, message = "Group must have at least 2 other members")
    private List<Long> memberIds;
}