package com._Blog.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostRequest {
    @NotBlank
    @Size(max = 300)
    private String title;
    @NotBlank
    @Size(max = 100000)
    private String content;

}
