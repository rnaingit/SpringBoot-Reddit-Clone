package com.rohit.reddit.springredditclonebackend.service;

import com.rohit.reddit.springredditclonebackend.dto.SubredditDto;
import com.rohit.reddit.springredditclonebackend.exceptions.SpringRedditException;
import com.rohit.reddit.springredditclonebackend.exceptions.SubredditNotFoundException;
import com.rohit.reddit.springredditclonebackend.mapper.SubredditMapper;
import com.rohit.reddit.springredditclonebackend.model.Subreddit;
import com.rohit.reddit.springredditclonebackend.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

import static java.time.Instant.now;


@Service
@Slf4j
@AllArgsConstructor
public class SubredditService {
//
//    private  final SubredditRepository subredditRepository;
//
//    @Transactional
//    public SubredditDto save(SubredditDto subredditDto){
//        Subreddit subreddit = mapSubredditDto(subredditDto);
//        Subreddit save =subredditRepository.save(subreddit);
//        subredditDto.setId(save.getId());
//        return subredditDto;
//
//    }
//
//    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//        return Subreddit.builder().name(subredditDto.getName())
//                .description(subredditDto.getDescription())
//                .build();
//    }
//
//    @Transactional(readOnly = true)
//    public List<SubredditDto> getAll() {
//        subredditRepository.findAll();
//        return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
//
//    }
//
//    private SubredditDto  mapToDto(Subreddit subreddit) {
//        return SubredditDto.builder().id(subreddit.getId())
//                .name(subreddit.getName())
//                .description(subreddit.getDescription())
//                .build();
//    }
//
//    public SubredditDto getSubreddit(Long id) {
//        Subreddit subreddit =subredditRepository.findById(id)
//                .orElseThrow(()-> new SpringRedditException("No subreddit found with this id" +id));
//        return SubredditDto.builder().id(subreddit.getId())
//                .name(subreddit.getName())
//                .description(subreddit.getDescription())
//                .build();
//
//    }

//
//    private final SubredditRepository subredditRepository;
//    private final SubredditMapper subredditMapper;
//
//    @Transactional
//    public SubredditDto save(SubredditDto subredditDto) {
//        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
//        subredditDto.setId(save.getId());
//        return subredditDto;
//    }
//
//    @Transactional(readOnly = true)
//    public List<SubredditDto> getAll() {
//        return subredditRepository.findAll()
//                .stream()
//                .map(subredditMapper::mapSubredditToDto)
//                .collect(toList());
//    }
//
//    public SubredditDto getSubreddit(Long id) {
//        Subreddit subreddit = subredditRepository.findById(id)
//                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
//        return subredditMapper.mapSubredditToDto(subreddit);
//    }

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));
        return mapToDto(subreddit);
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapToSubreddit(SubredditDto subredditDto) {
        return Subreddit.builder().name("/r/" + subredditDto.getName())
                .description(subredditDto.getDescription())
                .user(authService.getCurrentUser())
                .createdDate(now()).build();
    }

}
