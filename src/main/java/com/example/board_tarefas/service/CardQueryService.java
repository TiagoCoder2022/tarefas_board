package com.example.board_tarefas.service;

import com.example.board_tarefas.dto.CardDetailsDTO;
import com.example.board_tarefas.persistence.dao.CardDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CardQueryService {

    private final CardDAO cardDAO;

    public CardQueryService(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }
    public Optional<CardDetailsDTO> findById(Long id) {
        return cardDAO.findById(id);
    }
}
