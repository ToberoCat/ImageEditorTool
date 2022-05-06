package io.github.toberocat.gui.image;

import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SelectionHandle {
    private static SelectionHandle HANDLE;
    private final ArrayList<LabelSelection> selections;
    private Rectangle current;

    private Point start;

    private SelectionHandle() {
        this.selections = new ArrayList<>();
        this.current = new Rectangle();
    }

    public static SelectionHandle handle() {
        if (HANDLE == null) HANDLE = new SelectionHandle();
        return HANDLE;
    }

    public void mousePress(MouseEvent event) {
        start = new Point(event.getX() - ImageRenderer.instance().getScrollX(),
                event.getY() - ImageRenderer.instance().getScrollY());
    }

    public void mouseDrag(MouseEvent event) {
        Point end = new Point(event.getX() - ImageRenderer.instance().getScrollX(),
                event.getY() - ImageRenderer.instance().getScrollY());

        int startX = Math.min(start.x, end.x);
        int startY = Math.min(start.y, end.y);

        int endX = Math.max(start.x, end.x);
        int endY = Math.max(start.y, end.y);

        current = new Rectangle(startX, startY, endX - startX, endY - startY);
    }

    public void accept() {
        if (current.width == 0 || current.height == 0) return;

        selections.add(LabelSelection.fromRect(selection())
        );
        DataUtility.createFile(EventListener.IMAGE_BATCH.getCurrent().c(), current);
        current = new Rectangle();
    }

    public void cancel() {
        current = new Rectangle();
    }

    public Rectangle current() {
        return current;
    }

    public ArrayList<LabelSelection> getSelections() {
        return selections;
    }

    public Rectangle selection() {
        return new Rectangle(
                Math.round(current.x / ImageRenderer.instance().zoom()),
                Math.round(current.y / ImageRenderer.instance().zoom()),
                Math.round(current.width / ImageRenderer.instance().zoom()),
                Math.round(current.height / ImageRenderer.instance().zoom()));
    }
}
