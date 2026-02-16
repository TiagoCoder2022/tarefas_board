package com.example.board_tarefas.service;

import com.example.board_tarefas.persistence.dao.BoardColumnDAO;
import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class BoardColumnQueryService {

    private final BoardColumnDAO boardColumnDAO;

    public BoardColumnQueryService(BoardColumnDAO boardColumnDAO) {
        this.boardColumnDAO = boardColumnDAO;
    }

    public Optional<BoardColumnEntity> findById(Long id) {
        return boardColumnDAO.findById(id);
    }
}
