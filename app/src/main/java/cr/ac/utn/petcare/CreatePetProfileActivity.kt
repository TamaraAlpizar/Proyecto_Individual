package cr.ac.utn.petcare
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.petcare.network.ApiClient
import cr.ac.utn.petcare.network.PetDto
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CreatePetProfileActivity : AppCompatActivity() {

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

        imgPhotoPet = findViewById(R.id.imgPhotoPet)


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
        spinnerBreed.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            breeds
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }


        val units = listOf("Kg", "Lb")
        spinnerUnit.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            units
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        btnCamera.setOnClickListener { takePhotoPet() }
        btnGallery.setOnClickListener { selectPhotoPet() }

        btnSave.setOnClickListener {
            val nameStr = etName.text.toString().trim()
            val ageStr = etAge.text.toString().trim()
            val notesStr = etNotes.text.toString().trim()
            val breedStr = spinnerBreed.selectedItem?.toString() ?: ""

            if (nameStr.isEmpty()) {
                etName.error = "Name required"
                return@setOnClickListener
            }
            if (ageStr.isEmpty()) {
                etAge.error = "Age required"
                return@setOnClickListener
            }
            if (breedStr.isEmpty()) {
                Toast.makeText(this, "Seleccione una raza", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = when {
                rbMale.isChecked -> "Male"
                rbFemale.isChecked -> "Female"
                else -> "Unknown"
            }

            val ageInt = try {
                ageStr.toInt()
            } catch (e: NumberFormatException) {
                etAge.error = "Age must be a number"
                return@setOnClickListener
            }


            val photoBase64: String? = null


            lifecycleScope.launch {
                try {
                    val newPet = PetDto(
                        id = null,
                        petName = nameStr,
                        breed = breedStr,
                        gender = gender,
                        age = ageInt,
                        weight = 0.0,
                        ownerId = ownerId,
                        notes = notesStr,
                        photo = photoBase64
                    )

                    val response = ApiClient.vetApi.createPet(newPet)

                    Toast.makeText(
                        this@CreatePetProfileActivity,
                        "Pet registered successfully!",
                        Toast.LENGTH_LONG
                    ).show()


                    val resultIntent = Intent().apply {
                        putExtra(pet_summary.EXTRA_PET_NAME, response.petName)
                        putExtra(pet_summary.EXTRA_PET_BREED, response.breed)
                        putExtra(pet_summary.EXTRA_PET_AGE, "${response.age} años")
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                } catch (e: HttpException) {
                    Toast.makeText(
                        this@CreatePetProfileActivity,
                        "Server error: ${e.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@CreatePetProfileActivity,
                        "Connection error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
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