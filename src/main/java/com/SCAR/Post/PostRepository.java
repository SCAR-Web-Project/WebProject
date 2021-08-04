package com.SCAR.Post;

import com.SCAR.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//인터페이스에 미리 검색 메소드를 정의 해 두는 것


}

