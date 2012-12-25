
define([], function() {
  var Log;
  Log = (function() {

    function Log() {}

    Log.prototype.logSlide = function(id) {
      var errorFS, fname, initFS, now;
      now = new Date();
      initFS = function(fs) {
        return window.fs = fs;
      };
      errorFS = function(where) {
        return function(err) {
          var msg;
          msg = 'An error occured: ';
          switch (err.code) {
            case FileError.NOT_FOUND_ERR:
              msg += 'File or directory not found';
              break;
            case FileError.NOT_READABLE_ERR:
              msg += 'File or directory not readable';
              break;
            case FileError.PATH_EXISTS_ERR:
              msg += 'File or directory already exists';
              break;
            case FileError.TYPE_MISMATCH_ERR:
              msg += 'Invalid filetype';
              break;
            default:
              msg += 'Unknown Error';
          }
          msg += " (" + where + ")";
          return console.log(msg);
        };
      };
      if (!window.fs) {
        try {
          window.webkitStorageInfo.requestQuota(PERSISTENT, 5 * 1024 * 1024, function(grantedBytes) {
            return window.fs = window.webkitRequestFileSystem(window.PERSISTENT, grantedBytes, initFS, errorFS("init"));
          }, errorFS("quota"));
        } catch (error) {
          console.log("log file not supported");
        }
      }
      if (window.fs) {
        fname = (now.getMonth() + 1) + "m." + now.getDate() + 'd.slides.txt';
        console.log("writing to file " + fname);
        return fs.root.getFile(fname, {
          create: true
        }, function(fileEntry) {
          return fileEntry.createWriter(function(fileWriter) {
            fileWriter.seek(fileWriter.length);
            return fileWriter.write(new Blob([id + "," + now.getTime() + "\n"], {
              "type": "text\/plain"
            }));
          }, errorFS("write"));
        }, errorFS("get"));
      }
    };

    return Log;

  })();
  return new Log;
});
