package com.example.demo.post.controller.port;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Post findPostById(long id);

    Post update(long id, PostUpdate postUpdate);
}
