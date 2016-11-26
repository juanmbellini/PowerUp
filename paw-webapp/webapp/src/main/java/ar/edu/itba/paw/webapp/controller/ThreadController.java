package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.ThreadService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ThreadController extends BaseController {

    private final ThreadService threadService;

    @Autowired
    public ThreadController(UserService us, ThreadService threadService) {
        super(us);
        this.threadService = threadService;
    }

    @RequestMapping("/thread")
    public ModelAndView thread(@RequestParam(name = "id") long threadId) {
        ModelAndView mav = new ModelAndView("thread");
        Thread thread = threadService.findById(threadId);
        if (thread == null) {
            mav = new ModelAndView("error404");
        } else {
            mav.addObject("thread", threadService.findById(threadId));
        }
        return mav;
    }

    @RequestMapping(value = "/create-thread", method = RequestMethod.POST)
    public ModelAndView createThread(@RequestParam(name = "title") String title, @RequestParam(name = "initialComment", required = false, defaultValue = "") String initialComment) {
        ModelAndView mav = null;
        try {
            Thread thread = threadService.create(title, getCurrentUser().getId(), initialComment);
            LOG.info("{} created thread #{} with title \"{}\"", getCurrentUsername(), thread.getId(), thread.getTitle());
            mav = new ModelAndView("redirect:/thread?id=" + thread.getId());
        } catch(Exception e) {
            LOG.error("Error creating thread: {}", e);
            return new ModelAndView("error500");
        }
        return mav;
    }
}
