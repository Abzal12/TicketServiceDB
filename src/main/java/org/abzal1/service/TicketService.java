package org.abzal1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.abzal1.model.ticket.Ticket;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final ResourceLoader resourceLoader;

    public TicketService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ArrayList<Ticket> loadTicketsFromFile(String filePath) {
        List<Ticket> tickets = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            tickets = reader.lines()
                    .map(line -> {
                        try {
                            return objectMapper.readValue(line, Ticket.class);
                        } catch (JsonProcessingException e) {
                            System.err.println("Error processing JSON: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return (ArrayList<Ticket>) tickets;
    }
}

