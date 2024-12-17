package gg.moonflower.etched.core.extension;

import gg.moonflower.etched.api.record.TrackData;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public interface JukeboxSongExt {

    List<TrackData> veil$tracks();
}
