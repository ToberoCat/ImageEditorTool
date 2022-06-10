package io.github.toberocat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.gui.image.SelectionHandle;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.toberocat.Launch.LABEl_PATH;
import static io.github.toberocat.utils.Utility.getImage;

public class DataUtility {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static File join(File file, String path) {
        return new File(file.getPath() + "/" + path);
    }

    public static LabelSelection[] readRawLabels(File file) throws IOException {
        if (!file.exists()) return new LabelSelection[0];
        return objectMapper.readValue(file, LabelSelection[].class);
    }

    public static void saveObject(File file, Object object) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeContent(File file) {
        saveObject(getLabelFile(file), new LabelSelection[0]);
    }

    public static File getLabelFile(File imageFile) {
        return new File(LABEl_PATH.getPath() + "/" + imageFile.getName() + ".txt");
    }



    public static void createFile(File saveTo) {
        File f = getLabelFile(saveTo);
        saveObject(f, SelectionHandle.handle().getSelections().toArray(LabelSelection[]::new));
    }
}
