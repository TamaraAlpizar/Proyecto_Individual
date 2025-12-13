package Entity

import android.graphics.Bitmap
import java.util.Date

class Pet(
    var id: String = "",
    var petName: String = "",
    var Breed: String = "",
    var Gender: String = "",
    var Age: Int = 0,
    var Weight: Double = 0.0,
    var OwnerId: String = "",
    var Notes: String = "",
    var photo: Bitmap? = null
)