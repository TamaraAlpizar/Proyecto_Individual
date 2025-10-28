package Entity

import java.util.Date

class Appointment {
    private var id: String = ""
    private var petId: String = ""
    private var serviceType: String = ""
    private var date: Date
    private var hour: String = ""
    private var notes: String = ""
    private var status: String = "Pendiente"

    constructor(id: String, petId: String, serviceType: String,
                date: Date, hour: String, notes: String, status: String)
    {

        this.id = id
        this.petId = petId
        this.serviceType = serviceType
        this.date = date
        this.hour = hour
        this.notes = notes
        this.status = status
    }

    var Id: String
        get() = this.id
        set(value) { this.id = value }

    var PetId: String
        get() = this.petId
        set(value) { this.petId = value }

    var ServiceType: String
        get() = this.serviceType
        set(value) { this.serviceType = value }

    var Date: Date
        get() = this.date
        set(value) { this.date = value }

    var Hour: String
        get() = this.hour
        set(value) { this.hour = value }

    var Notes: String
        get() = this.notes
        set(value) { this.notes = value }

    var Status: String
        get() = this.status
        set(value) { this.status = value }
}
