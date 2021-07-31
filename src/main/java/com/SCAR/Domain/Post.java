package com.SCAR.Domain;

import com.SCAR.BaseTimeEntity;
import com.SCAR.Post.PostType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

// TODO 1)등록 2)수정 3)조회 먼저 구현
// TODO 그 다음에 댓글 기능

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Length(min=5, max=10)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String short_content;  //ja

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @Enumerated(EnumType.STRING)
    private PostType type;
    //TODO type enum으로 선언만 해주면 되는 지 확인
    //TODO account createBy랑 Study study 뭐였지?

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getTitle(){ return title; }
    public void setTitle(){ this.title = title; }
    public String getContents() {
        return content;
    }
    public void setContents(String content) { this.content = content; }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}
