package io.github.toberocat.gui.listener;

import io.github.toberocat.actions.ActionLog;
import io.github.toberocat.gui.image.ImageBatch;
import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.image.SelectionHandle;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Utility;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EventListener implements KeyListener, MouseListener, MouseMotionListener, ComponentListener {

    public static ImageBatch IMAGE_BATCH;

    public static int x, y, dragX, dragY;

    public EventListener(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);

        component.addKeyListener(this);

        component.addComponentListener(this);

        IMAGE_BATCH = new ImageBatch();
    }

    private void loadImage() {
        DataUtility.createFile(IMAGE_BATCH.getCurrent().c(), null);

        File file = IMAGE_BATCH.read().c();
        Image image = IMAGE_BATCH.getCurrent().t();

        ImageRenderer.instance().drawImage(image, Utility.readLabel(file.getName()), file.getName());
    }

    private void loadPreviousImage() {
        DataUtility.createFile(IMAGE_BATCH.getCurrent().c(), null);

        File file = IMAGE_BATCH.readPrevious().c();
        Image image = IMAGE_BATCH.getCurrent().t();

        ImageRenderer.instance().drawImage(image, Utility.readLabel(file.getName()), file.getName());
    }

    private void deleteEntireLabels() {
        File file = IMAGE_BATCH.getCurrent().c();
        if (file == null || !DataUtility.getLabelFile(file).exists()) return;

        ActionLog.logRemoval(DataUtility.getLabelFile(file));
        SelectionHandle.handle().getSelections().clear();
        DataUtility.removeContent(file);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 39 -> loadImage();
            case 37 -> loadPreviousImage();
            case 27 -> SelectionHandle.handle().cancel();
            case 8, 127 -> deleteEntireLabels();
            case 90 -> {
                if (e.isControlDown()) ActionLog.undo();
            }
            case 89 -> {
                if (e.isControlDown()) ActionLog.redo();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        SelectionHandle.handle().mousePress(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SelectionHandle.handle().current() == null) return;
        SelectionHandle.handle().accept();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        SelectionHandle.handle().mouseDrag(e);
        dragX = e.getX();
        dragY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        dragX = e.getX();

        y = e.getY();
        dragY = e.getY();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        ImageRenderer.instance().fitImage();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
