package com.example.rma_proj_esus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.rma_proj_esus.cats.repository.CatsRepository
import com.example.rma_proj_esus.navigation.AppNav
import rs.edu.raf.raf_vezbe3compose.core.theme.Rma_ProjTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CatsRepository.initialize(this)
        setContent {
            Rma_ProjTheme {
                AppNav()
            }
        }
    }
}



