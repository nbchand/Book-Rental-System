//deletes review
$('.del').on('click',function(event){
    event.preventDefault();
    const id = this.id;
    fetch("/author/"+id, {
        method: 'DELETE',
    })
        .then(response => response.text())
        .then(data => {
            if(data=="failed"){
                location.href = "/";
            }
            location.href = "/author";
            console.log(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
})