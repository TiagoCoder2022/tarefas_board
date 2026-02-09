package com.example.board_tarefas.service;

import com.example.board_tarefas.persistence.dao.BoardColumnDAO;
import com.example.board_tarefas.persistence.dao.BoardDAO;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;

    public Optional<BoardEntity> findById(Long id) {
        return boardDAO.findById(id)
                .map(board -> {
                    board.setBoardsColumns(
                            boardColumnDAO.findByBoardId(board.getId())
                    );
                    return board;
                });
    }
}
