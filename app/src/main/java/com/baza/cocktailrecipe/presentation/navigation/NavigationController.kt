package com.baza.cocktailrecipe.presentation.navigation

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.baza.cocktailrecipe.R

class NavigationController(
    private val fm: FragmentManager
) : Navigator {

    companion object {
        const val TAG = "navigationController"
    }

    private var navController: NavController? = null

    init {
        initBase()
    }

    private fun initBase() {
        val navHostFragment = fm.findFragmentById(R.id.fcv_main) as? NavHostFragment
        navHostFragment?.let { hostFragment ->
            navController = hostFragment.navController
        }
    }

    override fun addFragment(destinationId: Int) {
        navController?.navigate(destinationId)
    }

    override fun addFragment(destinationId: Int, vararg args: NavArguments) {
        val currentArgument = getArgument(*args)

        navController?.navigate(destinationId, currentArgument)
    }

    override fun addFragment(
        destinationId: Int,
        enterAnim: Int,
        exitAnim: Int,
        popEnterAnim: Int,
        popExitAnim: Int
    ) {
        val navOption = navOptions {
            anim {
                enter = enterAnim
                exit = exitAnim
                popEnter = popEnterAnim
                popExit = popExitAnim
            }
        }

        navController?.navigate(destinationId, null,  navOption)
    }

    override fun addFragment(
        destinationId: Int,
        enterAnim: Int,
        exitAnim: Int,
        popEnterAnim: Int,
        popExitAnim: Int,
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
            d("Current argument: $argument")
            newList.add(Pair(argument.key, argument.data))
        }

        return bundleOf(*newList.toTypedArray())
    }

    private fun d(message: String?) {
        message?.let { messageStr ->
            Log.d(TAG, messageStr)
        }
    }

    override fun getController(): NavController? = navController


}