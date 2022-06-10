package io.github.toberocat.actions;

import io.github.toberocat.stack.CappedStackListener;

public interface Action extends CappedStackListener {
    void undo();
    void redo();
}
