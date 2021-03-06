//계층 간 데이터 교환을 위한 객체

package com.SCAR.Post;

import lombok.Getter;
import com.SCAR.Domain.Post;
import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String title;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.modifiedDate = entity.getLastModifiedAt();
    }
}
