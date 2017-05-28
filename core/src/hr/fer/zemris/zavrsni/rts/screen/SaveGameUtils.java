package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import hr.fer.zemris.zavrsni.rts.common.IGameState;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SaveGameUtils {

    private static final String SAVE_DIR = "saves/";
    private static final String SAVE_EXTENSION = ".save";

    public static List<String> showSaves() {
        FileHandle directory = Gdx.files.local(SAVE_DIR);

        return Arrays.stream(directory.list())
                .filter(f -> f.name().endsWith(SAVE_EXTENSION))
                .map(FileHandle::nameWithoutExtension)
                .collect(Collectors.toList());
    }

    public static IGameState loadSave(String saveName) {
        FileHandle file = Gdx.files.local(SAVE_DIR + saveName + SAVE_EXTENSION);

        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.file())))) {

            return (IGameState) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveGame(String saveName, IGameState gameState) {
        FileHandle file = Gdx.files.local(saveName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file.file())))) {

            oos.writeObject(gameState);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SaveGameUtils() {}
}
