package com.project.alan.service;

import com.project.alan.model.User;
import com.project.alan.model.UserRole;
import com.project.alan.repositories.PersonRepository;
import com.project.alan.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserDetailsServiceImpl Tests")
public class UserDetailsServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User("testuser", "hashedpassword", UserRole.USER);
    }

    @Test
    @DisplayName("Debe cargar usuario por username exitosamente")
    public void testLoadUserByUsernameSuccess() {
        // Given
        when(personRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("hashedpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException cuando usuario no existe")
    public void testLoadUserByUsernameNotFound() {
        // Given
        when(personRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        assertEquals("Username nonexistent does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Debe asignar rol USER correctamente")
    public void testLoadUserByUsernameUserRole() {
        // Given
        User userRole = new User("user", "password", UserRole.USER);
        when(personRepository.findByUsername("user")).thenReturn(Optional.of(userRole));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        // Then
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Debe manejar username null")
    public void testLoadUserByUsernameNullUsername() {
        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    @DisplayName("Debe manejar username vacío")
    public void testLoadUserByUsernameEmptyUsername() {
        // Given
        when(personRepository.findByUsername("")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });

        assertEquals("Username  does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Debe verificar que el usuario tenga exactamente una autoridad")
    public void testLoadUserByUsernameSingleAuthority() {
        // Given
        when(personRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    @DisplayName("Debe verificar que la autoridad tenga el prefijo ROLE_")
    public void testLoadUserByUsernameAuthorityPrefix() {
        // Given
        when(personRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertTrue(userDetails.getAuthorities().stream()
            .allMatch(auth -> auth.getAuthority().startsWith("ROLE_")));
    }

    @Test
    @DisplayName("Debe manejar usuario con rol ADMIN")
    public void testLoadUserByUsernameAdminRole() {
        // Given
        User adminUser = new User("admin", "adminpass", UserRole.ADMIN);
        when(personRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // Then
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Debe verificar que todos los flags de usuario estén habilitados por defecto")
    public void testLoadUserByUsernameDefaultFlags() {
        // Given
        when(personRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Debe llamar al repositorio exactamente una vez")
    public void testLoadUserByUsernameRepositoryCall() {
        // Given
        when(personRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        userDetailsService.loadUserByUsername("testuser");

        // Then
        verify(personRepository, times(1)).findByUsername("testuser");
    }
}
