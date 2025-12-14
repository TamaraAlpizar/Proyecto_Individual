package cr.ac.utn.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.petcare.network.ApiClient
import cr.ac.utn.petcare.network.OwnerDto
import cr.ac.utn.petcare.network.RegisterOwnerRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newaccount)

        val name = findViewById<EditText>(R.id.txtNameSignUp)
        val firstLastName = findViewById<EditText>(R.id.txtFirstLastNameSignUp)
        val secondLastName = findViewById<EditText>(R.id.txtSecondLastNameSignUp)
        val email = findViewById<EditText>(R.id.TextEmailSignUp)
        val password = findViewById<EditText>(R.id.TextPasswordSignUp)
        val phone = findViewById<EditText>(R.id.TextNumber)
        val address = findViewById<EditText>(R.id.TextAddress)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val nameStr = name.text.toString().trim()
            val firstLastStr = firstLastName.text.toString().trim()
            val secondLastStr = secondLastName.text.toString().trim()
            val emailStr = email.text.toString().trim()
            val passStr = password.text.toString().trim()
            val phoneStr = phone.text.toString().trim()
            val addressStr = address.text.toString().trim()

            if (nameStr.isEmpty() || firstLastStr.isEmpty() || emailStr.isEmpty()
                || passStr.isEmpty() || phoneStr.isEmpty() || addressStr.isEmpty()
            ) {
                Toast.makeText(this, "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val request = RegisterOwnerRequest(
                        name = nameStr,
                        fLastName = firstLastStr,
                        sLastName = secondLastStr,
                        email = emailStr,
                        password = passStr,
                        phone = phoneStr,
                        address = addressStr
                    )

                    val owner: OwnerDto = ApiClient.vetApi.registerOwner(request)

                    Toast.makeText(
                        this@NewAccountActivity,
                        "Cuenta creada exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()


                    val prefs = getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                    prefs.edit()
                        .putString("owner_id", owner.id)
                        .putString("owner_name", nameStr)
                        .putString("owner_fLastName", firstLastStr)
                        .putString("owner_sLastName", secondLastStr)
                        .putString("owner_email", emailStr)
                        .putString("owner_phone", phoneStr)
                        .putString("owner_address", addressStr)
                        .apply()


                    val intent = Intent(this@NewAccountActivity, PersonDetailsActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (e: HttpException) {
                    if (e.code() == 400) {
                        Toast.makeText(
                            this@NewAccountActivity,
                            "Datos inválidos para el registro",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@NewAccountActivity,
                            "Error en el servidor (${e.code()})",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@NewAccountActivity,
                        "Error de conexión",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}