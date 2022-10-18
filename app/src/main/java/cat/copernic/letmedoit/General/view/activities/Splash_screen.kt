package cat.copernic.letmedoit.General.view.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cat.copernic.letmedoit.Visitante.view.activities.Login
import cat.copernic.letmedoit.databinding.ActivitySplashScreenBinding

class Splash_screen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenSplash.setKeepOnScreenCondition{ false }
        Handler(Looper.getMainLooper()).postDelayed({
           startActivity(Intent(this, Login::class.java))
        },2000)
    }
}