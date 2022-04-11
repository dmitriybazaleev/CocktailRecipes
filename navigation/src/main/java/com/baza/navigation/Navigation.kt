package com.baza.navigation

import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.attachController(
    navController: NavController?
) {
    navController?.let { controllerNotNull ->
        NavigationUtils.setUpWithBottomNav(this, controllerNotNull)
    }
}