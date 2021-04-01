// USERNAME:
// FULL NAME:

// this makes the browser catch a LOT more runtime errors. leave it here!
"use strict";

// arr.removeItem(obj) finds and removes obj from arr.
Array.prototype.removeItem = function(item) {
	let i = this.indexOf(item);

	if(i > -1) {
		this.splice(i, 1);
		return true;
	}

	return false;
}

// gets a random int in the range [min, max) (lower bound inclusive, upper bound exclusive)
function getRandomInt(min, max) {
	min = Math.ceil(min);
	max = Math.floor(max);
	return Math.floor(Math.random() * (max - min)) + min;
}

// ------------------------------------------------------------------------------------
// Add your code below!
// ------------------------------------------------------------------------------------

// constants for you.
const IMG_W = 120;    // width of the mosquito image
const IMG_H = 88;     // height of the mosquito image
const SCREEN_W = 640; // width of the playfield area
const SCREEN_H = 480; // height of the playfield area

// global variables. add more here as you need them.
let gameMessage

window.onload = function() {
	// here is where you put setup code.

	// this way, we can write gameMessage.innerHTML or whatever in your code.
	gameMessage = document.getElementById('gameMessage')
};

// given a side (0, 1, 2, 3 = T, R, B, L), returns a 2-item array containing the x and y
// coordinates of a point off the edge of the screen on that side.
function randomPointOnSide(side) {
	switch(side) {
		/* top    */ case 0: return [getRandomInt(0, SCREEN_W - IMG_W), -IMG_H];
		/* right  */ case 1: return [SCREEN_W, getRandomInt(0, SCREEN_H - IMG_H)];
		/* bottom */ case 2: return [getRandomInt(0, SCREEN_W - IMG_W), SCREEN_H];
		/* left   */ case 3: return [-IMG_W, getRandomInt(0, SCREEN_H - IMG_H)];
	}
}

// returns a 4-item array containing the x, y, x direction, and y direction of a mosquito.
// use it like:
// let [x, y, vx, vy] = pickPointAndVector()
// then you can multiply vx and vy by some number to change the speed.
function pickPointAndVector() {
	let side = getRandomInt(0, 4);                    // pick a side...
	let [x, y] = randomPointOnSide(side);             // pick where to place it...
	let [tx, ty] = randomPointOnSide((side + 2) % 4); // pick a point on the opposite side...
	let [dx, dy] = [tx - x, ty - y];                  // find the vector to that other point...
	let mag = Math.hypot(dx, dy);                     // and normalize it.
	let [vx, vy] = [(dx / mag), (dy / mag)];
	return [x, y, vx, vy];
}