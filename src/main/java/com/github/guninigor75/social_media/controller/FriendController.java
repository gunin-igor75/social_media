package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Validated
@Tag(name = "Friend Controller", description = "Friend API")
public class FriendController {

    private final UserService userService;

    @Operation(summary = "Friendship Request")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                  @AuthenticationPrincipal SecurityUser securityUser) {
        userService.createRequestFriendship(securityUser.getId(), id);
    }

    @Operation(summary = "Acceptance of friendship")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("/accepted/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestAcceptFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        userService.acceptedFriendShip(securityUser.getId(), id);
    }

    @Operation(summary = "Denial of friendship")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("/rejected/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestARejectFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        userService.rejectedFriendShip(securityUser.getId(), id);
    }

    @Operation(summary = "Unfriending")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestDeleteFriend(@PathVariable("id") @PositiveOrZero Long id,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteFriend(securityUser.getId(), id);
    }
}
