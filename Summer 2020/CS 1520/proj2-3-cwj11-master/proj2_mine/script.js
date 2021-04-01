"use strict";

// ------------------------------------------------------------------------------------------------
// Helpers
// ------------------------------------------------------------------------------------------------

Array.prototype.removeItem = function(it) {
	let i = this.indexOf(it)

	if(i > -1) {
		this.splice(i, 1)
		return true
	}

	return false
}

function getRandomInt(min, max) {
	min = Math.ceil(min)
	max = Math.floor(max)
	return Math.floor(Math.random() * (max - min)) + min
}

// ------------------------------------------------------------------------------------------------
// Constants
// ------------------------------------------------------------------------------------------------

const IMG_W = 120
const IMG_H = 88
const SCREEN_W = 640
const SCREEN_H = 480
const MAX_SCORES = 5
const SCORE_MOSQUITO = 100
const SCORE_ROUND_BONUS = 1000
const SCORE_MISS_PENALTY = 250

// ------------------------------------------------------------------------------------------------
// Globals
// ------------------------------------------------------------------------------------------------

const GameState = {
	round: 1,
	remaining: 10,
	misses: 0,
	score: 0,
	mosquitoes: [],
}

let roundDisplay,
	mosquitoDisplay,
	missesDisplay,
	scoreDisplay,
	playfield,
	gameMessage,
	highScores;

// ------------------------------------------------------------------------------------------------
// DOM interaction
// ------------------------------------------------------------------------------------------------

function grabElements() {
	roundDisplay    = document.getElementById('roundDisplay');
	mosquitoDisplay = document.getElementById('mosquitoDisplay');
	missesDisplay   = document.getElementById('missesDisplay');
	scoreDisplay    = document.getElementById('scoreDisplay');
	playfield       = document.getElementById('playfield');
	gameMessage     = document.getElementById('gameMessage');
	highScores      = document.getElementById('highScores');
}

function updateDisplay() {
	roundDisplay.innerHTML = GameState.round.toString()
	mosquitoDisplay.innerHTML = GameState.remaining.toString()
	missesDisplay.innerHTML = GameState.misses.toString()
	scoreDisplay.innerHTML = GameState.score.toString()
}

function showHighScores() {
	let scores = getHighScores()

	highScores.innerHTML = ''

	for(let i = 0; i < MAX_SCORES; i++) {
		let el = document.createElement('li')

		if(i < scores.length)
			el.innerHTML = scores[i].toString()
		else
			el.innerHTML = '---'

		highScores.appendChild(el)
	}
}

function showMessage(text) {
	gameMessage.innerHTML = text
	gameMessage.style.display = 'flex'
}

function hideMessage() {
	gameMessage.style.display = 'none'
}

// ------------------------------------------------------------------------------------------------
// Mosquito object
// ------------------------------------------------------------------------------------------------

function Mosquito(x, y, vx, vy) {
	this.x = x
	this.y = y
	this.vx = vx
	this.vy = vy

	this.el = document.createElement('img')
	this.el.owner = this
	this.el.src = "mosquito.png"
	this.el.style.position = "absolute"
	this.el.onmousedown = clickMosquito
	playfield.appendChild(this.el)
	this.updateElement()
}

function clickMosquito(e) {
	if(e.button == 0) {
		e.target.owner.killed()
	}

	e.stopPropagation = true
}

Mosquito.prototype.updateElement = function() {
	this.el.style.left = this.x + "px"
	this.el.style.top = this.y + "px"
}

Mosquito.prototype.updatePosition = function() {
	this.x += this.vx
	this.y += this.vy
	this.updateElement()
	this.checkOutOfBounds()
}

Mosquito.prototype.checkOutOfBounds = function() {
	if(this.x < -IMG_W	|| this.x > SCREEN_W + 5 || this.y < -IMG_H || this.y > SCREEN_H + 5) {
		this.escaped()
	}
}

Mosquito.prototype.escaped = function() {
	if(GameState.misses < 5) {
		GameState.misses++
	}
	this.destroy()
}

Mosquito.prototype.killed = function() {
	if(GameState.remaining > 0) {
		GameState.remaining--
	}
	GameState.score += SCORE_MOSQUITO
	this.destroy()
}

Mosquito.prototype.destroy = function() {
	GameState.mosquitoes.removeItem(this)
	this.el.parentNode.removeChild(this.el)
}

// ------------------------------------------------------------------------------------------------
// Mosquito spawning
// ------------------------------------------------------------------------------------------------

function randomPointOnSide(side) {
	switch(side) {
		/* top    */ case 0: return [getRandomInt(0, SCREEN_W - IMG_W), -IMG_H]
		/* bottom */ case 2: return [getRandomInt(0, SCREEN_W - IMG_W), SCREEN_H]
		/* right  */ case 1: return [SCREEN_W, getRandomInt(0, SCREEN_H - IMG_H)]
		/* left   */ case 3: return [-IMG_W, getRandomInt(0, SCREEN_H - IMG_H)]
	}
}

function pickPointAndVector() {
	let side = getRandomInt(0, 4)
	let [x, y] = randomPointOnSide(side)
	let [tx, ty] = randomPointOnSide((side + 2) % 4)
	let [dx, dy] = [tx - x, ty - y]
	let mag = Math.hypot(dx, dy)
	let [vx, vy] = [(dx / mag), (dy / mag)]
	return [x, y, vx, vy]
}

function spawnMosquito() {
	let [x, y, vx, vy] = pickPointAndVector()
	GameState.mosquitoes.push(new Mosquito(x, y, vx * 3, vy * 3))
}

function spawner() {
	spawnMosquito()

	if(GameState.running) {
		window.setTimeout(spawner, 1000)
	}
}

// ------------------------------------------------------------------------------------------------
// High scores
// ------------------------------------------------------------------------------------------------

function getHighScores() {
	let ret = window.localStorage.getItem('highscores')

	if(ret === null) {
		ret = [1000]
		setHighScores(ret)
	} else {
		ret = JSON.parse(ret)
	}

	return ret
}

function setHighScores(scores) {
	window.localStorage.setItem('highscores', JSON.stringify(scores))
}

function checkHighScore(newScore) {
	var scores = getHighScores()
	scores.push(newScore)
	scores.sort((a, b) => b - a)

	if(scores.length > MAX_SCORES)
		scores.length = MAX_SCORES

	setHighScores(scores)
	showHighScores()
}

// ------------------------------------------------------------------------------------------------
// Game flow
// ------------------------------------------------------------------------------------------------

function startGame() {
	GameState.running = true
	hideMessage()
	window.setTimeout(spawner, 1000)
	updateDisplay()
	requestAnimationFrame(updateGame)
}

function stopGame() {
	GameState.running = false
	clearMosquitoes()
}

function setupRound(n) {
	GameState.round = n
	GameState.remaining = 10
	GameState.misses = 0
}

function gameOver() {
	stopGame()
	checkHighScore(GameState.score)
	GameState.score = 0
	setupRound(1)
	showMessage("GAME OVER! Click to try again.")
}

function clearMosquitoes() {
	while(GameState.mosquitoes.length) {
		GameState.mosquitoes[0].destroy()
	}
}

function nextRound() {
	GameState.score += (SCORE_ROUND_BONUS - (GameState.misses * SCORE_MISS_PENALTY))
	stopGame()
	setupRound(GameState.round + 1)
	showMessage("Round " + GameState.round + "! Click to start.")
}

function updateGame() {
	for(let m of GameState.mosquitoes) {
		m.updatePosition()
	}

	if(GameState.misses == 5) {
		gameOver()
	} else if(GameState.remaining == 0) {
		nextRound()
	}

	updateDisplay()

	if(GameState.running) {
		requestAnimationFrame(updateGame)
	}
}

window.onload = function() {
	grabElements()
	showHighScores()

	playfield.onclick = function() {
		if(!GameState.running) {
			startGame()
		}
	};
};