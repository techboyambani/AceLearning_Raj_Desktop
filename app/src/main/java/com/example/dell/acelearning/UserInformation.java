package com.example.dell.acelearning;


public class UserInformation  {

    private String name;
    private String f_subjects;
    private String url;
    private String email;
    private String usertype;
    private String phn;
    private String password;
  //  private String password;

    public UserInformation(){

    }
    public UserInformation(String name,String email, String usertype, String phn, String password, String f_subjects, String url) {
        this.email = email;
        this.name = name;
        this.usertype = usertype;
        this.phn=phn;
        this.password=password;
        this.f_subjects=f_subjects;
        this.url=url;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setF_subjects(String f_subjects)
    {
        this.f_subjects=f_subjects;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype= usertype;
    }

    public void setPhn(String phn)
    {
        this.phn=phn;
    }
    public String getPhn()
    {
        return phn;
    }
    public String getF_subjects()
    {
        return f_subjects;
    }
    public String getUrl(){return url;}
    public void setUrl(String url){this.url=url;}

}
