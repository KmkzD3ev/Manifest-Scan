package com.example.emissormdfe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.emissormdfe.Bd.DatabaseHelper;

import com.google.android.material.navigation.NavigationView;

import br.com.zenitech.emissormdfe.R;
import br.com.zenitech.emissormdfe.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Inicialize o DrawerLayout e NavigationView
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Inicialize o DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Verifique se há um manifesto salvo
        String lastManifestoBarcode = databaseHelper.getLastManifestoBarcode();
        if (lastManifestoBarcode == null || lastManifestoBarcode.isEmpty()) {
            // Desabilite a navegação até que o manifesto seja escaneado
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
            // Habilite a navegação e o DrawerLayout
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        // Configure a navegação
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Defina o comportamento do item do menu para navegar para home
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navController.navigate(R.id.nav_home);
            } else if (id == R.id.nav_gallery) {
                navController.navigate(R.id.nav_gallery);
            } else if (id == R.id.nav_slideshow) {
                navController.navigate(R.id.nav_slideshow);
            }
            drawer.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
