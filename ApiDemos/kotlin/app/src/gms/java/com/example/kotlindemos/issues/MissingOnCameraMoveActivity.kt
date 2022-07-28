package com.example.kotlindemos.issues

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlindemos.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MissingOnCameraMoveActivity : AppCompatActivity(), OnMapReadyCallback {

    private val sydney = LatLng(-33.862, 151.21)
    lateinit var zoomLevelText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing_on_camera_move)

        zoomLevelText = findViewById(R.id.zoom_level)

        val mapFragment : SupportMapFragment? =
                supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f))
            addMarker(MarkerOptions().position(sydney))

            setOnCameraMoveListener {
                zoomLevelText.text = "Zoom level: ${googleMap.cameraPosition.zoom}"
            }

            animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f))
        }
    }
}
