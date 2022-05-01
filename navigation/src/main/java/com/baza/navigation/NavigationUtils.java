package com.baza.navigation;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;

public final class NavigationUtils {

    public static final String TAG = "navUtils";

    /**
     * Настраивает {@link BottomNavigationView} для использования с {@link NavController}. Это вызовет
     * {@link #onNavDestinationSelected (MenuItem, NavController)} при выборе пункта меню.
     *
     * @param bottomNav  BottomNavigationView, который должен синхронизироваться с
     *                   изменения в NavController.
     * @param controller Контроллер NavController, который предоставляет главное меню.
     *                   Действия навигации в этом NavController будут отражены в
     *                   выбранный элемент в BottomNavigationView.
     */
    public static void setUpWithBottomNav(
            @NonNull final BottomNavigationView bottomNav,
            @NonNull final NavController controller) {
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == bottomNav.getSelectedItemId()) return false;

            return onNavDestinationSelected(item, controller);
        });
        final WeakReference<BottomNavigationView> weakReference =
                new WeakReference<>(bottomNav);
        controller.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(
                            @NonNull NavController controller,
                            @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        Log.d(TAG, "destination changed: " + destination.getId());
                        final BottomNavigationView view = weakReference.get();
                        if (view == null) {
                            controller.removeOnDestinationChangedListener(this);
                            return;
                        }
                        Menu menu = view.getMenu();
                        for (int h = 0, size = menu.size(); h < size; h++) {
                            MenuItem item = menu.getItem(h);
                            if (matchDestination(destination, item.getItemId())) {
                                item.setChecked(true);
                            }
                        }
                    }
                });
    }

    @SuppressWarnings("WeakerAccess") /* synthetic access */
    private static boolean matchDestination(@NonNull NavDestination destination, @IdRes int destId) {
        NavDestination currentDestination = destination;
        while (currentDestination.getId() != destId && currentDestination.getParent() != null) {
            currentDestination = currentDestination.getParent();
        }
        return currentDestination.getId() == destId;
    }

    public static boolean onNavDestinationSelected(
            @NonNull MenuItem item,
            @NonNull NavController navController
    ) {
        final NavOptions.Builder builder = new NavOptions.Builder()
                .setLaunchSingleTop(true);
        if (navController.getCurrentDestination().getParent().findNode(item.getItemId())
                instanceof ActivityNavigator.Destination) {
            builder.setEnterAnim(R.anim.nav_default_enter_anim)
                    .setExitAnim(R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(R.anim.nav_default_pop_exit_anim);

        } else {
            builder.setEnterAnim(R.animator.nav_default_enter_anim)
                    .setExitAnim(R.animator.nav_default_exit_anim)
                    .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
                    .setPopExitAnim(R.animator.nav_default_pop_exit_anim);
        }
        if ((item.getOrder() & Menu.CATEGORY_SECONDARY) == 0) {
            builder.setPopUpTo(findStartDestination(
                    navController.getGraph()).getId(), false);
        }
        final NavOptions options = builder.build();
        try {
            navController.navigate(item.getItemId(), null, options);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Находит фактическое начальное место назначения графика, обрабатывая случаи, когда начало графика
     * пункт назначения сам по себе является NavGraph.
     */
    @SuppressWarnings("WeakerAccess") /* synthetic access */
    private static NavDestination findStartDestination(@NonNull NavGraph graph) {
        NavDestination startDestination = graph;
        while (startDestination instanceof NavGraph) {
            NavGraph parent = (NavGraph) startDestination;
            startDestination = parent.findNode(parent.getStartDestination());
        }
        return startDestination;
    }
}
