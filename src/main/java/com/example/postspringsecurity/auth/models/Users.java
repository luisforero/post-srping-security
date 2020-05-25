package com.example.postspringsecurity.auth.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class Users {
    @Id
    private ObjectId _id;

    public String username;
    public String password;
    public String role;
}
