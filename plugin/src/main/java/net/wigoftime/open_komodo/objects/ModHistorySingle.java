package net.wigoftime.open_komodo.objects;

import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class ModHistorySingle implements Serializable {
    public final long dateTime;
    public final UUID causerPlayer;
    public final String type;
    public final String context;

    public ModHistorySingle(long dateTime, UUID causerPlayer, String type, String context) {
        this.dateTime = dateTime;
        this.causerPlayer = causerPlayer;
        this.type = type;
        this.context = context;
    }
}
