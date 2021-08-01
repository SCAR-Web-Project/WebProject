// Service 는 알맞은 정보를 가공하여 Controller 에게 데이터를 넘긴다.
// Controller 는 Service 의 결과물을 Client 에게 전달해준다.

package com.SCAR.Post;

import com.SCAR.Domain.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;

    public ResponseEntity<Post> processNewPost(PostSaveRequestDto postSaveRequestDto) {
        return saveNewPost(postSaveRequestDto);
    }

    private ResponseEntity<Post> saveNewPost(PostSaveRequestDto postSaveRequestDto) {
        Post savedPost = postRepository.save(
                postSaveRequestDto.toEntity()
        );
        return ResponseEntity.ok(savedPost);
    }

    public Long save(PostSaveRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(Long id, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + id));

        post.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public void delete (Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + id));

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + id));

        return new PostResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllDesc() {
        return postRepository.findAll().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }
}
