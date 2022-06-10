package io.github.toberocat.utils.selection;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.awt.*;
import java.util.UUID;

public class LabelSelection {

    public static final int PIVOT_SIZE = 20;

    private int x;
    private int y;
    private int width;
    private int height;

    private UUID id;

    public LabelSelection() {
        id = UUID.randomUUID();
    }

    public LabelSelection(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
         id = UUID.randomUUID();
    }

    public static LabelSelection fromRect(Rectangle rectangle) {
        return new LabelSelection(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void shiftOrigin(int x, int y) {
        int xDiff = this.x - x;
        int yDiff = this.y -y;

        this.x = x;
        this.y = y;

        this.width += xDiff;
        this.height += yDiff;
    }

    public void shiftDimension(int x, int y) {
        int wDiff = x - this.x;
        int hDiff = y - this.y;

        this.width = wDiff;
        this.height = hDiff;
    }

    public void expandY(int value) {
        int delta = y - value;
        this.y -= delta;
        this.height = delta;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && y >= this.y && x <= (this.x + this.width) && y <= (this.y + this.height);
    }

    public LabelDragMode getDragMode(int x, int y) {
        if (contains(this.x, this.y, x, y)) return LabelDragMode.LEFT_TOP;
        if (contains(this.x + width, this.y, x, y)) return LabelDragMode.RIGHT_TOP;

        if (contains(this.x, this.y + height, x, y)) return LabelDragMode.LEFT_DOWN;
        if (contains(this.x + width, this.y + height, x, y)) return LabelDragMode.RIGHT_DOWN;

        if (contains(this.x + width / 2, this.y, x, y)) return LabelDragMode.TOP;
        if (contains(this.x + width / 2, this.y + height, x, y)) return LabelDragMode.DOWN;

        if (contains(this.x, this.y + height / 2, x, y)) return LabelDragMode.LEFT;
        if (contains(this.x + width, this.y + height / 2, x, y)) return LabelDragMode.RIGHT;

        return LabelDragMode.NONE;
    }

    private boolean contains(int oX, int oY, int cX, int cY) {
        return new Rectangle(oX - PIVOT_SIZE, oY - PIVOT_SIZE, PIVOT_SIZE * 2, PIVOT_SIZE * 2).contains(cX, cY);
    }

    @JsonGetter
    public int x() {
        return x;
    }

    @JsonSetter
    public void x(int x) {
        this.x = x;
    }

    @JsonGetter
    public int y() {
        return y;
    }

    @JsonSetter
    public void y(int y) {
        this.y = y;
    }

    @JsonGetter
    public int width() {
        return width;
    }

    @JsonSetter
    public void width(int width) {
        this.width = width;
    }

    @JsonGetter
    public int height() {
        return height;
    }

    @JsonSetter
    public void height(int height) {
        this.height = height;
    }

    @JsonIgnore
    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public void setId(UUID id) {
        this.id = id;
    }
}
