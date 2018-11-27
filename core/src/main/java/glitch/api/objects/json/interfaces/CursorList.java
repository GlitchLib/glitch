package glitch.api.objects.json.interfaces;

import com.google.gson.annotations.SerializedName;

public interface CursorList<E> extends OrdinalList<E> {
    @SerializedName(value = "cursor", alternate = ("_cursor"))
    String getCursor();
}
