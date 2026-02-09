package com.example.board_tarefas.persistence.entity;
import lombok.Data;
@Data
public class BoardColumnEntity {
    private Long id;
    private String name;
    private int columnOrder;
    private BoardColumnKindEnum kind;
    private BoardEntity board;

    public BoardColumnEntity() {
    }

    public BoardColumnEntity(Long id, String name, int columnOrder, BoardColumnKindEnum kind, BoardEntity board) {
        this.id = id;
        this.name = name;
        this.columnOrder = columnOrder;
        this.kind = kind;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColumnOrder() {
        return columnOrder;
    }

    public BoardColumnKindEnum getKind() {
        return kind;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColumnOrder(int columnOrder) {
        this.columnOrder = columnOrder;
    }

    public void setKind(BoardColumnKindEnum kind) {
        this.kind = kind;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }
}
