package com.project.alan.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Model Tests")
public class UserTest {

    @Test
    @DisplayName("Debe crear un usuario con constructor completo")
    public void testUserCreation() {
        // Given
        String username = "testuser";
        String passwordHash = "hashedpassword";
        UserRole role = UserRole.USER;

        // When
        User user = new User(username, passwordHash, role);

        // Then
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(role, user.getRole());
        assertNull(user.getId()); // ID should be null initially
    }

    @Test
    @DisplayName("Debe crear un usuario con constructor vacío")
    public void testUserEmptyConstructor() {
        // When
        User user = new User();

        // Then
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPasswordHash());
        assertNull(user.getRole());
    }

    @Test
    @DisplayName("Debe manejar ID correctamente")
    public void testUserId() {
        // Given
        User user = new User();

        // When
        user.setId(1L);

        // Then
        assertEquals(1L, user.getId());
    }

    @Test
    @DisplayName("Debe manejar username correctamente")
    public void testUserUsername() {
        // Given
        User user = new User();

        // When
        user.setUsername("testuser");

        // Then
        assertEquals("testuser", user.getUsername());
    }

    @Test
    @DisplayName("Debe manejar passwordHash correctamente")
    public void testUserPasswordHash() {
        // Given
        User user = new User();

        // When
        user.setPasswordHash("hashedpassword");

        // Then
        assertEquals("hashedpassword", user.getPasswordHash());
    }

    @Test
    @DisplayName("Debe manejar rol USER correctamente")
    public void testUserRoleUser() {
        // Given
        User user = new User();

        // When
        user.setRole(UserRole.USER);

        // Then
        assertEquals(UserRole.USER, user.getRole());
    }

    @Test
    @DisplayName("Debe manejar rol ADMIN correctamente")
    public void testUserRoleAdmin() {
        // Given
        User user = new User();

        // When
        user.setRole(UserRole.ADMIN);

        // Then
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Debe verificar igualdad de usuarios")
    public void testUserEquality() {
        // Given
        User user1 = new User("user", "pass", UserRole.USER);
        User user2 = new User("user", "pass", UserRole.USER);
        User user3 = new User("different", "pass", UserRole.USER);

        // Then
        assertEquals(user1, user2); // Lombok @Data should handle equals
        assertNotEquals(user1, user3);
    }

    @Test
    @DisplayName("Debe generar hashCode correctamente")
    public void testUserHashCode() {
        // Given
        User user1 = new User("user", "pass", UserRole.USER);
        User user2 = new User("user", "pass", UserRole.USER);

        // Then
        assertEquals(user1.hashCode(), user2.hashCode()); // Lombok @Data should handle hashCode
    }

    @Test
    @DisplayName("Debe generar toString correctamente")
    public void testUserToString() {
        // Given
        User user = new User("testuser", "hashedpass", UserRole.USER);

        // When
        String toString = user.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("USER"));
        // Password should not be in toString due to @JsonIgnore equivalent
    }

    @Test
    @DisplayName("Constructor de tres parámetros no debe incluir ID")
    public void testConstructorDoesNotSetId() {
        // When
        User user = new User("username", "password", UserRole.USER);

        // Then
        assertNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPasswordHash());
        assertEquals(UserRole.USER, user.getRole());
    }
}
