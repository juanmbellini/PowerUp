package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.GenreDao;
import ar.edu.itba.paw.webapp.interfaces.GenreService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreDao genreDao;

    @Override
    public Set<Genre> all() {
        return genreDao.all();
    }

    @Override
    public Genre findById(long id) {
        return genreDao.findById(id);
    }

    @Override
    public Genre findByName(String name) {
        return genreDao.findByName(name);
    }

    @Override
    public Set<Game> gamesWithGenre(Genre p) {
        return genreDao.gamesWithGenre(p);
    }
}
