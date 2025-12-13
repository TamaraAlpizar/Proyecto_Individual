package Entity

import android.graphics.Bitmap
import java.util.Date

class Pet {
     var id: String = ""
     var name: String = ""
     var breed: String = ""
     var gender: String = ""
     var age: Int = 0
     var weight: Double = 0.0
     var ownerId: String = ""

    var photo: Bitmap? = null
     var notes: String = ""

    constructor(id: String, name: String, breed: String, gender: String,
                age: Int, weight: Double, ownerId: String, notes: String, photo: Bitmap)
    {
        this.id = id
        this.name = name
        this.breed = breed
        this.gender = gender
        this.age = age
        this.weight = weight
        this.ownerId = ownerId
        this.notes = notes
        this.photo = photo
    }

    var Id: String
        get() = this.id
        set(value) { this.id = value }

    var Name: String
        get() = this.name
        set(value) { this.name = value }

    var Breed: String
        get() = this.breed
        set(value) { this.breed = value }

    var Gender: String
        get() = this.gender
        set(value) { this.gender = value }

    var Age: Int
        get() = this.age
        set(value) { this.age = value }

    var Weight: Double
        get() = this.weight
        set(value) { this.weight = value }

    var OwnerId: String
        get() = this.ownerId
        set(value) { this.ownerId = value }

    var Notes: String
        get() = this.notes
        set(value) { this.notes = value }

}