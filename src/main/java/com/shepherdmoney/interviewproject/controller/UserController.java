package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;

    // TODO: wire in the user repository (~ 1 line)
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        User user = new User();
        user.setName(payload.getName());
        user.setEmail(payload.getEmail());

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Return 200 OK with the user's id
        return ResponseEntity.ok(savedUser.getId());
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        // Check if the user with the given ID exists in the database
        if (userRepository.existsById(userId)) {
            // Delete the user from the database
            userRepository.deleteById(userId);
            // Return 200 OK with a success message
            return ResponseEntity.ok("User with ID " + userId + " deleted successfully.");
        } else {
            // Return 400 Bad Request with an error message
            return ResponseEntity.badRequest().body("User with ID " + userId + " does not exist.");
        }
    }
}
