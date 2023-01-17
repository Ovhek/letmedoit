package cat.copernic.letmedoit.presentation.view.users.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.databinding.ActivityRecoveryPasswordNewPasswordBinding

/**
 * Activity que infla y gestiona la recuperacion de la contraseña
 */
class RecoveryPasswordNewPassword : AppCompatActivity() {
    lateinit var binding: ActivityRecoveryPasswordNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password_new_password)
    }
}