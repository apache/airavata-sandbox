/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.hide = function( module, id ) {
__~debug:hide{   console.log( "ga.hide( " + module + " , " + id + " )" );}
    ga.hide.data = ga.hide.data || {};
    ga.hide.data[ module ] = ga.hide.data[ module ] || {};
    ga.hide.data[ module ][ id ] = ga.hide.data[ module ][ id ] || {};
    ga.hide.data[ module ][ id ].active = 1;

__~debug:hide{   console.log( "ga.hide( " + module + " , " + id + " ) hider added" );}
};

ga.hide.data = {};

ga.hide.update = function( module, id ) {
__~debug:hide{    console.log( "ga.hide.update( " + module + " , " + id + " )" );}
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
__~debug:hide{    console.log( "ga.hide.update( " + module + " , " + id + " ) hiding " + i );}
            $( i + "-itd" ).html(" ");
            $( i ).hide();
        } 
    } else {
        for ( i in ga.hide.data[ module ][ id ].hides ) {
__~debug:hide{    console.log( "ga.hide.update( " + module + " , " + id + " ) showing " + i );}
            $( i + "-itd" ).html( ga.hide.data[ module ][ id ].hides[ i ] );
            $( i ).show();
        } 
    }
// fix up help
    setHoverHelp();
__~debug:hide{    console.log( "ga.hide.update( " + module + " , " + id + " ) returns" );}
}

ga.hide.add = function( module, id, hiderid ) {
__~debug:hide{   console.log( "ga.hide.add( " + module + " , " + id + " , " + hiderid + " )" );}
    ga.hide.data = ga.hide.data || {};
    ga.hide.data[ module ] = ga.hide.data[ module ] || {};
    ga.hide.data[ module ][ hiderid ] = ga.hide.data[ module ][ hiderid ] || {};
    ga.hide.data[ module ][ hiderid ].hides = ga.hide.data[ module ][ hiderid ].hides || {};
    ga.hide.data[ module ][ hiderid ].hides[ id ] = $( id + "-itd" ).html();
__~debug:hide{   console.log( "ga.hide.add() html is " + ga.hide.data[ module ][ hiderid ].hides[ id ] );}
__~debug:hide{   console.log( "ga.hide.add( " + module + " , " + id + " , " + hiderid + " ) hide added" );}
};


    

