var notifyChecker;

function on_ready() {
    notifyChecker = setInterval(getCountUnreadEvents, 1000)
    getCountUnreadEvents();
}

function getCountUnreadEvents() {
    $.get("/notify/getCountUnread", {}, function (data, status) {
        var notifyBlock = $("#notifyCount")
        var notifyCount = data.count
        if (notifyCount) {
            notifyBlock.addClass("show-count")
            notifyBlock.attr("data-count", notifyCount);
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