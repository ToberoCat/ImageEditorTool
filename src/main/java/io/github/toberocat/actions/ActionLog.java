package io.github.toberocat.actions;

import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.stack.CappedStack;
import io.github.toberocat.utils.selection.LabelSelection;

import java.io.File;

public class ActionLog {
    private static final CappedStack<Action> HISTORY_UNDO = new CappedStack<>(50);
    private static final CappedStack<Action> HISTORY_REDO = new CappedStack<>(50);

    public static void logSelection(LabelSelection selection) {
        File image = EventListener.IMAGE_BATCH.getCurrent().c();
        HISTORY_UNDO.push(new SelectionAction(selection, image.getName()));
    }

    public static void logRemoval(File removedFile) {
        HISTORY_UNDO.push(new DeleteAction(removedFile));
    }

    public static void cleanup() {
        while (!HISTORY_UNDO.isEmpty()) {
            Action action = HISTORY_UNDO.pop();
            if (action == null) continue;

            action.dispose();
        }

        while (!HISTORY_REDO.isEmpty()) {
            Action action = HISTORY_REDO.pop();
            if (action == null) continue;

            action.dispose();
        }

        HISTORY_UNDO.clear();
        HISTORY_REDO.clear();
    }

    public static void undo() {
        if (HISTORY_UNDO.empty()) return;

        Action action = HISTORY_UNDO.pop();
        if (action == null) return;

        HISTORY_REDO.push(action);
        action.undo();
    }

    public static void redo() {
        if (HISTORY_REDO.empty()) return;

        Action action = HISTORY_REDO.pop();
        if (action == null) return;

        HISTORY_UNDO.push(action);
        action.redo();
    }
}
