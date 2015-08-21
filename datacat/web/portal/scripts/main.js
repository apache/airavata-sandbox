var loggedIn;                                                                   // initial user data
var user = '';
var allUserGroups;

var selectedDocId;                                                              // initial pagination parameters
var numberOfFields = 0;
var resultOffset = 1;
var numberOfResults = 10;
var lastData = '';
var nextDisabled = false;
var previewDisabled = true;

var fields = ["id", "fileName", "filePath", "ownerName", "generatedApplicationName", "dataArchiveNode", "createdDate"];                     // list of fields common for all data products

var service_url = "http://localhost/datacat/dataStoreService.php";

$(document).ready(function () {

    var loginPopover = $('#login-popover-btn');
    var logoutButton = $('#logout-btn');

    addFieldedField();                                                          // add one item to the graphical query construct by default

    loginPopover.popover({
        'placement': 'bottom',
        'html': true
    });

    loginPopover.hide();
    logoutButton.hide();
    $('#logged-is-as').hide();
    checkLogin();

    $(document).on("click", "#fielded-add-btn", addField);                      // bind events to dom items
    $(document).on("click", "#fielded-remove-btn", removeField);

    $(document).on("click", "#logout-btn", logout);
    $(document).on("click", "#login-btn", login);

    $(document).on("change", ".field-select", fieldChanged);
    $(document).on("change", ".relation-date", dateRelationChanged);

    $(document).on("click", "#search-btn", search);
    $('#form-outer-wrapper').keypress(function (e) {                            // trigger search function on pressing
        if (e.which == 13) {
            e.preventDefault();
            search();
            return false;
        }
    });

    $(document).on("click", "#getNext", getNext);
    $(document).on("click", "#getPrevious", getPrevious);

    $(document).on("click", ".share-btn", showAclList);
    $(document).on("click", "#update-acl-btn", updateAclList);

    getAllUserGroups();
});

var getMetadataFieldListCall = function () {                                    // get the full list of searchable/indexed fields
    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'getMetadataFieldList'
        }
    });

    request.done(function (msg) {
        var newFields = JSON.parse(msg);
        for (var i = 0; i < newFields.length; i++) {
            fields.push(newFields[i]);
        }

    addFieldedField();                                                          //Adding one field by default
    });

    request.fail(function (jqXHR, textStatus) {
        $('#results').html("<p style='text-align: center'>GET METADATA FIELD LIST FAILED</p>");
    });
};

var getAllUserGroups = function () {                                            // get the full list of user groups for sharing data products
    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'getAllUserGroups'
        }
    });

    request.done(function (msg) {
        allUserGroups = JSON.parse(msg);
    });

    request.fail(function (jqXHR, textStatus) {
        $('#results').html("<p style='text-align: center'>GET ALL USER GROUPS FAILED</p>");
    });
};

var login = function () {                                                       // authenticate user
    var username = $("#username").val();
    var password = $("#password").val();

    if (username == "") {
        alert("Please enter a valid username");
        return;
    } else if (password == "") {
        alert("Please enter a valid password");
        return;
    }

    var data = {
        "username": username,
        "password": password
    };

    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'login',
            'data': data
        }
    });

    request.done(function (msg) {
        if (msg == 'true') {
            loggedIn = true;
            user = username;
            checkLogin();
            $('#login-popover-btn').popover('hide');
        } else {
            alert('The username and password do not match.');
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert('Oops, something went wrong');
    });
};

var checkLogin = function () {                                                  //check user logged in and get username
    var loginPopover = $('#login-popover-btn');
    var logoutButton = $('#logout-btn');
    var loggedInAs = $('#logged-is-as');

    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'checkLogin'
        }
    });

    request.done(function (msg) {
        user = msg;
        var loginData = '<div id="login-form-wrapper">' +
            '    <form class="form-horizontal">' +
            '        <div class="form-group">' +
            '            <input type="text" placeholder="Username" id="username"/>' +
            '        </div>' +
            '        <div class="form-group">' +
            '            <input type="password" placeholder="Password" id="password"/>' +
            '        </div>' +

            '        <div class="form-group" id="login-btn-wrapper">' +
            '            <button type="button" class="btn btn-default col-xs-12" id="login-btn">Login</button>' +
            '        </div>' +
            '    </form>' +
            '</div>';

        if (msg == '%%false%%') {
            loggedIn = false;
            loginPopover.text('Log in');
            loginPopover.attr('data-content', loginData);
            logoutButton.hide();
            loginPopover.show();
            loggedInAs.hide();
        } else {
            loggedIn = true;
            loginPopover.text('Logged in');
            loginPopover.attr('data-content', '');
            loginPopover.hide();
            logoutButton.show();
            loggedInAs.html('Logged in as <strong>' + user + '</strong>');
            loggedInAs.show();
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert('Oops, something went wrong');
    });
};

var logout = function () {
    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'logout'
        }
    });

    request.done(function (msg) {
        if (msg) {
            loggedIn = false;
            user = '';
            checkLogin();
            $('#login-popover-btn').popover('hide');
        } else {
            alert('Logout failed.');
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert('Oops, something went wrong');
    });
};

var addField = function () {                                                    // add field to graphical query construct
    if (numberOfFields < 10) {
        addFieldedField();
    } else {
        alert('Too many fields');
    }
};

var addFieldedField = function () {
    var fieldsHtml = '<option value="id">Document ID</option>' +
        '<option value="fileName">File Name</option>' +
        '<option value="filePath">File Path</option>' +
        '<option value="gatewayId">Gateway ID</option>' +
        '<option value="applicationName">Application Name</option>' +
        '<option value="experimentId">Experiment ID</option>' +
        '<option value="computingResourceName">Computing Resource Name</option>' +
        '<option value="createdDate">Created Date</option>';

    numberOfFields += 1;

    $('#fielded-fields').append('<div class="form-group fielded-field" id="field-group-' + numberOfFields + '">' +
        '        <label class="col-xs-12">Field ' + numberOfFields + '</label>' +
        '        <div class="col-xs-6 col-sm-3">' +
        '            <select class="field-select form-control" id="field-' + numberOfFields + '">'
        + fieldsHtml +
        '            </select>' +
        '        </div>' +
        '        <div class="col-xs-6 col-sm-3">' +
        '            <select class="form-control" id="relation-' + numberOfFields + '">' +
        '                <option value="WILDCARD">Wildcard</option>' +
        '                <option value="EQUALS">Equals</option>' +
        '                <option value="PHRASE">Phrase</option>' +
        '                <option value="SUBSTRING">Substring</option>' +
        '            </select>' +
        '            </div>' +
        '            <div class="fielded-form-input-wrapper col-xs-12 col-sm-6">' +
        '                <input type="text" class="form-control" id="value-' + numberOfFields + '">' +
        '            </div>' +
        '        </div>');
};

var removeField = function () {                                                 // remove field from graphical query construct
    if (numberOfFields > 1) {
        $('#field-group-' + numberOfFields).remove();
        numberOfFields -= 1;
    } else {
        alert('You cannot remove the last field');
    }
};

var fieldChanged = function () {                                                // take action corresponding to selected query field
    var id = $(this).parent().parent().find('.fielded-form-input-wrapper input:first')[0].id.replace('value-', '');
    if ($(this).val() == 'createdDate') {
        $('#relation-' + id).parent().html('            <select class="relation-date form-control" id="relation-' + id + '">' +
            '                <option value="EQUALS">Equals</option>' +
            '                <option value="RANGE">Range</option>' +
            '            </select>');

        var html = '<input type="text" style="display:none;" class="form-control" id="value-' + id + '">' +
            '        <div class="input-group date datetimepicker">' +
            '            <input id="date-equals-' + id + '" type="text" class="form-control" />' +
            '            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>' +
            '        </div>';

        $(this).parent().parent().find('.fielded-form-input-wrapper:first').html(html);

        $('.datetimepicker').datetimepicker({
            maxDate: moment(),
            pickTime: false,
            defaultDate: moment(),
            format: "MM/DD/YYYY"
        });
    } else {
        $('#relation-' + id).parent().html('            <select class="form-control" id="relation-' + id + '">' +
            '                <option value="WILDCARD">Wildcard</option>' +
            '                <option value="EQUALS">Equals</option>' +
            '                <option value="PHRASE">Phrase</option>' +
            '                <option value="SUBSTRING">Substring</option>' +
            '            </select>');

        $(this).parent().parent().find('.fielded-form-input-wrapper:first').html('<input type="text" class="form-control" id="value-' + id + '">');
    }
};

var dateRelationChanged = function () {                                         // take action according to the date relation selected (range)
    var id = $(this).parent().parent().find('.fielded-form-input-wrapper input:first')[0].id.replace('value-', '');

    if ($(this).val() == 'RANGE') {
        var htmlRange = '<input type="text" style="display:none;" class="form-control" id="value-' + id + '">';
        htmlRange += '<form class="form-horizontal" role="form">' +
            '            <div style="margin-right: 0;" class="form-group">' +
            '            <label for="range-from" class="col-sm-3 col-md-2 control-label">From</label>' +
            '            <div class="col-sm-6 col-md-10 input-group date datetimepicker datetimepicker-from" id="datetimepicker-from-' + id + '">' +
            '            <input class="form-control" id="range-from-' + id + '">' +
            '            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>' +
            '            </div>' +
            '            </div>' +
            '        <div style="margin-right: 0;" class="form-group">' +
            '            <label for="range-to" class="col-sm-3 col-md-2 control-label">To</label>' +
            '            <div class="col-sm-6 col-md-10 input-group date datetimepicker datetimepicker-to" id="datetimepicker-to-' + id + '">' +
            '                <input type="text" class="form-control" id="range-to-' + id + '">' +
            '                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>' +
            '                </div>' +
            '            </div>' +
            '        </form>';

        $(this).parent().parent().find('.fielded-form-input-wrapper:first').html(htmlRange);

        $('.datetimepicker-from').datetimepicker({
            pickTime: false,
            defaultDate: moment().subtract(1, 'days'),
            format: "MM/DD/YYYY"
        });
        $('.datetimepicker-to').datetimepicker({
            maxDate: moment(),
            pickTime: false,
            defaultDate: moment(),
            format: "MM/DD/YYYY"
        });

        $('#datetimepicker-to-' + id).data("DateTimePicker").setMinDate(moment());
        $('#datetimepicker-from-' + id).data("DateTimePicker").setMaxDate(moment().subtract(1, 'days'));

        $("#datetimepicker-from-" + id).on("dp.change", function (e) {
            $('#datetimepicker-to-' + id).data("DateTimePicker").setMinDate(e.date);
        });
        $("#datetimepicker-to-" + id).on("dp.change", function (e) {
            $('#datetimepicker-from-' + id).data("DateTimePicker").setMaxDate(e.date);
        });
    } else if ($(this).val() == 'EQUALS') {
        var htmlEquals = '<input type="text" style="display:none;" class="form-control" id="value-' + id + '">' +
            '        <div class="input-group date datetimepicker">' +
            '            <input id="date-equals-' + id + '" type="text" class="form-control" />' +
            '            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>' +
            '        </div>';

        $(this).parent().parent().find('.fielded-form-input-wrapper:first').html(htmlEquals);

        $('.datetimepicker').datetimepicker({
            maxDate: moment(),
            pickTime: false,
            defaultDate: moment(),
            format: "MM/DD/YYYY"
        });
    }
};

var search = function () {                                                      // search data products
    var primaryQueryParameterList = [];
    var allFieldsEmpty = true;

    for (var i = 1; i <= numberOfFields; i++) {
        if ($('#field-' + i).val() == 'createdDate') {
            if ($('#relation-' + i).val() == 'EQUALS') {
                var primaryDateQueryParameter = {
                    "primaryQueryType": 'EQUALS',
                    "firstParameter": moment($("#date-equals-" + i).val()).format("YYYY-MM-DD") + "T00:00:00Z",
                    "secondParameter": null,
                    "field": $("#field-" + i).val()
                };
                primaryQueryParameterList.push(primaryDateQueryParameter);
                allFieldsEmpty = false;
            } else {
                var primaryRangeQueryParameter = {
                    "primaryQueryType": 'RANGE',
                    "firstParameter": moment($("#range-from-" + i).val()).format("YYYY-MM-DD") + "T00:00:00Z",
                    "secondParameter": moment($("#range-to-" + i).val()).format("YYYY-MM-DD") + "T00:00:00Z",
                    "field": $("#field-" + i).val()
                };
                primaryQueryParameterList.push(primaryRangeQueryParameter);
                allFieldsEmpty = false;
            }
        } else {
            var value = $("#value-" + i).val();
            if (value !== "") {
                var primaryQueryParameter = {
                    "primaryQueryType": $("#relation-" + i).val(),
                    "firstParameter": value,
                    "secondParameter": null,
                    "field": $("#field-" + i).val()
                };
                primaryQueryParameterList.push(primaryQueryParameter);
                allFieldsEmpty = false;
            }
        }
    }

    if (allFieldsEmpty) {
        alert("Please select some query parameters !!!");
        return;
    }

    var data = {
        "queryStringSet": false,

        "startRow": resultOffset,
        "numberOfRows": numberOfResults,

        "primaryQueryParameterList": primaryQueryParameterList
    };

    lastData = data;
    getResultsCall(data);

};

var getNext = function () {                                                     // get next results set
    if (!nextDisabled) {
        resultOffset += numberOfResults;
        lastData.startRow = resultOffset;
        previewDisabled = false;

        getResultsCall(lastData);
    }
};

var getPrevious = function () {                                                 //get previous results set
    if (resultOffset != 1) {
        if ((resultOffset - numberOfResults) == 1) {
            previewDisabled = true;
        }
        resultOffset -= numberOfResults;
        lastData.startRow = resultOffset;
        nextDisabled = false;

        getResultsCall(lastData);
    }
};

var getResultsCall = function (data) {                                          // common function to het results for searching and pagination
    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'getResults',
            'data': data
        }
    });

    request.done(function (msg) {
        var result = JSON.parse(msg);

        if (result.length == 0) {
            if (resultOffset == 1) {
                $('#results').html("<p style='text-align: center'>NO RESULTS TO SHOW</p>");
            } else {
                nextDisabled = true;
                resultOffset -= numberOfResults;
                lastData.startRow = resultOffset;
                $('#getNext').addClass('disabled');
                alert('No more results to show.');
            }
        } else {
            for (var i = 0; i < result.length; i++) {
                if (result[i].ownerId == user) {
                    result[i].ownerId += '<br>' +
                        '<button type="button" class="btn btn-primary btn-sm share-btn" id="share-' + i + '" data-toggle="modal" data-target="#myModal">Share</button>';
                }
            }

            // generate table header
            var htmlTable = '<div class="results-table-wrapper">' +
                '<table class="table table-striped table-bordered table-hover table-responsive" id="results-table">' +
                '        <thead>' +
                '            <th>Document Id</th>' +
                '            <th>Owner ID</th>' +
                '            <th>File Name</th>' +
                '            <th>File Path</th>' +
                '            <th>Gateway ID</th>' +
                '            <th>Application Name</th>' +
                '            <th>Experiment ID</th>' +
                '            <th>Resource Name</th>' +
                '            <th>Created Date</th>' +
                '        </thead>' +
                '        <tbody>';

            // generate table rows
            for (var j = 0; j < result.length; j++) {
                htmlTable += '<tr>' +
                    '   <td style="max-width: 150px; min-width: 150px; overflow-x: auto">' + result[j].id + '</td>' +
                    '   <td style="max-width: 100px; min-width: 100px; overflow-x: auto">' + result[j].ownerId + '</td>' +
                    '   <td style="max-width: 120px; min-width: 120px; overflow-x: auto">' + result[j].fileName + '</td>' +
                    '   <td style="max-width: 120px; min-width: 120px; overflow-x: auto">' + result[j].filePath + '</td>' +
                    '   <td style="max-width: 80px; min-width: 80px; overflow-x: auto">' + result[j].gatewayId + '</td>' +
                    '   <td style="max-width: 80px; min-width: 80px; overflow-x: auto">' + result[j].applicationName + '</td>' +
                    '   <td style="max-width: 80px; min-width: 80px; overflow-x: auto">' + result[j].experimentId + '</td>' +
                    '   <td style="max-width: 80px; min-width: 80px; overflow-x: auto">' + result[j].computingResourceName + '</td>' +
                    '   <td style="max-width: 120px; min-width: 120px; overflow-x: auto">' + moment(result[j].createdDate).format("MMM DD, YYYY") + '</td>' +
                    '</tr>';

            }
            htmlTable += '</tbody></table></div>' +
                '        <ul class="pager">' +
                '            <li class="previous" id="getPrevious"><a href="javascript:;">&larr; Previous</a></li>' +
                '            <li class="next" id="getNext"><a href="javascript:;">Next &rarr;</a></li>' +
                '        </ul>';

            $('#results').html(htmlTable);

            if (previewDisabled) {
                $('#getPrevious').addClass('disabled');
            }
            if (nextDisabled) {
                $('#getNext').addClass('disabled');
            }

            $("html, body").animate({ scrollTop: $('#results').offset().top }, 500);
        }
    });

    request.fail(function (jqXHR, textStatus) {
        $('#results').html("<p style='text-align: center'>GET RESULTS FAILED</p>");
    });
};

var showAclList = function () {                                                 // show the list of available communities to share with
    var thisId = this.id;
    var id = $('#' + thisId).parent().parent().find('td:first').text();         // TODO: If you change the table order, use another way to get the document id
    selectedDocId = id;

    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'getAclList',
            'data': {
                'id': id
            }
        }
    });

    request.done(function (msg) {
        var aclList = JSON.parse(msg);

        var modalHtml = '<p>Document ID: <strong>' + selectedDocId + '</strong></p>';

        for (var i = 0; i < allUserGroups.length; i++) {
            modalHtml += '<div class="checkbox">' +
                '        <label>' +
                '            <input type="checkbox" id="' + allUserGroups[i] + '" value="' + allUserGroups[i] + '">' + allUserGroups[i] +
                '        </label>' +
                '    </div>';
        }

        $('#modal-body').html(modalHtml);

        for (var j = 0; j < aclList.length; j++) {
            $("#" + aclList[j]).prop('checked', true);
        }
    });

    request.fail(function (jqXHR, textStatus) {
        $('#results').html("<p style='text-align: center'>GET ACL LIST FAILED</p>");
    });

};

var updateAclList = function () {                                               // update ACL list of a data product
    var id = selectedDocId;
    var newAclList = [];

    for (var i = 0; i < allUserGroups.length; i++) {
        if ($('#' + allUserGroups[i]).prop('checked')) {
            newAclList.push(allUserGroups[i]);
        }
    }
    newAclList.push(user);

    var request = $.ajax({
        url: service_url,
        type: "POST",
        data: {
            'call': 'updateAclList',
            'data': {
                'id': id,
                'acl': newAclList
            }
        }
    });

    request.done(function (msg) {
        alert('Done');
    });

    request.fail(function (jqXHR, textStatus) {
        $('#results').html("<p style='text-align: center'>UPDATE ACL LIST FAILED</p>");
    });

};