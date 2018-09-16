# ReplenishmentManager

Selecting a Database and designing the schema:
1. Considering scalability and also the nature of the data, noSQL will be the best option for managing the data.This project is being built with MongoDB as its noSQL database.

2. Collections have been defined and set of test data has been created.

3. Requirement Analysis is done and the following scenarios or use cases have been identified.

### Building blocks
 1. Create a new task.
 ```
 db.tasks.insert({description: 'Order the plastic cups', taskOwner: 'Teja', priority: 'high', estimate: '3 days', Assignee:   'tiru', status: 'to-do', })
 ```
 2. List all the tasks  from the table.
 ```
 db.tasks.find()
 ```
 3. List the tasks by status.
 ```
 db.tasks.find({"status":"to-do"})
 ```
 4. List the tasks by taskID.
 ```
 db.tasks.findOne(ObjectId("5b9b329e430a877cb0ea785a"))
 ```
 5. List the tasks by taskOwner.
 ```
 db.users.findOne({firstName:"Blair"})
 ```
6. List the tasks by assignee.
```
db.tasks.find({'Assignee':'tiru'}).pretty()
```
7. List the personalized tasks by an employee(where creator = assignee).	
```
db.tasks.find({$and:[{"taskOwner": "ObjectId(\"5b9bc06c430a877cb0ea785d\")"},{"Assignee":"ObjectId(\"5b9bc06c430a877cb0ea785d\")"}]}).pretty()
```
8. Update a task.
```
db.tasks.update({'status':'in-progress'},{$set:{'status':'completed'}})
```
9. Delete a task.
```
db.tasks.remove({'status':'completed'})
```

All these queries have been answered with mongoDB queries and tested if the database schema is working properly.

## Design and implementation
Scenario 1:
	Display sorted list of pending tasks
		o Sorted by first Status, second Rank
		o Rank should use an algorithm which weighs inputted priority against time estimated to perform the task
Assumptions: 
1. The tasks which are in both created and started status are considered to be pending tasks. (TODO)
Task Ranking algorithm:
1. List all the pending tasks from the task lists.
2. Rank has been calculated by weighing priority of the task against the estimated time.
3. Priorities  high = 3, medium =2, low = 1 and estimation is always in number of days taken to complete the task.
4. the way i understood the priority is nothing but the value that will be derived from completing a task.
5. so by dividing value/number of days taken to complete the task will give the value we get for one day
6. weightage formula is weightage = ( priority/estimate ) 
7. The task with highest weightage will be prioritized.

Scenario 2:
	User should have ability to track status for individual task
		o This status will be used to drive other features of app

Scenario 3:
Track time in each status after “Started” until “Finished”

Assumptions:
1. A task can be in any of the following status
    1. CREATED
    2. STARTED
    3. FINISHED
2. The status of a task must be changed in the following order
    1. CREATED —> STARTED—> FINISHED


Scenario 4:
	Recurring tasks should be duplicated based on recurrence frequency/schedule

Assumptions:
1. End date for the recurring task is not considered which means task will be repeated based on the frequency forever.
2. For simplicity of frequency calculation , monthly recurrence is provided as an option.

Approach:
1. every task has a indicator to indicate a recurring task
2. if recurring event is true, recurring details will be captured.
3. The recurring details include recurring frequency(monthly)
4. when the task status is changed to finished, a new task is created with a creation date calculated from the start date of this event and recurring frequency.

