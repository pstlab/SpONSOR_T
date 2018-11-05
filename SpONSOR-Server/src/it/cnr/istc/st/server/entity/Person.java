/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.entity;

import com.google.gson.Gson;
import it.cnr.istc.st.server.app.AppPerson;
import it.cnr.istc.st.server.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import it.cnr.istc.st.server.model.FreeTimeToken;
import it.cnr.istc.st.server.xml.Account;
import it.cnr.istc.st.server.xml.Interval;
import java.util.GregorianCalendar;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@NamedQueries(
        {
            @NamedQuery(name = "Person.findPersonByCode", query = "SELECT c FROM Person c WHERE c.code = :x"), //@NamedQuery(name = "AppTurn.findAllByCodeTurn", query = "SELECT c FROM AppTurn c WHERE c.codeTurn = :x"),
        // @NamedQuery(name = "AppTurn.findAllByCodeTurnANDcodePersone", query = "SELECT c FROM AppTurn c WHERE c.codeTurn = :x AND c.codePerson = :y")
        }
)
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nickname;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String note;
    private boolean admin;
    @Column(unique = true)
    private String code;
    @Column(length = 1000)
    private String otherData;
    private String plan;
    private boolean sleeping = false;
    @OneToMany
    private List<Keyword> keywords = new ArrayList<>();
    private transient boolean fixed = false;
    private transient Account account;
    private transient List<FreeTimeToken> expandedFreeTimes = new ArrayList<>();
    private boolean oneTurnPerDay = true;
    private int maxTurnPerWeek = 1;
    @OneToMany
    private List<ActivityName> onlyTheseActivities = new ArrayList<>();

    public AppPerson toAppPerson() {
        AppPerson ap = new AppPerson(id, code, email, name, surname, phone);
        return ap;
    }

    public List<FreeTimeToken> getExpandedFreeTimes() {
        return expandedFreeTimes;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isOneTurnPerDay() {
        return oneTurnPerDay;
    }

    public void setOneTurnPerDay(boolean oneTurnPerDay) {
        this.oneTurnPerDay = oneTurnPerDay;
    }

    public int getMaxTurnPerWeek() {
        return maxTurnPerWeek;
    }

    public void setMaxTurnPerWeek(int maxTurnPerWeek) {
        this.maxTurnPerWeek = maxTurnPerWeek;
    }

    public List<ActivityName> getOnlyTheseActivities() {
        return onlyTheseActivities;
    }

    public void setOnlyTheseActivities(List<ActivityName> onlyTheseActivities) {
        this.onlyTheseActivities = onlyTheseActivities;
    }

    public void fix() {
        if (fixed) {
            return;
        }
        this.password = "PASS";
        Gson gson = new Gson();
        if (otherData != null) {
            account = gson.fromJson(otherData, Account.class);

            List<Interval> intervals = account.getIntervals();
            for (Interval interval : intervals) {
                System.out.println("PERSON (" + name + " " + surname + "), FIXING: interval { " + interval + " }");
                GregorianCalendar gcStart = new GregorianCalendar();
                Date startDate = new Date(Utils.getCurrentMonday().getTime());
//                startDate.setDate(interval.getDayOfWeek());
                startDate.setHours(interval.getStartHourOfDay());
                startDate.setMinutes(0);
                startDate.setSeconds(0);
                gcStart.setTime(startDate);
                gcStart.add(GregorianCalendar.DAY_OF_YEAR, interval.getDayOfWeek());

                GregorianCalendar gcEnd = new GregorianCalendar();
                Date endDate = new Date(Utils.getCurrentMonday().getTime());
//                endDate.setDate(interval.getDayOfWeek());
                endDate.setHours(interval.getEndHourOfDay());
                endDate.setMinutes(0);
                endDate.setSeconds(0);
                gcEnd.setTime(endDate);
                gcEnd.add(GregorianCalendar.DAY_OF_YEAR, interval.getDayOfWeek());

                for (int i = 0; i < 12; i++) {

                    this.expandedFreeTimes.add(new FreeTimeToken(gcStart.getTime().getTime(), gcEnd.getTime().getTime()));
                    gcStart.add(GregorianCalendar.WEEK_OF_YEAR, 1);
                    gcEnd.add(GregorianCalendar.WEEK_OF_YEAR, 1);
                }
                //this.addFreeSlot(-1, interval.getDayOfWeek(), interval.getStartHourOfDay(), interval.getEndHourOfDay());
            }

        }
        fixed = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @PrePersist
    public void generateCode() {
        this.code = Utils.getAlpha(new Date().getTime());
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

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    public AppPerson toApp() {
        AppPerson ap = new AppPerson(id, code, email, name, surname, phone);
        return ap;
    }

}
