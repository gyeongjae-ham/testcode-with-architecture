package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("kok2020@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("kok3030@naver.com")
                .nickname("kok303")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        String email = "kok2020@naver.com";

        // When
        User result = userService.getByEmail(email);

        // Then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // Given
        String email = "kok303@naver.com";

        // When
        // Then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        // When
        User result = userService.getById(1);

        // Then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }

    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다() {
        // Given
        UserCreate userCreate = UserCreate.builder()
                .email("kok2020@kakao.com")
                .address("Gyeongi")
                .nickname("kok202-k")
                .build();

        // When
        User result = userService.create(userCreate);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaaaaaaaaa");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        // Given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .nickname("kok202-n")
                .build();

        // When
        userService.update(1, userUpdate);

        // Then
        User user = userService.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getNickname()).isEqualTo("kok202-n");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // Given
        // When
        userService.login(1);

        // Then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isGreaterThan(0L);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // Given
        // When
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        // Then
        User user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
