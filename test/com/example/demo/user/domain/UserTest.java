package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    void UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("kok202@kakao.com")
                .nickname("kok202")
                .address("Pangyo")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaaaaaaaaaaaaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("kok202@kakao.com");
        assertThat(user.getNickname()).isEqualTo("kok202");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaaaaaaaaaaaaaa");
    }

    @Test
    void UserUpdate_객체로_데이터를_업데이트할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kok202@kakao.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaaaaaaaaaaab")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("kok202-k")
                .address("Pangyo")
                .build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("kok202@kakao.com");
        assertThat(user.getNickname()).isEqualTo("kok202-k");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaaaaaaaaaaaaaab");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kok202@kakao.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaaaaaaaaaaab")
                .build();

        // when
        user = user.login(new TestClockHolder(100L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kok202@kakao.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaaaaaaaaaaab")
                .build();
        // when
        user = user.certificate("aaaaaaaaaaaaaaaaaaaab");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kok202@kakao.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaaaaaaaaaaab")
                .build();
        // when
        // then
        assertThatThrownBy(() -> user.certificate("asdfadsfdsad"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
