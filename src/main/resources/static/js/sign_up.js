function validate() {

    var name = $("#name");
    var surname = $("#surname");
    var username = $("#username");
    var password = $("#password");
    var secret_question = $("#SecretQuestion");
    var answer_question = $("#AnswerQuestion");
    var aboutMe = $("#AboutMe");

    var send_status = true;


    var fields = [name, surname, username, password, secret_question, answer_question, aboutMe];

    for (var i = 0; i < fields.length; i++) {
        var field = fields[i];
        var errorMsg = field.parent().find(".text-danger");

        if (field.val().length == 0 || field.val().replaceAll(" ", "").length == 0) {
            errorMsg.text("Это поле необходимо заполнить");
            send_status = false;
        } else {
            errorMsg.text("");
        }
    }

    $.get("/tools/check_exists", {"username": username.val()}, function (data, status) {
        if(data.result){
            var errorMsg = username.parent().find(".text-danger");
            errorMsg.text("Пользователь с таким именем уже существует");

            send_status = false;
        }
    })

    return send_status;
}