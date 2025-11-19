package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

// TODO： connect to database

@Entity
@Table(name="users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;  //login
  private String password;  //login

  private String name;
  private int age;
  private String gender;    //MALE, FEMALE, PREFER NOT TO SAY
  private String address;
  private String phone;


  private String triagePriority;  // AI pre-analysis: RED > YELLOW > GREEN


  private LocalDateTime createdAt;

  public User(){}

  public User(String username, String password, String name, int age, String gender, String address, String phone, String triagePriority) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.address = address;
    this.phone = phone;
    this.triagePriority = triagePriority;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getTriagePriority() {
    return triagePriority;
  }

  public void setTriagePriority(String triagePriority) {
    this.triagePriority = triagePriority;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
