package com.rohit.reddit.springredditclonebackend.repository;

import com.rohit.reddit.springredditclonebackend.model.Comment;
import com.rohit.reddit.springredditclonebackend.model.Post;
import com.rohit.reddit.springredditclonebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}