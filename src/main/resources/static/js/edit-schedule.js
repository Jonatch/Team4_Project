async function fetchSuggestions(query) {
    const response = await fetch(`/suggestions?query=${encodeURIComponent(query)}`);
    if (response.ok) {
        return await response.json();
    } else {
        console.error('Failed to fetch suggestions:', response.status, response.statusText);
        return [];
    }
}

async function updateSuggestions() {
    const searchInput = document.getElementById('search-input');

    if (!searchInput) {
        console.error('search-input element not found');
        return;
    }

    const query = searchInput.value;
    const suggestions = await fetchSuggestions(query);
    const dropdownMenu = document.querySelector('.dropdown-menu');
    dropdownMenu.innerHTML = '';

    suggestions.forEach(suggestion => {
        const item = document.createElement('a');
        item.classList.add('dropdown-item');
        item.href = '#';
        item.textContent = suggestion;
        item.addEventListener('click', () => {
            searchInput.value = suggestion;
        });
        dropdownMenu.appendChild(item);
    });
}
function init() {
    const searchInput = document.getElementById('search-input');

    // Call updateSuggestions() when the input field receives input
    if (searchInput) {
        searchInput.addEventListener('input', updateSuggestions);
    } else {
        console.error('search-input element not found');
    }
}

// Call the init function when the DOM is fully loaded
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}

const dropdownItems = document.querySelectorAll('.dropdown-item');

window.addEventListener("DOMContentLoaded", (event) => {
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('input', updateSuggestions);
    }
});

function addCourse(parameter) {
    if(parameter === 'remove'){
         $("#scheduleTable").load(window.location.href + " #scheduleTable>*", "");
         $("#schedInfo").load(window.location.href + " #schedInfo>*", "");
    }
    else{
        $.ajax({
            url: '/add-course',
            method: 'POST',
            data: { parameter: parameter },
            success: function(response) {
                // Handle the response from the controller
                console.log(response);
                 if (response === 'false') {
                        openConflictPopup();
                 } else {
                        $("#scheduleTable").load(window.location.href + " #scheduleTable>*", "");
                        $("#schedInfo").load(window.location.href + " #schedInfo>*", "");
                 }
            },
            error: function(xhr, status, error) {
                // Handle any errors that occurred during the AJAX request
                console.error(error);
            }
        });
    }
}

function openFilterPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("popup-container");
    popup.classList.toggle("active");
}

function closeFilterPopup(){
    var form = document.getElementById("filterForm");
    var formData = new FormData(form);
    var serializedData = new URLSearchParams(formData).toString();
    $.ajax({
        url: '/filter',
        method: 'post',
        data: serializedData,
        success: function(response) {
          // Handle the response from the controller
          console.log(response);
          $("#searchResults").html(response); // Update the content of the search results
        },
        error: function(xhr, status, error) {
          // Handle any errors that occurred during the AJAX request
          console.error(error);
        }
      });

    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("popup-container");
    popup.classList.toggle("active");
}

function clearFilterPopup(){
    var form = document.getElementById("filterForm");
    var formData = new FormData(form);
    var serializedData = new URLSearchParams(formData).toString();
    $.ajax({
        url: '/filter',
        method: 'post',
        data: serializedData,
        success: function(response) {
          // Handle the response from the controller
          console.log(response);
          $("#searchResults").html(response); // Update the content of the search results
        },
        error: function(xhr, status, error) {
          // Handle any errors that occurred during the AJAX request
          console.error(error);
        }
      });
}

function search() {
  // Get the search query from the input field
  const searchQuery = $("#search-input").val();

  $.ajax({
    url: '/search-box',
    method: 'post',
    data: {
      query: searchQuery,
    },
    success: function(response) {
      // Handle the response from the controller
      console.log(response);
      $("#searchResults").html(response); // Update the content of the search results
    },
    error: function(xhr, status, error) {
      // Handle any errors that occurred during the AJAX request
      console.error(error);
    }
  });
}

function openConflictPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("conflict-popup-container");
    popup.classList.toggle("active");

    $.ajax({
            url: '/handle-conflict',
            method: 'POST',
            data: { parameter: parameter },
            success: function(response) {
                // Handle the response from the controller
                console.log(response);
                 if (response === 'false') {
                        openConflictPopup();
                 } else {
                        $("#scheduleTable").load(window.location.href + " #scheduleTable>*", "");
                 }
            },
            error: function(xhr, status, error) {
                // Handle any errors that occurred during the AJAX request
                console.error(error);
            }
        });


}

function closeConflictPopup(){
    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("conflict-popup-container");
    popup.classList.toggle("active");
}

function openRemovePopup() {
  $.ajax({
    url: '/remove-popup',
    method: 'post',
    success: function(response) {
      // Handle the response from the controller
      console.log(response);
      $("#remove-popup-container").html(response); // Update the content of the popup
    },
    error: function(xhr, status, error) {
      // Handle any errors that occurred during the AJAX request
      console.error(error);
    }
  });

  var blur = document.getElementById("blur");
  blur.classList.toggle("active");

  var popup = document.getElementById("remove-popup-container");
  popup.classList.toggle("active");
}

function getSelectedCourses() {
        var selectedCourses = document.getElementsByName("selectedCourses");
        var selectedValues = [];

        for (var i = 0; i < selectedCourses.length; i++) {
            if (selectedCourses[i].checked) {
                selectedValues.push(selectedCourses[i].value);
            }
        }
        closeRemovePopup(selectedValues);
}




function closeRemovePopup(selectedCourses){


    if(selectedCourses!==""){
        $.ajax({
              url: '/remove-course',
              method: 'POST',
              contentType: 'application/json',
              data: JSON.stringify({ selectedCourses: selectedCourses }),
              success: function(response) {
                // Handle success response
                console.log(response);
                $("#scheduleTable").load(window.location.href + " #scheduleTable>*", "");
                $("#schedInfo").load(window.location.href + " #schedInfo>*", "");
              },
              error: function(xhr, status, error) {
                // Handle error response
                console.error(error);
              }
        });
     }



    var blur = document.getElementById("blur");
    blur.classList.toggle("active");

    var popup = document.getElementById("remove-popup-container");
    popup.classList.toggle("active");
}

function openInfoPopup(parameter){

    $.ajax({
            url: '/more-info',
            method: 'POST',
            data: { parameter: parameter },
            success: function(response) {
                // Handle the response from the controller
                console.log(response);
                $("#info-popup-container").html(response);
            },
            error: function(xhr, status, error) {
                // Handle any errors that occurred during the AJAX request
                console.error(error);
            }
        });

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


