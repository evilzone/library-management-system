package com.iitbtest.librarymanagementsystem.controller;


import com.iitbtest.librarymanagementsystem.Service.AuthenticationService;
import com.iitbtest.librarymanagementsystem.Service.JwtService;
import com.iitbtest.librarymanagementsystem.Service.MemberService;
import com.iitbtest.librarymanagementsystem.dto.Request;
import com.iitbtest.librarymanagementsystem.dto.Response;
import com.iitbtest.librarymanagementsystem.entity.Role;
import com.iitbtest.librarymanagementsystem.entity.User;
import com.iitbtest.librarymanagementsystem.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Response> signup(@ModelAttribute Request request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @GetMapping("/login")
    public ResponseEntity<Response> login(@ModelAttribute Request request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping("{id}")
    public ResponseEntity<User> viewUser(@PathVariable("id") Long id) {
        return new ResponseEntity<User>(memberService.viewMember(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return new ResponseEntity<User>(memberService.addMember(user), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return new ResponseEntity<User>(memberService.updateUser(user, id), HttpStatus.OK);
    }

    // librarian can remove any member
    // member can only delete its own account
    @DeleteMapping("{id}")
    public ResponseEntity<String> removeUser(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {

        String authHeader = httpServletRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtService.retrieveUsername(token);
        Optional<User> user = memberService.getMemberByEmail(email);

        // if user is member delete only its own account
        if(user.get().getRole().name().equals(Role.MEMBER.name()) && !user.get().getId().equals(id)) {
            throw new ResourceNotFoundException("User", "Id", id);
        }
        memberService.removeMember(id);
        return new ResponseEntity<String>("Member deleted successfully!!!", HttpStatus.OK);
    }
}
