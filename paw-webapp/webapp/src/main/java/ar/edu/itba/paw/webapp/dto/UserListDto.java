package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserListDto {
    private List<UserDto> users = new ArrayList<>();

    private int total;

    public UserListDto() {}

    public UserListDto(Collection<User> users) {
        for(User u : users) {
            this.users.add(new UserDto(u));
        }
    }

    public int getTotal() {
        return users.size();
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
