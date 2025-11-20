package cr.ac.utn.petcare

import Entity.Person
import Entity.Pet
import Data.PersonMemoryDataManager
import Data.PetMemoryDataManager
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PersonDetailsActivity : AppCompatActivity() {

    private var person: Person? = null
    private var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_details)

        val personId = intent.getStringExtra("person_id")
        person = personId?.let { PersonMemoryDataManager.getById(it) }

        loadPersonInfo()
        loadPetInfo()

        findViewById<Button>(R.id.btnBackToHome).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnEditPerson).setOnClickListener { editPerson() }
        findViewById<Button>(R.id.btnEditPet).setOnClickListener { editPet() }
        findViewById<Button>(R.id.btnAddPet).setOnClickListener { addNewPet() }
    }

    private fun loadPersonInfo() {
        person?.let {
            findViewById<TextView>(R.id.tvName).text = "Nombre: ${it.Name}"
            findViewById<TextView>(R.id.tvEmail).text = "Email: ${it.Email}"
            findViewById<TextView>(R.id.tvPhone).text = "Teléfono: ${it.Phone}"
            findViewById<TextView>(R.id.tvAddress).text = "Dirección: ${it.Address}"
        }
    }

    private fun loadPetInfo() {
        val ownerId = person?.Id ?: return
        pet = PetMemoryDataManager.getByOwnerId(ownerId)

        pet?.let {
            findViewById<TextView>(R.id.tvPetName).text = "Nombre: ${it.Name}"
            findViewById<TextView>(R.id.tvPetBreed).text = "Raza: ${it.Breed}"
            findViewById<TextView>(R.id.tvPetGender).text = "Género: ${it.Gender}"
            findViewById<TextView>(R.id.tvPetAge).text = "Edad: ${it.Age}"
            findViewById<TextView>(R.id.tvPetWeight).text = "Peso: ${it.Weight}"
            findViewById<TextView>(R.id.tvPetNotes).text = "Notas: ${it.Notes}"
        }
    }


    private fun editPerson() {
        val p = person ?: return

        val dialogView = layoutInflater.inflate(R.layout.activity_dialog_edit_person, null)

        val etName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEditEmail)
        val etPhone = dialogView.findViewById<EditText>(R.id.etEditPhone)
        val etAddress = dialogView.findViewById<EditText>(R.id.etEditAddress)


        etName.setText(p.Name)
        etEmail.setText(p.Email)
        etPhone.setText(p.Phone.toString())
        etAddress.setText(p.Address)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Información")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                p.Name = etName.text.toString()
                p.Email = etEmail.text.toString()
                p.Phone = etPhone.text.toString().toIntOrNull() ?: 0
                p.Address = etAddress.text.toString()

                PersonMemoryDataManager.update(p)

                loadPersonInfo()

                Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun editPet() {
        Toast.makeText(this, "Editar mascota ", Toast.LENGTH_SHORT).show()
    }

    private fun addNewPet() {
        Toast.makeText(this, "Agregar mascota ", Toast.LENGTH_SHORT).show()
    }
}