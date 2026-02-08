package com.example.board_tarefas.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardColumnEntity {
    private Long id;
    private String name;
    private int columnOrder;
    private BoardColumnKindEnum kind;
    private BoardEntity board;
}
