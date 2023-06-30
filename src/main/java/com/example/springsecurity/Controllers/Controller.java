package com.example.springsecurity.Controllers;

import com.example.springsecurity.Dto.AuthResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
@GetMapping("/hello")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok(new AuthResponseDTO("Hello World"));
    }
}
