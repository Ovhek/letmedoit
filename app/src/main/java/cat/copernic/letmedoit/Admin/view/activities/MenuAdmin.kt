package cat.copernic.letmedoit.Admin.model.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.databinding.ActivityMenuAdminBinding

class MenuAdmin : AppCompatActivity() {
    lateinit var binding : ActivityMenuAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {

    }
}