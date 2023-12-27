package ua.shortener.security.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.shortener.security.auth.dto.JwtAuthenticationResponse;
import ua.shortener.security.auth.dto.SignInRequest;
import ua.shortener.security.auth.dto.SignUpRequest;
import ua.shortener.security.jwt.JwtService;
import ua.shortener.security.jwt.JwtServiceImpl;
import ua.shortener.user.Role;
import ua.shortener.user.User;
import ua.shortener.user.repository.UserRepository;
import ua.shortener.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  UserService userService;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;
    @Mock
    private  AuthenticationManager authenticationManager;

    private JwtServiceImpl jwtServiceImpl = new JwtServiceImpl();


    @Test
    void signup() {

    }

    @Test
    void signIn() {
        //given
        User user = new User();
        user.setEnabled(true);
        user.setName("ivan");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setEmail("ivan@gmail.com");
        user.setRole(Role.USER);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User( user.getEmail(),
                user.getPassword(),
                List.of(user.getRole())
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList());
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTcwMzcxMTQ2NCwiZXhwIjoxNzAzNzEyMDY0fQ.-LcOHxKc_902XC-HsAhpQMKfexb0K6yY0z11nA98FsQ";

        //when
        Mockito.when(userService.loadUserByUsername("ivan@gmail.com")).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(userDetails)).thenReturn(validToken);

        //then
        UserDetails userDetails1 = userService.loadUserByUsername("ivan@gmail.com");
        String token = jwtService.generateToken(userDetails1);
        String name = "ivan";
//        System.out.println(token);

        Assertions.assertEquals(userDetails.getUsername(), jwtServiceImpl.extractUserName(token));
    }

//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private AuthenticationServiceImpl authenticationService;
//
//    @Test
//    void signup_Success() {
//        // Arrange
//        SignUpRequest request = new SignUpRequest("John", "john@example.com", "password");
//
//        // Mocking behavior
//        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(java.util.Optional.empty());
//        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
//
//        // Act
//        JwtAuthenticationResponse response = authenticationService.signup(request);
//
//        // Assert
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals("Successfully added", response.getMessage());
//
//        // Verify interactions
//        verify(userRepository).save(any(User.class));
//    }
//
//    @Test
//    void signup_UserAlreadyExists() {
//        // Arrange
//        SignUpRequest request = new SignUpRequest("John", "john@example.com", "password");
//
//        // Mocking behavior
//        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(java.util.Optional.of(new User()));
//
//        // Act
//        JwtAuthenticationResponse response = authenticationService.signup(request);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//        assertNotNull(response.getMessage());
//
//        // Verify interactions (no save should occur)
//        verify(userRepository, Mockito.never()).save(any(User.class));
//    }
//
//    @Test
//    void signIn_Success() {
//        // Arrange
//        SignInRequest request = new SignInRequest("john@example.com", "password");
//        HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
//        User user = new User();
//        user.setRole(Role.USER);
//        when(userService.loadUserByUsername(request.getEmail())).thenReturn(new org.springframework.security.core.userdetails.User(user.getEmail(),
//                user.getPassword(),
//                List.of(user.getRole())
//                        .stream()
//                        .map(role -> new SimpleGrantedAuthority(role.name()))
//                        .toList()));
//        when(jwtService.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(),
//                user.getPassword(),
//                List.of(user.getRole())
//                        .stream()
//                        .map(role -> new SimpleGrantedAuthority(role.name()))
//                        .toList()))).thenReturn("generatedToken");
//
//        // Act
//        JwtAuthenticationResponse response = authenticationService.signIn(request, servletResponse);
//
//        // Assert
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals("generatedToken", response.getMessage());
//
//        // Verify interactions
//        verify(servletResponse).addHeader("Authorization", "Bearer generatedToken");
//    }
//
//    @Test
//    void signIn_InvalidCredentials() {
//        // Arrange
//        SignInRequest request = new SignInRequest("john@example.com", "invalidPassword");
//        HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
//        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));
//
//        // Act
//        JwtAuthenticationResponse response = authenticationService.signIn(request, servletResponse);
//
//        // Assert
//        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
//        assertNotNull(response.getMessage());
//
//        // Verify interactions (no header should be added)
//        verify(servletResponse, Mockito.never()).addHeader(Mockito.eq("Authorization"), Mockito.anyString());
//    }
}