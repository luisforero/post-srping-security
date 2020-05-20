package com.example.postspringsecurity.repositories;

import com.example.postspringsecurity.models.Post;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, ObjectId> {
}
