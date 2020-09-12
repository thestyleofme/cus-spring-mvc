package com.github.codingdebugallday.pojo;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/12 16:41
 * @since 1.0.0
 */
public class QueryVo {

    private String mail;
    private String phone;

    private User user;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
