package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "emprenderedbd", name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "name", nullable = false, length = 50)
    @NotBlank
    private String name;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank
    private String lastName;

    @Column(name = "last_name2", length = 50)
    private String lastName2;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    @NotBlank @Email
    private String email;

    // No devolver password en responses
    @Column(name = "password", nullable = false, length = 200)
    @NotBlank
    /@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)/
    private String password;

    @Column(name = "phone_number", nullable = false, length = 20)
    @NotBlank
    private String phoneNumber;

    @Column(name = "documento", nullable = false, length = 20, unique = true)
    @NotBlank
    private String documento;

    @Column(name = "sexo", length = 1)
    private String sexo; // 'M', 'F', 'O'

    // Getters y setters
    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getLastName2() { return lastName2; }
    public void setLastName2(String lastName2) { this.lastName2 = lastName2; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
}