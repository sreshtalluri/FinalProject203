public class ActivityAction implements Action {

    private ActivityEntity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public ActivityAction(ActivityEntity entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(world, imageStore, scheduler);
    }

}
