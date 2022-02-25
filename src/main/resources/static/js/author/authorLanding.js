//submits delete author form
$('.del').on('click',function(event){
    event.preventDefault();
    $('.delete-form').submit();
})
//closes error message
$('.cross').on('click',function(event){
    event.preventDefault();
    $('.error-message-container').hide();
})