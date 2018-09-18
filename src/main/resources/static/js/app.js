$(document).ready(function() {
      
      $("#create_task_submit_btn").click(function(){
            var task = {};
            task.description = $("#ct_description").val();
            task.taskOwnerID = $("#ct_taskOwner_ID").val();
            task.assigneeID = $("#ct_assignee_ID").val();
            task.dateCreated = $("#ct_date_created").val();
            task.priority = $("#ct_priority").val();
            task.isRecurring = $("#ct_recurring").prop('checked');
            task.estimate = $("#ct_estimate").val();
            if(task.isRecurring){
                task.frequency = $("#ct_frequency").val();
            }
            console.log(task);

            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8080/createTask",
                "method": "POST",
                "headers": {
                  "Content-Type": "application/json",
                  "Cache-Control": "no-cache"
                },
                "processData": false,
                "data":JSON.stringify(task)
            }

       $.ajax(settings).done(function (response) {
             console.log(response);
         });

      });
});
  