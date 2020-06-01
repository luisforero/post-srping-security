package com.example.postspringsecurity.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
    private String name;
    private String status;
}
