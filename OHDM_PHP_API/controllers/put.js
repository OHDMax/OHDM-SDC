var putMyData = function (json) {
    $.ajax({
        contentType: 'application/json',
        data:
                JSON.stringify(json),
        dataType: 'json',
        success: function () {
            console.log("Data succesfully put");
        },
        error: function () {
            console.log("Putting Data failed");
        },
        type: 'PUT',
        url: 'https://studi.f4.htw-berlin.de/~s0544210/OHDM_PHP_API/SDC/create'
    });
};