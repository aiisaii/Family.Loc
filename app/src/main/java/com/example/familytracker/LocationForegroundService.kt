package com.example.familytracker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LocationForegroundService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var database = FirebaseDatabase.getInstance().reference
    private var auth = FirebaseAuth.getInstance()

    companion object {
        const val CHANNEL_ID = "LocationServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)
        startLocationUpdates()
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = android.app.PendingIntent.getActivity(
            this, 0, notificationIntent, android.app.PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.location_service_notification_title))
            .setContentText(getString(R.string.location_service_notification_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground) // You need to create this drawable
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(10000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    updateLocationInFirebase(location)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            // This would happen if permissions are not granted.
            // The service should be started only after permissions are granted.
        }
    }

    private fun updateLocationInFirebase(location: Location) {
        val userId = auth.currentUser?.uid ?: return
        val locationData = mapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "timestamp" to System.currentTimeMillis(),
            "email" to (auth.currentUser?.email ?: "Unknown")
        )
        database.child("locations").child(userId).setValue(locationData)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
