import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends ActivityEntity {

    private int resourceLimit;
    private int resourceCount;

    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public Point nextPositionDude(WorldModel world, Point destPos)
    {
        PathingStrategy pathing = new AStarPathingStrategy();
        List<Point> path = pathing.computePath(getPosition(), destPos, pos -> !(world.isOccupied(pos) && world.getOccupancyCell(pos).getClass() != Stump.class),
                (pos, pos2) -> Functions.adjacent(pos, pos2), PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0) {
            return getPosition();
        }

        return path.get(0);
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit) {
            DudeFull miner = Factory.createDudeFull(super.getId(),
                    super.getPosition(), super.getActionPeriod(),
                    super.getAnimationPeriod(),
                    resourceLimit,
                    super.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(super.getPosition(), target.getPosition())) {
            resourceCount += 1;
            if (target.getClass() == Sapling.class) {
                ((Sapling)target).setHealth(((Sapling)target).getHealth() - 1);
            } else {
                ((Tree)target).setHealth(((Tree)target).getHealth() - 1);
            }
            return true;
        }
        else {
            Point nextPos = nextPositionDude(world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(super.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !moveToNotFull(world, target.get(), scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

}
