package creatures;

import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    private final double moveLoss = 0.03;
    private final double stayLoss = 0.01;
    private final double replicateFactor = 0.5;

    /** creates clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** Should return a color with red = 34, blue = 231, and green = 0.
     */
    public Color color() {
        g = 0;
        r = 34;
        b = 231;
        return color(r, g, b);
    }

    /** Gain the energy of c. */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /** Clorus should lose 0.03 units of energy when moving.
     */
    public void move() {
        energy -= moveLoss;
    }


    /** Clorus lose 0.01 energy when staying. */
    public void stay() {
        energy -= stayLoss;
    }

    /** Clorus and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Clorus.
     */
    public Clorus replicate() {
        energy *= replicateFactor;
        return new Clorus(energy);
    }

    /** Clorus take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if any Plips, Attack one of them randomly.
     *  3. Otherwise, if energy >= 1, REPLICATE.
     *  4. Otherwise, if nothing else, MOVE
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (plips.size() > 0) {
            Direction d = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, d);
        } else if (energy >= 1) {
            Direction d = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, d);
        }
        Direction d = HugLifeUtils.randomEntry(empties);
        return new Action(Action.ActionType.MOVE, d);
    }
}
