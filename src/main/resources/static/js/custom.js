
function confirmArchiveDelete(archiveId) {
    Swal.fire({
        title: "Êtes-vous sûre de supprimer l'archive?",
        text: "Attention",
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: '<i class=\'far fa-window-close\'></i> Annuler',
        confirmButtonText: '<i class=\'far fa-check-circle\'></i> Oui, Supprimer!'
    }).then((result) => {
        if (result.value) {
            $("#archive" + archiveId).submit();
        }
    })
}


$(document).ready(() => {

    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd'
    });


});
