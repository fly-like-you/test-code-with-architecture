package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;

import java.util.Optional;

public class FakePostRepository implements PostRepository {
    @Override
    public Optional<Post> findById(long postId) {
        return Optional.empty();
    }

    @Override
    public Post save(Post post) {
        return null;
    }
}
