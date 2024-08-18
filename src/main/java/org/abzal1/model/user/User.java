package org.abzal1.model.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {
    private static int counter;
    private int id;
    private String name;
    private LocalDate startDate = LocalDate.now();

    public User() {
        id = ++counter;
    }
}
