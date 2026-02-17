package com.example.board_tarefas.dto;

import com.example.board_tarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}
