package com.pjatk.core.command;

public record BookTicketCommand (String ownerEmail, String ownerName, String eventId){
}
