package com.rohit.reddit.springredditclonebackend.service;
import com.rohit.reddit.springredditclonebackend.dto.PostRequest;
import com.rohit.reddit.springredditclonebackend.dto.PostResponse;
import com.rohit.reddit.springredditclonebackend.exceptions.PostNotFoundException;
import com.rohit.reddit.springredditclonebackend.exceptions.SubredditNotFoundException;
import com.rohit.reddit.springredditclonebackend.mapper.PostMapper;
import com.rohit.reddit.springredditclonebackend.model.Post;
import com.rohit.reddit.springredditclonebackend.model.Subreddit;
import com.rohit.reddit.springredditclonebackend.model.User;
import com.rohit.reddit.springredditclonebackend.repository.PostRepository;
import com.rohit.reddit.springredditclonebackend.repository.SubredditRepository;
import com.rohit.reddit.springredditclonebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    public void save(PostRequest postRequest) {
        System.out.println("inside post service");
        System.out.println(authService.getCurrentUser());
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        System.out.println("Id"+subredditId);
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        System.out.println(subreddit.getName());
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        System.out.println("username"+username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}