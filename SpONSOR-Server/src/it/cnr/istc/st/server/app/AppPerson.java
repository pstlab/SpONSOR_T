/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.app;

import it.cnr.istc.st.server.entity.Person;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class AppPerson {

    private long id;
    private String code;
    private String name;
    private String surname;
    private String email;
    private String phone;

    public AppPerson() {

    }

    public AppPerson(long id, String code, String email, String name, String surname, String phone) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }
}
