package hr.fer.zemris.zavrsni.rts.objects.units.squad;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

public class CompositeSquad implements ISquad {

    private final List<Squad> squadList;
    private final ILevel level;
    private final float joinDistance;

    public CompositeSquad(List<Squad> squadList, ILevel level, float joinDistance) {
        this.squadList = Objects.requireNonNull(squadList);
        this.level = level;
        this.joinDistance = joinDistance;
    }

    @Override
    public void update(float deltaTime) {
        Squad[] squadsToJoin = null;

        for (Squad squad : squadList) {
            Vector2 avg1 = getAverageSquadPosition(squad);
            if (avg1 == null) continue;

            for (Squad other : squadList) {
                if (squad == other) continue;

                Vector2 avg2 = getAverageSquadPosition(other);
                if (avg2 == null) continue;

                if (avg1.sub(avg2).len() < joinDistance) {
                    squadsToJoin = new Squad[] {squad, other};
                    squadsToJoin[0] = squad;
                    squadsToJoin[1] = other;
                    break;
                }
            }
        }

        if (squadsToJoin != null) {
            Set<Unit> squadMembers = squadsToJoin[1].getSquadMembers();
            for (Unit squadMember : squadMembers) {
                squadsToJoin[0].addUnit(squadMember);
            }
            squadList.remove(squadsToJoin[1]);
            squadsToJoin[1].stopSearch();
        }

        for (Squad squad : squadList) {
            squad.update(deltaTime);
        }
        squadList.removeIf(squad -> squad.getSquadMembers().isEmpty());
    }

    @Override
    public Set<Unit> getSquadMembers() {
        return squadList.stream().flatMap(s -> s.getSquadMembers().stream()).collect(Collectors.toSet());
    }

    @Override
    public void addUnit(Unit unit) {
        boolean isAdded = false;

        for (Squad squad: squadList) {
            Vector2 avgPosition = getAverageSquadPosition(squad);
            if (avgPosition == null) continue;

            if (Unit.distanceBetween(unit, avgPosition.x, avgPosition.y) < joinDistance) {
                squad.addUnit(unit);
                isAdded = true;
                break;
            }
        }

        if (!isAdded) {
            squadList.add(new Squad(new HashSet<>(Collections.singletonList(unit)), level));
        }
    }

    private Vector2 getAverageSquadPosition(Squad squad) {
        OptionalDouble avgX = squad.getSquadMembers().stream()
                .mapToDouble(Unit::getCenterX)
                .average();
        OptionalDouble avgY = squad.getSquadMembers().stream()
                .mapToDouble(Unit::getCenterY)
                .average();

        if (avgX.isPresent() && avgY.isPresent()) {
            return new Vector2((float) avgX.getAsDouble(), (float) avgY.getAsDouble());
        }

        return null;
    }

    @Override
    public void removeUnit(Unit unit) {
        for (Squad squad : squadList) {
            squad.removeUnit(unit);
        }
    }

    @Override
    public void sendToLocation(float x, float y) {
        for (Squad squad : squadList) {
            squad.sendToLocation(x, y);
        }
    }

    @Override
    public boolean isSearchStopped() {
        for (Squad squad : squadList) {
            if (!squad.isSearchStopped()) return false;
        }

        return true;
    }

    @Override
    public void stopSearch() {
        for (Squad squad : squadList) {
            squad.stopSearch();
        }
    }
}
