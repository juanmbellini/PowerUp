package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Droche on 27/11/2016.
 * This controller is in charge of handling file uploads.
 */
@Controller
public class FileController extends BaseController {


    @Autowired
    public FileController(UserService userService) {
        super(userService);
    }

    @RequestMapping(value = "/upload-file", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam(name = "file") MultipartFile file,
                                   @RequestParam(name = "username") String username) {
        if(username==null){
            //error TODO
            return null;
        }
        if(userService.existsWithUsername(username)){
            User user = userService.findByUsername(username);
            if(user!=null){
                try{
                    userService.setProfilePicture(user.getId(),file.getBytes());
                }catch (Exception e){
                    e.printStackTrace();
                    //TODO
                }

            }
        }

        //
        //
//        if (errors.hasErrors()) {
//            return uploadFile(gameId, reviewForm);
//        }
        //TODO only user should be able

        return new ModelAndView("redirect:profile?username="+username);

    }

    @ResponseBody
    @RequestMapping(value = "/profile-picture", method = RequestMethod.GET)
    public byte[] getProfilePicture(@RequestParam(value = "username") String username) {
        byte[] img = new byte[0];
        if (username == null) {
            return img;
        }
        if(!userService.existsWithUsername(username)){
            return img;
        }
        User user = userService.findByUsername(username);
        if(user==null || user.getProfilePicture() == null){
            return img;
        }
        return user.getProfilePicture();

        //
        //
//        if (errors.hasErrors()) {
//            return uploadFile(gameId, reviewForm);
//        }
        //TODO only user should be able


    }



}
