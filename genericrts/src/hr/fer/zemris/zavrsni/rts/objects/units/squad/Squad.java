package hr.fer.zemris.zavrsni.rts.objects.units.squad;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.ALIGNMENT_WEIGHT;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.COHESION_WEIGHT;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.GOAL_WEIGHT;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestUnitInRange;

public class Squad implements ISquad {

    private static final long serialVersionUID = -6849173030081042073L;

    private final Set<Unit> squadMembers;
    private final ILevel level;

    private float destinationX;
    private float destinationY;

    private Unit squadLeader;

    public Squad(Set<Unit> squadMembers, ILevel level) {
        this.squadMembers = Objects.requireNonNull(squadMembers);
        this.level = Objects.requireNonNull(level);
    }

    @Override
    public void update(float deltaTime) {
        squadMembers.removeIf(IDamageable::isDestroyed);
        if (squadLeader == null || squadLeader.isDestroyed()) {
            if (squadMembers.isEmpty()) return;

            sendToLocation(destinationX, destinationY);
        }

        squadLeader.updateSearchAgent();
        if (squadLeader.isSearchStopped()) {
            for (Unit squadMember : squadMembers) {
                squadMember.velocity.setLength(0);
            }
            return;
        }

        if (squadMembers.size() > 1) {
            for (Unit squadMember : squadMembers) {
                if (squadMember == squadLeader) continue;
                applyGoal(squadMember, squadLeader.getCurrentGoal());
            }
            squadMembers.forEach(this::applyCohesion);
            squadMembers.forEach(this::applyAlignment);
        }
        for (Unit squadMember : squadMembers) {
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

            float angle = squadMember.velocity.angleRad();
            dx += Math.cos(angle);
            dy += Math.sin(angle);
        }

        dx = dx / (squadMembers.size() - 1);
        dy = dy / (squadMembers.size() - 1);

        float angleRad = unit.velocity.angleRad();
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

    @Override
    public Set<Unit> getSquadMembers() {
        return Collections.unmodifiableSet(squadMembers);
    }

    @Override
    public void addUnit(Unit unit) {
        squadMembers.add(unit);
    }

    @Override
    public void removeUnit(Unit unit) {
        squadMembers.remove(unit);

        if (squadLeader == unit) {
            squadLeader = null;
        }
    }

    @Override
    public void sendToLocation(float x, float y) {
        stopSearch();

        squadLeader = closestUnitInRange(x, y, squadMembers, Float.POSITIVE_INFINITY);
        if (squadLeader == null) return;

        squadLeader.sendToDestination(x, y);

        destinationX = x;
        destinationY = y;
    }

    @Override
    public boolean isSearchStopped() {
        return squadMembers.isEmpty() || squadLeader.isSearchStopped();
    }

    @Override
    public void stopSearch() {
        if (squadLeader != null) {
            squadLeader.stopSearch();
        }

        for (Unit unit : squadMembers) {
            unit.velocity.setLength(0);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        sendToLocation(destinationX, destinationY);
    }
}
