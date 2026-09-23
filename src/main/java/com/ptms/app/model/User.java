package com.ptms.app.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int roleid;
    private String  rolename;
    public User()
    {

    }
    public User(int id, String name, String email, String password, int roleid, String rolename)
    {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleid = roleid;
        this.rolename = rolename;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public String getEmail()
    {
        return email;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public String getPassword()
    {
        return password;
    }
    public void  setRoleid(int roleid)
    {
        this.roleid=roleid;
    }
    public int getRoleid()
    {
        return roleid;
    }
    public void setRolename(String rolename)
    {
        this.rolename=rolename;
    }
    public String getRolename()
    {
        return rolename;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleid=" + roleid +
                ", rolename='" + rolename + '\'' +
                '}';
    }


}
