package com.example.foodOrderAndTrackingApp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodOrderAndTrackingApp.R
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton

class DeliveryTrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.delivery_tracking)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        intent.extras?.let {
            if (it.containsKey("OrderId") && it.getString("OrderId") != null) {
                val orderId = it.getString("OrderId")
                findViewById<AppCompatTextView>(R.id.tvOrderId).setText("Order Id : ${orderId}")
            }
        }

        findViewById<MaterialButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mapContainer, mapFragment)
            .commit()
        mapFragment.getMapAsync(this);

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLngMuhk = LatLng(22.316372, 114.180087)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.addMarker(
            MarkerOptions()
                .position(latLngMuhk)
                .title("MUHK")
        )

        googleMap.moveCamera(CameraUpdateFactory.zoomTo(18f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngMuhk))
        googleMap.isTrafficEnabled = true
    }
}