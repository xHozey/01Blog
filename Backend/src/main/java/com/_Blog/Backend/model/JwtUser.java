package com._Blog.Backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtUser {
    private final Long id;
    private final String username;
}
