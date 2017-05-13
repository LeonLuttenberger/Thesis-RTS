package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.List;
import java.util.function.Consumer;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.ALIGNMENT_WEIGHT;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.COHESION_WEIGHT;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.GOAL_WEIGHT;

public class Squad implements IUpdatable {

    private final Consumer<PlayerUnit> functionApplyCohesion = this::applyCohesion;
    private final Consumer<PlayerUnit> functionApplyAlignment = this::applyAlignment;

    private final List<PlayerUnit> squadMembers;
    private final ILevel level;

    private PlayerUnit squadLeader;

    public Squad(List<PlayerUnit> squadMembers, ILevel level) {
        this.squadMembers = squadMembers;
        this.level = level;
    }

    @Override
    public void update(float deltaTime) {
        squadMembers.removeIf(IDamageable::isDestroyed);
        if (squadLeader.isDestroyed()) {
            if (squadMembers.isEmpty()) return;
            squadLeader.transferPathfindingTo(squadMembers.get(0));
        }

        squadLeader.updateSearchAgent();
        if (squadLeader.isSearchStopped()) {
            for (Unit squadMember : squadMembers) {
                squadMember.getVelocity().setLength(0);
            }
            return;
        }

        if (squadMembers.size() > 1) {
            for (PlayerUnit squadMember : squadMembers) {
                if (squadMember == squadLeader) continue;
                applyGoal(squadMember, squadLeader.getCurrentGoal());
            }
            squadMembers.forEach(functionApplyCohesion);
            squadMembers.forEach(functionApplyAlignment);
        }
        for (PlayerUnit squadMember : squadMembers) {
            MovementUtility.applyTerrainSeparation(squadMember, level);
        }
    }

    private void applyCohesion(Unit unit) {
        float dx = 0, dy = 0;

        for (Unit squadMember : squadMembers) {
            if (squadMember == unit) continue;

            dx += squadMember.getCenterX();
            dy += squadMember.getCenterY();
        }

        dx = dx / (squadMembers.size() - 1);
        dy = dy / (squadMembers.size() - 1);

        unit.adjustDirection(
                (dx - unit.getCenterX()) * COHESION_WEIGHT,
                (dy - unit.getCenterY()) * COHESION_WEIGHT
        );
    }

    private void applyAlignment(Unit unit) {
        float dx = 0, dy = 0;

        for (Unit squadMember : squadMembers) {
            if (squadMember == unit) continue;

            float angle = squadMember.getVelocity().angleRad();
            dx += Math.cos(angle);
            dy += Math.sin(angle);
        }

        dx = dx / (squadMembers.size() - 1);
        dy = dy / (squadMembers.size() - 1);

        float angleRad = unit.getVelocity().angleRad();
        unit.adjustDirection(
                (float) (dx - Math.cos(angleRad)) * ALIGNMENT_WEIGHT,
                (float) (dy - Math.sin(angleRad)) * ALIGNMENT_WEIGHT
        );
    }

    private void applyGoal(Unit unit, Vector2 squadGoal) {
        unit.adjustDirection(
                (squadGoal.x - unit.getCenterX()) * GOAL_WEIGHT,
                (squadGoal.y - unit.getCenterY()) * GOAL_WEIGHT
        );
    }

    public void sendToLocation(float x, float y) {
        stopSearch();

        squadLeader = MovementUtility.closestUnit(x, y, squadMembers);
        squadLeader.sendToDestination(x, y);
    }

    public boolean isSearchStopped() {
        return squadMembers.isEmpty() || squadLeader.isSearchStopped();
    }

    public void stopSearch() {
        if (squadLeader != null) {
            squadLeader.stopSearch();
        }

        for (Unit unit : squadMembers) {
            unit.getVelocity().setLength(0);
        }
    }
}
