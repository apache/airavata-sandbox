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
__~debug:values{   console.log( "ga.value.setLastValue() pkg:" + pkg + " tag:" + tag + " type:" + t.attr( "type" ) + " tagName:" + t.prop( "tagName" ) + " value:" + t.val() );}
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
__~debug:values{ console.log( "ga.value.setLastValue() on undefined plot2d not yet: " + tl ); }
__~debug:plottwod{ console.log( "ga.value.setLastValue() on undefined plot2d not yet: " + tl ); }
                           break;
            case "filelink" :
            case "filelinkm" :
                $( "#global_data" ).data( tl, $( tag + "_filelink" ).html() );
__~debug:values{                console.log( "ga.value.setLastValue() done filelink on setLastValue" );}
                break;

            default : 
                      if ( defval )
                      {
__~debug:values{                console.log( "ga.value.setLastValue() default value set: " + defval );}
                         t.val( defval );
                      }                         
__~debug:values{            console.log( "ga.value.setLastValue() default attrib val: " + t.val() );}
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
__~debug:values{                     console.log( "ga.value.setLastValue() atomic structure global tag " + stag + " data tag " + tl );}
                  if ( $( "#global_data" ).data( tl ) ) {
__~debug:values{        console.log( "ga.value.setLastValue() atomic structure global data found tag " + stag );}
                      _jmol_info[ stag ].script = $( "#global_data" ).data( tl );
__~debug:values{        console.log( "ga.value.setLastValue() atomic structure jmol script: " + _jmol_info[ stag ].script );}
                      t.html(Jmol.getAppletHtml( "jmolApplet" + stag,  _jmol_info[ stag ] ) );
__~debug:values{        console.log( "ga.value.setLastValue() atomic structure jmol getAppletHtml finished" );}
                  } else {
__~debug:values{        console.log( "ga.value.setLastValue() atomic structure global data NOT found for tag " + stag );}
                      t.html("");
                  }
                  break;

            case "plot2d" : 
__~debug:values{                     console.log( "ga.value.setLastValue() on plot2d trying" );}
__~debug:plottwod{                     console.log( "ga.value.setLastValue() plot2d" );}
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
__~debug:values{    console.log( "ga.value.setLastValue() default tl " + tl + "  data(tl) " + $( "#global_data" ).data( tl ) );}
            
            t.val( $( "#global_data" ).data( tl ) );
            break;
        }
    }
}

ga.value.saveLastValue = function( pkg, tag ) {
   var t = $( tag );
__~debug:values{   console.log( "ga.value.saveLastValue() pkg:" + pkg + " tag:" + tag + " type:" + t.attr( "type" ) + " value:" + t.val() );}
   switch( t.attr( "type" ) )
   {
       case "file" : __~debug:values{ console.log( "ga.value.saveLastValue() file set is insecure, skipped" );} return; break;
       case "checkbox" :
       case "radio" :
                     $( "#global_data" ).data( pkg + ":" + tag + ":last_value", t.is( ":checked") ); break;
       case "div" :
       case "msgs" : $( "#global_data" ).data( pkg + ":" + tag + ":last_value", t.html() ); break;
       case "plot2d" : 
__~debug:values{ console.log( "ga.value.saveLastValue() on plot2d not yet" );  }
                       break;
       case "filelink" : 
       case "filelinkm" : 
                     $( "#global_data" ).data( pkg + ":" + tag + ":last_value", $( tag + "_filelink" ).html() ); 
                     break;
       case "atomicstructure" : 
                     var stag = tag.replace( /^#/, "" );
__~debug:values{ console.log( "ga.value.saveLastValue() saving atomic structure html from tag " + stag );  }
                     if ( _jmol_info && _jmol_info[ stag ] && _jmol_info[ stag ].length ) {
__~debug:values{ console.log( "ga.value.saveLastValue() atomic structure _jmol_info found for tag " + stag );}
                         $( "#global_data" ).data( pkg + ":" + tag + ":last_value", _jmol_info[ stag ].script ); 
                     } else {
__~debug:values{ console.log( "ga.value.saveLastValue() atomic structure _jmol_info NOT found for tag " + stag );}
                         $( "#global_data" ).data( pkg + ":" + tag + ":last_value", "" ); 
                     }
                     break;
       default: $( "#global_data" ).data( pkg + ":" + tag + ":last_value", t.val() ); break;
   }
__~debug:values{   console.log( "ga.value.saveLastValue() t is " + pkg + ":" + tag + ":last_value" );}
__~debug:values{   console.log( "ga.value.saveLastValue() " + $( "#global_data" ).data( pkg + ":" + tag + ":last_value" ) );}
}

ga.value.saveLastValues = function( pkg ) {
__~debug:values{   console.log( "ga.value.saveLastValues( " + pkg + " )" );}
   $( "#" + pkg + " :input" ).each(function() {
__~debug:values{   console.log( "ga.value.saveLastValues( " + pkg + " ) for " + $( this ).attr( "id" ) );}
      ga.value.saveLastValue( pkg, "#" + $( this ).attr( "id" ) );
   });
}

ga.value.resetDefaultValue = function( pkg, tag ) {
__~debug:values{   console.log( "ga.value.resetDefaultValue( " + pkg + " , " + tag + " )" );}
   var t = $( tag );
__~debug:values{   console.log( "ga.value.resetDefaultValue() type:" + t.attr( "type" ) );}
__~debug:values{   console.log( "ga.value.resetDefaultValue() tagname:" + t.prop( "tagName" ) );}
   if(  t.prop( "tagName" ) == 'SELECT' ) {
    t.val( $( "#global_data" ).data( pkg + ":" + tag + ":default_value" ) );
   } else {
      switch( t.attr( "type" ) )
      {
          case "file" : __~debug:values{console.log( "ga.value.resetDefaultValue() file set is insecure, skipped" );} return; break;
          case "checkbox" : 
                        $( "#global_data" ).removeData( pkg + ":" + tag + ":repeat:count" );
__~debug:values{                         console.log( "ga.value.resetDefaultValue() removeData: " + pkg + ":" + tag + ":repeat:count" );}
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
__~debug:plottwod{                     console.log( "ga.value.resetDefaultValue() plot2d" );}
                        $( "#global_data" ).data( pkg + ":" + tag + ":last_value", [[]] );
                        ga.value.clear.plot2d( tag );
                        t.plot( [[]], ga.value.get.plot2d.plot_options( tag ) ); break;
                        break;
          case "atomicstructure" : 
                        var stag = tag.replace( /^#/, "" );
__~debug:values{                         console.log( "ga.value.resetDefaultValue() atomic structure " + pkg + ":" + tag + ":last_value" );}
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
__~debug:values{   console.log( "ga.value.resetDefautValues( " + pkg + " )" );}
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
        __~debug:values{     console.log( "ga.value.resetDefaultValues() extra_resets " + i );}
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
__~debug:values{     console.log( "ga.value.extra_resets( " + id + " )" );}
    ga.value.extra_resets.data = ga.value.extra_resets.data || {};
    ga.value.extra_resets.data[ id ] = 1;
}

ga.value.extra_resets.clear = function() {
__~debug:values{     console.log( "ga.value.extra_resets.clear()" );}
    ga.value.extra_resets.data = {};
}
    

ga.value.setLastValueOutput = function( mod ) {
__~debug:values{     console.log( "ga.value.setLastValueOutput( " + mod + " )" );}
    var hmod            = "#" + mod,
        hmod_textarea   = hmod + "_textarea",
        jqhmod_textarea = $( hmod_textarea );

    ga.value.setLastValue( mod, hmod + "_msgs" );
    ga.value.setLastValue( mod, hmod_textarea );
    if ( jqhmod_textarea.val() ) {
__~debug:values{     console.log( "ga.value.setLastValueOutput() show textarea" );}
        jqhmod_textarea.show();
        $( hmod_textarea + "_label" ).show(); 
        jqhmod_textarea.height( parseFloat( jqhmod_textarea.prop( 'scrollHeight' ) ) + 
                                parseFloat( jqhmod_textarea.css ( 'borderTopWidth' ) ) + 
                                parseFloat( jqhmod_textarea.css ( 'borderBottomWidth' ) ) );
    } else {
__~debug:values{     console.log( "ga.value.setLastValueOutput() hide textarea" );}
        jqhmod_textarea.hide();
        $( hmod_textarea + "_label" ).hide();
    }
}
    
ga.value.get = {};
ga.value.set = {};
ga.value.clear = {};

ga.value.set.plot2d = function( tag, options ) {
__~debug:plottwod{ console.log( "ga.value.set.plot2d( " + tag + " , " + options + " )" );}
    var tagtitle  = tag + "_title",
        tagxlabel = tag + "_xlabel",
        tagylabel = tag + "_ylabel";

__~debug:plottwod{ console.log( "ga.value.set.plot2d() title  is " + options.title );}
__~debug:plottwod{ console.log( "ga.value.set.plot2d() xlabel is " + options.xlabel );}
__~debug:plottwod{ console.log( "ga.value.set.plot2d() ylabel is " + options.ylabel );}

    $( tagtitle  ).html( options.title  ? options.title  : "");
    $( tagxlabel ).html( options.xlabel ? options.xlabel : "");
    $( tagylabel ).html( options.ylabel ? options.ylabel : "");
}

ga.value.clear.plot2d = function( tag ) {
__~debug:plottwod{ console.log( "ga.value.clear.plot2d( " + tag + " )" );}
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
__~debug:plottwod{ console.log( "ga.value.set.plot2d.pan( " + tag + " , " + value + " )" );}
    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].pan = value ? true : false;
}

ga.value.set.plot2d.zoom = function( tag, value, pkg ) {
__~debug:plottwod{ console.log( "ga.value.set.plot2d.zoom( " + tag + " , " + value + " )" );}
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
__~debug:plottwod{ console.log( "ga.value.set.plot2d.pkg( " + pkg + " , " + tag + " )" );}

    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].pkg = pkg;
    $( tag + "_title," + tag + "_xlabel," + tag + "_ylabel," + tag + "_xy" )
        .on("click", ga.value.set.plot2d.reset );
}

ga.value.set.plot2d.reset = function( event ) {
    var id = "#" + event.target.id.replace( /(_title|_xlabel|_ylabel|_xy)$/, "" );
    event.preventDefault();
__~debug:plottwod{ console.log( "ga.value.set.plot2d.reset() pkg " + ga.value.settings[ id ].pkg + " id " + id );}
    ga.value.setLastValue( ga.value.settings[ id ].pkg, id );
}

ga.value.set.plot2d.hover = function( tag, value ) {
__~debug:plottwod{ console.log( "ga.value.set.plot2d.hover( " + tag + " , " + value + " )" );}
    ga.value.settings[ tag ] = ga.value.settings[ tag ] || {};
    ga.value.settings[ tag ].hover = value ? true : false;
}

ga.value.get.plot2d = {};
ga.value.get.plot2d.plot_options = function( tag, options ) {
__~debug:plottwod{ console.log( "ga.value.get.plot2d.plot_options( " + tag + " )" );}

    var plot_options = ga.plot_options();

    plot_options.pan.interactive  = ga.value.settings[ tag ].pan   ? true : false;
    plot_options.zoom.interactive = ga.value.settings[ tag ].zoom  ? true : false;
    plot_options.grid.hoverable   = ga.value.settings[ tag ].hover ? true : false;

    if ( options ) {
        if ( options.legend ) {
            plot_options.legend           = options.legend;
            __~debug:plottwod{console.log( "ga.value.get.plot2d.plot_options( " + tag + " , options ) has legend" );}
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
            __~debug:plottwod{console.log( "ga.value.get.plot2d.plot_options( " + tag + " , options ) has ymin of " + options.ymin );}
        }
        if ( options.ymax ) {
            plot_options.yaxis.max        = options.ymax;
            __~debug:plottwod{console.log( "ga.value.get.plot2d.plot_options( " + tag + " , options ) has ymax of " + options.ymax );}
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
__~debug:fp{    console.log( "FP val " + val + " dec " + dec + " tofixed " + val.toFixed( dec ) );}
        return val.toFixed( dec );
    }
    if ( val.toString().length > 6 ) {
__~debug:fp{    console.log( "FP val " + val + " dec " + dec + " toPrecision " + val.toExponential( 3 ) );}
        return val.toExponential( 3 ).replace( /0+e/, 'e' ).replace( /\.e/, 'e' );
    }
__~debug:fp{    console.log( "FP val " + val + " dec " + dec + " toFixed dropout " + val.toFixed( 0 ) );}
    return val.toFixed( 0 );
}

ga.value.plot2d.ticformatter = function formatter(val, axis) {
    var tval;
    if ( !axis._ehb || val <= axis.min ) {
__~debug:plottics{        console.log( "ticformatter initialized val " + val + " min " + axis.min + " max " + axis.max);}
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
__~debug:plottics{        console.log( "ticformatter snd plotted" );}
        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }

    if ( !axis._ehb.tr ) {
        axis._ehb.tr  = 2 * Math.abs( (val - axis._ehb.sndv ) ) / axis._ehb.rnge;
        axis._ehb.ptd = Math.abs( axis.options.transform( val ) - axis._ehb.tmin ) * axis._ehb.tmaxr;
__~debug:plottics{        console.log( "ticformatter tr set tr " + axis._ehb.tr + " ptd " + axis._ehb.ptd );}
        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }

    tval = ( axis.options.transform( val ) - axis._ehb.tmin ) * axis._ehb.tmaxr;

__~debug:plotticsx{    var tmp = Math.min( Math.abs( tval - axis._ehb.ptd ), 1 - tval );}

    if ( Math.min( Math.abs( tval - axis._ehb.ptd ), 1 - tval ) >= axis._ehb.tr )
    {
__~debug:plottics{    console.log( "tr " + axis._ehb.tr + " tval " + tval + " ptd " + axis._ehb.ptd + " tmp " + tmp + " plotted diff " + Math.min( Math.abs( tval - axis._ehb.ptd ), 1 - tval ) );}
        axis._ehb.ptd = tval;
        return ga.value.plot2d.toFP( val, axis.tickDecimals );
    }

__~debug:plotticsx{    console.log( "tr " + axis._ehb.tr + " tval " + tval + " ptd " + axis._ehb.ptd + " tmp " + tmp + " skipped" );}

    return "";
};
