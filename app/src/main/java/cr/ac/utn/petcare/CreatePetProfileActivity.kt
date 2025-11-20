package cr.ac.utn.petcare

import Controller.PetController
import Entity.Pet
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petcare.R

class CreatePetProfileActivity : AppCompatActivity() {

    private lateinit var petController: PetController
    private lateinit var imgPhotoPet: ImageView
    private var petPhotoBitmap: Bitmap? = null

    private val cameraPreviewLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imgPhotoPet.setImageBitmap(bitmap)
            petPhotoBitmap = bitmap
        }
    }

    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                imgPhotoPet.setImageURI(uri)
                val drawable = imgPhotoPet.drawable as? BitmapDrawable
                petPhotoBitmap = drawable?.bitmap
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet_profile)

        petController = PetController(this)
        imgPhotoPet = findViewById(R.id.imgPhotoPet)

        val ownerId = intent.getStringExtra("owner_id") ?: ""

        val etName = findViewById<EditText>(R.id.et_nombre)
        val spinnerBreed = findViewById<Spinner>(R.id.txtBreedPet)
        val etAge = findViewById<EditText>(R.id.txtAge)
        val spinnerUnit = findViewById<Spinner>(R.id.spinner_unidad)
        val rbMale = findViewById<RadioButton>(R.id.rb_macho)
        val rbFemale = findViewById<RadioButton>(R.id.rb_hembra)
        val etNotes = findViewById<EditText>(R.id.et_descripcion)
        val btnSave = findViewById<Button>(R.id.btnSavePetProfile)
        val btnCamera = findViewById<ImageButton>(R.id.btn_foto_camara)
        val btnGallery = findViewById<Button>(R.id.btnPhotoPet)

        val breeds = listOf(
            "Labrador", "Poodle", "Pitbull", "Boxer",
            "French Poodle", "Schnauzer", "Salchicha"
        )
        spinnerBreed.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, breeds)

        val units = listOf("Kg", "Lb")
        spinnerUnit.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)

        btnCamera.setOnClickListener { takePhotoPet() }
        btnGallery.setOnClickListener { selectPhotoPet() }

        btnSave.setOnClickListener {
            try {

                if (etName.text.isEmpty()) {
                    etName.error = "Name required"
                    return@setOnClickListener
                }
                if (etAge.text.isEmpty()) {
                    etAge.error = "Age required"
                    return@setOnClickListener
                }

                val generatedId = "PET-" + System.currentTimeMillis()
                val gender = if (rbMale.isChecked) "Male" else "Female"

                val bitmap = petPhotoBitmap
                    ?: (imgPhotoPet.drawable as? BitmapDrawable)?.bitmap

                val pet = Pet(
                    id = generatedId,
                    name = etName.text.toString(),
                    breed = spinnerBreed.selectedItem.toString(),
                    gender = gender,
                    age = etAge.text.toString().toInt(),
                    weight = 0.0,
                    ownerId = ownerId,
                    notes = etNotes.text.toString(),
                    photo = bitmap ?: throw Exception("Photo is required")
                )

                petController.addPet(pet)

                Toast.makeText(this, "Pet registered successfully!", Toast.LENGTH_LONG).show()
                finish()

            } catch (e: Exception) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun takePhotoPet() {
        cameraPreviewLauncher.launch(null)
    }

    private fun selectPhotoPet() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        selectImageLauncher.launch(intent)
    }
}
