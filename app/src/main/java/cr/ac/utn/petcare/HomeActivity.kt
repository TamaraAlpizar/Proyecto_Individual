package cr.ac.utn.petcare

import Data.PersonMemoryDataManager
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val personId = intent.getStringExtra("person_id")


        val user = PersonMemoryDataManager.getById(personId ?: "")


        val tvGreeting = findViewById<TextView>(R.id.tvGreetingHome)
        val btnProfile = findViewById<LinearLayout>(R.id.btnProfile)
        val btnAppointment = findViewById<LinearLayout>(R.id.btnAppointment)
        val btnMascotas = findViewById<LinearLayout>(R.id.btnMascotas)


        if (user != null) {
            tvGreeting.text = "Hola ${user.Name}"
        } else {
            tvGreeting.text = "Hola!"
        }


        btnProfile.setOnClickListener {
            val intent = Intent(this, PersonDetailsActivity::class.java)
            intent.putExtra("person_id", personId)
            startActivity(intent)
        }


        btnAppointment.setOnClickListener {
            val intent = Intent(this, AgendarCitaActivity::class.java)
            intent.putExtra("person_id", personId)
            startActivity(intent)
        }

        // ----- Botón Mascotas -----
        //btnMascotas.setOnClickListener {
            //val intent = Intent(this, PetListActivity::class.java)
           // intent.putExtra("owner_id", personId)
           // startActivity(intent)
        //}
    }
}
