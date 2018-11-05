/*
 * Copyright (C) 2017 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.sponsor.solver.sponsorsolver;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Statistics {

    // the number of users..
    public final int n_users;
    // the number of activities..
    public final int n_activities;
    // the ration between the number of users and the number of activities..
    public final double ua_ratio;
    // the ration between the total users' available time and the total time required by the activities..
    public final double ar_ratio;

    Statistics(int n_users, int n_activities, double ua_ratio, double ar_ratio) {
        this.n_users = n_users;
        this.n_activities = n_activities;
        this.ua_ratio = ua_ratio;
        this.ar_ratio = ar_ratio;
    }

    @Override
    public String toString() {
        return "Statistics{" + "nr. users=" + n_users + ", nr. activities=" + n_activities + ", VA=" + ua_ratio + ", AR=" + ar_ratio + '}';
    }
}
