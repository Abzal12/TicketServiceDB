package org.abzal1.model.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private LocalDate startDate = LocalDate.now();

    public User(String name) {
        this.name = name;
    }
}
