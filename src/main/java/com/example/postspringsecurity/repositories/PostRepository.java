package com.example.postspringsecurity.repositories;

import com.example.postspringsecurity.models.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, String> {
    @Query("{ '_id': ObjectId( ?0 )}")
    Optional<Post> findByObjectId(String id);
}
