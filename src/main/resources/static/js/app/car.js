var form = document.getElementById('note_form');
var input = document.getElementById('note_value_input');
var button = document.getElementById('note_submit');

input.value = '';

button.addEventListener('click', function () {
    if (input.value.length > 0)
        form.submit();
});
