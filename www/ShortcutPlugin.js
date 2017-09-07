//Copyright 2013 Jorge Cisneros jorgecis@gmail.com

var ShortcutPlugin = function () {
};

ShortcutPlugin.prototype.CreateShortcut = function (data, successCallback, errorCallback) {

  // to provide backwards compatibility
  if (typeof data === 'string') {
    data = {
      text: data
    };
  }

  if (typeof data !== 'object' || typeof data.name !== 'string' || typeof data.url !== 'string') {
    errorCallback('required text is not set or not a string');
    return;
  }

  cordova.exec(
    successCallback,
    errorCallback,
    'ShortcutPlugin',
    'addShortcut',
    [data]
  );
};

if (!window.plugins) {
  window.plugins = {};
}
if (!window.plugins.Shortcut) {
  window.plugins.Shortcut = new ShortcutPlugin();
}
