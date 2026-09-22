package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- Locations ---
    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE pinCode = :pinCode LIMIT 1")
    suspend fun getLocationByPin(pinCode: String): LocationEntity?

    @Query("SELECT * FROM locations WHERE pinCode LIKE '%' || :query || '%' OR area LIKE '%' || :query || '%' OR district LIKE '%' || :query || '%'")
    fun searchLocations(query: String): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<LocationEntity>)

    // --- Routes ---
    @Query("SELECT * FROM routes ORDER BY status ASC, startTime DESC")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routes WHERE pinCode = :pinCode")
    fun getRoutesByPin(pinCode: String): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routes WHERE routeId = :routeId LIMIT 1")
    suspend fun getRouteById(routeId: String): RouteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<RouteEntity>)

    // --- Cleaning Records ---
    @Query("SELECT * FROM cleaning_records ORDER BY id DESC")
    fun getAllCleaningRecords(): Flow<List<CleaningRecordEntity>>

    @Query("SELECT * FROM cleaning_records WHERE pinCode = :pinCode ORDER BY id DESC LIMIT 1")
    fun getLatestCleaningRecordByPin(pinCode: String): Flow<CleaningRecordEntity?>

    @Query("SELECT * FROM cleaning_records WHERE pinCode = :pinCode ORDER BY id DESC LIMIT 1")
    suspend fun getRecordForPin(pinCode: String): CleaningRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCleaningRecords(records: List<CleaningRecordEntity>)

    // --- Machines ---
    @Query("SELECT * FROM machines")
    fun getAllMachines(): Flow<List<MachineEntity>>

    @Query("SELECT * FROM machines WHERE machineId = :machineId LIMIT 1")
    suspend fun getMachineById(machineId: String): MachineEntity?

    @Query("SELECT * FROM machines WHERE assignedPinCode = :pinCode")
    fun getMachinesByPin(pinCode: String): Flow<List<MachineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachines(machines: List<MachineEntity>)

    // --- Schedules ---
    @Query("SELECT * FROM schedules ORDER BY scheduledDate ASC")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE pinCode = :pinCode")
    fun getSchedulesByPin(pinCode: String): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    // --- Citizen Reports ---
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    suspend fun updateReportStatus(id: Long, status: String)

    // Check count for seeding
    @Query("SELECT COUNT(*) FROM locations")
    suspend fun getLocationCount(): Int
}
