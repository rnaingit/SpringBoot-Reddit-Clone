package com.rohit.reddit.springredditclonebackend.mapper;
import com.rohit.reddit.springredditclonebackend.dto.CommentsDto;
import com.rohit.reddit.springredditclonebackend.model.Comment;
import com.rohit.reddit.springredditclonebackend.model.Post;
import com.rohit.reddit.springredditclonebackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}