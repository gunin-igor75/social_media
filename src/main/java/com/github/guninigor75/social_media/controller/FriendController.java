package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.UserService;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final UserService userService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                  @AuthenticationPrincipal SecurityUser securityUser) {
        userService.createRequestFriendship(securityUser.getId(), id);
    }

    @PatchMapping("/accepted/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestAcceptFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        userService.acceptedFriendShip(securityUser.getId(), id);
    }

    @PatchMapping("/rejected/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestARejectFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        userService.rejectedFriendShip(securityUser.getId(), id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestDeleteFriend(@PathVariable("id") @PositiveOrZero Long id,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteFriend(securityUser.getId(), id);
    }
}
