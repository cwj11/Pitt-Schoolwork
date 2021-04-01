from flask import Blueprint, render_template
from . import db
from flask_login import login_required, current_user


main = Blueprint('main', __name__)

@main.route('/')
def index():
    return render_template('index.html')

@main.route('/profile')
@login_required
def profile():
    return render_template('profile.html', name=current_user.name)

@main.route('/customer_info')
@login_required
def customer_info():
    return render_template('customer_info.html')

@main.route('/business_info')
@login_required
def business_info():
    return render_template('business_info.html')

@main.route('/customer')
@login_required
def customer():
    return render_template('customer.html')

@main.route('/business')
@login_required
def business():
    return render_template('business.html')
