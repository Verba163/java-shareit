package ru.practicum.shareit.server.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void builderShouldCreateUser() {

        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void allArgsConstructorShouldCreateUser() {

        User user = new User(1L, "Jane Doe", "jane.doe@example.com");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    public void noArgsConstructorShouldCreateUserWithNullFields() {

        User user = new User();

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isNull();
    }

    @Test
    public void equalsAndHashCodeShouldBeEqualByEmail() {
        User user1 = new User(1L, "User  One", "user@example.com");
        User user2 = new User(2L, "User  Two", "user@example.com");

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    public void equalsAndHashCodeShouldNotBeEqualByDifferentEmail() {
        User user1 = new User(1L, "User  One", "user1@example.com");
        User user2 = new User(2L, "User  Two", "user2@example.com");

        assertThat(user1).isNotEqualTo(user2);
        assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());
    }
}