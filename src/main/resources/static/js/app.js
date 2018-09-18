$(document).ready(function() {

      function passTaskId(currentElement){
          console.log(currentElement);
        var currentTaskid =  currentElement.data('id');
        $("#update_status_modal #ut_task_id").val(currentTaskid);
      }
      
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
                '<button id="task_details_btn" class="badge badge-info" data-toggle="modal" data-target="#task_details_modal">'+task.description+'</button>'+
                '<span class="badge badge-primary badge-pill">'+task.status+'</span>'+
                '<button class="badge badge-secondary" data-id='+task.taskID +' id="update_status_btn" data-toggle="modal" data-target="#update_status_modal">Update Status</button>'+
              '</li>');
            });
          });     
    }
     // getTasks();
      $("#create_task_submit_btn").click(function(){
            var task = {};
            task.description = $("#ct_description").val();
            task.taskOwnerID = $("#ct_taskOwner_ID").val();
            task.assigneeID = $("#ct_assignee_ID").val();
            task.dateCreated = $("#ct_date_created").val();
            task.priority = $("#ct_priority").val();
            task.recurringTask = $("#ct_recurring").prop('checked');
            task.estimate = $("#ct_estimate").val();
            if(task.recurringTask){
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
    
    
        //onload get all pending tasks and display in priority order as per the ranking algorithm
        function getPendingTasks(){
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8080/pendingtasks/",
                "method": "GET",
                "headers": {
                  "Cache-Control": "no-cache"
                }
              }
              
              $.ajax(settings).done(function (response) {
                //iterate and append tasks to list
                response.forEach(task => {
                    $("#task_list ul").append('<li class="list-group-item d-flex justify-content-between align-items-center">'+
                    '<a href="" id="task_details_btn" class="badge badge-light" data-toggle="modal" data-target="#task_details_modal" style="text-decoration: underline;">'+task.description+'</a>'+
                    '<span class="badge badge-primary badge-pill badge-info">'+task.status+'</span>'+
                    '<span class="badge badge-primary badge-pill badge-info">Weightage: '+task.weightage+'</span>'+
                    '<a href="" class="badge badge-light" data-id='+task.taskID +' id="update_status_btn" data-toggle="modal" data-target="#update_status_modal" style="text-decoration: underline;">Update Status</a>'+
                  '</li>');
                });
              });     
        }
        getPendingTasks();

        function getTaskDetails(taskid) {

            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8080/tasks/5b9e7f2e9e58662c28581e4f/",
                "method": "GET",
                "headers": {
                    "Cache-Control": "no-cache"
                }
            }

            $.ajax(settings).done(function (task) {
                console.log(task);
                 $("#details_task_ID").text(task.taskID);
                 $("#details_description").text(task.description);
                 $("#details_taskOwner_ID").text(task.taskOwnerID);
                 $("#details_assignee_ID").text(task.assigneeID);
                 $("#details_date_created").text(task.dateCreated);
                 $("#details_priority").text(task.priority);
                 $("#details_recurring").text(task.recurringTask);
                 $("#details_estimate").text(task.estimate);
                    if(task.recurringTask){
                        $("#details_frequency").text(task.frequency || '');
                    }
                 $("#details_status").text(task.status);
                 $("#details_weightage").text(task.weightage);
                 $("#details_dateCompleted").text(task.dateCompleted);
                 $("#details_dateStarted").text(task.dateStarted);
                 $("#details_timeinCreatedstatus").text(task.timeinCreatedstatus);
                 $("#details_timeInDateStartedstatus").text(task.timeInDateStartedstatus);
            });
        }


        $('body').on('click', '#task_details_btn', function (e) {
            // convert target to jquery object
            var $target = $(e.target);
            // modal targeted by the button
            var modalSelector = $target.data('target');
            var taskId = $target.data("id");
            getTaskDetails(taskId);
        });
});
  