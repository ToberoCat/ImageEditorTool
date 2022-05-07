package io.github.toberocat.gui.image;

import io.github.toberocat.gui.EditorGui;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.loop.LoopEvent;
import io.github.toberocat.utils.Mathf;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageRenderer implements LoopEvent {

    private static final Color SELECTION_BOX_INSIDE = new Color(0, 120, 215, 120);
    private static final Color SELECTION_BOX_BORDER = new Color(76, 76, 255);
    private static final Color MOUSE_AXIS_MOVE = new Color(144, 238, 144);
    private static final Color MOUSE_AXIS_DRAG = new Color(92, 100, 0);

    private static ImageRenderer RENDERER;
    private final Canvas canvas;
    private int scrollX, scrollY;
    private float zoom;
    private Image image;

    private ImageRenderer(Canvas canvas) {
        this.canvas = canvas;
    }

    public static void register(Canvas canvas) {
        if (RENDERER != null) throw new RuntimeException("Already set image renderer");
        RENDERER = new ImageRenderer(canvas);
    }

    public static ImageRenderer instance() {
        return RENDERER;
    }

    @Override
    public void render(Graphics g) {
        renderImage(g);

        renderAxis(g);
        renderSelections(g);
        renderCurrentSelection(g);
    }

    private void renderAxis(Graphics g) {

        g.setColor(MOUSE_AXIS_DRAG);
        g.drawLine(EventListener.dragX, 0, EventListener.dragX, canvas.getHeight());
        g.drawLine(0, EventListener.dragY, canvas.getWidth(), EventListener.dragY);

        g.setColor(MOUSE_AXIS_MOVE);
        g.drawLine(EventListener.x, 0, EventListener.x, canvas.getHeight());
        g.drawLine(0, EventListener.y, canvas.getWidth(), EventListener.y);

    }

    private void renderSelections(Graphics g) {
        ArrayList<LabelSelection> selections = SelectionHandle.handle().getSelections();
        if (selections == null) return;

        LabelSelection[] labelSelections = selections.toArray(LabelSelection[]::new);
        for (LabelSelection selection : labelSelections) {
            int x = Math.round(selection.x() * zoom) + scrollX;
            int y = Math.round(selection.y() * zoom) + scrollY;
            int width = Math.round(selection.width() * zoom);
            int height = Math.round(selection.height() * zoom);

            g.setColor(SELECTION_BOX_INSIDE);
            g.fillRect(x, y, width, height);

            g.setColor(SELECTION_BOX_BORDER);
            g.drawRect(x + 1, y + 1, width - 1, height - 1);
        }
    }

    private void renderCurrentSelection(Graphics g) {
        Rectangle rect = SelectionHandle.handle().current();
        int x = rect.x + scrollX;
        int y = rect.y + scrollY;
        int width = rect.width;
        int height = rect.height;

        g.setColor(SELECTION_BOX_INSIDE);
        g.fillRect(x, y, width, height);

        g.setColor(SELECTION_BOX_BORDER);
        g.drawRect(x, y, width, height);

    }

    private void renderImage(Graphics g) {
        if (image == null) return;

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        int drawWidth = Math.round(imageWidth * zoom);
        int drawHeight = Math.round(imageHeight * zoom);
        g.drawImage(image, scrollX, scrollY, drawWidth, drawHeight, null);
    }

    public void drawImage(Image image, List<LabelSelection> selections, String name) {
        this.image = image;

        fitImage();

        EditorGui.EDITOR.setTitle(name);
        SelectionHandle.handle().getSelections().clear();
        SelectionHandle.handle().getSelections().addAll(selections);
    }

    //<editor-fold desc="Get & Set">
    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public void scrollX(int scroll) {
        if (image == null) return;
        scrollX = Mathf.clampRound(scrollX + scroll, 0, canvas.getWidth() - image.getWidth(null) * zoom);
    }

    public void scrollY(int scroll) {
        if (image == null) return;
        scrollY = Mathf.clampRound(scrollY + scroll, 0, canvas.getHeight() - image.getHeight(null) * zoom);
    }

    public void zoom(int amount) {
        this.zoom = Mathf.clampRound(zoom + amount / 30f, 0.1f, 100);
    }

    public void fitImage() {
        if (image == null) return;

        float width = image.getWidth(null), height = image.getHeight(null);


        float zoom_x = canvas.getWidth() / width;
        float zoom_y = canvas.getHeight() / height;

        zoom = Math.min(zoom_x, zoom_y);

        width *= zoom;

        scrollY = 0;
        scrollX = Math.round(canvas.getWidth() / 2f - width / 2);
    }

    public float zoom() {
        return zoom;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Image getImage() {
        return image;
    }

    //</editor-fold>
}
