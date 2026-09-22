package com.example.data

object DemoDataInitializer {

    suspend fun seedDemoDataIfEmpty(dao: AppDao) {
        if (dao.getLocationCount() > 0) return

        // 1. Locations
        val demoLocations = listOf(
            LocationEntity(
                pinCode = "624306",
                state = "Tamil Nadu",
                district = "Dindigul",
                localBody = "Oddanchatram Municipality",
                villageOrTown = "Oddanchatram",
                area = "Kamarajar Market & Bus Terminal Zone",
                latitude = 10.4852,
                longitude = 77.7472
            ),
            LocationEntity(
                pinCode = "624001",
                state = "Tamil Nadu",
                district = "Dindigul",
                localBody = "Dindigul Corporation",
                villageOrTown = "Dindigul",
                area = "Rock Fort & Palani Road Corridor",
                latitude = 10.3673,
                longitude = 77.9803
            ),
            LocationEntity(
                pinCode = "625001",
                state = "Tamil Nadu",
                district = "Madurai",
                localBody = "Madurai Municipal Corporation",
                villageOrTown = "Madurai City",
                area = "Meenakshi Amman Temple Circle & South Masi",
                latitude = 9.9195,
                longitude = 78.1193
            ),
            LocationEntity(
                pinCode = "600001",
                state = "Tamil Nadu",
                district = "Chennai",
                localBody = "Greater Chennai Corporation (Zone 5)",
                villageOrTown = "Chennai North",
                area = "George Town & Parry's Corner Heritage Arterial",
                latitude = 13.0878,
                longitude = 80.2885
            ),
            LocationEntity(
                pinCode = "641001",
                state = "Tamil Nadu",
                district = "Coimbatore",
                localBody = "Coimbatore Municipal Corporation (Central)",
                villageOrTown = "Coimbatore",
                area = "Gandhipuram & Cross Cut Commercial Avenue",
                latitude = 11.0168,
                longitude = 76.9558
            ),
            LocationEntity(
                pinCode = "620001",
                state = "Tamil Nadu",
                district = "Tiruchirappalli",
                localBody = "Tiruchirappalli Corporation",
                villageOrTown = "Tiruchirappalli",
                area = "Rockfort & Main Guard Gate Corridor",
                latitude = 10.8269,
                longitude = 78.6926
            )
        )
        dao.insertLocations(demoLocations)

        // 2. Routes
        // Statuses: "Completed", "Active", "Scheduled"
        val demoRoutes = listOf(
            RouteEntity(
                routeId = "RT-624306-01",
                area = "Oddanchatram - Market Bypass",
                pinCode = "624306",
                routeName = "Oddanchatram Vegetable Market to NH-83 Link Road",
                distanceKm = 14.8,
                date = "2026-09-22",
                startTime = "05:30 AM",
                endTime = "08:15 AM",
                status = "Completed",
                colorHex = "#198754",
                pathPointsJson = "[{\"x\":0.15,\"y\":0.35},{\"x\":0.25,\"y\":0.42},{\"x\":0.4,\"y\":0.38},{\"x\":0.55,\"y\":0.55}]"
            ),
            RouteEntity(
                routeId = "RT-624306-02",
                area = "Oddanchatram - Bus Terminal Ring",
                pinCode = "624306",
                routeName = "Oddanchatram Central Bus Stand & Railway Feeder Road",
                distanceKm = 8.6,
                date = "2026-09-22",
                startTime = "09:00 AM",
                endTime = "11:30 AM",
                status = "Active",
                colorHex = "#0A3663",
                pathPointsJson = "[{\"x\":0.4,\"y\":0.55},{\"x\":0.55,\"y\":0.68},{\"x\":0.7,\"y\":0.62},{\"x\":0.82,\"y\":0.75}]"
            ),
            RouteEntity(
                routeId = "RT-624306-03",
                area = "Oddanchatram - Dharapuram Highway",
                pinCode = "624306",
                routeName = "Oddanchatram - Dharapuram State Highway Sweeping",
                distanceKm = 19.4,
                date = "2026-09-22",
                startTime = "09:00 PM",
                endTime = "01:30 AM",
                status = "Scheduled",
                colorHex = "#F59E0B",
                pathPointsJson = "[{\"x\":0.82,\"y\":0.75},{\"x\":0.88,\"y\":0.45},{\"x\":0.72,\"y\":0.3},{\"x\":0.6,\"y\":0.2}]"
            ),
            RouteEntity(
                routeId = "RT-625001-01",
                area = "Madurai - Chithirai Veedhi",
                pinCode = "625001",
                routeName = "Meenakshi Amman 4 Chithirai Streets Smart Sweeper Ring",
                distanceKm = 11.2,
                date = "2026-09-22",
                startTime = "04:30 AM",
                endTime = "07:00 AM",
                status = "Completed",
                colorHex = "#198754",
                pathPointsJson = "[{\"x\":0.2,\"y\":0.25},{\"x\":0.5,\"y\":0.22},{\"x\":0.75,\"y\":0.4},{\"x\":0.45,\"y\":0.6}]"
            ),
            RouteEntity(
                routeId = "RT-600001-01",
                area = "Chennai - Rajaji Salai Corridor",
                pinCode = "600001",
                routeName = "George Town to Port Gate 1 Sweeping Corridor",
                distanceKm = 16.5,
                date = "2026-09-22",
                startTime = "06:00 AM",
                endTime = "10:00 AM",
                status = "Active",
                colorHex = "#0A3663",
                pathPointsJson = "[{\"x\":0.3,\"y\":0.2},{\"x\":0.45,\"y\":0.45},{\"x\":0.6,\"y\":0.7},{\"x\":0.75,\"y\":0.85}]"
            )
        )
        dao.insertRoutes(demoRoutes)

        // 3. Cleaning Records
        val demoRecords = listOf(
            CleaningRecordEntity(
                pinCode = "624306",
                area = "Oddanchatram Municipality (Zone 1 & 2)",
                date = "2026-09-22",
                distanceCleanedKm = 23.4,
                wasteCollectedKg = 412.5,
                operatingHours = 5.2,
                routesCompleted = 2,
                lastCleaningTime = "Today, 08:15 AM",
                nextCleaningTime = "Today, 09:00 PM (Night Shift)",
                activeMachinesCount = 2,
                machineId = "TN-SWP-04"
            ),
            CleaningRecordEntity(
                pinCode = "624001",
                area = "Dindigul Central Ward 14",
                date = "2026-09-22",
                distanceCleanedKm = 31.8,
                wasteCollectedKg = 620.0,
                operatingHours = 6.4,
                routesCompleted = 3,
                lastCleaningTime = "Today, 07:45 AM",
                nextCleaningTime = "Tomorrow, 05:00 AM",
                activeMachinesCount = 3,
                machineId = "TN-SWP-02"
            ),
            CleaningRecordEntity(
                pinCode = "625001",
                area = "Madurai Heritage Perimeter",
                date = "2026-09-22",
                distanceCleanedKm = 28.5,
                wasteCollectedKg = 540.8,
                operatingHours = 5.8,
                routesCompleted = 2,
                lastCleaningTime = "Today, 07:00 AM",
                nextCleaningTime = "Today, 10:00 PM",
                activeMachinesCount = 4,
                machineId = "TN-SWP-08"
            ),
            CleaningRecordEntity(
                pinCode = "600001",
                area = "Chennai Zone 5 - George Town",
                date = "2026-09-22",
                distanceCleanedKm = 42.1,
                wasteCollectedKg = 890.3,
                operatingHours = 7.1,
                routesCompleted = 3,
                lastCleaningTime = "Today, 09:10 AM",
                nextCleaningTime = "Today, 11:30 PM",
                activeMachinesCount = 5,
                machineId = "TN-SWP-11"
            )
        )
        dao.insertCleaningRecords(demoRecords)

        // 4. Machines
        val demoMachines = listOf(
            MachineEntity(
                machineId = "TN-SWP-04",
                modelName = "EcoSweep EV-400 Smart Road Sweeper",
                gpsLat = 10.4871,
                gpsLng = 77.7490,
                speedKmh = 12.4,
                totalTravelDistanceKm = 34.2,
                activeCleaningDistanceKm = 23.4,
                operatingHours = 5.2,
                cleaningModeOn = true,
                machineStatus = "Active Sweeping",
                networkStatus = "4G IoT Connected (Telemetry OK)",
                batteryPercent = 78,
                assignedRouteId = "RT-624306-02",
                assignedArea = "Oddanchatram - Kamarajar Bypass",
                assignedPinCode = "624306"
            ),
            MachineEntity(
                machineId = "TN-SWP-05",
                modelName = "EcoSweep Heavy Duty Highway Sweeper",
                gpsLat = 10.4912,
                gpsLng = 77.7523,
                speedKmh = 0.0,
                totalTravelDistanceKm = 18.0,
                activeCleaningDistanceKm = 14.8,
                operatingHours = 3.1,
                cleaningModeOn = false,
                machineStatus = "Standby / Depo Charging",
                networkStatus = "4G IoT Connected",
                batteryPercent = 94,
                assignedRouteId = "RT-624306-03",
                assignedArea = "Oddanchatram Depo Base",
                assignedPinCode = "624306"
            ),
            MachineEntity(
                machineId = "TN-SWP-08",
                modelName = "Compact City Smart Sweeper MK-2",
                gpsLat = 9.9210,
                gpsLng = 78.1215,
                speedKmh = 14.0,
                totalTravelDistanceKm = 46.5,
                activeCleaningDistanceKm = 28.5,
                operatingHours = 5.8,
                cleaningModeOn = true,
                machineStatus = "Active Sweeping",
                networkStatus = "5G Mesh Connected",
                batteryPercent = 65,
                assignedRouteId = "RT-625001-01",
                assignedArea = "Madurai Meenakshi Perimeter",
                assignedPinCode = "625001"
            ),
            MachineEntity(
                machineId = "TN-SWP-11",
                modelName = "EcoSweep Turbo Electric Sweeper",
                gpsLat = 13.0890,
                gpsLng = 80.2901,
                speedKmh = 11.8,
                totalTravelDistanceKm = 58.2,
                activeCleaningDistanceKm = 42.1,
                operatingHours = 7.1,
                cleaningModeOn = true,
                machineStatus = "Active Sweeping",
                networkStatus = "4G IoT Connected",
                batteryPercent = 52,
                assignedRouteId = "RT-600001-01",
                assignedArea = "Chennai George Town Arterial",
                assignedPinCode = "600001"
            )
        )
        dao.insertMachines(demoMachines)

        // 5. Schedules
        val demoSchedules = listOf(
            ScheduleEntity(
                scheduleId = "SCH-624306-M1",
                pinCode = "624306",
                area = "Oddanchatram Bus Stand & Vegetable Market",
                machineId = "TN-SWP-04",
                scheduledDate = "Tomorrow, 2026-09-23",
                shift = "Early Morning (05:00 AM - 08:30 AM)",
                targetDistanceKm = 18.0,
                status = "Scheduled"
            ),
            ScheduleEntity(
                scheduleId = "SCH-624306-N1",
                pinCode = "624306",
                area = "Dharapuram - Palani Link Road",
                machineId = "TN-SWP-05",
                scheduledDate = "Tonight, 2026-09-22",
                shift = "Night Shift (09:30 PM - 02:00 AM)",
                targetDistanceKm = 22.0,
                status = "Scheduled"
            ),
            ScheduleEntity(
                scheduleId = "SCH-625001-M1",
                pinCode = "625001",
                area = "Madurai Town Hall & Periyar Bus Stand Road",
                machineId = "TN-SWP-08",
                scheduledDate = "Tomorrow, 2026-09-23",
                shift = "Morning (04:30 AM - 08:00 AM)",
                targetDistanceKm = 15.5,
                status = "Scheduled"
            )
        )
        dao.insertSchedules(demoSchedules)

        // 6. Citizen Reports Demo Data
        val demoReports = listOf(
            ReportEntity(
                referenceNumber = "TN-REP-2026-081",
                timestamp = System.currentTimeMillis() - (1000L * 60 * 120),
                locationAddress = "Gandhi Market Entry Road, Oddanchatram",
                pinCode = "624306",
                category = "Waste Accumulation",
                description = "Vegetable crates and plastic waste accumulated along the median edge.",
                photoUri = null,
                status = "Action Dispatched"
            ),
            ReportEntity(
                referenceNumber = "TN-REP-2026-079",
                timestamp = System.currentTimeMillis() - (1000L * 60 * 360),
                locationAddress = "Near South Gate Police Booth, Madurai",
                pinCode = "625001",
                category = "Road Not Cleaned",
                description = "Left curb was missed during morning sweeping shift due to parked delivery vans.",
                photoUri = null,
                status = "Under Inspection"
            )
        )
        for (report in demoReports) {
            dao.insertReport(report)
        }
    }
}
