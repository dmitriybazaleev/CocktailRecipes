package com.baza.navigation

import android.os.Bundle
import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions

class NavigationController(
    private val fm: FragmentManager,
    @IdRes private val navHostFragmentId: Int
) : Navigator {

    companion object {
        const val TAG = "navigationController"
    }

    private var navController: NavController? = null
    private var navHostFragment: NavHostFragment? = null

    init {
        create()
    }

    private fun create() {
        navHostFragment = fm.findFragmentById(navHostFragmentId) as? NavHostFragment
        navHostFragment?.let { hostFragment ->
            navController = hostFragment.navController
        }
    }

    override fun addFragment(@IdRes destinationId: Int) {
        navController?.navigate(destinationId)
    }

    override fun addFragment(@IdRes destinationId: Int, vararg args: NavArguments) {
        navController?.navigate(destinationId, getArgument(*args))
    }

    override fun addFragment(
        @IdRes destinationId: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int,
        @AnimRes popEnterAnim: Int,
        @AnimRes popExitAnim: Int
    ) {
        val navOption = navOptions {
            anim {
                enter = enterAnim
                exit = exitAnim
                popEnter = popEnterAnim
                popExit = popExitAnim
            }
        }

        navController?.navigate(destinationId, null, navOption)
    }

    override fun addFragment(
        @IdRes destinationId: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int,
        @AnimRes popEnterAnim: Int,
        @AnimRes popExitAnim: Int,
        vararg args: NavArguments
    ) {
        val navOption = navOptions {
            anim {
                enter = enterAnim
                exit = exitAnim
                popEnter = popEnterAnim
                popExit = popExitAnim
            }
        }
        val arguments = getArgument(*args)


        navController?.navigate(destinationId, arguments, navOption)
    }

    override fun popBackStack() {
        navController?.popBackStack()
    }

    private fun getArgument(vararg navArguments: NavArguments): Bundle {
        val newList = mutableListOf<Pair<String, Any>>()

        navArguments.forEach { argument ->
            Log.d(TAG, "Current argument: $argument")
            newList.add(Pair(argument.key, argument.data))
        }

        return bundleOf(*newList.toTypedArray())
    }

    override fun getController(): NavController? = navController

    override fun getCurrentFragment(): Fragment? =
        navHostFragment?.childFragmentManager?.fragments?.get(0)

}