from flask import Flask, request, session, url_for, redirect, render_template, abort, g, flash
from app import *
from models import db, User, Book

#########################################################################################
# Utilities
#########################################################################################

# Given a username, gives
def get_user_id(username):
	rv = User.query.filter_by(username=username).first()
	return rv.user_id if rv else None

# This decorator will cause this function to run at the beginning of each request,
# before any of the route functions run. We're using this to check if the user is
# logged in, so that we don't have to do that on every page.
@app.before_request
def before_request():
	# 'g' is a general-purpose global variable thing that Flask gives you.
	# it's a "magic global" like session, request etc. so it's useful
	# for storing globals that you only want to exist for one request.
	g.user = None
	g.userType = "visitor"
	if 'user_id' in session:
		g.user = User.query.filter_by(user_id=session['user_id']).first()

		if g.user:
			g.userType = "librarian" if g.user.librarian else "patron"

#########################################################################################
# User account management page routes
#########################################################################################

# This stuff is taken pretty much directly from the "minitwit" example.
# It's pretty standard stuff, so... I'm not gonna make you reimplement it.

@app.route('/login', methods=['GET', 'POST'])
def login():
	"""Logs the user in."""
	if g.user:
		return redirect(url_for('home'))
	error = None
	if request.method == 'POST':

		user = User.query.filter_by(username=request.form['username']).first()
		if user is None:
			error = 'Invalid username'
		elif user.password != request.form['password']:
			error = 'Invalid password'
		else:
			flash('You were logged in. Hi, {}!'.format(user.username))
			session['user_id'] = user.user_id
			return redirect(url_for('home'))

	return render_template('login.html', error=error)

def checkValidUserForm():
	error = None

	if not request.form['username']:
		error = 'You have to enter a username'
	elif not request.form['email'] or '@' not in request.form['email']:
		error = 'You have to enter a valid email address'
	elif not request.form['password']:
		error = 'You have to enter a password'
	elif request.form['password'] != request.form['password2']:
		error = 'The two passwords do not match'
	elif get_user_id(request.form['username']) is not None:
		error = 'The username is already taken'

	return error

def addUserFromForm(librarian):
	db.session.add(User(
		username = request.form['username'],
		email = request.form['email'],
		password = request.form['password'],
		librarian = librarian))
	db.session.commit()

@app.route('/register', methods=['GET', 'POST'])
def register():
	"""Registers the user."""
	if g.user:
		return redirect(url_for('home'))

	error = None
	if request.method == 'POST':
		error = checkValidUserForm()
		if not error:
			addUserFromForm(librarian = False)
			flash('You were successfully registered! Please log in.')
			return redirect(url_for('login'))

	return render_template('register.html', error=error)

@app.route('/logout')
def logout():
	"""Logs the user out."""
	flash('You were logged out. Thanks!')
	session.pop('user_id', None)
	return redirect(url_for('home'))

#########################################################################################
# Other page routes
#########################################################################################

def allBooks():
	return Book.query.order_by(Book.title).all()

def abortIfNotLoggedIn(code):
	if not g.user:
		abort(code)

def getBookFromID(id):
	if id:
		return Book.query.filter_by(book_id = id).first()
	return None

def borrowOrReturnBook(book):
	if book in g.user.borrows:
		g.user.borrows.remove(book)
		flash("Thank you for returning '{}'!".format(book.title))
	else:
		g.user.borrows.append(book)
		flash("Enjoy your book!")

	db.session.commit()
	return redirect(url_for('home'))

# The home page shows a listing of books.
@app.route('/', methods=['GET', 'POST'])
def home():
	if request.method == 'POST':
		abortIfNotLoggedIn(401)
		book = getBookFromID(request.form.get('book_id'))

		if book:
			return borrowOrReturnBook(book)

	return render_template('home.html', books = allBooks())

def bookFromID(id):
	return Book.query.filter_by(book_id = id).first()

def haveBook(title):
	return Book.query.filter_by(title = title).first() is not None

def bookManagementPage():
	error = None

	if request.method == 'POST':
		title = request.form['title']
		author = request.form['author']

		if not title:
			error = "Please enter a title."
		elif not author:
			error = "Please enter an author."
		elif haveBook(title):
			error = "That book already exists."
		else:
			db.session.add(Book(title = title, author = author))
			db.session.commit()
			flash("Added book '{}'!".format(title))
			return redirect(url_for('books'))

	return render_template('newbook.html', error = error)

def bookDetailPage(book_id):
	book = bookFromID(book_id)
	if not book: abort(404)
	borrowers = book.borrowed_by.all()
	error = None

	if request.method == 'POST':
		if len(borrowers) > 0:
			error = "Hey, you can't delete that book! People are borrowing it."
		else:
			title = book.title
			db.session.delete(book)
			db.session.commit()
			flash("Deleted book '{}'!".format(title))
			return redirect(url_for('home'))

	return render_template('bookdetails.html', error = error, book = book, borrowers = borrowers)

@app.route('/books/', methods = ['GET', 'POST'])
@app.route('/books/<book_id>', methods = ['GET', 'POST'])
def books(book_id = None):
	if g.userType != 'librarian':
		return redirect(url_for('home'))

	if book_id is None:
		return bookManagementPage()
	else:
		return bookDetailPage(book_id)

def userFromID(id):
	return User.query.filter_by(user_id = id).first()

def haveUser(username):
	return User.query.filter_by(username = username).first() is not None

def userManagementPage():
	patrons    = User.query.filter_by(librarian = False).order_by(User.username).all()
	librarians = User.query.filter_by(librarian = True).order_by(User.username).all()
	error      = None

	if request.method == 'POST':
		error = checkValidUserForm()
		if not error:
			addUserFromForm(librarian = True)
			flash("Successfully created librarian '{}'.".format(request.form['username']))
			return redirect(url_for('accounts'))

	return render_template('users.html',
		error = error, patrons = patrons, librarians = librarians)

def userDetailPage(user_id):
	user = userFromID(user_id)
	if not user: abort(404)
	error = None

	if request.method == 'POST':
		if user.username == 'owner':
			error = "Hey, you can't delete the owner! How rude."
		else:
			username = user.username
			db.session.delete(user)
			db.session.commit()
			flash("Deleted user '{}'!".format(username))
			return redirect(url_for('accounts'))

	books = user.borrows.order_by(Book.title).all()
	return render_template('userdetails.html', error = error, user = user, books = books)

@app.route('/accounts/', methods=['GET', 'POST'])
@app.route('/accounts/<user_id>', methods=['GET', 'POST'])
def accounts(user_id = None):
	if g.userType != 'librarian':
		return redirect(url_for('home'))

	if user_id is None:
		return userManagementPage()
	else:
		return userDetailPage(user_id)
