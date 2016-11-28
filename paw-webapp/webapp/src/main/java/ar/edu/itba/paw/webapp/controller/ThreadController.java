package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.form.CommentForm;
import ar.edu.itba.paw.webapp.form.CreateThreadForm;
import ar.edu.itba.paw.webapp.interfaces.ThreadService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.Set;

@Controller
public class ThreadController extends BaseController {

    private final ThreadService threadService;

    @Autowired
    public ThreadController(UserService us, ThreadService threadService) {
        super(us);
        this.threadService = threadService;
    }

    @RequestMapping("/threads")
    public ModelAndView recentThreads() {
        ModelAndView mav = new ModelAndView("threads");
        Set<Thread> threads = threadService.findHottest(50);
        mav.addObject("threads", threads);
        return mav;
    }

    @RequestMapping("/thread")
    public ModelAndView thread(@RequestParam(name = "id") long threadId, @ModelAttribute("commentForm") final CommentForm form) {
        ModelAndView mav = new ModelAndView("thread");
        Thread thread = threadService.findById(threadId);
        if (thread == null) {
            mav = new ModelAndView("error404");
        } else {
            mav.addObject("thread", thread);
            form.setThreadId(thread.getId());
        }
        return mav;
    }

    @RequestMapping(value = "/create-thread", method = RequestMethod.GET)
    public ModelAndView createThreadGet(@ModelAttribute("createThreadForm") final CreateThreadForm form) {
        return new ModelAndView("create-thread");
    }

    @RequestMapping(value = "/create-thread", method = {RequestMethod.POST})
    public ModelAndView createThread(@Valid @ModelAttribute("createThreadForm") final CreateThreadForm form, final BindingResult errors) {

        if (errors.hasErrors()) {
            return createThreadGet(form);
        }
        //Valid, create
        ModelAndView mav = null;
        try {
            String title = form.getTitle().trim(),
                initialComment = form.getInitialComment().trim();
            Thread thread = threadService.create(title, getCurrentUser().getId(), initialComment);
            LOG.info("{} created thread #{} with title \"{}\"", getCurrentUsername(), thread.getId(), title);
            mav = new ModelAndView("redirect:/thread?id=" + thread.getId());
        } catch (Exception e) {
            LOG.error("Error creating thread: {}", e);
            return new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/comment", method = {RequestMethod.POST})
    public ModelAndView comment(@Valid @ModelAttribute("commentForm") final CommentForm form, final BindingResult errors) {

        if (errors.hasErrors()) {
            return thread(form.getThreadId(), form);
        }
        //Valid, create
        ModelAndView mav = null;
        try {
            String commentContent = form.getComment().trim();
            Comment comment = threadService.comment(form.getThreadId(), getCurrentUser().getId(), commentContent);
            LOG.info("{} commented on thread #{}: \"{}\"", getCurrentUsername(), form.getThreadId(), commentContent);
            mav = new ModelAndView("redirect:/thread?id=" + form.getThreadId() + "#" + comment.getId());
        } catch (Exception e) {
            LOG.error("Error creating thread: {}", e);
            return new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/reply", method = {RequestMethod.POST})
    public ModelAndView reply(@RequestParam(name = "threadId") final long threadId,
                              @RequestParam(name = "parentCommentId") final long parentCommentId,
                              @RequestParam(name = "reply") String reply) {
        if (reply.isEmpty()) {
            LOG.warn("Empty reply to comment {}, denied", parentCommentId);
            return new ModelAndView("error400");
        }
        try {
            reply = reply.trim();
            Comment createdReply = threadService.replyToComment(parentCommentId, getCurrentUser().getId(), reply);
            LOG.info("{} replied to comment #{} with \"{}\"", getCurrentUsername(), parentCommentId, reply);
            return new ModelAndView("redirect:/thread?id=" + threadId + "#" + createdReply.getId());
        } catch (Exception e) {
            LOG.error("Error creating reply to comment #{}: {}", parentCommentId, e);
            return new ModelAndView("error500");
        }
    }

    @RequestMapping(value = "/like-thread", method = RequestMethod.POST)
    public ModelAndView likeThread(@RequestParam(name = "threadId") final long threadId,
                                   @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.likeThread(threadId, getCurrentUser().getId());
            LOG.info("{} liked thread #{}", getCurrentUsername(), threadId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to like nonexistent thread #{}, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error liking thread #{} for {}: {}", threadId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/unlike-thread", method = RequestMethod.POST)
    public ModelAndView unlikeThread(@RequestParam(name = "threadId") final long threadId,
                                     @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.unlikeThread(threadId, getCurrentUser().getId());
            LOG.info("{} unliked thread #{}", getCurrentUsername(), threadId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to unlike nonexistent thread #{}, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error liking thread #{} for {}: {}", threadId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/edit-thread-title", method = RequestMethod.POST)
    public ModelAndView editThread(@RequestParam(name = "threadId") final long threadId,
                                    @RequestParam(name = "newTitle") String newTitle,
                                    @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            newTitle = newTitle.trim();
            threadService.changeTitle(threadId, getCurrentUser().getId(), newTitle);
            LOG.info("{} changed thread #{}'s title to \"{}\"", getCurrentUsername(), threadId, newTitle);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (UnauthorizedException e) {
            LOG.warn("{} attempted to edit title of thread thread #{} which is not theirs, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error400"); //TODO make 403 page
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to edit title of nonexistent thread #{}, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error editing title of thread #{} for {}: {}", threadId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/edit-thread-initial-comment", method = RequestMethod.POST)
    public ModelAndView deleteThread(@RequestParam(name = "threadId") final long threadId,
                                     @RequestParam(name = "newComment") final String newInitialComment,
                                     @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.changeInitialComment(threadId, getCurrentUser().getId(), newInitialComment.trim());
            LOG.info("{} changed initial comment of thread #{}", getCurrentUsername(), threadId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (UnauthorizedException e) {
            LOG.warn("{} attempted to change initial comment of thread #{} which is not theirs, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error400"); //TODO make 403 page
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to change initial comment of nonexistent thread #{}, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error changing initial comment of thread #{} by {}: {}", threadId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/delete-thread", method = RequestMethod.POST)
    public ModelAndView deleteThread(@RequestParam(name = "threadId") final long threadId,
                                      @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.deleteThread(threadId, getCurrentUser().getId());
            LOG.info("{} deleted thread #{}", getCurrentUsername(), threadId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (UnauthorizedException e) {
            LOG.warn("{} attempted to delete thread #{} which is not theirs, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error400"); //TODO make 403 page
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to delete nonexistent thread #{}, denied", getCurrentUsername(), threadId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error deleting thread #{} by {}: {}", threadId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/like-comment", method = RequestMethod.POST)
    public ModelAndView likeComment(@RequestParam(name = "commentId") final long commentId,
                                    @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.likeComment(commentId, getCurrentUser().getId());
            LOG.info("{} liked comment #{}", getCurrentUsername(), commentId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to like nonexistent comment #{}, denied", getCurrentUsername(), commentId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error liking comment #{} for {}: {}", commentId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/unlike-comment", method = RequestMethod.POST)
    public ModelAndView unlikeComment(@RequestParam(name = "commentId") final long commentId,
                                      @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.unlikeComment(commentId, getCurrentUser().getId());
            LOG.info("{} unliked comment #{}", getCurrentUsername(), commentId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to unlike nonexistent comment #{}, denied", getCurrentUsername(), commentId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error liking comment #{} for {}: {}", commentId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/edit-comment", method = RequestMethod.POST)
    public ModelAndView editComment(@RequestParam(name = "commentId") final long commentId,
                                    @RequestParam(name = "newComment") String newComment,
                                    @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            newComment = newComment.trim();
            threadService.editComment(commentId, getCurrentUser().getId(), newComment);
            LOG.info("{} edited comment #{} to \"{}\"", getCurrentUsername(), commentId, newComment);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (UnauthorizedException e) {
            LOG.warn("{} attempted to edit comment #{} which is not theirs, denied", getCurrentUsername(), commentId);
            mav = new ModelAndView("error400"); //TODO make 403 page
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to edit nonexistent comment #{}, denied", getCurrentUsername(), commentId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error editing comment #{} for {}: {}", commentId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/delete-comment", method = RequestMethod.POST)
    public ModelAndView deleteComment(@RequestParam(name = "commentId") final long commentId,
                                    @RequestParam(name = "returnUrl", required = false, defaultValue = "/threads") final String returnUrl) {
        ModelAndView mav = null;
        try {
            threadService.deleteComment(commentId, getCurrentUser().getId());
            LOG.info("{} deleted comment #{}", getCurrentUsername(), commentId);
            mav = new ModelAndView("redirect:" + returnUrl);
        } catch (UnauthorizedException e) {
            LOG.warn("{} attempted to delete comment #{} which is not theirs, denied", getCurrentUsername(), commentId);
            mav = new ModelAndView("error400"); //TODO make 403 page
        } catch (NoSuchEntityException e) {
            LOG.warn("{} attempted to delete nonexistent comment #{}, denied", getCurrentUsername(), commentId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error deleting comment #{} by {}: {}", commentId, getCurrentUsername(), e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }
}
