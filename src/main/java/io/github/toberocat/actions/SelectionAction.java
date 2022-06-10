package io.github.toberocat.actions;

import io.github.toberocat.Launch;
import io.github.toberocat.gui.image.SelectionHandle;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Utility;
import io.github.toberocat.utils.selection.LabelSelection;

import java.util.List;
import java.util.UUID;

import static io.github.toberocat.utils.DataUtility.join;
import static io.github.toberocat.utils.DataUtility.saveObject;

public class SelectionAction implements Action {

    private LabelSelection selection;
    private String image;

    public SelectionAction(LabelSelection selection, String image) {
        this.selection = selection;
        this.image = image;
    }

    @Override
    public void undo() {
        System.out.println("Undo");
        List<LabelSelection> labelSelections = Utility.readLabel(image);

        saveObject(DataUtility.getLabelFile(join(Launch.ORIGINAL_IMAGES_PATH, image)),
                labelSelections.stream().filter(x -> x.x() != selection.x() || x.y() != selection.y() || x.width() != selection.width()
                        || x.height() != selection.height()).toArray(LabelSelection[]::new));

        SelectionHandle.handle().reloadFromDisk();
    }

    @Override
    public void redo() {
        List<LabelSelection> labelSelections = Utility.readLabel(image);
        labelSelections.add(selection);

        saveObject(DataUtility.getLabelFile(join(Launch.ORIGINAL_IMAGES_PATH, image)), labelSelections.toArray(LabelSelection[]::new));

        SelectionHandle.handle().reloadFromDisk();

        SelectionHandle.handle().reloadFromDisk();
    }

    @Override
    public void dispose() {
        selection = null;
    }
}
