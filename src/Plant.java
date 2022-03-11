import processing.core.PImage;

import java.util.List;

public abstract class Plant extends ActivityEntity{

    private int health;

    public Plant(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int num) {
        health = num;
    }

}
