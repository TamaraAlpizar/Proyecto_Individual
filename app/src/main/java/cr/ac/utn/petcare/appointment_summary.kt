package cr.ac.utn.petcare

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class appointment_summary : AppCompatActivity() {

    companion object {
        const val REQUEST_CREATE_APPOINTMENT = 2001
    }

    private lateinit var appointmentContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_summary)

        appointmentContainer = findViewById(R.id.appointmentContainer)


        val btnBack = findViewById<ImageButton>(R.id.btnBackAppointment)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        val fabAddAppointment = findViewById<FloatingActionButton>(R.id.fabAddAppointment)
        fabAddAppointment.setOnClickListener {
            val intent = Intent(this, AgendarCitaActivity::class.java)

            startActivityForResult(intent, REQUEST_CREATE_APPOINTMENT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CREATE_APPOINTMENT && resultCode == RESULT_OK && data != null) {

            val petName = data.getStringExtra(AgendarCitaActivity.EXTRA_APPOINTMENT_PET) ?: return
            val service = data.getStringExtra(AgendarCitaActivity.EXTRA_APPOINTMENT_SERVICE) ?: ""
            val date = data.getStringExtra(AgendarCitaActivity.EXTRA_APPOINTMENT_DATE) ?: ""
            val time = data.getStringExtra(AgendarCitaActivity.EXTRA_APPOINTMENT_TIME) ?: ""

            addAppointmentCard(petName, service, date, time)
        }
    }

    private fun addAppointmentCard(petName: String, service: String, date: String, time: String) {

        val cardView = layoutInflater.inflate(R.layout.item_appointment_card, appointmentContainer, false)

        val tvPetName = cardView.findViewById<TextView>(R.id.tvAppointmentPetName)
        val tvService = cardView.findViewById<TextView>(R.id.tvAppointmentService)
        val tvDateTime = cardView.findViewById<TextView>(R.id.tvAppointmentDateTime)
        val btnEdit = cardView.findViewById<MaterialButton>(R.id.btnEditAppointment)
        val btnDelete = cardView.findViewById<MaterialButton>(R.id.btnDeleteAppointment)

        tvPetName.text = petName
        tvService.text = service
        tvDateTime.text = "$date - $time"


        btnEdit.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.activity_dialog_edit_appointment, null)

            val etPetName = dialogView.findViewById<EditText>(R.id.etEditAppointmentPetName)
            val etService = dialogView.findViewById<EditText>(R.id.etEditAppointmentService)
            val etDate = dialogView.findViewById<EditText>(R.id.etEditAppointmentDate)
            val etTime = dialogView.findViewById<EditText>(R.id.etEditAppointmentTime)

            etPetName.setText(tvPetName.text.toString())
            etService.setText(tvService.text.toString())

            val parts = tvDateTime.text.toString().split("-")
            if (parts.size == 2) {
                etDate.setText(parts[0].trim())
                etTime.setText(parts[1].trim())
            }

            val dialog = AlertDialog.Builder(this)
                .setTitle("Editar Cita")
                .setView(dialogView)
                .setPositiveButton("Guardar") { _, _ ->

                    tvPetName.text = etPetName.text.toString()
                    tvService.text = etService.text.toString()
                    tvDateTime.text = etDate.text.toString() + " - " + etTime.text.toString()

                    Toast.makeText(this, "Cita actualizada", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .create()

            dialog.show()
        }


        btnDelete.setOnClickListener {
            appointmentContainer.removeView(cardView)
        }

        appointmentContainer.addView(cardView)
    }
}