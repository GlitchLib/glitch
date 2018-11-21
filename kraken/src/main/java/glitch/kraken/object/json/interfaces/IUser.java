package glitch.kraken.object.json.interfaces;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.api.objects.json.interfaces.Updated;

public interface IUser extends IDObject<Long>, Creation, Updated {
    @SerializedName(value = "username", alternate = {"login", "name"})
    String getUsername();
    String getDisplayName();

    String getLogo();

    @SerializedName(value = "bio", alternate = {"description"})
    String getUserBio();
}
