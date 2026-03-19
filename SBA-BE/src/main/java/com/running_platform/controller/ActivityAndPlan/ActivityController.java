package com.running_platform.controller.ActivityAndPlan;

import com.running_platform.dto.request.activityAndPlan.ActivityRequest;
import com.running_platform.dto.response.activityAndPlan.ActivityResponse;
import com.running_platform.mapper.ActivityMapper;
import com.running_platform.service.IActivityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {
    private IActivityService activityCrudService;
    private ActivityMapper mapper;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<Page<ActivityResponse>> getAll(
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").descending());
            Page<ActivityResponse> activities = activityCrudService.getAll( pageable).map(
                    mapper::EntityToRespond);
            return ResponseEntity.ok().body(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @GetMapping(value = "/friends", produces = "application/json")
//    public ResponseEntity<Page<ActivityResponse>> getMineAndMyFriends(@RequestHeader(name = "Authorization") String token,
//                                                                 @RequestParam(defaultValue = "0") int page,
//                                                                 @RequestParam(defaultValue = "10") int size) {
//        try {
//            Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").descending());
//            Page<ActivityResponse> activities = activityCrudService.getMineAndMyFriends(token, pageable).map(
//                    activity -> convertToDTO(token, activity, false, true)
//            );
//            return ResponseEntity.ok().body(activities);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<Page<ActivityResponse>> getByUser(@PathVariable long id,
                                                            @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").descending());
            Page<ActivityResponse> activities = activityCrudService.getByUser(id, pageable).map(
                    mapper::EntityToRespond
            );
            return ResponseEntity.ok().body(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<ActivityResponse> create(@RequestBody ActivityRequest activity, @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok().body(mapper.EntityToRespond(activityCrudService.create(mapper.toEntity(activity))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ActivityResponse> retrieve(@PathVariable long id, @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok().body(mapper.EntityToRespond(activityCrudService.getById(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping(value = "/", consumes = "application/json")
    public ResponseEntity<ActivityResponse> update(@RequestBody ActivityRequest activity, @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok().body(mapper.EntityToRespond(activityCrudService.update(mapper.toEntity(activity))));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<String> delete(@RequestParam(value = "id") Long id, @RequestHeader(name = "Authorization") String token) {
        try {
            activityCrudService.delete(id);
            return ResponseEntity.ok("Activity successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    /**
//     * Like an activity.
//     *
//     * @param id    The ID of the activity to like.
//     * @param token The authorization token.
//     * @return boolean
//     */
//    @PostMapping("/like")
//    public ResponseEntity<String> likeActivity(@RequestParam(value = "id") Long id, @RequestHeader(name = "Authorization") String token) {
//        try {
//            activityCrudService.like(id, token);
//            return ResponseEntity.ok("Activity liked successfully");
//        } catch (NotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity not found");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    /**
//     * Dislike an activity.
//     *
//     * @param id    The ID of the activity to dislike.
//     * @param token The authorization token.
//     * @return boolean
//     */
//    @PostMapping("/dislike")
//    public ResponseEntity<String> dislikeActivity(@RequestParam(value = "id") Long id, @RequestHeader(name = "Authorization") String token) {
//        try {
//            activityCrudService.dislike(id, token);
//            return ResponseEntity.ok("Activity disliked successfully");
//        } catch (NotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity not found");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    /**
//     * Creates a comment
//     *
//     * @param comment The string containing the activity comment
//     * @param activityId The activity Id
//     * @param token The authorization token.
//     * @return The created ActivityCommentDto object.
//     */
//    @PostMapping(value = "/comment")
//    public ResponseEntity<ActivityCommentDto> createComment(@RequestParam("comment") String comment, @RequestParam("activityId") Long activityId, @RequestHeader(name = "Authorization") String token) {
//        try {
//            return ResponseEntity.ok().body(convertCommentToDTO(activityCommentService.create(comment, activityId, token)));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    /**
//     * Updates an existing comment.
//     *
//     * @param id The id of the comment.
//     * @param comment The comment.
//     * @param token The authorization token.
//     * @return The updated ActivityCommentDto object.
//     */
//    @PutMapping(value = "/comment")
//    public ResponseEntity<ActivityCommentDto> updateComment(@RequestParam("id") Long id, @RequestParam("comment") String comment, @RequestHeader(name = "Authorization") String token) {
//        try {
//            return ResponseEntity.ok().body(convertCommentToDTO(activityCommentService.update(id, comment, token)));
//        } catch (NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    /**
//     * Deletes a comment.
//     *
//     * @param id The ID of the activity to delete.
//     * @param token The authorization token.
//     * @return A ResponseEntity with the deletion status.
//     */
//    @DeleteMapping(value = "/comment")
//    public ResponseEntity<String> deleteComment(@RequestParam(value = "id") Long id, @RequestHeader(name = "Authorization") String token) {
//        try {
//            activityCommentService.delete(token, id);
//            return ResponseEntity.ok("Comment successfully deleted");
//        } catch (ChangeSetPersister.NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

}
