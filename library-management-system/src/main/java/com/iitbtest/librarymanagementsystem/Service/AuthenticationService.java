package com.iitbtest.librarymanagementsystem.Service;

import com.iitbtest.librarymanagementsystem.dto.Request;
import com.iitbtest.librarymanagementsystem.dto.Response;
import com.iitbtest.librarymanagementsystem.entity.Role;
import com.iitbtest.librarymanagementsystem.entity.User;
import com.iitbtest.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Response signup(Request request) {

        Role role = request.getUser().equals("LIBRARIAN") ? Role.LIBRARIAN : Role.MEMBER;

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return Response
                .builder()
                .token(token)
                .userType(user.getRole().name()).build();
    }

    public Response login(Request request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("invalid user"));
        String token = jwtService.generateToken(user);

        return Response
                .builder()
                .token(token).userType(user.getRole().name()).build();
    }
}
