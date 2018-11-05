# SpONSOR-Solver

This library is aimed at solving the problem of assigning a set of *a<sub>i</sub>* activities to a set of *u<sub>j</sub>* users.

Each user *u<sub>j</sub>* has a set of numeric parameters, representing the psychological profile of the user, and a set of intervals, representing the temporal availability of the user.

Similarly, each activity *a<sub>i</sub>* has a set of boolean parameters, representing the required skill for the activity, as well as an interval representing the temporal span of the activity.

While the temporal constraints are managed as *hard constraints*, skill parameters are managed as *soft constraints*. The role of the solver is, therefore, to find the *optimal* assignment.

Each activity *must* be performed by exactly one user. Each user *must* perform at least one activity. Notice that each user can perform more than one activity, as long as they are not temporally overlapping.
