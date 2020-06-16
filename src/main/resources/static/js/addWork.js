function validate() {
    var name = $('#work_name').val();
    var price = $('#price').val();
    var description = $("#description").val();

    var name_error = $("#name_error");
    var price_error = $("#price_error");
    var description_error = $("#description_error");

    var arr_fields = [[name, name_error], [price, price_error], [description, description_error]];

    var passForm = true;

    for (var i = 0; i < arr_fields.length; i++) {
        var field_handle = arr_fields[i][0];
        var field_error = arr_fields[i][1];

        if (field_handle.length == 0) {
            field_error.text("Данное поле должно быть обязательно заполнено");
            passForm = false;
        } else if (field_handle.replace(' ', '').length == 0) {
            field_error.text("Данное поле не правильно заполнено");
            passForm = false;
        } else {
            field_error.text("");
        }
    }

    return passForm;

}