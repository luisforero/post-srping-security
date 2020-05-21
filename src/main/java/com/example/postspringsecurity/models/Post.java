package com.example.postspringsecurity.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String title, body;
    private Date date;
    private Date lastUpdate;
    private Category category;
    private List<String> tags;
    private User user;
    private Integer views;
    private List<Comment> comments;
}
