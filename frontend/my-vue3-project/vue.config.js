module.exports = {
  devServer: {
    https: true,
    proxy: {
      "/api": {
        target: "https://localhost:12345",
        changeOrigin: true,
        // 移除 pathRewrites
      },
    },
  },
};
