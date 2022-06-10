package io.github.toberocat.actions;

import io.github.toberocat.gui.image.SelectionHandle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static io.github.toberocat.Launch.TEMP;
import static io.github.toberocat.utils.DataUtility.join;

public class DeleteAction implements Action {

    private final File disposedFile;
    private final File temporaryStorage;

    public DeleteAction(File disposedFile) {
        this.disposedFile = disposedFile;

        temporaryStorage = join(TEMP, disposedFile.getName());

        TEMP.mkdirs();
        try {
            Files.copy(disposedFile.toPath(), temporaryStorage.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void use(File temporaryStorage, File disposedFile) {
        if (!temporaryStorage.exists()) return;
        try {
            Files.move(Paths.get(temporaryStorage.getPath()), Paths.get(disposedFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            dispose();
            e.printStackTrace();
        }

        SelectionHandle.handle().reloadFromDisk();
    }

    @Override
    public void undo() {
        use(temporaryStorage, disposedFile);
    }


    @Override
    public void redo() {
        use(disposedFile, temporaryStorage);
    }

    @Override
    public void dispose() {
        if (temporaryStorage.exists()) temporaryStorage.delete();
    }
}
