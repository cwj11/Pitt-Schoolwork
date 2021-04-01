from flask import Blueprint, render_template, redirect, url_for, request, flash
from werkzeug.security import generate_password_hash, check_password_hash
from . import db
from flask_login import login_user, logout_user, login_required, current_user
from .models import *
import sys
import sqlite3
import os
from pathlib import Path

auth = Blueprint('auth', __name__)


@auth.route('/login')
def login():
	return render_template('login.html')

@auth.route('/login', methods=['POST'])
def login_post():
	email = request.form.get('email')
	password = request.form.get('password')
	remember = True if request.form.get('remember') else False

	user = User.query.filter_by(email=email).first()

	# check if the user actually exists
	# take the user-supplied password, hash it, and compare it to the hashed password in the database
	if not user or not check_password_hash(user.password, password):
		flash('Please check your login details and try again.')
		return redirect(url_for('auth.login')) # if the user doesn't exist or password is wrong, reload the page

	# if the above check passes, then we know the user has the right credentials
	login_user(user, remember=remember)


	if(user.user_type=='customer'):
		return redirect(url_for('main.customer'))

	elif(user.user_type=='business'):
		return redirect(url_for('main.business'))


	return redirect(url_for('main.profile'))

@auth.route('/signup')
def signup():
	return render_template('signup.html')

@auth.route('/signup', methods=['POST'])
def signup_post():
	email = request.form.get('email')
	name = request.form.get('name')
	password = request.form.get('password')
	user_type = request.form.get('user_type')

	user = User.query.filter_by(email=email).first() # if this returns a user, then the email already exists in database

	if user: # if a user is found, we want to redirect back to signup page so user can try again
		flash('Email address already exists')
		return redirect(url_for('auth.signup'))

	# create a new user with the form data. Hash the password so the plaintext version isn't saved.
	new_user = User(email=email, name=name, password=generate_password_hash(password, method='sha256'), user_type=user_type)

	# add the new user to the database
	db.session.add(new_user)
	db.session.commit()

	return redirect(url_for('auth.login'))

@auth.route('/logout')
@login_required
def logout():
	logout_user()
	return redirect(url_for('main.index'))

@auth.route('/customer_info')
def customer_info():
    return render_template('customer_info.html')

@auth.route('/customer_info', methods=['POST'])
def customer_info_post():
    age = request.form.get('age')
    gender = request.form.get('gender')
    occupation = request.form.get('occupation')
    curr_id = current_user.id

    user_info = customers(customer_id=curr_id, age=age, gender=gender, occupation=occupation)
    db.session.add(user_info)
    db.session.commit()

    return redirect(url_for('main.customer'))

@auth.route('/customer_preference')
def customer_preference():
    return render_template('customer_preference.html')

@auth.route('/customer_preference', methods=['POST'])
def customer_preference_post():
    genre = request.form.get('genre')
    length = request.form.get('length')
    year = request.form.get('year')
    curr_id = current_user.id

    customer_info = customer_movie_preferences(customer_id=curr_id, movie_genre=genre, movie_length=length, release_year=year)
    db.session.add(customer_info)
    db.session.commit()

    return redirect(url_for('main.customer'))

@auth.route('/business_info')
def business_info():
	return render_template('business_info.html')

@auth.route('/business_info', methods=['POST'])
def business_info_post():
	preferred_age_min = request.form.get('ageMin')
	preferred_age_max = request.form.get('ageMax')
	preferred_gender = request.form.get('gender')
	preferred_occupation = request.form.get('occupation')

	curr_id = current_user.id

	business_info = businesses(business_id=curr_id, preferred_age_min=preferred_age_min, preferred_age_max=preferred_age_max, preferred_gender=preferred_gender, preferred_occupation=preferred_occupation)
	db.session.add(business_info)
	db.session.commit()

	return redirect(url_for('main.business'))

@auth.route('/business_movies')
def business_movies():
	return render_template('business_movies.html')

@auth.route('/business_movies', methods=['POST'])
def business_movies_post():
	name = request.form.get('name')
	genre = request.form.get('genre')
	length = request.form.get('length')
	year = request.form.get('year')

	curr_id = current_user.id

	business_info = business_movie_item(business_id=curr_id, movie_name=name, movie_genre=genre, movie_length=length, release_year=year)
	db.session.add(business_info)
	db.session.commit()

	return redirect(url_for('main.business'))

@auth.route('/profile')
def profile():

	curr_id = current_user.id

	user = User.query.filter_by(id=curr_id).first()

	if(user.user_type=='customer'):
		return redirect(url_for('main.customer'))

	elif(user.user_type=='business'):
		return redirect(url_for('main.business'))

	return redirect(url_for('main.profile'))

@auth.route('/customer_recommendation')
def customer_recommendation():

	p = Path(__file__).with_name('db.sqlite')

	con = sqlite3.connect(p.as_posix())
	cur = con.cursor()
	curr_id = current_user.id


	cur.execute("""
		SELECT recommendation_list.recommendation_id, recommendation_list.coupon_id, coupon_list.coupon_desc, recommendation_list.customer_id, recommendation_list.customer_score
		FROM recommendation_list LEFT JOIN coupon_list ON recommendation_list.coupon_id = coupon_list.coupon_id
		WHERE customer_id = """ + str(curr_id))

	recommendation_details = cur.fetchall()
	return render_template('customer_recommendation.html', data=recommendation_details)

@auth.route('/customer_recommendation', methods=['POST'])
def customer_recommendation_post():
	recommendation_id = request.form.get('recommendation_id')
	change_rating = request.form.get('change_rating')

	find_recommendation = recommendation_list.query.filter_by(recommendation_id=recommendation_id).first()
	find_recommendation.customer_score = change_rating
	db.session.commit()

	return redirect(url_for('main.customer'))


@auth.route('/business_recommendation')
def business_recommendation():

	p = Path(__file__).with_name('db.sqlite')

	con = sqlite3.connect(p.as_posix())
	cur = con.cursor()
	curr_id = current_user.id
	cur.execute("""
		SELECT recommendation_list.recommendation_id, recommendation_list.coupon_id, coupon_list.coupon_desc, recommendation_list.customer_id, recommendation_list.business_id, recommendation_list.business_score, customers.age, customers.gender, customers.occupation, User.name
		FROM recommendation_list
		LEFT JOIN customers ON recommendation_list.customer_id = customers.customer_id
		LEFT JOIN coupon_list ON recommendation_list.coupon_id = coupon_list.coupon_id
		LEFT JOIN User ON recommendation_list.customer_id = User.id
		WHERE recommendation_list.business_id =
		""" + str(curr_id))

	recommendation_details = cur.fetchall()
	return render_template('business_recommendation.html', data=recommendation_details)

@auth.route('/business_recommendation', methods=['POST'])
def business_recommendation_post():
	recommendation_id = request.form.get('recommendation_id')
	change_rating = request.form.get('change_rating')

	find_recommendation = recommendation_list.query.filter_by(recommendation_id=recommendation_id).first()
	find_recommendation.business_score = change_rating

	db.session.commit()

	return redirect(url_for('main.business'))
