var notifyChecker;

function on_ready() {
    notifyChecker = setInterval(getCountUnreadEvents, 1000)
    getCountUnreadEvents();
}

function getCountUnreadEvents() {
    $.get("/notify/getCountUnread", {}, function (data, status) {
        var notifyBlock = $("#notifyCount");

        if(!data.hasOwnProperty("count")){
            location.replace("/");
        }

        else if (data.count != 0) {
            notifyBlock.addClass("show-count");
            notifyBlock.attr("data-count", data.count);
            notifyBlock.attr("href", "/notify");
        }
        else {
            notifyBlock.removeClass("show-count");
            notifyBlock.removeAttr("data-count");
            notifyBlock.removeAttr("href");
        }
    })
}


$().ready(on_ready);