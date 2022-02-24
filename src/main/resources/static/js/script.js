//displays sub menu items of master when it is clicked
//and hides the sub-menu items when clicked second time
$('.master-btn').click(function(){
    $('.master-show').toggleClass("show-sub-menu");
    $('nav ul .first').toggleClass("rotate");

    if($('.master-show').hasClass("show-sub-menu")){
        $('.master-btn').addClass("active");
        $('.transaction-btn').removeClass("active");
    }
    else{
        $('.master-btn').removeClass("active");
    }
});

//displays sub menu items of transaction when it is clicked
//and hides the sub-menu items when clicked second time
$('.transaction-btn').click(function(){
    $('.transaction-show').toggleClass("show-sub-menu");
    $('nav ul .second').toggleClass("rotate");

    if($('.transaction-show').hasClass("show-sub-menu")){
        $('.transaction-btn').addClass("active");
        $('.master-btn').removeClass("active");
    }
    else{
        $('.transaction-btn').removeClass("active");
    }
});