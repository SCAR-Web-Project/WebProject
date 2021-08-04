package com.SCAR.Domain;
import com.SCAR.BaseTimeEntity;
import com.SCAR.Post.PostType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean blinded;

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getContents() {
        return content;
    }
    public void setContents(String content) { this.content = content; }
}
