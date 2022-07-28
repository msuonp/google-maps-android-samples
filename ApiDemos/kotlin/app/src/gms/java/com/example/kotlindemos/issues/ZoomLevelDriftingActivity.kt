package com.example.kotlindemos.issues

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlindemos.R
import com.example.kotlindemos.TileOverlayDemoActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.math.round

class ZoomLevelDriftingActivity : AppCompatActivity(), OnMapReadyCallback {

    private val sydney = LatLng(-33.862, 151.21)
    lateinit var zoomLevelText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_level_drifting)

        zoomLevelText = findViewById(R.id.zoom_level)

        val mapFragment : SupportMapFragment? =
                supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        with(map) {
            setMaxZoomPreference(MOON_ZOOM_MAX.toFloat())
            moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, MOON_ZOOM_MAX.toFloat()))
            addMarker(MarkerOptions().position(sydney))

            addTileOverlay(map)

            updateZoomLevelText(map)
            setOnCameraMoveListener {
                updateZoomLevelText(map)
            }

            findViewById<Button>(R.id.zoom_in).setOnClickListener {
                val currentCameraPosition = map.cameraPosition
                val newZoom = round(currentCameraPosition.zoom + 1)
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(currentCameraPosition)
                            .zoom(newZoom)
                            .build())
                )
            }

            findViewById<Button>(R.id.zoom_out).setOnClickListener {
                val currentCameraPosition = map.cameraPosition
                val newZoom = round(currentCameraPosition.zoom - 1)
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(currentCameraPosition)
                            .zoom(newZoom)
                            .build())
                )
            }
        }
    }

    private fun updateZoomLevelText(map: GoogleMap) {
        zoomLevelText.text = "Zoom level: ${map.cameraPosition.zoom}"
    }

    private fun addTileOverlay(map: GoogleMap) {
        val tileProvider: TileProvider = object : UrlTileProvider(256, 256) {
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
                if (zoom < MOON_ZOOM_MIN || zoom > MOON_ZOOM_MAX) {
                    return null
                }

                //Log.d("TEST", "Thread ${Thread.currentThread().name}: Getting tile x = $x, y = $y, z = $zoom")

                val reversedY = (1 shl zoom) - y - 1
                val s = String.format(Locale.US, MOON_MAP_URL_FORMAT, zoom, x, reversedY)
                var url: URL? = null
                url = try {
                    URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }

                Thread.sleep(1000)

                return url
            }
        }
        map.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(tileProvider)
        )
    }

    companion object {
        private const val MOON_ZOOM_MIN = 8
        private const val MOON_ZOOM_MAX = 9
        private const val MOON_MAP_URL_FORMAT =
            "https://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%d/%d/%d.jpg"
    }
}
