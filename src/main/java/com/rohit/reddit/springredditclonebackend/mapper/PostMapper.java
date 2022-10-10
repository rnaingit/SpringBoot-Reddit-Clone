package com.rohit.reddit.springredditclonebackend.mapper;

import com.rohit.reddit.springredditclonebackend.dto.PostRequest;
import com.rohit.reddit.springredditclonebackend.dto.PostResponse;
import com.rohit.reddit.springredditclonebackend.model.Post;
import com.rohit.reddit.springredditclonebackend.model.Subreddit;
import com.rohit.reddit.springredditclonebackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}