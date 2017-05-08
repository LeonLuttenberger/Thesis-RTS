package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

import java.util.List;

public class Squad implements IUpdatable {

    private static final float COHESION_WEIGHT = 1/100f * 100;
    private static final float ALIGNMENT_WEIGHT = 1/5f * 100;
    private static final float GOAL_WEIGHT = 1/100f * 100;

    private final List<Unit> squadMembers;
    private Unit squadLeader;

    public Squad(List<Unit> squadMembers) {
        this.squadMembers = squadMembers;
        this.squadLeader = squadMembers.get(0);
    }

    @Override
    public void update(float deltaTime) {
        squadLeader.updateSearchAgent();
        if (squadLeader.isSearchStopped()) {
            for (Unit squadMember : squadMembers) {
                squadMember.getVelocity().setLength(0);
            }
            return;
        }

        if (squadMembers.size() > 1) {
            for (Unit member : squadMembers) {
                if (member != squadLeader) {
                    applyGoal(member, squadLeader.getCurrentGoal());
                }

                applyCohesion(member);
                applyAlignment(member);
            }
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

        unit.getVelocity().x += (dx - unit.getCenterX()) * COHESION_WEIGHT;
        unit.getVelocity().y += (dy - unit.getCenterY()) * COHESION_WEIGHT;
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
        unit.getVelocity().x += (dx - Math.cos(angleRad)) * ALIGNMENT_WEIGHT;
        unit.getVelocity().y += (dy - Math.sin(angleRad)) * ALIGNMENT_WEIGHT;
    }

    private void applyGoal(Unit unit, Vector2 squadGoal) {
        unit.getVelocity().x = (squadGoal.x - unit.getPosition().x) * GOAL_WEIGHT;
        unit.getVelocity().y = (squadGoal.y - unit.getPosition().y) * GOAL_WEIGHT;
    }

    public void sendToLocation(float x, float y) {
        squadLeader.sendToDestination(x, y);
    }

    public boolean isSearchStopped() {
        return squadLeader.isSearchStopped();
    }
}
