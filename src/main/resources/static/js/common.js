//closes error message
$('.cross').on('click',function(event){
    event.preventDefault();
    $(this).parent().hide();
})