package Data

import Entity.Appointment

interface IAppointmentDataManager {
    fun add(appointment: Appointment)
    fun update(appointment: Appointment)
    fun remove(id: String)
    fun getAll(): List<Appointment>
    fun getById(id: String): Appointment?
}