package com.example.keepnotes.ui

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.keepnotes.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // important note:
        // make sure that the menu items id is same the fragments/destinations id in the nav_graph.xml

        // this for status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            window.statusBarColor = Color.WHITE

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.BLACK)

        // to show the hamburger icon, and when clicking on it show the navigation drawer
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // to show the navigation drawer menu icons, as you added it, without any changes
        // navView.itemIconTintList = null

        // navigation controller
        val navController: NavController = this.findNavController(R.id.nav_host_fragment)

        /*appBarConfig = AppBarConfiguration(setOf(
            R.id.notes, R.id.tasks, R.id.important, R.id.deleted, R.id.addNote, R.id.addLabel),
            drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)*/

        // for back arrow and drawer layout
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        // for navigation view
        NavigationUI.setupWithNavController(navView, navController)

        // to change the toolbar title like the fragment/destination label
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            toolbar.title = destination.label
        }

    } // end of onCreate method

    // for back arrow and drawer layout
    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
        //return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
    }

    // This companion object For "Keys"
    companion object {
        // this variable for bundle
        const val NOTE = "note"
        const val NOTE_IMAGE = "note_image"
    }

    // this code to hide keyboard
    //val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    //inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

}