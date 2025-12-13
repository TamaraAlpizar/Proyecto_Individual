package cr.ac.utn.petcare

import Data.PersonMemoryDataManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

            val person = PersonMemoryDataManager.getByEmail(email)

            if (person == null) {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (person.Password != password) {
                Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("person_id", person.Id)
            startActivity(intent)
        }


        btnRegister.setOnClickListener {
            val intent = Intent(this, NewAccountActivity::class.java)
            startActivity(intent)
        }
    }
}