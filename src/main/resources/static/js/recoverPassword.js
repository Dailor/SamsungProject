var validate = validateLogin;

function validateLogin() {
    var login = $("#username").val();
    var loginError = $("#username_error");

    if(login.length == 0){
        loginError.text("Это поле не может быть пустым");
        return false;
    }
    else{
        loginError.text("");
    }

    $.get("/forgot_password/get_question", {"username": login}, function (data, status) {
        if(data.hasOwnProperty("error")){
            loginError.text(data.error);
        }
        else{
            loginError.parent().remove();
            $(".form-question").removeAttr("hidden");

            $(".question").text("Секретный вопрос: " + data.question);
            $("#username_ans").val(login);
            validate = validateSecretQuestion;
        }
    })


    return false;
}

function validateSecretQuestion() {
    var login = $("#username_ans").val();
    var answer = $("#answer").val();

    $.get("/forgot_password/check_question", {"username": login, "answer": answer}, function (data, status) {
        if(data.hasOwnProperty("error")){
            $("#answer_error").text(data.error);
        }
        else{


            $(".form-question").remove();
            $(".form-new-password").removeAttr("hidden");


            $("#password").val("")
            $("#password-repeat").val("")

            $("#old-hashed-password").val(data.old_password_hash);
            $("#username_recover").val(data.username)
            validate = validateRecoverPassword;
        }
    })

    return false
}

function validateRecoverPassword() {
    var password =  $("#password").val();
    var repeat_password =  $("#password-repeat").val();
    var password_error = $("#password_error");

    if(password != repeat_password){
        password_error.text("Пароли не совпадают");
        return false;
    }

    else if(password.replace(" ", "") == 0){
        password_error.text("Поле для пароля не должно быть пустым")
        return false;
    }

}

