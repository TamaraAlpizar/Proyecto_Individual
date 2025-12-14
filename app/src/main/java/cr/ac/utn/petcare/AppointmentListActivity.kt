package cr.ac.utn.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import Data.AppointmentMemoryDataManager

class AppointmentListActivity : AppCompatActivity() {

    private lateinit var listCitas: ListView
    private lateinit var btnAgregar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_list)

        listCitas = findViewById(R.id.listCitas)
        btnAgregar = findViewById(R.id.btnAgregarCita)
        btnVolver = findViewById(R.id.btnVolver)

        loadAppointments()


        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgendarCitaActivity::class.java)
            startActivity(intent)
        }


        btnVolver.setOnClickListener {
            finish()
        }


        listCitas.setOnItemClickListener { _, _, position, _ ->
            val appointment = AppointmentMemoryDataManager.getAll()[position]

            val intent = Intent(this, AgendarCitaActivity::class.java)
            intent.putExtra("appointment_id", appointment.Id)
            startActivity(intent)
        }


        listCitas.setOnItemLongClickListener { _, _, position, _ ->
            val appointment = AppointmentMemoryDataManager.getAll()[position]

            AppointmentMemoryDataManager.remove(appointment.Id)
            loadAppointments()

            Toast.makeText(this, "Cita eliminada", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        loadAppointments()
    }

    private fun loadAppointments() {
        val appointments = AppointmentMemoryDataManager.getAll()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            appointments.map {
                "Mascota: ${it.PetId}\nServicio: ${it.ServiceType}\n${it.Date} - ${it.Hour}"
            }
        )

        listCitas.adapter = adapter
    }
}
