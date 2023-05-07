function addCourse(parameter) {
    $.ajax({
        url: '/add-course',
        method: 'POST',
        data: { parameter: parameter },
        success: function(response) {
            // Handle the response from the controller
            console.log(response);
             $("#scheduleTable").load(window.location.href + " #scheduleTable>*", "");
        },
        error: function(xhr, status, error) {
            // Handle any errors that occurred during the AJAX request
            console.error(error);
        }
    });
}

function openFilterPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("popup-container");
    popup.classList.toggle("active");
}

function closeFilterPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("popup-container");
    popup.classList.toggle("active");
}

function openRemovePopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("remove-popup-container");
    popup.classList.toggle("active");

    $.ajax({
            url: '/remove-courses',
            method: 'get',
            success: function(response) {
                // Handle the response from the controller
                console.log(response);
            },
            error: function(xhr, status, error) {
                // Handle any errors that occurred during the AJAX request
                console.error(error);
            }
        });
}

function closeRemovePopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("remove-popup-container");
    popup.classList.toggle("active");
}

function openInfoPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("info-popup-container");
    popup.classList.toggle("active");
}

function closeInfoPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("info-popup-container");
    popup.classList.toggle("active");
}

function openEventPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("event-popup-container");
    popup.classList.toggle("active");
}

function closeEventPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("event-popup-container");
    popup.classList.toggle("active");
}

$(document).ready(function() {

  // Submit the form using AJAX when the form is submitted
  $('#eventForm').submit(function(e) {
    e.preventDefault(); // Prevent default form submission
    var form = $(this);
    $.ajax({
      url: form.attr('action'),
      method: form.attr('method'),
      data: form.serialize(),
      success: function(response) {
        // Handle success response
        console.log(response);
      },
      error: function(xhr, status, error) {
        // Handle error response
        console.error(error);
      }
    });

  var eventForm = $('#eventForm');
      eventForm[0].reset();
  });
});


