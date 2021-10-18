var curr_decr = [];
$(document).ready(function() {
    $.getJSON("/getcurrencies", function(data) {
        var curr_index = 0;
        $.each(data, function(key, val) {
            $('#curr_select').append($('<option>', {
                value: curr_index,
                text : key
            }));
            $('#base_curr_select').append($('<option>', {
                value: curr_index,
                text : key
            }));
            curr_decr.push(val);
            curr_index++;
        });

        // TODO: Delete this block if you are rich and had paid the licence and uncomment update_todays_gif() after :)
        $.get("/getbasecurrency", function(base_curr) {
            $("#base_curr_select option").filter(function() {
              return $(this).text() == base_curr;
            }).prop('selected', true);

            update_todays_gif();
        });

        //update_todays_gif();
    });
});

$("#curr_select").change(function() {
    update_todays_gif();
});
$("#base_curr_select").change(function() {
    update_todays_gif();
});

function update_todays_gif() {
    $("#img_div").hide();
    $("#gif_img_loader").show();

    var selected_curr_index = $("#curr_select").val();
    var selected_curr = $("#curr_select option:selected").text();
    var selected_base_curr = $("#base_curr_select option:selected").text();
    $("#curr_descr").text(curr_decr[selected_curr_index]);

    $.getJSON("/gettodaysgif", { "ccode": selected_curr, "bccode": selected_base_curr }, function(json_data) {
        var gif_img = $("#gif_img");
        gif_img.on("load", function () {
            $("#currval_prevday").text(json_data.prevDayAmount);
            $("#currval_curday").text(json_data.curDayAmount);
            $("#gif_img_loader").hide();
            $("#img_div").show();
        });
        gif_img.attr("src", json_data.gifUrl);
    }).fail(function () {
        go_error();
    });
}

function go_error() {
    $("#gif_img_loader").hide();
    $("#img_div").hide();
    $("#error_div").show();
}