package io.github.toberocat.gui.image;

import io.github.toberocat.actions.ActionLog;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Mathf;
import io.github.toberocat.utils.Mathi;
import io.github.toberocat.utils.Utility;
import io.github.toberocat.utils.selection.LabelDragMode;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static io.github.toberocat.utils.Mathf.clampRound;

public class SelectionHandle {
    private static SelectionHandle HANDLE;
    private final ArrayList<LabelSelection> selections;
    private Rectangle current;
    private LabelSelection selected;
    private LabelDragMode dragMode = LabelDragMode.NONE;

    private Point start;
    private Point distanceBetweenStartAndRect;


    private SelectionHandle() {
        this.selections = new ArrayList<>();
        this.current = new Rectangle();
    }

    public static SelectionHandle handle() {
        if (HANDLE == null) HANDLE = new SelectionHandle();
        return HANDLE;
    }

    public void mousePress(MouseEvent event) {
        int abX = event.getX() - ImageRenderer.instance().getScrollX();
        int abY = event.getY() - ImageRenderer.instance().getScrollY();

        int sX = Math.round(abX / ImageRenderer.instance().zoom());
        int sY = Math.round(abY / ImageRenderer.instance().zoom());

        boolean hittedSelection = false;
        for (LabelSelection selection : selections) {
            if (selection.contains(sX, sY)) {
                selected = selection;
                distanceBetweenStartAndRect = new Point(sX - selected.x(), sY - selected.y());
                hittedSelection = true;
                break;
            }
        }

        if (!hittedSelection) selected = null;

        if (selected != null) dragMode = selected.getDragMode(sX, sY);


        start = new Point(abX, abY);
    }

    public void mouseDrag(MouseEvent event) {
        if (start == null) {
            System.out.println("Drag happened, start isn't defined");
            return;
        }

        Point end = new Point(event.getX() - ImageRenderer.instance().getScrollX(),
                event.getY() - ImageRenderer.instance().getScrollY());

        if (dragMode != LabelDragMode.NONE && selected != null) {

            switch (dragMode) {
                case LEFT_TOP -> selected.shiftOrigin(getAbsoluteX(event.getX()), getAbsoluteY(event.getY()));
                case TOP -> selected.shiftOrigin(selected.x(), getAbsoluteY(event.getY()));
                case RIGHT_TOP -> {
                    selected.width(getAbsoluteX(event.getX()) - selected.x());
                    selected.shiftOrigin(selected.x(), getAbsoluteY(event.getY()));
                }
                case RIGHT -> selected.width(getAbsoluteX(event.getX()) - selected.x());
                case RIGHT_DOWN -> selected.shiftDimension(getAbsoluteX(event.getX()), getAbsoluteY(event.getY()));
                case DOWN -> selected.height(getAbsoluteY(event.getY()) - selected.y());
                case LEFT_DOWN -> {
                    selected.height(getAbsoluteY(event.getY()) - selected.y());
                    selected.shiftOrigin(getAbsoluteX(event.getX()), selected.y());
                }
                case LEFT -> selected.shiftOrigin(getAbsoluteX(event.getX()), selected.y());
                case MOVE -> {
                    selected.x(getAbsoluteX(event.getX()) - distanceBetweenStartAndRect.x);
                    selected.y(getAbsoluteY(event.getY()) - distanceBetweenStartAndRect.y);
                }
            }

            updateFileContent();
            return;
        }


        Image image = ImageRenderer.instance().getImage();
        float width = image.getWidth(null) * ImageRenderer.instance().zoom(),
                height = image.getHeight(null) * ImageRenderer.instance().zoom();

        int startX = clampRound(Math.min(start.x, end.x), 0, width);
        int startY = clampRound(Math.min(start.y, end.y), 0, height);

        int endX = clampRound(Math.max(start.x, end.x), 0, width);
        int endY = clampRound(Math.max(start.y, end.y), 0, height);

        current = new Rectangle(startX, startY, endX - startX, endY - startY);
    }

    private int getAbsoluteX(int x) {
        return Math.round((x - ImageRenderer.instance().getScrollX()) / ImageRenderer.instance().zoom());
    }

    private int getAbsoluteY(int y) {
        return Math.round((y - ImageRenderer.instance().getScrollY()) / ImageRenderer.instance().zoom());
    }

    public void accept() {
        if (current.width <= 5 || current.height <= 5) return;

        LabelSelection selection = LabelSelection.fromRect(selection());
        ActionLog.logSelection(selection);

        selections.add(selection);
        selected = selection;
        updateFileContent();
        current = new Rectangle();

        EventListener.dragX = 0;
        EventListener.dragY = 0;
        EventListener.x = 0;
        EventListener.y = 0;
    }

    public void cancel() {
        if (current().isEmpty()) selected = null;
        else {
            current = new Rectangle();
            EventListener.dragX = 0;
            EventListener.dragY = 0;
            EventListener.x = 0;
            EventListener.y = 0;
        }
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


    public LabelSelection getSelected() {
        return selected;
    }

    public SelectionHandle setSelected(LabelSelection selected) {
        this.selected = selected;
        dragMode = LabelDragMode.NONE;
        return this;
    }

    public void reloadFromDisk() {
        selections.clear();
        selections.addAll(Utility.readLabel(EventListener.IMAGE_BATCH.getCurrent().c().getName()));
    }

    public void updateFileContent() {
        DataUtility.createFile(EventListener.IMAGE_BATCH.getCurrent().c());
    }
}
