# ReplenishmentManager

### [Architecture and Design documentation](https://github.com/suryatejaswi89/ReplenishmentManager/wiki/Architecture-and-Design)

### [API documentation](https://github.com/suryatejaswi89/ReplenishmentManager/wiki/API-Docs) 

### [UI documentation](https://github.com/suryatejaswi89/ReplenishmentManager/wiki/UI-Design-and-Experience)

### [Future Roadmap](https://github.com/suryatejaswi89/ReplenishmentManager/wiki/Future-Road-map)


### **Work environment set up :**

* Install mongodb and make sure it is running on the port 27017
* Clone this project.
```
git clone https://github.com/suryatejaswi89/ReplenishmentManager.git
```
* This is maven project, please make sure you have maven installed.
* Change directory to project's root and issue below command to build and run JUnit tests.
```
mvn clean install
```
* To run the project, issue below command. It will start embedded Tomcat Webserver and deploy ReplenishmentmanagerApplication.
```
mvn spring-boot:run 
```
* Wait for console to show "Started ReplenishmentmanagerApplication" to start using the API.

* Refer [API docs](https://github.com/suryatejaswi89/ReplenishmentManager/wiki/API-documentation-for-Replenishment-Manager) to test scenarios of interest.

* Every method is documented to allow Java docs generation. Docs can be generated with below command. Create "docs" directory before running the command.
```
command: javadoc -d docs/ -sourcepath src/main/java/ -subpackages com.replenishmentmanager
```

## Assumptions and Design choices:

* Any task can have any status from  the following
	 Created
	 Started
	 completed 
* The status of the task can only be changed from created → started→ completed.
* If "isRecurring" condition for any task is true, the next recurring task is created only when its status is updated as “COMPLETED”
* Pending tasks include both created and started tasks which have not been completed.
* When ever the task status is changed, the timestamp is captured and stored to track time spent in each status.
* A task can be created by individual and added to his personalised tasks by keeping the taskOnwerID and assigneeID as same.
* End date for the recurringTask has not been considered which means the task will be created repeatedly based on the frequency for ever.
