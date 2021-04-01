from flask_login import UserMixin
from . import db


class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    name = db.Column(db.String(1000))
    user_type = db.Column(db.String(100)) #"customer", "business", "expert"


class businesses(db.Model):
	business_id = db.Column(db.Integer,db.ForeignKey('user.id'), primary_key=True)
	#business_name = db.Column(db.String(100))
	preferred_age_min = db.Column(db.Integer, nullable=True)
	preferred_age_max = db.Column(db.Integer, nullable=True)
	preferred_gender = db.Column(db.String(100), nullable=True)
	preferred_occupation = db.Column(db.String(100), nullable=True) 


class customers(db.Model):
	customer_id = db.Column(db.Integer,db.ForeignKey('user.id'), primary_key=True)
	age = db.Column(db.Integer, nullable=True)	
	gender = db.Column(db.String(100), nullable=True)
	occupation = db.Column(db.String(100), nullable=True)


class experts(db.Model):
	expert_id = db.Column(db.Integer,db.ForeignKey('user.id'), primary_key=True)


class coupon_list(db.Model):
	coupon_id = db.Column(db.Integer, primary_key=True)
	business_id = db.Column(db.Integer,db.ForeignKey('user.id'))
	coupon_desc = db.Column(db.String(1000), nullable=True)
	#exp_date


class customer_movie_preferences(db.Model):
	customer_id = db.Column(db.Integer,db.ForeignKey('user.id'), primary_key=True)
	movie_genre = db.Column(db.String(100), nullable=True)
	movie_length = db.Column(db.Integer, nullable=True)	#In minutes
	release_year = db.Column(db.Integer, nullable=True) 
	movie_rating = db.Column(db.Float, nullable=True) #Scored from 0 to 5


class business_movie_item(db.Model):
	movie_id = db.Column(db.Integer, primary_key=True)
	business_id = db.Column(db.Integer,db.ForeignKey('user.id'))
	movie_name = db.Column(db.String(100))
	movie_genre = db.Column(db.String(100), nullable=True)
	movie_length = db.Column(db.Integer, nullable=True)	#In minutes
	release_year = db.Column(db.Integer, nullable=True) 
	movie_rating = db.Column(db.Float, nullable=True)	#Scored from 0 to 5


class recommendation_list(db.Model):
	recommendation_id = db.Column(db.Integer, primary_key=True)
	business_id = db.Column(db.Integer,db.ForeignKey('user.id'))
	customer_id = db.Column(db.Integer,db.ForeignKey('user.id'))
	coupon_id = db.Column(db.Integer,db.ForeignKey('coupon_list.coupon_id'))
	customer_score = db.Column(db.Integer, nullable=True) #Score 0-5
	business_score = db.Column(db.Integer, nullable=True) #Score 0-5
	expert_score = db.Column(db.Integer, nullable=True)  #Score 0-5
