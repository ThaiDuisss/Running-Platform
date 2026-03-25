package com.running_platform.controller;

import com.running_platform.dto.request.highlightRoute.HighlightRouteRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.HighlightRouteResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.service.HighlightRouteService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/highlight-routes")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HightlightRouteController {

    HighlightRouteService highlightRouteService;

    @GetMapping
    public ApiResponse<List<HighlightRouteResponse>> getHomepageRoutes() {
        return ApiResponse.success(
                "Get homepage highlight routes successfully",
                highlightRouteService.getHomepageRoutes()
        );
    }

    @GetMapping("/by-location")
    public ApiResponse<List<HighlightRouteResponse>> getByLocation(
            @RequestParam String location,
            @RequestParam Integer limit
    ) {
        return ApiResponse.success(
                "Get highlight routes by location successfully",
                highlightRouteService.getRoutesByLocation(location, limit)
        );
    }

    @PostMapping
    public ApiResponse<HighlightRouteResponse> create(
            @RequestBody HighlightRouteRequest request
    ) {
        return ApiResponse.created(
                "Highlight route created successfully",
                highlightRouteService.create(request)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<HighlightRouteResponse> update(
            @PathVariable Long id,
            @RequestBody HighlightRouteRequest request
    ) {
        return ApiResponse.success(
                "Highlight route updated successfully",
                highlightRouteService.update(id, request)
        );
    }

    @PostMapping(value = "/{id}/thumbnail", consumes = "multipart/form-data")
    public ApiResponse<HighlightRouteResponse> uploadThumbnail(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) {
        return ApiResponse.success(
                "Thumbnail uploaded successfully",
                highlightRouteService.uploadThumbnail(id, file)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        highlightRouteService.delete(id);
        return ApiResponse.success(
                "Highlight route deleted successfully",
                null);
    }

    @GetMapping("/admin")
    public ApiResponse<PageResponse<HighlightRouteResponse>> getPage(
            @ModelAttribute HighlightRouteRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                "Get highlight routes (admin) successfully",
                highlightRouteService.getPage(request, page, size)
        );
    }
}
