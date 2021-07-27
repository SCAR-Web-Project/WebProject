//controller는 프론트와 백의 매개체, 요청에 따라 어떤 처리를 할 지 결정

package com.SCAR.Post;

import com.SCAR.Authentication.AccountSecurityAdapter;
import com.SCAR.Domain.Account;
import com.SCAR.web.dto.PostListResponseDto;
import com.SCAR.web.dto.PostResponseDto;
import com.SCAR.web.dto.PostSaveRequestDto;
import com.SCAR.web.dto.PostUpdateRequestDto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postsService;

    @PostMapping("/post")
    public Long save(@RequestBody PostSaveRequestDto requestDto, @AuthenticationPrincipal Account user) {
        return postsService.save(requestDto);
    }

    // 작성자 권한이 있을 때 update, delete
    @PutMapping("/post/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/post/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

    @GetMapping("/post/{id}")
    public PostResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @GetMapping("/post/list")
    public List<PostListResponseDto> findAll() {
        //findById
        return postsService.findAllDesc();
    }

}
