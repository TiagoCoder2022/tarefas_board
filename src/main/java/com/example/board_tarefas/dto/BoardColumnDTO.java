package com.example.board_tarefas.dto;

import com.example.board_tarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(Long id, String name, BoardColumnKindEnum kind, int cardsAmount) {
}
