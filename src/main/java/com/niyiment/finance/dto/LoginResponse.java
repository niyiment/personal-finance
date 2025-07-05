package com.niyiment.finance.dto;

public record LoginResponse(
        String token,
        long expiresIn
) {
}
