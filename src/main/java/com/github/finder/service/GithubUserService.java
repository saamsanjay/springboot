package com.github.finder.service;

import com.github.finder.model.GithubUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class GithubUserService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public GithubUserService(@Value("${github.api.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public GithubUser findUserByUsername(String username) {
        String url = baseUrl + "/users/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GithubUser> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    GithubUser.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GitHub user not found: " + username);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(NOT_FOUND, "GitHub user not found: " + username, e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to fetch GitHub user information", e);
        }
    }
}
