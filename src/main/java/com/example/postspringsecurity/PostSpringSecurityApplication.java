package com.example.postspringsecurity;

import com.example.postspringsecurity.auth.models.Users;
import com.example.postspringsecurity.auth.repositories.UsersRepository;
import com.example.postspringsecurity.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PostSpringSecurityApplication implements
        CommandLineRunner {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(PostSpringSecurityApplication.class, args);
    }


    // This is just used to initialize some users into the Database.
    // We should define a different approach like use a .ylm file or something else.
    @Override
    public void run(String... args) throws Exception {
        usersRepository.deleteAll();

        Users luwifor = new Users().builder()
                .username("luwifor")
                .password(passwordEncoder.encode("symphony"))
                .role(ApplicationUserRole.VIEWER.name())
                .build();
        usersRepository.save(luwifor);

        Users katya = new Users().builder()
                .username("katya")
                .password(passwordEncoder.encode("symphony"))
                .role(ApplicationUserRole.AUTHOR.name())
                .build();
        usersRepository.save(katya);

        Users ludwig = new Users().builder()
                .username("ludwig")
                .password(passwordEncoder.encode("symphony"))
                .role(ApplicationUserRole.ADMIN.name())
                .build();
        usersRepository.save(ludwig);
    }
}
