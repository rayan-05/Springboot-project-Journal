package com.rayanmitra.journalApp.controller;

import com.rayanmitra.journalApp.entity.JournalEntry;
import com.rayanmitra.journalApp.entity.User;
import com.rayanmitra.journalApp.service.JournalEntryService;
import com.rayanmitra.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public boolean addUser(@RequestBody User user){
        userService.saveEntry(user);
        return true;
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser( @RequestBody User user, @PathVariable String userName){
        User user1= userService.findByUsername(userName);

        if(user1!=null){
            user1.setUserName(user.getUserName());
            user1.setPassword(user.getPassword());
            userService.saveEntry(user1);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }



}
