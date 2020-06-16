function validate() {

    var name = document.forms['form']['name'].value;
    var surname = document.forms['form']['surname'].value;
    var username = document.forms['form']['username'].value;
    var password = document.forms['form']['password'].value;
    var secret_question = document.forms['form']['secret_question'].value;
    var answer_question = document.forms['form']['answer_question'].value;
    var aboutMe = document.forms["form"]["aboutMe"].value;

    var fields = [name, surname, username, password, secret_question, answer_question, aboutMe]

    if (name.length == 0) {
        document.getElementById('name_error').innerHTML = '*данное поле обязательно для заполнения';
    } else if(name.contains(' ')){
        document.getElementById("name_error").innerHTML = "*данное поле не должно содержать пустые символы"
    }
    else {
        document.getElementById('name_error').innerHTML = '';
    }

    if (surname.length == 0) {
        document.getElementById('surname_error').innerHTML = '*данное поле обязательно для заполнения';
    } else if(surname.contains(' ')){
        document.getElementById("surname_error").innerHTML = "*данное поле не должно содержать пустые символы"
    }
    else {
        document.getElementById('surname_error').innerHTML = '';
    }

    if (username.length == 0) {
        document.getElementById('login_error').innerHTML = '*данное поле обязательно для заполнения';
    } else if(username.contains(' ')){
        document.getElementById("login_error").innerHTML = "*данное поле не должно содержать пустые символы"
    }
    else {
        document.getElementById('login_error').innerHTML = '';
    }

    if (password.length == 0) {
        document.getElementById('password_error').innerHTML = '*данное поле обязательно для заполнения';
    } else if(password.contains(' ')){
        document.getElementById("password_error").innerHTML = "*данное поле не должно содержать пустые символы"
    }
    else {
        document.getElementById('password_error').innerHTML = '';
    }

    if (secret_question.length == 0) {
        document.getElementById('question_error').innerHTML = '*данное поле обязательно для заполнения';
    } else if(secret_question.contains(' ')){
        document.getElementById("question_error").innerHTML = "*данное поле не должно содержать пустые символы"
    }
    else {
        document.getElementById('question_error').innerHTML = '';
    }

    if (answer_question.length == 0) {
        document.getElementById('answer_error').innerHTML = '*данное поле обязательно для заполнения';
    } else if(answer_question.contains(' ')){
        document.getElementById("answer_error").innerHTML = "*данное поле не должно содержать пустые символы"
    }
    else {
        document.getElementById('answer_error').innerHTML = '';
    }

    if (aboutMe.length == 0) {
        document.getElementById('aboutMe').innerHTML = '*данное поле обязательно для заполнения';
    } else if (aboutMe.length > 500) {
        document.getElementById('aboutMe').innerHTML = '*максимум 500 символов';
    } else {
        document.getElementById('aboutMe').innerHTML = '';
    }

    for (var i = 0; i < fields.length; i++) {
        if (fields[i].length == 0 || (fields[i].contains(' ') && i != fields.length - 1)) {
            return false;
        }
    }
}