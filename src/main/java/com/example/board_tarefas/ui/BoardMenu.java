package com.example.board_tarefas.ui;

import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import com.example.board_tarefas.service.BoardColumnQueryService;
import com.example.board_tarefas.service.BoardQueryService;

import java.util.Scanner;
public class BoardMenu {
    private Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity boardEntity;
    private final BoardQueryService boardQueryService;
    private final BoardColumnQueryService boardColumnQueryService;

    public BoardMenu(
            BoardEntity boardEntity,
            BoardQueryService boardQueryService,
            BoardColumnQueryService boardColumnQueryService) {
        this.boardEntity = boardEntity;
        this.boardQueryService = boardQueryService;
        this.boardColumnQueryService = boardColumnQueryService;
    }
    public void execute() {
        System.out.printf("Bem vindo ao board %s, slecione a operaçao desejada", boardEntity.getId());
        var option = -1;

        while (option != 9) {
            System.out.println("1 - Criar um card");
            System.out.println("2 - Mover um card");
            System.out.println("3 - Bloquear um card");
            System.out.println("4 - Desbloquear um card");
            System.out.println("5 - Cancelar um card");
            System.out.println("6 - Visualizar board");
            System.out.println("7 - Visualizar colunas com cards");
            System.out.println("8 - Ver cards");
            System.out.println("9 - Voltar para o menu anterior");
            System.out.println("10 - Sair");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> createCard();
                case 2 -> moveCardToNextColumn();
                case 3 -> blockCard();
                case 4 -> unblockCard();
                case 5 -> cancelCard();
                case 6 -> showBoard();
                case 7 -> showColumn();
                case 8 -> showCard();
                case 9 -> System.out.println("Voltando para o menu anterior");
                case 10 -> System.exit(0);
                default -> System.out.println("Opcao invalida, informe uma opcao do menu");
            }
        }
    }

    private void createCard() {
    }

    private void moveCardToNextColumn() {
    }

    private void blockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() {
        var optional = boardQueryService.showBoardDetails(boardEntity.getId());
        optional.ifPresent(b -> {
            System.out.printf("Board [%s,%s]\n", b.id(), b.name());
            b.columns().forEach(c -> {
                System.out.printf("Coluns [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount());
            });
        });
    }

    private void showColumn() {
        var columnsIds = boardEntity.getBoardsColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumn = -1L;
        while (!columnsIds.contains(selectedColumn)) {
            System.out.printf("Escolha uma coluna do board %s\n", boardEntity.getName());
            boardEntity.getBoardsColumns().forEach(c -> System.out.printf("%s - %s\n", c.getId(), c.getName()));
            selectedColumn = scanner.nextLong();
        }
        var column = boardColumnQueryService.findById(selectedColumn);
        column.ifPresent( co -> {
            System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
            co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescricao: %s", ca.getId(), ca.getTitle(), ca.getDescription()));
        });
    }

    private void showCard() {
    }
}
