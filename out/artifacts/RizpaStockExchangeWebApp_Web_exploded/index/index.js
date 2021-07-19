
$(function() { // onload...do
     $("#loginForm").submit(function() {
         var name = $("#username").val();
         sessionStorage.removeItem("username");
         sessionStorage.setItem("username",name);
     })

    $(':input[type="submit"]').prop('disabled', true);
    $("#username").keyup(function () {
        if($(this).val().length > 0 )
        {
            $(':input[type="submit"]').prop('disabled', false);
        }
        else
        {
            $(':input[type="submit"]').prop('disabled', true);
        }
       })
});

