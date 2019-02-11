package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.User

class Editors(
        override val data: List<User>
) : OrdinalList<User>
