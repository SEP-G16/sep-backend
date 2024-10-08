package com.devx.auth_service.dto;

import java.util.Set;

public record ProfileResponse(String username, Set<String> roles) {
}
