

define('app/slide/log',[], function() {
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

/*!
 * reveal.js 1.4
 * http://lab.hakim.se/reveal-js
 * MIT licensed
 * 
 * Copyright (C) 2012 Hakim El Hattab, http://hakim.se
 */
var Reveal = (function(){
	
	var HORIZONTAL_SLIDES_SELECTOR = '.reveal .slides>section',
		VERTICAL_SLIDES_SELECTOR = '.reveal .slides>section.present>section',

		IS_TOUCH_DEVICE = !!( 'ontouchstart' in window ),

		// The horizontal and verical index of the currently active slide
		indexh = 0,
		indexv = 0,

		// Configurations options, can be overridden at initialization time 
		config = {
			controls: true,
			progress: false,
			history: false,
			loop: false,
			mouseWheel: true,
			rollingLinks: true,
			transition: 'default',
			theme: 'default'
		},

		// Slides may hold a data-state attribute which we pick up and apply 
		// as a class to the body. This list contains the combined state of 
		// all current slides.
		state = [],

		// Cached references to DOM elements
		dom = {},

		// Detect support for CSS 3D transforms
		supports3DTransforms =  document.body.style['WebkitPerspective'] !== undefined || 
                        		document.body.style['MozPerspective'] !== undefined ||
                        		document.body.style['msPerspective'] !== undefined ||
                        		document.body.style['OPerspective'] !== undefined ||
                        		document.body.style['perspective'] !== undefined,
        
        supports2DTransforms =  document.body.style['WebkitTransform'] !== undefined || 
                        		document.body.style['MozTransform'] !== undefined ||
                        		document.body.style['msTransform'] !== undefined ||
                        		document.body.style['OTransform'] !== undefined ||
                        		document.body.style['transform'] !== undefined,

        // Detect support for elem.classList
        supportsClassList = !!document.body.classList;
		
		// Throttles mouse wheel navigation
		mouseWheelTimeout = 0,

		// Delays updates to the URL due to a Chrome thumbnailer bug
		writeURLTimeout = 0,

		// Holds information about the currently ongoing touch input
		touch = {
			startX: 0,
			startY: 0,
			startSpan: 0,
			startCount: 0,
			handled: false,
			threshold: 40
		};
	
	
	/**
	 * Starts up the slideshow by applying configuration
	 * options and binding various events.
	 */
	function initialize( options ) {
		
		if( ( !supports2DTransforms && !supports3DTransforms ) || !supportsClassList ) {
			document.body.setAttribute( 'class', 'no-transforms' );

			// If the browser doesn't support core features we won't be 
			// using JavaScript to control the presentation
			return;
		}

		// Cache references to DOM elements
		dom.wrapper = document.querySelector( '.reveal' );
		dom.progress = document.querySelector( '.reveal .progress' );
		dom.progressbar = document.querySelector( '.reveal .progress span' );
		
		if ( config.controls ) {
			dom.controls = document.querySelector( '.reveal .controls' );
			dom.controlsLeft = document.querySelector( '.reveal .controls .left' );
			dom.controlsRight = document.querySelector( '.reveal .controls .right' );
			dom.controlsUp = document.querySelector( '.reveal .controls .up' );
			dom.controlsDown = document.querySelector( '.reveal .controls .down' );
		}

		addEventListeners();

		// Copy options over to our config object
		extend( config, options );

		// Updates the presentation to match the current configuration values
		configure();

		// Read the initial hash
		readURL();

		// Set up hiding of the browser address bar
		if( navigator.userAgent.match( /(iphone|ipod|android)/i ) ) {
			// Give the page some scrollable overflow
			document.documentElement.style.overflow = 'scroll';
			document.body.style.height = '120%';

			// Events that should trigger the address bar to hide
			window.addEventListener( 'load', removeAddressBar, false );
			window.addEventListener( 'orientationchange', removeAddressBar, false );
		}
		
	}

	function configure() {
		// Fall back on the 2D transform theme 'linear'
		if( supports3DTransforms === false ) {
			config.transition = 'linear';
		}

		if( config.controls && dom.controls ) {
			dom.controls.style.display = 'block';
		}

		if( config.progress ) {
			dom.progress.style.display = 'block';
		}

		if( config.transition !== 'default' ) {
			dom.wrapper.classList.add( config.transition );
		}

		if( config.theme !== 'default' ) {
			dom.wrapper.classList.add( config.theme );
		}

        /*
		if( config.mouseWheel ) {
			document.addEventListener( 'DOMMouseScroll', onDocumentMouseScroll, false ); // FF
			document.addEventListener( 'mousewheel', onDocumentMouseScroll, false );
		}

		if( config.rollingLinks ) {
			// Add some 3D magic to our anchors
			linkify();
		}
		*/
	}

	function addEventListeners() {
		document.addEventListener( 'keydown', onDocumentKeyDown, false );
		document.addEventListener( 'touchstart', onDocumentTouchStart, false );
		document.addEventListener( 'touchmove', onDocumentTouchMove, false );
		document.addEventListener( 'touchend', onDocumentTouchEnd, false );
		window.addEventListener( 'hashchange', onWindowHashChange, false );

		if ( config.controls && dom.controls ) {
			dom.controlsLeft.addEventListener( 'click', preventAndForward( navigateLeft ), false );
			dom.controlsRight.addEventListener( 'click', preventAndForward( navigateRight ), false );
			dom.controlsUp.addEventListener( 'click', preventAndForward( navigateUp ), false );
			dom.controlsDown.addEventListener( 'click', preventAndForward( navigateDown ), false );	
		}
	}

	function removeEventListeners() {
		document.removeEventListener( 'keydown', onDocumentKeyDown, false );
		document.removeEventListener( 'touchstart', onDocumentTouchStart, false );
		document.removeEventListener( 'touchmove', onDocumentTouchMove, false );
		document.removeEventListener( 'touchend', onDocumentTouchEnd, false );
		window.removeEventListener( 'hashchange', onWindowHashChange, false );
		
		if ( config.controls && dom.controls ) {
			dom.controlsLeft.removeEventListener( 'click', preventAndForward( navigateLeft ), false );
			dom.controlsRight.removeEventListener( 'click', preventAndForward( navigateRight ), false );
			dom.controlsUp.removeEventListener( 'click', preventAndForward( navigateUp ), false );
			dom.controlsDown.removeEventListener( 'click', preventAndForward( navigateDown ), false );
		}
	}

	/**
	 * Extend object a with the properties of object b. 
	 * If there's a conflict, object b takes precedence.
	 */
	function extend( a, b ) {
		for( var i in b ) {
			a[ i ] = b[ i ];
		}
	}

	/**
	 * Measures the distance in pixels between point a
	 * and point b. 
	 * 
	 * @param {Object} a point with x/y properties
	 * @param {Object} b point with x/y properties
	 */
	function distanceBetween( a, b ) {
		var dx = a.x - b.x,
			dy = a.y - b.y;

		return Math.sqrt( dx*dx + dy*dy );
	}

	/**
	 * Prevents an events defaults behavior calls the 
	 * specified delegate.
	 * 
	 * @param {Function} delegate The method to call 
	 * after the wrapper has been executed
	 */
	function preventAndForward( delegate ) {
		return function( event ) {
			event.preventDefault();
			delegate.call();
		}
	}

	/**
	 * Causes the address bar to hide on mobile devices, 
	 * more vertical space ftw.
	 */
	function removeAddressBar() {
		setTimeout( function() {
			window.scrollTo( 0, 1 );
		}, 0 );
	}
	
	/**
	 * Handler for the document level 'keydown' event.
	 * 
	 * @param {Object} event
	 */
	function onDocumentKeyDown( event ) {
        // HACK: modal visible? don't react!
        if($("#ideModal").is(':visible')) return;

		// FFT: Use document.querySelector( ':focus' ) === null 
		// instead of checking contentEditable?

		// Disregard the event if the target is editable or a 
		// modifier is present
		if ( event.target.contentEditable != 'inherit' || event.shiftKey || event.altKey || event.ctrlKey || event.metaKey ) return;
				
		var triggered = false;

		switch( event.keyCode ) {
			// p, page up
			/*case 80:*/ case 33: navigateNext(); triggered = true; break;
			// n, page down
			/*case 78:*/ case 34: navigatePrev(); triggered = true; break;
			// h, left, F7
			/*case 72:*/ case 37: case 118: navigateLeft(); triggered = true; break;
			// l, right, F9
			/*case 76:*/ case 39: case 120: navigateRight(); triggered = true; break;
			// k, up
			/*case 75:*/ //case 38: navigateUp(); triggered = true; break;
			// j, down
			/*case 74:*/ //case 40: navigateDown(); triggered = true; break;
			// home
			//case 36: navigateTo( 0 ); triggered = true; break;
			// end
			//case 35: navigateTo( Number.MAX_VALUE ); triggered = true; break;
			// space
			//case 32: overviewIsActive() ? deactivateOverview() : navigateNext(); triggered = true; break;
			// return
			//case 13: if( overviewIsActive() ) { deactivateOverview(); triggered = true; } break;
		}

		if( triggered ) {
			event.preventDefault();
		}
		/*else if ( event.keyCode === 27 && supports3DTransforms ) {
			if( overviewIsActive() ) {
				deactivateOverview();
			}
			else {
				activateOverview();
			}
			event.preventDefault();
		}*/
	}

	/**
	 * Handler for the document level 'touchstart' event,
	 * enables support for swipe and pinch gestures.
	 */
	function onDocumentTouchStart( event ) {
		touch.startX = event.touches[0].clientX;
		touch.startY = event.touches[0].clientY;
		touch.startCount = event.touches.length;

		// If there's two touches we need to memorize the distance 
		// between those two points to detect pinching
		if( event.touches.length === 2 ) {
			touch.startSpan = distanceBetween( {
				x: event.touches[1].clientX,
				y: event.touches[1].clientY
			}, {
				x: touch.startX,
				y: touch.startY
			} );
		}
	}
	
	/**
	 * Handler for the document level 'touchmove' event.
	 */
	function onDocumentTouchMove( event ) {
		// Each touch should only trigger one action
		if( !touch.handled ) {
			var currentX = event.touches[0].clientX;
			var currentY = event.touches[0].clientY;

			// If the touch started off with two points and still has 
			// two active touches; test for the pinch gesture
			if( event.touches.length === 2 && touch.startCount === 2 ) {

				// The current distance in pixels between the two touch points
				var currentSpan = distanceBetween( {
					x: event.touches[1].clientX,
					y: event.touches[1].clientY
				}, {
					x: touch.startX,
					y: touch.startY
				} );

				// If the span is larger than the desire amount we've got 
				// ourselves a pinch
				if( Math.abs( touch.startSpan - currentSpan ) > touch.threshold ) {
					touch.handled = true;

					if( currentSpan < touch.startSpan ) {
						activateOverview();
					}
					else {
						deactivateOverview();
					}
				}

			}
			// There was only one touch point, look for a swipe
			else if( event.touches.length === 1 ) {
				var deltaX = currentX - touch.startX,
					deltaY = currentY - touch.startY;

				if( deltaX > touch.threshold && Math.abs( deltaX ) > Math.abs( deltaY ) ) {
					touch.handled = true;
					navigateLeft();
				} 
				else if( deltaX < -touch.threshold && Math.abs( deltaX ) > Math.abs( deltaY ) ) {
					touch.handled = true;
					navigateRight();
				} 
				else if( deltaY > touch.threshold ) {
					touch.handled = true;
					navigateUp();
				} 
				else if( deltaY < -touch.threshold ) {
					touch.handled = true;
					navigateDown();
				}
			}

			event.preventDefault();
		}
	}

	/**
	 * Handler for the document level 'touchend' event.
	 */
	function onDocumentTouchEnd( event ) {
		touch.handled = false;
	}

	/**
	 * Handles mouse wheel scrolling, throttled to avoid 
	 * skipping multiple slides.
	 */
    /*
	function onDocumentMouseScroll( event ){
		clearTimeout( mouseWheelTimeout );

		mouseWheelTimeout = setTimeout( function() {
			var delta = event.detail || -event.wheelDelta;
			if( delta > 0 ) {
				navigateNext();
			}
			else {
				navigatePrev();
			}
		}, 100 );
	}
	*/
	
	/**
	 * Handler for the window level 'hashchange' event.
	 * 
	 * @param {Object} event
	 */
	function onWindowHashChange( event ) {
		readURL();
	}

	/**
	 * Wrap all links in 3D goodness.
	 */
    /*
	function linkify() {
        if( supports3DTransforms ) {
        	var nodes = document.querySelectorAll( '.reveal .slides section a:not(.image)' );

	        for( var i = 0, len = nodes.length; i < len; i++ ) {
	            var node = nodes[i];
	            
	            if( node.textContent && !node.querySelector( 'img' ) && ( !node.className || !node.classList.contains( node, 'roll' ) ) ) {
	                node.classList.add( 'roll' );
	                node.innerHTML = '<span data-title="'+ node.text +'">' + node.innerHTML + '</span>';
	            }
	        };
        }
	}
	*/

	/**
	 * Displays the overview of slides (quick nav) by 
	 * scaling down and arranging all slide elements.
	 * 
	 * Experimental feature, might be dropped if perf 
	 * can't be improved.
	 */
    /*
	function activateOverview() {

        return; // disable

		dom.wrapper.classList.add( 'overview' );

		var horizontalSlides = Array.prototype.slice.call( document.querySelectorAll( HORIZONTAL_SLIDES_SELECTOR ) );

		for( var i = 0, len1 = horizontalSlides.length; i < len1; i++ ) {
			var hslide = horizontalSlides[i],
				htransform = 'translateZ(-2500px) translate(' + ( ( i - indexh ) * 105 ) + '%, 0%)';
			
			hslide.setAttribute( 'data-index-h', i );
			hslide.style.display = 'block';
			hslide.style.WebkitTransform = htransform;
			hslide.style.MozTransform = htransform;
			hslide.style.msTransform = htransform;
			hslide.style.OTransform = htransform;
			hslide.style.transform = htransform;
		
			if( !hslide.classList.contains( 'stack' ) ) {
				// Navigate to this slide on click
				hslide.addEventListener( 'click', onOverviewSlideClicked, true );
			}
	
			var verticalSlides = Array.prototype.slice.call( hslide.querySelectorAll( 'section' ) );

			for( var j = 0, len2 = verticalSlides.length; j < len2; j++ ) {
				var vslide = verticalSlides[j],
					vtransform = 'translate(0%, ' + ( ( j - indexv ) * 105 ) + '%)';

				vslide.setAttribute( 'data-index-h', i );
				vslide.setAttribute( 'data-index-v', j );
				vslide.style.display = 'block';
				vslide.style.WebkitTransform = vtransform;
				vslide.style.MozTransform = vtransform;
				vslide.style.msTransform = vtransform;
				vslide.style.OTransform = vtransform;
				vslide.style.transform = vtransform;

				// Navigate to this slide on click
				vslide.addEventListener( 'click', onOverviewSlideClicked, true );
			}
			
		}
	}
	*/
	
	/**
	 * Exits the slide overview and enters the currently
	 * active slide.
	 */
    /*
	function deactivateOverview() {
		dom.wrapper.classList.remove( 'overview' );

		var slides = Array.prototype.slice.call( document.querySelectorAll( '.reveal .slides section' ) );

		for( var i = 0, len = slides.length; i < len; i++ ) {
			var element = slides[i];

			// Resets all transforms to use the external styles
			element.style.WebkitTransform = '';
			element.style.MozTransform = '';
			element.style.msTransform = '';
			element.style.OTransform = '';
			element.style.transform = '';

			element.removeEventListener( 'click', onOverviewSlideClicked );
		}

		slide();
	}
	*/

	/**
	 * Checks if the overview is currently active.
	 * 
	 * @return {Boolean} true if the overview is active,
	 * false otherwise
	 */
	function overviewIsActive() {
		return false; //return dom.wrapper.classList.contains( 'overview' );
	}

	/**
	 * Invoked when a slide is and we're in the overview.
	 */
    /*
	function onOverviewSlideClicked( event ) {
		// TODO There's a bug here where the event listeners are not 
		// removed after deactivating the overview.
		if( overviewIsActive() ) {
			event.preventDefault();

			deactivateOverview();

			indexh = this.getAttribute( 'data-index-h' );
			indexv = this.getAttribute( 'data-index-v' );

			slide();
		}
	}
	*/

	/**
	 * Updates one dimension of slides by showing the slide
	 * with the specified index.
	 * 
	 * @param {String} selector A CSS selector that will fetch
	 * the group of slides we are working with
	 * @param {Number} index The index of the slide that should be
	 * shown
	 * 
	 * @return {Number} The index of the slide that is now shown,
	 * might differ from the passed in index if it was out of 
	 * bounds.
	 */
	function updateSlides( selector, index ) {
		
		// Select all slides and convert the NodeList result to
		// an array
		var slides = Array.prototype.slice.call( document.querySelectorAll( selector ) ),
			slidesLength = slides.length;
		
		if( slidesLength ) {

			// Should the index loop?
			if( config.loop ) {
				index %= slidesLength;

				if( index < 0 ) {
					index = slidesLength + index;
				}
			}
			
			// Enforce max and minimum index bounds
			index = Math.max( Math.min( index, slidesLength - 1 ), 0 );
			
			for( var i = 0; i < slidesLength; i++ ) {
				var slide = slides[i];

				// Optimization; hide all slides that are three or more steps 
				// away from the present slide
				if( overviewIsActive() === false ) {
					// The distance loops so that it measures 1 between the first
					// and last slides
					var distance = Math.abs( ( index - i ) % ( slidesLength - 3 ) ) || 0;

					slide.style.display = distance > 1 ? 'none' : 'block';
				}

				slides[i].classList.remove( 'past' );
				slides[i].classList.remove( 'present' );
				slides[i].classList.remove( 'future' );

				if( i < index ) {
					// Any element previous to index is given the 'past' class
					slides[i].classList.add( 'past' );
				}
				else if( i > index ) {
					// Any element subsequent to index is given the 'future' class
					slides[i].classList.add( 'future' );
				}

				// If this element contains vertical slides
				if( slide.querySelector( 'section' ) ) {
					slides[i].classList.add( 'stack' );
				}
			}

			// Mark the current slide as present
			slides[index].classList.add( 'present' );

			// If this slide has a state associated with it, add it
			// onto the current state of the deck
			var slideState = slides[index].getAttribute( 'data-state' );
			if( slideState ) {
				state = state.concat( slideState.split( ' ' ) );
			}
		}
		else {
			// Since there are no slides we can't be anywhere beyond the 
			// zeroth index
			index = 0;
		}
		
		return index;
		
	}
	
	/**
	 * Updates the visual slides to represent the currently
	 * set indices. 
	 */
	function slide( h, v ) {
		// Remember the state before this slide
		var stateBefore = state.concat();

		// Reset the state array
		state.length = 0;

		var indexhBefore = indexh,
			indexvBefore = indexv;

		// Activate and transition to the new slide
		indexh = updateSlides( HORIZONTAL_SLIDES_SELECTOR, h === undefined ? indexh : h );
		indexv = updateSlides( VERTICAL_SLIDES_SELECTOR, v === undefined ? indexv : v );

		// Apply the new state
		stateLoop: for( var i = 0, len = state.length; i < len; i++ ) {
			// Check if this state existed on the previous slide. If it 
			// did, we will avoid adding it repeatedly.
			for( var j = 0; j < stateBefore.length; j++ ) {
				if( stateBefore[j] === state[i] ) {
					stateBefore.splice( j, 1 );
					continue stateLoop;
				}
			}

			document.documentElement.classList.add( state[i] );

			// Dispatch custom event matching the state's name
			dispatchEvent( state[i] );
		}

		// Clean up the remaints of the previous state
		while( stateBefore.length ) {
			document.documentElement.classList.remove( stateBefore.pop() );
		}

		// Update progress if enabled
		if( config.progress ) {
			dom.progressbar.style.width = ( indexh / ( document.querySelectorAll( HORIZONTAL_SLIDES_SELECTOR ).length - 1 ) ) * window.innerWidth + 'px';
		}

		// Close the overview if it's active
		/*if( overviewIsActive() ) {
			activateOverview();
		}*/

		updateControls();
		
		clearTimeout( writeURLTimeout );
		writeURLTimeout = setTimeout( writeURL, 1500 );

		// Only fire if the slide index is different from before
		if( indexh !== indexhBefore || indexv !== indexvBefore ) {
			// Query all horizontal slides in the deck
			var horizontalSlides = document.querySelectorAll( HORIZONTAL_SLIDES_SELECTOR );

			// Find the previous and current horizontal slides
			var previousHorizontalSlide = horizontalSlides[ indexhBefore ],
				currentHorizontalSlide = horizontalSlides[ indexh ];

			// Query all vertical slides inside of the previous and current horizontal slides
			var previousVerticalSlides = previousHorizontalSlide.querySelectorAll( 'section' );
				currentVerticalSlides = currentHorizontalSlide.querySelectorAll( 'section' );

			// Dispatch an event notifying observers of the change in slide
			dispatchEvent( 'slidechanged', {
				// Include the current indices in the event
				'indexh': indexh, 
				'indexv': indexv,

				// Passes direct references to the slide HTML elements, attempts to find
				// a vertical slide and falls back on the horizontal parent
				'previousSlide': previousVerticalSlides[ indexvBefore ] || previousHorizontalSlide,
				'currentSlide': currentVerticalSlides[ indexv ] || currentHorizontalSlide
			} );
		}
	}

	/**
	 * Updates the state and link pointers of the controls.
	 */
	function updateControls() {
		if ( !config.controls || !dom.controls ) {
			return;
		}
		
		var routes = availableRoutes();

		// Remove the 'enabled' class from all directions
		[ dom.controlsLeft, dom.controlsRight, dom.controlsUp, dom.controlsDown ].forEach( function( node ) {
			node.classList.remove( 'enabled' );
		} )

		if( routes.left ) dom.controlsLeft.classList.add( 'enabled' );
		if( routes.right ) dom.controlsRight.classList.add( 'enabled' );
		if( routes.up ) dom.controlsUp.classList.add( 'enabled' );
		if( routes.down ) dom.controlsDown.classList.add( 'enabled' );
	}

	/**
	 * Determine what available routes there are for navigation.
	 * 
	 * @return {Object} containing four booleans: left/right/up/down
	 */
	function availableRoutes() {
		var horizontalSlides = document.querySelectorAll( HORIZONTAL_SLIDES_SELECTOR );
		var verticalSlides = document.querySelectorAll( VERTICAL_SLIDES_SELECTOR );

		return {
			left: indexh > 0,
			right: indexh < horizontalSlides.length - 1,
			up: indexv > 0,
			down: indexv < verticalSlides.length - 1
		};
	}
	
	/**
	 * Reads the current URL (hash) and navigates accordingly.
	 */
	function readURL() {
		// Break the hash down to separate components
		var bits = window.location.hash.slice(2).split('/');
		
		// Read the index components of the hash
		indexh = parseInt( bits[0] ) || 0 ;
		indexv = parseInt( bits[1] ) || 0 ;
		
		navigateTo( indexh, indexv );
	}
	
	/**
	 * Updates the page URL (hash) to reflect the current
	 * state. 
	 */
	function writeURL() {
		if( config.history ) {
			var url = '/';
			
			// Only include the minimum possible number of components in
			// the URL
			if( indexh > 0 || indexv > 0 ) url += indexh;
			if( indexv > 0 ) url += '/' + indexv;
			
			window.location.hash = url;

            //logSlide(window.location.href + url); // HACK
		}
	}

	/**
	 * Dispatches an event of the specified type from the 
	 * reveal DOM element.
	 */
	function dispatchEvent( type, properties ) {
		var event = document.createEvent( "HTMLEvents", 1, 2 );
		event.initEvent( type, true, true );
		extend( event, properties );
		dom.wrapper.dispatchEvent( event );
	}

	/**
	 * Navigate to the next slide fragment.
	 * 
	 * @return {Boolean} true if there was a next fragment,
	 * false otherwise
	 */
	function nextFragment() {
		// Vertical slides:
		if( document.querySelector( VERTICAL_SLIDES_SELECTOR + '.present' ) ) {
			var verticalFragments = document.querySelectorAll( VERTICAL_SLIDES_SELECTOR + '.present .fragment:not(.visible)' );
			if( verticalFragments.length ) {
				verticalFragments[0].classList.add( 'visible' );

				// Notify subscribers of the change
				dispatchEvent( 'fragmentshown', { fragment: verticalFragments[0] } );
				return true;
			}
		}
		// Horizontal slides:
		else {
			var horizontalFragments = document.querySelectorAll( HORIZONTAL_SLIDES_SELECTOR + '.present .fragment:not(.visible)' );
			if( horizontalFragments.length ) {
				horizontalFragments[0].classList.add( 'visible' );

				// Notify subscribers of the change
				dispatchEvent( 'fragmentshown', { fragment: horizontalFragments[0] } );
				return true;
			}
		}

		return false;
	}

	/**
	 * Navigate to the previous slide fragment.
	 * 
	 * @return {Boolean} true if there was a previous fragment,
	 * false otherwise
	 */
	function previousFragment() {
		// Vertical slides:
		if( document.querySelector( VERTICAL_SLIDES_SELECTOR + '.present' ) ) {
			var verticalFragments = document.querySelectorAll( VERTICAL_SLIDES_SELECTOR + '.present .fragment.visible' );
			if( verticalFragments.length ) {
				verticalFragments[ verticalFragments.length - 1 ].classList.remove( 'visible' );

				// Notify subscribers of the change
				dispatchEvent( 'fragmenthidden', { fragment: verticalFragments[ verticalFragments.length - 1 ] } );
				return true;
			}
		}
		// Horizontal slides:
		else {
			var horizontalFragments = document.querySelectorAll( HORIZONTAL_SLIDES_SELECTOR + '.present .fragment.visible' );
			if( horizontalFragments.length ) {
				horizontalFragments[ horizontalFragments.length - 1 ].classList.remove( 'visible' );

				// Notify subscribers of the change
				dispatchEvent( 'fragmenthidden', { fragment: horizontalFragments[ horizontalFragments.length - 1 ] } );
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Triggers a navigation to the specified indices.
	 * 
	 * @param {Number} h The horizontal index of the slide to show
	 * @param {Number} v The vertical index of the slide to show
	 */
	function navigateTo( h, v ) {
		slide( h, v );
	}
	
	function navigateLeft() {
		// Prioritize hiding fragments
		if( overviewIsActive() || previousFragment() === false ) {
			slide( indexh - 1, 0 );
		}
	}
	function navigateRight() {
		// Prioritize revealing fragments
		if( overviewIsActive() || nextFragment() === false ) {
			slide( indexh + 1, 0 );
		}
	}
	function navigateUp() {
		// Prioritize hiding fragments
		if( overviewIsActive() || previousFragment() === false ) {
			slide( indexh, indexv - 1 );
		}
	}
	function navigateDown() {
		// Prioritize revealing fragments
		if( overviewIsActive() || nextFragment() === false ) {
			slide( indexh, indexv + 1 );
		}
	}

	/**
	 * Navigates backwards, prioritized in the following order:
	 * 1) Previous fragment
	 * 2) Previous vertical slide
	 * 3) Previous horizontal slide
	 */
	function navigatePrev() {
		// Prioritize revealing fragments
		if( previousFragment() === false ) {
			if( availableRoutes().up ) {
				navigateUp();
			}
			else {
				// Fetch the previous horizontal slide, if there is one
				var previousSlide = document.querySelector( '.reveal .slides>section.past:nth-child(' + indexh + ')' );

				if( previousSlide ) {
					indexv = ( previousSlide.querySelectorAll('section').length + 1 ) || 0;
					indexh --;
					slide();
				}
			}
		}
	}

	/**
	 * Same as #navigatePrev() but navigates forwards.
	 */
	function navigateNext() {
		// Prioritize revealing fragments
		if( nextFragment() === false ) {
			availableRoutes().down ? navigateDown() : navigateRight();
		}
	}

	/**
	 * Toggles the slide overview mode on and off.
	 */
    /*
	function toggleOverview() {
		if( overviewIsActive() ) {
			deactivateOverview();
		}
		else {
			activateOverview();
		}
	}
	*/
	
	// Expose some methods publicly
	return {
		initialize: initialize,
		navigateTo: navigateTo,
		navigateLeft: navigateLeft,
		navigateRight: navigateRight,
		navigateUp: navigateUp,
		navigateDown: navigateDown,
		navigatePrev: navigatePrev,
		navigateNext: navigateNext,
		//toggleOverview: toggleOverview,

		addEventListeners: addEventListeners,
		removeEventListeners: removeEventListeners,

		// Forward event binding to the reveal DOM element
		addEventListener: function( type, listener, useCapture ) {
			( dom.wrapper || document.querySelector( '.reveal' ) ).addEventListener( type, listener, useCapture );
		},
		removeEventListener: function( type, listener, useCapture ) {
			( dom.wrapper || document.querySelector( '.reveal' ) ).removeEventListener( type, listener, useCapture );
		}
	};
	
})();

define("lib/reveal", ["app/slide/log"], (function (global) {
    return function () {
        return global.Reveal;
    }
}(this)));


define('app/slide/progress',["jquery"], function($) {
  window.resetProgress = function() {
    var key;
    if (confirm('Reset progress - are you sure?')) {
      for (key in localStorage) {
        if (_.str.contains(key, "progress.")) {
          localStorage.removeItem(key);
        }
      }
      return document.location.reload();
    }
  };
  window.initProgress = function() {
    return $(".slidedecks .status").each(function(idx, input) {
      var key;
      $(input).click(updateProgress);
      key = getProgressKey(input);
      if (localStorage[key]) {
        return setProgress(input, true);
      }
    });
  };
  window.updateProgress = function(evt) {
    if ($(this).is(':checked')) {
      return setProgress($(this), true);
    } else {
      return setProgress($(this), void 0);
    }
  };
  window.setProgress = function(input, val) {
    var key, li;
    key = getProgressKey(input);
    li = $(input).parent().parent();
    $(li).removeClass("done");
    localStorage.removeItem(key);
    $(input).attr("checked", false);
    if (val) {
      $(li).addClass("done");
      localStorage[key] = true;
      return $(input).attr("checked", true);
    }
  };
  return window.getProgressKey = function(input) {
    return "progress." + $(input).data("key");
  };
});

// moment.js
// version : 1.7.0
// author : Tim Wood
// license : MIT
// momentjs.com
(function (Date, undefined) {

    /************************************
        Constants
    ************************************/

    var moment,
        VERSION = "1.7.0",
        round = Math.round, i,
        // internal storage for language config files
        languages = {},
        currentLanguage = 'en',

        // check for nodeJS
        hasModule = (typeof module !== 'undefined' && module.exports),

        // Parameters to check for on the lang config.  This list of properties
        // will be inherited from English if not provided in a language
        // definition.  monthsParse is also a lang config property, but it
        // cannot be inherited and as such cannot be enumerated here.
        langConfigProperties = 'months|monthsShort|weekdays|weekdaysShort|weekdaysMin|longDateFormat|calendar|relativeTime|ordinal|meridiem'.split('|'),

        // ASP.NET json date format regex
        aspNetJsonRegex = /^\/?Date\((\-?\d+)/i,

        // format tokens
        formattingTokens = /(\[[^\[]*\])|(\\)?(Mo|MM?M?M?|Do|DDDo|DD?D?D?|ddd?d?|do?|w[o|w]?|YYYY|YY|a|A|hh?|HH?|mm?|ss?|SS?S?|zz?|ZZ?)/g,
        localFormattingTokens = /(LT|LL?L?L?)/g,
        formattingRemoveEscapes = /(^\[)|(\\)|\]$/g,

        // parsing tokens
        parseMultipleFormatChunker = /([0-9a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)/gi,

        // parsing token regexes
        parseTokenOneOrTwoDigits = /\d\d?/, // 0 - 99
        parseTokenOneToThreeDigits = /\d{1,3}/, // 0 - 999
        parseTokenThreeDigits = /\d{3}/, // 000 - 999
        parseTokenFourDigits = /\d{1,4}/, // 0 - 9999
        parseTokenWord = /[0-9a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+/i, // any word characters or numbers
        parseTokenTimezone = /Z|[\+\-]\d\d:?\d\d/i, // +00:00 -00:00 +0000 -0000 or Z
        parseTokenT = /T/i, // T (ISO seperator)

        // preliminary iso regex
        // 0000-00-00 + T + 00 or 00:00 or 00:00:00 or 00:00:00.000 + +00:00 or +0000
        isoRegex = /^\s*\d{4}-\d\d-\d\d(T(\d\d(:\d\d(:\d\d(\.\d\d?\d?)?)?)?)?([\+\-]\d\d:?\d\d)?)?/,
        isoFormat = 'YYYY-MM-DDTHH:mm:ssZ',

        // iso time formats and regexes
        isoTimes = [
            ['HH:mm:ss.S', /T\d\d:\d\d:\d\d\.\d{1,3}/],
            ['HH:mm:ss', /T\d\d:\d\d:\d\d/],
            ['HH:mm', /T\d\d:\d\d/],
            ['HH', /T\d\d/]
        ],

        // timezone chunker "+10:00" > ["10", "00"] or "-1530" > ["-15", "30"]
        parseTimezoneChunker = /([\+\-]|\d\d)/gi,

        // getter and setter names
        proxyGettersAndSetters = 'Month|Date|Hours|Minutes|Seconds|Milliseconds'.split('|'),
        unitMillisecondFactors = {
            'Milliseconds' : 1,
            'Seconds' : 1e3,
            'Minutes' : 6e4,
            'Hours' : 36e5,
            'Days' : 864e5,
            'Months' : 2592e6,
            'Years' : 31536e6
        },

        // format function strings
        formatFunctions = {},

        /*
         * moment.fn.format uses new Function() to create an inlined formatting function.
         * Results are a 3x speed boost
         * http://jsperf.com/momentjs-cached-format-functions
         *
         * These strings are appended into a function using replaceFormatTokens and makeFormatFunction
         */
        formatFunctionStrings = {
            // a = placeholder
            // b = placeholder
            // t = the current moment being formatted
            // v = getValueAtKey function
            // o = language.ordinal function
            // p = leftZeroFill function
            // m = language.meridiem value or function
            M    : '(a=t.month()+1)',
            MMM  : 'v("monthsShort",t.month())',
            MMMM : 'v("months",t.month())',
            D    : '(a=t.date())',
            DDD  : '(a=new Date(t.year(),t.month(),t.date()),b=new Date(t.year(),0,1),a=~~(((a-b)/864e5)+1.5))',
            d    : '(a=t.day())',
            dd   : 'v("weekdaysMin",t.day())',
            ddd  : 'v("weekdaysShort",t.day())',
            dddd : 'v("weekdays",t.day())',
            w    : '(a=new Date(t.year(),t.month(),t.date()-t.day()+5),b=new Date(a.getFullYear(),0,4),a=~~((a-b)/864e5/7+1.5))',
            YY   : 'p(t.year()%100,2)',
            YYYY : 'p(t.year(),4)',
            a    : 'm(t.hours(),t.minutes(),!0)',
            A    : 'm(t.hours(),t.minutes(),!1)',
            H    : 't.hours()',
            h    : 't.hours()%12||12',
            m    : 't.minutes()',
            s    : 't.seconds()',
            S    : '~~(t.milliseconds()/100)',
            SS   : 'p(~~(t.milliseconds()/10),2)',
            SSS  : 'p(t.milliseconds(),3)',
            Z    : '((a=-t.zone())<0?((a=-a),"-"):"+")+p(~~(a/60),2)+":"+p(~~a%60,2)',
            ZZ   : '((a=-t.zone())<0?((a=-a),"-"):"+")+p(~~(10*a/6),4)'
        },

        ordinalizeTokens = 'DDD w M D d'.split(' '),
        paddedTokens = 'M D H h m s w'.split(' ');

    while (ordinalizeTokens.length) {
        i = ordinalizeTokens.pop();
        formatFunctionStrings[i + 'o'] = formatFunctionStrings[i] + '+o(a)';
    }
    while (paddedTokens.length) {
        i = paddedTokens.pop();
        formatFunctionStrings[i + i] = 'p(' + formatFunctionStrings[i] + ',2)';
    }
    formatFunctionStrings.DDDD = 'p(' + formatFunctionStrings.DDD + ',3)';


    /************************************
        Constructors
    ************************************/


    // Moment prototype object
    function Moment(date, isUTC, lang) {
        this._d = date;
        this._isUTC = !!isUTC;
        this._a = date._a || null;
        date._a = null;
        this._lang = lang || false;
    }

    // Duration Constructor
    function Duration(duration) {
        var data = this._data = {},
            years = duration.years || duration.y || 0,
            months = duration.months || duration.M || 0,
            weeks = duration.weeks || duration.w || 0,
            days = duration.days || duration.d || 0,
            hours = duration.hours || duration.h || 0,
            minutes = duration.minutes || duration.m || 0,
            seconds = duration.seconds || duration.s || 0,
            milliseconds = duration.milliseconds || duration.ms || 0;

        // representation for dateAddRemove
        this._milliseconds = milliseconds +
            seconds * 1e3 + // 1000
            minutes * 6e4 + // 1000 * 60
            hours * 36e5; // 1000 * 60 * 60
        // Because of dateAddRemove treats 24 hours as different from a
        // day when working around DST, we need to store them separately
        this._days = days +
            weeks * 7;
        // It is impossible translate months into days without knowing
        // which months you are are talking about, so we have to store
        // it separately.
        this._months = months +
            years * 12;

        // The following code bubbles up values, see the tests for
        // examples of what that means.
        data.milliseconds = milliseconds % 1000;
        seconds += absRound(milliseconds / 1000);

        data.seconds = seconds % 60;
        minutes += absRound(seconds / 60);

        data.minutes = minutes % 60;
        hours += absRound(minutes / 60);

        data.hours = hours % 24;
        days += absRound(hours / 24);

        days += weeks * 7;
        data.days = days % 30;

        months += absRound(days / 30);

        data.months = months % 12;
        years += absRound(months / 12);

        data.years = years;

        this._lang = false;
    }


    /************************************
        Helpers
    ************************************/


    function absRound(number) {
        if (number < 0) {
            return Math.ceil(number);
        } else {
            return Math.floor(number);
        }
    }

    // left zero fill a number
    // see http://jsperf.com/left-zero-filling for performance comparison
    function leftZeroFill(number, targetLength) {
        var output = number + '';
        while (output.length < targetLength) {
            output = '0' + output;
        }
        return output;
    }

    // helper function for _.addTime and _.subtractTime
    function addOrSubtractDurationFromMoment(mom, duration, isAdding) {
        var ms = duration._milliseconds,
            d = duration._days,
            M = duration._months,
            currentDate;

        if (ms) {
            mom._d.setTime(+mom + ms * isAdding);
        }
        if (d) {
            mom.date(mom.date() + d * isAdding);
        }
        if (M) {
            currentDate = mom.date();
            mom.date(1)
                .month(mom.month() + M * isAdding)
                .date(Math.min(currentDate, mom.daysInMonth()));
        }
    }

    // check if is an array
    function isArray(input) {
        return Object.prototype.toString.call(input) === '[object Array]';
    }

    // compare two arrays, return the number of differences
    function compareArrays(array1, array2) {
        var len = Math.min(array1.length, array2.length),
            lengthDiff = Math.abs(array1.length - array2.length),
            diffs = 0,
            i;
        for (i = 0; i < len; i++) {
            if (~~array1[i] !== ~~array2[i]) {
                diffs++;
            }
        }
        return diffs + lengthDiff;
    }

    // convert an array to a date.
    // the array should mirror the parameters below
    // note: all values past the year are optional and will default to the lowest possible value.
    // [year, month, day , hour, minute, second, millisecond]
    function dateFromArray(input, asUTC) {
        var i, date;
        for (i = 1; i < 7; i++) {
            input[i] = (input[i] == null) ? (i === 2 ? 1 : 0) : input[i];
        }
        // we store whether we used utc or not in the input array
        input[7] = asUTC;
        date = new Date(0);
        if (asUTC) {
            date.setUTCFullYear(input[0], input[1], input[2]);
            date.setUTCHours(input[3], input[4], input[5], input[6]);
        } else {
            date.setFullYear(input[0], input[1], input[2]);
            date.setHours(input[3], input[4], input[5], input[6]);
        }
        date._a = input;
        return date;
    }

    // Loads a language definition into the `languages` cache.  The function
    // takes a key and optionally values.  If not in the browser and no values
    // are provided, it will load the language file module.  As a convenience,
    // this function also returns the language values.
    function loadLang(key, values) {
        var i, m,
            parse = [];

        if (!values && hasModule) {
            values = require('./lang/' + key);
        }

        for (i = 0; i < langConfigProperties.length; i++) {
            // If a language definition does not provide a value, inherit
            // from English
            values[langConfigProperties[i]] = values[langConfigProperties[i]] ||
              languages.en[langConfigProperties[i]];
        }

        for (i = 0; i < 12; i++) {
            m = moment([2000, i]);
            parse[i] = new RegExp('^' + (values.months[i] || values.months(m, '')) +
                '|^' + (values.monthsShort[i] || values.monthsShort(m, '')).replace('.', ''), 'i');
        }
        values.monthsParse = values.monthsParse || parse;

        languages[key] = values;

        return values;
    }

    // Determines which language definition to use and returns it.
    //
    // With no parameters, it will return the global language.  If you
    // pass in a language key, such as 'en', it will return the
    // definition for 'en', so long as 'en' has already been loaded using
    // moment.lang.  If you pass in a moment or duration instance, it
    // will decide the language based on that, or default to the global
    // language.
    function getLangDefinition(m) {
        var langKey = (typeof m === 'string') && m ||
                      m && m._lang ||
                      null;

        return langKey ? (languages[langKey] || loadLang(langKey)) : moment;
    }


    /************************************
        Formatting
    ************************************/


    // helper for building inline formatting functions
    function replaceFormatTokens(token) {
        return formatFunctionStrings[token] ?
            ("'+(" + formatFunctionStrings[token] + ")+'") :
            token.replace(formattingRemoveEscapes, "").replace(/\\?'/g, "\\'");
    }

    // helper for recursing long date formatting tokens
    function replaceLongDateFormatTokens(input) {
        return getLangDefinition().longDateFormat[input] || input;
    }

    function makeFormatFunction(format) {
        var output = "var a,b;return '" +
            format.replace(formattingTokens, replaceFormatTokens) + "';",
            Fn = Function; // get around jshint
        // t = the current moment being formatted
        // v = getValueAtKey function
        // o = language.ordinal function
        // p = leftZeroFill function
        // m = language.meridiem value or function
        return new Fn('t', 'v', 'o', 'p', 'm', output);
    }

    function makeOrGetFormatFunction(format) {
        if (!formatFunctions[format]) {
            formatFunctions[format] = makeFormatFunction(format);
        }
        return formatFunctions[format];
    }

    // format date using native date object
    function formatMoment(m, format) {
        var lang = getLangDefinition(m);

        function getValueFromArray(key, index) {
            return lang[key].call ? lang[key](m, format) : lang[key][index];
        }

        while (localFormattingTokens.test(format)) {
            format = format.replace(localFormattingTokens, replaceLongDateFormatTokens);
        }

        if (!formatFunctions[format]) {
            formatFunctions[format] = makeFormatFunction(format);
        }

        return formatFunctions[format](m, getValueFromArray, lang.ordinal, leftZeroFill, lang.meridiem);
    }


    /************************************
        Parsing
    ************************************/


    // get the regex to find the next token
    function getParseRegexForToken(token) {
        switch (token) {
        case 'DDDD':
            return parseTokenThreeDigits;
        case 'YYYY':
            return parseTokenFourDigits;
        case 'S':
        case 'SS':
        case 'SSS':
        case 'DDD':
            return parseTokenOneToThreeDigits;
        case 'MMM':
        case 'MMMM':
        case 'dd':
        case 'ddd':
        case 'dddd':
        case 'a':
        case 'A':
            return parseTokenWord;
        case 'Z':
        case 'ZZ':
            return parseTokenTimezone;
        case 'T':
            return parseTokenT;
        case 'MM':
        case 'DD':
        case 'YY':
        case 'HH':
        case 'hh':
        case 'mm':
        case 'ss':
        case 'M':
        case 'D':
        case 'd':
        case 'H':
        case 'h':
        case 'm':
        case 's':
            return parseTokenOneOrTwoDigits;
        default :
            return new RegExp(token.replace('\\', ''));
        }
    }

    // function to convert string input to date
    function addTimeToArrayFromToken(token, input, datePartArray, config) {
        var a;
        //console.log('addTime', format, input);
        switch (token) {
        // MONTH
        case 'M' : // fall through to MM
        case 'MM' :
            datePartArray[1] = (input == null) ? 0 : ~~input - 1;
            break;
        case 'MMM' : // fall through to MMMM
        case 'MMMM' :
            for (a = 0; a < 12; a++) {
                if (getLangDefinition().monthsParse[a].test(input)) {
                    datePartArray[1] = a;
                    break;
                }
            }
            break;
        // DAY OF MONTH
        case 'D' : // fall through to DDDD
        case 'DD' : // fall through to DDDD
        case 'DDD' : // fall through to DDDD
        case 'DDDD' :
            if (input != null) {
                datePartArray[2] = ~~input;
            }
            break;
        // YEAR
        case 'YY' :
            input = ~~input;
            datePartArray[0] = input + (input > 70 ? 1900 : 2000);
            break;
        case 'YYYY' :
            datePartArray[0] = ~~Math.abs(input);
            break;
        // AM / PM
        case 'a' : // fall through to A
        case 'A' :
            config.isPm = ((input + '').toLowerCase() === 'pm');
            break;
        // 24 HOUR
        case 'H' : // fall through to hh
        case 'HH' : // fall through to hh
        case 'h' : // fall through to hh
        case 'hh' :
            datePartArray[3] = ~~input;
            break;
        // MINUTE
        case 'm' : // fall through to mm
        case 'mm' :
            datePartArray[4] = ~~input;
            break;
        // SECOND
        case 's' : // fall through to ss
        case 'ss' :
            datePartArray[5] = ~~input;
            break;
        // MILLISECOND
        case 'S' :
        case 'SS' :
        case 'SSS' :
            datePartArray[6] = ~~ (('0.' + input) * 1000);
            break;
        // TIMEZONE
        case 'Z' : // fall through to ZZ
        case 'ZZ' :
            config.isUTC = true;
            a = (input + '').match(parseTimezoneChunker);
            if (a && a[1]) {
                config.tzh = ~~a[1];
            }
            if (a && a[2]) {
                config.tzm = ~~a[2];
            }
            // reverse offsets
            if (a && a[0] === '+') {
                config.tzh = -config.tzh;
                config.tzm = -config.tzm;
            }
            break;
        }
    }

    // date from string and format string
    function makeDateFromStringAndFormat(string, format) {
        var datePartArray = [0, 0, 1, 0, 0, 0, 0],
            config = {
                tzh : 0, // timezone hour offset
                tzm : 0  // timezone minute offset
            },
            tokens = format.match(formattingTokens),
            i, parsedInput;

        for (i = 0; i < tokens.length; i++) {
            parsedInput = (getParseRegexForToken(tokens[i]).exec(string) || [])[0];
            string = string.replace(getParseRegexForToken(tokens[i]), '');
            addTimeToArrayFromToken(tokens[i], parsedInput, datePartArray, config);
        }
        // handle am pm
        if (config.isPm && datePartArray[3] < 12) {
            datePartArray[3] += 12;
        }
        // if is 12 am, change hours to 0
        if (config.isPm === false && datePartArray[3] === 12) {
            datePartArray[3] = 0;
        }
        // handle timezone
        datePartArray[3] += config.tzh;
        datePartArray[4] += config.tzm;
        // return
        return dateFromArray(datePartArray, config.isUTC);
    }

    // date from string and array of format strings
    function makeDateFromStringAndArray(string, formats) {
        var output,
            inputParts = string.match(parseMultipleFormatChunker) || [],
            formattedInputParts,
            scoreToBeat = 99,
            i,
            currentDate,
            currentScore;
        for (i = 0; i < formats.length; i++) {
            currentDate = makeDateFromStringAndFormat(string, formats[i]);
            formattedInputParts = formatMoment(new Moment(currentDate), formats[i]).match(parseMultipleFormatChunker) || [];
            currentScore = compareArrays(inputParts, formattedInputParts);
            if (currentScore < scoreToBeat) {
                scoreToBeat = currentScore;
                output = currentDate;
            }
        }
        return output;
    }

    // date from iso format
    function makeDateFromString(string) {
        var format = 'YYYY-MM-DDT',
            i;
        if (isoRegex.exec(string)) {
            for (i = 0; i < 4; i++) {
                if (isoTimes[i][1].exec(string)) {
                    format += isoTimes[i][0];
                    break;
                }
            }
            return parseTokenTimezone.exec(string) ?
                makeDateFromStringAndFormat(string, format + ' Z') :
                makeDateFromStringAndFormat(string, format);
        }
        return new Date(string);
    }


    /************************************
        Relative Time
    ************************************/


    // helper function for moment.fn.from, moment.fn.fromNow, and moment.duration.fn.humanize
    function substituteTimeAgo(string, number, withoutSuffix, isFuture, lang) {
        var rt = lang.relativeTime[string];
        return (typeof rt === 'function') ?
            rt(number || 1, !!withoutSuffix, string, isFuture) :
            rt.replace(/%d/i, number || 1);
    }

    function relativeTime(milliseconds, withoutSuffix, lang) {
        var seconds = round(Math.abs(milliseconds) / 1000),
            minutes = round(seconds / 60),
            hours = round(minutes / 60),
            days = round(hours / 24),
            years = round(days / 365),
            args = seconds < 45 && ['s', seconds] ||
                minutes === 1 && ['m'] ||
                minutes < 45 && ['mm', minutes] ||
                hours === 1 && ['h'] ||
                hours < 22 && ['hh', hours] ||
                days === 1 && ['d'] ||
                days <= 25 && ['dd', days] ||
                days <= 45 && ['M'] ||
                days < 345 && ['MM', round(days / 30)] ||
                years === 1 && ['y'] || ['yy', years];
        args[2] = withoutSuffix;
        args[3] = milliseconds > 0;
        args[4] = lang;
        return substituteTimeAgo.apply({}, args);
    }


    /************************************
        Top Level Functions
    ************************************/


    moment = function (input, format) {
        if (input === null || input === '') {
            return null;
        }
        var date,
            matched;
        // parse Moment object
        if (moment.isMoment(input)) {
            return new Moment(new Date(+input._d), input._isUTC, input._lang);
        // parse string and format
        } else if (format) {
            if (isArray(format)) {
                date = makeDateFromStringAndArray(input, format);
            } else {
                date = makeDateFromStringAndFormat(input, format);
            }
        // evaluate it as a JSON-encoded date
        } else {
            matched = aspNetJsonRegex.exec(input);
            date = input === undefined ? new Date() :
                matched ? new Date(+matched[1]) :
                input instanceof Date ? input :
                isArray(input) ? dateFromArray(input) :
                typeof input === 'string' ? makeDateFromString(input) :
                new Date(input);
        }

        return new Moment(date);
    };

    // creating with utc
    moment.utc = function (input, format) {
        if (isArray(input)) {
            return new Moment(dateFromArray(input, true), true);
        }
        // if we don't have a timezone, we need to add one to trigger parsing into utc
        if (typeof input === 'string' && !parseTokenTimezone.exec(input)) {
            input += ' +0000';
            if (format) {
                format += ' Z';
            }
        }
        return moment(input, format).utc();
    };

    // creating with unix timestamp (in seconds)
    moment.unix = function (input) {
        return moment(input * 1000);
    };

    // duration
    moment.duration = function (input, key) {
        var isDuration = moment.isDuration(input),
            isNumber = (typeof input === 'number'),
            duration = (isDuration ? input._data : (isNumber ? {} : input)),
            ret;

        if (isNumber) {
            if (key) {
                duration[key] = input;
            } else {
                duration.milliseconds = input;
            }
        }

        ret = new Duration(duration);

        if (isDuration) {
            ret._lang = input._lang;
        }

        return ret;
    };

    // humanizeDuration
    // This method is deprecated in favor of the new Duration object.  Please
    // see the moment.duration method.
    moment.humanizeDuration = function (num, type, withSuffix) {
        return moment.duration(num, type === true ? null : type).humanize(type === true ? true : withSuffix);
    };

    // version number
    moment.version = VERSION;

    // default format
    moment.defaultFormat = isoFormat;

    // This function will load languages and then set the global language.  If
    // no arguments are passed in, it will simply return the current global
    // language key.
    moment.lang = function (key, values) {
        var i;

        if (!key) {
            return currentLanguage;
        }
        if (values || !languages[key]) {
            loadLang(key, values);
        }
        if (languages[key]) {
            // deprecated, to get the language definition variables, use the
            // moment.fn.lang method or the getLangDefinition function.
            for (i = 0; i < langConfigProperties.length; i++) {
                moment[langConfigProperties[i]] = languages[key][langConfigProperties[i]];
            }
            moment.monthsParse = languages[key].monthsParse;
            currentLanguage = key;
        }
    };

    // returns language data
    moment.langData = getLangDefinition;

    // compare moment object
    moment.isMoment = function (obj) {
        return obj instanceof Moment;
    };

    // for typechecking Duration objects
    moment.isDuration = function (obj) {
        return obj instanceof Duration;
    };

    // Set default language, other languages will inherit from English.
    moment.lang('en', {
        months : "January_February_March_April_May_June_July_August_September_October_November_December".split("_"),
        monthsShort : "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_"),
        weekdays : "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),
        weekdaysShort : "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_"),
        weekdaysMin : "Su_Mo_Tu_We_Th_Fr_Sa".split("_"),
        longDateFormat : {
            LT : "h:mm A",
            L : "MM/DD/YYYY",
            LL : "MMMM D YYYY",
            LLL : "MMMM D YYYY LT",
            LLLL : "dddd, MMMM D YYYY LT"
        },
        meridiem : function (hours, minutes, isLower) {
            if (hours > 11) {
                return isLower ? 'pm' : 'PM';
            } else {
                return isLower ? 'am' : 'AM';
            }
        },
        calendar : {
            sameDay : '[Today at] LT',
            nextDay : '[Tomorrow at] LT',
            nextWeek : 'dddd [at] LT',
            lastDay : '[Yesterday at] LT',
            lastWeek : '[last] dddd [at] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : "in %s",
            past : "%s ago",
            s : "a few seconds",
            m : "a minute",
            mm : "%d minutes",
            h : "an hour",
            hh : "%d hours",
            d : "a day",
            dd : "%d days",
            M : "a month",
            MM : "%d months",
            y : "a year",
            yy : "%d years"
        },
        ordinal : function (number) {
            var b = number % 10;
            return (~~ (number % 100 / 10) === 1) ? 'th' :
                (b === 1) ? 'st' :
                (b === 2) ? 'nd' :
                (b === 3) ? 'rd' : 'th';
        }
    });


    /************************************
        Moment Prototype
    ************************************/


    moment.fn = Moment.prototype = {

        clone : function () {
            return moment(this);
        },

        valueOf : function () {
            return +this._d;
        },

        unix : function () {
            return Math.floor(+this._d / 1000);
        },

        toString : function () {
            return this._d.toString();
        },

        toDate : function () {
            return this._d;
        },

        toArray : function () {
            var m = this;
            return [
                m.year(),
                m.month(),
                m.date(),
                m.hours(),
                m.minutes(),
                m.seconds(),
                m.milliseconds(),
                !!this._isUTC
            ];
        },

        isValid : function () {
            if (this._a) {
                return !compareArrays(this._a, (this._a[7] ? moment.utc(this) : this).toArray());
            }
            return !isNaN(this._d.getTime());
        },

        utc : function () {
            this._isUTC = true;
            return this;
        },

        local : function () {
            this._isUTC = false;
            return this;
        },

        format : function (inputString) {
            return formatMoment(this, inputString ? inputString : moment.defaultFormat);
        },

        add : function (input, val) {
            var dur = val ? moment.duration(+val, input) : moment.duration(input);
            addOrSubtractDurationFromMoment(this, dur, 1);
            return this;
        },

        subtract : function (input, val) {
            var dur = val ? moment.duration(+val, input) : moment.duration(input);
            addOrSubtractDurationFromMoment(this, dur, -1);
            return this;
        },

        diff : function (input, val, asFloat) {
            var inputMoment = this._isUTC ? moment(input).utc() : moment(input).local(),
                zoneDiff = (this.zone() - inputMoment.zone()) * 6e4,
                diff = this._d - inputMoment._d - zoneDiff,
                year = this.year() - inputMoment.year(),
                month = this.month() - inputMoment.month(),
                date = this.date() - inputMoment.date(),
                output;
            if (val === 'months') {
                output = year * 12 + month + date / 30;
            } else if (val === 'years') {
                output = year + (month + date / 30) / 12;
            } else {
                output = val === 'seconds' ? diff / 1e3 : // 1000
                    val === 'minutes' ? diff / 6e4 : // 1000 * 60
                    val === 'hours' ? diff / 36e5 : // 1000 * 60 * 60
                    val === 'days' ? diff / 864e5 : // 1000 * 60 * 60 * 24
                    val === 'weeks' ? diff / 6048e5 : // 1000 * 60 * 60 * 24 * 7
                    diff;
            }
            return asFloat ? output : round(output);
        },

        from : function (time, withoutSuffix) {
            return moment.duration(this.diff(time)).lang(this._lang).humanize(!withoutSuffix);
        },

        fromNow : function (withoutSuffix) {
            return this.from(moment(), withoutSuffix);
        },

        calendar : function () {
            var diff = this.diff(moment().sod(), 'days', true),
                calendar = this.lang().calendar,
                allElse = calendar.sameElse,
                format = diff < -6 ? allElse :
                diff < -1 ? calendar.lastWeek :
                diff < 0 ? calendar.lastDay :
                diff < 1 ? calendar.sameDay :
                diff < 2 ? calendar.nextDay :
                diff < 7 ? calendar.nextWeek : allElse;
            return this.format(typeof format === 'function' ? format.apply(this) : format);
        },

        isLeapYear : function () {
            var year = this.year();
            return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
        },

        isDST : function () {
            return (this.zone() < moment([this.year()]).zone() ||
                this.zone() < moment([this.year(), 5]).zone());
        },

        day : function (input) {
            var day = this._isUTC ? this._d.getUTCDay() : this._d.getDay();
            return input == null ? day :
                this.add({ d : input - day });
        },

        startOf: function (val) {
            // the following switch intentionally omits break keywords
            // to utilize falling through the cases.
            switch (val.replace(/s$/, '')) {
            case 'year':
                this.month(0);
                /* falls through */
            case 'month':
                this.date(1);
                /* falls through */
            case 'day':
                this.hours(0);
                /* falls through */
            case 'hour':
                this.minutes(0);
                /* falls through */
            case 'minute':
                this.seconds(0);
                /* falls through */
            case 'second':
                this.milliseconds(0);
                /* falls through */
            }
            return this;
        },

        endOf: function (val) {
            return this.startOf(val).add(val.replace(/s?$/, 's'), 1).subtract('ms', 1);
        },

        sod: function () {
            return this.clone().startOf('day');
        },

        eod: function () {
            // end of day = start of day plus 1 day, minus 1 millisecond
            return this.clone().endOf('day');
        },

        zone : function () {
            return this._isUTC ? 0 : this._d.getTimezoneOffset();
        },

        daysInMonth : function () {
            return moment.utc([this.year(), this.month() + 1, 0]).date();
        },

        // If passed a language key, it will set the language for this
        // instance.  Otherwise, it will return the language configuration
        // variables for this instance.
        lang : function (lang) {
            if (lang === undefined) {
                return getLangDefinition(this);
            } else {
                this._lang = lang;
                return this;
            }
        }
    };

    // helper for adding shortcuts
    function makeGetterAndSetter(name, key) {
        moment.fn[name] = function (input) {
            var utc = this._isUTC ? 'UTC' : '';
            if (input != null) {
                this._d['set' + utc + key](input);
                return this;
            } else {
                return this._d['get' + utc + key]();
            }
        };
    }

    // loop through and add shortcuts (Month, Date, Hours, Minutes, Seconds, Milliseconds)
    for (i = 0; i < proxyGettersAndSetters.length; i ++) {
        makeGetterAndSetter(proxyGettersAndSetters[i].toLowerCase(), proxyGettersAndSetters[i]);
    }

    // add shortcut for year (uses different syntax than the getter/setter 'year' == 'FullYear')
    makeGetterAndSetter('year', 'FullYear');


    /************************************
        Duration Prototype
    ************************************/


    moment.duration.fn = Duration.prototype = {
        weeks : function () {
            return absRound(this.days() / 7);
        },

        valueOf : function () {
            return this._milliseconds +
              this._days * 864e5 +
              this._months * 2592e6;
        },

        humanize : function (withSuffix) {
            var difference = +this,
                rel = this.lang().relativeTime,
                output = relativeTime(difference, !withSuffix, this.lang());

            if (withSuffix) {
                output = (difference <= 0 ? rel.past : rel.future).replace(/%s/i, output);
            }

            return output;
        },

        lang : moment.fn.lang
    };

    function makeDurationGetter(name) {
        moment.duration.fn[name] = function () {
            return this._data[name];
        };
    }

    function makeDurationAsGetter(name, factor) {
        moment.duration.fn['as' + name] = function () {
            return +this / factor;
        };
    }

    for (i in unitMillisecondFactors) {
        if (unitMillisecondFactors.hasOwnProperty(i)) {
            makeDurationAsGetter(i, unitMillisecondFactors[i]);
            makeDurationGetter(i.toLowerCase());
        }
    }

    makeDurationAsGetter('Weeks', 6048e5);


    /************************************
        Exposing Moment
    ************************************/


    // CommonJS module is defined
    if (hasModule) {
        module.exports = moment;
    }
    /*global ender:false */
    if (typeof ender === 'undefined') {
        // here, `this` means `window` in the browser, or `global` on the server
        // add `moment` as a global object via a string identifier,
        // for Closure Compiler "advanced" mode
        this['moment'] = moment;
    }
    /*global define:false */
    if (typeof define === "function" && define.amd) {
        define("moment", [], function () {
            return moment;
        });
    }
}).call(this, Date);
define("lib/util/moment", (function (global) {
    return function () {
        return global.moment;
    }
}(this)));


define('app/slide/time',["lib/util/moment"], function(moment) {
  var Time;
  Time = (function() {
    var addTime, getTime, setTime;

    function Time() {}

    setTime = function(key, s) {
      if (s <= 0) {
        localStorage[key + ".start"] = void 0;
        return localStorage[key + ".end"] = void 0;
      } else {
        localStorage[key + ".start"] = moment().toDate();
        return localStorage[key + ".end"] = moment().add('seconds', s).toDate();
      }
    };

    addTime = function(key, s) {
      var end;
      end = localStorage[key + ".end"];
      if (end) {
        return localStorage[key + ".end"] = moment(end).add('seconds', s).toDate();
      }
    };

    getTime = function(key) {
      return [localStorage[key + ".start"], localStorage[key + ".end"]];
    };

    return Time;

  })();
  return new Time;
});

/*
Easy pie chart is a jquery plugin to display simple animated pie charts for only one value

Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.

Built on top of the jQuery library (http://jquery.com)

@source: http://github.com/rendro/easy-pie-chart/
@autor: Robert Fleischmann
@version: 1.0.1

Inspired by: http://dribbble.com/shots/631074-Simple-Pie-Charts-II?list=popular&offset=210
Thanks to Philip Thrasher for the jquery plugin boilerplate for coffee script
*/
(function() {

  (function($) {
    $.easyPieChart = function(el, options) {
      var addScaleLine, animateLine, drawLine, easeInOutQuad, renderBackground, renderScale, renderTrack,
        _this = this;
      this.el = el;
      this.$el = $(el);
      this.$el.data("easyPieChart", this);
      this.init = function() {
        var percent;
        _this.options = $.extend({}, $.easyPieChart.defaultOptions, options);
        percent = parseInt(_this.$el.data('percent'), 10);
        _this.percentage = 0;
        _this.canvas = $("<canvas width='" + _this.options.size + "' height='" + _this.options.size + "'></canvas>").get(0);
        _this.$el.append(_this.canvas);
        if (typeof G_vmlCanvasManager !== "undefined" && G_vmlCanvasManager !== null) {
          G_vmlCanvasManager.initElement(_this.canvas);
        }
        _this.ctx = _this.canvas.getContext('2d');
        _this.ctx.translate(_this.options.size / 2, _this.options.size / 2);
        _this.$el.addClass('easyPieChart');
        _this.$el.css({
          width: _this.options.size,
          height: _this.options.size,
          lineHeight: "" + _this.options.size + "px"
        });
        _this.update(percent);
        return _this;
      };
      this.update = function(percent) {
        if (_this.options.animate === false) {
          return drawLine(percent);
        } else {
          return animateLine(_this.percentage, percent);
        }
      };
      renderScale = function() {
        var i, _i, _results;
        _this.ctx.fillStyle = _this.options.scaleColor;
        _this.ctx.lineWidth = 1;
        _results = [];
        for (i = _i = 0; _i <= 24; i = ++_i) {
          _results.push(addScaleLine(i));
        }
        return _results;
      };
      addScaleLine = function(i) {
        var offset;
        offset = i % 6 === 0 ? 0 : _this.options.size * 0.017;
        _this.ctx.save();
        _this.ctx.rotate(i * Math.PI / 12);
        _this.ctx.fillRect(_this.options.size / 2 - offset, 0, -_this.options.size * 0.05 + offset, 1);
        return _this.ctx.restore();
      };
      renderTrack = function() {
        var offset;
        offset = _this.options.size / 2 - _this.options.lineWidth / 2;
        if (_this.options.scaleColor !== false) {
          offset -= _this.options.size * 0.08;
        }
        _this.ctx.beginPath();
        _this.ctx.arc(0, 0, offset, 0, Math.PI * 2, true);
        _this.ctx.closePath();
        _this.ctx.strokeStyle = _this.options.trackColor;
        _this.ctx.lineWidth = _this.options.lineWidth;
        return _this.ctx.stroke();
      };
      renderBackground = function() {
        if (_this.options.scaleColor !== false) {
          renderScale();
        }
        if (_this.options.trackColor !== false) {
          return renderTrack();
        }
      };
      drawLine = function(percent) {
        var offset;
        renderBackground();
        _this.ctx.strokeStyle = $.isFunction(_this.options.barColor) ? _this.options.barColor(percent) : _this.options.barColor;
        _this.ctx.lineCap = _this.options.lineCap;
        offset = _this.options.size / 2 - _this.options.lineWidth / 2;
        if (_this.options.scaleColor !== false) {
          offset -= _this.options.size * 0.08;
        }
        _this.ctx.save();
        _this.ctx.rotate(-Math.PI / 2);
        _this.ctx.beginPath();
        _this.ctx.arc(0, 0, offset, 0, Math.PI * 2 * percent / 100, false);
        _this.ctx.stroke();
        return _this.ctx.restore();
      };
      animateLine = function(from, to) {
        var currentStep, fps, steps;
        fps = 30;
        steps = fps * _this.options.animate / 1000;
        currentStep = 0;
        _this.options.onStart.call(_this);
        _this.percentage = to;
        if (_this.animation) {
          clearInterval(_this.animation);
          _this.animation = false;
        }
        return _this.animation = setInterval(function() {
          _this.ctx.clearRect(-_this.options.size / 2, -_this.options.size / 2, _this.options.size, _this.options.size);
          renderBackground.call(_this);
          drawLine.call(_this, [easeInOutQuad(currentStep, from, to - from, steps)]);
          currentStep++;
          if ((currentStep / steps) > 1) {
            clearInterval(_this.animation);
            _this.animation = false;
            return _this.options.onStop.call(_this);
          }
        }, 1000 / fps);
      };
      easeInOutQuad = function(t, b, c, d) {
        t /= d / 2;
        if (t < 1) {
          return c / 2 * t * t + b;
        } else {
          return -c / 2 * ((--t) * (t - 2) - 1) + b;
        }
      };
      return this.init();
    };
    $.easyPieChart.defaultOptions = {
      barColor: '#ef1e25',
      trackColor: '#f2f2f2',
      scaleColor: '#dfe0e0',
      lineCap: 'round',
      size: 110,
      lineWidth: 3,
      animate: false,
      onStart: $.noop,
      onStop: $.noop
    };
    $.fn.easyPieChart = function(options) {
      return $.each(this, function(i, el) {
        var $el;
        $el = $(el);
        if (!$el.data('easyPieChart')) {
          return $el.data('easyPieChart', new $.easyPieChart(el, options));
        }
      });
    };
    return void 0;
  })(jQuery);

}).call(this);
define("lib/chart/piechart",[], function(){});


define('app/slide/init',["jquery", "lib/util/underscore", "lib/reveal", "app/editor/init", "app/slide/progress", "app/slide/time", "lib/chart/piechart"], function($, _, Reveal, Editor, Progress, Timer, PieChart) {
  var Slide;
  return Slide = (function() {
    var embedDocs;

    function Slide(clazz) {
      var li;
      this.initSnippets();
      if (!$("body").hasClass("light")) {
        this.subtleToolbar();
        this.embedEditor();
        this.embedDocs();
        this.embedGlossary();
        this.embedNavi();
        $("#naviLogout").remove();
        li = $("#naviSlides");
        li.parent().append(li);
        li.find(".divider").remove();
        this.initCountdowns();
        this.initTimer();
      } else {
        $(".fragment").removeClass("fragment");
        $(".slide-end").remove();
      }
    }

    Slide.prototype.embedNavi = function() {
      var naviElemLinks, subtitles, titles;
      $('<li id="naviGoTo" class="dropdown">\
           <a class="dropdown-toggle" data-toggle="dropdown" href="#naviGoTo">\
             <span>Go To</span>\
             <b class="caret"></b>\
           </a>\
           <span class="divider">|</span>\
           <ul class="dropdown-menu"></ul>\
         </li>').insertAfter($("#naviSlides"));
      titles = [];
      subtitles = [];
      naviElemLinks = $("#naviGoTo").find("ul");
      $(naviElemLinks).append('\
        <li class="main">\
          <a href="#">Start</a>\
        </li>\
      ');
      return $(".reveal section").each(function(idx, elem) {
        var subtitle, title;
        title = $(elem).data("title");
        subtitle = $(elem).data("subtitle");
        if (title && _.last(titles) !== title) {
          titles.push(title);
          $(naviElemLinks).append('\
              <li class="main">\
                <a href="#/' + idx + '">' + title + '</a>\
              </li>\
            ');
        }
        if (subtitle && _.last(subtitles) !== subtitle) {
          subtitles.push(subtitle);
          return $(naviElemLinks).append('\
              <li class="sub">\
                <a href="#/' + idx + '"> - ' + subtitle + '</a>\
              </li>\
            ');
        }
      });
    };

    Slide.prototype.embedEditor = function() {
      return $("#navi a.openEditor").bind("click", function(evt) {
        initModalEditor();
        return evt.preventDefault();
      });
    };

    Slide.prototype.embedGlossary = function() {
      return $("#naviGlossary").bind("click", function() {
        var m;
        m = $("#glossaryModal");
        m.bind("shown", function(evt) {
          var mbody;
          mbody = $(m).find(".modal-body");
          return $(mbody).html('\
            <iframe src="/app/glossary" width="100%" height="100%"\
                    style="position: absolute; top: 0; bottom: 0; left: 0; right: 0; overflow: hidden"></iframe>\
          ');
        });
        m.modal();
        return false;
      });
    };

    Slide.prototype.initSnippets = function() {
      return $('.slides div.snippet').each(function(idx, elem) {
        return Editor.initSnippet(elem);
      });
    };

    Slide.prototype.embedStyle = function() {
      $('<li id="naviStyle">\
          <a href="#" class="openStyle">\
              <span>Style</span>\
          </a>\
          <span class="divider">|</span>\
        </li>').insertAfter($("#naviDocs"));
      return $("#naviStyle").bind("click", function() {
        var m;
        m = $("#styleModal");
        m.bind("shown", function(evt) {
          var mbody;
          return mbody = $(m).find(".modal-body");
        });
        m.modal();
        return false;
      });
    };

    embedDocs = function() {
      $('<li id="naviDocs">\
            <a href="#" class="openDocs">\
                <span>Docs</span>\
            </a>\
            <span class="divider">|</span>\
        </li>').insertAfter($("#naviEditor"));
      return $("#naviDocs").bind("click", function() {
        var m;
        m = $("#docsModal");
        m.bind("shown", function(evt) {
          var mbody;
          mbody = $(m).find(".modal-body");
          if ($(mbody).is(":empty")) {
            return $(mbody).html('\
              <iframe src="http://www.scala-lang.org/api/current/index.html" width="99.5%" height="99.5%"\
                      style="position: absolute; top: 5px; bottom: 5px; left: 5px; right: 5px; overflow: hidden"></iframe>\
            ');
          }
        });
        m.modal();
        return false;
      });
    };

    Slide.prototype.initCountdowns = function() {
      return $('div.countdown').each(function(idx, elem) {
        var key, upd;
        key = "countdown_" + idx;
        upd = function() {
          var diff, end, mm, ms_end, ms_start, now, state, time;
          time = getTime(key);
          ms_end = time[1];
          ms_start = time[0];
          if (ms_start) {
            now = moment();
            end = moment(ms_end);
            state = $(elem).data("state");
            diff = state === "running" ? end.diff(now, 'seconds') : $(elem).data("start");
            if (diff >= 0) {
              $(elem).data("start", diff);
              $(elem).find(".mm").text(Math.floor(diff / 60));
              mm = diff % 60;
              console.log("diff: " + diff);
              $(elem).find(".ss").text((mm < 10 ? "0" : "") + mm);
              return state === "running";
            } else {
              $(elem).data("active", "false");
              return false;
            }
          } else {
            return false;
          }
        };
        $(elem).find(".minus").bind("click", function() {
          $(elem).data("start", $(elem).data("start") - 60);
          addTime(key, -60);
          return upd();
        });
        $(elem).find(".plus").bind("click", function() {
          $(elem).data("start", $(elem).data("start") + 60);
          addTime(key, 60);
          return upd();
        });
        return $(elem).find(".toggle").bind("click", function() {
          var state;
          state = $(elem).data("state");
          if (state === "running") {
            return $(elem).data("state", "stopped");
          } else {
            Timer.setTime(key, $(elem).data("start"));
            $(elem).data("state", "running");
            return updateClockSchedule(upd, 500);
          }
        });
      });
    };

    Slide.prototype.initTimer = function() {
      var target;
      target = $('#timer');
      $('<li class="dropdown" id="naviTimer">\
          <a class="dropdown-toggle" data-toggle="dropdown" href="#naviTimer">\
            <span>Timer</span>\
            <b class="caret"></b>\
          </a>\
          <ul class="dropdown-menu">\
            <li>\
              <a href="#" data-min="90">90m</a>\
            </li>\
            <li>\
              <a href="#" data-min="60">60m</a>\
            </li>\
            <li>\
              <a href="#" data-min="45">45m</a>\
            </li>\
            <li>\
              <a href="#" data-min="30">30m</a>\
            </li>\
            <li>\
              <a href="#" data-min="15">15m</a>\
            </li>\
            <li>\
              <a href="#" data-addmin="5">+5m</a>\
            </li>\
            <li>\
              <a href="#" data-addmin="-5">-5m</a>\
            </li>\
            <li>\
              <a href="#" data-min="-1">reset</a>\
            </li>\
            <li>\
              <a href="#" class="toggle">toggle</a>\
            </li>\
          </ul>\
          <span class="divider">|</span>\
        </li>').insertAfter($("#naviDocs"));
      $("#naviTimer ul a").bind("click", function(evt) {
        var addmin, min;
        min = $(this).data("min");
        if (min) {
          setTime("timer", min * 60);
        } else {
          addmin = $(this).data("addmin");
          if (addmin) {
            Timer.addTime("timer", addmin * 60);
          } else if ($(this).hasClass("toggle")) {
            $(target).toggle();
          }
        }
        this.updateTimer();
        return false;
      });
      target.easyPieChart({
        barColor: function(p) {
          return "#ddd";
        },
        trackColor: "#eee",
        scaleColor: false,
        animate: false,
        lineCap: 'butt',
        lineWidth: 15,
        size: 30
      });
      ({
        updateTimer: function() {
          var elapsed, end, ms_end, ms_start, now, percent, start, time, togo, total;
          time = Timer.getTime("timer");
          ms_end = time[1];
          ms_start = time[0];
          if (ms_start) {
            now = moment();
            end = moment(ms_end);
            start = moment(ms_start);
            total = end.diff(start, 'minutes');
            togo = Math.max(0, end.diff(now, 'minutes'));
            elapsed = now.diff(start, 'minutes');
            percent = Math.min(100, Math.max(1, 100 * elapsed / total));
          }
          $(target).data("easyPieChart").update(percent || 100);
          $(target).find("div").text(togo || "");
          return true;
        }
      });
      updateClockSchedule(updateTimer, 10000);
      return updateTimer();
    };

    Slide.prototype.updateClockSchedule = function(upd, t) {
      return setTimeout(function() {
        if (upd()) {
          return updateClockSchedule(upd, t);
        }
      }, t);
    };

    return Slide;

  })();
});
