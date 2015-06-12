/*jslint white: true, plusplus: true*/
/* assumes: jquery > 1.11.0, jqtree >= 3.0.9, jquery-base64 */

ga.valid = {};

ga.valid.checkMatch = function( tag1, tag2 ) {
__~debug:check{   console.log( "checkMatch " + tag1 + " " + tag2 );}
   if ( $( tag1 ).val() != $( tag2 ).val() )
   {
      $( tag1 + "_msg" ).html( " does not match" );
   } else {
      $( tag1 + "_msg" ).html( "" );
   }
}
    
ga.valid.checkFloat = function( tag ) {
__~debug:check{   console.log( "checkfloat " + tag );}
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
__~debug:check{   console.log( "checkint " + tag );}
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
__~debug:valid{   console.log( "ga.valid.checkLRfile( " + tag + " )");}
   var t   = $( tag ),
       r   = $( tag + '_altval > i' ),
       msg = $( tag + "_msg" ),
       ok  = 0;
   if ( !t || !t.is(':visible') ) {
       return 1;
   }
   if ( t && t.val() && t.val().length ) {
__~debug:valid{       msg.html( "checkLRfile ok local file:" + t.val() );}
       ok = 1;
   } else {
       if ( r && r.html() && r.html().length && r.html() === "Server" ) {
__~debug:valid{       msg.html( "checkLRfile ok server file:" + r.html() );}
           ok = 1;
       }
   }
   if ( !ok ) {
       msg.html( " missing required field" );
   }
   return ok;
}

ga.valid.checkRpath = function( tag ) {
__~debug:valid{   console.log( "ga.valid.checkRfile( " + tag + " )");}
   var t   = $( tag ),
       r   = $( tag + '_altval > i' ),
       msg = $( tag + "_msg" ),
       ok  = 0;

   if ( !t || !t.is(':visible') ) {
       return 1;
   }
   if ( r && r.html() && r.html().length && r.html() === "Server" ) {
__~debug:valid{       msg.html( "checkRfile ok server file:" + r.html() );}
       ok = 1;
   }
   if ( !ok ) {
       msg.html( " missing required field" );
   }

   return ok;
}

ga.valid.checkRfile = function( tag ) {
__~debug:valid{   console.log( "ga.valid.checkRfile( " + tag + " )");}
   var t   = $( tag ),
       r   = $( tag + '_altval > i' ),
       msg = $( tag + "_msg" ),
       ok  = 0;

   if ( !t || !t.is(':visible') ) {
       return 1;
   }
   if ( r && r.html() && r.html().length && r.html() === "Server" ) {
__~debug:valid{       msg.html( "checkRfile ok server file:" + r.html() );}
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
__~debug:valid{   console.log( "ga.valid.checksubmit( " + module + " )" );}
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
          
   
