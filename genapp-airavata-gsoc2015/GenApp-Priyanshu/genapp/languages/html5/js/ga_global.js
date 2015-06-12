/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

var ga = {};
ga.tmp = {};

// extend jstree for singleselect & conditional select plugins:

(function ($, undefined) {
  "use strict";
  $.jstree.defaults.conditionalselect = function () { return true; };
  $.jstree.plugins.conditionalselect = function (options, parent) {
    this.activate_node = function (obj, e) {
      if(this.settings.conditionalselect.call(this, this.get_node(obj))) {
        parent.activate_node.call(this, obj, e);
      }
    };
  };
  $.jstree.plugins.singleselect = function (options, parent) {
    this.activate_node = function (obj, e) {
      if(this.is_leaf( obj )) {
        parent.activate_node.call(this, obj, e);
      }
    };
  };
  $.jstree.plugins.selectonlyleaf = function (options, parent) {
    this.activate_node = function (obj, e) {
      if(this.is_leaf( obj )) {
        parent.activate_node.call(this, obj, e);
      }
    };
  };
  $.jstree.plugins.singleselectpath = function (options, parent) {
    this.activate_node = function (obj, e) {
      if(!this.is_leaf( obj )) {
        parent.activate_node.call(this, obj, e);
      }
    };
  };
  $.jstree.plugins.selectnoleaf = function (options, parent) {
    this.activate_node = function (obj, e) {
      if(!this.is_leaf( obj )) {
        parent.activate_node.call(this, obj, e);
      }
    };
  };
  $.jstree.defaults.sort = function (a,b) {
      return this.get_node( a ).data.time < this.get_node( b ).data.time ? 1 : -1;
  };
})(jQuery);

RegExp.quote = function(str) {
   return str.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1");
};

Object.size = function(obj) {
__~debug:pull{   console.log( "object.size called" )}
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
__~debug:pull{   console.log( "object.size size " + size )}
    return size;
};

ga.colors = function( colors ) {
    ga.colors.background = colors.background;
    ga.colors.text       = colors.text;
};

ga.plot_options = function () {
    var textcolor = "rgb( " + ga.colors.text + " )",
        retobj = {
            font : {
                color : textcolor
            },
            grid : {
                hoverable: false
            },
            xaxis : {
                color : "gray",
                lineWidth : 0.5,
                font : {
                    color : textcolor
                }
            },
            yaxis : {
                color : "gray",
                lineWidth : 0.5,
                font : {
                    color : textcolor
                }
            },
            lines: { 
                lineWidth : 1.0
            },
            zoom: {
                interactive: false
            },
            pan: {
                interactive: false
            }
        };

    return retobj;
};

ga.set = function( param, value ) {
    if ( value ) {
        ga.set.data[ param ] = value;
    }
    return ga.set.data[ param ];
}

ga.set.data = {};

ga.admin = {};
