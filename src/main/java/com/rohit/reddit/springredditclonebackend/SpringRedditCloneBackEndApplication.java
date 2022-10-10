package com.rohit.reddit.springredditclonebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringRedditCloneBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRedditCloneBackEndApplication.class, args);
	}

}
