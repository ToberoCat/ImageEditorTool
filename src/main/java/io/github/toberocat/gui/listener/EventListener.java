package io.github.toberocat.gui.listener;

import io.github.toberocat.Launch;
import io.github.toberocat.actions.ActionLog;
import io.github.toberocat.gui.image.ImageBatch;
import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.image.SelectionHandle;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Utility;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.toberocat.utils.DataUtility.join;
import static io.github.toberocat.utils.DataUtility.saveObject;

public class EventListener implements KeyListener, MouseListener, MouseMotionListener, ComponentListener {

    public static ImageBatch IMAGE_BATCH;

    public static int x, y, dragX, dragY;

    public EventListener(Canvas component) throws IOException {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);

        component.addKeyListener(this);

        component.addComponentListener(this);

        IMAGE_BATCH = new ImageBatch();
    }

    private void loadImage() {
        DataUtility.createFile(IMAGE_BATCH.getCurrent().c());

        File file = IMAGE_BATCH.read().c();
        Image image = IMAGE_BATCH.getCurrent().t();

        SelectionHandle.handle().setSelected(null);
        ImageRenderer.instance().drawImage(image, Utility.readLabel(file.getName()), file.getName());
    }

    private void loadPreviousImage() {
        DataUtility.createFile(IMAGE_BATCH.getCurrent().c());

        File file = IMAGE_BATCH.readPrevious().c();
        Image image = IMAGE_BATCH.getCurrent().t();

        SelectionHandle.handle().setSelected(null);
        ImageRenderer.instance().drawImage(image, Utility.readLabel(file.getName()), file.getName());
    }

    private void delete() {
        LabelSelection selected = SelectionHandle.handle().getSelected();
        File image = IMAGE_BATCH.getCurrent().c();
        if (image == null || !DataUtility.getLabelFile(image).exists()) return;

        if (selected == null) {

            ActionLog.logRemoval(DataUtility.getLabelFile(image));
            SelectionHandle.handle().getSelections().clear();
            DataUtility.removeContent(image);
        } else {
            ActionLog.logSelectionDeletion(selected);

            List<LabelSelection> labelSelections = Utility.readLabel(image.getName());
            saveObject(DataUtility.getLabelFile(join(Launch.ORIGINAL_IMAGES_PATH, image.getName())),
                    labelSelections.stream().filter(x -> x.x() != selected.x() || x.y() != selected.y() || x.width() != selected.width()
                            || x.height() != selected.height()).toArray(LabelSelection[]::new));

            SelectionHandle.handle().setSelected(null);
        }

        SelectionHandle.handle().reloadFromDisk();
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
            case 8, 127 -> delete();
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
