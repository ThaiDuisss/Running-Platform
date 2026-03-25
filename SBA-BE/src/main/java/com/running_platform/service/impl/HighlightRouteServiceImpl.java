package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.highlightRoute.HighlightRouteRequest;
import com.running_platform.dto.response.HighlightRouteResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.entity.RunActivities.HighlightRoute;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.HighlightRouteMapper;
import com.running_platform.repository.HighlightRoute.HighlightRouteRepository;
import com.running_platform.service.CloudinaryService;
import com.running_platform.service.HighlightRouteService;
import com.running_platform.util.DistanceUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class HighlightRouteServiceImpl implements HighlightRouteService {

    HighlightRouteRepository highlightRouteRepository;
    HighlightRouteMapper highlightRouteMapper;
    CloudinaryService cloudinaryService;

    @Override
    public HighlightRouteResponse create(HighlightRouteRequest request) {

        HighlightRoute route = highlightRouteMapper.toEntity(request);

        route.setThumbnail(null); // chưa có ảnh

        String polyline = request.getPolyline();
        Double totalDistance = DistanceUtils.calculateDistanceFromPolyline(polyline);
        route.setDistanceLabel(String.valueOf(totalDistance));

        HighlightRoute saved = highlightRouteRepository.save(route);

        return highlightRouteMapper.EntityToRespond(saved);
    }

    @Override
    public HighlightRouteResponse update(Long id, HighlightRouteRequest request) {

        HighlightRoute route = highlightRouteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.BAD_REQUEST));

        highlightRouteMapper.update(route, request);

        HighlightRoute saved = highlightRouteRepository.save(route);

        return highlightRouteMapper.EntityToRespond(saved);
    }

    @Override
    public HighlightRouteResponse uploadThumbnail(Long routeId, MultipartFile file) {

        HighlightRoute route = highlightRouteRepository.findById(routeId)
                .orElseThrow(() -> new AppException(ErrorEnum.BAD_REQUEST));

        String url = cloudinaryService.uploadHighlightRouteImage(file, routeId);

        route.setThumbnail(url);

        HighlightRoute saved = highlightRouteRepository.save(route);

        return highlightRouteMapper.EntityToRespond(saved);
    }

    @Override
    public List<HighlightRouteResponse> getHomepageRoutes() {

        PageRequest pageable = PageRequest.of(
                0,
                6,
                Sort.by("priority").ascending()
        );

        Page<HighlightRoute> page = highlightRouteRepository.findAllByIsActiveTrue(pageable);
        return page.getContent()
                .stream()
                .map(highlightRouteMapper::EntityToRespond)
                .toList();
    }

    @Override
    public List<HighlightRouteResponse> getRoutesByLocation(
            String location, Integer limit
    ) {

        if (limit <= 0) {
            throw new AppException(ErrorEnum.BAD_REQUEST);
        }

        PageRequest pageable = PageRequest.of(
                0,
                limit,
                Sort.by("priority").ascending()
        );

        return highlightRouteRepository
                .findByLocationAndIsActiveTrue(location.trim(), pageable)
                .getContent()
                .stream()
                .map(highlightRouteMapper::EntityToRespond)
                .toList();
    }

    @Override
    public void delete(Long id) {
        HighlightRoute route = highlightRouteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.BAD_REQUEST));

        route.setIsActive(false);
        route.setIsDeleted(true);

        highlightRouteRepository.save(route);
    }

    @Override
    public PageResponse<HighlightRouteResponse> getPage(
            HighlightRouteRequest request,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("priority").ascending()
        );

        // Lấy dữ liệu
        Page<HighlightRoute> pageData =
                highlightRouteRepository.search(request, pageable);

        // Map trực tiếp từ Entity -> Response
        Page<HighlightRouteResponse> responsePage =
                pageData.map(highlightRouteMapper::EntityToRespond);

        // Convert sang PageResponse
        return PageResponse.from(responsePage);
    }
}
