package com.example.board_tarefas.exception;

import jakarta.persistence.EntityNotFoundException;

public class CardBlockedException extends RuntimeException {
    public CardBlockedException(String message) { super(message);}
}
