package com.example.board_tarefas.service;

import com.example.board_tarefas.dto.BoardColumnInfoDTO;
import com.example.board_tarefas.exception.CardBlockedException;
import com.example.board_tarefas.exception.CardFinishedException;
import com.example.board_tarefas.exception.EntityNotFoundException;
import com.example.board_tarefas.persistence.dao.BlockDAO;
import com.example.board_tarefas.persistence.dao.CardDAO;
import com.example.board_tarefas.persistence.entity.CardEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.board_tarefas.persistence.entity.BoardColumnKindEnum.CANCELED;
import static com.example.board_tarefas.persistence.entity.BoardColumnKindEnum.FINAL;

@Service
public class CardService {
    private final CardDAO cardDAO;
    private final BlockDAO blockDAO;

    public CardService(CardDAO cardDAO, BlockDAO blockDAO) {
        this.cardDAO = cardDAO;
        this.blockDAO = blockDAO;
    }

    public CardEntity insert(CardEntity entity) {
        return cardDAO.insert(entity);
    }

    public void moveToNextColumn(Long cardId, List<BoardColumnInfoDTO> boardColumnsInfo) {
        var optional = cardDAO.findById(cardId);
        var dto = optional.orElseThrow(
                () -> new EntityNotFoundException("O card ded id %s nao foi encontrado".formatted(cardId))
        );
        if (dto.blocked()) {
            var message = "O card %s está bloqueado, é necessário desbloquear-lo para movelo".formatted(cardId);
            throw new CardBlockedException(message);
        }
        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.columnId()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("O card informado pertence a outro board"));
        if (currentColumn.kind().equals(FINAL)) {
            throw new CardFinishedException("O card já foi finalizados");
        }
        var nextColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.order() == currentColumn.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
        cardDAO.moveToColumn(nextColumn.id(), cardId);
    }

    public void cancel(Long cardId, Long cancelColumnId, List<BoardColumnInfoDTO> boardColumnsInfo) {
        var optional = cardDAO.findById(cardId);
        var dto = optional.orElseThrow(
                () -> new EntityNotFoundException("O card ded id %s nao foi encontrado".formatted(cardId))
        );
        if (dto.blocked()) {
            var message = "O card %s está bloqueado, é necessário desbloquear-lo para movelo".formatted(cardId);
            throw new CardBlockedException(message);
        }
        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.columnId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
        if (currentColumn.kind().equals(FINAL)) {
            throw new CardFinishedException("O card já foi finalizados");
        }
        boardColumnsInfo.stream()
                .filter(bc -> bc.order() == currentColumn.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
        cardDAO.moveToColumn(cancelColumnId, cardId);
    }

    public void block(Long id, String reason, List<BoardColumnInfoDTO> boardColumnInfo) {
        var optional = cardDAO.findById(id);
        var dto = optional.orElseThrow(
                () -> new EntityNotFoundException("O card ded id %s nao foi encontrado".formatted(id))
        );
        if (dto.blocked()) {
            var message = "O card %s já está bloqueado".formatted(id);
            throw new CardBlockedException(message);
        }
        var currentColumn = boardColumnInfo.stream()
                .filter(bc -> bc.id().equals(dto.columnId()))
                .findFirst()
                .orElseThrow();
        if (currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCELED)) {
            var message = "O card está em uma coluna do tipo %s e nao pode ser bloqueado".formatted(currentColumn.kind());
            throw new IllegalStateException(message);
        }
        blockDAO.block(reason, id);
    }
}
