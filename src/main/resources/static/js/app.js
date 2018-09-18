$(document).ready(function() {

      function passTaskId(currentElement){
          console.log(currentElement);
        var currentTaskid =  currentElement.data('id');
        $("#update_status_modal #ut_task_id").val(currentTaskid);
      }
      
    //onload get available tasks and list 
    function getTasks(){
        var settings = {
            "async": true,
            "crossDomain": true,
            "url": "http://localhost:8080/tasks/",
            "method": "GET",
            "headers": {
              "Content-Type": "application/json",
              "Cache-Control": "no-cache"
            }
          }
          
          $.ajax(settings).done(function (response) {
            //iterate and append tasks to list
            response.forEach(task => {
                $("#task_list ul").append('<li class="list-group-item d-flex justify-content-between align-items-center">'+
                '<span>'+task.description+'</span>'+
                '<span class="badge badge-primary badge-pill">'+task.status+'</span>'+
                '<button class="badge badge-secondary" data-id='+task.taskID +' id="update_status_btn" data-toggle="modal" data-target="#update_status_modal">Update Status</button>'+
              '</li>');
            });
          });     
    }
      getTasks();
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
             window.location.replace("http://localhost:8080")
         });

      });

    function updateStatus() {
        var taskId = $("#ut_task_id").val();
        var newStatus = $("#ut_status").val();
        var url = "http://localhost:8080/updateStatus/" + taskId + "/" + newStatus + "/";
        var settings = {
            "async": true,
            "crossDomain": true,
            "url": url,
            "method": "PUT",
            "headers": {
                "Content-Type": "application/json",
                "Cache-Control": "no-cache"
            }
        }

        $.ajax(settings).done(function (response) {
            console.log(response);
            window.location.replace("http://localhost:8080")
        });
    }

    //logic to pass values to modal

    $('body').on('click', '#update_status_btn', function (e) {
        // convert target to jquery object
        var $target = $(e.target);
        // modal targeted by the button
        var modalSelector = $target.data('target');
        var dataValue = $target.data("id");
        $("#ut_task_id").val(dataValue || '');
    });

    $("#update_status_submit_btn").click(function(){
        updateStatus();
    });
		
});
  