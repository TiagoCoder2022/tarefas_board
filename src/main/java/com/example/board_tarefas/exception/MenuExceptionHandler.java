package com.example.board_tarefas.exception;

import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
public class MenuExceptionHandler {
    public void execute(Runnable action) {
        try {
            action.run();
        } catch (CardBlockedException e) {
            System.out.println("⚠️  " + e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (CardFinishedException e) {
            System.out.println("🚫 " + e.getMessage());
        } catch (IllegalStateException e) {
                System.out.println("🚫 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("💥 Erro inesperado: " + e.getMessage());
        }
    }
}
