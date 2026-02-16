package com.example.board_tarefas.persistence.entity;

import lombok.*;

import java.util.List;

@Data
public class BoardEntity {
    private Long id;
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardsColumns;

    public BoardEntity() {
    }

    public BoardEntity(Long id, String name, List<BoardColumnEntity> boardsColumns) {
        this.id = id;
        this.name = name;
        this.boardsColumns = boardsColumns;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBoardsColumns(List<BoardColumnEntity> boardsColumns) {
        this.boardsColumns = boardsColumns;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<BoardColumnEntity> getBoardsColumns() {
        return boardsColumns;
    }
}
