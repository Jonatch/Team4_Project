function addCourse(parameter) {
    $.ajax({
        url: '/add-course',
        method: 'POST',
        data: { parameter: parameter },
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
}

function closeRemovePopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("remove-popup-container");
    popup.classList.toggle("active");
}


