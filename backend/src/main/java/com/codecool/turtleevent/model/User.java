package com.codecool.turtleevent.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {

    public static class UserView extends AllUsersView {
    }

    public static class AllUsersView{
    }

    @JsonView(AllUsersView.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(AllUsersView.class)
    @Column(name="user_name")
    private String userName;
    @JsonView(AllUsersView.class)
    @Column(name="first_name")
    private String firstName;
    @JsonView(AllUsersView.class)
    @Column(name="last_name")
    private String lastName;
    @JsonView(AllUsersView.class)
    private String email;
    @JsonView(AllUsersView.class)
    private String password;

    @JsonView(UserView.class)
    @ManyToMany
    @JoinTable(name="friends",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="friend_id")
    )
    private List<User> friends;

    @JsonView(UserView.class)
    @ManyToMany
    @JoinTable(name="friends",
            joinColumns=@JoinColumn(name="friend_id"),
            inverseJoinColumns=@JoinColumn(name="user_id")
    )
    private List<User> friendOf;

    @JsonView(UserView.class)
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    @JsonManagedReference(value="user-eventroles")
    private Set<UserEventRole> eventRoles;

    @JsonView(UserView.class)
    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
    @JsonManagedReference(value="user-messages")
    private Set<Message> messages;

    @JsonView(AllUsersView.class)
    @Column(nullable = false)
    private LocalDateTime registered;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(List<User> friendOf) {
        this.friendOf = friendOf;
    }

    public Set<UserEventRole> getEventRoles() {
        return eventRoles;
    }

    public void setEventRoles(Set<UserEventRole> eventRoles) {
        this.eventRoles = eventRoles;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }
}