package com.example.board_tarefas.ui;

import com.example.board_tarefas.dto.BoardColumnInfoDTO;
import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import com.example.board_tarefas.persistence.entity.CardEntity;
import com.example.board_tarefas.service.BoardColumnQueryService;
import com.example.board_tarefas.service.BoardQueryService;
import com.example.board_tarefas.service.CardQueryService;
import com.example.board_tarefas.service.CardService;

import java.util.Scanner;

public class BoardMenu {
    private Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity boardEntity;
    private final BoardQueryService boardQueryService;
    private final BoardColumnQueryService boardColumnQueryService;
    private final CardQueryService cardQueryService;
    private final CardService cardService;

    public BoardMenu(
            BoardEntity boardEntity,
            BoardQueryService boardQueryService,
            BoardColumnQueryService boardColumnQueryService,
            CardQueryService cardQueryService,
            CardService cardService
    ) {
        this.boardEntity = boardEntity;
        this.boardQueryService = boardQueryService;
        this.boardColumnQueryService = boardColumnQueryService;
        this.cardQueryService = cardQueryService;
        this.cardService = cardService;
    }
    public void execute() {
        System.out.printf("Bem vindo ao board %s, slecione a operaçao desejada\n", boardEntity.getId());
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
        var card = new CardEntity();
        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());
        System.out.println("Informe a descricao do card");
        card.setDescription(scanner.next());

        card.setBoardColumn(boardEntity.getInitialColumn());
        cardService.insert(card);
    }

    private void moveCardToNextColumn() {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        var cardId = scanner.nextLong();
        var boardColumnsInfo = boardEntity.getBoardsColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getColumnOrder(), bc.getKind()))
                .toList();
        cardService.moveToNextColumn(cardId, boardColumnsInfo);
    }

    private void blockCard() {
        System.out.println("Informe o id do card que será bloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do bloqueio do card");
        var reason = scanner.next();
    }

    private void unblockCard() {
    }

    private void cancelCard() {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        var cancelColumn = boardEntity.getCancelColumn();
        var boardColumnsInfo = boardEntity.getBoardsColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getColumnOrder(), bc.getKind()))
                .toList();
        cardService.cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
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
            co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescricao: %s \n", ca.getId(), ca.getTitle(), ca.getDescription()));
        });
    }

    private void showCard() {
        System.out.println("Informe o id do card que deseja visualizar");
        var selectedCardId = scanner.nextLong();
        cardQueryService.findById(selectedCardId)
                .ifPresentOrElse(
                c -> {
                    System.out.printf("Card %s - %s.\n", c.id(), c.title());
                    System.out.printf("Descricao: %s.\n", c.description());
                    System.out.println(c.blocked() ? "Está bloqueado. Motivo: " + c.blockReason() : "Nao está desbloqueado");
                    System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                    System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                },
                () -> System.out.printf("Nao existe um card com o id %s\n", selectedCardId)
        );
    }
}
