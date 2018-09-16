# ReplenishmentManager

Selecting a Database and designing the schema:
1. Considering scalability and also the nature of the data, noSQL will be the best option for managing the data.This project is being built with MongoDB as its noSQL database.

2. Collections have been defined and set of test data has been created.

3. Requirement Analysis is done and the following scenarios or use cases have been identified.
  * Building blocks
	1. Create a new task.
		```
		db.tasks.insert({description: 'Order the plastic cups', taskOwner: 'Teja', priority: 'high', estimate: '3 days', Assignee: 'tiru', status: 'to-do', })
		```
		
⋅⋅* List all the tasks  from the table.
	```
	db.tasks.find()
	```
⋅⋅* List the tasks by status.
	```
	db.tasks.find({"status":"to-do"})
	```
⋅⋅* List the tasks by taskID.
	```
	db.tasks.findOne(ObjectId("5b9b329e430a877cb0ea785a"))
	```
⋅⋅* List the tasks by taskOwner.
	```
	db.users.findOne({firstName:"Blair"})
	```
⋅⋅* List the tasks by assignee.
	```
	db.tasks.find({'Assignee':'tiru'}).pretty()
	```
⋅⋅* List the personalized tasks by an employee(where creator = assignee).	
	```
	db.tasks.find({$and:[{"taskOwner": "ObjectId(\"5b9bc06c430a877cb0ea785d\")"},{"Assignee":"ObjectId(\"5b9bc06c430a877cb0ea785d\")"}]}).pretty()
	```
⋅⋅* Update a task.
	```
	db.tasks.update({'status':'in-progress'},{$set:{'status':'completed'}})
	```
⋅⋅* Delete a task.
	```
	db.tasks.remove({'status':'completed'})
	```

All these queries have been answered with mongoDB queries and tested if the database schema is working properly.


