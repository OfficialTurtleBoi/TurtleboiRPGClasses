package net.turtleboi.turtlerpgclasses.network.packet.experience;

import net.turtleboi.turtlecore.network.packet.util.experience.ExperienceHandler;
import net.turtleboi.turtlerpgclasses.client.ui.talenttrees.TalentPointAllocator;

public class TalentExperienceHandler implements ExperienceHandler {
    private final TalentPointAllocator allocator;

    public TalentExperienceHandler(TalentPointAllocator allocator) {
        this.allocator = allocator;
    }

    @Override
    public void handleExperienceSync(int totalExperience, int experienceLevel, float experienceProgress) {
        if (allocator != null) {
            allocator.handleExperienceSync(totalExperience, experienceLevel, experienceProgress);
            //System.out.println("This allocator is named:" + allocator); //debug code
        } else {
            //System.out.println("This allocator is null!"); //debug code
        }
    }
}
