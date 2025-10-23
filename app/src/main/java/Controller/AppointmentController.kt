package Controller


import Data.AppointmentMemoryDataManager
import Entity.Appointment
import android.content.Context
import cr.ac.utn.petcare.R

class AppointmentController (private val context: Context) {

    private val dataManager = AppointmentMemoryDataManager

    fun addAppointment(appointment: Appointment) {
        try {
            dataManager.add(appointment)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAddAppointment))
        }
    }

    fun updateAppointment(appointment: Appointment) {
        try {
            dataManager.update(appointment)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdateAppointment))
        }
    }

    fun getAppointments(): List<Appointment> {
        try {
            return dataManager.getAll()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetAppointment))
        }
    }

    fun getById(id: String): Appointment {
        return dataManager.getById(id)
            ?: throw Exception(context.getString(R.string.ErrorMsgGetAppointmentById))
    }
}