logSlide = (id) ->
  now = new Date()

  initFS = (fs) ->
    window.fs = fs

  errorFS = (where) ->
    (err) ->
      msg = 'An error occured: '
      switch (err.code)
        when FileError.NOT_FOUND_ERR
          msg += 'File or directory not found'
        when FileError.NOT_READABLE_ERR
          msg += 'File or directory not readable'
        when FileError.PATH_EXISTS_ERR
          msg += 'File or directory already exists'
        when FileError.TYPE_MISMATCH_ERR
          msg += 'Invalid filetype';
        else
          msg += 'Unknown Error';
      msg += " (" + where + ")"
      console.log(msg)

  if(!window.fs)
    try
      window.webkitStorageInfo.requestQuota(PERSISTENT, 5*1024*1024, (grantedBytes) ->
        window.fs = window.webkitRequestFileSystem(window.PERSISTENT, grantedBytes, initFS, errorFS("init"))
      , errorFS("quota"))
    catch error
      console.log("log file not supported")

  if(window.fs)
    fname = (now.getMonth()+1) + "m." +  now.getDate() + 'd.slides.txt'
    console.log("writing to file " + fname)
    fs.root.getFile(fname, {create: true},
      (fileEntry) ->
        fileEntry.createWriter((fileWriter) ->
          fileWriter.seek(fileWriter.length)
          fileWriter.write(new Blob([id + "," + now.getTime() + "\n"], { "type" : "text\/plain" }))
        , errorFS("write"));
    , errorFS("get"))