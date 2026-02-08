package com.example.board_tarefas.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardEntity {
    private Long id;
    private String name;
    private List<BoardColumnEntity> boardsColumns;
}
