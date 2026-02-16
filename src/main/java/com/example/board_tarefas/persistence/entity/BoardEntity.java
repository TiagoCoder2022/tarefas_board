package com.example.board_tarefas.persistence.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.board_tarefas.persistence.entity.BoardColumnKindEnum.INITIAL;

@Data
public class BoardEntity {
    private Long id;
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardsColumns = new ArrayList<>();

    public BoardEntity() {
    }

    public BoardEntity(Long id, String name, List<BoardColumnEntity> boardsColumns) {
        this.id = id;
        this.name = name;
        this.boardsColumns = boardsColumns;
    }

    public BoardColumnEntity getInitialColumn() {
        return boardsColumns.stream()
                .filter(bc -> bc.getKind().equals(INITIAL))
                .findFirst().orElseThrow();

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
