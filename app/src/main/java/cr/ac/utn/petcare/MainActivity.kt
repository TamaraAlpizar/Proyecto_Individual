package cr.ac.utn.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch
import cr.ac.utn.petcare.network.ApiClient
import cr.ac.utn.petcare.network.LoginRequest
import cr.ac.utn.petcare.network.OwnerDto
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtEmail = findViewById<EditText>(R.id.editTextText3)
        val txtPassword = findViewById<EditText>(R.id.editTextTextPassword4)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response: OwnerDto = ApiClient.vetApi.login(
                        LoginRequest(email = email, password = password)
                    )


                    val prefs = getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                    prefs.edit()
                        .putString("owner_id", response.id)
                        .putString("owner_name", response.name)
                        .apply()

                    Toast.makeText(
                        this@MainActivity,
                        "Welcome ${response.name}",
                        Toast.LENGTH_LONG
                    ).show()


                    val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                        putExtra("person_id", response.id)
                    }
                    startActivity(intent)
                    finish()

                } catch (e: HttpException) {
                    if (e.code() == 401) {
                        Toast.makeText(
                            this@MainActivity,
                            "Incorrect email or password.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Server error (${e.code()}).",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@MainActivity,
                        "Connection error.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, NewAccountActivity::class.java)
            startActivity(intent)
        }
    }
}