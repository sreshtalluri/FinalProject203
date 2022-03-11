import processing.core.PImage;

import java.util.List;

public abstract class AnimatingEntity extends Entity {

    private int imageIndex;
    private int animationPeriod;

    public AnimatingEntity(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod)
    {
        super(id, position, images);
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public PImage getCurrentImage() {
        return super.getImages().get(imageIndex);
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % super.getImages().size();
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                getAnimationPeriod());
    }

}
