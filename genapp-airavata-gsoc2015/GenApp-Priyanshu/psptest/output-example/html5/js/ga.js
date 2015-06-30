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

    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }

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
/*jslint white: true, plusplus: true*/
/* assumes: ga, jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */


ga.event = function ( menu, module, action ) {

    ga.event.log.push( {
        menu   : menu,
        module : module,
        action : action,
        when   : new Date()
    });
}

ga.event.log = [];

ga.event.list = function() {
    var j=0,
        l = ga.event.log.length,
        now = new Date(),
        result = "Client Date/Time is " + now.toUTCString() + "\n";


    for ( ; j < l ; j++ ) {
        result += ga.event.log[ j ].menu + " " + ga.event.log[ j ].module + " " + ga.event.log[ j ].action + " " + ga.event.log[ j ].when.toUTCString() + "\n";
    }


    return result;
}

    

    


/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.fc = function( id, cb ) {
    var i,
        waits;

    
    if ( ga.fc.cache[ id ] ) {
        
        cb( ga.fc.cache[ id ] );
    } else {
        // only one ajax call per id
        if ( !ga.fc.waits[ id ] )
        {
            
            ga.fc.waits[ id ] = [ cb ];
            $.ajax( ga.fc.url + id ).success( function( data ) {
                
                waits = ga.fc.waits[ id ];
                delete ga.fc.waits[ id ];
                data = $.parseJSON( data );
                ga.fc.cache[ id ] = data;
                
                for ( i = waits.length; i--; )
                {
                    waits[ i ]( data );
                }
            }).error( function( error ) {
                
                waits = ga.fc.waits[ id ];
                delete ga.fc.waits[ id ];
                console.log( "ajax error" );
                console.dir( error );
                for ( i = waits.length; i--; )
                {
                    waits[ i ]( "**error**" );
                }
                ga.fc.ajax_error_msg( "ajax get error: " + error.statusText );
            });
        } else {
            
            ga.fc.waits[ id ].push( cb );
        }
    }
    return true;
};

ga.fc.clear = function( id ) {
    var i,
        children = ga.fc.children( id );
    

    for ( i in children ) {
        if ( ga.fc.cache[ i ] ) {
            delete ga.fc.cache[ i ];
        }
    }
    if ( ga.fc.cache[ id ] ) {
        delete ga.fc.cache[ id ];
    }
    for ( i in ga.fc.trees ) {
        if ( $( i ).length )
        {
            if ( id !== "#" ) {
                $( i ).jstree( true ).refresh_node( id );
            } else {
                $( i ).jstree( true ).refresh();
            }
        }
    }
};

ga.fc.refresh = function( id ) {
    var i;

    

    for ( i in ga.fc.trees ) {
        if ( $( i ).length )
        {
            if ( ga.fc.cache[ id ] ) {
                if ( id !== "#" ) {
                    $( i ).jstree( true ).refresh_node( id );
                } else {
                    $( i ).jstree( true ).refresh();
                }
            }
        }
    }
};


ga.fc.delete_node = function( ids ) {
    var i;

    

    if ( !ids.length ) {
        return;
    }
        

    if ( ga.fc.url_delete && ga.fc.url_delete.length !== 0 ) {
        $.ajax({
              url      : ga.fc.url_delete,
              data     :  {
                            _window : window.name,
                           _spec   : "fc_cache",
                           _delete : ids.join( ',' )
                         },
              dataType : 'json',
              method   : 'POST'
            }).success( function( data ) {
            console.log( "ajax delete done" );
//            console.dir( data );
            if ( data.error && data.error.length ) {
//                ga.fc.refresh( "#" );
                ga.fc.delete_error_msg( ids, data.error );
            } else {
// we are always clearing the whole tree on delete
//                if ( data.reroot && data.reroot === 1 ) {
                ga.fc.clear( "#" );
//                } else {
//                    for ( i in ga.fc.trees ) {
//                        if ( $( i ).length )
//                        {
//                            console.log( "ga.fc.remove from tree " + i );
//                            console.dir( ids );
//                            $( i ).jstree( true ).delete_node( ids );
//                        }
//                    }
//                }
            }

        }).error( function( error ) {
            console.log( "ajax error" );
            console.dir( error );
//            ga.fc.refresh( "#" );
            ga.fc.ajax_error_msg( "ajax delete error: " + error.statusText );
        });
;
    } else {
        console.log( "ga.fc.delete_node, no url_delete " + ids.join( "," )  );
    }
};

ga.fc.delete_node_message = function( ids ) {
    var msg = "You are about to permanently remove " + ids.length + " file";
//        strip2 = function(str) { return str.substr( 2 ); };    

    if ( !ids.length ) {
        return "Can not remove a directory from here";
    }
    if ( ids.length > 1 ) {
        msg += "s and/or directories";
    } else {
        msg += " or directory";
    }
    msg += " and the contents, including subdirectories, of any directory listed below<p>";

    return msg;
// :<p>" + $.map( $.map( ids.slice( 0, 5 ), $.base64.decode ), strip2 ).join( "<p>" );
//    if ( ids.length > 5 ) {
//        msg += "<p> Note: an additional " + ( ids.length - 5 ) + " entr";
//        if ( ids.length > 6 ) {
//            msg += "ies are not shown. ";
//        } else {
//            msg += "y is not shown. ";
//        }
//    }
//    return msg;
};

ga.fc.delete_node_message_files = function( ids ) {
    return ids.length ? "<div class=\"table-wrapper\"><table><tr><td>" + 
           $.map( $.map( ids, $.base64.decode ), function(str) { return str.substr( 2 ); } )
           .join( "</td></tr><tr><td>" ) + "</td></tr></table></div>" : "";
};

ga.fc.children = function( id, result ) {
    var i,
        idc;
    
    result = result || {};
    if ( ga.fc.cache[ id ] )
    {
        // expand and return all children in the cache
        for ( i = ga.fc.cache[ id ].length; i--; ) {
            if ( ga.fc.cache[ id ][ i ].children ) {
                idc = ga.fc.cache[ id ][ i ].id;
                if ( ga.fc.cache[ idc ] ) {
                   result[ idc ] = true;
                   result = ga.fc.children( idc, result );
                }
            }
        }
    }
    return result;
};   

ga.fc.cache = {};
ga.fc.waits = {};
ga.fc.trees = {};
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.jc = function( id, cb ) {
    var i,
        waits;

;

    if ( ga.jc.cache[ id ] ) {
        cb( ga.jc.cache[ id ] );
    } else {
        // only one ajax call per id
        if ( !ga.jc.waits[ id ] )
        {
            ga.jc.waits[ id ] = [ cb ];
            $.ajax( { url:ga.jc.url , data:{ _tree:id, _window: window.name } } ).success( function( data ) {
                waits = ga.jc.waits[ id ];
                delete ga.jc.waits[ id ];
                data = $.parseJSON( data );
                ga.jc.cache[ id ] = data;
                
                for ( i = waits.length; i--; )
                {
                    waits[ i ]( data );
                }
            }).error( function( error ) {
                waits = ga.jc.waits[ id ];
                delete ga.jc.waits[ id ];
                console.log( "ajax error" );
                console.dir( error );
                for ( i = waits.length; i--; )
                {
                    waits[ i ]( "**error**" );
                }
                ga.jc.ajax_error_msg( "ajax get error: " + error.statusText );
            });
        } else {
            ga.jc.waits[ id ].push( cb );
        }
    }
    return true;
};

ga.jc.clear_leaf = function( id ) {
;
    var i,
        any_contain = 0,
        node;

// does any tree currently have the node ?
    for ( i in ga.jc.trees ) {
        if ( $( i ).length )
        {
            node = $( i ).jstree( true ).get_node( id );
            if ( node ) {
                any_contain = 1;
                break;
            }
        }
    }

    any_contain ? ga.jc.clear( node.parent ) : ga.jc.clear( "#" );
}

ga.jc.clear = function( id ) {
    var i,
        children = ga.jc.children( id );

    for ( i in children ) {
        if ( ga.jc.cache[ i ] ) {
            delete ga.jc.cache[ i ];
        }
    }
    if ( ga.jc.cache[ id ] ) {
        delete ga.jc.cache[ id ];
    }
    for ( i in ga.jc.trees ) {
        if ( $( i ).length )
        {
            if ( id !== "#" ) {
                $( i ).jstree( true ).refresh_node( id );
            } else {
                $( i ).jstree( true ).refresh();
            }
        }
    }
};

ga.jc.refresh = function( id ) {
    var i;

    for ( i in ga.jc.trees ) {
        if ( $( i ).length )
        {
            if ( ga.jc.cache[ id ] ) {
                if ( id !== "#" ) {
                    $( i ).jstree( true ).refresh_node( id );
                } else {
                    $( i ).jstree( true ).refresh();
                }
            }
        }
    }
};


ga.jc.delete_node = function( ids ) {
    var i;

    if ( !ids.length ) {
        return;
    }
        

    if ( ga.jc.url_delete && ga.jc.url_delete.length !== 0 ) {
        $.ajax({
              url      : ga.jc.url_delete,
              data     :  {
                            _window : window.name,
                           _spec   : "fc_cache",
                           _delete : ids.join( ',' )
                         },
              dataType : 'json',
              method   : 'POST'
            }).success( function( data ) {
            console.log( "ajax delete done" );
//            console.dir( data );
            if ( data.error && data.error.length ) {
//                ga.jc.refresh( "#" );
                ga.jc.delete_error_msg( ids, data.error );
            } else {
// we are always clearing the whole tree on delete
//                if ( data.reroot && data.reroot === 1 ) {
                ga.jc.clear( "#" );
//                } else {
//                    for ( i in ga.jc.trees ) {
//                        if ( $( i ).length )
//                        {
//                            console.log( "ga.jc.remove from tree " + i );
//                            console.dir( ids );
//                            $( i ).jstree( true ).delete_node( ids );
//                        }
//                    }
//                }
            }

        }).error( function( error ) {
            console.log( "ajax error" );
            console.dir( error );
//            ga.jc.refresh( "#" );
            ga.jc.ajax_error_msg( "ajax delete error: " + error.statusText );
        });
;
    } else {
        console.log( "ga.jc.delete_node, no url_delete " + ids.join( "," )  );
    }
};

ga.jc.delete_node_message = function( ids ) {
    var msg = "You are about to permanently remove " + ids.length + " job";

    if ( ids.length > 1 ) {
        msg += "s";
    }
    return msg;
};

ga.jc.delete_node_message_files = function( ids ) {
    return ids.length ? "<div class=\"table-wrapper\"><table><tr><td>" + 
           $.map( $.map( ids, $.base64.decode ), function(str) { return str.substr( 2 ); } )
           .join( "</td></tr><tr><td>" ) + "</td></tr></table></div>" : "";
};

ga.jc.children = function( id, result ) {
    var i,
        idc;
    result = result || {};
    if ( ga.jc.cache[ id ] )
    {
        // expand and return all children in the cache
        for ( i = ga.jc.cache[ id ].length; i--; ) {
            if ( ga.jc.cache[ id ][ i ].children ) {
                idc = ga.jc.cache[ id ][ i ].id;
                if ( ga.jc.cache[ idc ] ) {
                   result[ idc ] = true;
                   result = ga.jc.children( idc, result );
                }
            }
        }
    }
    return result;
};   

ga.jc.cache = {};
ga.jc.waits = {};
ga.jc.trees = {};
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.altfile = function( module, idfile, idref ) {

    ga.altfile.data[ module ] = ga.altfile.data[ module ] || {};
    ga.altfile.data[ module ][ idfile ] = idref;
};

ga.altfile.data  = {};
ga.altfile.bdata = {};

ga.altfile.list = function( module ) {
    var i;
    if ( !ga.altfile.data[ module ] ) {
        console.log( "module:" + module + " Empty" );
        return;
    }

    for ( i in ga.altfile.data[ module ] ) {
        console.log( "module:" + module + " idfile:" + i + " idref:" + ga.altfile.data[ module ][ i ] );
    }
};

ga.altfile.listall = function() {
    var i;
    if ( !ga.altfile.data ) {
        console.log( "ga.altfile:no modules" );
        return;
    }

    for ( i in ga.altfile.data ) {
        ga.altfile.list( i );
    }
};
    
ga.altfile.test = function() {
   ga.altfile( "module1", "field1", "ref1" );
   ga.altfile( "module1", "field2", "ref2" );
   ga.altfile( "module2", "field3", "ref3" );
   ga.altfile.listall();
};

ga.altfile.add = function( module ) {
   var i,
       add = "",
       ms = "#" + module;


   $( ms + " ._hidden_altfiles" ).remove();
   $( ms + " ._hidden_buttonvals" ).remove();

   if ( ga.altfile.data[ module ] ) {
      for ( i in ga.altfile.data[ module ] ) {

         add += '<input type="hidden" name="_selaltval_' + i + '" value="' + ga.altfile.data[ module ][ i ] + '" class="_hidden_altfiles">';
      }
   }

   if ( ga.altfile.bdata[ module ] ) {
      for ( i in ga.altfile.bdata[ module ] ) {

         add += '<input type="hidden" name="' + i + '" value="' + ga.altfile.bdata[ module ][ i ].val + '" class="_hidden_buttonvals">';
      }
   }

   if ( add.length ) {
      $( ms ).append( add );
   }
};

ga.altfile.button = function( module, id, text, call, cb, required ) {

   ga.altfile.bdata[ module ] = ga.altfile.bdata[ module ] || {};
   ga.altfile.bdata[ module ][ id ] = {};
   ga.altfile.bdata[ module ][ id ].val = {};
   ga.altfile.bdata[ module ][ id ].text = text;
   ga.altfile.bdata[ module ][ id ].call = call;  // the name of the sys module to call
   ga.altfile.bdata[ module ][ id ].cb = cb;      // the callback called upon 'submit' cb of the module
   ga.altfile.bdata[ module ][ id ].req = required || 0;
}

ga.altfile.button.value = function( module, id, val ) {

   ga.altfile.bdata[ module ][ id ].val = val;
}

ga.altfile.button.call = function( module, id ) {
   var tmp;

   if ( ga.altfile.bdata[ module ][ id ].call.length ) {
       tmp = $( '#_state' ).data( '_logon' );
       if ( !tmp || !tmp.length ) {
           messagebox( {
               icon : "warning.png",
               text : "You must login to browse server information",
               buttons : [
                 { id    : "ok",
                   label : "OK" } ]
            });
       } else {
          $( "#configbody" ).load( "etc/" + ga.altfile.bdata[ module ][ id ].call + ".html", function() {
// ok, this is saving the last call back, but modals are singleton, so it *should* be ok
              ga.altfile.bdata[ ga.altfile.bdata[ module ][ id ].call ] = {};
              ga.altfile.bdata[ ga.altfile.bdata[ module ][ id ].call ].cb = ga.altfile.bdata[ module ][ id ].cb;

              $( "#" + ga.altfile.bdata[ module ][ id ].call + "text_label" ).text( ga.altfile.bdata[ module ][ id ].text );
          });
          ga.repeats.save();
          $( ".modalDialog" ).addClass( "modalDialog_on" );
       }
   } else {
     ga.altfile.bdata[ module ][ id ].cb("cb");
   }
   return false;
}

ga.altfile.button.simplecall = function( module, id ) {
    var tmp;
    
    $( "#configbody" ).load( "ajax/" + module + "/" + id + ".html", function() {
        
    });
    ga.repeats.save();
    $( ".modalDialog" ).addClass( "modalDialog_on" );
    return false;
}

ga.altfile.button.cb = function() {

   closeModal();
}

ga.altfile.button.lrfile = function( treeid, module, id ) {
  var r      = [],
      hmod   = "#" + module,
      hid    = "#" + id,
      add    = "",
      hclass = "_hidden_lrfile_sels_" + id;


  $( hmod + " ." + hclass ).remove();
  $.each( $(treeid).jstree("get_checked", true), function() {

     if ( !this.children.length ) {
       add += '<input type="hidden" name="' + id + '_altval[]" value="' + this.id + '" class="' + hclass + '">';
       r.push( $.base64.decode( this.id ).substr( 2 ) );
     }
  });

  if ( r.length ) {
     $( hid + "_altval").html( "<i>Server</i>: " + r );
     $( hid + "_msg").html( "" );
     $( hid ).val("");
     $( hmod ).append( add );

  }
}
   
ga.altfile.button.rpath = function( treeid, module, id ) {
  var r      = [],
      hmod   = "#" + module,
      hid    = "#" + id,
      add    = "",
      hclass = "_hidden_rpath_sels_" + id,
      s      = $(treeid).jstree(true);


  $( hmod + " ." + hclass ).remove();
  $.each( s.get_top_checked(true), function() {
 
     if ( !s.is_leaf( this ) ) {
       add += '<input type="hidden" name="' + id + '[]" value="' + this.id + '" class="' + hclass + '">' +
              '<input type="hidden" name="_decodepath_' + id + '" class="' + hclass + '">';
       r.push( $.base64.decode( this.id ).substr( 2 ) );
     }
  });

  if ( r.length ) {
     $( hid + "_altval").html( "<i>Server</i>: " + r );
     $( hid + "_msg").html( "" );
     $( hid ).val("");
     $( hmod ).append( add );

  }
}

ga.altfile.button.rfile = function( treeid, module, id ) {
  var r      = [],
      hmod   = "#" + module,
      hid    = "#" + id,
      add    = "",
      hclass = "_hidden_rfile_sels_" + id,
      s      = $(treeid).jstree(true);


  $( hmod + " ." + hclass ).remove();
  $.each( $(treeid).jstree("get_checked", true), function() {

     if ( !this.children.length ) {
       add += '<input type="hidden" name="' + id + '_altval[]" value="' + this.id + '" class="' + hclass + '">';
       r.push( $.base64.decode( this.id ).substr( 2 ) );
     }
  });

  if ( r.length ) {
     $( hid + "_altval").html( "<i>Server</i>: " + r );
     $( hid + "_msg").html( "" );
     $( hid ).val("");
     $( hmod ).append( add );

  }
}

ga.altfile.button.job = function( treeid, module, id ) {
  var r      = "",
      hmod   = "#" + module,
      hid    = "#" + id,
      add    = "",
      hclass = "_hidden_job_sels_" + id,
      s      = $(treeid).jstree(true);


  $( hmod + " ." + hclass ).remove();
  $.each( $(treeid).jstree("get_checked", true), function() {

     if ( !this.children.length ) {
       add += '<input type="hidden" name="' + id + '_altval[]" value="' + this.id + '" class="' + hclass + '">';
       r+="<tr><td>" + this.parent +":" + this.text + "</td></tr>";
     }
  });

  if ( r.length ) {
     $( hid + "_altval").html( "<table>" + r + "</table>" );
     $( hid + "_msg").html( "" );
     $( hid ).val("");
     $( hmod ).append( add );

  }
}

// ga.altfile.test();

ga.altfile.button.getnames = function( id, type ) {
    var r = [];
    switch( type ) {
    case "rpath" :
        r.push( id + '[]' );
        // r.push( '_decodepath_' + id );
        break;
    case "rfile" :
        r.push( id + '_altval[]' );
        break;
    case "lrfile" :
        id = id.replace( /_button$/, "" );
        r.push( id + '_altval[]' );
        break;
    default :
        console.warn( "ga.altfile.button.getnames( " + id + " , " + type + " )" );
        break;
    }
    return r;
}

ga.altfile.button.getnamesinput = function( id, type ) {
    var r = [];
    switch( type ) {
    case "rpath" :
        r.push( id );
        // r.push( '_decodepath_' + id );
        break;
    case "rfile" :
        r.push( id + '_altval' );
        break;
    case "lrfile" :
        id = id.replace( /_button$/, "" );
        r.push( id + '_altval' );
        break;
    default :
        console.warn( "ga.altfile.button.getnames( " + id + " , " + type + " )" );
        break;
    }
    return r;
}


// this should probably be moved to load and not added at the end
ga.altfile.button.addhtml = function( mod, id, type, vals ) {
    var add = "",
        hclass;

    
    

    switch( type ) {
    case "rpath" :
        hclass = "_hidden_rpath_sels_" + id;
        add += '<input type="hidden" name="' + id + '[]" value="' + vals[ 0 ] + '" class="' + hclass + '">' +
               '<input type="hidden" name="_decodepath_' + id + '" class="' + hclass + '">';
        break;
    case "rfile" :
        hclass = "_hidden_rfile_sels_" + id,
        add += '<input type="hidden" name="' + id + '_altval[]" value="' + vals[ 0 ] + '" class="' + hclass + '">';
        break;
    case "lrfile" :
        id = id.replace( /_button$/, "" );
        hclass = "_hidden_lrfile_sels_" + id;
        add += '<input type="hidden" name="' + id + '_altval[]" value="' + vals[ 0 ] + '" class="' + hclass + '">';
        break;
    default :
        console.warn( "ga.altfile.button.getnames( " + id + " , " + type + " )" );
        break;
    }
    $( "#" + mod ).append( add );
    
    
}

/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.valid = {};

ga.valid.checkMatch = function( tag1, tag2 ) {

   if ( $( tag1 ).val() != $( tag2 ).val() )
   {
      $( tag1 + "_msg" ).html( " does not match" );
   } else {
      $( tag1 + "_msg" ).html( "" );
   }
}
    
ga.valid.checkFloat = function( tag ) {

   var t = $( tag );
   var fieldValue=t.val();
   if ( isNaN( fieldValue ) )
   {
       t.val( t.prop( "defaultValue" ) );
       $( tag + "_msg" ).html( " not a valid floating point number, reset to default" );
   } else {
       if ( fieldValue < parseFloat ( t.attr( "min" ) ) )
       { 
           t.val( t.attr( "min" ) );
           $( tag + "_msg" ).html( " value set to minimum allowed" );
       } else {
           if ( fieldValue > parseFloat ( t.attr( "max" ) ) )
           { 
               t.val( t.attr( "max" ) );
               $( tag + "_msg" ).html( " value set to maximum allowed" );
           } else {
               $( tag + "_msg" ).html( "" );
           }
       } 
   }
}

ga.valid.checkInt = function( tag ) {

   var t = $( tag );
   var fieldValue=t.val();
   if ( isNaN( fieldValue ) )
   {
       t.val( t.prop( "defaultValue" ) );
       $( tag + "_msg" ).html( " not a valid number, reset to default" );
   } else {
       if ( fieldValue < parseInt ( t.attr( "min" ) ) )
       { 
           t.val( t.attr( "min" ) );
           $( tag + "_msg" ).html( " value set to minimum allowed" );
       } else {
           if ( fieldValue > parseInt ( t.attr( "max" ) ) )
           { 
               t.val( t.attr( "max" ) );
               $( tag + "_msg" ).html( " value set to maximum allowed" );
           } else {
               if ( parseInt( fieldValue ) != fieldValue )
               {   
                   $( tag + "_msg" ).html( " value rounded to nearset integer" );
                   t.val( parseInt( parseFloat( fieldValue ) + .5 ) );
               } else {
                   $( tag + "_msg" ).html( "" );
               }
           }
       }
   }
}

ga.valid.safeFile = function( tag ) {
   var t = $( tag );
   var fieldValue=t.val();
   if ( !fieldValue.match( "^[a-zA-Z0-9]+([a-zA-Z0-9_\.\-]+|\/[a-zA-Z0-9_\-])+$" ) )
   {
       t.val( t.prop( "defaultValue" ) );
       $( tag + "_msg" ).html( "Not an acceptable filename, reset to default" );
   } else {
       $( tag + "_msg" ).html( "" );
   }
}

ga.valid.checkLrfile = function( tag ) {

   var t   = $( tag ),
       r   = $( tag + '_altval > i' ),
       msg = $( tag + "_msg" ),
       ok  = 0;
   if ( !t || !t.is(':visible') ) {
       return 1;
   }
   if ( t && t.val() && t.val().length ) {

       ok = 1;
   } else {
       if ( r && r.html() && r.html().length && r.html() === "Server" ) {

           ok = 1;
       }
   }
   if ( !ok ) {
       msg.html( " missing required field" );
   }
   return ok;
}

ga.valid.checkRpath = function( tag ) {

   var t   = $( tag ),
       r   = $( tag + '_altval > i' ),
       msg = $( tag + "_msg" ),
       ok  = 0;

   if ( !t || !t.is(':visible') ) {
       return 1;
   }
   if ( r && r.html() && r.html().length && r.html() === "Server" ) {

       ok = 1;
   }
   if ( !ok ) {
       msg.html( " missing required field" );
   }

   return ok;
}

ga.valid.checkRfile = function( tag ) {

   var t   = $( tag ),
       r   = $( tag + '_altval > i' ),
       msg = $( tag + "_msg" ),
       ok  = 0;

   if ( !t || !t.is(':visible') ) {
       return 1;
   }
   if ( r && r.html() && r.html().length && r.html() === "Server" ) {

       ok = 1;
   }
   if ( !ok ) {
       msg.html( " missing required field" );
   }

   return ok;
}

ga.valid.checksubmit = function( module ) {
   var i,
       ok = 1;

   if ( !ga.altfile.bdata[ module ] ) {
      return 1;
   }

   for ( i in ga.altfile.bdata[ module ] ) {
      if ( ga.altfile.bdata[ module ][ i ].req  ) {
          switch ( ga.altfile.bdata[ module ][ i ].req ) {
              case "lrfile" : ok = ok && ga.valid.checkLrfile( "#" + i ); break;
              case "rpath"  : ok = ok && ga.valid.checkRpath ( "#" + i ); break;
              case "rfile"  : ok = ok && ga.valid.checkRfile ( "#" + i ); break;
              default       : console.log( "ga.valid.checksubmit() unsupported required check " +  ga.altfile.bdata[ module ][ i ].req ); break;
          }
      }
   }
   return ok;
}
          
   
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.value = {};
ga.value.settings = {};

ga.value.setLastValue = function( pkg, tag, defval ) {
    var tl = pkg + ":" + tag + ":last_value";
    var dv = pkg + ":" + tag + ":default_value";
    var t = $( tag );
    var p2d;
    if ( !/_output$/.test( pkg ) ) {
        return false;
    }

    if ( $( "#global_data" ).data( tl ) == undefined ) {
        switch( t.attr( "type" ) )
        {
            case "checkbox" :
            case "radio" :
                $( "#global_data" ).data( tl, t.is( ":checked") );
                $( "#global_data" ).data( dv, t.is( ":checked") ); break;
            case "div" : 
            case "msgs" : 
                $( "#global_data" ).data( tl, t.html() ); 
                $( "#global_data" ).data( dv, t.html() );
                break;
            case "plot2d" :


                           break;
            case "filelink" :
            case "filelinkm" :
                $( "#global_data" ).data( tl, $( tag + "_filelink" ).html() );

                break;

            default : 
                      if ( defval )
                      {

                         t.val( defval );
                      }                         

                      $( "#global_data" ).data( tl, t.val() );
                      $( "#global_data" ).data( dv, t.val() );
                      break;
        }
    } else {
        switch( t.attr( "type" ) )
        {
            case "checkbox": 
            case "radio": 
                   t.prop( "checked", $( "#global_data" ).data( tl ) ); break;
            case "div" : 
            case "msgs" : t.html( $( "#global_data" ).data( tl ) ); break;
            case "atomicstructure" : 
                  var stag = tag.replace( /^#/, "" );

                  if ( $( "#global_data" ).data( tl ) ) {

                      _jmol_info[ stag ].script = $( "#global_data" ).data( tl );

                      t.html(Jmol.getAppletHtml( "jmolApplet" + stag,  _jmol_info[ stag ] ) );

                  } else {

                      t.html("");
                  }
                  break;

            case "plot2d" : 


                     p2d = gd.data( tl );
                     if ( p2d.data ) {
                         ga.value.set.plot2d( tag, p2d.options );
                         t.plot( p2d.data, ga.value.get.plot2d.plot_options( tag, p2d.options ) );
                     } else {
                         t.plot( p2d, ga.value.get.plot2d.plot_options( tag ) );
                     }
                     break;
            case "filelink" : 
            case "filelinkm" : 
                     $( tag + "_filelink" ).html( $( "#global_data" ).data( tl ) );
                     break;
            default: 

            
            t.val( $( "#global_data" ).data( tl ) );
            break;
        }
    }
}

ga.value.saveLastValue = function( pkg, tag ) {
   var t = $( tag );

   switch( t.attr( "type" ) )
   {
       case "file" :  return; break;
       case "checkbox" :
       case "radio" :
                     $( "#global_data" ).data( pkg + ":" + tag + ":last_value", t.is( ":checked") ); break;
       case "div" :
       case "msgs" : $( "#global_data" ).data( pkg + ":" + tag + ":last_value", t.html() ); break;
       case "plot2d" : 

                       break;
       case "filelink" : 
       case "filelinkm" : 
                     $( "#global_data" ).data( pkg + ":" + tag + ":last_value", $( tag + "_filelink" ).html() ); 
                     break;
       case "atomicstructure" : 
                     var stag = tag.replace( /^#/, "" );

                     if ( _jmol_info && _jmol_info[ stag ] && _jmol_info[ stag ].length ) {

                         $( "#global_data" ).data( pkg + ":" + tag + ":last_value", _jmol_info[ stag ].script ); 
                     } else {

                         $( "#global_data" ).data( pkg + ":" + tag + ":last_value", "" ); 
                     }
                     break;
       default: $( "#global_data" ).data( pkg + ":" + tag + ":last_value", t.val() ); break;
   }


}

ga.value.saveLastValues = function( pkg ) {

   $( "#" + pkg + " :input" ).each(function() {

      ga.value.saveLastValue( pkg, "#" + $( this ).attr( "id" ) );
   });
}

ga.value.resetDefaultValue = function( pkg, tag ) {

   var t = $( tag );


   if(  t.prop( "tagName" ) == 'SELECT' ) {
    t.val( $( "#global_data" ).data( pkg + ":" + tag + ":default_value" ) );
   } else {
      switch( t.attr( "type" ) )
      {
          case "file" :  return; break;
          case "checkbox" : 
                        $( "#global_data" ).removeData( pkg + ":" + tag + ":repeat:count" );

          case "radio" : 
                        t.prop( "checked", $( "#global_data" ).data( pkg + ":" + tag + ":default_value" ) ); break;
          case "div" :
          case "msgs" : t.html( $( "#global_data" ).data( pkg + ":" + tag + ":default_value" ) ); 
                        break;
          case "filelink" :
          case "filelinkm" :
                        $( tag + "_filelink" ).html( " " );
                        break;
          case "plot2d" : 

                        $( "#global_data" ).data( pkg + ":" + tag + ":last_value", [[]] );
                        ga.value.clear.plot2d( tag );
                        t.plot( [[]], ga.value.get.plot2d.plot_options( tag ) ); break;
                        break;
          case "atomicstructure" : 
                        var stag = tag.replace( /^#/, "" );

                        $( "#global_data" ).data( pkg + ":" + tag + ":last_value", "" );
                        $( tag ).html("");
                        break;
          default: t.val( t.attr( "value" ) ); break;
      }
   }
   ga.value.saveLastValue( pkg, tag );
   $( tag + "_msg" ).html("");
}

ga.value.resetDefaultValues = function( pkg, msgs ) {

    var i,
    hmod_textarea;
    if ( !/_output$/.test( pkg ) ) {
        return false;
    }

    $( "#" + pkg + " :input" ).each(function() {
        ga.value.resetDefaultValue( pkg, "#" + $( this ).attr( "id" ) );
    });
    ga.sync.reset( pkg );
    for ( i in ga.value.extra_resets.data ) 
    {
        
        ga.value.resetDefaultValue( pkg, "#" + i );
    }
    if ( msgs ) {
        ga.value.resetDefaultValue( pkg, "#" + pkg + "_msgs" );
        hmod_textarea = "#" + pkg + "_textarea";
        ga.value.resetDefaultValue( pkg, hmod_textarea );
        $( hmod_textarea ).hide();
        $( hmod_textarea + "_label" ).hide();
    }
}

ga.value.extra_resets = function( id ) {

    ga.value.extra_resets.data = ga.value.extra_resets.data || {};
    ga.value.extra_resets.data[ id ] = 1;
}

ga.value.extra_resets.clear = function() {

    ga.value.extra_resets.data = {};
}
    

ga.value.setLastValueOutput = function( mod ) {

    var hmod            = "#" + mod,
        hmod_textarea   = hmod + "_textarea",
        jqhmod_textarea = $( hmod_textarea );

    ga.value.setLastValue( mod, hmod + "_msgs" );
    ga.value.setLastValue( mod, hmod_textarea );
    if ( jqhmod_textarea.val() ) {

        jqhmod_textarea.show();
        $( hmod_textarea + "_label" ).show(); 
        jqhmod_textarea.height( parseFloat( jqhmod_textarea.prop( 'scrollHeight' ) ) + 
                                parseFloat( jqhmod_textarea.css ( 'borderTopWidth' ) ) + 
                                parseFloat( jqhmod_textarea.css ( 'borderBottomWidth' ) ) );
    } else {

        jqhmod_textarea.hide();
        $( hmod_textarea + "_label" ).hide();
    }
}
    
ga.value.get = {};
ga.value.set = {};
ga.value.clear = {};

ga.value.set.plot2d = function( tag, options ) {

    var tagtitle  = tag + "_title",
        tagxlabel = tag + "_xlabel",
        tagylabel = tag + "_ylabel";





    $( tagtitle  ).html( options.title  ? options.title  : "");
    $( tagxlabel ).html( options.xlabel ? options.xlabel : "");
    $( tagylabel ).html( options.ylabel ? options.ylabel : "");
}

ga.value.clear.plot2d = function( tag ) {

    var tagtitle  = tag + "_title",
        tagxlabel = tag + "_xlabel",
        tagylabel = tag + "_ylabel";
        tagxy     = tag + "_xy";

    $( tagtitle  ).html("");
    $( tagxlabel ).html("");
    $( tagylabel ).html("");
    $( tagxy     ).html("");
}


ga.value.set.plot2d.pan = function( tag, value ) {

    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].pan = value ? true : false;
}

ga.value.set.plot2d.zoom = function( tag, value, pkg ) {

    var tagtitle  = tag + "_title",
        tagxlabel = tag + "_xlabel",
        tagylabel = tag + "_ylabel";
        tagxy     = tag + "_xy";

    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].zoom = value ? true : false;
    if ( value ) {
       ga.value.settings[ tag ].pkg = pkg;

       $( tag + "_title," + tag + "_xlabel," + tag + "_ylabel," + tag + "_xy" )
            .on("click", ga.value.set.plot2d.zoom.click );
    }
}

ga.value.set.plot2d.pkg = function( pkg, tag ) {


    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].pkg = pkg;
    $( tag + "_title," + tag + "_xlabel," + tag + "_ylabel," + tag + "_xy" )
        .on("click", ga.value.set.plot2d.reset );
}

ga.value.set.plot2d.reset = function( event ) {
    var id = "#" + event.target.id.replace( /(_title|_xlabel|_ylabel|_xy)$/, "" );
    event.preventDefault();

    ga.value.setLastValue( ga.value.settings[ id ].pkg, id );
}

ga.value.set.plot2d.hover = function( tag, value ) {

    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].hover = value ? true : false;
}

ga.value.get.plot2d = {};
ga.value.get.plot2d.plot_options = function( tag, options ) {


    var plot_options = ga.plot_options();

    plot_options.pan.interactive  = ga.value.settings[ tag ].pan   ? true : false;
    plot_options.zoom.interactive = ga.value.settings[ tag ].zoom  ? true : false;
    plot_options.grid.hoverable   = ga.value.settings[ tag ].hover ? true : false;

    if ( options ) {
        if ( options.legend ) {
            plot_options.legend           = options.legend;
            
            if ( options.legend.container ) {
                plot_options.legend.container = $( tag + "_legend" );
            }
        }
        if ( options.xmin ) {
            plot_options.xaxis.min        = options.xmin;
        }
        if ( options.xmax ) {
            plot_options.xaxis.max        = options.xmax;
        }
        if ( options.xscale ) {
            switch ( options.xscale ) {
                case "log" :
                plot_options.xaxis.transform        = function(v) { return v > 0 ? Math.log( v ) : 1e-99; };
                plot_options.xaxis.inverseTransform = function(v) { return Math.exp( v ); };
                plot_options.xaxis.tickFormatter    = ga.value.plot2d.ticformatter;
                break;
                default : 
                console.log( "ga.value.get.plot2d.plot_options( " + tag + " , options ) has unsupported xscale of " + options.xscale );
                break;
            }
        }
        if ( options.xtics ) {
            plot_options.xaxis.ticks = options.xtics;
        }
        if ( options.ymin ) {
            plot_options.yaxis.min        = options.ymin;
            
        }
        if ( options.ymax ) {
            plot_options.yaxis.max        = options.ymax;
            
        }
        if ( options.yscale ) {
            switch ( options.yscale ) {
                case "log" :
                plot_options.yaxis.transform        = function(v) { return v > 0 ? Math.log( v ) : 1e-99; };
                plot_options.yaxis.inverseTransform = function(v) { return Math.exp( v ); };
                plot_options.yaxis.tickFormatter    = ga.value.plot2d.ticformatter;
                break;
                default : 
                console.log( "ga.value.get.plot2d.plot_options( " + tag + " , options ) has unsupported yscale of " + options.yscale );
                break;
            }
        }
        if ( options.ytics ) {
            plot_options.yaxis.ticks = options.ytics;
        }
    }

    return plot_options;
}
        
ga.value.plot2d = {};
ga.value.plot2d.toFP = function( val, dec ) {
    if ( dec > 0 ) {

        return val.toFixed( dec );
    }
    if ( val.toString().length > 6 ) {

        return val.toExponential( 3 ).replace( /0+e/, 'e' ).replace( /\.e/, 'e' );
    }

    return val.toFixed( 0 );
}

ga.value.plot2d.ticformatter = function formatter(val, axis) {
    var tval;
    if ( !axis._ehb || val <= axis.min ) {

        axis._ehb       = {};
        axis._ehb.pv    = val;
        axis._ehb.min   = Math.min( axis.min, axis.max );
        axis._ehb.max   = Math.max( axis.min, axis.max );
        axis._ehb.tmin  = axis.options.transform( axis._ehb.min );
        axis._ehb.tmax  = axis.options.transform( axis._ehb.max );
        axis._ehb.tmaxr = 1 / axis._ehb.tmax;
        axis._ehb.rnge  = axis._ehb.max - axis._ehb.min;
        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }

//    if ( val >= axis.max ) {
//        return ga.value.plot2d.toFP( val, axis.tickDecimals );
//    }

    if ( !axis._ehb.snd ) {
        axis._ehb.snd = true;
        axis._ehb.sndv = val;
        axis._ehb.ptd = ( axis.options.transform( val ) - axis._ehb.tmin ) * axis._ehb.tmaxr;

        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }

    if ( !axis._ehb.tr ) {
        axis._ehb.tr  = 2 * Math.abs( (val - axis._ehb.sndv ) ) / axis._ehb.rnge;
        axis._ehb.ptd = Math.abs( axis.options.transform( val ) - axis._ehb.tmin ) * axis._ehb.tmaxr;

        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }

    tval = ( axis.options.transform( val ) - axis._ehb.tmin ) * axis._ehb.tmaxr;



    if ( Math.min( Math.abs( tval - axis._ehb.ptd ), 1 - tval ) >= axis._ehb.tr )
    {

        axis._ehb.ptd = tval;
        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }



    return "";
};
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.pull = {};

ga.pull.clearPull = function( repeater ) {
   if ( !repeater || typeof( repeater ) != "string" || repeater.length == 0 || repeater == "#__fields:repeat__" )
   {
      repeater = "";
   }

   $( "#global_data" ).data( "_pull_json"   + repeater, {} );
   $( "#global_data" ).data( "_pull_update" + repeater, {} );
   $( "#global_data" ).data( "_pull_type"   + repeater, {} );
}

ga.pull.toPull = function( pkg, tag, type, pulltag, repeater ) {

   if ( !repeater || typeof( repeater ) != "string" || repeater.length == 0 || repeater == "#__fields:repeat__" )
   {
      repeater = "";
   }

   var gd = $( "#global_data" );
   var tj = gd.data( "_pull_json"   + repeater );
   var tu = gd.data( "_pull_update" + repeater );
// for now, just set to 0
   tj[ pulltag ] = 0;
   if ( typeof( tu[ pulltag ] ) != "object" )
   {
      tu[ pulltag ] = {};
   }


   tu[ pulltag ][ tag ] = type;

   gd.data( "_pull_json"   + repeater, tj );
   gd.data( "_pull_update" + repeater, tu );


}

ga.pull.doPull = function( repeater ) {
   if ( !repeater || typeof( repeater ) != "string" || repeater.length == 0 || repeater == "#__fields:repeat__" )
   {
      repeater = "";
   }

   var gd = $( "#global_data" );
   var s = $( '#_state' );
   var l = s.data( '_logon' );
   if ( l && l.length )
   {
      var tj = gd.data( "_pull_json" + repeater );
      tj[ "_window" ] = window.name;
      tj[ '_logon' ] = l;

      if ( Object.size( tj ) > 2 )
      {

         $.getJSON( "ajax/sys_config/sys_pull.php", tj )
         .done( function( data, status, xhr ) {


            var tu = gd.data( "_pull_update" + repeater );
            $.each(data, function(k, v) {


               if ( typeof( tu[ k ] ) == "object" )
               {
                  $.each( tu[ k ], function( k2, v2 ) {

                     var t = $( k2 );
                     switch( v2 )
                     {
                        case "checkbox" : 
                         t.prop( "checked", v == "on" ); break;
                        case "email" : 
                        case "text" : 
                        case "integer" : 
                        case "float" : 
                         t.val( v ); break;
                        case "listbox" : 
                         t.empty();
// setup html for results


                         $.each( v, function( k3, v3 ) {

                           t.append($("<option></option>").attr( "value", v3 ).text( v3 ) );
                         });
                         break;
                        case "label" : 

                         t.html( v );
                         break;
                        default : 
                         console.log( "ga.pull.doPull(): not yet" );
                     }
                  });
               }
            });
         })
         .fail( function( xhr, status, errorThrown ) {

         });
      } else {

      }
   }
}
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.repeats = {};

ga.repeats.cache            = {};
ga.repeats.cache._jmol_info = {};

ga.repeats.save = function() {
    
    ga.repeats.cache._jmol_info = _jmol_info || {};
};

ga.repeats.restore = function() {
    
    _jmol_info = ga.repeats.cache._jmol_info;
};
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.repeat               = {};
ga.repeat.data          = {};

// register a repeat
// equivalent of ga.repeats.registerRepeat
// initializes the repeat structure & stores the html and eval for a field and returns a placeholder

ga.repeat.repeat = function( mod, id, html, this_eval  ) {
    

    ga.repeat.data[ mod ] = ga.repeat.data[ mod ] || {};
    ga.repeat.data[ mod ].repeat = ga.repeat.data[ mod ].repeat || {};
    ga.repeat.data[ mod ].repeat[ id ] = {};
    ga.repeat.data[ mod ].repeat[ id ].html = html;
    ga.repeat.data[ mod ].repeat[ id ].eval = this_eval;

    // fix up html & eval for easy unconfused replacement

    ga.repeat.data[ mod ].repeat[ id ].htmlr = 
        html
        .replace( /<\/label>/, "%%label%%</label>" )
        .replace( RegExp( 'id="' + id + '"' ), 'id="%%id%%"' )
        .replace( RegExp( 'name="' + id ), 'name="%%id%%' )
        .replace( RegExp( 'for="' + id + '"' ), 'for="%%id%%"' )
        .replace( RegExp( 'id="' + id + '_msg"' ), 'id="%%id%%_msg"' )
        .replace( RegExp( 'id="' + id + '_button"' ), 'id="%%id%%_button"' )
        .replace( RegExp( '="' + id + '_altval"', 'g' ), '="%%id%%_altval"' )
        .replace( RegExp( 'name="_selaltval_' + id + '"' ), 'name="_selaltval_%%id%%"' )
    ;    

    ga.repeat.data[ mod ].repeat[ id ].evalr = 
        this_eval 
        .replace( RegExp( '"#' + id + '"', "g" ), '"#%%id%%"' )
        .replace( RegExp( ':' + id + ':', "g" ), ':%%id%%:' )
        .replace( RegExp( '"#' + id + '_msg"', "g" ), '"#%%id%%_msg"' )
        .replace( RegExp( '"' + id + '"', "g" ), '"%%id%%"' )
        .replace( RegExp( '"#' + id + '_button"', "g" ), '"#%%id%%_button"' )
        .replace( RegExp( '"' + id + '_altval"', "g" ), '"%%id%%_altval"' )
        .replace( RegExp( '"#' + id + '_altval"', "g" ), '"#%%id%%_altval"' )
    ;

    
    return '<tr><td></td><td><span id="' + id + '-span"></span></td></tr>';
}

// add a repeat repeater reference
// equivalent of ga.repeats.addRepeat 
// the repeat should already exist

ga.repeat.repeatOn = function( mod, id, refid  ) {
    
    var rxcolon = /^(.*):(.*)$/,
        rxcolonval = rxcolon.exec( refid ),
        refbase,
        refchoice
    ;

    ga.repeat.data[ mod ].repeater = ga.repeat.data[ mod ].repeater || {};
    ga.repeat.data[ mod ].repeater[ refid ] = ga.repeat.data[ mod ].repeater[ refid ] || {};
    ga.repeat.data[ mod ].repeater[ refid ].child = ga.repeat.data[ mod ].repeater[ refid ].child || [];
    ga.repeat.data[ mod ].repeater[ refid ].child.push( id );
    ga.repeat.data[ mod ].repeat[ id ].refid = refid;

    if ( rxcolonval ) {
        refbase   = rxcolonval[ 1 ];
        refchoice = rxcolonval[ 2 ];
        

        ga.repeat.data[ mod ].repeater[ refbase ] = ga.repeat.data[ mod ].repeater[ refbase ] || {};
        ga.repeat.data[ mod ].repeater[ refbase ].child = ga.repeat.data[ mod ].repeater[ refbase ].child || [];
        ga.repeat.data[ mod ].repeater[ refbase ].choice = ga.repeat.data[ mod ].repeater[ refbase ].choice || [];
        ga.repeat.data[ mod ].repeater[ refbase ].child.push( id );
        ga.repeat.data[ mod ].repeater[ refbase ].choice.push( refchoice );
    }
        
}

// add a repeater
// no exact equivalent in ga.repeats, this was encapsulated in various updateRepeats

ga.repeat.repeater = function( mod, id, type ) {
    
    ga.repeat.data[ mod ] = ga.repeat.data[ mod ] || {};
    ga.repeat.data[ mod ].repeater = ga.repeat.data[ mod ].repeater || {};
    ga.repeat.data[ mod ].repeater[ id ] = ga.repeat.data[ mod ].repeater[ id ] || {};
    ga.repeat.data[ mod ].repeater[ id ].type = type;
}

// return all children

ga.repeat.children = function( mod, id, result ) {
    var i;

    

    result = result || {};

    if ( !ga.repeat.data[ mod ] || 
         !ga.repeat.data[ mod ].repeater || 
         !ga.repeat.data[ mod ].repeater[ id ] ) {
        
        return result;
    }

    if ( !ga.repeat.data[ mod ].repeater[ id ].child ) {
        
        return result;
    }

    for ( i in ga.repeat.data[ mod ].repeater[ id ].child ) {
        result[ ga.repeat.data[ mod ].repeater[ id ].child[ i ] ] = true;
        if ( ga.repeat.data[ mod ].repeater[ i ] ) {
            result = ga.repeat.children( mod, i, result );
        }
    }
    return result;
}

// change
// quasi equivalent in ga.repeats in updateRepeats{,Cb,Lb}

ga.repeat.change = function( mod, id, init ) {
    var val,
    hid = "#" + id,
    jqhid = $( hid ),
    children,
    add_html = "",
    add_eval = "",
    i,
    j;

    
    if ( !ga.repeat.data[ mod ] || 
         !ga.repeat.data[ mod ].repeater || 
         !ga.repeat.data[ mod ].repeater[ id ] ) {
        
        return false;
    }

    if ( !jqhid.length ) {
        
        return false;
    }

    // get value of repeater
    switch ( ga.repeat.data[ mod ].repeater[ id ].type ) {
    case "checkbox" : 
        val = jqhid.prop( "checked" ) ? 1 : 0;
        break;
        
    case "integer" :
    case "listbox" :
        val = jqhid.val();
        break;

    default :
        console.warn( "ga.repeat.change( " + mod + " , " + id + " ) type " + ga.repeat.data[ mod ].repeater[ id ].type + " not supported" );
        return false;
        break;
    }

    // has the value changed ?

    if ( !init && ga.repeat.data[ mod ].repeater[ id ].value === val ) {
        
        return false;
    }
    
    

    // get children
    children = ga.repeat.children( mod, id );

    

    // build up add_html & add_eval

    switch ( ga.repeat.data[ mod ].repeater[ id ].type ) {
    case "checkbox" : 
        if ( val ) {
            for ( i in children ) {
                add_html += ga.repeat.data[ mod ].repeat[ i ].html;
                add_eval += ga.repeat.data[ mod ].repeat[ i ].eval;
                if ( ga.repeat.data[ mod ].repeater[ i ] &&
                     ga.repeat.data[ mod ].repeater[ i ].value ) {
                    delete ga.repeat.data[ mod ].repeater[ i ].value;
                }
            }
        }
        break;

    case "integer" :

        for ( j = 1; j <= val; ++j ) {
            for ( i in children ) {
                
                
                add_html += ga.repeat.data[ mod ].repeat[ i ].htmlr.replace( /%%id%%/g, i + "-" + j ).replace( "%%label%%", "[" + j + "]" );
                add_eval += ga.repeat.data[ mod ].repeat[ i ].evalr.replace( /%%id%%/g, i + "-" + j );
                if ( ga.repeat.data[ mod ].repeater[ i ] &&
                     ga.repeat.data[ mod ].repeater[ i ].value ) {
                    delete ga.repeat.data[ mod ].repeater[ i ].value;
                }
            }
        }
        break;

    case "listbox" :
        
        j = id + ":" + val;

        children = ga.repeat.children( mod, j );
        

        for ( i in children ) {
            add_html += ga.repeat.data[ mod ].repeat[ i ].html;
            add_eval += ga.repeat.data[ mod ].repeat[ i ].eval;
            if ( ga.repeat.data[ mod ].repeater[ i ] &&
                 ga.repeat.data[ mod ].repeater[ i ].value ) {
                delete ga.repeat.data[ mod ].repeater[ i ].value;
            }
        }
        break;

    default :
        console.warn( "ga.repeat.change( " + mod + " , " + id + " ) type " + ga.repeat.data[ mod ].repeater[ id ].type + " not supported" );
        return false;
        break;
    }

    
    
    

    $( hid + "-repeater" ).html( add_html );
    eval( add_eval );

    ga.repeat.data[ mod ].repeater[ id ].value = val;
    resetHoverHelp();
}
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.hide = function( module, id ) {

    ga.hide.data = ga.hide.data || {};
    ga.hide.data[ module ] = ga.hide.data[ module ] || {};
    ga.hide.data[ module ][ id ] = ga.hide.data[ module ][ id ] || {};
    ga.hide.data[ module ][ id ].active = 1;


};

ga.hide.data = {};

ga.hide.update = function( module, id ) {

    var i;

    if ( !ga.hide.data[ module ] || !ga.hide.data[ module ][ id ] ) {
        console.log( "ga.hide.update( " + module + " , " + id + " ) error, hider has not been defined" );
        return;
    }

    if ( !ga.hide.data[ module ][ id ].hides ) {
        console.log( "ga.hide.update( " + module + " , " + id + " ) error, no hides attached to this hider" );
        return;
    }

    if ( $( id ).prop( 'checked' ) ) {
        for ( i in ga.hide.data[ module ][ id ].hides ) {

            $( i + "-itd" ).html(" ");
            $( i ).hide();
        } 
    } else {
        for ( i in ga.hide.data[ module ][ id ].hides ) {

            $( i + "-itd" ).html( ga.hide.data[ module ][ id ].hides[ i ] );
            $( i ).show();
        } 
    }
// fix up help
    setHoverHelp();

}

ga.hide.add = function( module, id, hiderid ) {

    ga.hide.data = ga.hide.data || {};
    ga.hide.data[ module ] = ga.hide.data[ module ] || {};
    ga.hide.data[ module ][ hiderid ] = ga.hide.data[ module ][ hiderid ] || {};
    ga.hide.data[ module ][ hiderid ].hides = ga.hide.data[ module ][ hiderid ].hides || {};
    ga.hide.data[ module ][ hiderid ].hides[ id ] = $( id + "-itd" ).html();


};


    

/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.data = {};
ga.data.nofcrefresh = {};

// apply the data to the screen output, return an object with job_status

ga.data.update = function( mod, data, msging_f, msg_id ) {
    var output_msgs_cleared = 0,
        appended            = 0,
        state_changed       = 0,
        do_close            = 0,
        do_close2           = 0,
        mod_out             = mod + "_output",
        hmod_out            = "#" + mod_out,
        jqmod_out           = $( hmod_out ),
        retobj              = {},
        hmod_out_msgs       = hmod_out + "_" + "msgs",
        jqhmod_out_msgs     = $( hmod_out_msgs ),
        htag,
        savekey,
        tlink,
        match;


 

    if ( msging_f ) {

        $( "#" + mod + "_progress" ).html( "" );
        jqhmod_out_msgs.text( "" );
    }

    $.each(data, function(k, v) {

        match = jqmod_out.find( "#" + k );
        if ( match.length )
        {
            if ( !output_msgs_cleared )
            {
                jqhmod_out_msgs.text( "" );
                output_msgs_cleared = 1;
            }
            switch ( match.attr( "type" ) )
            {
            case "plot2d" : 

                htag = "#" + k;
                if ( v.data ) {
                    ga.value.set.plot2d( htag, v.options );
                    $.plot( match, v.data, ga.value.get.plot2d.plot_options( htag, v.options ) );
                } else {
                    $.plot( match, v,  ga.value.get.plot2d.plot_options( htag ) );
                }
                    
                savekey = mod_out + ":#" + k + ":last_value";
                $( "#global_data" ).data( savekey , v ); 
                break;
            case "atomicstructure" : 
                //                               Jmol.setDocument( 0 );
                savekey = mod_out + ":#" + k + ":last_value";
                _jmol_info[ k ].script =
                    'set background [' + ga.colors.background + ']; set zoomlarge false;set echo top center;echo loading ' + v.split( '/' ).pop() + ';refresh;load "' + v + '";';
                //                               Jmol.getApplet("jmol", _jmol_info[ k ]);

                $( "#global_data" ).data( savekey , _jmol_info[ k ].script ); 
                $("#" + k ).html(Jmol.getAppletHtml( "jmolApplet" + k, _jmol_info[ k ] ));

                break;
            case "checkbox" : 
            case "radio" : 
                match.prop( "checked", true ); 
                break;
            case "div" :  
                match.html( v );
                break;
            case "filelink" : 
                tlink = "<a href=\"" + v + "\" target=\"_blank\">" + v.split( '/' ).pop() + "</a>";
                savekey = mod_out + ":#" + k + ":last_value";
                $( "#global_data" ).data( savekey , tlink );
                $( "#" + k + "_filelink" ).html( tlink );
                break;
            case "filelinkm" : 
                savekey = mod_out + ":#" + k + ":last_value";
                tlink = "";
                $.each( v, function( k2, v2 ) {
                    tlink += "<a href=\"" + v2 + "\" target=\"_blank\">" + v2.split( '/' ).pop() + "</a> ";
                } );
                $( "#global_data" ).data( savekey , tlink );
                $( "#" + k + "_filelink" ).html( tlink );
                break;
            default :
                if ( $( "#global_data" ).data( "_append:" + mod_out + "_" + k ) )
                {
                    match.val( match.val() + "\n" + v );
                    match.height( parseFloat( match.prop( 'scrollHeight' ) + parseFloat( match.css("borderTopWidth") ) + parseFloat( match.css("borderBottomWidth") ) ) );
                } else {
                    match.val( v );
                }
                break;
            }
        } else {
            if ( msging_f ) {
                if ( k.charAt( 0 ) == "_" ) {
                    if ( !/^_fs_/.test( k ) || !ga.data.nofcrefresh[ mod ] ) {
                        if ( k == "_message" )
                        { 
                            messagebox( v );
                        }
                        if ( /^_getinput/.test( k ) )
                        { 
                            
                            if ( k == "_getinputerror" ) {
                                
                            } 
                            if ( k == "_getinput" ) {
                                ga.valuen.input( mod, v );
                            }
                        }
                        if ( k == "_textarea" )
                        { 
                            
                            ga.data.textarea( hmod_out, v );
                        }
                        if ( k == "_status" )
                        { 
                            
                            if ( v == "complete" ) {
                                msging_f( msg_id, 0, 0 );
                            }
                        }
                    }
                } else {
                    if ( !appended )
                    {
                        jqhmod_out_msgs.append( "<p>Unexpected results:</p>" );
                        appended = 1;
                    }
                    jqhmod_out_msgs.append( "<p>" + k + " => " + v + "</p>" );
                }
            } else {
                if ( k.charAt( 0 ) == "_" ) {
                    if ( !/^_fs_/.test( k ) || !ga.data.nofcrefresh[ mod ] ) {
                        $( "#_state" ).data( k, v );
                        state_changed = 1;
                        if ( k == "_status" )
                        { 
                            
                            retobj.job_status = v;
                        }
                        if ( /^_getinput/.test( k ) )
                        { 
                            
                            if ( k == "_getinputerror" ) {
                                
                            }
                            if ( k == "_getinput" ) {
                                ga.valuen.input( mod, v );
                            }
                        }
                        if ( k == "_textarea" )
                        { 
                            
                            ga.data.textarea( hmod_out, v );
                        }
                    }
                } else {
                    if ( k == "-close" )
                    {
                        do_close = 1;
                    } else {
                        if ( k == "-close2" )
                        {
                            do_close2 = 1;
                        } else {
                            if ( !appended )
                            {
                                jqhmod_out_msgs.text( "" );
                                jqhmod_out_msgs.append( "<p>Unexpected results:</p>" );
                                appended = 1;
                                output_msgs_cleared = 1;
                            }
                            jqhmod_out_msgs.append( "<p>" + k + " => " + v + "</p>" );
                        }
                    }
                }
            }
        }
    });
    ga.value.saveLastValues( mod_out );
    ga.value.saveLastValue( mod_out, hmod_out_msgs );
    $( hmod_out + '_progress' ).html( "" );
    if ( state_changed )
    {
        syncState();
    }
    if ( do_close )
    {
        closeModal();
    }
    if ( do_close2 )
    {
        closeModal2();
    }
    return retobj;
};

ga.data.textarea = function( hmod_out, v ) {
    var hmod_out_textarea   = hmod_out + "_textarea",
        jqhmod_out_textarea = $( hmod_out_textarea );


    if ( jqhmod_out_textarea.is( ":hidden" ) ) {

        jqhmod_out_textarea.show();
        $( hmod_out_textarea + "_label" ).show(); 
    }

    jqhmod_out_textarea.val( jqhmod_out_textarea.val() + v );
    if ( !ga.set( "textarea:rows" ) ) {
        jqhmod_out_textarea.height( parseFloat( jqhmod_out_textarea.prop( 'scrollHeight' ) ) + 
                                    parseFloat( jqhmod_out_textarea.css ( 'borderTopWidth' ) ) + 
                                    parseFloat( jqhmod_out_textarea.css ( 'borderBottomWidth' ) ) );
    }
};
    
/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

// create or join a sync group
ga.sync = function( pkg, mod, sync ) {
    
    var i,
    jqt = $( "#" + mod ),
    jqo;

    // does one already exist in DOM, if so, set our val to it
    if ( jqt &&
         ga.sync.data &&
         ga.sync.data[ pkg ] &&
         ga.sync.data[ pkg ][ sync ] ) {
        ga.sync.data[ pkg ][ sync ][ mod ] = true;
        for ( i in ga.sync.data[ pkg ][ sync ] ) {
            if ( i != mod ) {
                jqo = $( "#" + i );
                if ( jqo && $.isNumeric( jqo.val() ) ) {
                    
                    jqt.val( jqo.val() );
                    jqt.change();
                    return;
                }
            }
        }
        if ( ga.sync.data[ pkg ][ sync ]._lastval &&
             $.isNumeric( ga.sync.data[ pkg ][ sync ]._lastval ) )
        {
            
            jqt.val( ga.sync.data[ pkg ][ sync ]._lastval );
            jqt.change();
            return;
        }
        
        return;
    }        
    ga.sync.data = ga.sync.data || {};
    ga.sync.data[ pkg ] = ga.sync.data[ pkg ] || {};
    ga.sync.data[ pkg ][ sync ] = ga.sync.data[ pkg ][ sync ] || {};
    ga.sync.data[ pkg ][ sync ][ mod ] = true;
}

// when a value changes, also set the others in the sync group
ga.sync.change = function( pkg, mod, sync ) {
    var i,
    jqt = $( "#" + mod ),
    jqtv,
    jqo;
    

    if ( !( jqt &&
            $.isNumeric( jqt.val() ) &&
            ga.sync.data &&
            ga.sync.data[ pkg ] &&
            ga.sync.data[ pkg ][ sync ] ) ) {
        // nothing to do
        
        return;
    }
    
    ga.sync.data[ pkg ][ sync ]._lastval = jqt.val();
    for ( i in ga.sync.data[ pkg ][ sync ] ) {
        if ( i != mod ) {
            jqo = $( "#" + i );
            if ( jqo && jqo.val() != jqt.val() ) {
                
                jqo.val( jqt.val() );
                jqo.change();
            }
        }
    }
}
    
ga.sync.reset = function( pkg ) {
    var i;
    

    if ( !( ga.sync.data &&
            ga.sync.data[ pkg ] ) ) {
        
        return;
    }

    for ( i in ga.sync.data[ pkg ] ) {
        
        if ( ga.sync.data[ pkg ][ i ]._lastval ) {
            
            delete ga.sync.data[ pkg ][ i ]._lastval;
        }
        
    }
}

/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.valuen = {};
ga.valuen.data = {};
ga.valuen.html = {};
ga.valuen.dflt = {};
ga.valuen.dflt.data = {};
ga.valuen.dflt.html = {};
ga.valuen.lastload = "";

// restore data to form

ga.valuen.restore = function( form, data, html ) {
    var hform = "#" + form,
        jqhform = $( hform ),
        els = jqhform.find(':input').get(),
        repeaters = {},
        repeaters_added,
        i;

    data = data || ga.valuen.data[ form ];
    html = html || ga.valuen.html[ form ];

    

    if ( !data ) {
        // console.warn( "ga.valuen.restore( " + form + " ) no data" );
        return;
    }
    // if ( !html ) {
    // // console.warn( "ga.valuen.restore( " + form + " ) no html" );
    // return;
    //}

    $( hform + " .field_msg" ).html("");

    // add repeaters repeatedly until no more unassigned repeaters exist
    
    do {
        repeaters_added = false;
        $.each(els, function() {
            var i,
            names,
            $this = $( this ),
            val,
            found
            ;

            if ( $this.attr( "data-repeater" ) &&
                 !repeaters[ this.name ] ) {
                
                repeaters[ this.name ] = true;

                if ( this.name && 
                     ( data[ this.name ] ||
                       /checkbox|radio/i.test( this.type ) )
                   ) {
                    names = data[ this.name ];
                    if( /checkbox|radio/i.test( this.type ) ) { 
                        val = $this.val();
                        found = false;
                        if ( names ) {
                            for( i = 0; i < names.length; i++ ) {
                                if( names[ i ] == val ) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        $this.prop( "checked", found );
                        
                    } else {
                        $this.val( names[ 0 ] );
                        
                    }
                    // probably need to update repeaters at this point
                    repeaters_added = true;
                    ga.repeat.change( form, this.name, true );
                    
                    els = jqhform.find(':input').get();
                    return false;  // "break" equivalent for jquery's $.each
                } else {
                    if ( !data[ this.name ] && 
                         !/checkbox|radio/i.test( this.type ) ) {
                        console.warn( "ga.valuen.restore() no data found for repeater setting value on " + this.name + " type " + this.type + " to " + names[ 0 ] );
                    }
                }
            }
        });
    } while ( repeaters_added );

    // everything else

    $.each(els, function() {
        var i,
            names,
            $this,
            val,
            found,
            typetype,
            typenames
            ;
        
        if ( this.name && 
             !repeaters[ this.name ] ) {
            $this = $( this );
            if ( ( data[ this.name ] ||
                   /checkbox|radio/i.test( this.type ) ) &&
                 !/button/i.test( this.nodeName )
               ) {
                names = data[ this.name ];
                if( /checkbox|radio/i.test( this.type ) ) { 
                    val = $this.val();
                    found = false;
                    if ( names ) {
                        for( i = 0; i < names.length; i++ ) {
                            if( names[ i ] == val ) {
                                found = true;
                                break;
                            }
                        }
                    }
                    $this.prop( "checked", found );
                    
                } else {
                    if ( this.type === "file" ) {
                        if ( names[ 0 ] ) {
                            $( "#" + this.id + "_msg" ).html( " " + names[ 0 ] + " please reload manually (programmatic setting of local files disallowed by browser security)" );
                        } else {
                            $this.val( "" );
                        }
                    } else {
                        $this.val( names[ 0 ] );
                    }
                    
                }
            } else {
                if ( /button/i.test( this.nodeName ) &&
                     ( typetype = $this.attr( "data-type" ) ) ) {
                    
                    typenames = ga.altfile.button.getnames( this.id, typetype );
                    if ( typenames ) {
                        for ( i = 0; i < typenames.length; ++i ) {
                            
                            if ( data[ typenames[ i ] ] ) {
                                
                                ga.altfile.button.addhtml( form, this.id, typetype, data[ typenames[ i ] ] );
                            }
                        }
                    }   
                }
            }    
        }
    });

    // set html

    for ( i in html ) {
        
        $( "#" + i ).html( html[ i ] );
    };
}

// restore data to form from dflts

ga.valuen.restore.dflt = function( form ) {
    return ga.valuen.restore( form, ga.valuen.dflt.data[ form ], ga.valuen.dflt.html[ form ] );
}

// save data from form and optionally store as dflt

ga.valuen.save = function( form, asdflt ) {
    var els = $( "#" + form ).find(':input').get();
        data = {},
        html = {};
    

    // ga.valuen.data[ form ] = {};
    // ga.valuen.html[ form ] = {};

    $.each( els, function() {
        var tjq = $( this ),
            namenotdisabled = this.name && !this.disabled,
            idadd = tjq.attr( "data-add" );

        
        if ( namenotdisabled ) {
            if ( this.checked
                 || /select|textarea/i.test( this.nodeName )
                 || /file|email|number|text|hidden|password/i.test( this.type )
               ) {
                if( data[ this.name ] == undefined ){
                    data[ this.name ] = [];
                }
                data[ this.name ].push( tjq.val() );
                
            }
            if ( idadd ) {
                
                if( html[ idadd ] == undefined ){
                    html[ idadd ] = [];
                }
                html[ idadd ].push( $( "#" + idadd ).html() );
            }                
        }
    });

    if ( asdflt ) {
        ga.valuen.dflt.data[ form ] = data;
        ga.valuen.dflt.html[ form ] = html;
    } else {
        ga.valuen.data[ form ] = data;
        ga.valuen.html[ form ] = html;
    }
}

// take input data and put on form

ga.valuen.input = function( form, data ) {
    
    var hform = "#" + form,
        jqhform = $( hform ),
        els = jqhform.find(':input').get(),
        repeaters = {},
        repeaters_added,
        i;

//    $.each( data, function(k, v) {
//        console.log( "ga.valuen.input() k " + k + " v " + v );
//    });

    

    if ( !data ) {
        console.warn( "ga.valuen.input( " + form + " ) no data" );
        return;
    }

    // add repeaters repeatedly until no more unassigned repeaters exist
    
    do {
        repeaters_added = false;
        $.each(els, function() {
            var i,
            names,
            $this = $( this ),
            val,
            found
            ;

            if ( $this.attr( "data-repeater" ) &&
                 !repeaters[ this.name ] ) {
                
                repeaters[ this.name ] = true;

                if ( this.name && 
                     ( data[ this.name ] ||
                       /checkbox|radio/i.test( this.type ) )
                   ) {
                    names = data[ this.name ];
                    if ( Object.prototype.toString.call(names) !== '[object Array]' ) {
                        names = [ names ];
                    }
                    if( /checkbox|radio/i.test( this.type ) ) { 
                        val = $this.val();
                        found = false;
                        if ( names ) {
                            for( i = 0; i < names.length; i++ ) {
                                if( names[ i ] == val ) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        $this.prop( "checked", found );
                        
                    } else {
                        $this.val( names[ 0 ] );
                        
                    }
                    // probably need to update repeaters at this point
                    repeaters_added = true;
                    ga.repeat.change( form, this.name, true );
                    
                    els = jqhform.find(':input').get();
                    return false;  // "break" equivalent for jquery's $.each
                } else {
                    if ( !data[ this.name ] && 
                         !/checkbox|radio/i.test( this.type ) ) {
                        console.warn( "ga.valuen.input() no data found for repeater setting value on " + this.name + " type " + this.type + " to " + names[ 0 ] );
                    }
                }
            }
        });
    } while ( repeaters_added );

    // everything else

    $.each(els, function() {
        var i,
            names,
            $this,
            val,
            found,
            typetype,
            typenames
            ;
        
        if ( this.name && 
             !repeaters[ this.name ] ) {
            $this = $( this );
            if ( ( data[ this.name ] ||
                   /checkbox|radio/i.test( this.type ) ) &&
                 !/button/i.test( this.nodeName )
               ) {
                names = data[ this.name ];
                if ( Object.prototype.toString.call(names) !== '[object Array]' ) {
                    names = [ names ];
                }
                if( /checkbox|radio/i.test( this.type ) ) { 
                    val = $this.val();
                    found = false;
                    if ( names ) {
                        for( i = 0; i < names.length; i++ ) {
                            if( names[ i ] == val ) {
                                found = true;
                                break;
                            }
                        }
                    }
                    $this.prop( "checked", found );
                    
                } else {
                    if ( this.type === "file" ) {
                        if ( names[ 0 ] ) {
                            $( "#" + this.id + "_msg" ).html( " " + names[ 0 ] + " please reload manually (programmatic setting of local files disallowed by browser security)" );
                        }
                    } else {
                        $this.val( names[ 0 ] );
                    }
                    
                }
            } else {
                if ( /button/i.test( this.nodeName ) &&
                     ( typetype = $this.attr( "data-type" ) ) ) {
                    
                    typenames      = ga.altfile.button.getnames     ( this.id, typetype );
                    typenamesinput = ga.altfile.button.getnamesinput( this.id, typetype );
                    if ( typenames ) {
                        for ( i = 0; i < typenames.length; ++i ) {
                            
                            if ( data[ typenamesinput[ i ] ] ) {
                                
                                ga.altfile.button.addhtml( form, this.id, typetype, data[ typenamesinput[ i ] ] );
                            }
                        }
                    }   
                }
            }    
        }
    });

    $.each( data, function(k, v) {
        var jqk;
        if ( /^_html_/.test( k ) ) {
            k = k.replace( /^_html_/, "" );
            if ( jqk = $( "#" + k ) ) {
                jqk.html( v );
            }
        }
//        if ( k == "_datetime" ) {
//            jqhform.prepend( "<span class='removeme'><p><i>Reattached from job submitted at " + v + " </i></p></span>" );
//        }
    });
}

ga.valuen.addhtml = function( form ) {
    var jqhform = $( "#" + form ),
        els = jqhform.find(':input').get(),
        add = "";

    

    $.each( els, function() {
        var tjq = $( this ),
            namenotdisabled = this.name && !this.disabled,
            idadd = tjq.attr( "data-add" );

        if ( namenotdisabled ) {
            if ( idadd ) {
                
                add += '<input type="hidden" name="_html_' + idadd + '" value="' +  $( "#" + idadd ).html() + '">';
            }                
        }
    });

    

    jqhform.append( add );
}

ga.valuen.reset = function() {
    ga.valuen.data = {};
    ga.valuen.html = {};
    ga.valuen.dflt = {};
    ga.valuen.dflt.data = {};
    ga.valuen.dflt.html = {};
    ga.valuen.lastload = "";
}    

/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.license = function( req ) {
    var checks = req.split( ',' ),
        needs = [],
        msg,
        button_info = [],
        i;

    ;

    if ( checks.length ) {
        msg = 
            "<p>Submitting to this module requires " + 
            ( checks.length > 1 ? "approved licenses" : "an approved license" ) 
            + " for <em>" + checks.join( "</em> and <em>" ) + "</em></p>";
    }

    for ( i in checks ) {
        button_info.push( { id : checks[ i ],
                            label : checks[ i ] + " Management",
                            data : checks[ i ],
                            cb : function( data ) { return ga.altfile.button.simplecall( "license", data ); } } );
        ;
        if ( ga.license.data[ checks[ i ] ] &&
             ga.license.data[ checks[ i ] ][ 'status' ] ) {
            switch ( ga.license.data[ checks[ i ] ][ 'status' ] ) {
            case "approved" :
                ;
                break;
            case "denied" :
                ;
                msg += "<p>Your license request for <em>" + checks[ i ] + "</em> has been <strong>denied</strong>.</p>";
                needs.push( checks[ i ] );
                break;
            case "pending" :
                ;
                msg += "<p>Your license request for <em>" + checks[ i ] + "</em> is pending approval.</p>";
                needs.push( checks[ i ] );
                break;
            default :
                console.warn( "ga.license() " + checks[ i ] + " unknown status " + ga.license.data[ checks[ i ] ][ 'status' ] );
                needs.push( checks[ i ] );
                break;
            }
        } else {
            needs.push( checks[ i ] );
        }
    }

    if ( needs.length ) {

        messagebox( {
            icon  : "warning.png",
            text  : msg,
            buttons : button_info
        });
        return false;
    } else {
        return true;
    }
}

ga.license.data = {};

// get licenses for user
ga.license.get = function() {
    ;

    ga.license.data = {};

    if ( ga.license.url ) {
        ;

        $.getJSON( 
            ga.license.url,
            {
                tagmode: "any"
                ,format: "json"
                ,_window : window.name
                ,_logon : $( "#_state" ).data( "_logon" )
            } )
            .done( function( data, status, xhr ) {
                ;
                if ( data[ 'license' ] ) {
                    ga.license.data = data[ 'license' ];
                }
                if ( data[ 'hasadmin' ] ) {
                    $( ga.admin.ids.join() ).show();
                } else {
                    $( ga.admin.ids.join() ).hide();
                }
                
            })
            .fail( function( xhr, status, errorThrown ) {
                ;
                console.warn( "could not get license data" );
            });
    } else {
        ;
    }
}
