package com.running_platform.dto;

public interface FollowUserProjection {
    Long getId();
    String getUsername();
    String getFullName();
    String getImageUrl();
    String getAddress();
    Double getDistanceKm();
}
