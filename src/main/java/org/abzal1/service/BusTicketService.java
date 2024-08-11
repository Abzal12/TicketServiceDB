package org.abzal1.service;

import org.abzal1.model.ticket.BusTicket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BusTicketService {
    public static List<BusTicket> getObjectsFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, lines.get(i).replaceAll("[\u201C\u201D]", "\""));
        }

        return convertToBusTicketObject(lines);
    }

    private static List<BusTicket> convertToBusTicketObject(List<String> lines) {
        List<BusTicket> busTicketObjectList = new ArrayList<>();
        long lineNum = 0;
        for (String line : lines) {
            lineNum++;
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                BusTicket busTicketObject = mapper.readValue(line, BusTicket.class);
                busTicketObjectList.add(busTicketObject);
            } catch (JsonProcessingException e) {
                System.err.println("Something went wrong while processing json in ticketData.txt file in the line of "
                        + lineNum + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }
        return busTicketObjectList;
    }
}
