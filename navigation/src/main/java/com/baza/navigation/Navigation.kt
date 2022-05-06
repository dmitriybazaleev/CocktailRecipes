package com.baza.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.attachController(
    navController: NavController?
) {
    navController?.let { controllerNotNull ->
        NavigationUtilsNew.setUpWithNavController(this, controllerNotNull)
    }
}

fun NavController.setUpWithBottomNavigation(
    bottomNavigationView: BottomNavigationView?
) {
    bottomNavigationView?.let { bottomNav ->
        NavigationUtilsNew.setUpWithNavController(bottomNav, this)
    }
}

internal fun NavController.popAllInstances(
    @IdRes destinationId: Int,
    inclusive: Boolean
): Boolean {
    var popped: Boolean

    while (true) {
        popped = popBackStack(destinationId, inclusive)

        if (!popped) {
            break
        }
    }

    return popped
}