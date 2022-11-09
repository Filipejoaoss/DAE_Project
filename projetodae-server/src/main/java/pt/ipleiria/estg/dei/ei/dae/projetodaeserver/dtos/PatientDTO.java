package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos;

public class PatientDTO {
    private String username;
    private String name;
    private String password;
    private String contact;
    private String address;
    private String email;
    private String medic;

    public PatientDTO() {
    }

    public PatientDTO(String username, String name, String password, String contact, String address, String email, String medic) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.email = email;
        this.medic = medic;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedic() {
        return medic;
    }

    public void setMedic(String medic) {
        this.medic = medic;
    }
}
