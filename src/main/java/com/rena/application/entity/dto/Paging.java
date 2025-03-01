package com.rena.application.entity.dto;

import java.util.List;

public record Paging<T>(Long total, List<T> content) {
}
