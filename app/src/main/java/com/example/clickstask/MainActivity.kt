package com.example.clickstask

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.clickstask.databinding.ActivityMainBinding
import com.example.clickstask.broadcast.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProgressHandle,
    NavController.OnDestinationChangedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var viewRef: ActivityMainBinding

    private lateinit var navController: NavController

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewRef = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewRef.root)

        // initialize navigation component
        setNavComponent()

        // initialize Snack bar
        initSnackBar()

        // initialize Broadcast Receiver
        initBroadcastReceiver()
    }

    private fun initSnackBar() {
        snackBar = Snackbar.make(
            findViewById(R.id.root),
            getString(R.string.checkConnection), Snackbar.LENGTH_INDEFINITE
        )
    }

    private fun initBroadcastReceiver() {
        registerReceiver(
            ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun setNavComponent() {
        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment
        navController = navHostFrag.navController
        navController.setGraph(R.navigation.main_nav, intent.extras)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.homeFragment -> {
                showProgressBar()
            }
            R.id.detailsFragment -> {
                hideProgressBar()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar!!.show()
        } else if (isConnected) {
            if (snackBar!!.isShown) {
                snackBar!!.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(ConnectivityReceiver())
    }
}