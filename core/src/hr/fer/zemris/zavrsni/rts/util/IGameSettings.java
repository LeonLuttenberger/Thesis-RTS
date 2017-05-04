package hr.fer.zemris.zavrsni.rts.util;

public interface IGameSettings {

    void save();
    void load();

    boolean showFPSCounter();
    void setShowFPSCounter(boolean showFPSCounter);
}
