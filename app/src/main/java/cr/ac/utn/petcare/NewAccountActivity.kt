package cr.ac.utn.petcare

import Data.PersonMemoryDataManager
import Entity.Person
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class NewAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newaccount)

        val name = findViewById<EditText>(R.id.txtNameSignUp)
        val email = findViewById<EditText>(R.id.TextEmailSignUp)
        val password = findViewById<EditText>(R.id.TextPasswordSignUp)
        val phone = findViewById<EditText>(R.id.TextNumber)
        val address = findViewById<EditText>(R.id.TextAddress)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val nameStr = name.text.toString().trim()
            val emailStr = email.text.toString().trim()
            val passStr = password.text.toString().trim()
            val phoneStr = phone.text.toString().trim()
            val addressStr = address.text.toString().trim()

            if (nameStr.isEmpty() || emailStr.isEmpty() || passStr.isEmpty() ||
                phoneStr.isEmpty() || addressStr.isEmpty()
            ) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val existing = PersonMemoryDataManager.getByEmail(emailStr)
            if (existing != null) {
                Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = Person(
                id = UUID.randomUUID().toString(),
                name = nameStr,
                fLastName = "",
                sLastName = "",
                email = emailStr,
                password = passStr,
                phone = phoneStr.toIntOrNull() ?: 0,
                address = addressStr
            )

            PersonMemoryDataManager.add(newUser)
            Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("person_id", newUser.Id)
            startActivity(intent)
            finish()
        }
    }
}
