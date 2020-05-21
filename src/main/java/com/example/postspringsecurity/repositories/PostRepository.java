package com.example.postspringsecurity.repositories;

import com.example.postspringsecurity.models.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, String> {
    @Query("{ '_id': ObjectId( ?0 )}")
    public Post findByObjectId(String id);
}
