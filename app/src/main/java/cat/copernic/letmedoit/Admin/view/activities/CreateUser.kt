package cat.copernic.letmedoit.Admin.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.letmedoit.databinding.ActivityAdminCreateUserBinding
class CreateUser : AppCompatActivity() {
    private lateinit var binding: ActivityAdminCreateUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}