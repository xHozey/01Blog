package com._Blog.Backend.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileUpdateRequest {
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
    @Size(max = 1500)
    private String iconProfile;
    @Size(max = 500)
    private String bio;
}
