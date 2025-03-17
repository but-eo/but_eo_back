package org.example.but_eo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chatting {

    @Id
    private String chat_Id;

    private String state;

    private String title;

    private LocalDateTime created_at;

    @ManyToMany(mappedBy = "chattings")
    private Set<Users> users = new HashSet<>();
}
