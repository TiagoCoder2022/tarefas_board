package com.example.board_tarefas.service;

import com.example.board_tarefas.persistence.dao.CardDAO;
import com.example.board_tarefas.persistence.entity.CardEntity;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    private final CardDAO cardDAO;

    public CardService(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }
    public CardEntity insert(CardEntity entity) {
        return cardDAO.insert(entity);
    }
}
