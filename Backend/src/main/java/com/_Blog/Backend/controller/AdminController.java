package com._Blog.Backend.controller;

import com._Blog.Backend.dto.AdminPostDTO;
import com._Blog.Backend.dto.AdminUserDTO;
import com._Blog.Backend.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/post/{id}/hide")
    public ResponseEntity<String> hidePost(@PathVariable Long id){
        this.adminService.hidePost(id);
        return ResponseEntity.ok("post hided");
    }

    @DeleteMapping("/post/{id}/delete")
    public ResponseEntity<String> deletePost(@PathVariable Long id){
        this.adminService.deletePost(id);
        return ResponseEntity.ok("post deleted");
    }

    @PostMapping("/comment/{id}/hide")
    public void hideComment(@PathVariable Long id){

    }

    @DeleteMapping("/comment/{id}/delete")
    public void deleteComment(@PathVariable Long id){
        this.adminService.deleteComment(id);
    }

    @GetMapping("/post")
    public ResponseEntity<List<AdminPostDTO>> getPosts(@RequestParam(defaultValue = "0") Integer page){
        List<AdminPostDTO> posts = this.adminService.getPosts(page);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user")
    public ResponseEntity<List<AdminUserDTO>> getUsers(@RequestParam(defaultValue = "0") Integer page) {
        List<AdminUserDTO> users = this.adminService.getUsers(page);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/banned")
    public ResponseEntity<List<AdminUserDTO>> getUserBanned(@RequestParam(defaultValue = "0") Integer page) {
        List<AdminUserDTO> bannedUsers = this.adminService.getBannedUsers(page);
        return  ResponseEntity.ok(bannedUsers);
    }

    @PostMapping("/user/{id}/ban")
    public ResponseEntity<String> banUser(@PathVariable Long id){
        this.adminService.banUser(id);
        return ResponseEntity.ok("user has banned");
    }

    @PostMapping("/user/{id}/unban")
    public ResponseEntity<String> unbanUser(@PathVariable Long id){
        this.adminService.unBanUser(id);
        return ResponseEntity.ok("user has unbanned");
    }

}
