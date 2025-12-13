package cr.ac.utn.petcare

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.petcare.network.ApiClient
import cr.ac.utn.petcare.network.AppointmentDto
import cr.ac.utn.petcare.network.PetDto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Calendar

class AgendarCitaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_APPOINTMENT_PET = "extra_appointment_pet"
        const val EXTRA_APPOINTMENT_SERVICE = "extra_appointment_service"
        const val EXTRA_APPOINTMENT_DATE = "extra_appointment_date"
        const val EXTRA_APPOINTMENT_TIME = "extra_appointment_time"
    }

    private lateinit var spinnerMascota: AutoCompleteTextView
    private lateinit var spinnerServicio: AutoCompleteTextView
    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var tvSummaryPet: TextView
    private lateinit var tvSummaryService: TextView
    private lateinit var tvSummaryDate: TextView
    private lateinit var btnConfirm: Button

    private var selectedPetName: String = ""
    private var selectedPetId: String? = null
    private var selectedService = ""
    private var selectedDate = ""
    private var selectedTime = ""

    private var petList: List<PetDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_cita)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        val ownerIdFromIntent = intent.getStringExtra("owner_id")
        val prefs = getSharedPreferences("petcare_prefs", MODE_PRIVATE)
        val ownerId = ownerIdFromIntent ?: prefs.getString("owner_id", "") ?: ""

        if (ownerId.isEmpty()) {
            Toast.makeText(
                this,
                "No se encontró el propietario. Inicie sesión nuevamente.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        spinnerMascota = findViewById(R.id.spinnerMascota)
        spinnerServicio = findViewById(R.id.spinnerServicio)
        btnSelectDate = findViewById(R.id.btnSelectDate)
        btnSelectTime = findViewById(R.id.btnSelectTime)
        tvSummaryPet = findViewById(R.id.tvSummaryPet)
        tvSummaryService = findViewById(R.id.tvSummaryService)
        tvSummaryDate = findViewById(R.id.tvSummaryDate)
        btnConfirm = findViewById(R.id.btnConfirm)


        cargarMascotasDesdeApi(ownerId)


        val services = listOf(
            "Baño y Corte",
            "Corte de Uñas",
            "Consulta General",
            "Vacunación",
            "Desparasitación",
            "Otro"
        )

        val serviceAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, services)
        spinnerServicio.setAdapter(serviceAdapter)

        spinnerServicio.setOnItemClickListener { _, _, position, _ ->
            selectedService = services[position]
            tvSummaryService.text = selectedService
        }


        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val picker = DatePickerDialog(
                this,
                { _, y, m, d ->
                    // Formato simple (igual que antes)
                    selectedDate = "$d/${m + 1}/$y"
                    updateSummaryDate()
                },
                year, month, day
            )

            picker.show()
        }


        btnSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val picker = TimePickerDialog(
                this,
                { _, h, min ->
                    selectedTime = String.format("%02d:%02d", h, min)
                    updateSummaryDate()
                },
                hour, minute, true
            )

            picker.show()
        }


        btnConfirm.setOnClickListener {

            if (selectedPetName.isEmpty()) {
                selectedPetName = spinnerMascota.text.toString().trim()
            }

            if (selectedPetId == null) {
                Toast.makeText(this, "Seleccione una mascota válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedService.isEmpty()) {
                selectedService = spinnerServicio.text.toString().trim()
            }

            if (selectedPetName.isEmpty() ||
                selectedService.isEmpty() ||
                selectedDate.isEmpty() ||
                selectedTime.isEmpty()
            ) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            crearCitaEnApi(
                petId = selectedPetId!!,
                petName = selectedPetName,
                service = selectedService,
                date = selectedDate,
                time = selectedTime
            )
        }
    }

    private fun cargarMascotasDesdeApi(ownerId: String) {
        lifecycleScope.launch {
            try {
                val allPets = ApiClient.vetApi.getPets()
                petList = allPets.filter { it.ownerId == ownerId }

                if (petList.isEmpty()) {
                    spinnerMascota.setText("Sin mascotas", false)
                    spinnerMascota.isEnabled = false
                    Toast.makeText(
                        this@AgendarCitaActivity,
                        "No tiene mascotas registradas.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                val petNames = petList.map { it.petName }
                val petAdapter = ArrayAdapter(
                    this@AgendarCitaActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    petNames
                )
                spinnerMascota.setAdapter(petAdapter)

                spinnerMascota.setOnItemClickListener { _, _, position, _ ->
                    val pet = petList[position]
                    selectedPetName = pet.petName
                    selectedPetId = pet.id
                    tvSummaryPet.text = selectedPetName
                }

            } catch (e: HttpException) {
                Toast.makeText(
                    this@AgendarCitaActivity,
                    "Error al cargar mascotas (${e.code()})",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@AgendarCitaActivity,
                    "Error de conexión al cargar mascotas",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun crearCitaEnApi(
        petId: String,
        petName: String,
        service: String,
        date: String,
        time: String
    ) {
        lifecycleScope.launch {
            try {
                val newAppointment = AppointmentDto(
                    id = null,
                    petId = petId,
                    serviceType = service,
                    date = date,
                    hour = time,
                    notes = "",
                    status = null
                )

                val created = ApiClient.vetApi.createAppointment(newAppointment)

                Toast.makeText(
                    this@AgendarCitaActivity,
                    "Cita creada para $petName",
                    Toast.LENGTH_LONG
                ).show()


                val resultIntent = intent.apply {
                    putExtra(EXTRA_APPOINTMENT_PET, petName)
                    putExtra(EXTRA_APPOINTMENT_SERVICE, service)
                    putExtra(EXTRA_APPOINTMENT_DATE, date)
                    putExtra(EXTRA_APPOINTMENT_TIME, time)

                }

                setResult(RESULT_OK, resultIntent)
                finish()

            } catch (e: HttpException) {
                Toast.makeText(
                    this@AgendarCitaActivity,
                    "Error al crear cita (${e.code()})",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@AgendarCitaActivity,
                    "Error de conexión al crear cita",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateSummaryDate() {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            tvSummaryDate.text = "$selectedDate - $selectedTime"
        }
    }
}
