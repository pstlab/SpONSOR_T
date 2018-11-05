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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
abstract class ASolver implements ISolver {

    protected final List<User> users = new ArrayList<>();
    protected final List<Activity> activities = new ArrayList<>();
    protected final List<AtMostN> at_most_ns = new ArrayList<>();
    protected final List<AtLeastOneAmong> at_least_one_among_users = new ArrayList<>();
    protected final List<OnlyIn> only_in = new ArrayList<>();
    protected final List<NotIn> exclude_froms = new ArrayList<>();

    @Override
    public void read(Path path) throws IOException {
        JsonElement element = new JsonParser().parse(Files.newBufferedReader(path));

        // the users..
        JsonArray us_array = element.getAsJsonObject().get("users").getAsJsonArray();
        for (int i = 0; i < us_array.size(); i++) {
            JsonObject u_object = us_array.get(i).getAsJsonObject();
            String name = u_object.get("name").getAsString();
            JsonArray u_profile = u_object.get("profile").getAsJsonArray();
            int[] profile = new int[u_profile.size()];
            for (int j = 0; j < u_profile.size(); j++) {
                profile[j] = u_profile.get(j).getAsInt();
            }
            JsonArray u_availability = u_object.get("availability").getAsJsonArray();
            Interval[] availability = new Interval[u_availability.size()];
            for (int j = 0; j < u_availability.size(); j++) {
                JsonArray intv_array = u_availability.get(j).getAsJsonArray();
                availability[j] = new Interval(intv_array.get(0).getAsInt(), intv_array.get(1).getAsInt());
            }
            newUser(name, profile, availability);
        }

        // the activities..
        JsonArray as_array = element.getAsJsonObject().get("activities").getAsJsonArray();
        for (int i = 0; i < as_array.size(); i++) {
            JsonObject a_object = as_array.get(i).getAsJsonObject();
            String name = a_object.get("name").getAsString();
            JsonArray a_skills = a_object.get("skills").getAsJsonArray();
            boolean[] skills = new boolean[a_skills.size()];
            for (int j = 0; j < a_skills.size(); j++) {
                skills[j] = a_skills.get(j).getAsBoolean();
            }
            JsonArray a_span = a_object.get("span").getAsJsonArray();
            Interval span = new Interval(a_span.get(0).getAsInt(), a_span.get(1).getAsInt());
            newActivity(name, skills, span);
        }
    }

    @Override
    public void write(Path path) throws IOException {
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(path))) {
            writer.setIndent("  ");
            writer.beginObject();

            writer.name("users");
            writer.beginArray();
            for (User user : users) {
                writer.beginObject();
                writer.name("name").value(user.name);

                writer.name("profile");
                writer.beginArray();
                for (int par : user.pars) {
                    writer.value(par);
                }
                writer.endArray();

                writer.name("availability");
                writer.beginArray();
                for (Interval a : user.ta) {
                    writer.beginArray();
                    writer.value(a.lb);
                    writer.value(a.ub);
                    writer.endArray();
                }
                writer.endArray();

                writer.endObject();
            }
            writer.endArray();

            writer.name("activities");
            writer.beginArray();
            for (Activity activity : activities) {
                writer.beginObject();
                writer.name("name").value(activity.name);

                writer.name("skills");
                writer.beginArray();
                for (boolean par : activity.pars) {
                    writer.value(par);
                }
                writer.endArray();

                writer.name("span");
                writer.beginArray();
                writer.value(activity.ti.lb);
                writer.value(activity.ti.ub);
                writer.endArray();

                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
        }
    }

    @Override
    public Statistics getStatistics() {
        double u_time = 0;
        for (User u : users) {
            for (Interval ta : u.ta) {
                u_time += ta.ub - ta.lb;
            }
        }
        double a_time = 0;
        for (Activity a : activities) {
            a_time += a.ti.ub - a.ti.lb;
        }

        return new Statistics(users.size(), activities.size(), users.size() / (double) activities.size(), u_time / a_time);
    }

    @Override
    public User newUser(String name, int[] pars, Interval[] ta) {
        User user = new User(users.size(), name, pars, ta);
        users.add(user);
        return user;
    }

    @Override
    public Activity newActivity(String name, boolean[] pars, Interval ti) {
        Activity activity = new Activity(activities.size(), name, pars, ti);
        activities.add(activity);
        return activity;
    }

    @Override
    public AtMostN at_most_n(Activity[] as, int n) {
        assert as.length > 0 : "The number of activities should be > 0";
        assert Stream.of(as).allMatch(a -> activities.contains(a)) : "The activity instances must have been previously created by this solver instance";
        assert n >= 0 : "'n' should be >= 0";
        AtMostN amn = new AtMostN(users.toArray(new User[users.size()]), as, n);
        at_most_ns.add(amn);
        return amn;
    }

    @Override
    public AtMostN at_most_n(User u, Activity[] as, int n) {
        assert users.contains(u) : "The given user was not created by this solver instance";
        assert Stream.of(as).allMatch(a -> activities.contains(a)) : "The activity instances must have been previously created by this solver instance";
        assert n >= 0 : "'n' should be >= 0";
        AtMostN amn = new AtMostN(new User[]{u}, as, n);
        at_most_ns.add(amn);
        return amn;
    }

    @Override
    public void remove(AtMostN amn) {
        boolean removed = at_most_ns.remove(amn);
        assert removed : "The constraint was not previously created by this solver instance";
    }

    @Override
    public AtLeastOneAmong at_least_one_of(Activity[] as, User[] us) {
        assert us.length > 0 : "The number of users should be > 0";
        assert Stream.of(us).allMatch(u -> users.contains(u)) : "The user instances must have been previously created by this solver instance";
        assert as.length > 0 : "The number of activities should be > 0";
        assert Stream.of(as).allMatch(a -> activities.contains(a)) : "The activity instances must have been previously created by this solver instance";
        AtLeastOneAmong aloa = new AtLeastOneAmong(as, us);
        at_least_one_among_users.add(aloa);
        return aloa;
    }

    @Override
    public void remove(AtLeastOneAmong aloa) {
        boolean removed = at_least_one_among_users.remove(aloa);
        assert removed : "The constraint was not previously created by this solver instance";
    }

    @Override
    public OnlyIn only_in(User[] us, Activity[] as) {
        assert us.length > 0 : "The number of users should be > 0";
        assert Stream.of(us).allMatch(u -> users.contains(u)) : "The user instances must have been previously created by this solver instance";
        assert as.length > 0 : "The number of activities should be > 0";
        assert Stream.of(as).allMatch(a -> activities.contains(a)) : "The activity instances must have been previously created by this solver instance";
        OnlyIn oi = new OnlyIn(us, as);
        only_in.add(oi);
        return oi;
    }

    @Override
    public void remove(OnlyIn oa) {
        boolean removed = only_in.remove(oa);
        assert removed : "The constraint was not previously created by this solver instance";
    }

    @Override
    public NotIn not_in(User[] us, Activity[] as) {
        assert us.length > 0 : "The number of users should be > 0";
        assert Stream.of(us).allMatch(u -> users.contains(u)) : "The user instances must have been previously created by this solver instance";
        assert as.length > 0 : "The number of activities should be > 0";
        assert Stream.of(as).allMatch(a -> activities.contains(a)) : "The activity instances must have been previously created by this solver instance";
        NotIn ef = new NotIn(us, as);
        exclude_froms.add(ef);
        return ef;
    }

    @Override
    public void remove(NotIn ef) {
        boolean removed = exclude_froms.remove(ef);
        assert removed : "The constraint was not previously created by this solver instance";
    }

    /**
     * Checks whether the activity {@code a} is temporally compatible with the
     * user {@code u}
     *
     * @param a the activity.
     * @param u the user.
     * @return a boolean representing whether the given activity is temporally
     * compatible with the given user.
     */
    protected static boolean is_compatible(Activity a, User u) {
        for (Interval i : u.ta) {
            if (i.contains(a.ti)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates the assignment of the activity {@code a} to the user {@code u}.
     *
     * @param a the activity.
     * @param u the user.
     * @return an integer representing the evaluation of the assignment of the
     * given activity to the given user.
     */
    protected static int evaluate(Activity a, User u) {
        assert a.pars.length == u.pars.length;
        int val = 0;
        for (int i = 0; i < u.pars.length; i++) {
            if (a.pars[i]) {
                val += u.pars[i];
            }
        }
        return val;
    }
}
