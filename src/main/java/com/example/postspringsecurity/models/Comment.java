package com.example.postspringsecurity.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment {
    private String user;
    private String body;
    private Date date;
}
