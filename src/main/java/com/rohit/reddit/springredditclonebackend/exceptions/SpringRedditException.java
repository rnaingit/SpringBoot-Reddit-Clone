package com.rohit.reddit.springredditclonebackend.exceptions;

import org.springframework.mail.MailException;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String exMessage) {
        //purpose of doing this
        //whenever the exception occurs we don't want to display the technical information to the
        //end user like null pointer exception etc. so we send understandable messages through custom
        //exceptions
        super(exMessage);
    }
}
