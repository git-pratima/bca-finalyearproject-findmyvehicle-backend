package com.pratima.bca.findmyvehicle.dto.dashboard;

import java.util.List;

public record DashboardData(
        DashboardUser user,
        DashboardSummary summary,
        DashboardActivity activity,
        long unreadNotificationCount,
        List<DashboardVehicle> recentMissingVehicles) {

    public record DashboardUser(Long id, String name, String email, String profileImageUrl) {}

    public record DashboardSummary(
            long totalReports, long recovered, long inProgress, long totalRegisteredVehicles) {}

    public record DashboardActivity(Integer unreadNotifications, Integer unreadMessages) {}

    public record DashboardVehicle(
            Long id,
            String name,
            String registration,
            String location,
            String reportedAt,
            String image,
            String chassis,
            String engine,
            String color,
            String description,
            String status) {}
}