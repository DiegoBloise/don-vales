function expiredSession(ms) {
    setTimeout(function() {
        window.location.reload(true);
    }, ms);
}

var ms = 6 * 60 * 60 * 1000;
expiredSession(ms);
