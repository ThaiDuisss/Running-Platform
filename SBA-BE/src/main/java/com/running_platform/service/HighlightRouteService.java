package com.running_platform.service;

import com.running_platform.dto.request.HighlightRouteRequest;
import com.running_platform.dto.response.HighlightRouteResponse;
import com.running_platform.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HighlightRouteService {
    HighlightRouteResponse create(HighlightRouteRequest request);
    HighlightRouteResponse update(Long id, HighlightRouteRequest request);
    HighlightRouteResponse uploadThumbnail(Long routeId, MultipartFile file);
    List<HighlightRouteResponse> getRoutesByLocation(String location, Integer limit);
    List<HighlightRouteResponse> getHomepageRoutes();
    void delete(Long id);
    PageResponse<HighlightRouteResponse> getPage(HighlightRouteRequest request ,int page, int size);
}
