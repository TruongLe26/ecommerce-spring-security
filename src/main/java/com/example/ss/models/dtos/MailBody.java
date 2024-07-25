package com.example.ss.models.dtos;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
