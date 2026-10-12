package com.github.finder.controller;

import com.github.finder.model.GithubUser;
import com.github.finder.service.GithubUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GithubUserController {

    private final GithubUserService githubUserService;

    public GithubUserController(GithubUserService githubUserService) {
        this.githubUserService = githubUserService;
    }

    @GetMapping("/users/{username}")
    public GithubUser getUser(@PathVariable String username) {
        return githubUserService.findUserByUsername(username);
    }

    @GetMapping("/")
    public String health() {
        return "GitHub User Finder is running";
    }
}
