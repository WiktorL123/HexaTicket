package com.pjatk.core.command;

import lombok.AllArgsConstructor;

public record BookTicketCommand (String ownerEmail, String ownerName, String eventId){
}
