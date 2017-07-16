package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Droche on 27/11/2016.
 * This controller is in charge of handling file uploads.
 */
@Controller
public class ProfilePictureController extends BaseController {

    @Autowired
    public ProfilePictureController(UserService userService) {
        super(userService);
    }

    @RequestMapping(value = "/profile-picture", method = RequestMethod.POST)
    public ModelAndView uploadProfilePicture(@RequestParam(name = "picture") MultipartFile picture) {
        ModelAndView mav = null;
        try {
            userService.changeProfilePicture(getCurrentUser().getId(), picture.getBytes(), picture.getContentType(),
                    getCurrentUser().getId());
//            userService.setProfilePicture(getCurrentUser().getId(), picture.getBytes(), picture.getContentType());
            LOG.info("Updated profile picture for {}", getCurrentUsername());
            mav = new ModelAndView("redirect:/profile?username=" + getCurrentUsername());
        } catch (NoSuchEntityException e) {
            LOG.warn("Attempted to set profile picture of nonexistent user #{}, denied", getCurrentUser().getId());
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error setting profile picture for {}: {}", getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/profile-picture", method = RequestMethod.GET)
    public ResponseEntity getProfilePicture(@RequestParam(value = "username") String username) {
//        User user = userService.findByUsername(username);
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        HttpHeaders responseHeaders = new HttpHeaders();
//        if (user.getProfilePicture() == null) {
//            //No profile picture, redirect to default picture URL
//            try {
//                responseHeaders.setLocation(new URI(Game.DEFAULT_COVER_PICTURE_URL));
//                return new ResponseEntity(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
//            } catch (URISyntaxException e) {
//                LOG.error("Couldn't retrieve default profile picture: {}", e);
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            byte[] data = user.getProfilePicture();
//            responseHeaders.setContentLength(data.length);
//            return new ResponseEntity<>(data, responseHeaders, HttpStatus.OK);
//        }
        return null;
    }

    @RequestMapping(value = "/remove-profile-picture", method = RequestMethod.POST)
    public ModelAndView removeProfilePicture(@RequestParam(name = "returnUrl", required = false, defaultValue = "/profile") String returnUrl) {
        ModelAndView mav = null;
        try {
            userService.removeProfilePicture(getCurrentUser().getId(), getCurrentUser().getId());
            LOG.info("Removed profile picture for {}", getCurrentUsername());
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (Exception e) {
            LOG.error("Error removing profile picture for {}: {}", getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }
}
