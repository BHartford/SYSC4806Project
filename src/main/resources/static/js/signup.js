$(document).ready(function () {
    $('#signupForm').submit(function() {
      if ($("#username").val() != "" && $("#password").val() != "" && ($("#seller").prop('checked') || $("#buyer").prop('checked'))){
              var submit;
              $.ajax({
               type: "POST",
               url: "/public/checkSignup",
               contentType: 'application/json',
               data: JSON.stringify({
                   username: $("#username").val()
               }),
               success: function (data, status, xhr) {
                   submit = data.result;
               },
               async: false
               });
               if (submit){
                    alert("Successfully signed up");
                    return true;
               } else {
                    alert("Username already exists");
                    return false;
               }
          } else {
              alert("Missing username, password or type");
              return false;
          }
    });
});