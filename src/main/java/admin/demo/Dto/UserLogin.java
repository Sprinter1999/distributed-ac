package admin.demo.Dto;

public class UserLogin {
    public long userId;
    public String password;
    public Double inittemp;

    public Double getInittemp() {
        return inittemp;
    }

    public void setInittemp(Double inittemp) {
        this.inittemp = inittemp;
    }

    public long getUserId() {
        return userId;
    }


    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

