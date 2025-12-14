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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.petcare.network.ApiClient
import cr.ac.utn.petcare.network.UpdatePetRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class pet_summary : AppCompatActivity() {

    companion object {
        const val REQUEST_CREATE_PET = 1001

        const val EXTRA_PET_ID = "extra_pet_id"
        const val EXTRA_PET_NAME = "extra_pet_name"
        const val EXTRA_PET_BREED = "extra_pet_breed"
        const val EXTRA_PET_AGE = "extra_pet_age"
    }

    private lateinit var petContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_summary)

        petContainer = findViewById(R.id.petContainer)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        findViewById<FloatingActionButton>(R.id.fabAddPet).setOnClickListener {
            startActivityForResult(
                Intent(this, CreatePetProfileActivity::class.java),
                REQUEST_CREATE_PET
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CREATE_PET && resultCode == RESULT_OK && data != null) {

            val petId = data.getStringExtra(EXTRA_PET_ID) ?: return
            val name = data.getStringExtra(EXTRA_PET_NAME) ?: return
            val breed = data.getStringExtra(EXTRA_PET_BREED) ?: ""
            val age = data.getStringExtra(EXTRA_PET_AGE) ?: ""

            addPetCard(petId, name, breed, age)
        }
    }

    private fun addPetCard(petId: String, name: String, breed: String, age: String) {

        val cardView = layoutInflater.inflate(R.layout.item_pet_card, petContainer, false)

        val tvPetName = cardView.findViewById<TextView>(R.id.tvPetName)
        val tvPetBreed = cardView.findViewById<TextView>(R.id.tvPetBreed)
        val tvPetAge = cardView.findViewById<TextView>(R.id.tvPetAge)
        val btnEdit = cardView.findViewById<MaterialButton>(R.id.btnEdit)
        val btnDelete = cardView.findViewById<MaterialButton>(R.id.btnDelete)

        tvPetName.text = name
        tvPetBreed.text = breed
        tvPetAge.text = "$age años"


        btnEdit.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.activity_dialog_edit_pet, null)

            val etName = dialogView.findViewById<EditText>(R.id.etEditPetName)
            val etBreed = dialogView.findViewById<EditText>(R.id.etEditPetBreed)
            val etGender = dialogView.findViewById<EditText>(R.id.etEditPetGender)
            val etAge = dialogView.findViewById<EditText>(R.id.etEditPetAge)
            val etWeight = dialogView.findViewById<EditText>(R.id.etEditPetWeight)
            val etNotes = dialogView.findViewById<EditText>(R.id.etEditPetNotes)

            etName.setText(tvPetName.text)
            etBreed.setText(tvPetBreed.text)
            etAge.setText(age)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Editar Mascota")
                .setView(dialogView)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create()

            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val body = UpdatePetRequest(
                    petName = etName.text.toString(),
                    breed = etBreed.text.toString(),
                    gender = etGender.text.toString(),
                    age = etAge.text.toString().toInt(),
                    weight = etWeight.text.toString().toDoubleOrNull() ?: 0.0,
                    notes = etNotes.text.toString()
                )

                lifecycleScope.launch {
                    try {
                        val updated = ApiClient.vetApi.updatePet(petId, body)

                        tvPetName.text = updated.petName
                        tvPetBreed.text = updated.breed
                        tvPetAge.text = "${updated.age} años"

                        Toast.makeText(this@pet_summary, "Mascota actualizada", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()

                    } catch (e: HttpException) {
                        Toast.makeText(this@pet_summary, "Error servidor", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


       btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Mascota")
                .setMessage("¿Desea eliminar esta mascota?")
                .setPositiveButton("Sí") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            ApiClient.vetApi.deletePet(petId)
                            petContainer.removeView(cardView)
                            Toast.makeText(this@pet_summary, "Mascota eliminada", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@pet_summary, "No se pudo eliminar", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        petContainer.addView(cardView)
    }
}