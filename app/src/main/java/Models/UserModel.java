package Models;

/**
 * Created by Sh Junaid on 12/3/2016.
 */
public class UserModel {
    private int Id;
    private String Student_EmployeeId;
    private String UserName;
    private String Phone;

    public void setId(int id) {
        Id = id;
    }

    public void setStudent_EmployeeId(String std_EmpId) {
        Student_EmployeeId = std_EmpId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setPhoneStatus(String phoneStatus) {
        PhoneStatus = phoneStatus;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getId() {

        return Id;
    }

    public String getStudent_EmployeeId() {
        return Student_EmployeeId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPhone() {
        return Phone;
    }

    public String getPhoneStatus() {
        return PhoneStatus;
    }

    public String getPassword() {
        return Password;
    }

    private String PhoneStatus;
    private String Password;



    }
