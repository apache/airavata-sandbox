<!DOCTYPE html>
<html>

<head>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale = 1.0">

    <title>ADS web Portal</title>

    <link rel="stylesheet" href="styles/main.css"/>
    <link rel="stylesheet" href="styles/bootstrap.css"/>
    <link rel="stylesheet" href="styles/bootstrap-theme.css"/>
    <link rel="stylesheet" href="styles/bootstrap-datetimepicker.min.css"/>

    <script src="scripts/jquery-1.11.1.js"></script>
    <script src="scripts/moment.js"></script>
    <script src="scripts/bootstrap.js"></script>
    <script src="scripts/bootstrap-datetimepicker.js"></script>
    <script src="scripts/main.js"></script>
</head>

<body>

<div class="header-wrapper">
    <div class="header col-xs-12">
        <div class="col-xs-7">
            <h1>Airavata Data Store</h1>
        </div>
        <div class="col-xs-5">
            <p id="logged-is-as"></p>
            <button id="logout-btn" type="button" class="btn btn-default btn-success">Log out</button>
            <button id="login-popover-btn" type="button" class="btn btn-primary">Log in</button>
        </div>
    </div>
</div>

<div class="content-wrapper">
    <div class="content">

        <div class="col-xs-12" id="form-outer-wrapper">

            <div class="fielded-form-wrapper col-xs-12">
                <div class="fielded-form-inner-wrapper">
                    <form class="form-horizontal" role="form">

                        <div id="fielded-fields">
                        </div>

                        <div class="form-group">
                            <button type="button" class="btn btn-default col-xs-offset-1 col-xs-2" id="fielded-add-btn">
                                Add Field
                            </button>
                            <button type="button" class="btn btn-default col-xs-offset-1 col-xs-2"
                                    id="fielded-remove-btn">Remove Field
                            </button>
                            <button type="button" class="btn btn-default btn-primary col-xs-offset-2 col-xs-2"
                                    id="search-btn">Search
                            </button>
                        </div>

                    </form>
                </div>
            </div>

            <!--<div class="form-group">
                <button type="button" class="btn btn-default btn-success col-xs-offset-4 col-xs-4" id="search-btn">Search</button>
            </div>-->

        </div>

        <div class="col-xs-12" id="results"></div>

        <div class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Select groups you want to share this document with.</h4>
                    </div>
                    <div class="modal-body" id="modal-body">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" id="update-acl-btn">Save changes</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>


</body>
</html>