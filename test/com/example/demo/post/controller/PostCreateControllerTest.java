package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 16L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build());
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("kok202");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(16L);
    }
}
