$('#js-message-container').hide();
$("#code-list").change(function () {
    let code = $(this).val();
    if(code==''){
        $('#rent-date').val('');
        $('#return-date').val('');
        $('#member').val('');
        $('#status').val('');
        $('#book').val('');
        return;
    }
    $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/return/data",
        data: code,
        success: function (bookTransactionDto) {
            $('#rent-date').val(bookTransactionDto['fromDateString']);
            $('#return-date').val(bookTransactionDto['toDateString']);
            $('#member').val(bookTransactionDto['memberDto']['name']);
            $('#status').val(bookTransactionDto['rentType']);
            $('#book').val(bookTransactionDto['bookDto']['name']);
        },
        error: function(err) {
            $("#js-message-container").show();
            $("#js-error").html("Transaction detail of provided code not found");
        }
    });
});