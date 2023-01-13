package com.example.kotlindemos.issues

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlindemos.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions


class LabelTextsNotStyledActivity : AppCompatActivity(), OnMapReadyCallback {

    val SYDNEY = LatLng(-33.862, 151.21)
    val ZOOM_LEVEL = 15f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_map_demo)
        val mapFragment : SupportMapFragment? =
                supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@LabelTextsNotStyledActivity, R.raw.style_json
                )
            )

            moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))
        }
    }
}
