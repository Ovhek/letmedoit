package cat.copernic.letmedoit.presentation.view.general.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cat.copernic.letmedoit.databinding.ActivitySplashScreenBinding

/**
 * Activity que muestra el Splash Screen
 */
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenSplash.setKeepOnScreenCondition{ false }
        Handler(Looper.getMainLooper()).postDelayed({
           startActivity(Intent(this, Home::class.java))
        },2000)
    }
}