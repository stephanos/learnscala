/**
 * Handles opening of and synchronization with the reveal.js
 * notes window.
 */
var RevealNotes = (function() {

	function openNotes() {
		var notesPopup = window.open( 'notes/', 'Notes', 'width=1120,height=850' );

		// Fires when slide is changed
		Reveal.addEventListener( 'slidechanged', function( event ) {
			post('slidechanged');
		} );

		// Fires when a fragment is shown
		Reveal.addEventListener( 'fragmentshown', function( event ) {
			post('fragmentshown');
		} );

		// Fires when a fragment is hidden
		Reveal.addEventListener( 'fragmenthidden', function( event ) {
			post('fragmenthidden');
		} );

		/**
		 * Posts the current slide data to the notes window
		 *
		 * @param {String} eventType Expecting 'slidechanged', 'fragmentshown'
		 * or 'fragmenthidden' set in the events above to define the needed
		 * slideDate.
		 */
		function post( eventType ) {
			var slideElement = Reveal.getCurrentSlide(),
				messageData;

			if( eventType === 'slidechanged' ) {
				var notes = slideElement.querySelector( 'aside.notes' ),
					indexh = Reveal.getIndices().h,
					indexv = Reveal.getIndices().v,
					nextindexh,
					nextindexv;

				if( slideElement.nextElementSibling && slideElement.parentNode.nodeName == 'SECTION' ) {
					nextindexh = indexh;
					nextindexv = indexv + 1;
				} else {
					nextindexh = indexh + 1;
					nextindexv = 0;
				}

				messageData = {
					notes : notes ? notes.innerHTML : '',
					indexh : indexh,
					indexv : indexv,
					nextindexh : nextindexh,
					nextindexv : nextindexv,
					markdown : notes ? typeof notes.getAttribute( 'data-markdown' ) === 'string' : false
				};
			}
			else if( eventType === 'fragmentshown' ) {
				messageData = {
					fragment : 'next'
				};
			}
			else if( eventType === 'fragmenthidden' ) {
				messageData = {
					fragment : 'prev'
				};
			}

			notesPopup.postMessage( JSON.stringify( messageData ), '*' );
		}

		// Navigate to the current slide when the notes are loaded
		notesPopup.addEventListener( 'load', function( event ) {
			post('slidechanged');
		}, false );
	}

	// Open the notes when the 's' key is hit
	document.addEventListener( 'keydown', function( event ) {
		// Disregard the event if the target is editable or a
		// modifier is present
		if ( document.querySelector( ':focus' ) !== null || event.shiftKey || event.altKey || event.ctrlKey || event.metaKey ) return;

		if( event.keyCode === 78 ) { // n
			event.preventDefault();
			openNotes();
		}
	}, false );

	return { open: openNotes };
})();