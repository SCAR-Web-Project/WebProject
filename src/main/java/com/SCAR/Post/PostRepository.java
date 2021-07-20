package com.SCAR.Post;

import com.SCAR.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

   // @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
   // List<Post> findAllDesc();

}

//책 참고