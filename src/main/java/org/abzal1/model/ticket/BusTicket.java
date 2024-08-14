package org.abzal1.model.ticket;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BusTicket {

    private int id;

    private int user_id;

    private String ticketClass;

    private TicketType ticketType;

    private LocalDate startDate = LocalDate.now();

    private BigDecimal price;

    public BusTicket(String ticketClass, TicketType ticketType, BigDecimal price) {
        this.ticketClass = ticketClass;
        this.ticketType = ticketType;
        this.price = price;
    }
}
