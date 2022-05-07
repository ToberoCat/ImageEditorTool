package io.github.toberocat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static void createFile(File saveTo, Rectangle rectangle) {
        File f = getLabelFile(saveTo);
        if (rectangle == null) {
            if (f.exists()) return;
            saveObject(f, new LabelSelection[0]);
            return;
        }

        LabelSelection label = LabelSelection.fromRect(rectangle);

        List<LabelSelection> labels = new ArrayList<>();
        if (Arrays.stream(LABEl_PATH.list()).anyMatch(x -> x.equals(saveTo.getName() + ".txt"))) {
            try {
                LabelSelection[] items = readRawLabels(new File(LABEl_PATH.getPath() + "/" + saveTo.getName() + ".txt"));

                labels.addAll(Arrays.asList(items));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        labels.add(label);
        saveObject(f, labels.toArray(LabelSelection[]::new));
    }
}
