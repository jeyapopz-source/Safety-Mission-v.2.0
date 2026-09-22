package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val pinCode: String,
    val state: String = "Tamil Nadu",
    val district: String,
    val localBody: String,
    val villageOrTown: String,
    val area: String,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey val routeId: String,
    val area: String,
    val pinCode: String,
    val routeName: String,
    val distanceKm: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: String, // "Completed", "Active", "Scheduled"
    val colorHex: String,
    val pathPointsJson: String // Serialized points for canvas map
)

@Entity(tableName = "cleaning_records")
data class CleaningRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pinCode: String,
    val area: String,
    val date: String,
    val distanceCleanedKm: Double,
    val wasteCollectedKg: Double,
    val operatingHours: Double,
    val routesCompleted: Int,
    val lastCleaningTime: String,
    val nextCleaningTime: String,
    val activeMachinesCount: Int,
    val machineId: String
)

@Entity(tableName = "machines")
data class MachineEntity(
    @PrimaryKey val machineId: String,
    val modelName: String,
    val gpsLat: Double,
    val gpsLng: Double,
    val speedKmh: Double,
    val totalTravelDistanceKm: Double,
    val activeCleaningDistanceKm: Double,
    val operatingHours: Double,
    val cleaningModeOn: Boolean,
    val machineStatus: String, // "Active Sweeping", "En Route", "Idle", "Charging"
    val networkStatus: String, // "4G IoT Connected"
    val batteryPercent: Int,
    val assignedRouteId: String,
    val assignedArea: String,
    val assignedPinCode: String
)

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val scheduleId: String,
    val pinCode: String,
    val area: String,
    val machineId: String,
    val scheduledDate: String,
    val shift: String,
    val targetDistanceKm: Double,
    val status: String // "Scheduled", "In Progress", "Completed"
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val referenceNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val locationAddress: String,
    val pinCode: String,
    val category: String, // "Road Not Cleaned", "Waste Accumulation", "Route Missed", "Overflowing Waste", "Damaged Road", "Other"
    val description: String,
    val photoUri: String? = null,
    val status: String = "Submitted" // "Submitted", "Under Inspection", "Action Dispatched", "Resolved"
)
