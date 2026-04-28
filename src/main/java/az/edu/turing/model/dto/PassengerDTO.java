package az.edu.turing.model.dto;

import az.edu.turing.enums.GENDERS;

import java.util.Objects;

public class PassengerDTO {
    private Long passengerId;
    private String fullName;
    private GENDERS gender;
    private String nationality;
    private String passportNumber;
    private String phoneNumber;

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public GENDERS getGender() {
        return gender;
    }

    public void setGender(GENDERS gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PassengerDTO{" +
                "passengerId=" + passengerId +
                ", fullName='" + fullName + '\'' +
                ", gender=" + gender +
                ", nationality='" + nationality + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PassengerDTO that)) return false;
        return Objects.equals(passengerId, that.passengerId) && Objects.equals(fullName, that.fullName) && gender == that.gender && Objects.equals(nationality, that.nationality) && Objects.equals(passportNumber, that.passportNumber) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId, fullName, gender, nationality, passportNumber, phoneNumber);
    }
}
