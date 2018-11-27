package glitch.kraken.object.json.list;


import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Community;
import lombok.Data;

@Data
public class Communities implements OrdinalList<Community> {
    private final ImmutableList<Community> data;
}
