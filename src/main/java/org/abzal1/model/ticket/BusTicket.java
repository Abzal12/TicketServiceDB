package org.abzal1.model.ticket;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BusTicket {

    private static int counter;

    private int id;

    private int user_id;

    private TicketType ticketType;

    private LocalDate startDate = LocalDate.now();

    private BigDecimal price;

    public BusTicket() {
        id = ++counter;
    }


}
