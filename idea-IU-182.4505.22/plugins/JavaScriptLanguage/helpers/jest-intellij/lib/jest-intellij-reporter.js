const Tree = require('../../base-test-reporter/intellij-tree');
const tree = new Tree(null, getStdoutWrite());
const jestIntellijUtil = require('./jest-intellij-util');
const intellijUtil = require('../../base-test-reporter/intellij-util');

function getStdoutWrite() {
  if (typeof process.stdout._intellijOriginalWrite === 'function') {
    return process.stdout._intellijOriginalWrite;
  }
  return process.stdout.write.bind(process.stdout);
}

tree.startNotify();

let runningTests = {};

function IntellijReporter() {
}

IntellijReporter.prototype.onRunStart = jestIntellijUtil.safeFn(function (results, options) {
  runningTests = {};
  tree.testingStarted();
});

IntellijReporter.prototype.onTestStart = jestIntellijUtil.safeFn(function (test) {
  const testFileNode = jestIntellijUtil.addTestFileNode(tree, test.path);
  testFileNode.id = test.path;
  testFileNode.register();
  testFileNode.start();
  const jasmineReporterConfigured = configureJasmineReporter(test);
  runningTests[test.path] = {node:testFileNode, jasmineReporter: jasmineReporterConfigured};
});

function configureJasmineReporter(test) {
  if (!canConfigureJasmineReporter(test)) {
    return false;
  }
  const jasmineReporter = require.resolve('./jest-intellij-jasmine.js');
  if (test.context.config.setupTestFrameworkScriptFile === jasmineReporter) {
    return true;
  }
  const dirname = require('path').dirname;
  // ignore helpers/ directory from /path/to/helpers/jest-intellij/lib/jest-intellij-jasmine.js
  const ignorePattern = '^' + escapePattern(dirname(dirname(dirname(jasmineReporter))));

  const newGlobals = jestIntellijUtil.createGlobals(test.context.config.setupTestFrameworkScriptFile);
  test.context.config = Object.assign({}, test.context.config, {
    setupTestFrameworkScriptFile: jasmineReporter,
    transformIgnorePatterns: concatArrays(test.context.config.transformIgnorePatterns, [ignorePattern]),
    unmockedModulePathPatterns: concatArrays(test.context.config.unmockedModulePathPatterns, [ignorePattern]),
    globals: Object.assign({}, test.context.config.globals || {}, newGlobals)
  });
  return true;
}

function concatArrays(array1, array2) {
  if (Array.isArray(array1) && Array.isArray(array2)) {
    return array1.concat(array2);
  }
  return Array.isArray(array1) ? array1 : array2;
}

function canConfigureJasmineReporter(test) {
  if (!process.env[jestIntellijUtil.JASMINE_REPORTER_DISABLED] && test && test.context && test.context.config) {
    if (intellijUtil.isString(test.context.config.testRunner)) {
      const testRunner = test.context.config.testRunner.replace(/\\/g, '/');
      const suffix = '/jest-jasmine2/build/index.js';
      if (testRunner.length > suffix.length && testRunner.lastIndexOf(suffix) === testRunner.length - suffix.length) {
        return true;
      }
    }
  }
  return false;
}

function escapePattern(str) {
  return str.replace(/[.?*+^$[\]\\(){}|]/g, "\\$&");
}

IntellijReporter.prototype.onTestResult = jestIntellijUtil.safeFn(function (test, testResult, aggregatedResult) {
  const testFileObj = runningTests[testResult.testFilePath];
  if (testFileObj) {
    jestIntellijUtil.reportTestFileResults(testFileObj.node, testResult, !testFileObj.jasmineReporter);
  }
  else {
    jestIntellijUtil.warn('No started test for ' + testResult.testFilePath);
  }
  delete runningTests[testResult.testFilePath];
});

IntellijReporter.prototype.onRunComplete = jestIntellijUtil.safeFn(function (contexts, results) {
  tree.testingFinished();
});

module.exports = IntellijReporter;
