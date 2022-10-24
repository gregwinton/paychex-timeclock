# paychex-timeclock

Projects fullfills the requirements as set forth in the Interview Candidate Project.pdf, and copied here:

## Requirements (from 
Allow user to be identified using a unique ID assigned to each employee.
1. Users that cannot be identified should not be able to use the application.
    1. Allow users to start a work shift.
1. Do not allow users to start multiple shifts simultaneously.
    1. Allow users to end a work shift.
1. Do not allow users to start a shift during an active shift.
    1. Allow users to start/end a break, but only during an active shift. o Do not allow employees to end a shift if a break is active.
1. Allow users to start/end a lunch, but only during an active shift. o Do not allow employees to end a shift if a lunch is active.
1. All shift data performed by users should be recorded and made available upon returning to the application.
1. (Optional) Allow new users to register themselves in the application.
1. (Optional) Allow for two types of users in the application; administrators and non-
administrators.
1. (Optional) Allow administrators to perform any function at any time regardless of the rules
stated previously.
    - [GW] Need greater clarification on this rule. What is expected behavior in each case?
1. (Optional) Allow administrators to view a summary report section that summarizes all the
employee’s shift activity.
    1. (Optional) Allow administrators to filter the report data.

## Analysis

Even without the optional requirements, this is a tall set of requirements to implement over a relatively short time - 3 days, in this case. So rather than trying to do too much too quickly, I designed and built a high quality, maintainable and extensible MVP focused only on the non-optional requirements.

As it is, the result still could use a bit of polish:

## Next Steps
- persist data between sessions
    - ponder "saving" active shifts
- clean up, comment, refactor
    - optimize use of MutableLiveData<> objects. 
        - not sure all are necessary.
- add an application icon
- change user selection from current full page list of users to a more traditional login window (albeit without password)
    - allow user to register
- add support for admin users 
    - limit access user list screen to admin users <- user's will only see their own shift activity
- add filtering to employee work shift list

## Known Issues
- Employee WorkShift List elapsed time for shift does not stop or account for break, lunch.
    - Simple Fix: Use "net time" instead of "total time". That may require change to Adapter <- change WorkShiftListItem
- Active work shift does not appear in employee work shift list. 
    - Tapping "add" button brings up active work shift, if there is one.
- Date display on WorkShift fragment - be smarter about date inclusion, don't need it for every date/time.

