package com._Blog.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSuggetion {
    private UserResponse userResponse;
    private Boolean isFollowing;
}
