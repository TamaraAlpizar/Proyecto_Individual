package Data

import Entity.Appointment

object AppointmentMemoryDataManager : IAppointmentDataManager {
    private var appointmentList = mutableListOf<Appointment>()

    override fun add(appointment: Appointment) {
        appointmentList.add(appointment)
    }

    override fun update(appointment: Appointment) {
        remove(appointment.Id)
        add(appointment)
    }

    override fun remove(id: String) {
        appointmentList.removeIf { it.Id.trim() == id.trim() }
    }

    override fun getAll(): List<Appointment> = appointmentList.toList()

    override fun getById(id: String): Appointment? {
        return appointmentList.find { it.Id.trim() == id.trim() }
    }
}
