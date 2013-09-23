module.exports = function(config) {
  config.set({
  basePath : '../',
  frameworks: ['jasmine'],
files : [
  'webapp/js/vendor/angular-*.js',
  'webapp/js/vendor/ui-bootstrap-tpls-0.4.0.min.js',
  'webapp/js/**/*.js',
  'test/**/*.js'
],
autoWatch : true,
browsers : ['Chrome'],
junitReporter : {
  outputFile: 'test_out/unit.xml',
  suite: 'unit'
}
});
};