package net.danygames2014.spawneggs.api.event;

import net.mine_diver.unsafeevents.Event;

import java.util.List;

public class SpawnEggBlacklistEvent extends Event {
    List<String> blacklist;

    public SpawnEggBlacklistEvent(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void addBlacklistEntry(String id) {
        blacklist.add(id);
    }
}
