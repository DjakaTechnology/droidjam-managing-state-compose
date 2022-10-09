//config.resolve = {
//    fallback: {
//        fs: false,
//        path: false,
//        crypto: false,
//    }
//};

const webpack = require("webpack");

module.exports = function override(config) {
  const fallback = config.resolve.fallback || {};
  Object.assign(fallback, {
    path: require.resolve("path-browserify"),
    fs: require.resolve("browserify-fs"),
    "util": require.resolve("util/"),
    "buffer": require.resolve("buffer/"),
    "stream": require.resolve("stream-browserify"),
    "crypto": require.resolve("crypto-browserify")
  });
  config.resolve.fallback = fallback;
  return config;
};