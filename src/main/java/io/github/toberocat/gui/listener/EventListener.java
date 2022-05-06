package io.github.toberocat.gui.listener;

import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.image.SelectionHandle;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Utility;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EventListener implements KeyListener, MouseListener, MouseMotionListener, ComponentListener {

    public static ImageBatch IMAGE_BATCH;

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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 39 -> loadImage();
            case 37 -> loadPreviousImage();
            case 27 -> SelectionHandle.handle().cancel();
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
