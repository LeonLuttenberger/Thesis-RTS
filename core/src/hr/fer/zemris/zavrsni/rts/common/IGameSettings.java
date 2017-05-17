package hr.fer.zemris.zavrsni.rts.common;

public interface IGameSettings {

    void save();
    void load();

    boolean showFPSCounter();
    void setShowFPSCounter(boolean showFPSCounter);

    boolean showPlayerUnitHealthBars();
    void setShowPlayerUnitHealthBars(boolean showPlayerUnitHealthBars);

    boolean showHostileUnitHealthBars();
    void setShowHostileUnitHealthBars(boolean showHostileUnitHealthBars);

    boolean showResourceHealthBars();
    void setShowResourceHealthBars(boolean showResourceHealthBars);

    boolean showBuildingHealthBars();
    void setShowBuildingHealthBars(boolean showBuildingHealthBars);
}
