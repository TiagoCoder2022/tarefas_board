package com.example.board_tarefas.service;

import com.example.board_tarefas.persistence.dao.BoardColumnDAO;
import com.example.board_tarefas.persistence.dao.BoardDAO;
import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BoardService {

    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;

    public BoardService(BoardDAO boardDAO, BoardColumnDAO boardColumnDAO) {
        this.boardDAO = boardDAO;
        this.boardColumnDAO = boardColumnDAO;
    }

    @Transactional
    public BoardEntity create(BoardEntity board) {
        BoardEntity savedBoard = boardDAO.insert(board);

        for (BoardColumnEntity column : board.getBoardsColumns()) {
            column.setBoard(savedBoard);
            boardColumnDAO.insert(column);
        }

        savedBoard.setBoardsColumns(board.getBoardsColumns());
        return savedBoard;
    }

    @Transactional
    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id não pode ser nulo");
        }

        if (!boardDAO.exists(id)) {
            return false;
        }

        boardDAO.delete(id);
        return true;
    }
}