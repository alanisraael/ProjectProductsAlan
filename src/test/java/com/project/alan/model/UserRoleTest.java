package com.project.alan.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRole Enum Tests")
public class UserRoleTest {

    @Test
    @DisplayName("Debe tener exactamente dos valores: ADMIN y USER")
    public void testUserRoleValues() {
        // When
        UserRole[] values = UserRole.values();

        // Then
        assertEquals(2, values.length);
        assertEquals(UserRole.ADMIN, values[0]);
        assertEquals(UserRole.USER, values[1]);
    }

    @Test
    @DisplayName("Debe encontrar ADMIN por nombre")
    public void testUserRoleValueOfAdmin() {
        // When
        UserRole admin = UserRole.valueOf("ADMIN");

        // Then
        assertEquals(UserRole.ADMIN, admin);
    }

    @Test
    @DisplayName("Debe encontrar USER por nombre")
    public void testUserRoleValueOfUser() {
        // When
        UserRole user = UserRole.valueOf("USER");

        // Then
        assertEquals(UserRole.USER, user);
    }

    @Test
    @DisplayName("ADMIN debe ser diferente de USER")
    public void testUserRoleAdminNotEqualUser() {
        // Then
        assertNotEquals(UserRole.ADMIN, UserRole.USER);
    }

    @Test
    @DisplayName("ADMIN debe tener nombre correcto")
    public void testUserRoleAdminName() {
        // Then
        assertEquals("ADMIN", UserRole.ADMIN.name());
    }

    @Test
    @DisplayName("USER debe tener nombre correcto")
    public void testUserRoleUserName() {
        // Then
        assertEquals("USER", UserRole.USER.name());
    }

    @Test
    @DisplayName("ADMIN debe tener ordinal 0")
    public void testUserRoleAdminOrdinal() {
        // Then
        assertEquals(0, UserRole.ADMIN.ordinal());
    }

    @Test
    @DisplayName("USER debe tener ordinal 1")
    public void testUserRoleUserOrdinal() {
        // Then
        assertEquals(1, UserRole.USER.ordinal());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException para valor inválido")
    public void testUserRoleInvalidValue() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            UserRole.valueOf("INVALID");
        });
    }

    @Test
    @DisplayName("ADMIN toString debe retornar 'ADMIN'")
    public void testUserRoleAdminToString() {
        // Then
        assertEquals("ADMIN", UserRole.ADMIN.toString());
    }

    @Test
    @DisplayName("USER toString debe retornar 'USER'")
    public void testUserRoleUserToString() {
        // Then
        assertEquals("USER", UserRole.USER.toString());
    }
}
