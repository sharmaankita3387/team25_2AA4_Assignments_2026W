package Assignment1;

import java.util.Random;

public class RegularDice implements Dice {
    private static final Random random = new Random();
    private final int sides;

    public RegularDice(int sides) {
        this.sides = sides;
    }

    public int roll() {
        return random.nextInt(sides) + 1;
    }
}


