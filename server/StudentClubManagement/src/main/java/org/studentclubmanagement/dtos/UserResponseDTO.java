package org.studentclubmanagement.dtos;

import org.studentclubmanagement.models.Role;
import org.studentclubmanagement.models.UserClub;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public class UserResponseDTO {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private String phone;

    private String address;

    private Date birthday;

    private int joinedClubs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<UserClub> userClubs;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getJoinedClubs() {
        return joinedClubs;
    }

    public void setJoinedClubs(int joinedClubs) {
        this.joinedClubs = joinedClubs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<UserClub> getUserClubs() {
        return userClubs;
    }

    public void setUserClubs(Set<UserClub> userClubs) {
        this.userClubs = userClubs;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
