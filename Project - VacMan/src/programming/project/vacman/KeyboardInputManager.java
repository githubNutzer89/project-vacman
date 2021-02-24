package programming.project.vacman;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* File: KeyboardInputManager.java
 * -----------------------
 * This class handles keyboard inputs. Therefore it implements the {@code KeyListener}. 
 * The game is not event based and it should be possible to handle more than one pressed key at a time.
 * That's why this class uses a technique called key polling.
 */
public class KeyboardInputManager implements KeyListener {

	//Number of keys to be checked
	private static final int KEY_COUNT = 256;

	//The possible key states
	private enum KeyState {
		RELEASED, 			// Not pressed
		PRESSED,  			// Pressed and has been pressed
		PRESSED_ONCE    	// Pressed for the first time
	}

	// Current state of the keyboard
	private boolean[] currentPressedKeys = null;

	// Polled keyboard state
	private KeyState[] polledKeys = null;

	/**
	 * Initializes a newly created KeyboardInput object.  
	 * 
	 */
	public KeyboardInputManager() {
		//Initialize arrays currentKeys and keys
		currentPressedKeys = new boolean[ KEY_COUNT ];
		polledKeys = new KeyState[ KEY_COUNT ];
		
		//Set all fields in keys[] to KeyState.RELEASE
		for( int i = 0; i < KEY_COUNT; ++i ) {
			polledKeys[ i ] = KeyState.RELEASED;
		}
	}

	/*
	 * Compares the currently pressed keys with the keys pressed one frame before and sets the state appropriately.
	 */
	public synchronized void poll() {
		for( int i = 0; i < KEY_COUNT; ++i ) {
			//Set the state for each key 
			if( currentPressedKeys[ i ] ) {
				//The key is pressed
				if( polledKeys[ i ] == KeyState.RELEASED )
					//If it wasn't on the last frame set it to ONCE...
					polledKeys[ i ] = KeyState.PRESSED_ONCE;
				else
					//...otherwise to PRESSED
					polledKeys[ i ] = KeyState.PRESSED;
			} else {
				//The key is not pressed
				polledKeys[ i ] = KeyState.RELEASED;
			}
		}
	}

	/*
	 * Checks for a given {@code keyCode} the state of this key.
	 * 
	 * @return If the given key is in state {@code PRESSED} or {@code ONCE} it returns true otherwise false.
	 */
	public boolean keyPressed( int keyCode ) {
		return polledKeys[ keyCode ] == KeyState.PRESSED_ONCE ||
				polledKeys[ keyCode ] == KeyState.PRESSED;
	}

	/*
	 * Checks for a given {@code keyCode} the state of this key.
	 * 
	 * @return If the given key is in state {@code ONCE} it returns true otherwise false.
	 */
	public boolean keyPressedOnce( int keyCode ) {
		return polledKeys[ keyCode ] == KeyState.PRESSED_ONCE;
	}

	/*
	 * Checks for a given {@code keyCode} the state of this key.
	 * 
	 * @return If the given key is in state {@code RELEASED} it returns true otherwise false.
	 */
	public boolean keyReleased( int keyCode ) {
		return polledKeys[ keyCode ] == KeyState.RELEASED;
	}

	/*
	 * Called by the system. Updates the {@code currentKey[]} array for the key-code of the pressed key.
	 */
	@Override
	public synchronized void keyPressed( KeyEvent e ) {
		int keyCode = e.getKeyCode();
		
		if( keyCode >= 0 && keyCode < KEY_COUNT ) {
			currentPressedKeys[ keyCode ] = true;
		}
	}

	/*
	 * Called by the system. Updates the {@code currentKey[]} array for the key-code of the released key.
	 */
	@Override
	public synchronized void keyReleased( KeyEvent e ) {
		int keyCode = e.getKeyCode();
		
		if( keyCode >= 0 && keyCode < KEY_COUNT ) {
			currentPressedKeys[ keyCode ] = false;
		}
	}

	/*
	 * Not implemented.
	 */
	@Override
	public void keyTyped( KeyEvent e ) {
		// Not required
	}
}