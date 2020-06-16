function validate() {
    var description = $("#result").val();
    var declaration_error = $("#result_error")

    if(description.replace(' ','').length == 0){
        declaration_error.text("Не правильно заполенно поле");
        return false
    }
    else if(description.length == 0){
        declaration_error.text("Это поле не должно быть пустым");
    }

}