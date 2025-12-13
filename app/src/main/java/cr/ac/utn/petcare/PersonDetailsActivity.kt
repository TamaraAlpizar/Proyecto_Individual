package cr.ac.utn.petcare
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.petcare.network.ApiClient
import cr.ac.utn.petcare.network.UpdateOwnerRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PersonDetailsActivity : AppCompatActivity() {

    private val PREFS = "petcare_prefs"

    private val KEY_OWNER_ID = "owner_id"
    private val KEY_OWNER_NAME = "owner_name"
    private val KEY_OWNER_FLAST = "owner_fLastName"
    private val KEY_OWNER_SLAST = "owner_sLastName"
    private val KEY_OWNER_EMAIL = "owner_email"
    private val KEY_OWNER_PHONE = "owner_phone"
    private val KEY_OWNER_ADDRESS = "owner_address"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_details)

        findViewById<Button>(R.id.btnBackToHome).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnEditPerson).setOnClickListener { editPerson() }

        loadPersonInfo()
    }

    override fun onResume() {
        super.onResume()
        loadPersonInfo()
    }

    private fun loadPersonInfo() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)

        val name = prefs.getString(KEY_OWNER_NAME, "") ?: ""
        val fLast = prefs.getString(KEY_OWNER_FLAST, "") ?: ""
        val sLast = prefs.getString(KEY_OWNER_SLAST, "") ?: ""
        val email = prefs.getString(KEY_OWNER_EMAIL, "") ?: ""
        val phone = prefs.getString(KEY_OWNER_PHONE, "") ?: ""
        val address = prefs.getString(KEY_OWNER_ADDRESS, "") ?: ""

        findViewById<TextView>(R.id.tvName).text = "Nombre: $name $fLast $sLast"
        findViewById<TextView>(R.id.tvEmail).text = "Correo: $email"
        findViewById<TextView>(R.id.tvPhone).text = "Teléfono: $phone"
        findViewById<TextView>(R.id.tvAddress).text = "Dirección: $address"
    }

    private fun editPerson() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)

        val ownerId = prefs.getString(KEY_OWNER_ID, "") ?: ""
        if (ownerId.isEmpty()) {
            Toast.makeText(this, "No se encontró el ID del dueño. Inicie sesión de nuevo.", Toast.LENGTH_LONG).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.activity_dialog_edit_person, null)

        val etName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etFLast = dialogView.findViewById<EditText>(R.id.etEditFirstLastName)
        val etSLast = dialogView.findViewById<EditText>(R.id.etEditSecondLastName)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEditEmail)
        val etPhone = dialogView.findViewById<EditText>(R.id.etEditPhone)
        val etAddress = dialogView.findViewById<EditText>(R.id.etEditAddress)


        etName.setText(prefs.getString(KEY_OWNER_NAME, ""))
        etFLast.setText(prefs.getString(KEY_OWNER_FLAST, ""))
        etSLast.setText(prefs.getString(KEY_OWNER_SLAST, ""))
        etEmail.setText(prefs.getString(KEY_OWNER_EMAIL, ""))
        etPhone.setText(prefs.getString(KEY_OWNER_PHONE, ""))
        etAddress.setText(prefs.getString(KEY_OWNER_ADDRESS, ""))

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar información")
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            val nameStr = etName.text.toString().trim()
            val fLastStr = etFLast.text.toString().trim()
            val sLastStr = etSLast.text.toString().trim()
            val emailStr = etEmail.text.toString().trim()
            val phoneStr = etPhone.text.toString().trim()
            val addressStr = etAddress.text.toString().trim()

            if (nameStr.isEmpty() || fLastStr.isEmpty() || emailStr.isEmpty() || phoneStr.isEmpty() || addressStr.isEmpty()) {
                Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = UpdateOwnerRequest(
                name = nameStr,
                fLastName = fLastStr,
                sLastName = sLastStr,
                email = emailStr,
                phone = phoneStr,
                address = addressStr
            )

            lifecycleScope.launch {
                try {

                    val updated = ApiClient.vetApi.updateOwner(ownerId, body)


                    prefs.edit()
                        .putString(KEY_OWNER_NAME, updated.name)
                        .putString(KEY_OWNER_FLAST, updated.fLastName)
                        .putString(KEY_OWNER_SLAST, updated.sLastName)
                        .putString(KEY_OWNER_EMAIL, updated.email)
                        .putString(KEY_OWNER_PHONE, updated.phone)
                        .putString(KEY_OWNER_ADDRESS, updated.address)
                        .apply()


                    loadPersonInfo()

                    Toast.makeText(this@PersonDetailsActivity, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                } catch (e: HttpException) {
                    Toast.makeText(
                        this@PersonDetailsActivity,
                        "Error servidor (${e.code()})",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@PersonDetailsActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}