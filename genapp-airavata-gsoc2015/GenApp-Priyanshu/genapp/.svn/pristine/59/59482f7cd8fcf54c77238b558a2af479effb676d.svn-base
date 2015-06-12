#!/usr/bin/perl

$notes = "usage: $0
checks the files
note: env variable GENAPP must be defined
" ;

$gap = $ENV{ "GENAPP" } || die "$0: error env variable GENAPP must be defined\n";

require "$gap/etc/perl/genapp_util.pl";

check_files() || die "Errors found\n";

print "ok\n";
