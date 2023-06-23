package com.example.demo.repository;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus로_유저_데이터를_찾아올_수_있다() {
        // Given
        // When
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // Then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // Given
        // When
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_유저_데이터를_찾아올_수_있다() {
        // Given
        // When
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("kok2020@naver.com", UserStatus.ACTIVE);

        // Then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // Given
        // When
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("kok2020@naver.com", UserStatus.PENDING);

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

}
